package com.jam.neural.controller;

import java.util.Random;

import com.jam.game.controller.Game;
import com.jam.game.controller.Game.DefaultGamePreference;
import com.jam.game.model.Board;
import com.jam.game.model.Board.UncoverResult;
import com.jam.game.model.Coord;
import com.jam.game.model.Field;
import com.jam.neural.model.NeuralTask;

public class NeuralMinesweeper implements NeuralTask{
	private Game game;
	private final int visibleSquareSize;
	private Coord pos;
	private Coord spawnPos;
	private Coord[] visibleCoords;
	private TaskState state;
	private final int fieldsToUncoverInitial;
	private long seed;
	
	private int fitness;
	private static final int FITNESS_CLICK_INCREMENT = 2;
	private static final int FITNESS_VICTORY_BONUS = 30;
	
	public NeuralMinesweeper(int visibleSquareSize, int xSpawn, int ySpawn, Long seed, DefaultGamePreference pref){
		this.game = new Game(pref, seed, false);
		this.visibleSquareSize = visibleSquareSize;
		this.spawnPos = new Coord(xSpawn, ySpawn);
		this.seed = seed;
		this.fieldsToUncoverInitial = game.getSize() * game.getSize() - game.getBombsInitial();
		
		reset();
	}

	@Override
	public void reset() {
		seed = new Random(seed).nextLong();
		game.resetGame(seed);
		state = TaskState.PROCESSING;
		pos = new Coord(spawnPos);
		visibleCoords = new Coord[visibleSquareSize * visibleSquareSize];
		fitness = 1;
		
		//Click on the spawn coords. This is an arbitrary action and it doesnt interfere with the neural network
		game.leftClickField(pos);
	}
	
	/**
	 * Translates the fields within the visible square into their corresponding float values
	 * Outside = -1;
	 * Empty = 0
	 * 1 = 0.125
	 * 2 = 0.25
	 * 3 = 0.375
	 * 4 = 0.5
	 * 5 = 0.625
	 * 6 = 0.75
	 * 7 = 0.875
	 * 8 = 1
	 * Covered = 2
	 * This is gonna fail spectacularly 
	 * @return Field translated into a float value
	 */
	private float translateField(Field field){
		if (field == null) return -1;
		switch (field){
			case COVERED_EMPTY:
			case COVERED_MINE:
				return 2;
			case EMPTY:
				return 0;
			case ONE:
				return 0.125f;
			case TWO:
				return 0.25f;
			case THREE:
				return 0.375f;
			case FOUR:
				return 0.5f;
			case FIVE:
				return 0.625f;
			case SIX:
				return 0.75f;
			case SEVEN:
				return 0.875f;
			case EIGHT:
				return 1;
			default:
				//Should never happen
				return -1;
		}
	}
	
	private Field[] getSectorFields(Coord coord, Board board, int sectorSize){
		if (sectorSize % 2 == 0) throw new IllegalArgumentException("The sector size must be odd");
		
		Field[] coords = new Field[sectorSize * sectorSize];
		int index = 0;
		int maxOffset = sectorSize / 2;
		
		Coord c = new Coord();
		for (int dx = -maxOffset; dx <= maxOffset; dx++){
			for (int dy = -maxOffset; dy <= maxOffset; dy++){
				c.set(coord.getX() + dx, coord.getY() + dy);
				visibleCoords[index] = new Coord(c);
				if (board.isWithinBoard(c)){
					coords[index++] = board.getField(c);
				} else {
					coords[index++] = null;
				}
			}
		}
		
		return coords;
	}

	@Override
	public float[] getInputs() {
		Field[] fields = getSectorFields(pos, game.getBoard(), visibleSquareSize);
		
		float[] inputs = new float[fields.length];
		for (int i = 0; i < inputs.length; i++) {
			inputs[i] = translateField(fields[i]);
		}
		return inputs;
	}
	
	private Coord[] getCoveredFields(){
		Coord[] res = new Coord[game.getBoard().getLeftToUncover() + game.getBombsInitial()];
		
		int index = 0;
		for (int i = 0; i < game.getSize() * game.getSize(); i++) {
			Coord c = Coord.getCoord(i, game.getSize());
			if (!game.getBoard().isUncovered(c)){
				res[index++] = c;
			}
		}
		
		return res;
	}

	@Override
	public void setOutputs(float[] outputs) {
		for (int i = 0; i < outputs.length - 1; i++) {
			if (outputs[i] == 1 && game.getBoard().isWithinBoard(visibleCoords[i])){
				UncoverResult res = game.leftClickField(visibleCoords[i]);
				if (res == UncoverResult.MINE){
					state = TaskState.FAILED;
					return;
				} else if (res == UncoverResult.VICTORY){
					state = TaskState.SUCCEEDED;
					fitness += FITNESS_VICTORY_BONUS;
					return;
				} else if (res == UncoverResult.SUCCESS){
					fitness += FITNESS_CLICK_INCREMENT;
				}
			}
		}
		if (outputs[outputs.length - 1] == 1){
			Random r  = new Random();
			Coord[] coords = getCoveredFields();
			pos = coords[r.nextInt(coords.length)];
		}
	}

	@Override
	public int getNumOutputs() {
		return getNumInputs() + 1;
	}

	@Override
	public int getNumInputs() {
		return visibleSquareSize * visibleSquareSize;
	}

	@Override
	public int getFitness() {
		//TODO: Make this much more sensitive to fields that aren't uncovered recursively
		//Right now, the genomes that actually do succeed with a click have pretty much the same chance of getting
		//offspring as the retarded ones.
		//return fieldsToUncoverInitial - game.getBoard().getLeftToUncover();
		
		return fitness;
	}

	@Override
	public TaskState getTaskState() {
		return state;
	}

	@Override
	public boolean isBinary() {
		return true;
	}

	@Override
	public void setTaskState(TaskState state) {
		this.state = state;
	}
	
	public Game getGame(){
		return game;
	}
}

package com.jam.main;

import java.util.Random;

import com.jam.game.controller.DefaultGamePreference;
import com.jam.game.controller.Game;
import com.jam.game.model.Board;
import com.jam.game.model.Coord;
import com.jam.game.model.Field;
import com.jam.game.model.UncoverResult;
import com.jam.neural.NeuralTask;
import com.jam.neural.TaskState;

public class NeuralMinesweeper implements NeuralTask{
	private Game game;
	private final int visibleSquareSize;
	private Coord pos;
	private Coord spawnPos;
	private Coord[] visibleCoords;
	private TaskState state;
	private final int fieldsToUncoverInitial;
	
	public NeuralMinesweeper(int visibleSquareSize, int xSpawn, int ySpawn, DefaultGamePreference pref){
		this.game = new Game(pref, false);
		this.visibleSquareSize = visibleSquareSize;
		this.spawnPos = new Coord(xSpawn, ySpawn);
		this.fieldsToUncoverInitial = game.getSize() * game.getSize() - game.getBombsInitial();
		
		reset();
	}

	@Override
	public void reset() {
		game.resetGame();
		state = TaskState.PROCESSING;
		pos = new Coord(spawnPos);
		visibleCoords = new Coord[visibleSquareSize * visibleSquareSize];
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
				}
				if (res == UncoverResult.VICTORY){
					state = TaskState.SUCCEEDED;
					return;
				}
			}
		}
		if (outputs[outputs.length - 1] == 1){
			Random r  = new Random();
			Coord[] coords = getCoveredFields();
			pos = coords[r.nextInt(coords.length)];
			if (pos == null){
				System.out.println("OMOMGOMGOMGOGM");
			}
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
		return fieldsToUncoverInitial - game.getBoard().getLeftToUncover();
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

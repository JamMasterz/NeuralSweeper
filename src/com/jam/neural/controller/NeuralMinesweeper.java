package com.jam.neural.controller;

import com.jam.minesweeper.controller.MinesweeperController;
import com.jam.minesweeper.model.Board;
import com.jam.minesweeper.model.Board.UncoverResult;
import com.jam.minesweeper.model.Coord;
import com.jam.minesweeper.model.Field;
import com.jam.neural.model.NeuralTask;

import java.util.Arrays;
import java.util.Random;

public class NeuralMinesweeper implements NeuralTask {
	private MinesweeperController game;
	private TaskState state;
	private long seed;

	private Coord nextInterestingField;

	private int fitness;
	private static final int VISIBLE_SQUARE_SIZE = 5;
	private static final int FITNESS_CLICK_INCREMENT = 2;
	private static final int FITNESS_VICTORY_BONUS = 30;
	private static final float CLICK_THRESHOLD = 0.85f;
	private static final int CLICKING_INCENTIVE = 5;
	
	public NeuralMinesweeper(Long seed, MinesweeperController game){
		this.game = game;
		this.seed = seed;

		reset();
	}

	@Override
	public void reset() {
		seed = new Random(seed).nextLong();
		game.resetGame(seed);
		state = TaskState.PROCESSING;
		fitness = 1;
		
		//Click on the spawn coords. This is an arbitrary action and it doesnt interfere with the neural network
		game.leftClickField(new Coord(4, 5));

		nextInterestingField = getNextInterestingField();
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
				System.err.println("Unknown case!!!");
				return -1;
		}
	}
	
	private Field[] getSectorFields(Coord coord, Board board, int sectorSize){
		if (sectorSize % 2 == 0) throw new IllegalArgumentException("The sector size must be odd");
		
		Field[] coords = new Field[sectorSize * sectorSize - 1]; //-1 to skip self
		int index = 0;
		int maxOffset = sectorSize / 2;
		
		Coord c = new Coord();
		for (int dy = -maxOffset; dy <= maxOffset; dy++){
			for (int dx = -maxOffset; dx <= maxOffset; dx++){
				c.set(coord.getX() + dx, coord.getY() + dy);
				if (coord.equals(c)) continue; //Skip middle
				if (board.isWithinBoard(c)){
					coords[index++] = board.getField(c);
				} else {
					coords[index++] = null;
				}
			}
		}
		
		return coords;
	}

	private Coord getNextInterestingField() {
		if (nextInterestingField == null) {
			nextInterestingField = new Coord(0, 0);
		} else {
			//Set to the next one. Dont worry about outside of board, loops can handle it
			nextInterestingField.add(1, 0);
		}

		return nextInterestingField = findNextCoveredField(nextInterestingField, game.getSize());
	}

	private Coord findNextCoveredField(Coord start, int boardSize) {
		Coord c = new Coord();
		for (int i = start.getY(); i < boardSize; i++) {
			for (int j = start.getX(); j < boardSize; j++) {
				c.set(j, i);
				if (!game.getBoard().isUncovered(c) && hasAnyAdjacentUncoveredField(c)) {
					return c;
				}
			}
		}

		//Happens when we got through all and didnt do anything
		return null;
	}

	private boolean hasAnyAdjacentUncoveredField(Coord coord) {
		Coord c = new Coord();
		for (int dy = -1; dy <= 1; dy++){
			for (int dx = -1; dx <= 1; dx++){
				c.set(coord.getX() + dx, coord.getY() + dy);
				if (coord.equals(c)) continue; //Skip middle
				if (game.getBoard().isWithinBoard(c) && game.getBoard().isUncovered(c)) {
					return true;
				}
			}
		}

		return false;
	}

	@Override
	public float[] getInputs() {
		Field[] interestingFieldSurroundings = getSectorFields(nextInterestingField, game.getBoard(), VISIBLE_SQUARE_SIZE);
		float[] inputs = new float[interestingFieldSurroundings.length];
		for (int i = 0; i < inputs.length; i++) {
			inputs[i] = translateField(interestingFieldSurroundings[i]);
		}

//		System.out.println(Arrays.toString(inputs));
		return inputs;
	}
	
	@Override
	public void setOutputs(float[] outputs) {
		if (outputs[0] > CLICK_THRESHOLD) {
//			System.out.println("Im clicking!");
			UncoverResult res = game.leftClickField(nextInterestingField);
			if (res == UncoverResult.MINE){
				state = TaskState.FAILED;
				if (fitness == 1) fitness += CLICKING_INCENTIVE; //If dies by first click, it still better than non-clickers
				return;
			} else if (res == UncoverResult.VICTORY){
				state = TaskState.SUCCEEDED;
				fitness += FITNESS_VICTORY_BONUS;
				System.out.println("Winner winner chicken dinner");
				return;
			} else if (res == UncoverResult.SUCCESS){
				fitness += FITNESS_CLICK_INCREMENT;
				//Reset nextInterestingField because the board has changed
				nextInterestingField = null;
			}
		}

		nextInterestingField = getNextInterestingField();
		if (nextInterestingField == null) {
			state = TaskState.FAILED; //We ran out of things to click and did nothing
		}
	}

	@Override
	public int getNumOutputs() {
		return 1; //To click or not to click, that is the question
	}

	@Override
	public int getNumInputs() {
		return VISIBLE_SQUARE_SIZE * VISIBLE_SQUARE_SIZE - 1; //-1 for excluding self
	}

	@Override
	public int getFitness() {
		return fitness;
	}

	@Override
	public TaskState getTaskState() {
		return state;
	}

	@Override
	public boolean isBinary() {
		return false;
	}

	@Override
	public void updateAll() {
		game.getBoard().update();
	}

	@Override
	public void setTaskState(TaskState state) {
		this.state = state;
	}
	
	public MinesweeperController getGame(){
		return game;
	}
}

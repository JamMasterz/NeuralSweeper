package com.jam.neural.controller;

import com.jam.runner.controller.RunnerController;
import com.jam.runner.model.Player;
import com.jam.neural.model.NeuralTask;

import java.util.Observer;
import java.util.Random;

public class NeuralRunner implements NeuralTask {
	private RunnerController game;
	private int playerNumber;

	private TaskState state;
	private long seed;

	private int failedMovesInARow;

	private static final int FITNESS_LOSS_PENALTY = -1;
	private static final float OUTPUT_THRESHOLD = 0.85f;

	public NeuralRunner(RunnerController game, int playerNumber, Long seed){
		this.game = game;
		this.playerNumber = playerNumber;
		this.seed = seed;

		reset();
	}

	@Override
	public void reset() {
		seed = new Random(seed).nextLong();
		game.resetGame(seed);
		state = TaskState.PROCESSING;
	}
	
	@Override
	public float[] getInputs() {
		Player player = game.getBoard().getPlayer(this.playerNumber);
		int width = game.getBoard().getWidth(), height = game.getBoard().getHeight();

		float mappedX = map(player.getX(), 0, width, 0, 1);
		float mappedY = map(player.getY(), 0, height, 0, 1);

		float closeToWall = game.getBoard().isCloseToAnyWall(player.getX(), player.getY()) ? 1.0f : 0.0f;

		float proximityToTopDeath = map(Math.min(player.getY(), height / 19), 0, height / 20, 1, 0);
		float proximityToLowDeath = map(Math.min(height - player.getY(), height / 19), 0, height / 20, 1, 0);

//		return new float[]{mappedX, mappedY, closeToWall};
		return new float[]{closeToWall, proximityToTopDeath, proximityToLowDeath};
	}

	private float map(float num, long inMin, long inMax, long outMin, long outMax) {
		return (num - inMin) * (outMax - outMin) / (inMax - inMin) + outMin;
	}
	
	@Override
	public void setOutputs(float[] outputs) {
		boolean moveSuccess = false;

		if (outputs[0] > OUTPUT_THRESHOLD) {
			moveSuccess |= this.game.moveX(playerNumber, true);
		}
		if (outputs[1] > OUTPUT_THRESHOLD) {
			moveSuccess |= this.game.moveX(playerNumber, false);
		}
		if (outputs[2] > OUTPUT_THRESHOLD) {
			moveSuccess |= this.game.moveY(playerNumber, true);
		}
		if (outputs[3] > OUTPUT_THRESHOLD) {
			moveSuccess |= this.game.moveY(playerNumber, false);
		}

		failedMovesInARow = moveSuccess ? 0: failedMovesInARow + 1;

		if (failedMovesInARow > 1) {
			state = TaskState.FAILED;
		}

		if (game.getBoard().getPlayer(playerNumber).getX() > game.getBoard().getDeathWall()) {
			state = TaskState.SUCCEEDED;
		}
	}

	@Override
	public int getNumOutputs() {
		return 4;
	}

	@Override
	public int getNumInputs() {
		return 3;
	}

	@Override
	public int getFitness() {
		Player player = game.getBoard().getPlayer(playerNumber);
		boolean hasLost = player.getX() < game.getBoard().getDeathWall();
		hasLost = hasLost || player.getY() == game.getBoard().getHeight() || player.getY() == 0;

		return Math.max(player.getX() - (hasLost ? 2000 : 0), 1);
//		return Math.max(0 + (int) (0.2 * player.getDistanceTraveled()) * (hasLost ? FITNESS_LOSS_PENALTY : 1), 0);
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
	
	public RunnerController getGame(){
		return game;
	}
}

package com.jam.neural.controller;

import com.jam.runner.controller.Game;
import com.jam.runner.model.Player;
import com.jam.neural.model.NeuralTask;

import java.util.Observer;
import java.util.Random;

public class NeuralRunner implements NeuralTask {
	private Game game;
	private int playerNumber;

	private TaskState state;
	private long seed;

	private int fitness;
	private static final int FITNESS_LOSS_PENALTY = 1000;

	private Observer updateObserver;

	public NeuralRunner(Game game, int playerNumber, Long seed, Observer updateObserver){
		this.game = game;
		this.playerNumber = playerNumber;
		this.seed = seed;
		this.updateObserver = updateObserver;

		reset();
	}

	@Override
	public void reset() {
		seed = new Random(seed).nextLong();
		game.resetGame(seed);
		state = TaskState.PROCESSING;
		fitness = 1;
	}
	
	@Override
	public float[] getInputs() {
		Player player = game.getBoard().getPlayer(this.playerNumber);
		int width = game.getBoard().getWidth(), height = game.getBoard().getHeight();

		float mappedX = map(player.getX(), 0, width, 0, 1);
		float mappedY = map(player.getY(), 0, height, 0, 1);

		return new float[]{mappedX, mappedY};
	}

	private float map(float num, long inMin, long inMax, long outMin, long outMax) {
		return (num - inMin) * (outMax - outMin) / (inMax - inMin) + outMin;
	}
	
	@Override
	public void setOutputs(float[] outputs) {
		float output = outputs[0];

		//Encoded movement in 4 different directions
		if (output < 0.25) {
			this.game.moveX(playerNumber, true);
		} else if (output < 0.5) {
			this.game.moveX(playerNumber, false);
		} else if (output < 0.75) {
			this.game.moveY(playerNumber, true);
		} else {
			this.game.moveY(playerNumber, false);
		}
	}

	@Override
	public int getNumOutputs() {
		return 1;
	}

	@Override
	public int getNumInputs() {
		return 2;
	}

	@Override
	public int getFitness() {
		Player player = game.getBoard().getPlayer(playerNumber);
		boolean hasLost = player.getX() < game.getBoard().getWidth() / 2;

		return Math.max(player.getX() - (hasLost ? FITNESS_LOSS_PENALTY : 0), 1);
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
	public void setTaskState(TaskState state) {
		this.state = state;
	}
	
	public Game getGame(){
		return game;
	}
}

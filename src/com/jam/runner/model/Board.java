package com.jam.runner.model;

import java.awt.*;
import java.util.Random;

public class Board {
	private Player[] players;

	private int width, height;
	private int numPlayers;

	private long seed;
	
	private boolean debug;
	
	/**
	 * @param seed Seed to use when generating level. If same seed is used for multiple games, and the
	 * user starts them by clicking on the same field, the boards will be identical. Use null if not using this feature.
	 */
	public Board(int width, int height, Long seed, int numPlayers){
		this.width = width;
		this.height = height;
		this.numPlayers = numPlayers;

		restartGame(seed);
	}
	
	/**
	 * This resets the game into its initial state. Note that it doesn't generate the board,
	 * that happens when user first clicks on a tile.
	 * @param seed Seed to use for generating the new board
	 */
	public void restartGame(Long seed){
		this.seed = (seed == null) ? new Random().nextLong() : seed;
		this.players = new Player[numPlayers];

		initEmptyBoardArray();
	}

	private void initEmptyBoardArray(){
		Random rand = new Random(seed);
		for (int i = 0; i < numPlayers; i++) {
			int randX = rand.nextInt(width);
			int randY = rand.nextInt(height);
			this.players[i] = new Player(randX, randY, new Color(rand.nextInt(255), rand.nextInt(255), rand.nextInt(255)));
		}
	}
	
	public void setDebug(boolean debug){
		this.debug = debug;
	}

	public boolean moveX(int playerNumber, boolean left) {
		Player player = players[playerNumber];

		if (left && player.getX() > 0) {
			players[playerNumber].setX(player.getX() - 1);
			return true;
		} else if (!left && player.getX() < width) {
			players[playerNumber].setX(player.getX() + 1);
			return true;
		}

		return false;
	}

	public boolean moveY(int playerNumber, boolean up) {
		Player player = players[playerNumber];

		if (up && player.getY() > 0) {
			players[playerNumber].setY(player.getY() - 1);
			return true;
		} else if (!up && player.getY() < height) {
			players[playerNumber].setY(player.getY() + 1);
			return true;
		}

		return false;
	}

	public Player getPlayer(int playerNumber) {
		return this.players[playerNumber];
	}

	public int getNumPlayers() {
		return this.numPlayers;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}
}

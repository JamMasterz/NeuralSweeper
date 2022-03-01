package com.jam.runner.model;

import com.jam.neural.model.Updatable;

import java.awt.*;
import java.util.Observable;
import java.util.Random;

public class Board extends Observable implements Updatable {
	private Player[] players;
	private Wall[] walls;

	private int deathWall;

	private int width, height;
	private int numPlayers, numWalls;

	private long seed;
	
	private boolean debug;
	
	/**
	 * @param seed Seed to use when generating level. If same seed is used for multiple games, and the
	 * user starts them by clicking on the same field, the boards will be identical. Use null if not using this feature.
	 */
	public Board(int width, int height, Long seed, int numPlayers, int numWalls, int deathWall){
		this.width = width;
		this.height = height;
		this.numPlayers = numPlayers;
		this.deathWall = deathWall;
		this.numWalls = numWalls;

		restartGame(seed);
	}

	public boolean collidesWithAnyWall(int x, int y) {
		for (Wall w : walls) {
			if (w.collides(x, y)) {
				return true;
			}
		}

		return false;
	}

	public boolean isCloseToAnyWall(int x, int y) {
		for (Wall w : walls) {
			if (w.isCloseTo(x, y)) {
				return true;
			}
		}

		return false;
	}

	/**
	 * This resets the game into its initial state. Note that it doesn't generate the board,
	 * that happens when user first clicks on a tile.
	 * @param seed Seed to use for generating the new board
	 */
	public void restartGame(Long seed){
		this.seed = (seed == null) ? new Random().nextLong() : seed;
		this.players = new Player[numPlayers];
		this.walls = new Wall[numWalls];
		Random r = new Random(seed);

		if (numWalls != 0) {
			int wallHeight = height / numWalls * 2;
			int wallSpacing = width / numWalls;
			for (int i = 0; i < this.numWalls; i++) {
				int randomX = r.nextInt(numWalls - 2) + 1;
				int randomY = r.nextInt(numWalls / 2);

				walls[i] = new Wall(randomX * wallSpacing, randomY * wallHeight, wallHeight);
			}
		}

		initEmptyBoardArray(); //Observers notified inside
	}

	private void initEmptyBoardArray(){
		Random rand = new Random(seed);
		for (int i = 0; i < numPlayers; i++) {
			int randX = rand.nextInt(width);
			int randY = rand.nextInt(height);
			this.players[i] = new Player(randX, randY, new Color(rand.nextInt(255), rand.nextInt(255), rand.nextInt(255)));
		}

//		setChanged();
		notifyObservers();
	}
	
	public void setDebug(boolean debug){
		this.debug = debug;
	}

	public boolean moveX(int playerNumber, boolean left) {
		Player player = players[playerNumber];

		if (left && player.getX() > 0 && !collidesWithAnyWall(player.getX() - 1, player.getY())) {
			players[playerNumber].add(-1, 0);
//			setChanged();
			notifyObservers();
			return true;
		} else if (!left && player.getX() < width && !collidesWithAnyWall(player.getX() + 1, player.getY())) {
			players[playerNumber].add(1, 0);
//			setChanged();
			notifyObservers();
			return true;
		}

		return false;
	}

	public boolean moveY(int playerNumber, boolean up) {
		Player player = players[playerNumber];

		if (up && player.getY() > 0 && !collidesWithAnyWall(player.getX(), player.getY() - 1)) {
			players[playerNumber].add(0, -1);
//			setChanged();
			notifyObservers();
			return true;
		} else if (!up && player.getY() < height && !collidesWithAnyWall(player.getX(), player.getY() + 1)) {
			players[playerNumber].add(0, 1);
//			setChanged();
			notifyObservers();
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

	public Wall[] getWalls() {
		return walls;
	}

	@Override
	public void update() {
		setChanged();
		notifyObservers();
	}

	public int getDeathWall() {
		return deathWall;
	}
}

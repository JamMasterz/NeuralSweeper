package com.jam.runner.controller;

import com.jam.runner.model.Board;
import com.jam.runner.model.Wall;
import com.jam.runner.view.RunnerGameGUI;

import javax.swing.*;

public class RunnerController {
	private Board board;
	private RunnerGameGUI gui;

	private boolean debug = false;

	public RunnerController(int width, int height, Long seed, int numPlayers){
		this.board = new Board(width, height, seed, numPlayers, 20, 19 * width / 20);
		board.setDebug(debug);
	}

	public RunnerGameGUI getGUI(double scale){
		if (gui == null) {
			gui = new RunnerGameGUI(this);
			board.addObserver(gui);
		}
		gui.setDebug(debug);
		
		return gui;
	}
	
	/**
	 * Removes GUI for this game
	 */
	public void disconnectGUI() {
		board.deleteObserver(gui);
		gui.disconnect();
		gui = null;
	}

	public boolean moveX(int playerNumber, boolean left) {
		boolean moveSuccess = board.moveX(playerNumber, left);

		return moveSuccess;
	}

	public boolean moveY(int playerNumber, boolean up) {
		boolean moveSuccess = board.moveY(playerNumber, up);

		return moveSuccess;
	}
	
	/**
	 * @return The game board
	 */
	public Board getBoard(){
		return this.board;
	}
	
	/**
	 * Resets the game so it can be played again
	 * @param seed Seed to use when generating the board. Can be used to generate identical boards
	 */
	public void resetGame(Long seed){
		board.restartGame(seed);
		if (gui != null){
			gui.updateTime();
		}
	}
}

package com.jam.runner.controller;

import com.jam.runner.model.Board;
import com.jam.runner.view.RunnerGameGUI;

import javax.swing.*;
import java.util.Observable;

public class Game extends Observable {
	private Board board;
	private RunnerGameGUI gui;
	private boolean debug = false;

	public Game(int width, int height, Long seed, int numPlayers){
		this.board = new Board(width, height, seed, numPlayers);
		board.setDebug(debug);
	}

	public JPanel getGUI(double scale){
		if (gui == null) gui = new RunnerGameGUI(this);
		gui.setDebug(debug);
		
		return gui.getGUI();
	}
	
	/**
	 * Removes GUI for this game
	 */
	public void disconnectGUI(){
		gui.disconnect();
		gui = null;
	}

	public boolean moveX(int playerNumber, boolean left) {
		boolean moveSuccess = board.moveX(playerNumber, left);

		notifyObservers(playerNumber);

		return moveSuccess;
	}

	public boolean moveY(int playerNumber, boolean up) {
		boolean moveSuccess = board.moveY(playerNumber, up);

		notifyObservers(playerNumber);

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
			gui.updateGUI(getBoard().getNumPlayers() - 1);
			gui.updateTime();
		}
		notifyObservers(board.getNumPlayers() - 1);
	}
}

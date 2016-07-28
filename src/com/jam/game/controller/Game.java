package com.jam.game.controller;

import javax.swing.JPanel;

import com.jam.game.model.Board;
import com.jam.game.model.Coord;
import com.jam.game.model.TagResult;
import com.jam.game.model.UncoverResult;
import com.jam.game.view.MinesweeperGUI;

public class Game {
	public static final int NOOB_SIZE = 9;
	public static final int INTERMEDIATE_SIZE = 16;
	public static final int EXPERT_SIZE = 22;
	public static final int NOOB_BOMBS = 9;
	public static final int INTERMEDIATE_BOMBS = 40;
	public static final int EXPERT_BOMBS = 100;
	
	private Board board;
	private MinesweeperGUI gui;
	private boolean automatic = false;
	private boolean debug = false;
	
	public Game(int size, int bombs){
		this.board = new Board(size, bombs);
		board.setDebug(debug);
	}
	
	public Game(DefaultGamePreference pref){
		switch (pref){
			case NOOB:
				this.board = new Board(NOOB_SIZE, NOOB_BOMBS);
				break;
			case INTERMEDIATE:
				this.board = new Board(INTERMEDIATE_SIZE, INTERMEDIATE_BOMBS);
				break;
			case EXPERT:
				this.board = new Board(EXPERT_SIZE, EXPERT_BOMBS);
				break;
		}
		board.setDebug(debug);
	}
	
	public JPanel getGUI(){
		if (gui == null) gui = new MinesweeperGUI(this);
		gui.setDebug(debug);
		
		return gui.getGUI();
	}
	
	public void disconnectGUI(){
		//TODO: disconnect gui
		
		setAutomatic(true); //Maybe not
	}
	
	public void leftClickField(Coord coord){
		UncoverResult result = board.uncoverSingle(coord);
		if (gui != null){
			if (result != UncoverResult.FAILED){
				gui.updateBoard();
			} 
			if (result == UncoverResult.VICTORY || result == UncoverResult.MINE){
				gui.displayGameState(result);
			}
		}
	}
	
	public void rightClickField(Coord coord){
		TagResult result = board.tagSingleField(coord);
		if (gui != null){
			if (result == TagResult.FAILED){
				return;
			} else {
				gui.updateBoard();
			}
		}
	}
	
	public void setAutomatic(boolean automatic){
		this.automatic = automatic;
	}
	
	public boolean isAutomatic(){
		return this.automatic;
	}
	
	public Board getBoard(){
		return this.board;
	}
	
	public int getSize(){
		return board.getSize();
	}
}

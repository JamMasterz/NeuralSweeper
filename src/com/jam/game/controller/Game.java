package com.jam.game.controller;

import javax.swing.JPanel;

import com.jam.game.model.Board;
import com.jam.game.model.Coord;
import com.jam.game.model.GameState;
import com.jam.game.model.TagResult;
import com.jam.game.model.UncoverResult;
import com.jam.game.view.MinesweeperGUI;

//TODO: On defeat, show all the mines, color the killing mine red, put an X over bad flags
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
	private int bombs;
	
	public Game(int size, int bombs){
		this.board = new Board(size, bombs);
		this.bombs = bombs;
		board.setDebug(debug);
	}
	
	public Game(DefaultGamePreference pref){
		switch (pref){
			case NOOB:
				this.board = new Board(NOOB_SIZE, NOOB_BOMBS);
				this.bombs = NOOB_BOMBS;
				break;
			case INTERMEDIATE:
				this.board = new Board(INTERMEDIATE_SIZE, INTERMEDIATE_BOMBS);
				this.bombs = INTERMEDIATE_BOMBS;
				break;
			case EXPERT:
				this.board = new Board(EXPERT_SIZE, EXPERT_BOMBS);
				this.bombs = EXPERT_BOMBS;
				break;
		}
		board.setDebug(debug);
	}
	
	public JPanel getGUI(double scale){
		if (gui == null) gui = new MinesweeperGUI(this, scale);
		gui.setDebug(debug);
		
		return gui.getGUI();
	}
	
	public void disconnectGUI(){
		gui = null;
		
		setAutomatic(true); //Maybe not
	}
	
	public UncoverResult leftClickField(Coord coord){
		return board.uncoverSingle(coord);
	}
	
	public TagResult rightClickField(Coord coord){
		return board.tagSingleField(coord);
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
	
	public GameState getGameState(){
		return board.getState();
	}
	
	public int getBombsInitial(){
		return bombs;
	}
	
	public void resetGame(){
		board.restartGame();
		if (gui != null){
			gui.updateBoard();
			gui.updateTime();
		}
	}
}

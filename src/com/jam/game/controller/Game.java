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
	private boolean controllable;
	private boolean debug = false;
	private int bombs;
	
	public Game(int size, int bombs, boolean controllable){
		constructor(size, bombs, controllable);
	}
	
	public Game(DefaultGamePreference pref, boolean controllable){
		switch (pref){
			case NOOB:
				constructor(NOOB_SIZE, NOOB_BOMBS, controllable);
				break;
			case INTERMEDIATE:
				constructor(INTERMEDIATE_SIZE, INTERMEDIATE_BOMBS, controllable);
				break;
			case EXPERT:
				constructor(EXPERT_SIZE, EXPERT_BOMBS, controllable);
				break;
		}
	}
	
	/**
	 * Because java is stiupid
	 */
	private void constructor(int size, int bombs, boolean controllable){
		this.board = new Board(size, bombs);
		this.bombs = bombs;
		this.controllable = controllable;
		board.setDebug(debug);
	}
	
	public JPanel getGUI(double scale){
		if (gui == null) gui = new MinesweeperGUI(this, scale, controllable);
		gui.setDebug(debug);
		
		return gui.getGUI();
	}
	
	public void disconnectGUI(){
		gui = null;
	}
	
	public UncoverResult leftClickField(Coord coord){
		UncoverResult r = board.uncoverSingle(coord);
		
		if (!controllable && gui != null){
			gui.updateBoard();
		}
		
		return r;
	}
	
	public TagResult rightClickField(Coord coord){
		TagResult r = board.tagSingleField(coord);
		
		if (!controllable && gui != null){
			gui.updateBoard();
		}
		
		return r;
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

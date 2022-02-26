package com.jam.minesweeper.controller;

import javax.swing.JPanel;

import com.jam.minesweeper.model.Board;
import com.jam.minesweeper.model.Board.GameState;
import com.jam.minesweeper.model.Board.TagResult;
import com.jam.minesweeper.model.Board.UncoverResult;
import com.jam.minesweeper.model.Coord;
import com.jam.minesweeper.view.MinesweeperGUI;

import java.util.Observable;

//TODO: On defeat, show all the mines, color the killing mine red, put an X over bad flags
public class GameController extends Observable {
	public enum DefaultGamePreference {
		NOOB, INTERMEDIATE, EXPERT;
	}
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
	
	/**
	 * @param size Amount of fields in 1 dimension. The board is square
	 * @param bombs Amount of bombs
	 * @param seed Seed to use when generating the board. Can be used to generate identical boards
	 * @param controllable Whether the GUI is responsive to user actions
	 */
	public GameController(int size, int bombs, Long seed, boolean controllable){
		constructor(size, bombs, seed, controllable);
	}
	
	/**
	 * @param pref Game preset
	 * @param seed Seed to use when generating the board. Can be used to generate identical boards
	 * @param controllable Whether the GUI is responsive to user actions
	 */
	public GameController(DefaultGamePreference pref, Long seed, boolean controllable){
		switch (pref){
			case NOOB:
				constructor(NOOB_SIZE, NOOB_BOMBS, seed, controllable);
				break;
			case INTERMEDIATE:
				constructor(INTERMEDIATE_SIZE, INTERMEDIATE_BOMBS, seed, controllable);
				break;
			case EXPERT:
				constructor(EXPERT_SIZE, EXPERT_BOMBS, seed, controllable);
				break;
		}
	}
	
	/**
	 * Because java is stiupid
	 */
	private void constructor(int size, int bombs, Long seed, boolean controllable){
		this.board = new Board(size, bombs, seed);
		this.bombs = bombs;
		this.controllable = controllable;
		board.setDebug(debug);
	}
	
	/**
	 * @param scale Scale of the GUI
	 * @return Panel containing the GUI of the game. Can be used to stack multiple games in one window
	 */
	public JPanel getGUI(double scale){
		if (gui == null) gui = new MinesweeperGUI(this, scale, controllable);
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
	
	/**
	 * Clicks on a specified field
	 * @param coord Coordinate to click on
	 * @return Result of the action
	 */
	public UncoverResult leftClickField(Coord coord){
		UncoverResult r = board.uncoverSingle(coord);
		
//		if (!controllable && gui != null){
//			gui.updateGUI(0);
//		}

		notifyObservers();

		return r;
	}
	
	/**
	 * Tags a specified field
	 * @param coord Coordinate to click on
	 * @return Result of the action
	 */
	public TagResult rightClickField(Coord coord){
		TagResult r = board.tagSingleField(coord);
		
//		if (!controllable && gui != null){
//			gui.updateGUI(0);
//		}

		notifyObservers();
		
		return r;
	}
	
	/**
	 * @return The game board
	 */
	public Board getBoard(){
		return this.board;
	}
	
	/**
	 * @return Amount of fields in 1 dimension. The board is square
	 */
	public int getSize(){
		return board.getSize();
	}
	
	/**
	 * @return Game state
	 */
	public GameState getGameState(){
		return board.getState();
	}
	
	/**
	 * @return How many bombs there were at the start
	 */
	public int getBombsInitial(){
		return bombs;
	}
	
	/**
	 * Resets the game so it can be played again
	 * @param seed Seed to use when generating the board. Can be used to generate identical boards
	 */
	public void resetGame(Long seed){
		board.restartGame(seed);
		if (gui != null){
//			gui.updateGUI(0);
			gui.updateTime();
		}
		notifyObservers();
	}
}

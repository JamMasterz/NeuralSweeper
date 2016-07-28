package com.jam.game.controller;

import javax.swing.JPanel;

import com.jam.game.model.Board;
import com.jam.game.model.Coord;
import com.jam.game.model.TagResult;
import com.jam.game.model.UncoverResult;

public class Game {
	public static final int NOOB_SIZE = 9;
	public static final int INTERMEDIATE_SIZE = 16;
	public static final int EXPERT_SIZE = 22;
	public static final int NOOB_BOMBS = 9;
	public static final int INTERMEDIATE_BOMBS = 40;
	public static final int EXPERT_BOMBS = 100;
	
	private Board board;
	private boolean automatic = false;
	
	public Game(int size, int bombs){
		this.board = new Board(size, bombs);
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
	}
	
	public void addGUI(JPanel parent){
		
	}
	
	public void disconnectGUI(){
		//TODO: disconnect gui
		
		setAutomatic(true); //Maybe not
	}
	
	public void leftClickField(Coord coord){
		UncoverResult result = board.uncoverSingle(coord);
	}
	
	public void rightClickField(Coord coord){
		TagResult result = board.tagSingleField(coord);
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

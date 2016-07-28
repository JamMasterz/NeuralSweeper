package com.jam.game.controller;

import javax.swing.JPanel;

import com.jam.game.model.Board;
import com.jam.game.model.Coord;
import com.jam.game.model.TagResult;
import com.jam.game.model.UncoverResult;

public class Game {
	private Board board;
	private boolean automatic = false;
	
	public Game(int size, int bombs){
		this.board = new Board(size, bombs);
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

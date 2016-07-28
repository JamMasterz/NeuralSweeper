package com.jam.game.controller;

import javax.swing.JPanel;

import com.jam.game.model.Board;

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
	
	public void setAutomatic(boolean automatic){
		this.automatic = automatic;
	}
	
	public boolean isAutomatic(){
		return this.automatic;
	}
}

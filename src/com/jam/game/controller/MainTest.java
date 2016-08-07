package com.jam.game.controller;

import com.jam.main.BoardFrame;

public class MainTest {
	
	public static void main(String[] args) {
		Game[] games = new Game[6];
		for (int i = 0; i < games.length; i++){
			games[i] = new Game(DefaultGamePreference.NOOB, true);
		}
		
		new BoardFrame(games, games.length);
	}
}

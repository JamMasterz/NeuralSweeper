package com.jam.game.controller;

import com.jam.main.GameArrayFrame;

public class MainTest {
	
	public static void main(String[] args) {
		Game[] games = new Game[6];
		for (int i = 0; i < games.length; i++){
			games[i] = new Game(DefaultGamePreference.NOOB, true);
		}
		
		new GameArrayFrame(games, 3, 3, 1);
	}
}

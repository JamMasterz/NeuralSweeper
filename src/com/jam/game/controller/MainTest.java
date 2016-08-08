package com.jam.game.controller;

import java.util.Random;

import com.jam.main.GameArrayFrame;

public class MainTest {
	
	public static void main(String[] args) {
		Game[] games = new Game[6];
		long seed = new Random().nextLong();
		for (int i = 0; i < games.length; i++){
			games[i] = new Game(DefaultGamePreference.NOOB, seed, true);
		}
		
		new GameArrayFrame(games, 3, 3, 1);
	}
}

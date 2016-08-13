package com.jam.game.controller;

import java.util.Random;

import com.jam.neural.view.GameArrayFrame;

public class MainTest {
	
	public static void main(String[] args) {
		Game[] games = new Game[1];
		long seed = new Random().nextLong();
		for (int i = 0; i < games.length; i++){
			games[i] = new Game(DefaultGamePreference.NOOB, seed, true);
		}
		
		new GameArrayFrame(games, 1, 1, 1);
	}
}

package com.jam.minesweeper.controller;

import java.util.Random;

import com.jam.minesweeper.controller.GameController.DefaultGamePreference;
import com.jam.neural.view.GameArrayFrame;

public class MainTest {
	
	public static void main(String[] args) {
		GameController[] games = new GameController[1];
		long seed = new Random().nextLong();
		for (int i = 0; i < games.length; i++){
			games[i] = new GameController(DefaultGamePreference.NOOB, seed, true);
		}
		
		new GameArrayFrame(games, 1, 1, 1);
	}
}

package com.jam.runner.controller;

import com.jam.minesweeper.controller.GameController;
import com.jam.minesweeper.controller.GameController.DefaultGamePreference;
import com.jam.neural.view.GameArrayFrame;

import java.util.Random;

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

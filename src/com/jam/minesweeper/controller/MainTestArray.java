package com.jam.minesweeper.controller;

import java.util.Random;

public class MainTestArray {
	
	public static void main(String[] args) {
		long seed = new Random().nextLong();
		ArrayMinesweeperController game = new ArrayMinesweeperController(MinesweeperController.DefaultGamePreference.NOOB, seed, true, 3);

		game.getGUI(1);
	}
}

package com.jam.minesweeper.controller;

import java.awt.*;
import java.util.Random;

import com.jam.minesweeper.controller.MinesweeperController.DefaultGamePreference;

import javax.swing.*;

public class MainTest {
	
	public static void main(String[] args) {
		long seed = new Random().nextLong();
		MinesweeperController game = new MinesweeperController(DefaultGamePreference.INTERMEDIATE, seed, true);

		JFrame frame = new JFrame("Game Boards");
		frame.setLayout(new GridLayout(1, 1));
		frame.setResizable(false);

		frame.add(game.getGUI(1));

		frame.pack();
		frame.setVisible(true);
		frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
	}
}

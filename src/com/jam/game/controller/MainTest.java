package com.jam.game.controller;

import javax.swing.JFrame;

public class MainTest {

	public static void main(String[] args) {
		Game game = new Game(DefaultGamePreference.INTERMEDIATE);
		
		JFrame frame = new JFrame("Minesweeper test");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		frame.add(game.getGUI(1));
		frame.setResizable(false);
		
		frame.pack();
		frame.setVisible(true);
	}
}

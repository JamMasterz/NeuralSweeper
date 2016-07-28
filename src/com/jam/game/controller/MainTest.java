package com.jam.game.controller;

import javax.swing.JFrame;

public class MainTest {

	public static void main(String[] args) {
		Game game = new Game(DefaultGamePreference.NOOB);
		
		JFrame frame = new JFrame("Minesweeper test");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		frame.add(game.getGUI());
		
		frame.pack();
		frame.setVisible(true);
	}
}

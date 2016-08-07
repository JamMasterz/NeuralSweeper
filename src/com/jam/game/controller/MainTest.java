package com.jam.game.controller;

import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class MainTest {

	public static void main(String[] args) {
		Game[] games = new Game[1];
		for (int i = 0; i < games.length; i++){
			games[i] = new Game(DefaultGamePreference.NOOB, true);
		}
		
		JFrame frame = new JFrame("Minesweeper test");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new GridLayout(1, 1));
		frame.setResizable(false);
		for (int i = 0; i < games.length; i++) {
			JPanel p = games[i].getGUI(1);
			p.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
			frame.add(p);
		}
		frame.pack();
		frame.setVisible(true);
	}
}

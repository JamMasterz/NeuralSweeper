package com.jam.neural.view;

import java.awt.GridLayout;
import java.awt.event.WindowListener;

import javax.swing.JFrame;

import com.jam.game.controller.Game;

public class GameArrayFrame{
	private JFrame frame;

	public GameArrayFrame(Game[] games, int gridWidth, int gridHeight, float scale){
		frame = new JFrame("Game Boards");
		frame.setLayout(new GridLayout(gridHeight, gridWidth));
		frame.setResizable(false);
		
		for (int i = 0; i < gridWidth * gridHeight; i++) {
			if (i >= games.length) break;
			frame.add(games[i].getGUI(scale));
		}
		
		frame.pack();
		frame.setVisible(true);
	}
	
	public void setWindowListener(WindowListener list){
		frame.addWindowListener(list);
	}
}

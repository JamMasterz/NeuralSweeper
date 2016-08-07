package com.jam.main;

import java.awt.GridLayout;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;

import com.jam.game.controller.Game;

public class GameArrayFrame implements WindowListener{
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

	@Override
	public void windowOpened(WindowEvent e) {
		Main.guiAttached = true;
	}
	
	@Override
	public void windowClosed(WindowEvent e) {
		Main.guiAttached = false;
	}
	
	@Override
	public void windowActivated(WindowEvent e) {}

	@Override
	public void windowClosing(WindowEvent e) {}

	@Override
	public void windowDeactivated(WindowEvent e) {}

	@Override
	public void windowDeiconified(WindowEvent e) {}

	@Override
	public void windowIconified(WindowEvent e) {}
}

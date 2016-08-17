package com.jam.neural.view;

import java.awt.GridLayout;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

import com.jam.game.controller.Game;

public class GameArrayFrame{
	private JFrame frame;
	private Game[] games;

	public GameArrayFrame(Game[] games, int gridWidth, int gridHeight, float scale){
		this.games = games;
		frame = new JFrame("Game Boards");
		frame.setLayout(new GridLayout(gridHeight, gridWidth));
		frame.setResizable(false);
		
		for (int i = 0; i < gridWidth * gridHeight; i++) {
			if (i >= games.length) break;
			frame.add(games[i].getGUI(scale));
		}
		
		frame.pack();
		frame.setVisible(true);
		frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
	}
	
	public void setWindowListener(WindowListener list){
		frame.addWindowListener(list);
	}
	
	public void closeWidnow(){
		for (Game g : games){
			g.disconnectGUI();
		}
		frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
	}
}

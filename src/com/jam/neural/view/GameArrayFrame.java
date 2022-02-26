package com.jam.neural.view;

import java.awt.GridLayout;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

import com.jam.minesweeper.controller.GameController;

public class GameArrayFrame extends Observable implements Observer {
	private JFrame frame;
	private GameController[] games;

	public GameArrayFrame(GameController[] games, int gridWidth, int gridHeight, float scale){
		this.games = games;
		frame = new JFrame("Game Boards");
		frame.setLayout(new GridLayout(gridHeight, gridWidth));
		frame.setResizable(false);
		
		for (int i = 0; i < gridWidth * gridHeight; i++) {
			if (i >= games.length) break;
			frame.add(games[i].getGUI(scale));
		}

		for (GameController g : games) {
			g.addObserver(this);
		}
		
		frame.pack();
		frame.setVisible(true);
		frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
	}
	
	public void setWindowListener(WindowListener list){
		frame.addWindowListener(list);
	}
	
	public void closeWidnow(){
		for (GameController g : games){
			g.disconnectGUI();
		}
		frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
	}

	@Override
	public void update(Observable o, Object arg) {
		notifyObservers();
	}
}

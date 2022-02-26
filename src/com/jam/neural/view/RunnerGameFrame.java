package com.jam.neural.view;

import com.jam.runner.controller.Game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.Observable;
import java.util.Observer;

public class RunnerGameFrame extends Observable implements Observer {
	private JFrame frame;
	private Game game;

	public RunnerGameFrame(Game game, float scale){
		this.game = game;
		frame = new JFrame("Game Boards");
		frame.setResizable(false);
		frame.add(game.getGUI(scale));
		frame.setVisible(true);
		frame.setSize(new Dimension(game.getBoard().getWidth(), game.getBoard().getHeight()));
		frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

		game.addObserver(this);
	}
	
	public void setWindowListener(WindowListener list){
		frame.addWindowListener(list);
	}
	
	public void closeWidnow(){
		game.disconnectGUI();
		frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
	}

	@Override
	public void update(Observable o, Object arg) {
		notifyObservers(arg);
	}
}

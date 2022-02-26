package com.jam.neuralsweeper.main;

import com.jam.runner.controller.Game;
import com.jam.neural.controller.NeuralRunner;
import com.jam.neural.model.NeuralTask;
import com.jam.neural.model.TaskSetup;
import com.jam.neural.view.RunnerGameFrame;
import com.jam.neural.view.Updatable;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.event.WindowListener;
import java.util.Observable;
import java.util.Observer;
import java.util.Random;

public class RunnerGameSetup implements TaskSetup, Observer {
	private static JPanel panel;

	private RunnerGameFrame gui;
	private Game game;

	private int width, height;

	public RunnerGameSetup(int width, int height) {
		panel = new JPanel();
		panel.setBorder(new TitledBorder("Runner Game Settings"));
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));

		this.width = width;
		this.height = height;
	}
	
	@Override
	public NeuralTask[] createNeuralTasks(int numTasks) {
		long seed = new Random().nextLong();
		this.game = new Game(this.width, this.height, seed, numTasks);

		NeuralRunner[] runners = new NeuralRunner[numTasks];

		for (int i = 0; i < runners.length; i++) {
			runners[i] = new NeuralRunner(game, i, seed, this);
		}

		return runners;
	}

	@Override
	public int getTickCap() {
		return width;
	}

	@Override
	public boolean allowsRepeating() {
		return true;
	}

	@Override
	public void attachGUI(NeuralTask[] objects, WindowListener listener, int width, int height, float scale) {
		gui = new RunnerGameFrame(game, scale);
		gui.setWindowListener(listener);
	}
	
	@Override
	public void detachGUI() {
		gui.closeWidnow();
	}

	@Override
	public JPanel getTaskPanel() {
		return panel;
	}

	@Override
	public void update(Observable o, Object arg) {
		System.out.println("Im here");
		((Updatable) gui).updateGUI((Integer) arg);
	}
}

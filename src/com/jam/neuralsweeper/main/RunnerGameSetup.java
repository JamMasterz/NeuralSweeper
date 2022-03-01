package com.jam.neuralsweeper.main;

import com.jam.neural.controller.NeuralRunner;
import com.jam.neural.model.NeuralTask;
import com.jam.neural.model.TaskSetup;
import com.jam.neural.view.RunnerGameFrame;
import com.jam.runner.controller.RunnerController;
import com.jam.runner.model.Wall;
import com.jam.runner.view.RunnerGameGUI;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.event.WindowListener;
import java.util.Random;

public class RunnerGameSetup implements TaskSetup {
	private static JPanel panel;
	private RunnerGameFrame frame;

	private RunnerController game;
	private int combinedWallLength;

	private int width, height;

	public RunnerGameSetup(int width, int height) {
		this.width = width;
		this.height = height;
		constructRunnerPanel();
	}

	private void constructRunnerPanel() {
		panel = new JPanel();
		panel.setBorder(new TitledBorder("Runner Game Settings"));
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
	}
	
	@Override
	public NeuralTask[] createNeuralTasks(int numTasks) {
		long seed = new Random().nextLong();
		this.game = new RunnerController(this.width, this.height, seed, numTasks);
		for (Wall w : game.getBoard().getWalls()) {
			this.combinedWallLength += w.getLength();
		}

		NeuralRunner[] runners = new NeuralRunner[numTasks];
		for (int i = 0; i < runners.length; i++) {
			runners[i] = new NeuralRunner(game, i, seed);
		}

		return runners;
	}

	@Override
	public int getTickCap() {
		return width + combinedWallLength;
	}

	@Override
	public boolean allowsRepeating() {
		return true;
	}

	@Override
	public void attachGUI(NeuralTask[] objects, WindowListener listener, int width, int height, float scale) {
		RunnerGameGUI gui = game.getGUI(scale);
		frame = new RunnerGameFrame(gui, this.width, this.height);
		frame.setWindowListener(listener);
	}
	
	@Override
	public void detachGUI() {
		frame.closeWindow();
		frame = null;
	}

	@Override
	public JPanel getTaskPanel() {
		return panel;
	}

	@Override
	public Runnable getTickUpdater() {
		return () -> game.getBoard().update();
	}
}

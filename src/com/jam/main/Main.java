package com.jam.main;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Timer;
import java.util.TimerTask;

import com.jam.game.controller.DefaultGamePreference;
import com.jam.neural.Population;

public class Main {
	static final int MAX_MOVES_TO_SOLVE_EASY = 200;
	static final int VISIBLE_SQUARE_SIZE = 7;
	static final int SPAWN_X = 4;
	static final int SPAWN_Y = 5;
	static boolean generated = false;
	static boolean running = false;
	static Population population;
	static Timer timer;

	public static void main(String[] args) {
		MainFrame mainFrame = new MainFrame();
		mainFrame.setSize(670, 450);
		mainFrame.setVisible(true);
		
		mainFrame.setStartActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (!generated){
					NeuralMinesweeper[] sweepers = new NeuralMinesweeper[mainFrame.getNumSpecimens()];
					for (int i = 0; i < sweepers.length; i++) {
						sweepers[i] = new NeuralMinesweeper(VISIBLE_SQUARE_SIZE, SPAWN_X, SPAWN_Y, DefaultGamePreference.NOOB);
					}
					population = new Population(sweepers, mainFrame.getNumSpecimens(), MAX_MOVES_TO_SOLVE_EASY, mainFrame.getNumHiddenLayers(), mainFrame.getNodesPerLayer());
					
					generated = true;
				}
				if (!running){
					running = true;
					mainFrame.setRunningIndicator(true);
					
					int interval = (int) (1000.0f / mainFrame.getTPS());
					timer = new Timer();
					timer.scheduleAtFixedRate(new TimerTask() {
						@Override
						public void run() { //TODO: Move this inside Population class, and have just 1 method for all this stuff
							if (!population.isGenerationDone()){
								population.tickGeneration(false);
							} else {
								System.out.println("repop");
								population.initNewGeneration();
								mainFrame.bumpGenerationNumber();
							}
						}
					}, 0, interval);
				}
			}
		});
		mainFrame.setStopActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (running){
					mainFrame.setRunningIndicator(false);
					timer.cancel();
					timer.purge();
					
					running = false;
				}
			}
		});
		mainFrame.setAttachActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//TODO: Attach GUI
			}
		});
		mainFrame.setShowGraphsActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//TODO: Show graphs
			}
		});
	}
}

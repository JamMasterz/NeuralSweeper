package com.jam.main;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import com.jam.game.controller.DefaultGamePreference;
import com.jam.game.controller.Game;
import com.jam.neural.Population;

public class Main {
	static final int MAX_MOVES_TO_SOLVE_EASY = 200;
	static final int VISIBLE_SQUARE_SIZE = 7;
	static final int SPAWN_X = 4;
	static final int SPAWN_Y = 5;
	
	static boolean generated = false;
	static boolean running = false;
	static boolean guiAttached = false;
	
	static Population population;
	static Timer timer;
	static NeuralMinesweeper[] sweepers;
	
	static MainFrame mainFrame;
	static GameArrayFrame guiFrame;

	public static void main(String[] args) {
		mainFrame = new MainFrame();
		mainFrame.setSize(670, 450);
		mainFrame.setVisible(true);
		
		mainFrame.setStartActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (!generated){
					sweepers = new NeuralMinesweeper[mainFrame.getNumSpecimens()];
					long seed = new Random().nextLong();
					for (int i = 0; i < sweepers.length; i++) {
						sweepers[i] = new NeuralMinesweeper(VISIBLE_SQUARE_SIZE, SPAWN_X, SPAWN_Y, seed, DefaultGamePreference.NOOB);
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
				if (!guiAttached && running){
					Game[] games = new Game[sweepers.length];
					for (int i = 0; i < sweepers.length; i++) {
						games[i] = sweepers[i].getGame();
					}
					
					int width = mainFrame.getGamesHorizontally();
					int height = mainFrame.getGamesVertically();
					float scale = mainFrame.getGamesScale();
					
					guiFrame = new GameArrayFrame(games, width, height, scale);
				}
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

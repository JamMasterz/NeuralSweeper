package com.jam.neural.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.Timer;
import java.util.TimerTask;

import com.jam.neural.model.Population;
import com.jam.neural.model.TaskSetup;
import com.jam.neural.view.FitnessGraph;
import com.jam.neural.view.MainFrame;

public class NeuralNetwork {
	private boolean initialized = false;
	private boolean running = false;
	private boolean guiAttached = false;
	private boolean graphing = false;
	
	private Population population;
	private Timer timer;
	private NeuralMinesweeper[] sweepers;
	
	private MainFrame mainFrame;
	private FitnessGraph fitnessGraph;

	public NeuralNetwork(TaskSetup setup) {
		mainFrame = new MainFrame(setup.getTaskPanel());
		
		mainFrame.setStartActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (!initialized){
					sweepers = (NeuralMinesweeper[]) setup.createNeuralTasks(mainFrame.getNumSpecimens());
					population = new Population(sweepers, mainFrame.getNumSpecimens(), setup.getTickCap(), mainFrame.getNumHiddenLayers(), mainFrame.getNodesPerLayer());
					
					initialized = true;
				}
				if (!running){
					mainFrame.setRunningIndicator(true);
					
					int interval = (int) (1000.0f / mainFrame.getTPS());
					timer = new Timer();
					timer.scheduleAtFixedRate(new TimerTask() {
						@Override
						public void run() { 
							if (population.tickGeneration(false)) mainFrame.bumpGenerationNumber();
							if (graphing){
								fitnessGraph.addFitness(population.getAverageFitness(), population.getBestFitness());
							}
						}
					}, 0, interval);
					
					running = true;
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
					WindowListener l = new WindowListener() {
						@Override
						public void windowOpened(WindowEvent e) {
							guiAttached = true;
						}
						@Override
						public void windowClosed(WindowEvent e) {
							guiAttached = false;
						}
						@Override
						public void windowIconified(WindowEvent e) {}
						@Override
						public void windowDeiconified(WindowEvent e) {}
						@Override
						public void windowDeactivated(WindowEvent e) {}
						@Override
						public void windowClosing(WindowEvent e) {}
						@Override
						public void windowActivated(WindowEvent e) {}
					};
					setup.attachGUI(sweepers, l, mainFrame.getGamesHorizontally(), mainFrame.getGamesVertically(), mainFrame.getGamesScale());
				}
			}
		});
		mainFrame.setShowGraphsActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (!graphing && running){
					if (fitnessGraph == null){
						fitnessGraph = new FitnessGraph();
						fitnessGraph.addWindowListener(new WindowListener() {
							@Override
							public void windowOpened(WindowEvent e) {
								graphing = true;
							}
							@Override
							public void windowClosed(WindowEvent e) {
								graphing = false;
							}
							@Override
							public void windowIconified(WindowEvent e) {}
							@Override
							public void windowDeiconified(WindowEvent e) {}
							@Override
							public void windowDeactivated(WindowEvent e) {}
							@Override
							public void windowClosing(WindowEvent e) {}
							@Override
							public void windowActivated(WindowEvent e) {}
						});
					} else {
						fitnessGraph.setVisible(true);
					}
				}
			}
		});
	}
}

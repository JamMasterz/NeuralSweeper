package com.jam.neural.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.jam.neural.model.Population;
import com.jam.neural.model.TaskSetup;
import com.jam.neural.view.FitnessGraph;
import com.jam.neural.view.MainFrame;
import com.jam.statistics.FitnessDatapoint;
import com.jam.statistics.StatisticsManager;

//TODO: Add window listener to this window and close the executor on exit
public class NeuralNetwork {
	public enum Mode{
		STOPPED, NORMAL, ACCELERATED;
	}
	
	public static final int STOP_DELAY = 5000;
	
	private boolean initialized = false;
	private Mode running = Mode.STOPPED;
	private boolean guiAttached = false;
	private boolean graphing = false;
	
	private Population population;
	private NeuralMinesweeper[] sweepers;
	private Timer timer;
	private ExecutorService executor;
	private Future<Void> acceleratedFuture;
	
	private StatisticsManager<FitnessDatapoint> stats;
	
	private MainFrame mainFrame;
	private FitnessGraph fitnessGraph;

	public NeuralNetwork(TaskSetup setup) {
		mainFrame = new MainFrame(setup.getTaskPanel());
		executor = Executors.newSingleThreadExecutor();
		stats = new StatisticsManager<>(1000, 10);
		
		mainFrame.setStartActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (!initialized){
					sweepers = (NeuralMinesweeper[]) setup.createNeuralTasks(mainFrame.getNumSpecimens());
					population = new Population(sweepers, mainFrame.getNumSpecimens(), setup.getTickCap(),
							mainFrame.getNumHiddenLayers(), mainFrame.getNodesPerLayer(), setup.allowsRepeating());
					population.createThreadPool(mainFrame.getNumThreads()); //TODO: Make the num of threads variable
					
					initialized = true;
				}
				if (running == Mode.STOPPED){
					mainFrame.setRunningIndicator(true);
					if (mainFrame.isEvolutionAccelerated()){
						startGraphWindow();
						acceleratedFuture = executor.submit(new Callable<Void>() {
							@Override
							public Void call() throws Exception {
								int genStart = population.getGeneration();
								while(population.getGeneration() < genStart + mainFrame.getGensToRun() && !Thread.interrupted()){
									try {
										if (population.tickGenerationMultiThreaded()) mainFrame.bumpGenerationNumber();
											if (graphing && population.isGenerationDone()){
												stats.put(new FitnessDatapoint(population.getGeneration(), population.getAverageFitness(), population.getBestFitness()));
												fitnessGraph.addFitness(population.getAverageFitness(), population.getBestFitness());
											}
									} catch (Exception e1) {
										e1.printStackTrace();
									}
								}
								//After N generations have been run, it stops itself
								mainFrame.setRunningIndicator(false);
								running = Mode.STOPPED;
								System.out.println("Finished accelerated evolution");
								return null;
							}
						});
						running = Mode.ACCELERATED;
					} else {
						startGraphWindow();
						int interval = (int) (1000.0f / mainFrame.getTPS());
						timer = new Timer();
						timer.scheduleAtFixedRate(new TimerTask() {
							@Override
							public void run() {
								try {
									if (population.tickGenerationMultiThreaded()) mainFrame.bumpGenerationNumber();
									if (graphing){
										stats.put(new FitnessDatapoint(population.getGeneration(), population.getAverageFitness(), population.getBestFitness()));
										fitnessGraph.addFitness(population.getAverageFitness(), population.getBestFitness());
									}
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
						}, 0, interval);
						running = Mode.NORMAL;
					}
				}
			}
		});
		mainFrame.setStopActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (running == Mode.NORMAL){
					timer.cancel();
					timer.purge();
				} else if (running == Mode.ACCELERATED){
					acceleratedFuture.cancel(true);
				}
				mainFrame.setRunningIndicator(false);
				
				running = Mode.STOPPED;
			}
		});
		mainFrame.setAttachActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				startGameArrayWindow(setup);
			}
		});
		mainFrame.setShowGraphsActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				startGraphWindow();
			}
		});
	}
	
	private void startGraphWindow(){
		if (!graphing){
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
	
	private void startGameArrayWindow(TaskSetup setup){
		if (!guiAttached && running == Mode.NORMAL){
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
}

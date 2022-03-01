package com.jam.neural.controller;

import com.jam.neural.model.NeuralTask;
import com.jam.neural.model.Population;
import com.jam.neural.model.TaskSetup;
import com.jam.neural.view.FitnessGraph;
import com.jam.neural.view.MainFrame;
import com.jam.statistics.FitnessDatapoint;
import com.jam.statistics.StatisticsManager;
import com.jam.statistics.StatisticsManager.UpdateRequirement;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

//TODO: Genomes have very similiar reactions at the start, this shouldnt be. Also, they are getting too inbred
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
	private NeuralTask[] tasks;
	private Timer timer;
	private ExecutorService executor;
	private Future<Void> acceleratedFuture;
	
	private StatisticsManager<FitnessDatapoint> stats;
	
	private MainFrame mainFrame;
	private FitnessGraph fitnessGraph;

	public NeuralNetwork(TaskSetup setup) {
		mainFrame = new MainFrame(setup.getTaskPanel());
		executor = Executors.newSingleThreadExecutor();
		stats = new StatisticsManager<>(500, 10);
		
		mainFrame.setStartActionListener(e -> {
			if (!initialized){
				initialize(setup);
			}
			if (running == Mode.STOPPED){
				mainFrame.setRunningIndicator(true);
				startGraphWindow();
				if (mainFrame.isEvolutionAccelerated()){
					if (!guiAttached) {
						runAccelerated(setup);
					} else {
						System.out.println("Cant run accelerated mode with GUI!");
					}
				} else {
					runNormal(setup);
				}
			}
		});
		mainFrame.setStopActionListener(e -> stopRunning());
		mainFrame.setAttachActionListener(e -> startGameArrayWindow(setup));
		mainFrame.setShowGraphsActionListener(e -> startGraphWindow());
		mainFrame.setTickActionListener(e -> runSingleTick(setup));
		mainFrame.setAcceptChangesActionListener(e -> {
			initialize(setup);
			stats.reset();
			mainFrame.setGenerationNumber(0);
		});
		
		KeyboardFocusManager focusManager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
		KeyStroke tickKey = KeyStroke.getKeyStroke('t');
		focusManager.addKeyEventDispatcher(e -> {
			if (KeyStroke.getKeyStrokeForEvent(e).equals(tickKey)){
				runSingleTick(setup);

				return true;
			}
			return false;
		});
		
		mainFrame.addWindowListener(new WindowListener() {
			@Override
			public void windowClosing(WindowEvent e) {
				stopRunning();
			}
			@Override
			public void windowOpened(WindowEvent e) {}
			@Override
			public void windowIconified(WindowEvent e) {}
			@Override
			public void windowDeiconified(WindowEvent e) {}
			@Override
			public void windowDeactivated(WindowEvent e) {}
			@Override
			public void windowClosed(WindowEvent e) {}
			@Override
			public void windowActivated(WindowEvent e) {}
		});
	}
	
	private void stopRunning(){
		if (running == Mode.NORMAL){
			timer.cancel();
			timer.purge();
		} else if (running == Mode.ACCELERATED){
			acceleratedFuture.cancel(true);
		}
		mainFrame.setRunningIndicator(false);
		
		running = Mode.STOPPED;
	}
	
	private void runNormal(TaskSetup setup){
		int interval = (int) (1000.0f / mainFrame.getTPS());
		timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				tickGeneration(setup);
			}
		}, 0, interval);
		running = Mode.NORMAL;
	}
	
	private void runAccelerated(TaskSetup setup){
		acceleratedFuture = executor.submit(() -> {
			int genStart = population.getGeneration();
			while(population.getGeneration() < genStart + mainFrame.getGensToRun() && !acceleratedFuture.isCancelled()){
				tickGeneration(setup);
			}

			//After N generations have been run, it stops itself
			mainFrame.setRunningIndicator(false);
			running = Mode.STOPPED;
			System.out.println("Finished accelerated evolution");
			return null;
		});
		running = Mode.ACCELERATED;
	}
	
	private void runSingleTick(TaskSetup setup){
		if (running == Mode.STOPPED){
			if (!initialized){
				initialize(setup);
			}
			tickGeneration(setup);
		}
	}
	
	private boolean tickGeneration(TaskSetup setup){
		boolean repopulated = false;
		try {
			if (population.tickGenerationMultiThreaded()){
				mainFrame.bumpGenerationNumber();
				repopulated = true;
			}
			if (setup.getTickUpdater() != null) {
				setup.getTickUpdater().run();
			}
			if (population.isGenerationDone()){
				UpdateRequirement update = stats.put(new FitnessDatapoint(population.getGeneration(),
						population.getAverageFitness(), population.getBestFitness()));
				if (graphing) fitnessGraph.update(update);
			}
		} catch (Exception e2) {
			e2.printStackTrace();
		}
		
		return repopulated;
	}
	
	private void initialize(TaskSetup setup){
		System.out.println("Initializing new neural net" + mainFrame.getNumSpecimens());
		tasks = setup.createNeuralTasks(mainFrame.getNumSpecimens());
		population = new Population(tasks, mainFrame.getNumSpecimens(), setup.getTickCap(),
				mainFrame.getNumHiddenLayers(), mainFrame.getNodesPerLayer(), setup.allowsRepeating());
		population.createThreadPool(mainFrame.getNumThreads()); //TODO: Make the num of threads variable
		
		initialized = true;
	}
	
	private void startGraphWindow(){
		if (!graphing){
			fitnessGraph = new FitnessGraph(stats.getData());
			graphing = true;
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
		}
	}
	
	private void startGameArrayWindow(TaskSetup setup){
		if (!initialized){
			initialize(setup);
		}
		if (!guiAttached && running != Mode.ACCELERATED){
			WindowListener l = new WindowListener() {
				@Override
				public void windowOpened(WindowEvent e) {
					guiAttached = true;
				}
				@Override
				public void windowClosed(WindowEvent e) {
					guiAttached = false;
					setup.detachGUI();
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
			setup.attachGUI(tasks, l, mainFrame.getGamesHorizontally(), mainFrame.getGamesVertically(), mainFrame.getGamesScale());
		}
	}
}

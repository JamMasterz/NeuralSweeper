package com.jam.neural.model;

import java.awt.event.WindowListener;

import javax.swing.JPanel;

/**
 * Implement this interface to setup the neural task
 */
public interface TaskSetup {
	/**
	 * @return Array of NeuralTasks. A neural task is a intermediary between an arbitrary program and the neural network
	 */
	NeuralTask[] createNeuralTasks(int numTasks);
	
	/**
	 * @return How many times a single task can be ticked before its interrupted
	 */
	int getTickCap();
	
	/**
	 * @return Return whether the network should kill tasks that are repeating their outputs
	 */
	boolean allowsRepeating();
	
	/**
	 * Implement this to allow GUI attaching. GUI can be attached at any moment
	 * @param objects Objects that are going to be represented on the GUI
	 * @param listener A window listener for the new window. Null if not used
	 * @param width How many GUIs will be places horizontally
	 * @param height How many GUIs will be placed vertically
	 * @param scale Scale of the GUIs
	 */
	void attachGUI(Object[] objects, WindowListener listener, int width, int height, float scale);
	
	/**
	 * @return A panel that will be placed in the GUI. It can be used to set task-specific settings
	 */
	JPanel getTaskPanel();
}

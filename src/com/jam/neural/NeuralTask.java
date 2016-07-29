package com.jam.neural;

/**
 *	The task (or in this case the game) that the neural network is going to be processing, has to implement this
 */
public interface NeuralTask {
	static NeuralTask getInstance() {
		return null;
	}
	float[] getInputs();
	void setOutputs(float[] outputs);
	int getNumOutputs();
	int getNumInputs();
	int getFitness();
	TaskState getTaskState();
	boolean isBinary();
}

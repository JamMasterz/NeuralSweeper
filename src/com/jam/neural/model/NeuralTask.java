package com.jam.neural.model;

/**
 *	The task that the neural network is going to be processing, has to implement this
 */
public interface NeuralTask {
	float[] getInputs();
	void reset();
	void setOutputs(float[] outputs);
	int getNumOutputs();
	int getNumInputs();
	int getFitness();
	TaskState getTaskState();
	void setTaskState(TaskState state);
	boolean isBinary();
}

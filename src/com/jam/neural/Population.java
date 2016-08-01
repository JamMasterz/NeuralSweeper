package com.jam.neural;

import java.util.Random;

public class Population{
	private Genome[] genomes;
	private NeuralTask[] tasks;
	private int generationNumber;
	private int ticksLeft;
	
	public Population(NeuralTask[] tasks, int numGenomes, int maxTicksPerGen, int numHiddenLayers, int numNodesPerHiddenLayer){
		if (numGenomes < 1) throw new IllegalArgumentException("The number of genomes must be greater than 1");
		if (tasks.length != numGenomes) throw new IllegalArgumentException("The amount of NeuralTasks has to be the same as number of genomes");
		
		this.generationNumber = 1;
		this.ticksLeft = maxTicksPerGen;
		this.genomes = new Genome[numGenomes];
		this.tasks = tasks;
		
		for (int i = 0; i < numGenomes; i++){
			genomes[i] = new Genome(tasks[i].getNumInputs(), numHiddenLayers, numNodesPerHiddenLayer, tasks[i].getNumOutputs());
		}
	}
	
	/**
	 * Uses the neural network to progress in the task 1 step
	 */
	public void tickGeneration(){
		for (int i = 0; i < genomes.length; i++){
			TaskState state = tasks[i].getTaskState();
			if (state == TaskState.PROCESSING){ //tick it only if it hasnt lost or won
				tasks[i].setOutputs(genomes[i].evalutateNetwork(tasks[i].getInputs(), tasks[i].isBinary()));
			}
		}
		ticksLeft--;
	}
	
	/**
	 * @return Whether the generation is done processing. They either succeeded, failed or have been running for too long
	 */
	public boolean isGenerationDone(){
		if (ticksLeft <= 0) return true;
		for (NeuralTask task: tasks){
			if (task.getTaskState() == TaskState.PROCESSING){
				return false;
			}
		}
		return true;
	}
	
	public void initNewGeneration(){
		generationNumber++;
		
		populateWithChildren();
		for (int i = 0; i < tasks.length; i++){
			tasks[i].reset();
		}
	}
	
	private void populateWithChildren(){
		Genome[] children = new Genome[genomes.length];
		
		System.out.println("fitness : " + getTotalFitness());
		int[] probabilityArray = new int[getTotalFitness()];
		Random r = new Random();
		int nextIndex = 0;
		for (int i = 0; i < tasks.length; i++) {
			for (int j = 0; j < tasks[i].getFitness(); j++) {
				probabilityArray[nextIndex++] = i;
			}
		}
		for (int i = 0; i < children.length; i++) {
			Genome parent1 = genomes[probabilityArray[r.nextInt(probabilityArray.length)]];
			Genome parent2 = genomes[probabilityArray[r.nextInt(probabilityArray.length)]];
			children[i] = parent1.getOffspringGenome(parent2);
		}
	}
	
	public int getTotalFitness(){
		int fitness = 0;
		
		for (int i = 0; i < tasks.length; i++) {
			fitness += tasks[i].getFitness();
		}
		
		return fitness;
	}
	
	public int getGeneration(){
		return this.generationNumber;
	}
}

package com.jam.neural.model;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.jam.neural.model.NeuralTask.TaskState;

public class Population{
	private Genome[] genomes;
	private NeuralTask[] tasks;
	
	private ExecutorService service;
	private ArrayList<Callable<Void>> tickTasks;
	
	private int generationNumber;
	private int ticksLeft;
	private final int maxTicksPerGen;
	private boolean allowRepeating;
	
	public Population(NeuralTask[] tasks, int numGenomes, int maxTicksPerGen, int numHiddenLayers, int numNodesPerHiddenLayer, boolean allowRepeating){
		if (numGenomes < 1) throw new IllegalArgumentException("The number of genomes must be greater than 1");
		if (tasks.length != numGenomes) throw new IllegalArgumentException("The amount of NeuralTasks has to be the same as number of genomes");
		
		this.generationNumber = 1;
		this.ticksLeft = maxTicksPerGen;
		this.genomes = new Genome[numGenomes];
		this.tasks = tasks;
		this.allowRepeating = allowRepeating;
		this.maxTicksPerGen = maxTicksPerGen;
		
		for (int i = 0; i < numGenomes; i++){
			genomes[i] = new Genome(tasks[i].getNumInputs(), numHiddenLayers, numNodesPerHiddenLayer, tasks[i].getNumOutputs());
		}
	}
	
	/**
	 * This initializes the internal threadpool. This can only be done once.
	 * @param numThreads
	 */
	public void createThreadPool(int numThreads){
		if (service == null){
			service = Executors.newFixedThreadPool(numThreads);
			
			tickTasks = new ArrayList<>();
			for (int i = 0; i < genomes.length; i++) {
				tickTasks.add(getTickGenomeCallable(i));
			}
		}
	}
	
	/**
	 * Uses the neural network to progress in the task 1 step
	 * @return Whether this tick resulted in a repopulation
	 */
//	public boolean tickGeneration(){
//		if (!isGenerationDone()){
//			int ticking = 0;
//			for (int i = 0; i < genomes.length; i++){
//				if (tasks[i].getTaskState() == TaskState.PROCESSING){ //tick it only if it hasnt lost or won
//					float[] outputs = genomes[i].evalutateNetwork(tasks[i].getInputs(), tasks[i].isBinary());
//
//					if (!allowRepeating && genomes[i].isRepeating()){
//						//System.out.println("Killing braindead genome");
//						tasks[i].setTaskState(TaskState.FAILED);
//						continue;
//					}
//
//					ticking++;
//					tasks[i].setOutputs(outputs);
//				}
//			}
//
//
//
//			ticksLeft--;
//
//			System.out.println("Still ticking: " + ticking);
//
//			return false;
//		} else {
//			initNewGeneration();
//
//			return true;
//		}
//	}
	
	/**
	 * Uses the neural network to progress in the task 1 step. The neural network is evaluated using multiple threads.
	 * @return Whether this tick resulted in a repopulation
	 * @throws Exception If the threadPool hasnt been initialized. Call createThreadPool(...) to fix it
	 */
	public boolean tickGenerationMultiThreaded() throws InterruptedException {
		if (service == null) throw new RuntimeException("The ExecutorService has not been initialized. Call createThreadPool(...)");
		
		if (!isGenerationDone()){
			service.invokeAll(tickTasks); //Will block until all are done
			//TODO: InterruptedException when closing the window
			ticksLeft--;
			
			return false;
		} else {
			initNewGeneration();
			
			return true;
		}
	}
	
	/**
	 * @return Whether the generation is done processing. They either succeeded, failed or have been running for too long
	 */
	public boolean isGenerationDone(){
		if (ticksLeft <= 0){
			return true;
		}
		for (NeuralTask task: tasks){
			if (task.getTaskState() == TaskState.PROCESSING){
				return false;
			}
		}
		return true;
	}
	
	//TODO: Maybe split this into threads too
	public void initNewGeneration(){
		generationNumber++;
		ticksLeft = maxTicksPerGen;
		
		populateWithChildren();
		for (int i = 0; i < tasks.length; i++){
			tasks[i].reset();
		}
	}
	
	private void populateWithChildren(){
		Genome[] children = new Genome[genomes.length];
		
		//int totalFitness = getTotalFitness();
		//System.out.println("fitness : " + totalFitness);
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
		
		genomes = children;
	}
	
	private Callable<Void> getTickGenomeCallable(int index){
		return () -> {
			if (tasks[index].getTaskState() == TaskState.PROCESSING){ //tick it only if it hasnt lost or won
				float[] outputs = genomes[index].evalutateNetwork(tasks[index].getInputs(), tasks[index].isBinary());

				if (!allowRepeating && genomes[index].isRepeating()){
					//System.out.println("Killing braindead genome");
					tasks[index].setTaskState(TaskState.FAILED);
					return null;
				}

				tasks[index].setOutputs(outputs);
			}

			return null;
		};
	}
	
	public int getTotalFitness(){
		int fitness = 0;
		
		for (int i = 0; i < tasks.length; i++) {
			fitness += tasks[i].getFitness();
		}
		
		return fitness;
	}
	
	public int getAverageFitness(){
		return getTotalFitness() / tasks.length;
	}
	
	public int getBestFitness(){
		int best = -1;
		
		for (int i = 0; i < tasks.length; i++) {
			int fitness = tasks[i].getFitness();
			if (fitness > best) best = fitness;
		}
		
		return best;
	}
	
	public int getGeneration(){
		return this.generationNumber;
	}
}

package com.jam.neural.model;

import java.util.Random;

import com.jam.util.Util;

public class NeuralNode {
	private float[] weights;
	private float threshold;
	private int numInputs;
	
	/**
	 * Generates a node with random weights between -1 and 1, and random threshold between -1 and 1
	 * @param numInputs Number of inputs into this node
	 */
	public NeuralNode(int numInputs){
		if (numInputs < 0) throw new IllegalArgumentException("Number of inputs to a node must be greater than 0");
		
		Random r = new Random();
		
		this.numInputs = numInputs;
		this.threshold = Util.getRandomFloat(r, -1, 1);
		this.weights = new float[numInputs];
		for (int i = 0; i < numInputs; i++){
			weights[i] = Util.getRandomFloat(r, -1, 1); //Center it around 0
		}
	}
	
	/**
	 * Creates a node with provided weights and threshold
	 * @param weights Weights
	 * @param threshold Threshold
	 */
	public NeuralNode(float[] weights, float threshold){
		if (weights.length < 0) throw new IllegalArgumentException("Number of inputs must be greater than 0");
		
		this.weights = weights;
		this.threshold = threshold;
		this.numInputs = weights.length;
	}
	
	/**
	 * Creates a node from the genes inside a chromosome.
	 * @param chromosome All the genes in a genome as a float array
	 * @param start Start index of the genes for this node
	 * @param numInputs Number of inputs into this node
	 */
	public NeuralNode(float[] chromosome, int start, int numInputs){
		this.numInputs = numInputs;
		this.weights = new float[numInputs];
		
		setGene(chromosome, start);
	}
	
	/**
	 * Creates a copy of a node
	 * @param node Node to copy
	 */
	public NeuralNode(NeuralNode node){
		this.weights = node.weights;
		this.threshold = node.threshold;
		this.numInputs = node.numInputs;
	}
	
	/**
	 * @return Number of inputs into this node
	 */
	public int getNumInputs(){
		return numInputs;
	}
	
	/**
	 * @return Threshold of this node
	 */
	public float getThreshold(){
		return threshold;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		
		builder.append("Thr: " + threshold + ", W:");
		for (int i = 0; i < weights.length; i++){
			builder.append(" " + weights[i] + ",");
		}
		builder.deleteCharAt(builder.length() - 1); //comma
		
		return builder.toString();
	}
	
	/**
	 * @return Array of all the weights and the threshold at the end
	 */
	protected float[] getGene(){
		float[] output = new float[getGeneSize()];
		
		System.arraycopy(weights, 0, output, 0, weights.length);
		output[output.length - 1] = threshold;
		
		return output;
	}
	
	/**
	 * Sets the genes of this node from the given chromosome
	 * @param chromosome All the genes in a genome as a float array
	 * @param start Start index of the genes for this node
	 */
	protected void setGene(float[] chromosome, int start){
		System.arraycopy(chromosome, start, weights, 0, weights.length);
		threshold = chromosome[start + weights.length];
	}
	
	/**
	 * @return The side of a gene from this node
	 */
	protected int getGeneSize(){
		return weights.length + 1;
	}
	
	/**
	 * Evaluates this node. All the inputs are multiplied with their weight and added together.
	 * Then the threshold is subtracted from the sum. Then output is calculated depending on whether the node is binary or not
	 * @param inputs Float array of inputs into this node. This must be the same size as numInputs
	 * @param binary If the node is binary. If it is, it can only output 1 or 0. Otherwise, the output is the sum of all the
	 * inputs, multiplies with their weights, minus the threshold, ran through a Sigmond function.
	 * @return Output from this node
	 */
	public float evaluateNode(float[] inputs, boolean binary){
		if (inputs.length != weights.length) throw new IllegalArgumentException("The size of input doesnt match the amount of weights");
		
		float sum = 0;
		for (int i = 0; i < weights.length; i++){
			sum += inputs[i] * weights[i];
		}
		sum -= threshold;
		
		//If the sum of all inputs is greater than the threshold, output binary 1
		if (binary){
			return (sum > 0) ? 1 : 0;
		} else {
			return Util.sigmond(sum);
		}
	}
}

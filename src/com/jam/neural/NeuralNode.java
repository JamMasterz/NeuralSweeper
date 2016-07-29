package com.jam.neural;

import java.util.Random;

import com.jam.util.Util;

public class NeuralNode {
	private float[] weights;
	private float threshold;
	private int numInputs;
	
	
	public NeuralNode(int numInputs){
		if (numInputs < 1) throw new IllegalArgumentException("Number of inputs to a node must be greater than 0");
		
		Random r = new Random();
		
		threshold = Util.getRandomFloat(r, -1, 1);
		weights = new float[numInputs];
		for (int i = 0; i < numInputs; i++){
			weights[i] = Util.getRandomFloat(r, -1, 1); //Center it around 0
		}
	}
	
	public NeuralNode(float[] weights, float threshold){
		if (weights.length == 0) throw new IllegalArgumentException("Number of inputs must be greater than 0");
		
		this.weights = weights;
		this.threshold = threshold;
		this.numInputs = weights.length;
	}
	
	public NeuralNode(NeuralNode node){
		this.weights = node.weights;
		this.threshold = node.threshold;
		this.numInputs = node.numInputs;
	}
	
	protected float[] getWeights(){
		return weights;
	}
	
	public int getNumInputs(){
		return numInputs;
	}
	
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
	
	protected int getGeneSize(){
		return weights.length + 1;
	}
	
	protected float evaluateNode(float[] inputs, boolean binary){
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

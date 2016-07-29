package com.jam.neural;

import java.util.Random;

import com.jam.util.Util;

public class NeuralNode {
	private float[] weights;
	private float threshold;
	private int numInputs;
	
	
	public NeuralNode(int numInputs){
		if (numInputs == 0) throw new IllegalArgumentException("Number of inputs to a node must be greater than 0");
		
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
	
	protected float evaluateNode(float[] inputs){
		if (inputs.length != weights.length) throw new IllegalArgumentException("The size of input doesnt match the amount of weights");
		
		float sum = 0;
		for (int i = 0; i < weights.length; i++){
			sum += inputs[i] * weights[i];
		}
		sum -= threshold;
	
		return Util.sigmond(sum);
	}
}

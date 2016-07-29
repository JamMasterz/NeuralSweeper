package com.jam.neural;

public class NodeLayer {
	private NeuralNode[] nodes;
	private int numInputsPerNode;
	
	public NodeLayer(int numNodes, int numInputsPerNode){
		if (numNodes == 0) throw new IllegalArgumentException("The number of nodes in a layer must be greater than 0");
		this.numInputsPerNode = numInputsPerNode;
		
		for (int i = 0; i < numNodes; i++){
			nodes[i] = new NeuralNode(numInputsPerNode);
		}
	}
	
	/**
	 * Given some inputs, it will return outputs from all the nodes in this row.
	 * Note that each single input is connected to every node.
	 * @param inputs Inputs
	 * @return Output values from every node in this layer. Can be used as inputs to next layer
	 */
	protected float[] evaluateLayer(float[] inputs){
		if (numInputsPerNode != inputs.length) throw new IllegalArgumentException("The amount of inputs don't match this layers node configuration");
		
		float[] output = new float[nodes.length];
		
		for (int i = 0; i < nodes.length; i++){
			output[i] = nodes[i].evaluateNode(inputs);
		}
		
		return output;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		
		builder.append("Layer: ");
		for (NeuralNode node : nodes){
			builder.append('\t');
			builder.append(node.toString());
			builder.append('\n');
		}
		builder.deleteCharAt(builder.length() - 1); //newline
		
		return builder.toString();
	}
}

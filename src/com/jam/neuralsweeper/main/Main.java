package com.jam.neuralsweeper.main;

import com.jam.neural.controller.NeuralNetwork;

public class Main {

	public static void main(String[] args) {
		new NeuralNetwork(new NeuralSweeperSetup());
//		new NeuralNetwork(new RunnerGameSetup(1500, 1000));
	}
}

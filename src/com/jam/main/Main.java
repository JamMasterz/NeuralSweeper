package com.jam.main;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Main {

	public static void main(String[] args) {
		JFrame frame = new JFrame("Minesweeper Neural Network");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(670, 450);
		frame.setResizable(false);
		JPanel mainPanel = new MainPanel();
		frame.add(mainPanel);
		
		//frame.pack();
		frame.setVisible(true);
	}
}

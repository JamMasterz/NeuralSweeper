package com.jam.main;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.jam.neural.Generation;

public class Main {
	static final int MAX_MOVES_TO_SOLVE_EASY = 200;
	static boolean generated = false;
	static boolean running = false;
	static Generation generation;

	public static void main(String[] args) {
		MainFrame mainFrame = new MainFrame();
		mainFrame.setSize(670, 450);
		mainFrame.setVisible(true);
		
		mainFrame.setStartActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (!generated){
					generation = new Generation<>(mainFrame.getNumSpecimens(), MAX_MOVES_TO_SOLVE_EASY, mainFrame.getNumHiddenLayers(), mainFrame.getNodesPerLayer());
					
					generated = true;
				}
			}
		});
		mainFrame.setStopActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
			}
		});
		mainFrame.setAttachActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
			}
		});
		mainFrame.setShowGraphsActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
			}
		});
	}
}

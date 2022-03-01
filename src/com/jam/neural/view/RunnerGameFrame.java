package com.jam.neural.view;

import com.jam.runner.view.RunnerGameGUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class RunnerGameFrame {
	private JFrame frame;
	private RunnerGameGUI gui;

	public RunnerGameFrame(RunnerGameGUI gui, int width, int height){
		this.gui = gui;
		frame = new JFrame("Game Boards");
		frame.setResizable(false);
		frame.add(gui);
		frame.setVisible(true);
		frame.setSize(new Dimension(width, height));
		frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
	}

	public void setWindowListener(WindowListener list){
		frame.addWindowListener(list);
	}
	
	public void closeWindow(){
		gui.disconnect();
		frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
	}
}

package com.jam.minesweeper.view;

import com.jam.neural.view.Timable;

public class TimerThread extends Thread{
	private Timable gui;

	public TimerThread(Timable gui) {
		this.gui = gui;
	}

	@Override
	public void run() {
		while (!Thread.interrupted()){
			gui.updateTime();
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				return;
			}
		}
	}
}

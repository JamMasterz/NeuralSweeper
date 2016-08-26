package com.jam.game.view;

public class TimerThread extends Thread{
	private MinesweeperGUI gui;

	public TimerThread(MinesweeperGUI gui) {
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

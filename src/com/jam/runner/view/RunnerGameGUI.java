package com.jam.runner.view;

import com.jam.minesweeper.view.TimerThread;
import com.jam.neural.view.Timable;
import com.jam.runner.controller.RunnerController;
import com.jam.runner.model.Board;
import com.jam.runner.model.Player;
import com.jam.runner.model.Wall;

import javax.swing.*;
import java.awt.*;
import java.util.Observable;
import java.util.Observer;

public class RunnerGameGUI extends JPanel implements Timable, Observer {
	private RunnerController game;
	private TimerThread timer;

	@SuppressWarnings("unused")
	private boolean debug = false;

	public RunnerGameGUI(RunnerController game){
		this.game = game;
		this.timer = new TimerThread(this);


		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setPreferredSize(new Dimension(game.getBoard().getWidth(), game.getBoard().getHeight()));
		setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));

		timer.start();
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		Board board = game.getBoard();
		int width = game.getBoard().getWidth();
		int height = game.getBoard().getHeight();

		for (int i = 0; i < board.getNumPlayers(); i++){
			Player player = board.getPlayer(i);
			if (player == null) {
				//Probably some multithreading issue
				player = board.getPlayer(i);
			}
			g.setColor(player.getColor());
			g.fillRect(player.getX(), player.getY(), 4, 4);
		}

		for (Wall w : board.getWalls()) {
			g.setColor(Color.RED);
			g.fillRect(w.getStartX(), w.getStartY(), 2, w.getLength());
		}

		g.setColor(Color.BLACK);
		g.fillRect(board.getDeathWall(), 0, 2, height);
	}
	
	public void setDebug(boolean debug){
		this.debug = debug;
	}
	
	@Override
	public void updateTime(){ }
	
	protected void stopTimerThread(){
		if (!timer.isInterrupted()){
			System.out.println("Interrupting");
			timer.interrupt();
		}
	}
	
	public void disconnect(){
		stopTimerThread();
	}
	
	protected void restartTimerThread(){
		if (timer.isInterrupted()){
			timer = new TimerThread(this);
			timer.start();
		}
	}

	public void updateGUI() {
		repaint();
	}

	@Override
	public void update(Observable o, Object arg) {
		updateGUI();
	}
}

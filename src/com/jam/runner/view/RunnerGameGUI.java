package com.jam.runner.view;

import com.jam.minesweeper.view.TimerThread;
import com.jam.runner.controller.Game;
import com.jam.runner.model.Board;
import com.jam.runner.model.Player;
import com.jam.neural.view.Timable;
import com.jam.neural.view.Updatable;

import javax.swing.*;
import java.awt.*;

public class RunnerGameGUI implements Timable, Updatable {
	private Game game;
	private JPanel panel;
	private Canvas canvas;
	private TimerThread timer;

	@SuppressWarnings("unused")
	private boolean debug = false;

	public RunnerGameGUI(Game game){
		this.game = game;
		this.timer = new TimerThread(this);
		timer.start();
	}
	
	public JPanel getGUI(){
		if (panel == null){
			canvas = new Canvas() {
				@Override
				public void paint(Graphics g) {
					super.paint(g);
					canvas.setBackground(Color.WHITE);

					Board board = game.getBoard();
					for (int i = 0; i < board.getNumPlayers(); i++){
						Player player = board.getPlayer(i);
						g.setColor(player.getColor());
						g.fillRect(player.getX(), player.getY(), 4, 4);
					}
				}
			};
			canvas.setBackground(Color.WHITE);

			panel = new JPanel();
			panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
			panel.add(canvas);
			panel.setPreferredSize(new Dimension(game.getBoard().getWidth(), game.getBoard().getHeight()));
			panel.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
		}

		return panel;
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

	@Override
	public void updateGUI(int index) {
		System.out.println("Updating ui " + index + " numm players " + game.getBoard().getNumPlayers());
		if (index == game.getBoard().getNumPlayers() - 1) {
//		panel.repaint();
//		panel.revalidate();
			canvas.repaint();
//		canvas.revalidate();
		}
	}
}

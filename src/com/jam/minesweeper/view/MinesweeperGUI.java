package com.jam.minesweeper.view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.jam.minesweeper.controller.MinesweeperController;
import com.jam.minesweeper.model.Board;
import com.jam.minesweeper.model.Board.UncoverResult;
import com.jam.minesweeper.model.Coord;
import com.jam.minesweeper.model.Field;
import com.jam.neural.view.Timable;

public class MinesweeperGUI extends JPanel implements Timable, Observer {
	public static final String RESET = "\u263A";
	private static final int DISPLAY_HEIGHT = 20;
	private static final int DISPLAY_WIDTH = 50;
	private static final int RESET_SIZE = 40;
	private static final int FONT_SIZE = 30;
	
	private MinesweeperController game;
	private ArrayList<JMineField> fields;
	private MinefieldActionListener listener;
	private TimerThread timer;
	
	private JLabel bombsLeft;
	private JLabel elapsedTime;
	private JButton resetButton;
	@SuppressWarnings("unused")
	private boolean debug = false;
	private boolean controllable;
	private double scale;
	
	public MinesweeperGUI(MinesweeperController game, double scale, boolean controllable){
		this.game = game;
		this.fields = new ArrayList<>();
		this.scale = scale;
		this.controllable = controllable;
		this.timer = new TimerThread(this);

		if (controllable){
			listener = new MinefieldActionListener(fields, game, this);
		} else {
			listener = null;
		}

		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		add(getTopPanel());
		add(getGridPanel());
		setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));

		timer.start();
	}
	
	public void setDebug(boolean debug){
		this.debug = debug;
	}
	
	private JPanel getTopPanel(){
		Font font = new Font("Arial Black", Font.BOLD, (int) (FONT_SIZE * scale));
		
		JPanel top = new JPanel();
		top.setLayout(new BoxLayout(top, BoxLayout.X_AXIS));
		
		bombsLeft = new JLabel(Integer.toString(game.getBoard().getBombsLeft()));
		bombsLeft.setPreferredSize(new Dimension((int) (DISPLAY_WIDTH * scale), (int) (DISPLAY_HEIGHT * scale)));
		bombsLeft.setFont(font);
		bombsLeft.setForeground(Color.RED);
		bombsLeft.setBackground(Color.LIGHT_GRAY);
		bombsLeft.setAlignmentX(Component.RIGHT_ALIGNMENT);
		
		elapsedTime = new JLabel("0");
		elapsedTime.setPreferredSize(new Dimension((int) (DISPLAY_WIDTH * scale), (int) (DISPLAY_HEIGHT * scale)));
		elapsedTime.setFont(font);
		elapsedTime.setForeground(Color.RED);
		elapsedTime.setBackground(Color.LIGHT_GRAY);
		elapsedTime.setAlignmentX(Component.RIGHT_ALIGNMENT);
		
		resetButton = new JButton(RESET);
		resetButton.setPreferredSize(new Dimension((int) (RESET_SIZE * scale), (int) (RESET_SIZE * scale)));
		resetButton.setFont(font);
		resetButton.setHorizontalAlignment(SwingConstants.CENTER);
		resetButton.setVerticalTextPosition(SwingConstants.CENTER);
		if (controllable){
			resetButton.addActionListener(e -> {
				game.getBoard().restartGame(null);
				restartTimerThread();
				updateGUI();
			});
		}
		
		top.add(bombsLeft);
		top.add(resetButton);
		top.add(elapsedTime);
		
		return top;
	}
	
	private JPanel getGridPanel(){
		Board board = game.getBoard();
		JPanel grid = new JPanel(new GridLayout(board.getSize(), board.getSize()));
		
		for (int i = 0; i < board.getSize(); i++){
			for (int j = 0; j < board.getSize(); j++){
				JMineField field = new JMineField(board.getField(i, j), scale);
				field.setHorizontalAlignment(JMineField.CENTER);
				field.addMouseListener(listener);
				
				fields.add(field);
				grid.add(field);
			}
		}
		
		return grid;
	}
	
	public void displayGameState(UncoverResult res){
		if (res == UncoverResult.MINE){
			JOptionPane.showMessageDialog(this, "You lost!");
		} else if (res == UncoverResult.VICTORY){
			JOptionPane.showMessageDialog(this, "You won!");
		}
	}

	@Override
	public void updateTime(){
		if (elapsedTime != null){
			elapsedTime.setText(Integer.toString(game.getBoard().getSecondsElapsed()));
		}
	}
	
	protected void stopTimerThread(){
		if (!timer.isInterrupted()){
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
		Field field;
		Board board = game.getBoard();

		for (int i = 0; i < fields.size(); i++){
			field = board.getField(Coord.getCoord(i, game.getSize()));
			if (!fields.get(i).equals(field)){
				fields.get(i).setFieldType(field);
			}
		}
		repaint();
		revalidate();

		bombsLeft.setText(Integer.toString(game.getBoard().getBombsLeft()));
	}

	@Override
	public void update(Observable o, Object arg) {
		updateGUI();
	}
}

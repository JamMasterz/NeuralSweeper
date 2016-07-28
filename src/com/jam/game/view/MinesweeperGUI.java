package com.jam.game.view;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.jam.game.controller.Game;
import com.jam.game.model.Board;

public class MinesweeperGUI {
	public static final String RESET = "\u263A";
	private static final int DISPLAY_HEIGHT = 20;
	private static final int DISPLAY_WIDTH = 50;
	private static final int RESET_SIZE = 40;
	
	private Game game;
	private JPanel panel;
	private ArrayList<JMineField> fields;
	
	private JTextField bombsLeft;
	private JTextField elapsedTime;
	private JButton resetButton;
	
	public MinesweeperGUI(Game game){
		this.game = game;
	}
	
	public JPanel getGUI(){
		if (panel == null){
			panel = new JPanel();
			panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
			panel.add(getTopPanel());
			panel.add(getGridPanel());
		}
		
		return panel;
	}
	
	private JPanel getTopPanel(){
		Font font = new Font("Arial Black", Font.BOLD, JMineField.FONT_SIZE);
		
		JPanel top = new JPanel();
		top.setLayout(new BoxLayout(top, BoxLayout.X_AXIS));
		
		bombsLeft = new JTextField();
		bombsLeft.setPreferredSize(new Dimension(DISPLAY_WIDTH, DISPLAY_HEIGHT));
		bombsLeft.setFont(font);
		bombsLeft.setAlignmentX(Component.LEFT_ALIGNMENT);
		
		elapsedTime = new JTextField();
		elapsedTime.setPreferredSize(new Dimension(DISPLAY_WIDTH, DISPLAY_HEIGHT));
		elapsedTime.setFont(font);
		elapsedTime.setAlignmentX(Component.RIGHT_ALIGNMENT);
		
		resetButton = new JButton(RESET);
		resetButton.setPreferredSize(new Dimension(RESET_SIZE, RESET_SIZE));
		resetButton.setFont(font);
		resetButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		
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
				JMineField field = new JMineField(board.getField(i, j));
				field.setHorizontalAlignment(JMineField.CENTER);
				
				fields.add(field);
				grid.add(field);
			}
		}
		
		return grid;
	}
}

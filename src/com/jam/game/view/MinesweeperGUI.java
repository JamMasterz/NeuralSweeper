package com.jam.game.view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.jam.game.controller.Game;
import com.jam.game.model.Board;
import com.jam.game.model.Coord;
import com.jam.game.model.Field;
import com.jam.game.model.UncoverResult;

public class MinesweeperGUI {
	public static final String RESET = "\u263A";
	private static final int DISPLAY_HEIGHT = 20;
	private static final int DISPLAY_WIDTH = 50;
	private static final int RESET_SIZE = 40;
	private static final int FONT_SIZE = 30;
	
	private Game game;
	private JPanel panel;
	private ArrayList<JMineField> fields;
	private MinefieldActionListener listener;
	
	private JLabel bombsLeft;
	private JLabel elapsedTime;
	private JButton resetButton;
	
	public MinesweeperGUI(Game game){
		this.game = game;
		this.fields = new ArrayList<JMineField>();
	}
	
	public JPanel getGUI(){
		if (panel == null){
			listener = new MinefieldActionListener(fields, game);
			
			panel = new JPanel();
			panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
			panel.add(getTopPanel());
			panel.add(getGridPanel());
		}
		
		return panel;
	}
	
	private JPanel getTopPanel(){
		Font font = new Font("Arial Black", Font.BOLD, FONT_SIZE);
		
		JPanel top = new JPanel();
		top.setLayout(new BoxLayout(top, BoxLayout.X_AXIS));
		
		bombsLeft = new JLabel(Integer.toString(game.getBoard().getBombsLeft()));
		bombsLeft.setPreferredSize(new Dimension(DISPLAY_WIDTH, DISPLAY_HEIGHT));
		bombsLeft.setFont(font);
		bombsLeft.setForeground(Color.RED);
		bombsLeft.setBackground(Color.LIGHT_GRAY);
		bombsLeft.setAlignmentX(Component.RIGHT_ALIGNMENT);
		
		elapsedTime = new JLabel("0");
		elapsedTime.setPreferredSize(new Dimension(DISPLAY_WIDTH, DISPLAY_HEIGHT));
		elapsedTime.setFont(font);
		elapsedTime.setForeground(Color.RED);
		elapsedTime.setBackground(Color.LIGHT_GRAY);
		elapsedTime.setAlignmentX(Component.RIGHT_ALIGNMENT);
		
		resetButton = new JButton(RESET);
		resetButton.setPreferredSize(new Dimension(RESET_SIZE, RESET_SIZE));
		resetButton.setFont(font);
		resetButton.setHorizontalAlignment(SwingConstants.CENTER);
		resetButton.setVerticalTextPosition(SwingConstants.CENTER);
		resetButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				game.getBoard().restartGame();
				updateBoard();
			}
		});
		
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
				field.addMouseListener(listener);
				
				fields.add(field);
				grid.add(field);
			}
		}
		
		return grid;
	}
	
	public void updateBoard(){
		Field field;
		Board board = game.getBoard();
		
		for (int i = 0; i < fields.size(); i++){
			field = board.getField(Coord.getCoord(i, game.getSize()));
			if (!fields.get(i).equals(field)){
				fields.get(i).setFieldType(field);
				//probably isnt gonna sync automatically
			}
		}
		
		bombsLeft.setText(Integer.toString(game.getBoard().getBombsLeft()));
	}
	
	public void displayGameState(UncoverResult res){
		if (res == UncoverResult.MINE){
			JOptionPane.showMessageDialog(panel, "You lost!");
		} else if (res == UncoverResult.VICTORY){
			JOptionPane.showMessageDialog(panel, "You won!");
		}
	}
}

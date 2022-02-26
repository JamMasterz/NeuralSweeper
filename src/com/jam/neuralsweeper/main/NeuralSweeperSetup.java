package com.jam.neuralsweeper.main;

import java.awt.event.WindowListener;
import java.util.Observable;
import java.util.Observer;
import java.util.Random;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.border.TitledBorder;

import com.jam.minesweeper.controller.GameController;
import com.jam.minesweeper.controller.GameController.DefaultGamePreference;
import com.jam.neural.controller.NeuralMinesweeper;
import com.jam.neural.model.NeuralTask;
import com.jam.neural.model.TaskSetup;
import com.jam.neural.view.GameArrayFrame;
import com.jam.neural.view.Updatable;

public class NeuralSweeperSetup implements TaskSetup, Observer {
	public static final int MAX_MOVES_TO_SOLVE_EASY = 200;
	public static final int VISIBLE_SQUARE_SIZE = 7;
	public static final int SPAWN_X = 4;
	public static final int SPAWN_Y = 5;
	
	private static JPanel panel;
	private static JRadioButton radioEasy, radioMedium, radioHard;
	private static ButtonGroup radioGroup;
	
	private GameArrayFrame gui;
	
	public NeuralSweeperSetup() {
		panel = new JPanel();
		panel.setBorder(new TitledBorder("Minesweeper Settings"));
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
		
		radioGroup = new ButtonGroup();
		radioEasy = new JRadioButton("Easy");
		radioMedium = new JRadioButton("Medium");
		radioHard = new JRadioButton("Hard");
		
		radioEasy.setSelected(true);
		radioGroup.add(radioEasy);
		panel.add(radioEasy);
		
		radioMedium = new JRadioButton("Medium");
		radioGroup.add(radioMedium);
		panel.add(radioMedium);
		
		radioHard = new JRadioButton("Hard");
		radioGroup.add(radioHard);
		panel.add(radioHard);
	}
	
	@Override
	public NeuralMinesweeper[] createNeuralTasks(int numTasks) {
		NeuralMinesweeper[] sweepers = new NeuralMinesweeper[numTasks];
		long seed = new Random().nextLong();
		
		for (int i = 0; i < sweepers.length; i++) {
			sweepers[i] = new NeuralMinesweeper(VISIBLE_SQUARE_SIZE, SPAWN_X, SPAWN_Y, seed, getDifficulty());
		}

		gui.addObserver(this);

		return sweepers;
	}

	@Override
	public int getTickCap() {
		return MAX_MOVES_TO_SOLVE_EASY;
	}

	@Override
	public boolean allowsRepeating() {
		return false;
	}

	@Override
	public void attachGUI(NeuralTask[] objects, WindowListener listener, int width, int height, float scale) {
		GameController[] games = new GameController[objects.length];
		for (int i = 0; i < objects.length; i++) {
			games[i] = ((NeuralMinesweeper) objects[i]).getGame();
		}
		
		gui = new GameArrayFrame(games, width, height, scale);
		gui.setWindowListener(listener);;
	}
	
	@Override
	public void detachGUI() {
		gui.closeWidnow();
	}

	@Override
	public JPanel getTaskPanel() {
		return panel;
	}

	private DefaultGamePreference getDifficulty() {
		if (radioEasy.isSelected()){
			return DefaultGamePreference.NOOB;
		} else if (radioMedium.isSelected()){
			return DefaultGamePreference.INTERMEDIATE;
		} else if (radioHard.isSelected()){
			return DefaultGamePreference.EXPERT;
		}
		
		return null;
	}

	@Override
	public void update(Observable o, Object arg) {
		((Updatable) gui).updateGUI(0);
	}
}

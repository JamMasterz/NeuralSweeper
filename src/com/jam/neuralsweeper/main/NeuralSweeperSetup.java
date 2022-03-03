package com.jam.neuralsweeper.main;

import java.awt.event.WindowListener;
import java.util.Random;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.border.TitledBorder;

import com.jam.minesweeper.controller.ArrayMinesweeperController;
import com.jam.minesweeper.controller.MinesweeperController.DefaultGamePreference;
import com.jam.minesweeper.view.ArrayMinesweeperGUI;
import com.jam.neural.controller.NeuralMinesweeper;
import com.jam.neural.model.NeuralTask;
import com.jam.neural.model.TaskSetup;

public class NeuralSweeperSetup implements TaskSetup {
	public static final int MAX_MOVES_TO_SOLVE_EASY = 200;

	private static JPanel panel;
	private static JRadioButton radioEasy, radioMedium, radioHard;
	private static ButtonGroup radioGroup;

	private ArrayMinesweeperController game;
	
	public NeuralSweeperSetup() {
		constructSweeperPanel();
	}

	private void constructSweeperPanel() {
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
		long seed = new Random().nextLong();
		game = new ArrayMinesweeperController(DefaultGamePreference.NOOB, seed, false, numTasks);

		NeuralMinesweeper[] sweepers = new NeuralMinesweeper[numTasks];
		for (int i = 0; i < sweepers.length; i++) {
			sweepers[i] = new NeuralMinesweeper(seed, game.getGameForPlayer(i));
		}

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
		ArrayMinesweeperGUI gui = game.getGUI(scale);

		gui.setWindowListener(listener);
	}
	
	@Override
	public void detachGUI() {
		game.getGUI(1).closeWindow();
	}

	@Override
	public JPanel getTaskPanel() {
		return panel;
	}

	@Override
	public Runnable getTickUpdater() {
		return null;
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
}

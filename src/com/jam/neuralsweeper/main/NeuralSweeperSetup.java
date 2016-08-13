package com.jam.neuralsweeper.main;

import java.awt.event.WindowListener;
import java.util.Random;

import com.jam.game.controller.DefaultGamePreference;
import com.jam.game.controller.Game;
import com.jam.neural.controller.NeuralMinesweeper;
import com.jam.neural.model.TaskSetup;
import com.jam.neural.view.GameArrayFrame;

public class NeuralSweeperSetup implements TaskSetup{
	public static final int MAX_MOVES_TO_SOLVE_EASY = 200;
	public static final int VISIBLE_SQUARE_SIZE = 7;
	public static final int SPAWN_X = 4;
	public static final int SPAWN_Y = 5;
	
	@Override
	public NeuralMinesweeper[] createNeuralTasks(int numTasks) {
		NeuralMinesweeper[] sweepers = new NeuralMinesweeper[numTasks];
		long seed = new Random().nextLong();
		
		for (int i = 0; i < sweepers.length; i++) {
			sweepers[i] = new NeuralMinesweeper(VISIBLE_SQUARE_SIZE, SPAWN_X, SPAWN_Y, seed, DefaultGamePreference.NOOB);
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
	public void attachGUI(Object[] objects, WindowListener listener, int width, int height, float scale) {
		Game[] games = new Game[objects.length];
		for (int i = 0; i < objects.length; i++) {
			games[i] = ((NeuralMinesweeper) objects[i]).getGame();
		}
		
		new GameArrayFrame(games, width, height, scale).setWindowListener(listener);;
	}
}

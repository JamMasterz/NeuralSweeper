package com.jam.game.view;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.SwingUtilities;

import com.jam.game.controller.Game;
import com.jam.game.model.Coord;
import com.jam.game.model.TagResult;
import com.jam.game.model.UncoverResult;

public class MinefieldActionListener extends MouseAdapter{
	private ArrayList<JMineField> fields = new ArrayList<JMineField>();
	private MinesweeperGUI gui;
	private Game game;
	
	public MinefieldActionListener(ArrayList<JMineField> fields, Game game, MinesweeperGUI gui) {
		this.fields = fields;
		this.game = game;
		this.gui = gui;
	}
	
	private Coord getEventCoord(Object src){
		if (!(src instanceof JMineField)){
			throw new IllegalArgumentException("The source must be JMineField");
		}
		
		return Coord.getCoord(fields.indexOf(src), game.getSize());
	}

	@Override
	public void mouseClicked(MouseEvent e) {}

	@Override
	public void mouseEntered(MouseEvent e) {}

	@Override
	public void mouseExited(MouseEvent e) {}

	@Override
	public void mousePressed(MouseEvent e) {}

	@Override
	public void mouseReleased(MouseEvent e) {
		Coord coord = getEventCoord(e.getSource());
		if (SwingUtilities.isLeftMouseButton(e)){
			UncoverResult result = game.leftClickField(coord);
			if (result != UncoverResult.FAILED){
				gui.updateBoard();
			} 
			if (result == UncoverResult.VICTORY || result == UncoverResult.MINE){
				gui.displayGameState(result);
				gui.stopTimerThread();
			}
		} else if (SwingUtilities.isRightMouseButton(e)){
			TagResult result = game.rightClickField(coord);
			if (result == TagResult.FAILED){
				return;
			} else {
				gui.updateBoard();
			}
		}
	}
}

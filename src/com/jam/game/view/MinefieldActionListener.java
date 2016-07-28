package com.jam.game.view;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.SwingUtilities;

import com.jam.game.controller.Game;
import com.jam.game.model.Coord;

public class MinefieldActionListener extends MouseAdapter{
	private ArrayList<JMineField> fields = new ArrayList<JMineField>();
	private Game game;
	
	public MinefieldActionListener(ArrayList<JMineField> fields, Game game) {
		this.fields = fields;
		this.game = game;
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
			System.out.println("Left at " + coord.toString());
			game.leftClickField(coord);
		} else if (SwingUtilities.isRightMouseButton(e)){
			System.out.println("Right at " + coord.toString());
			game.rightClickField(coord);
		}
	}
}

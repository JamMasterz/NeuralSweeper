package com.jam.game.view;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;

import javax.swing.JButton;

import com.jam.game.model.Field;

public class JMineField extends JButton{
	private static final long serialVersionUID = -9053196072545355717L;
	public static final int FIELD_SIZE = 30;
	public static final int FONT_SIZE = 20;
	
	public JMineField(Field field){
		setPreferredSize(new Dimension(FIELD_SIZE, FIELD_SIZE));
		setFont(new Font("Helvetica", Font.BOLD, FONT_SIZE));
		setMargin(new Insets(0, 0, 0, 0));
	}
	
	public void setFieldType(Field field){
		String color = "";
		switch (field){
			case EMPTY:
			case ONE:
				color = "cyan";
				setContentAreaFilled(false);
				setFocusPainted(false);
				break;
			case TWO:
				color = "green";
				setContentAreaFilled(false);
				setFocusPainted(false);
				break;
			case THREE:
				color = "magenta";
				setContentAreaFilled(false);
				setFocusPainted(false);
				break;
			case FOUR:
				color = "blue";
				setContentAreaFilled(false);
				setFocusPainted(false);
				break;
			case FIVE:
				color = "red";
				setContentAreaFilled(false);
				setFocusPainted(false);
				break;
			case SIX:
				color = "orange";
				setContentAreaFilled(false);
				setFocusPainted(false);
				break;
			case SEVEN:
				color = "pink";
				setContentAreaFilled(false);
				setFocusPainted(false);
				break;
			case EIGHT:
				color = "black";
				setContentAreaFilled(false);
				setFocusPainted(false);
				break;
			case MINE:
				color = "black";
				setContentAreaFilled(false);
				setFocusPainted(false);
				break;
			case TAGGED_EMPTY:
			case TAGGED_MINE:
				color = "black";
			case COVERED_EMPTY:
			case COVERED_MINE:
				setContentAreaFilled(true);
				setFocusPainted(true);
				break;
		}
		this.setText("<html><font color=" + color + ">" + field.getChar() + "</font></html>");
	}
}

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
	
	public JMineField(Field field, double scale){
		setPreferredSize(new Dimension((int) (FIELD_SIZE * scale), (int) (FIELD_SIZE * scale)));
		setFont(new Font("Segoe UI Symbol", Font.BOLD, (int) (FONT_SIZE * scale)));
		setMargin(new Insets(0, 0, 0, 0));
	}
	
	public void setFieldType(Field field){
		String color = "";
		switch (field){
			case EMPTY:
			case ONE:
				color = "#3333FF";
				setContentAreaFilled(false);
				setFocusPainted(false);
				break;
			case TWO:
				color = "#009933";
				setContentAreaFilled(false);
				setFocusPainted(false);
				break;
			case THREE:
				color = "#FF0000";
				setContentAreaFilled(false);
				setFocusPainted(false);
				break;
			case FOUR:
				color = "#000099";
				setContentAreaFilled(false);
				setFocusPainted(false);
				break;
			case FIVE:
				color = "#663300";
				setContentAreaFilled(false);
				setFocusPainted(false);
				break;
			case SIX:
				color = "#336699";
				setContentAreaFilled(false);
				setFocusPainted(false);
				break;
			case SEVEN:
				color = "black";
				setContentAreaFilled(false);
				setFocusPainted(false);
				break;
			case EIGHT:
				color = "black";
				setContentAreaFilled(false);
				setFocusPainted(false);
				break;
			case MINE:
				color = "red";
				setContentAreaFilled(false);
				setFocusPainted(false);
				break;
			case TAGGED_EMPTY:
			case TAGGED_MINE:
				color = "black";
				setContentAreaFilled(true);
				setFocusPainted(true);
				break;
			case COVERED_EMPTY:
			case COVERED_MINE:
				setContentAreaFilled(true);
				setFocusPainted(true);
				break;
		}
		this.setText("<html><font color=" + color + ">" + field.getChar() + "</font></html>");
	}
}

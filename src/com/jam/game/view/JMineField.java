package com.jam.game.view;

import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JButton;

import com.jam.game.model.Field;

public class JMineField extends JButton{
	private static final long serialVersionUID = -9053196072545355717L;
	public static final int FIELD_SIZE = 30;
	public static final int FONT_SIZE = 25;
	private Field field;
	
	public JMineField(Field field){
		this.field = field;
		setPreferredSize(new Dimension(FIELD_SIZE, FIELD_SIZE));
		setFont(new Font("Arial Black", Font.BOLD, FONT_SIZE));
	}
	
	public void setFieldType(Field field){
		this.field = field;
		switch (field){
			case EMPTY:
			case ONE:
			case TWO:
			case THREE:
			case FOUR:
			case FIVE:
			case SIX:
			case SEVEN:
			case EIGHT:
			case MINE:
				setEnabled(false);
				break;
			case TAGGED_EMPTY:
			case TAGGED_MINE:
				setEnabled(true);
				break;
		}
		this.setText(field.getChar());
	}
}

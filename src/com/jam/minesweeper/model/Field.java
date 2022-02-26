package com.jam.minesweeper.model;

public enum Field {
	EMPTY(""),
	ONE("1"),
	TWO("2"),
	THREE("3"),
	FOUR("4"),
	FIVE("5"),
	SIX("6"),
	SEVEN("7"),
	EIGHT("8"),
	MINE("\uD83D\uDD25"), //\uD83D\uDCA3
	COVERED_MINE(""),
	COVERED_EMPTY(""),
	TAGGED_MINE("\u2691"),
	TAGGED_EMPTY("\u2691");
	
	private final String ch;
	
	private Field(String ch){
		this.ch = ch;
	}
	
	public String getChar(){
		return this.ch;
	}
	
	public static Field fromOrdinal(int ordinal){
		return Field.values()[ordinal];
	}
}

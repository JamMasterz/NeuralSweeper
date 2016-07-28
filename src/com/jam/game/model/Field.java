package com.jam.game.model;

public enum Field {
	EMPTY, ONE, TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, MINE, TAGGED_MINE, TAGGED_EMPTY;
	
	public static Field fromOrdinal(int ordinal){
		return Field.values()[ordinal];
	}
}

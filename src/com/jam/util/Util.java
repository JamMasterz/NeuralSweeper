package com.jam.util;

import com.jam.game.model.Coord;

public class Util {
	public static boolean contains(Coord[] array, Coord coord){
		for (int i = 0; i < array.length; i++){
			if (array[i].equals(coord)) return true;
		}
		
		return false;
	}
}

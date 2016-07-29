package com.jam.util;

import java.util.Random;

import com.jam.game.model.Coord;

public class Util {
	public static boolean contains(Coord[] array, Coord coord){
		for (int i = 0; i < array.length; i++){
			if (array[i].equals(coord)) return true;
		}
		
		return false;
	}
	
	public static float getRandomFloat(Random r, float low, float high){
		if (r == null){
			r = new Random();
		}
		float interval = Math.abs(low) + Math.abs(high);
		
		return r.nextFloat() * interval + low;
	}
	
	public static float sigmond(float val){
		return (float) (1 / (1 + Math.exp(-val)));
	}
}

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
	
	public static boolean randomDecision(float probability, Random r){
		if (r == null) r = new Random();
		
		return (r.nextFloat() <= probability) ? true : false;
	}
	
	public static void printFloatArr(float[] arr){
		StringBuilder builder = new StringBuilder();
		
		builder.append("[");
		for (int i = 0; i < arr.length; i++) {
			builder.append(arr[i]);
			builder.append(", ");
		}
		builder.deleteCharAt(builder.length() - 1);
		builder.append("]");
		
		System.out.println(builder.toString());
	}
	
	public static boolean equals(float[] arr1, float[] arr2){
		if (arr1 == null && arr2 == null) return false;
		if (arr1 == null ^ arr2 == null) return false;
		if (arr1.length != arr2.length) return false;
		
		for (int i = 0; i < arr1.length; i++) {
			if (arr1[i] != arr2[i]) return false;
		}
		
		return true;
	}
}

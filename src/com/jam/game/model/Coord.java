package com.jam.game.model;

public class Coord {
	private int x, y;
	
	public Coord(int x, int y){
		this.x = x;
		this.y = y;
	}
	
	public Coord(){
		this.x = 0;
		this.y = 0;
	}
	
	public Coord(Coord coord){
		this.x = coord.x;
		this.y = coord.y;
	}
	
	public void set(int x, int y){
		this.x = x;
		this.y = y;
	}
	
	public void set(Coord coord){
		this.x = coord.x;
		this.y = coord.y;
	}
	
	public void add(int dx, int dy){
		this.x += dx;
		this.y += dy;
	}
	
	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}
	
	public static Coord getCoord(int index1D, int size){
		int col = index1D % size;
		int row = index1D / size;
		return new Coord(row, col);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Coord){
			Coord c = (Coord) obj;
			return c.x == this.x && c.y == this.y;
		}
		return false;
	}
	@Override
	public String toString() {
		return "X = " + x + ", Y = " + y;
	}
}

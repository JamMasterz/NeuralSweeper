package com.jam.runner.model;

import java.awt.*;

public class Player {
	private int x, y;
	private Color color;
	
	public Player(int x, int y, Color color){
		this.x = x;
		this.y = y;
		this.color = color;
	}
	
	public Player(){
		this.x = 0;
		this.y = 0;
	}
	
	public Player(Player player){
		this.x = player.x;
		this.y = player.y;
	}
	
	public void set(int x, int y){
		this.x = x;
		this.y = y;
	}
	
	public void set(Player player){
		this.x = player.x;
		this.y = player.y;
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

	public Color getColor() {
		return this.color;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Player){
			Player c = (Player) obj;
			return c.x == this.x && c.y == this.y;
		}
		return false;
	}
	@Override
	public String toString() {
		return "X = " + x + ", Y = " + y;
	}
}

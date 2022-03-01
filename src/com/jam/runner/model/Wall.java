package com.jam.runner.model;

public class Wall {
    private int startX, startY, length;
    private int endX, endY;

    public Wall(int startX, int startY, int length) {
        this.startX = startX;
        this.startY = startY;
        this.length = length;
        this.endX = startX;
        this.endY = startY + length;
    }

    public boolean collides(int x, int y) {
        return x == startX && y >= startY && y <= endY;
    }

    public boolean isCloseTo(int x, int y) {
        return Math.abs(x - startX) < 2 && y >= startY && y <= endY;
    }

    public int getStartX() {
        return startX;
    }

    public int getStartY() {
        return startY;
    }

    public int getLength() {
        return length;
    }
}

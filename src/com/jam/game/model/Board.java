package com.jam.game.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import com.jam.util.Util;

public class Board {
	public static final int SECTOR_SIZE = 3;
	
	private static final int CORNER_NEIGHBOUR_COUNT = 4;
	private static final int EDGE_NEIGHBOUR_COUNT = 6;
	private static final int MIDDLE_NEIGHBOUR_COUNT = 9;
	
	private Field[][] board;
	private int size;
	private int amountBombs;
	private int amountFields;
	private boolean generated = false;
	private boolean lost = false;
	
	public Board(int size, int amountBombs){
		if (size < SECTOR_SIZE + 1){
			throw new IllegalArgumentException("The size must be bigger or equal to 4");
		} else if (amountBombs >= size * size / 3){
			throw new IllegalArgumentException("The amount of bombs must be less than 1/3 of all the fields");
		} else {
			this.size = size;
			this.amountBombs = amountBombs;
			this.amountFields = size * size;
			this.board = new Field[size][size];

			Arrays.fill(board, Field.EMPTY);
		}
	}
	
	public UncoverResult uncoverSingle(Coord coord){
		if (!isWithinBoard(coord)) throw new IllegalArgumentException("The coordinate is not within the board");
		if (!generated) generate(coord);
		if (lost) return UncoverResult.FAILED;
		
		switch (getField(coord)){
			case EMPTY:
				uncoverRecursively(coord);
				return UncoverResult.SUCCESS;
			case ONE:
			case TWO:
			case THREE:
			case FOUR:
			case FIVE:
			case SIX:
			case SEVEN:
			case EIGHT:
			case TAGGED_EMPTY:
			case TAGGED_MINE:
				return UncoverResult.FAILED;
			case MINE:
				lost = true;
				return UncoverResult.MINE;
			default:
				//This will never happen
				return UncoverResult.FAILED;
		}
	}
	
	/**
	 * This will uncover the specified field and all other fields that should be uncovered around coord
	 * @param coord Coordinate of the field being uncovered
	 */
	//TODO: Change name to signify that it also turns some fields into number indicators
	private void uncoverRecursively(Coord coord){
		Coord[] sector = getSectorCoords(coord);
		int bombs = countBombs(sector);
		
		if (bombs == 0){
			//Run this function for all fields in the sector except the source
			for (int i = 0; i < sector.length; i++){
				if (sector[i].equals(coord)) continue;
				uncoverRecursively(sector[i]);
			}
		} else {
			setField(coord, Field.fromOrdinal(bombs));
		}
	}
	
	private void generate(Coord coord){
		ArrayList<Coord> available = createAvailabilityArray(getSectorCoords(coord));
		Random rand = new Random();
		
		for (int i = 0; i < amountBombs; i++){
			int bombIndex = rand.nextInt(available.size());
			setField(available.get(bombIndex), Field.MINE);
		}
	}
	
	private int countBombs(Coord[] coords){
		int bombs = 0;
		
		for (int i = 0; i < coords.length; i++){
			if (getField(coords[i]) == Field.MINE) bombs++;
		}
		
		return bombs;
	}
	
	private Field getField(Coord coord){
		return board[coord.getX()][coord.getY()];
	}
	
	private void setField(Coord coord, Field field){
		board[coord.getX()][coord.getY()] = field;
	}
	
	/**
	 * @param Coordinate
	 * @return Array containing Coords of fields in a 3x3 area centered on coord. Handles cases when there arent fields in some spots
	 */
	private Coord[] getSectorCoords(Coord coord){
		Coord[] coords = new Coord[getSectorSize(coord)];
		int index = 0;
		
		Coord c = new Coord();
		for (int dx = -1; dx < 1; dx++){
			for (int dy = -1; dy < 1; dy++){
				c.set(coord.getX() + dx, coord.getY() + dy);
				if (isWithinBoard(c)){
					coords[index++] = new Coord(c);
				}
			}
		}
		
		return coords;
	}
	
	/**
	 * 
	 * @param exclusions Coords where no mines can be generated
	 * @return Array of coords where mines can be generated
	 */
	private ArrayList<Coord> createAvailabilityArray(Coord[] exclusions){
		int availableSize = amountFields - exclusions.length;
		ArrayList<Coord> available = new ArrayList<Coord>(availableSize);
		
		for (int i = 0; i < size; i++){
			for (int j = 0; j < size; j++){
				Coord c = new Coord(i, j);
				if (Util.contains(exclusions, c)) continue;
				available.add(c);
			}
		}
		
		return available;
	}
	
	/**
	 * Returns the number of fields in a 3x3 area centered on coord. This can be different when coord is on the edge or in the corner.
	 * @param coord Coordinate
	 * @return Number of fields in a 3x3 area centered on coord
	 */
	private int getSectorSize(Coord coord){
		if (isInCorner(coord)){
			return CORNER_NEIGHBOUR_COUNT;
		} else if (isOnEdge(coord)){
			return EDGE_NEIGHBOUR_COUNT;
		} else {
			return MIDDLE_NEIGHBOUR_COUNT;
		}
	}
	
	private boolean isInCorner(Coord coord){
		int last = size - 1;
		return (coord.getX() == 0 || coord.getX() == last) && (coord.getY() == 0 || coord.getY() == last);
	}
	
	private boolean isOnEdge(Coord coord){
		int last = size - 1;
		return (coord.getX() == 0 || coord.getX() == last) ^ (coord.getY() == 0 || coord.getY() == last);
	}
	
	private boolean isWithinBoard(Coord coord){
		return coord.getX() >= 0 && coord.getY() >= 0 && coord.getX() < size && coord.getY() < size;
	}
}

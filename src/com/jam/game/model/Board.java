package com.jam.game.model;

import java.util.ArrayList;
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
	private boolean generated;
	private GameState state;
	private long timeStarted;
	private long timeEnded;
	private int leftToUncover;
	private int bombsToTag;
	private boolean debug = false;
	
	public Board(int size, int amountBombs){
		if (size < SECTOR_SIZE + 1){
			throw new IllegalArgumentException("The size must be bigger or equal to 4");
		} else if (amountBombs >= size * size / 3){
			throw new IllegalArgumentException("The amount of bombs must be less than 1/3 of all the fields");
		} else {
			this.size = size;
			this.amountBombs = amountBombs;
			
			restartGame();
		}
	}
	
	public void restartGame(){
		this.generated = false;
		this.state = GameState.FROZEN;
		this.bombsToTag = amountBombs;
		this.amountFields = size * size;
		this.board = new Field[size][size];
		this.leftToUncover = amountFields - amountBombs;

		initEmptyBoardArray();
	}
	
	private void initDebugBoardArray(int index){
		//Bombs surrounding 2x2 in upper left corner
		if (index == 0){
			setField(new Coord(0, 2), Field.COVERED_MINE);
			setField(new Coord(1, 2), Field.COVERED_MINE);
			setField(new Coord(2, 2), Field.COVERED_MINE);
			setField(new Coord(2, 0), Field.COVERED_MINE);
			setField(new Coord(2, 1), Field.COVERED_MINE);
		}
		this.generated = true;
	}
	
	private void initEmptyBoardArray(){
		for (int i = 0; i < size; i++){
			for (int j = 0; j < size; j++){
				board[i][j] = Field.COVERED_EMPTY;
			}
		}
	}
	
	public TagResult tagSingleField(Coord coord){
		switch (getField(coord)){
			case COVERED_EMPTY:
				setField(coord, Field.TAGGED_EMPTY);
				bombsToTag--;
				return TagResult.TAGGED;
			case COVERED_MINE:
				setField(coord, Field.TAGGED_MINE);
				bombsToTag--;
				return TagResult.TAGGED;
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
				return TagResult.FAILED;
			case TAGGED_EMPTY:
			case TAGGED_MINE:
				setField(coord, Field.COVERED_MINE);
				bombsToTag++;
				return TagResult.UNTAGGED;
			default:
				//this will never happen
				return TagResult.FAILED;
		}
	}
	
	public UncoverResult uncoverSingle(Coord coord){
		if (!isWithinBoard(coord)) throw new IllegalArgumentException("The coordinate is not within the board");
		if (!generated){
			if (debug){
				initDebugBoardArray(0);
			} else {
				generate(coord);
			}
			state = GameState.PLAYING;
		}
		if (state != GameState.PLAYING) return UncoverResult.FAILED;
		
		timeStarted = System.currentTimeMillis();
		switch (getField(coord)){
			case COVERED_EMPTY:
				uncoverRecursively(coord);
				if (isVictory()){
					state = GameState.WIN;
					timeEnded = System.currentTimeMillis();
					return UncoverResult.VICTORY;
				}
				return UncoverResult.SUCCESS;
			case EMPTY:
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
			case MINE:
				return UncoverResult.FAILED;
			case COVERED_MINE:
				setField(coord, Field.MINE);
				state = GameState.LOSE;
				timeEnded = System.currentTimeMillis();
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
		System.out.println("Uncover res " + coord.toString());
		Coord[] sector = getSectorCoords(coord);
		int bombsAround = countBombsSector(sector);
		
		setField(coord, Field.EMPTY);
		if (bombsAround == 0){
			//Run this function for all fields in the sector except the source
			for (int i = 0; i < sector.length; i++){
				Coord c = sector[i];
				if (c.equals(coord)) continue;
				if (!isUncovered(c)) uncoverRecursively(c);
			}
		} else {
			setField(coord, Field.fromOrdinal(bombsAround));
			leftToUncover--;
		}
	}
	
	private boolean isVictory(){
		return leftToUncover == 0;
	}
	
	private boolean isUncovered(Coord coord){
		Field field = getField(coord);
		return field != Field.TAGGED_EMPTY && field != Field.TAGGED_MINE && field != Field.MINE && field != Field.COVERED_EMPTY;
	}
	
	private void generate(Coord coord){
		ArrayList<Coord> available = createAvailabilityArray(getSectorCoords(coord));
		Random rand = new Random();
		
		for (int i = 0; i < amountBombs; i++){
			int bombIndex = rand.nextInt(available.size());
			setField(available.get(bombIndex), Field.COVERED_MINE);
		}
		
		generated = true;
	}
	
	private int countBombsSector(Coord[] coords){
		int bombs = 0;
		
		for (int i = 0; i < coords.length; i++){
			if (getField(coords[i]) == Field.COVERED_MINE || getField(coords[i]) == Field.MINE) bombs++;
		}
		
		return bombs;
	}
	
	public Field getField(Coord coord){
		return board[coord.getX()][coord.getY()];
	}
	
	public Field getField(int x, int y){
		return board[x][y];
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
		for (int dx = -1; dx <= 1; dx++){
			for (int dy = -1; dy <= 1; dy++){
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
	
	public int getSize(){
		return this.size;
	}
	
	public int getSecondsElapsed(){
		if (state == GameState.PLAYING){
			return (int) ((System.currentTimeMillis() - timeStarted) / 1000);
		} else if (state == GameState.FROZEN){
			return 0;
		} else {
			return (int) (timeEnded - timeStarted) / 1000;
		}
	}
	
	public int getBombsLeft(){
		return this.bombsToTag;
	}
}

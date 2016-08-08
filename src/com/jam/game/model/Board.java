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
	private int leftToUncover;
	private int bombsToTag;
	
	private GameState state;
	private long seed;
	
	private long timeStarted;
	private long timeEnded;
	
	private boolean debug;
	
	/**
	 * @param size Side of the game board
	 * @param amountBombs Amount of bombs
	 * @param seed Seed to use when generating level. If same seed is used for multiple games, and the
	 * user starts them by clicking on the same field, the boards will be identical. Use null if not using this feature.
	 */
	public Board(int size, int amountBombs, Long seed){
		if (size < SECTOR_SIZE + 1){
			throw new IllegalArgumentException("The size must be bigger or equal to 4");
		} else if (amountBombs >= size * size / 3){
			throw new IllegalArgumentException("The amount of bombs must be less than 1/3 of all the fields");
		} else {
			this.size = size;
			this.amountBombs = amountBombs;
			
			restartGame(seed);
		}
	}
	
	/**
	 * This resets the game into its initial state. Note that it doesn't generate the board,
	 * that happens when user first clicks on a tile.
	 * @param seed Seed to use for generating the new board
	 */
	public void restartGame(Long seed){
		this.seed = (seed == null) ? new Random().nextLong() : seed;
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
		state = GameState.PLAYING;
	}
	
	private void initEmptyBoardArray(){
		for (int i = 0; i < size; i++){
			for (int j = 0; j < size; j++){
				board[i][j] = Field.COVERED_EMPTY;
			}
		}
	}
	
	/**
	 * Tags a field. Tagged fields can't be clicked. Tags are used for sector-uncovering.
	 * @param coord Coordinate of the field to tag
	 * @return Result of the operation
	 */
	public TagResult tagSingleField(Coord coord){
		if (!isWithinBoard(coord)) throw new IllegalArgumentException("The coordinate is not within the board");
		if (state != GameState.PLAYING) return TagResult.FAILED;
		
		switch (getField(coord)){
			case COVERED_EMPTY:
				setField(coord, Field.TAGGED_EMPTY);
				bombsToTag--;
				return TagResult.TAGGED;
			case COVERED_MINE:
				setField(coord, Field.TAGGED_MINE);
				bombsToTag--;
				return TagResult.TAGGED;
			case TAGGED_EMPTY:
				setField(coord, Field.COVERED_EMPTY);
				bombsToTag++;
				return TagResult.UNTAGGED;
			case TAGGED_MINE:
				setField(coord, Field.COVERED_MINE);
				bombsToTag++;
				return TagResult.UNTAGGED;
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
			default:
				return TagResult.FAILED;
		}
	}
	
	public void setDebug(boolean debug){
		this.debug = debug;
	}
	
	/**
	 * Used to uncover a field. This can result in many field being uncovered if the field at coord
	 * is empty.
	 * @param coord Coordinate of the field to uncover
	 * @return Result of the operation
	 */
	public UncoverResult uncoverSingle(Coord coord){
		if (!isWithinBoard(coord)) throw new IllegalArgumentException("The coordinate is not within the board");
		if (state == GameState.FROZEN) generate(coord);
		if (state != GameState.PLAYING) return UncoverResult.FAILED;
		
		switch (getField(coord)){
			case COVERED_EMPTY:
				uncoverRecursively(coord);
				
				if (isVictory()){
					state = GameState.WIN;
					timeEnded = System.currentTimeMillis();
					return UncoverResult.VICTORY;
				} else return UncoverResult.SUCCESS;
			case COVERED_MINE:
				setField(coord, Field.MINE);
				state = GameState.LOSE;
				timeEnded = System.currentTimeMillis();
				return UncoverResult.MINE;
			case ONE:
			case TWO:
			case THREE:
			case FOUR:
			case FIVE:
			case SIX:
			case SEVEN:
			case EIGHT:
				return uncoverSector(coord);
			case EMPTY:
			case TAGGED_EMPTY:
			case TAGGED_MINE:
			case MINE:
				return UncoverResult.FAILED;
			default:
				return UncoverResult.FAILED;
		}
	}
	
	/**
	 * If an uncovered number is clicked, and an there are equally many tags within the sector surrounding it,
	 * all the covered fields within the sector will be uncovered as if uncoverSingle has been used on each one of them.
	 * This can result in defeat if a tag was misplaced.
	 * @param coord Coordinate of the center of the sector
	 */
	public UncoverResult uncoverSector(Coord coord){
		if (!isWithinBoard(coord)) throw new IllegalArgumentException("The coordinate is not within the board");
		Coord[] sector = getSectorCoords(coord);
		
		UncoverResult result = UncoverResult.FAILED;
		if (getField(coord).ordinal() == countTaggedFields(sector)){
			for (int i = 0; i < sector.length; i++){
				if (getField(sector[i]) == Field.COVERED_EMPTY || getField(sector[i]) == Field.COVERED_MINE){
					UncoverResult res = uncoverSingle(sector[i]);
					if (res == UncoverResult.VICTORY){
						return res;
					} else if (result == UncoverResult.FAILED && res == UncoverResult.SUCCESS){
						result = UncoverResult.SUCCESS;
					} else if (res == UncoverResult.MINE){
						return UncoverResult.MINE;
					}
				}
			}
		}
		
		return result;
	}
	
	/**
	 * This will uncover the specified field and all other fields that should be uncovered around coord
	 * @param coord Coordinate of the field being uncovered
	 */
	private void uncoverRecursively(Coord coord){
		Coord[] sector = getSectorCoords(coord);
		int bombsAround = countBombsSector(sector);
		
		//Sets the field to a number signifying amount of bombs within the sector
		setField(coord, Field.fromOrdinal(bombsAround));
		leftToUncover--;
		if (bombsAround == 0){
			Coord c;
			//Run this function for all fields in the sector except the source
			for (int i = 0; i < sector.length; i++){
				c = sector[i];
				if (c.equals(coord)) continue;
				if (!isUncovered(c)) uncoverRecursively(c);
			}
		}
	}
	
	private boolean isVictory(){
		return leftToUncover == 0;
	}
	
	/**
	 * Checks whether the field at coord has been uncovered.
	 * @param coord Coordinate of the field
	 * @return Whether the field is uncovered or not
	 */
	public boolean isUncovered(Coord coord){
		if (!isWithinBoard(coord)) throw new IllegalArgumentException("The coordinate is not within the board");
		
		Field field = getField(coord);
		
		return field != Field.TAGGED_EMPTY && field != Field.TAGGED_MINE && field != Field.COVERED_MINE && field != Field.COVERED_EMPTY;
	}
	
	private void generate(Coord coord){
		if (!debug){
			ArrayList<Coord> available = createAvailabilityArray(getSectorCoords(coord));
			Random rand = new Random(seed);

			for (int i = 0; i < amountBombs; i++){
				int bombIndex = rand.nextInt(available.size());
				if (!debug){
					setField(available.remove(bombIndex), Field.COVERED_MINE);
				} else {
					setField(available.remove(bombIndex), Field.TAGGED_MINE);
				}
			}
		} else {
			initDebugBoardArray(0);
		}
		
		state = GameState.PLAYING;
		timeStarted = System.currentTimeMillis();
	}
	
	private int countBombsSector(Coord[] coords){
		int bombs = 0;
		
		for (int i = 0; i < coords.length; i++){
			if (getField(coords[i]) == Field.COVERED_MINE || getField(coords[i]) == Field.MINE || getField(coords[i]) == Field.TAGGED_MINE) bombs++;
		}
		
		return bombs;
	}
	
	private int countTaggedFields(Coord[] coords){
		int tagged = 0;
		
		for (int i = 0; i < coords.length; i++){
			if (getField(coords[i]) == Field.TAGGED_EMPTY || getField(coords[i]) == Field.TAGGED_MINE) tagged++;
		}
		
		return tagged;
	}
	
	/**
	 * @param coord Coordinate of the field
	 * @return The field
	 */
	public Field getField(Coord coord){
		return board[coord.getX()][coord.getY()];
	}
	
	/**
	 * @param x X-coordinate of the field
	 * @param y Y-coordinate of the field
	 * @return The field
	 */
	public Field getField(int x, int y){
		return board[x][y];
	}
	
	private void setField(Coord coord, Field field){
		board[coord.getX()][coord.getY()] = field;
	}
	
	/**
	 * @param Coord Coordinate
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
	
	/**
	 * @param coord Coordinate to check
	 * @return Whether coord is within the field or not
	 */
	public boolean isWithinBoard(Coord coord){
		return coord.getX() >= 0 && coord.getY() >= 0 && coord.getX() < size && coord.getY() < size;
	}
	
	/**
	 * @return The number of fields in one dimension of the board. The board is quadratic
	 */
	public int getSize(){
		return this.size;
	}
	
	/**
	 * @return How many seconds have elapsed since the game started. If the game has been won/lost, the time won't change
	 */
	public int getSecondsElapsed(){
		if (state == GameState.PLAYING){
			return (int) ((System.currentTimeMillis() - timeStarted) / 1000);
		} else if (state == GameState.FROZEN){
			return 0;
		} else {
			return (int) (timeEnded - timeStarted) / 1000;
		}
	}
	
	/**
	 * @return Bombs left to tag. If tags aren't used, this number is useless
	 */
	public int getBombsLeft(){
		return this.bombsToTag;
	}
	
	/**
	 * @return The game state
	 */
	public GameState getState(){
		return this.state;
	}
	
	/**
	 * @return How many fields are left to uncover. If this number is 0, the player won
	 */
	public int getLeftToUncover(){
		return this.leftToUncover;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		
		builder.append("Board :\n");
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[i].length; j++) {
				builder.append(board[i][j].toString() + ", ");
			}
			builder.deleteCharAt(builder.length() - 1);
			builder.append("\n");
		}
		
		return builder.toString();
	}
}

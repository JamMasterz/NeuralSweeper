package com.jam.minesweeper.controller;

import com.jam.minesweeper.model.Board;
import com.jam.minesweeper.model.Coord;
import com.jam.minesweeper.view.ArrayMinesweeperGUI;
import com.jam.minesweeper.view.MinesweeperGUI;

public class ArrayMinesweeperController {
    private MinesweeperController[] games;
    private ArrayMinesweeperGUI gui;

    /**
     * @param pref Game preset
     * @param seed Seed to use when generating the board. Can be used to generate identical boards
     * @param controllable Whether the GUI is responsive to user actions
     */
    public ArrayMinesweeperController(MinesweeperController.DefaultGamePreference pref, Long seed, boolean controllable, int numGames){
        games = new MinesweeperController[numGames];
        for (int i = 0; i < numGames; i++) {
            games[i] = new MinesweeperController(pref, seed, controllable);
        }
    }

    /**
     * Clicks on a specified field
     * @param coord Coordinate to click on
     * @return Result of the action
     */
    public Board.UncoverResult leftClickField(Coord coord, int playerIndex){
        Board.UncoverResult r = games[playerIndex].leftClickField(coord);

        return r;
    }

    /**
     * Tags a specified field
     * @param coord Coordinate to click on
     * @return Result of the action
     */
    public Board.TagResult rightClickField(Coord coord, int playerIndex){
        Board.TagResult r = games[playerIndex].rightClickField(coord);

        return r;
    }

    public ArrayMinesweeperGUI getGUI(double scale){
        if (gui == null) {
            MinesweeperGUI[] guis = new MinesweeperGUI[games.length];
            for (int i = 0; i < games.length; i++) {
                guis[i] = games[i].getGUI(scale);
                games[i].getBoard().update();
            }
            gui = new ArrayMinesweeperGUI(guis, 7, 3);
        }

        return gui;
    }

    public MinesweeperController getGameForPlayer(int playerIndex) {
        return games[playerIndex];
    }
}

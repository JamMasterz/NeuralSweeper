package com.jam.minesweeper.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class ArrayMinesweeperGUI {
    private JFrame frame;
    private MinesweeperGUI[] guis;

    public ArrayMinesweeperGUI(MinesweeperGUI[] guis, int gridWidth, int gridHeight) {
        this.guis = guis;

        frame = new JFrame("Game Boards");
        frame.setLayout(new GridLayout(gridHeight, gridWidth));
        frame.setResizable(false);

        for (int i = 0; i < gridWidth * gridHeight; i++) {
            if (i >= guis.length) break;
            frame.add(guis[i]);
        }

        frame.pack();
        frame.setVisible(true);
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    }

    public void closeWindow(){
        for (MinesweeperGUI g : guis){
            g.disconnect();
        }

        frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
    }

    public void setWindowListener(WindowListener list){
        frame.addWindowListener(list);
    }
}

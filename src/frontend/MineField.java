package frontend;

import java.awt.Component;
import java.awt.GridLayout;

import info.gridworld.grid.BoundedGrid;
import info.gridworld.grid.Location;

import javax.swing.JComponent;
import javax.swing.JOptionPane;

import backend.*;
import backend.event.GameOverEvent;
import backend.event.GameOverListener;

@SuppressWarnings("serial")
public class MineField extends JComponent implements GameOverListener{
	
	private Minesweeper game;
	
	public MineField(Minesweeper game){
		setGame(game);
		setUp();
	}
	
	private void setUp() {
		
		BoundedGrid<Spot> grid = game.getGrid();
		setLayout(new GridLayout(grid.getNumRows(), grid.getNumCols(), 0, 0));
		for(int r = 0; r < grid.getNumRows(); r++){
			for(int c = 0; c < grid.getNumCols(); c++){
				MineSpot s = new MineSpot(grid.get(new Location(r,c)), game);
				add(s);
			}
		}
		setSize();
		
		game.addEventListener(this);
		
	}

	private void setSize() {
		int wid = game.getGrid().getNumCols()*MineSpot.SIZE;
		int len = game.getGrid().getNumRows()*MineSpot.SIZE;
		
		setSize(wid, len);
	}

	public void setGame(Minesweeper g){
		game = g;
	}
	
	@Override
	public void handleEvent(GameOverEvent e) {
		JOptionPane.showMessageDialog(this,
			    "You triggered a bomb... you died.",
			    "Game Over",
			    JOptionPane.WARNING_MESSAGE);
	}
	
}

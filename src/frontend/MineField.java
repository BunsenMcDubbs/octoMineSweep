package frontend;

import java.awt.Component;
import java.awt.GridLayout;

import info.gridworld.grid.BoundedGrid;
import info.gridworld.grid.Location;

import javax.swing.JComponent;
import backend.*;

@SuppressWarnings("serial")
public class MineField extends JComponent {
	
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
				MineSpot s = new MineSpot(grid.get(new Location(r,c)));
				add(s);
			}
		}
		setSize();
	}

	private void setSize() {
		int wid = game.getGrid().getNumCols()*MineSpot.SIZE;
		int len = game.getGrid().getNumRows()*MineSpot.SIZE;
		
		setSize(wid, len);
	}

	public void setGame(Minesweeper g){
		game = g;
	}
	
	private void refreshSpots(){
	}
	
}

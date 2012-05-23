package frontend;

import java.awt.Component;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import info.gridworld.grid.BoundedGrid;
import info.gridworld.grid.Location;

import javax.swing.JComponent;
import javax.swing.JOptionPane;

import backend.*;
import backend.event.GameEndEvent;
import backend.event.GameEndListener;

@SuppressWarnings("serial")
public class MineField extends JComponent implements GameEndListener, MouseListener{
	
	private Minesweeper game;
	
	public MineField(Minesweeper game){
		setGame(game);
		setUp();
		addMouseListener(this);
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
	public void handleEvent(GameEndEvent e) {
		if(e.isWinner()){
			JOptionPane.showMessageDialog(this,
				    "The Mines have been Sweeped... you win\n" + ((Minesweeper)e.getSource()).getTime(),
				    "Winner",
				    JOptionPane.WARNING_MESSAGE);
			System.out.println(((Minesweeper)e.getSource()).getTime());
		}
		else{
			JOptionPane.showMessageDialog(this,
				    "You triggered a bomb... you died.",
				    "Game Over",
				    JOptionPane.WARNING_MESSAGE);
		}
		
	}

	// TODO NOT WORKING
	@Override
	public void mouseClicked(MouseEvent e) {
		Point p = e.getPoint();
		int c = p.x/MineSpot.SIZE;
		int r = p.y/MineSpot.SIZE;
		game.getGrid().get(new Location(r,c)).toggleFlag();
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		System.out.println("Entered");
		if(!game.isFinished())
			game.getTimer().start();
	}

	@Override
	public void mouseExited(MouseEvent e) {
		System.out.println("Exited");
		game.getTimer().stop();
	}

	@Override
	public void mousePressed(MouseEvent arg0) {}
	@Override
	public void mouseReleased(MouseEvent arg0) {}
	
}

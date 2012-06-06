package frontend;

import java.awt.Color;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import info.gridworld.grid.BoundedGrid;
import info.gridworld.grid.Location;

import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import backend.*;
import backend.event.GameEndEvent;
import backend.event.GameEndListener;

/**
 * MineField is a graphical, interactive representation of the Minesweeper game and
 * the <code>Minesweeper</code> backend. It contains other components (MineSpots)
 * to represent each spot on the board.
 * @author Andrew Dai
 *
 */

@SuppressWarnings("serial")
public class MineField extends JPanel implements GameEndListener{

	private Minesweeper game;

	/**
	 * Constructor for MineField that takes a Minesweeper game to
	 * represent
	 * @param game
	 */
	public MineField(Minesweeper game) {
		setGame(game);
		setUp();
	}

	/**
	 * Helper method to setup the MineField
	 */
	private void setUp() {

		BoundedGrid<Spot> grid = game.getGrid();
		setLayout(new GridLayout(grid.getNumRows(), grid.getNumCols(), 0, 0));
		for (int r = 0; r < grid.getNumRows(); r++) {
			for (int c = 0; c < grid.getNumCols(); c++) {
				MineSpot s = new MineSpot(grid.get(new Location(r, c)), game);
				add(s);
			}
		}
		setSize();

		game.addEventListener(this);

	}

	/**
	 * Helper method to set the size of the component after the game has
	 * been initialized.
	 */
	private void setSize() {
		int wid = game.getGrid().getNumCols() * MineSpot.SIZE;
		int len = game.getGrid().getNumRows() * MineSpot.SIZE;

		setSize(wid, len);
		setMinimumSize(getSize());
		setMaximumSize(getSize());
		setPreferredSize(getSize());
	}

	/**
	 * Setter for the Minesweeper game
	 * @param minesweeper game to run the backend
	 */
	public void setGame(Minesweeper g) {
		game = g;
	}

	/**
	 * Handler for when the game ends
	 */
	@Override
	public void handleEvent(GameEndEvent e) {
		repaint();
		if (e.isWinner()) {
			JOptionPane.showMessageDialog(this,
					"The Mines have been Sweeped... you win\n"
							+ TimeDisplay.tenthsToString(((Minesweeper) e.getSource()).getTime()) 
							+ "\nWith " + ((Minesweeper)e.getSource()).getClicks()
							+ " clicks",
					"Winner", JOptionPane.INFORMATION_MESSAGE);
			System.out.println(((Minesweeper) e.getSource()).getTime());
		} else {
			JOptionPane.showMessageDialog(this,
					"You triggered a bomb... you died.", "Game Over",
					JOptionPane.INFORMATION_MESSAGE);
		}

	}
}

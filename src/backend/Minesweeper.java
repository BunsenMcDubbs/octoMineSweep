package backend;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.Timer;

import backend.event.GameEndEvent;
import backend.event.GameEndListener;
import backend.event.OpenedSpotEvent;
import backend.event.OpenedSpotEventListener;

import info.gridworld.grid.BoundedGrid;
import info.gridworld.grid.Location;

/**
 * Minesweeper runs the backend game mechanics and runs of the game. It features
 * 3 difficulty levels (Easy, Medium and Hard) each with different size boards
 * and number of mines.</br> Minesweeper uses a BoundedGrid for the 2
 * dimensional array of Spots.
 * 
 * @author bunsenmcdubbs
 * 
 */
public class Minesweeper implements ActionListener {

	private BoundedGrid<Spot> grid;
	private int difficulty;
	private Timer timer;
	//tenths of second
	private int time;
	private int clicks;
	private boolean gameActive;

	/**
	 * Easy difficulty level 9 by 9 board with 10 bombs
	 */
	public static final int EASY = 1;

	/**
	 * Medium difficulty level 16 by 16 board with 40 bombs
	 */
	public static final int MEDIUM = 2;

	/**
	 * Hard difficulty level 30 by 16 board with 99 bombs
	 */
	public static final int HARD = 3;
	
	/**
	 * Custom difficulty level defined elsewhere
	 */
	public static final int CUSTOM = 0;

	/**
	 * Constructor for the game that takes an integer to define the difficulty
	 * level of the game.
	 * 
	 * @param difficulty
	 */
	public Minesweeper(int difficulty) {
		init(difficulty);
		timerSetup();
	}

	private void timerSetup() {
		time = 0;
		timer = new Timer(100, this);
	}

	/**
	 * Default constructor for the Minesweeper game, assumes a difficulty level
	 * of 1 or EASY
	 */
	public Minesweeper() {
		this(1);
	}

	public void init(int d) {
		setDifficulty(d);
		int seed;

		switch (difficulty) {
		case EASY:
			grid = new BoundedGrid<Spot>(9, 9);
			seed = 10;
			break;
		case MEDIUM:
			grid = new BoundedGrid<Spot>(16, 16);
			seed = 40;
			break;
		case HARD:
			grid = new BoundedGrid<Spot>(30, 16);
			seed = 99;
			break;
		default:
			seed = 0;
			grid = new BoundedGrid<Spot>(1, 1);
			break;
		}

		for (int r = 0; r < grid.getNumRows(); r++) {
			for (int c = 0; c < grid.getNumCols(); c++) {
				Location curr = new Location(r, c);
				grid.put(curr, new Spot(curr, grid, false));
			}
		}

		for (int i = 0; i < seed; i++) {
			int r = (int) (Math.random() * grid.getNumRows());
			int c = (int) (Math.random() * grid.getNumCols());
			((Spot) (grid.get(new Location(r, c)))).setBomb(true);
		}

		clicks = 0;
		gameActive = false;
	}

	public void open(int x, int y) {
		open(new Location(y, x));
	}

	public void open(Location loc) {
		if (!grid.get(loc).isOpen()) {
			if (clicks == 0) {
				timer.start();
			}
			if (clicks == 0 && grid.get(loc).isBomb()) {
				moveBomb(loc);
			}
			if (Spot.BOMB == grid.get(loc).discreteOpen()) {
				gameOver();
				timer.stop();
				return;
			}
			clicks++;
			int totalSpots = 0;
			if(difficulty == EASY){
				totalSpots = 71;
			}
			else if(difficulty == MEDIUM){
				totalSpots = 216;
			}
			else if(difficulty == HARD){
				totalSpots = 381;
			}
			else if(difficulty == CUSTOM){
				// TODO calculation stuff
			}
			
			if((totalSpots - clicks) == 0){
				win();
			}
		}
	}

	private void moveBomb(Location loc) {
		Location newLoc = new Location(0, 0);
		do {
			int r = (int) (Math.random() * grid.getNumRows());
			int c = (int) (Math.random() * grid.getNumCols());
			newLoc = new Location(r, c);
		} while (loc.equals(newLoc));
		((Spot) (grid.get(loc))).setBomb(false);
		((Spot) (grid.get(newLoc))).setBomb(true);
	}

	public boolean isFinished() {
		for (int r = 0; r < grid.getNumRows(); r++) {
			for (int c = 0; c < grid.getNumCols(); c++) {
				Spot s = (Spot) grid.get(new Location(r, c));
				if (!s.isOpen()) {
					if (s.isBomb())
						continue;
					return false;
				}
			}
		}
		return true;
	}

	public BoundedGrid<Spot> getGrid() {
		return grid;
	}

	public int getDifficulty() {
		return difficulty;
	}

	public void setDifficulty(int d) {
		difficulty = d;
	}

	public Timer getTimer() {
		return timer;
	}

	public int getTime() {
		return time;
	}

	private void win() {
		System.out.println("WINNER WINNER CHICKEN DINNER");
		fireEvent(true);
		gameActive = false;
	}

	private void gameOver() {
		System.out.println("GAME OVER");
		fireEvent(false);
		gameActive = false;
		revealAll();
	}

	// TODO find calls and only open bombs
	private void revealAll() {
		for (int r = 0; r < grid.getNumRows(); r++) {
			for (int c = 0; c < grid.getNumCols(); c++) {
				grid.get(new Location(r, c)).open();
			}
		}
	}

	public String toString() {
		String s = "Minesweeper\n";
		s += "+";
		for (int i = 1; i <= grid.getNumRows() * 2 + 1; i++) {
			s += "-";
		}
		s += "+\n";
		for (int r = 0; r < grid.getNumRows(); r++) {
			s += "| ";
			for (int c = 0; c < grid.getNumCols(); c++) {
				Spot curr = (Spot) grid.get(new Location(r, c));
				int n = curr.getState();
				if (n == Spot.BOMB)
					s += "X ";
				else if (n == Spot.CLOSED)
					s += "* ";
				else if (n == Spot.FLAG)
					s += "F ";
				else
					s += "" + n + " ";
			}
			s += "|\n";
		}
		s += "+";
		for (int i = 1; i <= grid.getNumRows() * 2 + 1; i++) {
			s += "-";
		}
		s += "+";
		return s;
	}

	public String testString() {
		String s = "Minesweeper\n";
		s += "+";
		for (int i = 1; i <= grid.getNumRows() * 2 + 1; i++) {
			s += "-";
		}
		s += "+\n";
		for (int r = 0; r < grid.getNumRows(); r++) {
			s += "| ";
			for (int c = 0; c < grid.getNumCols(); c++) {
				Spot curr = (Spot) grid.get(new Location(r, c));
				int n = curr.testGetState();
				if (n == Spot.BOMB)
					s += "X ";
				else if (n == Spot.FLAG)
					s += "F ";
				else
					s += "" + n + " ";
			}
			s += "|\n";
		}
		s += "+";
		for (int i = 1; i <= grid.getNumRows() * 2 + 1; i++) {
			s += "-";
		}
		s += "+";
		return s;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == timer) {
			time += 1;
		}
	}

	public boolean isActive() {
		return gameActive;
	}

	private ArrayList<GameEndListener> listeners = new ArrayList<GameEndListener>();

	public synchronized void addEventListener(GameEndListener listener) {
		listeners.add(listener);
	}

	public synchronized void removeEventListener(GameEndListener listener) {
		listeners.remove(listener);
	}

	// call this method whenever you want to notify
	// the event listeners of the particular event
	private synchronized void fireEvent(boolean win) {
		timer.stop();
		GameEndEvent event = new GameEndEvent(this, win);
		Iterator<GameEndListener> i = listeners.iterator();
		while (i.hasNext()) {
			((GameEndListener) i.next()).handleEvent(event);
		}
	}

}

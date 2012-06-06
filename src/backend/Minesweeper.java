package backend;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;

import javax.management.InvalidAttributeValueException;
import javax.swing.Timer;

import frontend.TimeDisplay;

import backend.event.GameEndEvent;
import backend.event.GameEndListener;
import backend.event.ClickedSpotEvent;
import backend.event.ClickedSpotEventListener;

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
public class Minesweeper implements ActionListener, ClickedSpotEventListener {

	private BoundedGrid<Spot> grid;
	private int difficulty;
	private Timer timer;
	//tenths of second
	private int time;
	private int clicks;
	private boolean gameActive;
	private boolean enabled;
	private int bombs;

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
	public static final int CUSTOM = 4;

	/**
	 * Constructor for the game that takes an integer to define the difficulty
	 * level of the game.
	 * 
	 * @param difficulty level for the game
	 */
	public Minesweeper(int difficulty) {
		init(difficulty);
		timerSetup();
		enabled = true;
	}
	
	/**
	 * Custom game constructor for minesweeper that takes a row & column count
	 * to define a custom size board and a number of bombs to put in the board
	 * @param rows - number of rows in the board
	 * @param columns - number of columns in the board
	 * @param bombs - number of bombs in the board
	 * @throws InvalidAttributeValueException when the number of bombs is more
	 * than one less than the number of spots on the board
	 */
	public Minesweeper(int rows, int columns, int bombs) throws InvalidAttributeValueException{
		customInit(rows, columns, bombs);
		timerSetup();
		enabled = true;
	}

	/**
	 * Private helper method to setup a custom Minesweeper game
	 * @param rows - number of rows in the board
	 * @param columns - number of columns in the board
	 * @param bombs - number of bombs in the board
	 * @throws InvalidAttributeValueException when the number of bombs is more
	 * than one less than the number of spots on the board
	 */
	private void customInit(int rows, int columns, int bombs)
		throws InvalidAttributeValueException{
		
		if((rows * columns) <= bombs)
			throw new InvalidAttributeValueException("There are too many bombs\n"
				+ "The maximum allowed for a board of size: "
				+ columns + ", " + rows + " is " + (rows * columns-1));
		else if(bombs < 0){
			throw new InvalidAttributeValueException("There cannot be less than zero bombs");
		}
		
		grid = new BoundedGrid<Spot>(rows, columns);
		this.bombs = bombs;
		
		init(CUSTOM);
	}

	/**
	 * Default constructor for the Minesweeper game, assumes a difficulty level
	 * of 1 or EASY
	 */
	public Minesweeper() {
		this(1);
	}

	/**
	 * Initializes the Minesweeper game and all of its parts according to the
	 * given difficulty setting
	 * @param d - difficulty level, matched to EASY MEDIUM HARD
	 */
	private void init(int d) {
		setDifficulty(d);

		switch (difficulty) {
		case EASY:
			grid = new BoundedGrid<Spot>(9, 9);
			bombs = 10;
			break;
		case MEDIUM:
			grid = new BoundedGrid<Spot>(16, 16);
			bombs = 40;
			break;
		case HARD:
			grid = new BoundedGrid<Spot>(16, 30);
			bombs = 99;
			break;
		case CUSTOM:
			break;
		default:
			bombs = 0;
			grid = new BoundedGrid<Spot>(1, 1);
			break;
		}

		for (int r = 0; r < grid.getNumRows(); r++) {
			for (int c = 0; c < grid.getNumCols(); c++) {
				Location curr = new Location(r, c);
				Spot s = new Spot(curr, grid, false);
				s.addEventListener(this);
				grid.put(curr, s);
			}
		}

		for (int i = 0; i < bombs; i++) {
//			System.out.println(i);
			int r = (int) (Math.random() * grid.getNumRows());
			int c = (int) (Math.random() * grid.getNumCols());
			if(!grid.get(new Location(r, c)).isBomb())
				grid.get(new Location(r, c)).setBomb(true);
			else
				i--;
		}

		clicks = 0;
		gameActive = false;
	}

	/**
	 * Private helper method setting up the timer
	 */
	private void timerSetup() {
		time = 0;
		timer = new Timer(100, this);
	}

	/**
	 * Opens the spot specified with the x and y coordinates
	 * @param x coordinate of the spot
	 * @param y coordinate of the spot
	 */
	public void open(int x, int y) {
		open(new Location(y, x));
	}

	/**
	 * Opens the spot specified with the given location
	 * @param location of the spot
	 */
	public void open(Location loc) {
		if(!enabled)
			return;
		if (!grid.get(loc).isOpen() && !grid.get(loc).isFlagged()) {
			startGame();
			if (clicks == 0) {
				if(grid.get(loc).isBomb())
					moveBomb(loc);
			}
			if (Spot.BOMB == grid.get(loc).open()) {
				lose();
				return;
			}
			clicks++;
			if(hasFinished()){
				win();
			}
		}
	}
	
	/**
	 * (Re)Starts the game, starting the timer and setting the gameActive
	 * flag to true
	 */
	public void startGame(){
		timer.start();
		gameActive = true;
	}
	
	/**
	 * Stops the game, stopping the timer and setting the gameActive flag
	 * to false
	 */
	public void stopGame(){
		timer.stop();
		gameActive = false;
	}

	/**
	 * Private helper method for moving the bomb at the given location if
	 * the player clicks on it on the first move since that is not allowed
	 * in the game rules. The method moves the bomb to a location that isnt
	 * already a bomb.
	 * @param location of the bomb to be moved
	 */
	private void moveBomb(Location loc) {
		System.out.println("Moved bomb, whew dodged a bullet (mine) there");
		Location newLoc = new Location(0, 0);
		do {
			int r = (int) (Math.random() * grid.getNumRows());
			int c = (int) (Math.random() * grid.getNumCols());
			newLoc = new Location(r, c);
		} while (loc.equals(newLoc));
		((Spot) (grid.get(loc))).setBomb(false);
		((Spot) (grid.get(newLoc))).setBomb(true);
	}

	/**
	 * Returns true if all the spots that aren't bombs have been opened
	 * and the game is finished.
	 * @return If the game as finished (the player has won)
	 */
	private boolean hasFinished() {
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
	
	/**
	 * Returns true if the game is enabled (the game hasn't ended)
	 * Only returns false if the player has won the game or a bomb
	 * has been clicked and the player lost
	 * @return
	 */
	public boolean isEnabled(){
		return enabled;
	}

	/**
	 * Returns the BoundedGrid holding all the spots in the game
	 * @return
	 */
	public BoundedGrid<Spot> getGrid() {
		return grid;
	}

	/**
	 * Returns the current difficulty level of the game
	 * @return
	 */
	public int getDifficulty() {
		return difficulty;
	}

	/**
	 * Sets the difficulty level of the game
	 * @param d
	 */
	private void setDifficulty(int d) {
		difficulty = d;
	}
	
	/**
	 * Restarts the game with a new difficulty level. If the "level" is
	 * 0 then the game resets with the current difficulty level.
	 * @param d
	 */
	public void resetGame(int d){
		if(d == 0)
			d = getDifficulty();
		init(d);
	}

	/**
	 * Returns the timer of the game
	 * @return
	 */
	public Timer getTimer() {
		return timer;
	}

	/**
	 * Returns the time as an integer in tenths of a second
	 * @return
	 */
	public int getTime() {
		return time;
	}

	/**
	 * Private helper method if the player has won the game
	 */
	private void win() {
		fireEvent(true);
	}

	/**
	 * Private helper method if the player has lost the game
	 */
	private void lose() {
		fireEvent(false);
	}

	/**
	 * Reveals all the bombs in the game
	 */
	private void revealAllBombs() {
		for (int r = 0; r < grid.getNumRows(); r++) {
			for (int c = 0; c < grid.getNumCols(); c++) {
				if(grid.get(new Location(r,c)).isBomb())
					grid.get(new Location(r, c)).reveal();
			}
		}
	}

	/**
	 * Returns a string representation of the game
	 */
	public String toString() {
		String s = "Minesweeper\n";
		s += "+";
		for (int i = 1; i <= grid.getNumCols() * 2 + 1; i++) {
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
		for (int i = 1; i <= grid.getNumCols() * 2 + 1; i++) {
			s += "-";
		}
		s += "+";
		return s;
	}

	/**
	 * Returns a String representation of the game with all the spots opened
	 * @return
	 */
	public String testString() {
		String s = "TestMinesweeper\n";
		s += "+";
		for (int i = 1; i <= grid.getNumCols() * 2 + 1; i++) {
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
		for (int i = 1; i <= grid.getNumCols() * 2 + 1; i++) {
			s += "-";
		}
		s += "+";
		return s;
	}

	/**
	 * String representation of the current attributes of the minesweeper game
	 * @return
	 */
	public String status(){
		String s = "";
		
		s += "Difficulty: " + difficulty;
		s += "\nEnabled: " + enabled;
		s += "\nClicks: "	+ clicks;
		s += "\nTime: "	+ TimeDisplay.tenthsToString(time);
		s += "\nActive: " + gameActive;
		
		return s;
	}
	/**
	 * Increments the time to keep track of the amount of time
	 * the current game is taking (in tenths of a second)
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == timer) {
			time += 1;
		}
	}

	/**
	 * @return if the game is currently active
	 */
	public boolean isActive() {
		return gameActive;
	}
	
	/**
	 * @return true if the game has started (clicks != 0)
	 */
	public boolean hasStarted(){
		return (clicks != 0);
	}

	private ArrayList<GameEndListener> listeners = new ArrayList<GameEndListener>();

	/**
	 * Adds a GameEndListener to the game
	 * @param listener
	 */
	public synchronized void addEventListener(GameEndListener listener) {
		listeners.add(listener);
	}

	/**
	 * Removes a GameEndListener from the list of listeners
	 * @param listener
	 */
	public synchronized void removeEventListener(GameEndListener listener) {
		listeners.remove(listener);
	}

	/**
	 * Fires an GameEndEvent to all the listeners when the game has ended.
	 * @param win - whether or not the player won the game
	 */
	private synchronized void fireEvent(boolean win) {
		if(win){
			System.out.println("WINNER WINNER CHICKEN DINNER");
		}
		else{
			System.out.println("GAME OVER");
			revealAllBombs();
		}
		gameActive = false;
		enabled = false;
		timer.stop();
		GameEndEvent event = new GameEndEvent(this, win);
		Iterator<GameEndListener> i = listeners.iterator();
		while (i.hasNext()) {
			((GameEndListener) i.next()).handleEvent(event);
		}
//		System.out.println(this);
//		System.out.println(testString());
	}

	/**
	 * Restarts the game after a click is detected
	 */
	@Override
	public void handleEvent(ClickedSpotEvent e) {
		startGame();
	}

	/**
	 * @return the clicks
	 */
	public int getClicks() {
		return clicks;
	}

	/**
	 * resets the click counter to zero
	 */
	public void resetClicks() {
		clicks = 0;
	}

}

package backend;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

import info.gridworld.grid.BoundedGrid;
import info.gridworld.grid.Location;

/**
 * Minesweeper runs the backend game mechanics and runs of the game.
 * It features 3 difficulty levels (Easy, Medium and Hard) each with
 * different size boards and number of mines.</br>
 * Minesweeper uses a BoundedGrid for the 2 dimensional array of Spots.
 * 
 * @author bunsenmcdubbs
 *
 */
public class Minesweeper implements ActionListener{
	
	private BoundedGrid<Spot> grid;
	private int difficulty;
	private Timer timer;
	private double time;
	private int clicks;
	
	public static final int EASY = 1, MEDIUM = 2, HARD = 3;
	
	/**
	 * Constructor for the game that takes an integer to define the
	 * difficulty level of the game.
	 * @param difficulty
	 */
	public Minesweeper(int difficulty){
		init(difficulty);
		timerSetup();
	}
	
	private void timerSetup() {
		time = 0;
		timer = new Timer(100, this);
	}

	/**
	 * 
	 */
	public Minesweeper(){
		this(1);
	}

	private void init(int d) {
		setDifficulty(d);
		int seed;
		
		switch(difficulty){
		case EASY:	grid = new BoundedGrid<Spot>(9, 9); seed = 10; break;
		case MEDIUM:	grid = new BoundedGrid<Spot>(16, 16); seed = 40; break;
		case HARD:	grid = new BoundedGrid<Spot>(30, 16); seed = 99; break;
		default: seed = 0; grid = new BoundedGrid<Spot>(1,1); break;
		}
		
		for(int r = 0; r < grid.getNumRows(); r++){
			for(int c = 0; c < grid.getNumCols(); c++){
				Location curr = new Location(r, c);
				grid.put(curr, new Spot(curr, grid, false));
			}
		}
		
		for(int i = 0; i < seed; i++){
			int r = (int)(Math.random()*grid.getNumRows());
			int c = (int)(Math.random()*grid.getNumCols());
			((Spot)(grid.get(new Location(r,c)))).setBomb(true);
		}
		
		clicks = 0;
	}
	
	public void open(int x, int y){
		open(new Location(y, x));
	}
	
	public void open(Location loc){
		if( clicks == 0){
			timer.start();
		}
		if( clicks == 0 && grid.get(loc).isBomb()){
			moveBomb(loc);
		}
		if(Spot.BOMB == grid.get(loc).open()){
			gameOver();
		}
		clicks++;
	}

	private void moveBomb(Location loc) {
		Location newLoc = new Location(0, 0);
		do{
			int r = (int)(Math.random()*grid.getNumRows());
			int c = (int)(Math.random()*grid.getNumCols());
			newLoc = new Location(r, c);
		}while(loc.equals(newLoc));
		((Spot)(grid.get(loc))).setBomb(false);
		((Spot)(grid.get(newLoc))).setBomb(true);
	}
	
	public boolean isFinished(){
		for(int r = 0; r < grid.getNumRows(); r++){
			for(int c = 0; c < grid.getNumCols(); c++){
				Spot s = (Spot)grid.get(new Location(r,c));
				if(!s.isOpened()){
					if(s.isBomb())
						continue;
					return false;
				}
			}
		}
		return true;
	}
	
	public BoundedGrid<Spot> getGrid(){
		return grid;
	}
	
	public int getDifficulty(){
		return difficulty;
	}
	
	public void setDifficulty(int d){
		difficulty = d;
	}
	
	public Timer getTimer(){
		return timer;
	}
	
	public double getTime(){
		return time;
	}
	
	private void win(){
		System.out.println("WINNER WINNER CHICKEN DINNER");
	}
	
	private void gameOver(){
		System.out.println("GAME OVER");
	}
	
	public String toString(){
		String s = "Minesweeper\n";
		s += "+";
		for(int i = 1; i <= grid.getNumRows()*2+1; i++){
			s += "-";
		}
		s += "+\n";
		for(int r = 0; r < grid.getNumRows(); r++){
			s += "| ";
			for(int c = 0; c < grid.getNumCols(); c++){
				Spot curr = (Spot)grid.get(new Location(r, c));
				int n = curr.getState();
				if(n == Spot.BOMB) s += "X ";
				else if(n == Spot.CLOSED) s += "¥ ";
				else if(n == Spot.FLAG)	s += "F ";
				else s += "" + n + " ";
			}
			s += "|\n";
		}
		s += "+";
		for(int i = 1; i <= grid.getNumRows()*2+1; i++){
			s += "-";
		}
		s += "+";
		return s;
	}
	
	public String testString(){
		String s = "Minesweeper\n";
		s += "+";
		for(int i = 1; i <= grid.getNumRows()*2+1; i++){
			s += "-";
		}
		s += "+\n";
		for(int r = 0; r < grid.getNumRows(); r++){
			s += "| ";
			for(int c = 0; c < grid.getNumCols(); c++){
				Spot curr = (Spot)grid.get(new Location(r, c));
				int n = curr.testGetState();
				if(n == Spot.BOMB) s += "X ";
				else if(n == Spot.FLAG)	s += "F ";
				else s += "" + n + " ";
			}
			s += "|\n";
		}
		s += "+";
		for(int i = 1; i <= grid.getNumRows()*2+1; i++){
			s += "-";
		}
		s += "+";
		return s;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == timer){
			time += .1;
		}
	}

}

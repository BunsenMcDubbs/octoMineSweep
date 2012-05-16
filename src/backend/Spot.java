package backend;

import info.gridworld.grid.Grid;
import info.gridworld.grid.Location;

import java.awt.Point;
import java.util.ArrayList;

public class Spot {
	
	private boolean flag;
	private boolean bomb;
	private boolean opened;
	public final Location loc;
	private Grid grid;
	
	public static final int ZERO = 0;
	public static final int ONE = 1;
	public static final int TWO = 2;
	public static final int THREE = 3;
	public static final int FOUR = 4;
	public static final int FIVE = 5;
	public static final int SIX = 6;
	public static final int SEVEN = 7;
	public static final int EIGHT = 8;
	
	public static final int FLAG = -1;
	public static final int BOMB = 9;
	public static final int CLOSED = 10;
	
	public Spot(Location location, Grid g, boolean bomb){
		loc = location;
		grid = g;
		flag = false;
		setBomb(bomb);
		opened = false;
	}
	
	/**
	 * @return the flagged
	 */
	public boolean isFlagged() {
		return flag;
	}
	/**
	 * sets flagged to true
	 */
	public void flag() {
		flag = true;
	}
	/**
	 * sets flagged to false
	 */
	public void unflag() {
		flag = false;
	}
	/**
	 * toggles the flag
	 */
	public void toggleFlag(){
		flag = !flag;
	}
	/**
	 * @return the bomb
	 */
	public boolean isBomb() {
		return bomb;
	}
	/**
	 * @param bomb the bomb to set
	 */
	public void setBomb(boolean bomb) {
		this.bomb = bomb;
	}
	/**
	 * @return the bombCount
	 */
	private int getBombCount() {
		ArrayList<Spot> neighbors = grid.getNeighbors(loc);
		int count = 0;
		for(Spot s : neighbors)
			if (s.isBomb())
				count++;
		return count;
	}
	/**
	 * @return if the slot is opened then it returns the
	 * bomb count, otherwise it returns 10
	 */
	public int getState(){
		if(!opened) return Spot.CLOSED;
		if(bomb) return Spot.BOMB;
		if(flag) return Spot.FLAG;
		return getBombCount();
	}
	/**
	 * @return the bomb count
	 */
	public int testGetState(){
		if(bomb) return Spot.BOMB;
		if(flag) return Spot.FLAG;
		return getBombCount();
	}
	/**
	 * @return the opened
	 */
	public boolean isOpened() {
		return opened;
	}
	
	/**
	 * Opens or reveals the mine
	 * @return the bomb count of the spot,
	 * -1 if flagged (aka doesn't open)
	 * 9 if bomb
	 * 0-8 for the number of bombs around the spot
	 */
	public int open(){
		if(flag){
			opened = false;
			return -1;
		}
		if(bomb){
			opened = true;
			return 9;
		}
		opened = true;
		if (getBombCount() == 0){
			@SuppressWarnings("unchecked")
			ArrayList<Spot> n = grid.getNeighbors(loc);
			for(Spot s : n){
				if(!s.opened)
					s.open();
			}
		}
		return getBombCount();
		
	}
	
	
}

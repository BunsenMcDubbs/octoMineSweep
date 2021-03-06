package backend;

import info.gridworld.grid.Grid;
import info.gridworld.grid.Location;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Iterator;

import backend.event.ClickedSpotEvent;
import backend.event.ClickedSpotEventListener;

public class Spot {
	
	private boolean flag;
	private boolean bomb;
	private boolean opened;
	public final Location loc;
	private Grid<Spot> grid;
	
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
	
	public Spot(Location location, Grid<Spot> g, boolean bomb){
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
		if(opened) return;
		System.out.println("flag Toggled at " + loc);
		flag = !flag;
		fireEvent(false);
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
		if(flag) return Spot.FLAG;
		if(!opened) return Spot.CLOSED;
		if(opened && bomb) return Spot.BOMB;
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
	public boolean isOpen() {
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
		
		fireEvent(true);
		return reveal();
	}
	
	public int reveal(){
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
			ArrayList<Spot> n = grid.getNeighbors(loc);
			for(Spot s : n){
				if(!s.opened)
					s.open();
			}
		}
		
		return getBombCount();
	}
	
	public String status(){
		String s = "";
		
		s += "Location: " + loc;
		s += "\nisBomb: " + bomb;
		s += "\nOpened: " + opened;
		s += "\nflagged: " + flag;
		return s;
	}
	
	private ArrayList<ClickedSpotEventListener> listeners = new ArrayList<ClickedSpotEventListener>();

	public synchronized void addEventListener(ClickedSpotEventListener listener) {
		listeners.add(listener);
	}

	public synchronized void removeEventListener(
			ClickedSpotEventListener listener) {
		listeners.remove(listener);
	}

	// call this method whenever you want to notify
	// the event listeners of the particular event
	private synchronized void fireEvent(boolean click) {
		ClickedSpotEvent event = null;
		if(click)
			event = new ClickedSpotEvent(this, ClickedSpotEvent.CLICK);
		else
			event = new ClickedSpotEvent(this, ClickedSpotEvent.FLAG);
		Iterator<ClickedSpotEventListener> i = listeners.iterator();
		while (i.hasNext()) {
			((ClickedSpotEventListener) i.next()).handleEvent(event);
		}
	}
	
}

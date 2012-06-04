package frontend;

import java.awt.Font;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Graphics2D;

import javax.swing.JLabel;
import javax.swing.Timer;

import backend.Minesweeper;

/**
 * TimeDisplay is a Frontend display for the timer in Minesweeper.
 * It is a JLabel and implements ActionListener, listening for the
 * Timer in the Minesweeper game to trigger the intervals (tenths of
 * a second) a which point it refreshes itself with the time that
 * is being stored in the Minesweeper class. It displays this time
 * as text through the JLabel functionality.
 * @author Andrew Dai (Bunsen Mcdubbs)
 *
 */

public class TimeDisplay extends JLabel implements ActionListener{
	
	public static final int TEXT_HEIGHT = 36;
	
	private Minesweeper game;
	
	/**
	 * Constructor for the TimeDisplay class that takes a Minesweeper
	 * game object to represent the time for.
	 * @param m
	 */
	public TimeDisplay(Minesweeper m){
		super();
		super.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 40));
		super.setHorizontalAlignment(JLabel.CENTER);
		setGame(m);
	}
	
	/**
	 * Setter for the game variable
	 * @param game
	 */
	public void setGame(Minesweeper game){
		this.game = game;
		game.getTimer().addActionListener(this);
		refresh();
	}
	
	/**
	 * Converts an integer representation of time (in tenths of a second)
	 * into a formatted String.
	 * @param time in tenths of a second
	 * @return formatted string
	 */
	public static String tenthsToString(int t){
		String s = "";
		//Minutes
		s += t / 600;
		s += ":";
		//Seconds
		if(((t % 600) / 10) < 10)
			s += "0";
		s += (t % 600) / 10;
		s += ".";
		//Tenths of a Second
		s += (t % 600) % 10;
		
		return s;
	}
	
	/**
	 * Refreshes the TimeDisplay by getting the time from the Minesweeper game
	 * and refreshing the text in the JLabel
	 */
	public void refresh(){
		int time = game.getTime();
		super.setText(tenthsToString(time));
		repaint();
	}
	
	/**
	 * Triggers the refresh when the timer triggers an event every .1 seconds
	 * or 100 milliseconds
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		refresh();
	}
	
	
	
}

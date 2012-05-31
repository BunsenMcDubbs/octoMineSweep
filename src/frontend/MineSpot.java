package frontend;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JComponent;
import javax.swing.SwingUtilities;

import backend.Minesweeper;
import backend.Spot;
import backend.event.ClickedSpotEvent;
import backend.event.ClickedSpotEventListener;
import backend.event.GameEndEvent;
import backend.event.GameEndListener;

/**
 * Graphical representation of a single spot in a Minesweeper game. It
 * extends JComponent. It is a square block set to certain colors
 * depending on the conditions or with the number of bombs around it
 * if it is opened.
 * @author Andrew Dai (BunsenMcdubbs)
 *
 */

@SuppressWarnings("serial")
public class MineSpot extends JComponent implements 
	MouseListener, ClickedSpotEventListener, GameEndListener {

	public static final int SIZE = 40;
	public static final Color HIGHLIGHT = Color.CYAN;
	public static final Color CLOSED = Color.GRAY;
	public static final Color FLAG = Color.YELLOW;
	public static final Color BOMB = Color.BLACK;

	private Spot spot;
	private Minesweeper game;
	private boolean enabled;
	private boolean highlight;
	
	/**
	 * Constructor for the MineSpot
	 * Precondition: the spot has to be part of the Minesweeper game
	 * @param s - spot to represent
	 * @param m - minesweeper game that contains the spot
	 */
	public MineSpot(Spot s, Minesweeper m) {
		setSpot(s);
		setGame(m);
		addMouseListener(this);
		spot.addEventListener(this);
		game.addEventListener(this);
		enabled = true;
		highlight = false;
	}

	/**
	 * Sets the minesweeper game to the given game
	 * @param m
	 */
	private void setGame(Minesweeper m) {
		game = m;
	}

	/**
	 * Sets the represented Spot to the given Spot
	 * @param s
	 */
	private void setSpot(Spot s) {
		spot = s;
	}

	/**
	 * Paints the MineSpot.
	 * If the mouse is hovered over a closed spot the spot's color is
	 * highlighted according to the constant HIGHLIGHT. Otherwise the
	 * color is the constant CLOSED. If the spot is an opened bomb it
	 * is set to the constant BOMB, if it isn't then there is a number
	 * centered in the component that is the number of bombs surrounding
	 * that spot. Flagged spots are yellow and if the game has ended
	 * and the spot was incorrectly flagged then the color is set to
	 * pink.
	 * 
	 */
	public void paint(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,  
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		g2.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 25));

		if (spot.isOpen()) {
			if(spot.isBomb()){
				g2.setColor(BOMB);
				g2.fill3DRect(0, 0, MineSpot.SIZE, MineSpot.SIZE, true);
			}
			else{
				g2.setColor(Color.blue);
				switch (spot.getState()) {
				case 0:
					return;
				case 1:
					g2.setColor(Color.BLUE); break;
				case 2:
					g2.setColor(Color.GREEN); break;
				case 3:
					g2.setColor(Color.RED); break;
				case 4:
					g2.setColor(Color.MAGENTA); break;
				case 5:
					g2.setColor(Color.CYAN); break;
				case 6:
					g2.setColor(Color.ORANGE); break;
				case 7:
					g2.setColor(Color.PINK); break;
				case 8:
					g2.setColor(Color.YELLOW); break;
				}
				String s = "" + spot.getState();
				
				//The following block is Copyright 2003 Fred Swartz MIT License
				//http://leepoint.net/notes-java/GUI-appearance/fonts/18font.html
				{
					// Find the size of string s in font f in the current Graphics context g.
					FontMetrics fm   = g2.getFontMetrics(g2.getFont());
					java.awt.geom.Rectangle2D rect = fm.getStringBounds(s, g);
	
					int textHeight = (int)(rect.getHeight()); 
					int textWidth  = (int)(rect.getWidth());
					int panelHeight= this.getHeight();
					int panelWidth = this.getWidth();
	
					// Center text horizontally and vertically
					int x = (panelWidth  - textWidth)  / 2;
					int y = (panelHeight - textHeight) / 2  + fm.getAscent();
	
					g.drawString(s, x, y);  // Draw the string.
				}
			}
		}
		else{
			if(highlight){
				g2.setColor(HIGHLIGHT);
			}
			else if(spot.isFlagged()){
				g2.setColor(FLAG);
				if(!game.isEnabled() && !spot.isBomb())
					g2.setColor(Color.PINK);
			}
			else
				g2.setColor(CLOSED);
			g2.fill3DRect(0, 0, MineSpot.SIZE, MineSpot.SIZE, true);

		}
	}

	public Spot getSpot() {
		return spot;
	}


	/**
	 * Event Listener for the Spot's opening event
	 */
	@Override
	public void handleEvent(ClickedSpotEvent e) {
		repaint();
		if(e.getStatus() == ClickedSpotEvent.CLICK)
			setEnabled(false);
	}

	/**
	 * Event listener for the game's GameEndEvent, when the event is fired
	 * then this spot is disabled.
	 */
	@Override
	public void handleEvent(GameEndEvent e) {
		setEnabled(false);
	}

	/**
	 * Setter for whether or not this minespot is enabled
	 */
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	/**
	 * @return whether or not this minespot is enabled
	 */
	public boolean isEnabled() {
		return enabled;
	}

	/**
	 * @deprecated
	 */
	@Override
	public void mouseClicked(MouseEvent e) {}

	/**
	 * If the mouse is hovered over this spot then the highlight flag
	 * is set to true.
	 * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseEntered(MouseEvent e) {
		highlight = true;
		repaint();
	}

	/**
	 * Once the mouse has exited this MineSpot then the highlight flag
	 * is set to false;
	 * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseExited(MouseEvent e) {
		highlight = false;
		repaint();
	}

	/**
	 * If the mouse is pressed (clicked) on this MineSpot then it is either
	 * 1) opened if it was a left click </br>
	 * 2) flagged if it is closed and it was a right click </br>
	 * or 3) opens all surrounding unflagged cells if it was a dual click 
	 * and the spot is open</br>
	 */
	@Override
	public void mousePressed(MouseEvent e) {
		
		if(!game.isEnabled()) return;
		
		switch(e.getButton()){
		case MouseEvent.BUTTON1: System.out.println("1"); break;
		case MouseEvent.BUTTON3: System.out.println("3"); break;
		}
		
		if(e.getModifiersEx() == 5120){
			if(spot.isOpen()){
				
			}
		}
		
		else if(SwingUtilities.isLeftMouseButton(e)){
			if(!spot.isFlagged()){
				game.open(spot.loc);
			}
		}
		else if (SwingUtilities.isRightMouseButton(e) && game.isEnabled()){
			if(!spot.isOpen())
				spot.toggleFlag();
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {}
}

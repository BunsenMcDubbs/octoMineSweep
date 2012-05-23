package frontend;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.JToggleButton;

import backend.*;
import backend.event.*;

// TODO stop extending Jtogglebutton
@SuppressWarnings("serial")
public class MineSpot extends JToggleButton implements ActionListener,
		ClickedSpotEventListener, GameEndListener {

	public static final int SIZE = 40;

	private Spot spot;
	private Minesweeper game;

	public MineSpot(Spot s, Minesweeper m) {
		setSpot(s);
		setGame(m);
		addActionListener(this);
		spot.addEventListener(this);
		game.addEventListener(this);
	}

	private void setGame(Minesweeper m) {
		game = m;
	}

	public void setSpot(Spot s) {
		spot = s;
	}

	@Override
	public void paint(Graphics g) {
		if (spot.isOpen()) {
			Graphics2D g2 = (Graphics2D) g;
			switch (spot.getState()) {
			case 0:
				g2.setColor(Color.WHITE); break;
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
			case -1:
				g2.setColor(Color.DARK_GRAY); break;
			}
			g2.fill3DRect(10, 10, 30, 30, true);
//			g2.drawString("herro", 0, 0);
		}
		else
			super.paint(g);
	}

	public Spot getSpot() {
		return spot;
	}

	/**
	 * Action Listener for the MineSpot button
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		
		if (!game.isActive()){
			if(!spot.isFlagged())
				game.open(spot.loc);
			else return;
		}
		else
			spot.discreteOpen();
	}

	/**
	 * Event Listener for the Spot's opening event
	 */
	@Override
	public void handleEvent(ClickedSpotEvent e) {
		repaint();
		doClick(0);
	}

	@Override
	public void handleEvent(GameEndEvent e) {
		setEnabled(false);
	}
}

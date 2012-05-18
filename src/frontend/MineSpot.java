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

@SuppressWarnings("serial")
public class MineSpot extends JToggleButton implements ActionListener, OpenedSpotEventListener, GameOverListener {
	
	public static final int SIZE = 50;
	
	private Spot spot;
	private Minesweeper game;
	
	public MineSpot(Spot s, Minesweeper m){
		setSpot(s);
		setGame(m);
		addActionListener(this);
		spot.addEventListener(this);
	}
	
	private void setGame(Minesweeper m) {
		game = m;
	}

	public void setSpot(Spot s){
		spot = s;
	}
	
	@Override
	public void paint(Graphics g){
		if(spot.isOpen()){
			Graphics2D g2 = (Graphics2D)g;
			g2.fillRect(10,10,10,10);
		}
		else
			super.paint(g);
	}
	
	public Spot getSpot(){
		return spot;
	}

	/**
	 * Action Listener for the MineSpot button
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		if(!game.gameIsOver())
			game.open(spot.loc);
		else
			spot.discreteOpen();
	}

	/**
	 * Event Listener for the Spot's opening event
	 */
	@Override
	public void handleEvent(OpenedSpotEvent e) {
		repaint();
		doClick(0);
	}

	@Override
	public void handleEvent(GameOverEvent e) {
		setEnabled(false);
	}
}

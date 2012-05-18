package frontend;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import backend.*;
import backend.event.*;

@SuppressWarnings("serial")
public class MineSpot extends JButton implements ActionListener, OpenedSpotEventListener {
	
	public static final int SIZE = 50;
	
	private Spot spot;
	
	public MineSpot(Spot s){
		setSpot(s);
		addActionListener(this);
		spot.addEventListener(this);
	}
	
	public void setSpot(Spot s){
		spot = s;
	}
	
	@Override
	public void paint(Graphics g){
		if(spot.isOpen()){
			Graphics2D g2 = (Graphics2D)g;
		}
		else
			super.paint(g);
	}

	/**
	 * Action Listener for the MineSpot button
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		spot.discreetOpen();
	}

	/**
	 * Event Listener for the Spot's opening event
	 */
	@Override
	public void handleEvent(OpenedSpotEvent e) {
		repaint();
	}

	
}

package frontend;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JToggleButton;

import backend.*;
import backend.event.*;

// TODO stop extending Jtogglebutton
@SuppressWarnings("serial")
public class MineSpot extends JComponent implements 
	MouseListener, ClickedSpotEventListener, GameEndListener {

	public static final int SIZE = 40;

	private Spot spot;
	private Minesweeper game;
	private boolean enabled;
	
	public MineSpot(Spot s, Minesweeper m) {
		setSpot(s);
		setGame(m);
		addMouseListener(this);
		spot.addEventListener(this);
		game.addEventListener(this);
		enabled = true;
	}

	private void setGame(Minesweeper m) {
		game = m;
	}

	public void setSpot(Spot s) {
		spot = s;
	}

	public void paint(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		if (spot.isOpen()) {
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
//			case Spot.FLAG:
//				g2.setColor(Color.PINK); break;
			}
			if(spot.isFlagged())
				g2.setColor(Color.PINK);
			g2.fill3DRect(0, 0, MineSpot.SIZE, MineSpot.SIZE, true);
			g2.drawString("herro", 0, 0);
		}
		else{
			g2.setColor(Color.DARK_GRAY);
			g2.fill3DRect(0, 0, MineSpot.SIZE, MineSpot.SIZE, true);
		}
	}

	public Spot getSpot() {
		return spot;
	}

	/**
	 * Action Listener for the MineSpot button
	 */
/*	@Override
	public void actionPerformed(ActionEvent e) {
		
		if (!game.isActive()){
			if(!spot.isFlagged())
				game.open(spot.loc);
			else return;
		}
		else
			spot.discreteOpen();
	}*/

	/**
	 * Event Listener for the Spot's opening event
	 */
	@Override
	public void handleEvent(ClickedSpotEvent e) {
		repaint();
		if(e.getStatus() == ClickedSpotEvent.CLICK)
			setEnabled(false);
	}

	@Override
	public void handleEvent(GameEndEvent e) {
		setEnabled(false);
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public boolean isEnabled() {
		return enabled;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
//		if (enabled) {
//			if(e.getButton() == MouseEvent.BUTTON1){
//				if(!spot.isFlagged())
//					game.open(spot.loc);
//			}
//			else if (e.getButton() == MouseEvent.BUTTON3){
//				System.out.println("L:KEjrl;KEJFL:KEJRL:KJ");
//				spot.toggleFlag();
//			}
//		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		//highlight
	}

	@Override
	public void mouseExited(MouseEvent e) {
		//unhighlight
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if (enabled) {
			if(e.getButton() == MouseEvent.BUTTON1){
				if(!spot.isFlagged())
					game.open(spot.loc);
			}
			else if (e.getButton() == MouseEvent.BUTTON3){
				System.out.println("L:KEjrl;KEJFL:KEJRL:KJ");
				spot.toggleFlag();
			}
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {}
}

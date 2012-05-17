package frontend;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import backend.*;

public class MineSpot extends JButton implements ActionListener {
	
	public static final int SIZE = 50;
	
	private Spot spot;
	
	public MineSpot(Spot s){
		setSpot(s);
		addActionListener(this);
	}
	
	public void setSpot(Spot s){
		spot = s;
	}
	
	@Override
	public void paint(Graphics g){
		if(spot.isOpen()){
			Graphics2D g2 = (Graphics2D)g;
			g2.setColor(Color.BLACK);
//			g2.drawString("" + spot.getState(), 0, 0);
			g2.fillRect(10,10,10,10);
		}
		else
			super.paint(g);
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		spot.open();
	}

	
}

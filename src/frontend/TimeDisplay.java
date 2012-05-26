package frontend;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.Timer;

import backend.Minesweeper;

public class TimeDisplay extends JLabel implements ActionListener{
	
	public static final int TEXT_HEIGHT = 36;
	
	private Minesweeper game;
	
	public TimeDisplay(Minesweeper m){
		super();
		super.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, TEXT_HEIGHT));
		super.setHorizontalAlignment(JLabel.CENTER);
		game = m;
		m.getTimer().addActionListener(this);
		refresh();
	}
	
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
	
	private void refresh(){
		int time = game.getTime();
		super.setText(tenthsToString(time));
		repaint();
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		refresh();
	}
	
	
	
}

package frontend;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.Timer;

import backend.*;

public class MineFrame extends JFrame {
	
	private Minesweeper game;
	private Timer timer;
	
	private MineField mF;
	
	public MineFrame(){
		super("Minesweeper");
		setTitle("Minesweeper");
		setLayout(new BorderLayout());
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		backend();
		selector();
		minefield();
		
		pack();
		
		setSize(new Dimension(100,100));
		
		setVisible(true);
		
	}

	private void backend() {
		game = new Minesweeper();
		timer = game.getTimer();
	}

	private void selector() {
		// TODO Auto-generated method stub
		
	}

	private void minefield() {
		mF = new MineField(game);
		this.add(mF);
	}
	
}

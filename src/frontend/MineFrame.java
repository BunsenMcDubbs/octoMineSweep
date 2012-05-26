package frontend;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

import backend.*;

public class MineFrame extends JFrame implements ActionListener{
	
	public static final Color bg = Color.DARK_GRAY;
	
	private Minesweeper game;
	private JPanel info;
	
	private MineField mF;
	
	public MineFrame(){
		super("Minesweeper");
		setTitle("Minesweeper");
		setLayout(new BorderLayout());
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		backend();
		selector();
		minefield();
		
		infoPanel();
		
		setSize();
		
		setVisible(true);
		
	}

	private void infoPanel() {
		info = new JPanel(new BorderLayout());
		//Adding the TimerDisplay to the top
		info.add(new TimeDisplay(game), BorderLayout.CENTER);
		
		JButton pause = new JButton("Pause");
		JButton restart = new JButton("Restart");
		pause.setActionCommand("pause");
		restart.setActionCommand("restart");
		pause.addActionListener(this);
		restart.addActionListener(this);
		
		info.add(pause, BorderLayout.EAST);
		info.add(restart, BorderLayout.WEST);
		
		add(info, BorderLayout.NORTH);
	}

	private void setSize() {
		Dimension s = new Dimension(mF.getSize().width, mF.getSize().height + TimeDisplay.TEXT_HEIGHT);
		setSize(s);
		setResizable(false);
	}
	
	private void backend(){
		backend(1);
	}

	private void backend(int d) {
		setGame(new Minesweeper(d));
	}

	private void selector() {
		// TODO make a selection screen
		
	}

	private void minefield() {
		mF = new MineField(game);
		this.add(mF, BorderLayout.CENTER);
		repaint();
	}
	
	public Minesweeper getGame(){
		return game;
	}
	
	public void setGame(Minesweeper m){
		game = m;
	}
	
	public MineField getField(){
		return mF;
	}
	
	public void setDifficulty(int d) {
		// TODO bugs with refreshing and generating new displays
		Minesweeper newGame = new Minesweeper(d);
		setGame(newGame);
		remove(mF);
		minefield();
		setSize();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equals("pause")){
			game.stopGame();
		}
		else if(e.getActionCommand().equals("restart")){
			System.out.println("Restart");
			// TODO restart implementation
		}
	}

}

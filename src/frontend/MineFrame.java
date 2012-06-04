package frontend;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;

import backend.*;

public class MineFrame extends JFrame implements ActionListener {

	public static final Color bg = Color.DARK_GRAY;

	private Minesweeper game;
	private JPanel info;

	private MineField mF;

	public MineFrame() {
		super("Minesweeper");
		setTitle("Minesweeper");
		setLayout(new BorderLayout());
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		init();

		setVisible(true);

	}

	private void init() {
		backend();
		selector();
		minefield();
		infoPanel();
		menubar();
		setSize();
	}

	private void menubar() {
		JMenuBar jmb = new JMenuBar();
		JMenu gameOptions = new JMenu("Game");
		JMenu highscores = new JMenu("Highscores");

		jmb.add(gameOptions);
		jmb.add(highscores);

		JMenuItem newGame = new JMenuItem("New Game");
		JMenuItem cDiff = new JMenuItem("Change Difficulty");
		JMenuItem pause = new JMenuItem("Pause");

		newGame.setActionCommand("restart");
		cDiff.setActionCommand("change diff");
		pause.setActionCommand("pause");

		newGame.addActionListener(this);
		cDiff.addActionListener(this);
		pause.addActionListener(this);

		gameOptions.add(newGame);
		gameOptions.add(cDiff);
		gameOptions.add(pause);

		setJMenuBar(jmb);
	}

	private void infoPanel() {
		info = new JPanel(new BorderLayout());
		// Adding the TimerDisplay to the top
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
		Dimension s = new Dimension(mF.getSize().width, mF.getSize().height +
		TimeDisplay.TEXT_HEIGHT + 20);
		setSize(s);
		
		//------------------------------
		
		pack();
		setResizable(false);
	}

	private void backend() {
		backend(changeDifficulty(true));
	}

	private void backend(int d) {
		setGame(new Minesweeper(d));
		System.out.println(game.testString());
	}

	private void selector() {
		// JOptionPane.
	}

	private void minefield() {
		mF = new MineField(game);
		add(mF, BorderLayout.CENTER);
		repaint();
	}

	public Minesweeper getGame() {
		return game;
	}

	public void setGame(Minesweeper m) {
		game = m;
	}

	public MineField getField() {
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
		if (e.getActionCommand().equalsIgnoreCase("pause")) {
			game.stopGame();
		} else if (e.getActionCommand().equalsIgnoreCase("restart")) {
			game.stopGame();
			if (game.isEnabled()) {
				if (JOptionPane.showConfirmDialog(this,
						"Are you sure you want to restart the game?",
						"Restart", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)
					restart();
			} else
				restart();
		} else if (e.getActionCommand().equalsIgnoreCase("change diff")) {
			restart(changeDifficulty(false));
		}
	}

	private int changeDifficulty(boolean newGame){
		String title = "Choose Difficulty Level";
		
		String[] options = new String[5];
		if(newGame)
			options = new String[4];
		options[0] = "Easy";
		options[1] = "Medium";
		options[2] = "Hard";
		options[3] = "Custom";
		if(!newGame)
			options[4] = "Cancel";
		
		JOptionPane pane = new JOptionPane(title,
				JOptionPane.QUESTION_MESSAGE, JOptionPane.DEFAULT_OPTION,
				null, options);
		if(!newGame)
			pane.setInitialSelectionValue(options[4]);
		JDialog dialog = pane.createDialog(this, title);
		dialog.setVisible(true);
		Object selectedValue = pane.getValue();
		if(selectedValue == null)
			return 0;
		if(!newGame && selectedValue == options[4])
			return 0;
		for(int counter = 0, maxCounter = options.length;
		   counter < maxCounter; counter++) {
		   if(options[counter].equals(selectedValue))
			   return ++counter;
		}
		return 0;

	}

	private void restart(int d) {

		if (d == 0) {
			d = game.getDifficulty();
		} else if (d != (1 | 2 | 3)) {
			return;
		}

		System.out.println(game.status());

		remove(mF);

		backend(d);
		minefield();

		Component[] infoParts = info.getComponents();
		for (Component c : infoParts) {
			if (c instanceof TimeDisplay) {
				((TimeDisplay) c).setGame(game);
				break;
			}
		}

		Dimension temp = getSize();
		setSize(0, 0);
		setSize(temp);

		System.out.println(game.status());

	}

	private void restart() {

		restart(0);

	}

}

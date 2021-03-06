package frontend;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.ArrayList;

import javax.management.InvalidAttributeValueException;
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
		setResizable(true);
		pack();
		setResizable(false);
	}

	private void backend() {
		backend(changeDifficulty(true));
	}

	private void backend(int d) {
		if(d == 4){
			setGame(customBackend());
		}
		else if(d == -1){
			if(game != null)
				return;
			backend(changeDifficulty(game == null));
			return;
		}
		else{
			setGame(new Minesweeper(d));
		}
		System.out.println(game.testString());
	}

	private Minesweeper customBackend() {
		Minesweeper game = null;
		int rows = 0, columns = 0, bombs = 0;
		boolean works = true;
		boolean isNumber = true;
		do {
			works = true;
			do {
				isNumber = true;
				try {
					String sRows = (String) JOptionPane.showInputDialog(this,
							"Number of rows", "Custom Game",
							JOptionPane.QUESTION_MESSAGE);
					rows = Integer.parseInt(sRows);
				} catch (NumberFormatException e) {
					System.out.println("fail");
					isNumber = false;
				}
				System.out.println(isNumber);
			} while (!isNumber);
			do {
				isNumber = true;
				try {
					String sColumns = (String) JOptionPane.showInputDialog(
							this, "Number of columns", "Custom Game",
							JOptionPane.QUESTION_MESSAGE);
					columns = Integer.parseInt(sColumns);
				} catch (NumberFormatException e) {
					isNumber = false;
				}
			} while (!isNumber);
			do {
				isNumber = true;
				try {
					String sBombs = (String) JOptionPane.showInputDialog(this,
							"Number of bombs", "Custom Game",
							JOptionPane.QUESTION_MESSAGE);
					bombs = Integer.parseInt(sBombs);
				} catch (NumberFormatException e) {
					isNumber = false;
				}
			} while (!isNumber);
			try{
				System.out.println(rows);
				System.out.println(columns);
				System.out.println(bombs);
				game = new Minesweeper(rows, columns, bombs);
			} catch(InvalidAttributeValueException e){
				works = false;
				System.out.println(e.getMessage());
			}
		} while (!works);
		return game;
	}

	private void minefield() {
		MineField m = new MineField(game);
		add(m, BorderLayout.CENTER);
		mF = m;
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
			game.stopGame();
			restart(changeDifficulty(false));
		}
	}

	private int changeDifficulty(boolean newGame){
		String title = "Choose Difficulty Level";
		
		String[] options = new String[4];
		if(newGame)
			options = new String[3];
		options[0] = "Easy";
		options[1] = "Medium";
		options[2] = "Hard";
//		options[3] = "Custom";
		if(!newGame)
			options[3] = "Cancel";
		
		JOptionPane pane = new JOptionPane(title,
				JOptionPane.QUESTION_MESSAGE, JOptionPane.DEFAULT_OPTION,
				null, options);
		if(!newGame)
			pane.setInitialSelectionValue(options[3]);
		JDialog dialog = pane.createDialog(this, title);
		dialog.setVisible(true);
		Object selectedValue = pane.getValue();
		if(selectedValue == null)
			return -1;
		if(selectedValue.equals("Cancel"))
			return -1;
		if (selectedValue.equals("Easy")) return 1;
		if (selectedValue.equals("Medium")) return 2;
		if (selectedValue.equals("Hard")) return 3;
//		if (selectedValue.equals("Custom")) return 4;
		return -1;

	}

	public void restart(int d) {
		
		System.out.println("\n\nCHANGE DIFFICULTY TO " + d + "\n\n\n");
		
		if (d == 0) {
			d = game.getDifficulty();
		}

		System.out.println("Current Game status:\n" + game.status());

		Minesweeper temp = game;
		backend(d);
		if(temp == game)
			return;
		remove(mF);
		minefield();

		Component[] infoParts = info.getComponents();
		for (Component c : infoParts) {
			if (c instanceof TimeDisplay) {
				((TimeDisplay) c).setGame(game);
				break;
			}
		}
		
		setSize();

		System.out.println("NEW Game status:\n" + game.status());

	}

	private void restart() {

		restart(0);

	}

}

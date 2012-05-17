package frontend;

import javax.swing.JComponent;
import backend.*;

public class MineField extends JComponent {
	
	private Minesweeper game;
	
	public MineField(Minesweeper game){
		setGame(game);
		setUp();
	}
	
	private void setUp() {
		// TODO Auto-generated method stub
		
	}

	public void setGame(Minesweeper g){
		game = g;
	}
	
}

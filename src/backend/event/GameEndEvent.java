package backend.event;

import java.util.EventObject;

import backend.*;

public class GameEndEvent extends EventObject {

	private boolean win;
	
	public GameEndEvent(Minesweeper m, boolean win) {
		super(m);
		this.win = win;
	}
	
	public boolean isWinner(){
		return win;
	}

}

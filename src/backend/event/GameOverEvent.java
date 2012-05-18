package backend.event;

import java.util.EventObject;

import backend.*;

public class GameOverEvent extends EventObject {

	public GameOverEvent(Minesweeper m) {
		super(m);
	}

}

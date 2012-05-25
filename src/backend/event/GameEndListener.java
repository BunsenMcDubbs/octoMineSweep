package backend.event;

import java.util.EventListener;

public interface GameEndListener extends EventListener {
	
	public void handleEvent(GameEndEvent e);
	
}

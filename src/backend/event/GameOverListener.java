package backend.event;

import java.util.EventListener;

public interface GameOverListener extends EventListener {
	
	public void handleEvent(GameOverEvent e);
	
}

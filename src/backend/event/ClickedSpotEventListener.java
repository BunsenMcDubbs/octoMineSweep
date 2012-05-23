package backend.event;

import java.util.EventListener;

public interface ClickedSpotEventListener extends EventListener{
	
	public void handleEvent(ClickedSpotEvent e);
	
}

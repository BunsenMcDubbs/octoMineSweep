package backend.event;

import java.util.EventListener;

public interface OpenedSpotEventListener extends EventListener{
	
	public void handleEvent(OpenedSpotEvent e);
	
}

package backend.event;

import java.util.EventObject;

import backend.*;

public class ClickedSpotEvent extends EventObject {

	public ClickedSpotEvent(Spot source) {
		super(source);
	}


}

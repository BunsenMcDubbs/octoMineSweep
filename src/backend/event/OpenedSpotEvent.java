package backend.event;

import java.util.EventObject;

import backend.*;

public class OpenedSpotEvent extends EventObject {

	public OpenedSpotEvent(Spot source) {
		super(source);
	}


}

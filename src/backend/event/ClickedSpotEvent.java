package backend.event;

import java.util.EventObject;

import backend.*;

public class ClickedSpotEvent extends EventObject {

	public static final int CLICK = 1;
	public static final int FLAG = -1;
	
	private int status;

	public ClickedSpotEvent(Spot source, int status) {
		super(source);
		setStatus(status);
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getStatus() {
		return status;
	}


}

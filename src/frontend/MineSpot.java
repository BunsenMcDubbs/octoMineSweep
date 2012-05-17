package frontend;

import javax.swing.JButton;
import backend.*;

public class MineSpot extends JButton {
	
	public static final int SIZE = 50;
	
	private Spot spot;
	
	public MineSpot(Spot s){
		setSpot(s);
	}
	
	public void setSpot(Spot s){
		spot = s;
	}
	
}

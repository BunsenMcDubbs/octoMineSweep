package test;

import info.gridworld.grid.Location;

import java.awt.Component;

import javax.swing.Timer;

import backend.*;
import frontend.*;

public class Gui {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		MineFrame m = new MineFrame();
		// TODO fix updating difficulty/new game refreshing functionality
//		mF.setDifficulty(1);
		
//		for( Component c : mF.getField().getComponents())
//			if(!((MineSpot)c).getSpot().isBomb())
//				((MineSpot)c).getSpot().open();
		m.repaint();
		System.out.println(m.getGame().testString());
	}

}

package test;

import info.gridworld.grid.Location;

import java.awt.Component;

import javax.swing.JOptionPane;
import javax.swing.Timer;

import backend.*;
import frontend.*;

public class Gui {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println(JOptionPane.CANCEL_OPTION);
		MineFrame m = new MineFrame();
		
//		for( Component c : m.getField().getComponents())
//			if(!((MineSpot)c).getSpot().isBomb())
//				m.getGame().open(((MineSpot)c).getSpot().loc);
		
//		System.out.println(m.getGame().testString());
	}

}

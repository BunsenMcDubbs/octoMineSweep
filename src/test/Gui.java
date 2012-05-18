package test;

import backend.*;
import frontend.*;

public class Gui {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		MineFrame mF = new MineFrame();
		System.out.println(mF.getGame().testString());
	}

}

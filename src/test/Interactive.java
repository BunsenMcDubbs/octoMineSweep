package test;

import java.util.Scanner;

import backend.Minesweeper;

public class Interactive {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Minesweeper m = new Minesweeper(2);
		Scanner sc = new Scanner(System.in);
		int x = 0 , y = 0;
		while(true){
			System.out.println(m);
//			System.out.println(m.testString());
			System.out.print("\nX coordinate\t");
			x = sc.nextInt();
			if(x == -1)
				return;
			System.out.print("\nY coordinate\t");
			y = sc.nextInt();
			if(y == -1)
				continue;
			m.open(x,y);
		}
	}

}

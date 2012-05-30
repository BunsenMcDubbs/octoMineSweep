package backend.highscore;

import java.io.*;

public class HighscoreIndex {

	private HighscoreItem[][] scores;

	public HighscoreIndex() {
		scores = new HighscoreItem[3][10];

		init();
	}

	private void init() {

	}

	public boolean isNewHighscore(int time) {
		return false;
	}

	public void addNewHighscore(HighscoreItem s) {

	}
	
	public void save() throws IOException {
		Writer output = null;
		String text = "";
		for(int level = 0; level < scores.length; level++){
			text += "" + (1+level) + "\n";
			for(int i = 0; i < scores[0].length; i++){
//				if(scores[level][i] == null)
//					break;
//				text += scores[level][i].toString();
				text += "Aayush " + i + "\n";
			}
		}
		File file = new File("score.txt");
		output = new BufferedWriter(new FileWriter(file));
		output.write(text);
		output.close();
	}
	
	public HighscoreItem[] getScores(int difficulty){
		HighscoreItem[] list = null;
		switch(difficulty){
		case 1: list = scores[0];
		case 2: list = scores[1];
		case 3: list = scores[2];
		}
		return list;
	}
	
	public static void main(String[] a){
		try {
			new HighscoreIndex().save();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}

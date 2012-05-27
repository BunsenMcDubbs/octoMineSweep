package backend.highscore;

public class HighscoreIndex {

	private HighscoreItem[] easy;
	private HighscoreItem[] medium;
	private HighscoreItem[] hard;

	public HighscoreIndex() {
		easy = new HighscoreItem[10];
		medium = new HighscoreItem[10];
		hard = new HighscoreItem[10];

		init();
	}

	private void init() {
		// TODO Auto-generated method stub

	}

	public boolean isNewHighscore(int time) {
		return false;
	}

	public void addNewHighscore(HighscoreItem s) {

	}

}

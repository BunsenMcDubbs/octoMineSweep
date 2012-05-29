package backend.highscore;

import java.io.*;
import java.util.ArrayList;

public class HighscoreIndex {

	public static final File f = new File("score.txt");

	private ArrayList<HighscoreItem> easy;
	private ArrayList<HighscoreItem> medium;
	private ArrayList<HighscoreItem> hard;
	
	public HighscoreIndex() {
		init();
	}

	private void init() {
		
		easy = new ArrayList<HighscoreItem>();
		medium = new ArrayList<HighscoreItem>();
		hard = new ArrayList<HighscoreItem>();
		
		try {
			load();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	public boolean isNewHighscore(int difficulty, int time) {
		ArrayList<HighscoreItem> curr = new ArrayList<HighscoreItem>();
		switch(difficulty){
		case 1: curr = easy; break;
		case 2: curr = medium; break;
		case 3: curr = hard; break;
		}
		if(curr.isEmpty())
			return true;
		for(HighscoreItem h : curr){
			if(time < h.getTime())
				return true;
		}
		return false;
	}

	public void addNewHighscore(int difficulty, HighscoreItem s) {
		if(!isNewHighscore(difficulty, s.getTime()))
			return;
		int i = 0;
		ArrayList<HighscoreItem> curr = new ArrayList<HighscoreItem>();
		switch(difficulty){
		case 1: curr = easy; break;
		case 2: curr = medium; break;
		case 3: curr = hard; break;
		}
		for(; i < curr.size(); i++){
			if(s.getTime() < curr.get(i).getTime()){
				curr.add(i, s);
				break;
			}
		}
	}

	public void load() throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(
				f));
		String line = null;
		ArrayList<HighscoreItem> curr = new ArrayList<HighscoreItem>();
		while ((line = reader.readLine()) != null) {
			System.out.println(line);
			if (!line.contains(":")){
				System.out.println(curr);
				int level = Integer.valueOf(line);
				switch(level){
				case 1: curr = easy; break;
				case 2: curr = medium; break;
				case 3: curr = hard; break;
				}
				continue;
			}
			String[] parts = line.split(":");
			int time = Integer.valueOf(parts[1]);
			curr.add( new HighscoreItem(parts[0], time));
			
		}
		System.out.println();
	}

	public void save() throws IOException {
		Writer output = null;
		String text = "";
		ArrayList<HighscoreItem> curr = new ArrayList<HighscoreItem>();
		for (int level = 0; level < 3; level++) {
			switch(level){
			case 0: curr = easy; break;
			case 1: curr = medium; break;
			case 2: curr = hard; break;
			}
			text += "" + (1 + level) + "\n";
			for (int i = 0; i < curr.size() && i < 10; i++) {
				text += curr.get(i).toString();
				text += "\n";
			}
		}
		System.out.println(text);
		output = new BufferedWriter(new FileWriter(f));
		output.write(text);
		output.close();
	}

	public static void main(String[] a) {
		HighscoreIndex h = new HighscoreIndex();
		String[] names = {"Andrew", "Bunsen", "Brian", "Chris", "David", "Elton",
				"Franklin", "Hoang", "Linda", "Ellen", "Mary", "Lisa", "Alex",
				"Peter", "Stacy", "Julia"};
		for(int i = 0; i < 3; i++){
			for(int j = 0; j < 12; j++){
				int name = (int)(Math.random()*names.length);
				int time = (int)(Math.random()*(1+i)*200);
				HighscoreItem item = new HighscoreItem(names[name], time);
				h.addNewHighscore(i, item);
			}
		}
		System.out.println("Test==================");
		System.out.println(h.easy);
		System.out.println(h.medium);
		System.out.println(h.hard);
		System.out.println("Test==================");
		
		try {
			h.save();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}

package it.marteEngine.test.fuzzy;

import org.newdawn.slick.util.Log;

import java.io.*;

public class FuzzyUtil {

	private FuzzyUtil() {
	}

	public static void saveLevel(int levelIndex, int deadCounter) {
		try {
			// Create file
			FileWriter fstream = new FileWriter("save.dat");
			BufferedWriter out = new BufferedWriter(fstream);
			out.write(levelIndex);
			out.newLine();
			out.write(deadCounter);
			// Close the output stream
			out.close();
		} catch (IOException e) {
			Log.error(e.getMessage());
		}
	}

	public static int[] loadLevel() {
		int[] result = new int[2];
		result[0] = -1;
		result[1] = -1;
		try {
			// Create file
			FileReader fstream = new FileReader("save.dat");
			BufferedReader in = new BufferedReader(fstream);
			int level = in.read();
			in.read();
			int deadCounter = in.read();
			// Close the output stream
			in.close();
			result[0] = level;
			result[1] = deadCounter;
		} catch (IOException e) {
			Log.error(e.getMessage());
		}
		return result;
	}

	public static void deleteSave() {
		// Create file
		try {
			File f = new File("save.dat");
			f.delete();
		} catch (Exception e) {
			Log.error(e.getMessage());
		}
	}

}

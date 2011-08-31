package it.marteEngine.test.fuzzy;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.newdawn.slick.util.Log;

public class FuzzyUtil {

	private FuzzyUtil() {
	}

	public static void saveLevel(int levelIndex) {
		try {
			// Create file
			FileWriter fstream = new FileWriter("save.dat");
			BufferedWriter out = new BufferedWriter(fstream);
			out.write(levelIndex);
			// Close the output stream
			out.close();
		} catch (IOException e) {
			Log.error(e.getMessage());
		}
	}

	public static int loadLevel() {
		try {
			// Create file
			FileReader fstream = new FileReader("save.dat");
			BufferedReader in = new BufferedReader(fstream);
			int strLine = in.read();
			// Close the output stream
			in.close();
			return Integer.valueOf(strLine);
		} catch (IOException e) {
			Log.error(e.getMessage());
		}
		return -1;
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

/*
 * Ian Liu / Seegal Panchal / Daniel Peng
 * June 13, 2017
 * 
 * A class which handles reading, writing and accessing from resources.
 */
package Resources;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Formatter;
import java.util.Scanner;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

public class ResourceGetter {

	private static Formatter hsFormatter = null;

	public static Scanner getHighscoresScanner() {
		File f2 = new File(ResourceGetter.class.getProtectionDomain().getCodeSource().getLocation().getPath());
		System.out.println(f2);
		f2 = new File((f2.getParent() + "\\TankFighter_Highscores.txt").replace("%20", " "));
		System.out.println(f2.getParent());
		Scanner s = null;
		if (!f2.exists()) {
			try {
				f2.createNewFile();

			} catch (Exception e) {
				JOptionPane.showMessageDialog(null, "error creating file");
			}
		}
		// System.out.println(f2);
		try {
			s = new Scanner(f2);
		} catch (FileNotFoundException e) {
			JOptionPane.showMessageDialog(null, "error with reading highscores");
		}
		return s;
	}

	// trả a BufferedReader từ 1 filepath
	public static BufferedReader getBufferedReader(String location, boolean gettingLevels) {
		// look right beside the class
		InputStream in;
		if (!gettingLevels) {// if getting highscores
			in = new ByteArrayInputStream(location.getBytes());// ResourceGetter.class.getResourceAsStream(location);
		} else {
			in = ResourceGetter.class.getResourceAsStream(location);
		}
		// System.out.println(location);
		// convert stream to reader
		InputStreamReader isr = new InputStreamReader(in);
		// convert the reader to a buffered reader
		return new BufferedReader(isr);
	}

	// returns a BufferedImage from a filepath
	public static BufferedImage getBufferedImage(String filepath) {
		URL url = ResourceGetter.class.getResource(filepath);
		try {
			return ImageIO.read(url);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "image error " + filepath);
		}
		return null;
	}

	// writes a name and score to the highscores
	public static void writeToHighscores(String name, int score) {
		// saveHighscore(ResourceGetter.class.getResource("highscores.txt"), name,
		// score);
		File f = new File(System.getProperty("user.home") + "\\TankFighter_Highscores.txt");
		File f2 = new File(ResourceGetter.class.getProtectionDomain().getCodeSource().getLocation().getPath());
		f2 = new File((f2.getParent() + "\\TankFighter_Highscores.txt").replace("%20", " "));

		if (!f2.exists()) {
			try {
				f2.createNewFile();
			} catch (IOException e) {
				System.out.println(e);
			}

		}

		try {
			PrintWriter pw = new PrintWriter(new FileWriter(f2, true));
			pw.append(name + "\n" + score + "\n");
			pw.close();
		} catch (Exception e) {
			System.out.println(e);
		}
	}

}

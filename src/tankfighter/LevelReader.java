// Lớp tạo danh sách các level từ việc đọc bản thiết kế map từ file .txt

package tankfighter;

import GameObjects.Wall;
import GameObjects.Player;
import GameObjects.Enemy;
import GameObjects.Tank;
import Resources.ResourceGetter;
import java.io.*;
import java.util.ArrayList;

public class LevelReader {

	public LevelReader() {
	}

	/**
	 * Loads the data file into Level objects
	 * 
	 * @return an ArrayList of all the Levels
	 */
	public ArrayList<Level> getLevels() {
		// tạo 1 danh sách lưu các level
		ArrayList<Level> levels = new ArrayList<>();

		// sử dụng try/catch để bắt lỗi IO
		try {
			// tạo BufferedReader
			BufferedReader br = ResourceGetter.getBufferedReader("levels.txt", true); // Đọc file "levels.txt"
			// dòng đầu tiên của file là số lượng các level
			int numLevels = Integer.parseInt(br.readLine());
			// với từng level
			for (int level = 0; level < numLevels; level++) {
				// Tạo các thuộc tính của 1 đối tượng Level
				Player player = new Player(0, 0, 0);
				ArrayList<Wall> walls = new ArrayList<>();
				ArrayList<Enemy> enemies = new ArrayList<>();

				// Với từng hàng
				for (int row = 0; row < GameStateHandler.FRAME_HEIGHT_IN_BLOCKS; row++) {
					// chia chuỗi thành các từ đơn lẻ
					String[] characters = br.readLine().split("");
					// số nguyên cuối cùng môi dòng đại diện cho độ quay của xe tăng trên dòng đó
					// (0 = 0 degrees, 1 = 90 degrees, 2 = 180 degrees, 3 = 270 degrees)
					// số lượng xe tănng trên từng dòng
					int currTankAngleIndex = 0;
					// trong từng từ
					for (int character = 0; character < 16; character++) {
						// lây ký tự đầu tiên của các từ
						char currChar = characters[character].charAt(0);

						// thiết lập vị trí hiện tại
						int pixelRow = row * (GameStateHandler.BLOCK_HEIGHT) + 50;
						int pixelCol = character * (GameStateHandler.BLOCK_WIDTH);

						// Dựa vào ký tự đầu tiên lấy ra, ta sẽ xác định được tạo đối tượng nào
						switch (currChar) {
						case '-':// tường
							walls.add(new Wall(pixelCol, pixelRow));
							break;
						case 'B':// tường có thể phá
							walls.add(new Wall(pixelCol, pixelRow, true));
							break;
						case 'E':// địch
							enemies.add(new Enemy(pixelCol, pixelRow,
									90 * Integer.parseInt(characters[16 + currTankAngleIndex])));
							currTankAngleIndex++;
							break;
						case 'F':// địch có đạn bắn nhanh
							enemies.add(new Enemy(pixelCol, pixelRow,
									90 * Integer.parseInt(characters[16 + currTankAngleIndex]), Tank.FAST_TYPE));
							currTankAngleIndex++;
							break;
						case 'S':// địch có đạn tự tìm đường
							enemies.add(new Enemy(pixelCol, pixelRow,
									90 * Integer.parseInt(characters[16 + currTankAngleIndex]), Tank.SEEKING_TYPE));
							currTankAngleIndex++;
							break;
						case 'P':// người chơi
							player = new Player(pixelCol, pixelRow,
									90 * Integer.parseInt(characters[16 + currTankAngleIndex]));
							currTankAngleIndex++;
							break;
						}
					}

				}

				// Thêm vào danh sách các level
				levels.add(new Level(player, enemies, walls));
			} // end for each level

		} catch (IOException e) {
			System.out.println("Error: " + e);
		}

		return levels;
	}
}

package GameStates;

import Resources.ResourceGetter;
import tankfighter.GameStateHandler;
import tankfighter.Level;
import tankfighter.LevelReader;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.util.ArrayList;

public class InGame extends GameState {
	private int numLevels;
	private int currLevelIndex = 0;
	private Level currLevel;
	// initialLevels lưu trữ trạng thái ban đầu của Cấp độ.
	// vì vậy nếu bạn chết ở giữa một cấp độ, Cấp độ đó sẽ được tìm thấy trong
	// initLevels
	// để đặt lại trạng thái của Cấp về trạng thái ban đầu
	// (ví dụ: thay thế xe tăng ở vị trí ban đầu)
	private ArrayList<Level> initialLevels;
	private ArrayList<Level> levels;

	private int livesLeft;
	private boolean beatGame = false;

	// những trái tim được vẽ để tượng trưng cho số lần có thể chết
	private BufferedImage heartImage = null;

	// constructor
	public InGame() {
		super();
		super.setLogoImageFromPath("levelLogo.png");
		resetState();

		// tải hình ảnh trái tim/số mạng sống
		heartImage = ResourceGetter.getBufferedImage("heart.png");
	}

	@Override
	public int update() {
		// levelState = 1 nếu người chơi đã vượt qua cấp độ, 2 nếu họ đã chết, 0 nếu
		// ngược lại
		int levelState = currLevel.update();
		String newLogoPath = currLevel.checkChangeLogo();

		// thay đổi logo
		if (newLogoPath != null) {
			if (newLogoPath.equals("levelLogo.png")) {
				newLogoPath = "level" + (currLevelIndex + 1) + ".png";
			}
			logoImages.clear();
			logoWidths.clear();
			setLogoImageFromPath(newLogoPath);

		}
		// nếu người chơi vượt qua được cấp độ
		if (levelState == 1) {
			boolean beatGame = nextLevel();
			if (beatGame) {
				return GameStateHandler.GAME_OVER_STATE;

			} else {// nếu người chơi vẫn đang chơi
				return GameStateHandler.IN_GAME_STATE;
			}

		}
		// nếu người chơi chết
		else if (levelState == 2) {
			// nếu họ vẫn còn mạng thì trừ đi 1 mạng
			if (livesLeft > 1) {
				livesLeft--;
				// đặt lại cấp độ
				levels.set(currLevelIndex, initialLevels.get(currLevelIndex).clone());
				currLevel = levels.get(currLevelIndex);
				return 3;
			}
			// nếu hết mạng thì người chơi thua
			else {
				// Chuyển đến trạng thái GameOver
				return 1;
			}
		}
		// nếu người chơi không vượt qua được cấp độ hoặc chết, ở lại trạng thái InGame
		// gameState
		return 3;
	}

	private boolean nextLevel() {
		// nếu còn cấp độ hơn để chơi
		if (currLevelIndex < numLevels - 1) {
			currLevelIndex++;
			currLevel = levels.get(currLevelIndex);
			currLevel.startTimer();
			return false;
		}
		// nếu không, người chơi đã thắng trò chơi
		beatGame = true;
		return true;
	}

	@Override
	public void draw(Graphics2D g, ImageObserver io) {
		currLevel.drawLevel(g, currLevelIndex + 1, io);
		// vẽ tim
		// x vị trí của trái tim chúng ta đang vẽ
		int currHeartX = 290;
		for (int i = 0; i < livesLeft; i++) {
			currHeartX += 50;
			g.drawImage(heartImage, currHeartX, 615, io);

		}
		// vẽ logo
		super.drawLogo(g, io);

	}

	// đặt lại trạng thái trong trò chơi
	@Override
	public void resetState() {
		beatGame = false;
		levels = new ArrayList<>();
		livesLeft = 3;

		// tạo LevelReader mới để khởi tạo lại Cấp độ
		LevelReader lr = new LevelReader();
		initialLevels = lr.getLevels();
		numLevels = initialLevels.size();
		for (Level l : initialLevels) {
			levels.add(l.clone());
		}
		currLevelIndex = 0;
		currLevel = levels.get(0);

		// buttons
		addReturnToMenuButton();

	}

	// getters
	public Level getCurrLevel() {
		return currLevel;
	}

	public int getCurrLevelNumber() {
		return currLevelIndex + 1;
	}

	public boolean didBeatGame() {
		return beatGame;
	}
}

// Lớp xử lý các trạng thái trong game (ví dụ menu chính, điểm cao,credits,exit...)
package tankfighter;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import GameStates.*;
import Resources.ResourceGetter;

public class GameStateHandler {

	// Các biến hằng là các kích thước dài,rộng
	final public static int FRAME_WIDTH = 800;
	final public static int FRAME_HEIGHT = 600;
	final public static int FRAME_WIDTH_IN_BLOCKS = 16;
	final public static int FRAME_HEIGHT_IN_BLOCKS = 12;
	final public static int BLOCK_WIDTH = FRAME_WIDTH / FRAME_WIDTH_IN_BLOCKS;
	final public static int BLOCK_HEIGHT = FRAME_HEIGHT / FRAME_HEIGHT_IN_BLOCKS;
	// Các số nguyên đại diện cho các trạng thái game
	final public static int CREDITS_STATE = 0;
	final public static int GAME_OVER_STATE = 1;
	final public static int HIGHSCORES_STATE = 2;
	final public static int IN_GAME_STATE = 3;
	final public static int INSTRUCTIONS_STATE = 4;
	final public static int MAIN_MENU_STATE = 5;
	final public static int SETTING_STATE=6;

	// Mảng chứa các trạng thái game
	private GameState[] gameStates;
	// trạng thái game hiện tại
	private GameState currGameState;
	// chỉ số của trạng thái game hiện tại
	private int currGameStateIndex;
	// mảng chứa 2 giá trị (x,y) là vị trí con chuột
	private int[] mousePos;// [x, y]

	// Khởi tạo
	public GameStateHandler() {
		gameStates = new GameState[7];
		gameStates[0] = new Credits();
		gameStates[1] = new GameOver();
		gameStates[2] = new Highscores();
		gameStates[3] = new InGame();
		gameStates[4] = new Instructions();
		gameStates[5] = new MainMenu();
		//gameStates[6]=new Setting();

		// Tạo trạng thái mặc định là trạng thái MainMenu.
		initGameState(5);

	}

	// Khởi tạo 1 trạng thái game bất kỳ theo chỉ số
	private void initGameState(int state) {
		if (state == -1) {// -1 represents that the user clicked "exit"
			// thoát chương trình
			System.exit(0);
		} else {
			// cập nhật lại chỉ số và trạng thái hiện tại thành mới.
			currGameStateIndex = state;
			currGameState = gameStates[currGameStateIndex];

			/*
			 * Nếu chúng ta chuẩn bị đến màn hình gameOver thì chúng ta phải cập nhật level
			 * cuối cùng cho level hiện tại để trạng thái gameOver biết được người chơi đạt
			 * được đến level mấy.
			 */
			if (currGameStateIndex == GAME_OVER_STATE) {
				int lastLevel = ((InGame) gameStates[IN_GAME_STATE]).getCurrLevelNumber();
				((GameOver) currGameState).setLastLevel(lastLevel);
			}
			// Hiện trạng thái mới lên màn hình
			currGameState.resetState();
		}
	}

	// cập nhật trạng thái hiện tại
	public void update() {
		int nextState = currGameState.update(); // Trả về chỉ số trạng thái tiếp theo
		if (nextState != currGameStateIndex) {// Nếu trạng thái game bị thay đổi
			// Hiển thị trạng thái mới
			initGameState(nextState);
		}
		// Nếu người chơi thắng thì thay vì hiển thị GameOver thì sẽ hiển thị ảnh Vitory
		if (nextState == GAME_OVER_STATE && ((InGame) gameStates[IN_GAME_STATE]).didBeatGame()) {
			currGameState.setLogoImageFromPath("logos\\victoryLogo.png");
		}
	}

	// vẽ game
	public void drawGame(Graphics2D g, ImageObserver io) {
		// vẽ nền
		BufferedImage image = ResourceGetter.getBufferedImage("groundTexture.jpg");

		// Do 50 pixel đầu là phần Level rồi nền vị trí tung độ y phải tăng theo 50
		// pixel
	g.drawImage(image, 0, 0, FRAME_WIDTH, FRAME_HEIGHT + 50, io);

		// vẽ trạng thái game hiện tại
		gameStates[currGameStateIndex].draw(g, io);

	}

	// Xử lý sự kiện bàn phím và chuột
	public void keyPressed(KeyEvent e) {
		if (currGameStateIndex == IN_GAME_STATE) {// Nếu đang ở trong game
			((InGame) currGameState).getCurrLevel().keyPressed(e);
		}
	}

	public void keyReleased(KeyEvent e) {
		if (currGameStateIndex == IN_GAME_STATE) {// Nếu trong game
			((InGame) currGameState).getCurrLevel().keyReleased(e);
		}
	}

	public void mouseMoved(int[] newPos) {
		mousePos = newPos;
		if (currGameStateIndex == IN_GAME_STATE) {// Nếu trong game
			((InGame) currGameState).getCurrLevel().mouseMoved(newPos);
		}
	}

	// Nếu nhấn chuột, ta phải xử lý chính xác ô được nhấn
	public void mousePressed() {
		if (currGameStateIndex == MAIN_MENU_STATE) {// Nếu đang ở phần menu
			int nextState = ((MainMenu) gameStates[MAIN_MENU_STATE]).mousePressed(mousePos);
			if (nextState != MAIN_MENU_STATE) {// Nếu trạng thái game thay đổi, tạo trạng thái mới
				initGameState(nextState);
			}
		} else if (currGameStateIndex == IN_GAME_STATE) {// Nếu trong game
			((InGame) currGameState).getCurrLevel().mousePressed(); // Bấn chuột sẽ bắn ra đạn
		} else if (currGameStateIndex == GAME_OVER_STATE) {// Nếu đang ở màn game_over, ta sẽ có nhiều lựa chọn
			int nextState = ((GameOver) gameStates[GAME_OVER_STATE]).mousePressed(mousePos,
					((InGame) gameStates[IN_GAME_STATE]).getCurrLevelNumber(),
					(Highscores) gameStates[HIGHSCORES_STATE]);
			if (nextState != GAME_OVER_STATE) {// Nếu trạng thái tiếp theo thay đổi
				initGameState(nextState); // Hiển thị trạng thái mới
			}
		} else {
			// Kiểm tra xem người dùng nhấn đúng vùng hợp lệ của nút hay chưa
			if ((currGameState).getButtons().get(0).isInBounds(mousePos)) {

				if (currGameStateIndex == HIGHSCORES_STATE) { // Nếu đang ở phần HighScore thì cập lại tên của người
																// chơi về null,
					// điểm bằng -1
					((Highscores) currGameState).setRecentPlayerName(null);
					((Highscores) currGameState).setRecentPlayerScore(-1);
				}
				// Hiển thị trạng thái mới
				initGameState(MAIN_MENU_STATE);

			}
		}

	}

}

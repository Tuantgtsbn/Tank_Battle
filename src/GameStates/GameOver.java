package GameStates;

import GameObjects.Button;
import Resources.ResourceGetter;
import tankfighter.GameStateHandler;

import static tankfighter.GameStateHandler.FRAME_WIDTH;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.ImageObserver;
import javax.swing.JOptionPane;

public class GameOver extends GameState {
	private boolean beatGame;
	private String playerName;
	// người chơi thua ở màn này (hiển thị trên màn hình Game Over và được ghi vào
	// điểm cao)
	private int lastLevel;

	public GameOver() {
		super();
		resetState();
	}

	@Override
	public int update() {
		// trạng thái trò chơi vẫn ở trạng thái này cho đến khi nhấn nút "trở lại menu"
		return GameStateHandler.GAME_OVER_STATE;
	}

	@Override
	public void draw(Graphics2D g, ImageObserver io) {
		super.drawLogo(g, io);

		// vẽ text "YOU GOT TO LEVEL #"
		getButtons().get(0).draw(g);

		// vẽ buttons "Choose a Name", "Record Score"
		// và "Return to Main Menu"
		for (int i = 1; i < 4; i++) {
			getButtons().get(i).drawWithShadow(g);
		}
	}

	@Override
	public void resetState() {
		// tạo buttons
		super.addButton(new Button(FRAME_WIDTH / 2 - Button.STANDARD_WIDTH / 2, 200, Button.STANDARD_WIDTH,
				Button.STANDARD_HEIGHT, new Color(0, 0, 0, 0), "", Button.STANDARD_FONT_SIZE, Color.BLACK));
		super.addButton(new Button(FRAME_WIDTH / 2 - Button.STANDARD_WIDTH / 2, 250, Button.STANDARD_WIDTH,
				Button.STANDARD_HEIGHT, Button.STANDARD_BOX_COLOR, "Choose a Name", Button.STANDARD_FONT_SIZE,
				Button.STANDARD_TEXT_COLOR));
		super.addButton(new Button(FRAME_WIDTH / 2 - Button.STANDARD_WIDTH / 2, 320, Button.STANDARD_WIDTH,
				Button.STANDARD_HEIGHT, Button.STANDARD_BOX_COLOR, "Record Score", Button.STANDARD_FONT_SIZE,
				Button.STANDARD_TEXT_COLOR));
		addReturnToMenuButton();

		super.setLogoImageFromPath("gameOverLogo.png");

		getButtons().get(0).setText("YOU GOT TO LEVEL " + lastLevel);
		getButtons().get(1).setText("Choose a Name");
	}

	public int mousePressed(int[] mousePos, int score, Highscores highscores) {
		// nếu người chơi ấn button "return to main menu" thì return 5
		if (getButtons().get(3).isInBounds(mousePos))
			return 5;
		// Nếu người chơi ấn "enter name here"
		if (getButtons().get(1).isInBounds(mousePos)) {
			playerName = JOptionPane.showInputDialog("Enter Your Name Below");
			if (playerName == null) {
				playerName = "UNNAMED";
			}
			getButtons().get(1).setText(playerName);
		}
		// Nếu người chơi ấn "Record Score"
		else if (getButtons().get(2).isInBounds(mousePos)) {
			// JOptionPane.showMessageDialog(null, "recording score");
			// chúng ta cần kiểm tra xem tên đó có còn rỗng không đề phòng trường hợp họ
			// không nhấn "Choose a Name"
			if (playerName == null) {
				playerName = "UNNAMED";
			}
			writeToHighscores(playerName, lastLevel);
			// cho trang điểm cao biết tên và điểm của người chơi hiện tại
			// (để nó có thể được hiển thị và đánh dấu trong trang điểm cao)
			highscores.setRecentPlayerName(playerName);
			highscores.setRecentPlayerScore(score);

			// yêu cầu GameStatesHandler truy cập trang điểm cao
			return 2;

		}
		// nếu người chơi không muốn thay đổi trạng thái trò chơi thì vẫn ở lại màn hình
		// GameOver
		return 1;

	}

	private void writeToHighscores(String line, int score) {
		ResourceGetter.writeToHighscores(line, score);
	}

	public void setLastLevel(int lastLevel) {
		this.lastLevel = lastLevel;
	}

	public void setBeatGame(boolean beatGame) {
		this.beatGame = beatGame;
	}

}
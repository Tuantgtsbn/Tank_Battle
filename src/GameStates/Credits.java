package GameStates;

import Resources.ResourceGetter;
import tankfighter.GameStateHandler;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;

public class Credits extends GameState {

	private BufferedImage credit;// Lưu hình ảnh credit

	public Credits() {
		super();// Gọi constructor của GameState
		super.setLogoImageFromPath("creditsLogo.png");// tải logo "Credits" lên
		resetState();
	}

	@Override
	public int update() {
		return GameStateHandler.CREDITS_STATE;// trạng thái trò chơi vẫn ở trạng thái này cho đến khi nhấn nút "trở lại
												// menu"
	}

	@Override
	public void draw(Graphics2D g, ImageObserver io) {
		super.drawLogo(g, io);
		// tải ảnh credit lên
		credit = ResourceGetter.getBufferedImage("credits.png");
		g.drawImage(credit, 0, 40, null);// tải ảnh credits.png lên màn hình
		getButtons().get(0).drawWithShadow(g);// Vẽ button "Back to Main Menu"

	}

	@Override
	public void resetState() {
		getButtons().clear();// xóa các button đã tải
		addReturnToMenuButton();// loads the "Back to Main Menu" button
	}

}
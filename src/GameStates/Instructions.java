package GameStates;

import Resources.ResourceGetter;
import tankfighter.GameStateHandler;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;

public class Instructions extends GameState {

	private BufferedImage instructionsImage;// giữ ảnh instructions

	public Instructions() {
		super();
		super.setLogoImageFromPath("instructionsLogo.png");
		resetState();
	}

	@Override
	public int update() {
		// trạng thái trò chơi vẫn ở trạng thái này cho đến khi nhấn nút "Back To Main
		// Menu"
		return GameStateHandler.INSTRUCTIONS_STATE;
	}

	@Override
	public void draw(Graphics2D g, ImageObserver io) {
		super.drawLogo(g, io);
		
		
		// tải instructions.png từ file hình ảnh
		instructionsImage = ResourceGetter.getBufferedImage("instructions.png");
		// vẽ hình ảnh instructions
		g.drawImage(instructionsImage, 40, 155, 700, 350, null);
		// Hiển thị "Return to Main Menu"
		getButtons().get(0).drawWithShadow(g);
	}

	@Override
	public void resetState() {
		addReturnToMenuButton();
	}

}
package GameStates;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;

import tankfighter.GameStateHandler;

public class Setting extends GameState{
	private BufferedImage settingImage;
	public Setting(){
		super();
		super.setLogoImageFromPath("settingImage.png");
		resetState();
	}
	@Override
	public void draw(Graphics2D g, ImageObserver io) {
		// TODO Auto-generated method stub
		super.drawLogo(g, io);
		
	}
	@Override
	public int update() {
		// TODO Auto-generated method stub
		return GameStateHandler.SETTING_STATE;
	}
	@Override
	public void resetState() {
		// TODO Auto-generated method stub
		getButtons().clear();
		addReturnToMenuButton();
	}
	
}

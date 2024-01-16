
package GameObjects;

import java.awt.Color;
import java.util.ArrayList;

public class SeekingBullet extends Bullet {
	final private static float ACCELERATION_RATE = (float) 0.1; // gia tốc
	final private static float MAX_SPEED = (float) 3.0; // tốc độ tối đa

	private float aX;// gia tốc theo trục x
	private float aY;// gia tốc theo trục y

	public SeekingBullet(float x, float y, int angle, boolean imaginary, Color color) {
		super(x, y, angle, false, imaginary, color);// the "false" means the Bullet is not a fast bullet.
		setvX((float) (MAX_SPEED * Math.cos(Math.toRadians(angle))));
		setvY((float) (MAX_SPEED * Math.sin(Math.toRadians(angle))));
		aX = 0;
		aY = 0;
	}

	public boolean update(Player p, ArrayList<Wall> walls) {

		float dX = p.getCenterX() - getX();

		float dY = p.getCenterY() - getY();

		float ratio = ACCELERATION_RATE / ((float) Math.sqrt(Math.pow(dX, 2) + Math.pow(dY, 2)));
		// calculate x and y accelerations
		aX = ratio * dX;
		aY = ratio * dY;

		setvX(getvX() + aX);
		setvY(getvY() + aY);

		float speed = (float) Math.sqrt(Math.pow(getvX(), 2) + Math.pow(getvY(), 2));
		if (speed > MAX_SPEED) {
			float speedRatio = MAX_SPEED / speed;
			setvX(getvX() * speedRatio);
			setvY(getvY() * speedRatio);
		}

		return super.update(walls);

	}
}

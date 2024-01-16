/*
 * A superclass for a drawable Object with a position.
    Has base methods that all game object must have.
 */
package GameObjects;

import java.awt.Graphics2D;

public abstract class GameObject {

	private float x;
	private float y;

	/**
	 * create a game object at this location
	 * 
	 * @param x x pos of game object (bullet, wall, tank, etc)
	 * @param y y pos of game object (bullet, wall, tank, etc)
	 */
	public GameObject(float x, float y) {
		this.x = x;
		this.y = y;
	}

	// Tất cả đối tượng đều phải có phương thức update và draw.
	public abstract void update();

	public abstract void draw(Graphics2D g);

	// getters
	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}

	// setters
	public void setX(float x) {
		this.x = x;
	}

	public void setY(float y) {
		this.y = y;
	}

}
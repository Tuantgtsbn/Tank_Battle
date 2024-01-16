/*
 * A class representing a Wall object. The wall is impassable terrain. 
    There are two types of walls, breakable, and unbreakable. 
    The wall is created when " - "'s are read from the data file.
 */
package GameObjects;

import java.awt.Color;
import java.awt.Graphics2D;

import tankfighter.GameStateHandler;

public class Wall extends GameObject {
	private boolean breakable;
	private Color color;

	// Đây phải là public vì chúng ta đang điền vào hàng phía trên trong Level với
	// màu này
	public static Color DEFAULT_COLOR = new Color(100, 100, 100);// màu xám
	private static Color BREAKABLE_COLOR = new Color(100, 100, 100, 80);

	/**
	 * The constructor to intialize a wall
	 * 
	 * @param x x position of the wall
	 * @param y y position of the wall
	 */
	public Wall(float x, float y) {
		// gọi constructor từ GameObject (khởi tạo vị trí)
		super(x, y);
		breakable = false;
		this.color = DEFAULT_COLOR;
	}

	// constructor bao gồm một biến boolean xác định xem tường có thể phá hủy hay
	// không
	public Wall(float x, float y, boolean breakable) {
		this(x, y);
		if (breakable) { // nếu đúng là tường có thể phá
			this.breakable = true; // set breakable
			this.color = BREAKABLE_COLOR; // màu xám nhạt là màu có thể phá
		}
	}

	@Override
	/**
	 * draw the wall
	 */
	public void draw(Graphics2D g) {
		g.setColor(color);
		g.fillRect((int) getX(), (int) getY(), GameStateHandler.BLOCK_WIDTH, GameStateHandler.BLOCK_HEIGHT);
	}

	@Override
	// Phương thức cập nhật được gọi vì nó là một phương thức của một đối tượng
	// game, nhưng tường không có máu và không di chuyển
	// nên update để trống
	public void update() {

	}

	/**
	 * creates a clone of the wall (isn't really needed in our game), might have
	 * some use in future
	 * 
	 * @return a wall with same x and y and if its breakable or not
	 */
	public Wall clone() {
		return new Wall(getX(), getY(), breakable);
	}

	/**
	 * Check if the wall is breakable
	 * 
	 * @return true: wall is breakable, false: wall is not breakable
	 */
	public boolean isBreakable() {
		return breakable;
	}
}
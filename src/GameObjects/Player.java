/*
   Lớp này chính là lớp dành cho người chơi. Lưu vị trí người chơi và nhận những phản hồi từ người dùng (ví dụ như di chuyển...)
   Lớp này có các phương thức để nhận trạng thái của game, nhận phản hồi người chơi như nhấn phím... và cập nhật vị trí người chơi.
 */
package GameObjects;

import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.event.KeyEvent;

public class Player extends Tank {

	private static final float MAX_SPEED = 2;// pixels/frame

	// 2 biến đại diện cho vị trí của con trỏ chuột
	private int mouseX;
	private int mouseY;
	// Các biến kiểu boolean có giá trị true nếu nút được nhấn, nếu không nhận false
	private boolean leftPressed;
	private boolean rightPressed;
	private boolean upPressed;
	private boolean downPressed;
	// Số frame kể từ lần cuối người chơi bắn đạn
	private int framesSinceShot;

	private static final int COOLDOWN_TIME = 30;// số frame mà sau khi bắn đạn xong, người chơi phải chờ để được bắn
												// tiếp

	/**
	 * 
	 * @param x     x position of player
	 * @param y     y position of player
	 * @param angle angle they are aiming at
	 */
	public Player(float x, float y, int angle) {
		// gọi hàm khởi tạo lớp Tank
		super(x, y, angle);
		this.bodyColor = new Color(60, 200, 20);
		this.turretColor = bodyColor.darker();
		leftPressed = false;
		rightPressed = false;
		upPressed = false;
		downPressed = false;
		framesSinceShot = COOLDOWN_TIME - 1;
		maxSpeed = MAX_SPEED;
	}

	// Vẽ người chơi
	@Override
	public void draw(Graphics2D g) {
		super.draw(g);
	}

	// Cập nhật
	@Override
	public void update() {
		framesSinceShot++; // tăng frame lên

		// cập nhật vận tốc dựa theo phím người chơi nhấn

		// chuyển động ngang
		if (leftPressed)
			vX = -MAX_SPEED;
		else if (rightPressed)
			vX = MAX_SPEED;
		else
			vX = 0;

		// chuyển động dọc
		if (upPressed)
			vY = -MAX_SPEED;
		else if (downPressed)
			vY = MAX_SPEED;
		else
			vY = 0;

		// gọi hàm update()
		super.update();
		// Cập nhật góc cho nòng súng.(Nó sẽ hướng về phía con trỏ)
		pointTurret(mouseX, mouseY);
	}

	/**
	 * 
	 * @param e key pressed? which one was pressed?
	 */
	public void keyPressed(KeyEvent e) {
		double key = e.getKeyCode();
		// Khi người chơi nhấn nút sang trái hoặc "A"
		if (key == KeyEvent.VK_LEFT || key == KeyEvent.VK_A) {

			leftPressed = true;
			rightPressed = false;
		} else if (key == KeyEvent.VK_RIGHT || key == KeyEvent.VK_D) {

			rightPressed = true;
			leftPressed = false;
		} else if (key == KeyEvent.VK_UP || key == KeyEvent.VK_W) {

			upPressed = true;
			downPressed = false;
		} else if (key == KeyEvent.VK_DOWN || key == KeyEvent.VK_S) {

			downPressed = true;
			upPressed = false;
		}
	}

	// Kiểm tra keyReleased và cập nhật các biến liên quan
	public void keyReleased(KeyEvent e) {
		double key = e.getKeyCode();
		// Sau khi người chơi nhả phím ra, xe tăng sẽ dừng lại ngay lập tức.
		if (key == KeyEvent.VK_LEFT || key == KeyEvent.VK_A) {
			leftPressed = false;
		} else if (key == KeyEvent.VK_RIGHT || key == KeyEvent.VK_D) {
			rightPressed = false;
		} else if (key == KeyEvent.VK_UP || key == KeyEvent.VK_W) {
			upPressed = false;
		} else if (key == KeyEvent.VK_DOWN || key == KeyEvent.VK_S) {
			downPressed = false;
		}
	}

	// cập nhật lại vị trí con trỏ
	public void mouseMoved(int[] newPos) {
		mouseX = newPos[0];
		mouseY = newPos[1];
	}

	// tạo 1 bản sao khác
	public Player clone() {
		return new Player(getX(), getY(), bodyAngle);
	}

	// Trả về đạn nếu người chơi bắn
	public Bullet shootBullet() {
		// Nếu thời gian chưa hết, người chơi không được bắn ra đạn.
		if (framesSinceShot < COOLDOWN_TIME)
			return null;
		// Ngược lại người chơi bắn đạn và reset frameSinceShot về 0.
		framesSinceShot = 0;
		return super.shootBullet(false);
	}

}

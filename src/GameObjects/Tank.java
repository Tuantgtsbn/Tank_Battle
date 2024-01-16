/*
    Lớp này sẽ tạo ra 1 xe tăng với các thuộc tính như:màu sắc,kích cỡ, tốc độ di chuyển, tốc độ quay. Cùng với đó là các phương thức
    như: drawn(),update()...
 */
package GameObjects;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;

import tankfighter.GameStateHandler;

public class Tank extends GameObject {

	// Sử dụng các số nguyên đại diện cho các kiểu xe tăng có trong game
	final static public int NORMAL_TYPE = 0;
	final static public int FAST_TYPE = 1;
	final static public int SEEKING_TYPE = 2;

	// các thông số kích thước hằng
	public static final int BODY_WIDTH = 50;
	public static final int BODY_HEIGHT = 50;
	public static final int TURRET_LENGTH = 30;
	public static final int TURRET_WIDTH = 5;
	public static final int BODY_ROTATION_SPEED = 5;// tốc độ quay phần thân mỗi khung hình

	// Các thuộc tính của đối tượng tank
	protected float vX;
	protected float vY;
	protected float maxSpeed;
	protected Color bodyColor;
	protected Color turretColor;
	protected int bodyAngle;
	protected int turretAngle;
	protected int type;
	private int rotatedThisFrame = 0; // xe tăng đã xoay bao nhiêu độ

	/**
	 * constructor (to create a tank, you need these base stats)
	 * 
	 * @param x     x position of the tank
	 * @param y     y position of the tank
	 * @param angle angle of the turret and body
	 */
	public Tank(float x, float y, int angle) {
		// gọi hàm khởi tạo của lớp GameObject
		super(x, y);
		turretAngle = angle;
		bodyAngle = angle;
		bodyColor = Color.LIGHT_GRAY;
		turretColor = Color.BLACK;
		type = NORMAL_TYPE;
	}

	/**
	 * chained constructor that also takes the type of tank
	 */
	public Tank(float x, float y, int angle, int type) {
		super(x, y);
		turretAngle = angle;
		bodyAngle = angle;
		bodyColor = Color.LIGHT_GRAY;
		turretColor = Color.BLACK;
		this.type = type;
	}

	@Override
	/**
	 * take the 2d graphics variable that we are using to draw
	 */
	public void draw(Graphics2D g) {

		// Ép các giá trị tọa độ x,y về số nguyên
		int tankX = (int) getX();
		int tankY = (int) getY();
		int tankCenterX = (int) getCenterX();
		int tankCenterY = (int) getCenterY();

		// TANK BODY
		AffineTransform old = g.getTransform();
		g.rotate(Math.toRadians(bodyAngle), tankCenterX, tankCenterY);

		g.setColor(bodyColor);
		g.fillRect(tankX, tankY, BODY_WIDTH - 5, BODY_HEIGHT);
		g.fillPolygon(new int[] { tankX + BODY_WIDTH - 7, tankX + BODY_WIDTH - 7, tankX + BODY_WIDTH + 2 },
				new int[] { tankY, tankY + BODY_HEIGHT, tankY + BODY_HEIGHT / 2 }, 3);

		g.setColor(Color.BLACK);
		g.fillRect(tankX, tankY, BODY_WIDTH - 5, BODY_HEIGHT / 6);
		g.fillRect(tankX, tankY + (int) Math.round(BODY_HEIGHT * (5.0 / 6.0)), BODY_WIDTH - 5, BODY_HEIGHT / 6);
		g.setTransform(old);

		// TANK TURRET
		old = g.getTransform();
		g.rotate(Math.toRadians(turretAngle), tankCenterX, tankCenterY);

		g.setColor(turretColor);
		g.fillRect(tankCenterX - (TURRET_WIDTH) / 2, tankCenterY - TURRET_WIDTH / 2, TURRET_LENGTH, TURRET_WIDTH);
		g.fillRect(tankCenterX - 10, tankCenterY - 10, 20, 20);
		g.setTransform(old);

	}

	@Override
	/**
	 * update the tanks variables (x, y, angle, rotation, etc ...)
	 */
	public void update() {

		// Tính tốc độ trong trường hợp xe tăng không đi thẳng
		double totalSpeed = Math.sqrt(vX * vX + vY * vY);
		// Nếu tốc độ lớn hơn tốc độ tối đa thì phải thay đổi lại
		if (totalSpeed > maxSpeed) {
			double ratio = (double) maxSpeed / totalSpeed;
			vX *= ratio;
			vY *= ratio;
		}

		// Cập nhật lại tọa độ
		setX(getX() + vX);
		setY(getY() + vY);

		// Chuẩn hóa lại góc có giá trị trong khoảng (0,360) độ
		if (bodyAngle < 0)
			bodyAngle += 360;
		if (bodyAngle >= 360)
			bodyAngle = bodyAngle % (360);

		if (turretAngle < 0)
			turretAngle += 360;
		if (turretAngle >= 360)
			turretAngle = turretAngle % (360);

		if (vX != 0 || vY != 0) {
			rotateBodyToMovement();
		}

	}

	/**
	 * updates the tank (checks if it is colliding with a bullet, wall, another
	 * tank, etc...)
	 * 
	 * @param walls   arrayList of walls
	 * @param bullets arrayList of bullets
	 * @param enemies arrayList of enemies
	 * @param player  the player (there is only 1)
	 * @return whether or not the player is still alive
	 */
	public boolean update(ArrayList<Wall> walls, ArrayList<Bullet> bullets, ArrayList<Enemy> enemies, Tank player) {
		rotatedThisFrame = 0;
		this.update();

		resolveWallCollisions(walls);
		resolveTankCollisions(enemies, player);

		return !isCollidingBullet(bullets);
	}

	/**
	 * checks to see if the tank being references has collided with a bullet or not
	 * 
	 * @param bullets arrayList of bullets (to keep track of all the bullets)
	 * @return if the tank has hit a bullet
	 */
	protected boolean isCollidingBullet(ArrayList<Bullet> bullets) {
		float tankX = getX();
		float tankY = getY();

		// Kiểm tra va chạm với đạn
		for (Bullet bullet : bullets) {
			// Nếu xảy ra va chạm
			if (bullet.getLife() > Bullet.START_KILL_FRAME && bullet.getX() > tankX
					&& bullet.getX() < tankX + BODY_WIDTH && bullet.getY() > tankY
					&& bullet.getY() < tankY + BODY_HEIGHT) {
				bullets.remove(bullet);

				return true;
			}
		}

		return false;
	}

	/**
	 * method that updates what happens when two tanks are moving in opposite
	 * direction and collide (they stop moving) if they are slightly angled though,
	 * they keep moving in their direction (stop is only for directly opposite)
	 * parameters are all the tanks that exist on the map
	 * 
	 * @param enemies arrayList of enemies
	 * @param player  the player
	 */
	private void resolveTankCollisions(ArrayList<Enemy> enemies, Tank player) {
		float tank1X = getX();
		float tank1Y = getY();
		ArrayList<Tank> tanks = new ArrayList<>();
		for (Enemy enemy : enemies) {
			tanks.add(enemy);
		}
		tanks.add(player);
		// Tank không thể đâm chính nó
		tanks.remove(this);
		float tank2X;
		float tank2Y;

		for (Tank tank2 : tanks) {
			tank2X = tank2.getX();
			tank2Y = tank2.getY();

			// Nếu chúng đâm nhau
			if (tank1X + BODY_WIDTH * 3 / 2 > tank2X && tank1X - BODY_WIDTH / 2 < tank2X + BODY_WIDTH
					&& tank1Y + BODY_HEIGHT * 3 / 2 > tank2Y && tank1Y - BODY_HEIGHT / 2 < tank2Y + BODY_HEIGHT) {
				Vector[] tank1Corners = new Vector[4];
				double angle2 = Math.toRadians((bodyAngle + 45) % 90);
				float tankRadius = (float) Math.sqrt(2 * Math.pow(BODY_WIDTH / 2, 2));

				float a = tankRadius * (float) (Math.cos(angle2));

				float b = tankRadius * (float) (Math.sin(angle2));

				float centerX = getCenterX();
				float centerY = getCenterY();

				tank1Corners[0] = new Vector(centerX + a, centerY + b);// Phải dưới
				tank1Corners[1] = new Vector(centerX - b, centerY + a);// Trái dưới
				tank1Corners[2] = new Vector(centerX - a, centerY - b);// Trái trên
				tank1Corners[3] = new Vector(centerX + b, centerY - a);// Phải trên

				Vector[] tank2Corners = new Vector[4];

				angle2 = Math.toRadians((bodyAngle + 45) % 90);

				a = tankRadius * (float) (Math.cos(angle2));
				b = tankRadius * (float) (Math.sin(angle2));

				centerX = tank2.getCenterX();
				centerY = tank2.getCenterY();

				tank2Corners[0] = new Vector(centerX + a, centerY + b);// bottom right
				tank2Corners[1] = new Vector(centerX - b, centerY + a);// bottom left
				tank2Corners[2] = new Vector(centerX - a, centerY - b);// top left
				tank2Corners[3] = new Vector(centerX + b, centerY - a);// top right

				// Kiểm tra nếu bất kỳ góc hoặc cạnh bị đâm
				if (Vector.areRotatedRectanglesColliding(tank1Corners, tank2Corners)) { // Nếu chúng bị đâm
					setX(tank1X - vX);
					setY(tank1Y - vY);
					break;
				}
			}
		}
	}

	/**
	 * check for tank collision with wall
	 * 
	 * @param walls walls on the map
	 */
	private void resolveWallCollisions(ArrayList<Wall> walls) {
		float tankX = getX();
		float tankY = getY();
		int numWalls = walls.size();
		Wall currWall;// lưu tường hiện tại đang được kiểm tra va chạm với xe tăng
		float currWallX;
		float currWallY;
		for (int wallNum = 0; wallNum < numWalls; wallNum++) {
			currWall = walls.get(wallNum); // current wall variable
			if (getX() + BODY_WIDTH > currWall.getX() && currWall.getX() + GameStateHandler.BLOCK_WIDTH > getX()
					&& getY() + BODY_HEIGHT > currWall.getY()
					&& currWall.getY() + GameStateHandler.BLOCK_HEIGHT > getY()) { // Nếu đúng là va chạm

				float overlapX = (BODY_WIDTH / 2 + GameStateHandler.BLOCK_WIDTH / 2)
						- Math.abs((getX() + BODY_WIDTH / 2) - (currWall.getX() + GameStateHandler.BLOCK_WIDTH / 2));
				float overlapY = (BODY_HEIGHT / 2 + GameStateHandler.BLOCK_HEIGHT / 2)
						- Math.abs((getY() + BODY_HEIGHT / 2) - (currWall.getY() + GameStateHandler.BLOCK_HEIGHT / 2));

				if (overlapX >= overlapY) {// Nếu xe tăng đâm vào phía trên hoặc phía dưới mặt phẳng
					if (getY() > currWall.getY()) { // Nếu xe tăng đi lên phía trên
						setY(getY() + overlapY); // di chuyển xe xuống dưới
					} else {
						setY(getY() - overlapY); // cho xe tăng đi lên
					}
				} else {// nếu xe tăng đâm vào cạnh
					if (getX() < currWall.getX()) { // nếu xe đang ở bên trái tường
						setX(getX() - overlapX); // xe đi sang trái
					} else {
						setX(getX() + overlapX); // xe đi sang phải
					}
				}
			}

		}

	}

	/**
	 * this method shoots a bullet (returns a bullet to the caller, caller adds it
	 * to bullet arrayList)
	 * 
	 * @param imaginary variable created to see if this is a fake bullet designed
	 *                  for the AI, or if its a real bullet that was shot
	 * @return a new bullet that was shot out of the turret
	 */
	public Bullet shootBullet(boolean imaginary) {
		if (this.type == NORMAL_TYPE) { // nếu đây là xe tăng bình thường
			return new Bullet(getCenterX() + TURRET_LENGTH * (float) Math.cos(Math.toRadians(turretAngle)), // gọi hàm
																											// khởi tạo
																											// để tạo ra
																											// 1 xe tăng
																											// bình
																											// thường
					getCenterY() + TURRET_LENGTH * (float) Math.sin(Math.toRadians(turretAngle)), turretAngle, false,
					imaginary, this.bodyColor);
		} else if (this.type == FAST_TYPE) {
			return new Bullet(getCenterX() + TURRET_LENGTH * (float) Math.cos(Math.toRadians(turretAngle)), // gọi hàm
																											// khởi tạo
																											// để tạo ra
																											// 1 xe tăng
																											// đi nhanh
					getCenterY() + TURRET_LENGTH * (float) Math.sin(Math.toRadians(turretAngle)), turretAngle, true,
					imaginary, this.bodyColor);
		} else {// nếu đó là xe tăng bắn ra seeking bullet
			return new SeekingBullet(getCenterX() + TURRET_LENGTH * (float) Math.cos(Math.toRadians(turretAngle)), // gọi
																													// hàm
																													// khởi
																													// tạo
																													// để
																													// tạo
																													// ra
																													// 1
																													// xe
																													// tăng
																													// bắn
																													// ra
																													// đạn
																													// seeking
																													// bullet
					getCenterY() + TURRET_LENGTH * (float) Math.sin(Math.toRadians(turretAngle)), turretAngle,
					imaginary, this.bodyColor);
		}

	}

	// Các phương thức getter, setter
	public float getCenterX() {
		return getX() + BODY_WIDTH / 2;
	}

	public float getCenterY() {
		return getY() + BODY_HEIGHT / 2;
	}

	public double getBodyRotation() {
		return bodyAngle;
	}

	public double getTurretRotation() {
		return turretAngle;
	}

	/**
	 * 
	 * @param x x position of mouse (or player if its the enemy tank)
	 * @param y y position of mouse (or player if its the enemy tank)
	 */
	protected void pointTurret(double x, double y) {
		double dX = x - getCenterX();
		double dY = y - getCenterY();
		// double hypotenuse = Math.sqrt(dY*dY+dX*dX);
		int newTurretAngle = (int) Math.toDegrees(Math.atan(dY / dX));
		if (dX < 0) {
			newTurretAngle = (newTurretAngle + 180) % 360;
		}
		turretAngle = newTurretAngle; // đầu súng luôn hướng theo con trỏ trên màn hình.
	}

	/**
	 * rotates the body of the tank
	 */
	private void rotateBodyToMovement() {

		double targetAngle = Math.toDegrees(Math.atan(((double) vY / (double) vX)));

		// Nếu đang di chuyển sang trái
		if (vX < 0) {
			targetAngle += 180;
		} else if (vX >= 0 && vY < 0) {
			targetAngle += 360;
		}

		// sửa targetAngle nếu >360 hoặc <0
		targetAngle = (360 + targetAngle) % 360;

		if (targetAngle - bodyAngle > 180) {
			targetAngle -= 360;
		} else if (bodyAngle - targetAngle > 180) {
			targetAngle += 360;
		}

		if (bodyAngle < targetAngle) {
			bodyAngle += BODY_ROTATION_SPEED;
			rotatedThisFrame = BODY_ROTATION_SPEED;
		} else if (bodyAngle > targetAngle) {
			bodyAngle -= BODY_ROTATION_SPEED;
			rotatedThisFrame = -BODY_ROTATION_SPEED;
		}

	}

	public boolean equals(Tank t) { // Kiểm tra xem 2 tank có là 1 hay không

		return getX() == t.getX() && getY() == t.getY();
	}

}

/*
 * The class for a bullet (shot by a tank).
 */
package GameObjects;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;

import tankfighter.GameStateHandler;

public class Bullet extends GameObject {

	///// VARIABLE DECLARATIONS/////

	// final class variables (constants)
	final static public float DEFAULT_SPEED = 3; // điểm ảnh / khung
	final static public float DEFAULT_FAST_SPEED = (float) 4.8; // điểm ảnh / khung. Tốc độ của viên đạn nhanh (một số
																// kẻ địch bắn nhanh)
	final static public int DIAMETER = 10;
	final static public int RADIUS = DIAMETER / 2;
	// Khung hình cho đến khi viên đạn có thể giết được một thứ gì đó (để tránh nhân
	// vật giết chính mình khi bắn ra một viên đạn)
	final static public int START_KILL_FRAME = 8;
	// Số khung hình mà viên đạn tồn tại cho đến khi nổ.
	final private static int LIFETIME = 300;

	private float vX; // vận tốc x
	private float vY; // vận tốc y
	private int angle; // góc, theo độ, hướng của vận tốc viên đạn
	private int life; // số khung hình(thời gian tồn tại) kể từ viên đạn được tạo (được sử dụng cho
						// hoạt ảnh "nổ" và để tránh người bắn tự giết mình)
	private Color color;
	// Kiểm tra viên đạn là ảo không. Kẻ địch bắn đạn ảo để kiểm tra xem có đường đi
	// rõ ràng đến người chơi không.
	private boolean imaginary;

	/**
	 * Constructor.
	 * 
	 * @param x     the x coordinate of the bullet's center
	 * @param y     the y coordinate of the bullet's center
	 * @param angle the angle, in degrees, in which the bullet velocity is pointing
	 * @param color the Bullet's color
	 */
	public Bullet(float x, float y, int angle, Color color) {
		// gọi constructor từ GameObject (khởi tạo vị trí)
		super(x, y);
		// tính toán vận tốc x và y dựa trên tốc độ và hướng
		this.vX = (float) (DEFAULT_SPEED * Math.cos(Math.toRadians(angle)));
		this.vY = (float) (DEFAULT_SPEED * Math.sin(Math.toRadians(angle)));
		// đặt màu sắc thành màu xám
		this.color = color.darker();
		// đặt số khung hình kể từ viên đạn được tạo là 0
		life = 0;
		// mặc định là viên đạn không phải là ảo
		imaginary = false;

	}

	/**
	 * Constructor, for creating a bullet, including the boolean to determine if it
	 * fast or not and if it is imaginary or not
	 * 
	 * @param x         the x coordinate of the bullet's center
	 * @param y         the y coordinate of the bullet's center
	 * @param angle     the angle, in degrees, in which the bullet velocity is
	 *                  pointing
	 * @param fast      whether or not the Bullet is a fast bullet
	 * @param imaginary whether or not the bullet is imaginary
	 * @param color     the Bullet's color
	 */
	public Bullet(float x, float y, int angle, boolean fast, boolean imaginary, Color color) {
		this(x, y, angle, color);
		if (fast) {
			// tính lại vX và vY cho tốc độ nhanh hơn
			this.vX = (float) (DEFAULT_FAST_SPEED * Math.cos(Math.toRadians(angle)));
			this.vY = (float) (DEFAULT_FAST_SPEED * Math.sin(Math.toRadians(angle)));
		}
		this.imaginary = imaginary;
	}

	/**
	 * Draws the Bullet
	 * 
	 * @param g the Graphics2D Object to use to draw
	 */
	@Override
	public void draw(Graphics2D g) {
		// khi vừa bắn ra khỏi tank, nó được vẽ như một "vụ nổ"
		if (life < START_KILL_FRAME) {
			// kích thước "vụ nổ" mở rộng rồi co lại.
			// lấy thời gian giữa "trung tâm" của vụ nổ
			// và thời gian hiện tại để tính kích thước hiện tại của vụ nổ
			int currDiameter = 5 * (4 - (Math.abs(life - 4)));
			int currRadius = currDiameter / 2;

			// vẽ vòng ngoài của vụ nổ màu đỏ
			g.setColor(Color.RED);
			g.fillOval((int) getX() - currDiameter, (int) getY() - currDiameter, currDiameter * 2, currDiameter * 2);
			// vẽ vòng trong của vụ nổ màu vàng
			g.setColor(Color.YELLOW);
			g.fillOval((int) getX() - currRadius, (int) getY() - currRadius, currDiameter, currDiameter);
		} else if (LIFETIME - life < 8) {
			// khi viên đạn sắp bị loại bỏ, nó cũng được vẽ như một vụ nổ
			int currDiameter = 5 * (4 - (Math.abs((LIFETIME - life) - 4)));
			int currRadius = currDiameter / 2;
			// vẽ vòng ngoài của vụ nổ
			g.setColor(Color.RED);
			g.fillOval((int) getX() - currDiameter, (int) getY() - currDiameter, currDiameter * 2, currDiameter * 2);
			// vẽ vòng trong của vụ nổ
			g.setColor(Color.YELLOW);
			g.fillOval((int) getX() - currRadius, (int) getY() - currRadius, currDiameter, currDiameter);
		}
		// nếu viên đạn không phải vừa được bắn ra hoặc sắp bị loại bỏ, vẽ nó như một
		// viên đạn bình thường
		else {
			g.setColor(color);
			g.fillOval((int) getX() - RADIUS, (int) getY() - RADIUS, DIAMETER, DIAMETER);
		}
	}

	/**
	 * Updates the Bullet's position and life.
	 */
	@Override
	public void update() {
		setX(getX() + vX);
		setY(getY() + vY);
		life++;
	}

	/**
	 * Updates the bullet, including bouncing off of walls and checking if it has
	 * reached the end of its lifetime.
	 * 
	 * @param walls the ArrayList of Walls in the current level
	 * @return true if the Bullet still exits, false otherwise
	 */
	public boolean update(ArrayList<Wall> walls) {
		// cập nhật vị trí và thời gian tồn tại của viên đạn
		this.update();

		// kiểm tra va chạm với các bức tường
		// để xử lý các va chạm này, chúng ta phải tìm ra bức tường mà viên đạn va chạm
		// đầu tiên
		// vì nó có thể "ở giữa" 2 bức tường.
		// maxDistanceFromCollision2 là bình phương của khoảng cách giữa vị trí hiện tại
		// của viên đạn và vị trí va chạm.
		// ví dụ, nếu viên đạn nằm giữa 2 bức tường và cách một điểm va chạm là 5 điểm
		// ảnh và cách điểm va chạm khác là 8 điểm ảnh,
		// thì điểm va chạm 8 điểm ảnh là điểm va chạm đầu tiên nên bỏ qua va chạm cách
		// 5 điểm ảnh.
		double maxDistanceFromCollision2 = 0;
		// lưu trữ các vị trí mới.
		// chúng ta không thể trực tiếp thay đổi vị trí và vận tốc khi kiểm tra từng bức
		// tường
		// vì chúng ta có thể tìm thấy một bức tường khác sau đó viên đạn va chạm trước
		float newX = 0;
		float newY = 0;
		boolean swapVX = false;
		boolean collidedWithAny = false;
		for (Wall currWall : walls) {
			// nếu true, nó đã va chạm với một bức tường
			if (getX() + RADIUS > currWall.getX() && getX() - RADIUS < currWall.getX() + GameStateHandler.BLOCK_WIDTH
					&& getY() + RADIUS > currWall.getY()
					&& getY() - RADIUS < currWall.getY() + GameStateHandler.BLOCK_HEIGHT) {
				if (currWall.isBreakable() && !imaginary) {
					walls.remove(currWall);
					return false;
				}
				// viên đạn theo dõi không thể bật lại từ bức tường nên chúng ta loại nó nếu nó
				// va chạm với bức tường
				if (this instanceof SeekingBullet) {
					return false;
				}
				// nếu di chuyển theo chiều dọc
				if (vX == 0) {
					vY = -vY;
					break;
				}
				// nếu di chuyển theo chiều ngang
				else if (vY == 0) {
					vX = -vX;
					break;
				}

				collidedWithAny = true;

				// va chạm hiện tại với khoảng cách từ vị trí hiện tại của viên đạn, tất cả đều
				// được bình phương
				double distanceFromCollision2;

				float wallBottom = currWall.getY() + GameStateHandler.BLOCK_HEIGHT;
				float wallRight = currWall.getX() + GameStateHandler.BLOCK_WIDTH;

				// tính toán intercepts. Đây là nơi viên đạn va chạm (hoặc sẽ va chạm) với bức
				// tường.
				// Bằng cách tính khoảng cách từ bức tường và intercept, chúng ta có thể tìm ra
				// vận tốc kết quả của viên đạn khi bị bật lại.
				// rightIntercept là tọa độ y của nơi phía bên phải của viên đạn va chạm (hoặc
				// sẽ va chạm) với bức tường
				float rightIntercept = (vY / vX) * ((currWall.getX() - RADIUS) - getX()) + getY();
				// leftIntercept là tọa độ y của nơi phía bên trái của viên đạn va chạm (hoặc sẽ
				// va chạm) với bức tường
				float leftIntercept = (vY / vX) * ((wallRight + RADIUS) - getX()) + getY();
				// topIntercept là tọa độ x của nơi phía trên của viên đạn va chạm (hoặc sẽ va
				// chạm) với bức tường
				float topIntercept = (vX / vY) * (wallBottom + RADIUS - getY()) + getX();
				// bottomIntercept là tọa độ x của nơi phía dưới của viên đạn va chạm (hoặc sẽ
				// va chạm) với bức tường
				float bottomIntercept = (vX / vY) * (currWall.getY() - RADIUS - getY()) + getX();

				if (vX > 0 && vY > 0) { // di chuyển xuống-phải
					if (rightIntercept - currWall.getY() > bottomIntercept - currWall.getX()) { // va chạm với phía trái
																								// của bức tường
						distanceFromCollision2 = Math.pow(getX() + RADIUS - currWall.getX(), 2)
								+ Math.pow(getY() - rightIntercept, 2);
						if (distanceFromCollision2 > maxDistanceFromCollision2) {
							maxDistanceFromCollision2 = distanceFromCollision2;
							newX = currWall.getX() - RADIUS;
							newY = rightIntercept;
							swapVX = true;
						}

					} else { // va chạm với phía trên của bức tường
						distanceFromCollision2 = Math.pow(getX() - bottomIntercept, 2)
								+ Math.pow(getY() + RADIUS - currWall.getY(), 2);
						if (distanceFromCollision2 > maxDistanceFromCollision2) {
							maxDistanceFromCollision2 = distanceFromCollision2;
							newY = currWall.getY() - RADIUS;
							newX = bottomIntercept;
							swapVX = false;
						}
					}
				} else if (vX < 0 && vY > 0) { // di chuyển xuống-trái
					if (leftIntercept - currWall.getY() > wallRight - bottomIntercept) { // va chạm với phía phải của
																							// bức tường
						distanceFromCollision2 = Math.pow(wallRight - (getX() - RADIUS), 2)
								+ Math.pow(getY() - leftIntercept, 2);
						if (distanceFromCollision2 > maxDistanceFromCollision2) {
							maxDistanceFromCollision2 = distanceFromCollision2;
							newX = wallRight + RADIUS;
							newY = leftIntercept;
							swapVX = true;
						}
					} else { // va chạm với phía trên của bức tường
						distanceFromCollision2 = Math.pow(bottomIntercept - getX(), 2)
								+ Math.pow(getY() + RADIUS - currWall.getY(), 2);
						if (distanceFromCollision2 > maxDistanceFromCollision2) {
							maxDistanceFromCollision2 = distanceFromCollision2;
							newY = currWall.getY() - RADIUS;
							newX = bottomIntercept;
							swapVX = false;
						}
					}
				} else if (vX > 0 && vY < 0) { // di chuyển lên-phải
					if ((wallBottom) - rightIntercept > topIntercept - currWall.getX()) { // va chạm với phía trái của
																							// bức tường
						distanceFromCollision2 = Math.pow((getX() + RADIUS) - currWall.getX(), 2)
								+ Math.pow(rightIntercept - getY(), 2);
						if (distanceFromCollision2 > maxDistanceFromCollision2) {
							maxDistanceFromCollision2 = distanceFromCollision2;
							newX = currWall.getX() - RADIUS;
							newY = rightIntercept;
							swapVX = true;
						}

					} else { // va chạm với phía dưới của bức tường
						distanceFromCollision2 = Math.pow(getX() - topIntercept, 2)
								+ Math.pow(wallBottom - (getY() - RADIUS), 2);
						if (distanceFromCollision2 > maxDistanceFromCollision2) {
							maxDistanceFromCollision2 = distanceFromCollision2;
							newY = wallBottom + RADIUS;
							newX = topIntercept;
							swapVX = false;
						}
					}
				} else if (vX < 0 && vY < 0) { // di chuyển lên-trái
					if (wallBottom - leftIntercept > wallRight - topIntercept) { // va chạm với phía phải của bức tường
						distanceFromCollision2 = Math.pow(wallRight - (getX() - RADIUS), 2)
								+ Math.pow(leftIntercept - getY(), 2);
						if (distanceFromCollision2 > maxDistanceFromCollision2) {
							maxDistanceFromCollision2 = distanceFromCollision2;
							newX = wallRight + RADIUS;
							newY = leftIntercept;
							swapVX = true;
						}
					} else { // va chạm với phía dưới của bức tường
						distanceFromCollision2 = Math.pow(topIntercept - getX(), 2)
								+ Math.pow(wallBottom - (getY() - RADIUS), 2);
						if (distanceFromCollision2 > maxDistanceFromCollision2) {
							maxDistanceFromCollision2 = distanceFromCollision2;
							newY = wallBottom + RADIUS;
							newX = topIntercept;
							swapVX = false;
						}
					}
				}
			}
		}
		// nếu viên đạn va chạm với ít nhất 1 bức tường, cập nhật vị trí và vận tốc của
		// nó
		if (collidedWithAny) {
			setX(newX);
			setY(newY);
			if (swapVX) {
				vX = -vX;
			} else {
				vY = -vY;
			}
		}
		// nếu viên đạn đã tồn tại trong thời gian tối đa, trả về false
		// (nó sẽ được loại bỏ khỏi danh sách bullets trong MainGame)
		if (life > LIFETIME)
			return false;
		// nếu không, trả về true
		return true;

	}

	// getters
	public int getLife() {
		return life;
	}

	public float getvX() {
		return vX;
	}

	public float getvY() {
		return vY;
	}

	public int getAngle() {
		return angle;
	}

	// setters
	public void setvX(float vX) {
		this.vX = vX;
	}

	public void setvY(float vY) {
		this.vY = vY;
	}

	public void setAngle(int angle) {
		this.angle = angle;
	}

	public void setColor(Color color) {
		this.color = color;
	}

}
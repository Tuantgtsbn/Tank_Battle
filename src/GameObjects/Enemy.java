/*
    Lớp xây dựng đối tượng địch. Lớp này kế thừa từ lớp Tank. Có phương thức update(nhận trạng thái game, xác định xem địch nên phản hồi như nào, rồi
    thực hiện phản hồi tối ưu nhất, ví dụ như bắn người chơi, lé đạn.
 */
package GameObjects;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;

import tankfighter.Node;
import tankfighter.Pathfinding;

public class Enemy extends Tank {
	private int timeUntilShoot;// khoảng thời gian cho tới khi địch bắn tiếp đạn
	final private static int SHOOT_DELAY = 300;// số khung hình delay giữa mỗi lần bắn (mặc định là 300)
	final private static float MAX_SPEED = (float) 1.25;// tốc độ tối đa

	// Khởi tạo
	public Enemy(float x, float y, int angle) {
		super(x, y, angle);
		this.maxSpeed = MAX_SPEED;
		this.bodyColor = Color.YELLOW;
		this.turretColor = bodyColor.darker();
		// khởi tạo biến timeUntilShoot ngẫu nhiên để các xe địch không bắn ra đạn cùng
		// 1 lúc
		timeUntilShoot = (int) (Math.random() * SHOOT_DELAY);
	}

	// Khởi tạo xe tăng có thêm tham số type (liên quan đến kiểu của đạn bắn ra)
	public Enemy(float x, float y, int angle, int type) {

		super(x, y, angle, type);
		// Mỗi xe tăng có kiểu đạn khác nhau sẽ có màu khác nhau để dễ phân biệt
		if (type == Tank.FAST_TYPE) {
			this.bodyColor = new Color(70, 70, 210);
		} else if (type == Tank.SEEKING_TYPE) {
			this.bodyColor = Color.RED;
		} else {
			this.bodyColor = Color.YELLOW;
		}
		this.turretColor = bodyColor.darker();
		timeUntilShoot = (int) (Math.random() * SHOOT_DELAY);
		this.maxSpeed = MAX_SPEED;
	}

	// vẽ địch
	@Override
	public void draw(Graphics2D g) {
		super.draw(g);
	}

	public boolean update(Player player, ArrayList<Wall> walls, ArrayList<Bullet> bullets, ArrayList<Enemy> enemies,
			Node[][] map) {

		// hướng nòng súng về người chơi
		pointTurret(player.getCenterX(), player.getCenterY());
		// giảm dần dần biến timeUntilShoot
		timeUntilShoot--;
		// Nếu đã đến lúc bắn đạn
		if (timeUntilShoot <= 0) {
			if (hasClearPath(player, walls, map)) {
				timeUntilShoot = SHOOT_DELAY;// timeUntilShoot reset về ban đầu
				bullets.add(shootBullet(false));
			}

		}
		// nếu kẻ địch ở vị trí có tọa độ x và y
		// rất gần với bội số của 50, chúng ta tính toán lại đường đi.
		// điều này là do việc tính toán lại đường dẫn của mỗi khung hình rất tốn công
		// sức và không cần thiết.
		// Tuy nhiên, chúng ta không thể tính toán lại chỉ khi giá trị x và y là bội số
		// chính xác của 50
		// vì khi nó di chuyển theo đường chéo nó sẽ không bao giờ đạt đến bội số chính
		// xác.
		if (((getX() + vX) % 50 < maxSpeed || (getX() + vX) % 50 > 50 - maxSpeed) // nếu x+vx gần 50
				&& ((getY() + vY) % 50 < maxSpeed || (getY() + vY) % 50 > 50 - maxSpeed)) {// nếu y+vy gần 50

			// đưa x,y về bội gần nhất của 50
			setX(50 * (Math.round(getX() / 50)));
			setY(50 * (Math.round(getY() / 50)));
			// tính toán lại đường đi
			return recalculatePath(player, walls, bullets, enemies, map);
		}
		return super.update(walls, bullets, enemies, player);
	}

	public boolean recalculatePath(Player player, ArrayList<Wall> walls, ArrayList<Bullet> bullets,
			ArrayList<Enemy> enemies, Node[][] map) {
		// tạo 1 bản sao map
		Node[][] mapCopy = new Node[16][12];
		// Tiến hành sao chép
		for (int i = 0; i < 16; i++) {
			for (int j = 0; j < 12; j++) {
				if (map[i][j] == null) {
					mapCopy[i][j] = null;
				} else {
					mapCopy[i][j] = map[i][j].clone();
				}
			}
		}

		// Với từng viên đạn trên bản đồ
		for (Bullet bullet : bullets) {
			// lấy vị trí đạn
			int x = (int) (bullet.getX() / 50.0);
			int y = (int) (bullet.getY() / 50.0);
			mapCopy[x][y] = null; // ô đạn
			if (x + 1 < 16) { // nếu có khoảng trống bên phải đạn
				mapCopy[x + 1][y] = null;
			}
			if (x - 1 >= 0) { // nếu có khoảng trống bên trái đạn
				mapCopy[x - 1][y] = null;
			}
			if (y + 1 < 12) { // nếu có khoảng trống bên dưới đạn
				mapCopy[x][y + 1] = null;
			}
			if (y - 1 >= 0) { // nếu có khoảng trống bên trên đạn
				mapCopy[x][y - 1] = null;
			}

			// Nếu bên phải trống
			if (x + 1 < 16) {

				if (y + 1 < 12) {

					mapCopy[x + 1][y + 1] = null;
				}
				if (y - 1 >= 0) {
					mapCopy[x + 1][y - 1] = null;
				}
			}
			// nếu bên trái trống
			if (x - 1 >= 0) {

				if (y + 1 < 12) {

					mapCopy[x - 1][y + 1] = null;
				}
				if (y - 1 >= 0) {
					mapCopy[x - 1][y - 1] = null;
				}
			}

		}

		// tạo ra 1 pathfinder
		Pathfinding q = new Pathfinding((int) getX(), (int) getY(), (int) player.getX(), (int) player.getY(), mapCopy);

		// tìm đường đến mục tiêu (chính là người chơi)
		q.findPath();
		ArrayList<Node> path = q.getPath(); // trả về đường đi đến mục tiêu

		int pathSize = path.size(); // lưu số nốt phải đi qua

		// nếu số nốt bằng 0 nghĩa là không tìm được đường đi
		if (pathSize == 0) {
			// tạo 1 vị trí ngẫu nhiên
			int x = (int) (Math.random() * 16);
			int y = (int) (Math.random() * 12);
			// nếu vị trí x,y ở phía trên không tồn tại thì tạo mới đến khi nào vị trí đó
			// tồn tại thì thôi

			while (mapCopy[x][y] == null) {
				x = (int) (Math.random() * 16);
				y = (int) (Math.random() * 12);
			}
			// tạo 1 pathfinder mới về vị trí mục tiêu là (x,y)
			q = new Pathfinding((int) getX(), (int) getY(), x * 50, y * 50, mapCopy);
			// tìm đường đi
			q.findPath();
			// nhận đường đi
			path = q.getPath();
			// nhận số node xuất hiện trong đường đi
			pathSize = path.size();
		}

		int nextX = (int) getX();
		int nextY = (int) getY();

		if (pathSize >= 2) {
			nextX = path.get(pathSize - 2).getX();
			nextY = path.get(pathSize - 2).getY();
		}

		// Nếu bạn đang ở dưới vị trí tiếp theo, thì phải di chuyển lên
		if (nextY < getY()) {
			vY = -maxSpeed;
		} else if (nextY > getY()) { // Nếu bạn đang ở trên, phải di chuyển xuống
			vY = maxSpeed;
		} else { // vị trí tiếp theo ở bên phải hoặc trái
			vY = 0;
		}
		if (nextX < getX()) { // nếu bạn ở bên phải nút tiếp theo
			vX = -maxSpeed; // di chuyển sang trái
		} else if (nextX > getX()) { // nếu bạn ở bên trái nút tiếp theo
			vX = maxSpeed; // di chuyển sang phải
		} else { // nếu bạn đang ở trên hoặc dưới vị trí tiếp theo
			vX = 0;
		}
		// cập nhật lại tank
		return super.update(walls, bullets, enemies, player);
	}

	// tạo 1 bản sao Enemy
	public Enemy clone() {
		return new Enemy(getX(), getY(), bodyAngle, type);
	}

	/**
	 * 
	 * @param player get the player object (contains all player info)
	 * @param walls  get the walls on the map (arrayList)
	 * @param map    get a grid of the level (the map split into 16*12 nodes)
	 * @return true if bullet hits the player, if it doesn't, then don't shoot
	 */
	public boolean hasClearPath(Player player, ArrayList<Wall> walls, Node[][] map) {
		Bullet imaginaryBullet = this.shootBullet(true);// tạo đạn ảo
		ArrayList<Bullet> imaginaryBulletList = new ArrayList<>(); // danh sách đạn ảo
		imaginaryBulletList.add(imaginaryBullet);
		Enemy imaginaryEnemy = this.clone(); // địch giả
		ArrayList<Enemy> imaginaryEnemyList = new ArrayList<>(); // danh sách địch giả
		imaginaryEnemyList.add(imaginaryEnemy);

		ArrayList<Bullet> empty = new ArrayList<>();

		while (true) { // lặp đến khi nào đạn ảo trúng người chơi, địch giả không còn gì nữa, hoặc
						// không còn đạn giả

			imaginaryEnemy.isCollidingBullet(imaginaryBulletList);
			if (imaginaryBullet.update(walls)
					&& imaginaryEnemy.recalculatePath(player, walls, empty, imaginaryEnemyList, map)) {// nếu đạn tồn
																										// tại và địch
																										// tồn tại
				if (player.isCollidingBullet(imaginaryBulletList)) { // nếu người chơi bị bắn trúng
					return true; // trả về true
				}
			} else {
				return false; // trả về false
			}
		}
	}
}

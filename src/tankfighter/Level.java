// Tạo class Level. Mỗi level là 1 màn chơi game khác nhau.
package tankfighter;

import GameObjects.Wall;
import GameObjects.Player;
import GameObjects.Enemy;
import GameObjects.Bullet;
import GameObjects.SeekingBullet;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.image.ImageObserver;
import java.util.ArrayList;
import java.util.Iterator;

public class Level {
	// Mỗi 1 level đều gồm có 1 người chơi, các xe địch, tường và đạn.
	private Player player;
	private ArrayList<Enemy> enemies;
	private ArrayList<Wall> walls;
	private ArrayList<Bullet> bullets;

	// map được sử dụng để tìm được đi cho địch. Nó xây dựng vị trí nào mà địch có
	// thể đi được.
	public Node map[][] = new Node[16][12]; // 16 hàng,12 cột

	public long startTime;// Thời gian khi màn game bắt đầu
	public float currentTime;// thời gian kể từ khi bắt đầu chơi màn game

	// Ước lượng thời gian cho 1 khung hình thay đổi. Nó được sử dụng để cập nhật
	// phần Level.
	// Ví dụ nếu thời gian nằm giữa 10 và 4000 thì đó là khoảng thời gian để thay
	// đổi logo
	// từ chữ "Set" sang "Flight"
	private static final int APPROX_TIME_PER_FRAME = 80;
	private static final int INTRO_DELAY = 500;// ms khoảng thời gian giữa các lego

	// Khởi tạo
	public Level(Player player, ArrayList<Enemy> enemies, ArrayList<Wall> walls) {
		this.player = player;
		this.enemies = enemies;
		this.walls = walls;
		startTimer();
		bullets = new ArrayList<>();
		loadMap();
	}

	// Tính thời gian bắt đầu chơi màn game
	public void startTimer() {
		startTime = System.currentTimeMillis();
	}

	/**
	 * Updates the level.
	 * 
	 * @return 1 if the player has completed the level, 2 if the player has died, 0
	 *         otherwise
	 */
	// Cập nhật các level
	// Nếu người chơi thắng, trả về 1, thua trả về 2. Còn lại trả về 0
	public int update() {
		currentTime = System.currentTimeMillis() - startTime;

		if (currentTime < 5 * INTRO_DELAY) {
			return 0; // Không cần phải update
		}

		if (!player.update(walls, bullets, enemies, player)) {// Nếu người chơi chết
			return 2;
		}

		// Cập nhật địch
		for (int i = 0; i < enemies.size(); i++) {
			if (!enemies.get(i).update(player, walls, bullets, enemies, map)) {// Nếu địch chết
				enemies.remove(i);
			}
		}
		// Cập nhật đạn
		for (Iterator<Bullet> iterator = bullets.iterator(); iterator.hasNext();) {
			Bullet bullet = iterator.next();
			if (!(bullet instanceof SeekingBullet)) {
				if (!bullet.update(walls)) {// Nếu đạn không tồn tại

					loadMap();// Cập nhật lại đường đi mà địch có thể đi được
					iterator.remove();// Xóa đạn
				}
			} else {
				if (!(((SeekingBullet) bullet).update(player, walls))) {
					loadMap();// Cập nhật lại đường đi mà địch có thể đi được
					iterator.remove();// Xóa đạn
				}
			}
		}

		// Nếu người chơi thắng màn chơi
		if (enemies.isEmpty())
			return 1;

		// Nếu người chơi vẫn chưa chết
		return 0;
	}

	// Kiểm tra liệu logo có thay đổi không
	// Trả về null nếu không đổi
	// trả về đường dẫn của 1 logo mới nếu thay đổi
	// Logo chỉ thay đổi từ "level"->"set"->"fight"
	public String checkChangeLogo() {
		if ((currentTime > 0 && currentTime < APPROX_TIME_PER_FRAME)
				|| (currentTime < 5 * INTRO_DELAY && currentTime > 5 * INTRO_DELAY - APPROX_TIME_PER_FRAME)) {
			return "levelLogo.png";
		} else if (currentTime < 2 * INTRO_DELAY && currentTime > 2 * INTRO_DELAY - APPROX_TIME_PER_FRAME) {// "ready"
																											// logo
			return "readyLogo.png";
		} else if (currentTime < 3 * INTRO_DELAY && currentTime > 3 * INTRO_DELAY - APPROX_TIME_PER_FRAME) {// "set"
																											// logo
			return "setLogo.png";
		} else if (currentTime < 4 * INTRO_DELAY && currentTime > 4 * INTRO_DELAY - APPROX_TIME_PER_FRAME) {// "fight"
																											// logo
			return "fightLogo.png";
		}
		// Nếu logo không phải thay đổi
		return null;
	}

	// Vẽ chữ level
	public void drawLevel(Graphics2D g, int levelNumber, ImageObserver io) {
		// Tạo 1 hình chữ nhật kích thước 50*800 ở phía trên cùng
		g.fillRect(0, 0, GameStateHandler.FRAME_WIDTH, GameStateHandler.BLOCK_HEIGHT);

		// Vẽ người chơi
		player.draw(g);

		// Vẽ địch
		for (Enemy enemy : enemies) {
			enemy.draw(g);
		}

		// Vẽ từng bức tường
		for (Wall wall : walls) {
			wall.draw(g);
		}

		// Vẽ từng viên đạn
		for (Bullet bullet : bullets) {
			bullet.draw(g);
		}

	}

	// Các hàm getter()
	public Player getPlayer() {
		return player;
	}

	public ArrayList<Enemy> getEnemies() {
		return enemies;
	}

	public ArrayList<Wall> getWalls() {
		return walls;
	}

	public ArrayList<Bullet> getBullets() {
		return bullets;
	}

	// Trả về bản sao của 1 level
	public Level clone() {
		ArrayList<Enemy> enemiesClone = new ArrayList<>(enemies.size());
		for (Enemy enemy : enemies) {
			enemiesClone.add(enemy.clone());
		}

		ArrayList<Wall> wallsClone = new ArrayList<>(walls.size());
		for (Wall wall : walls) {
			wallsClone.add(wall.clone());
		}
		return new Level(player.clone(), enemiesClone, wallsClone);
	}

	// Các sự kiện nhấn phím và kích chuột
	public void keyPressed(KeyEvent e) {
		player.keyPressed(e);
	}

	public void keyReleased(KeyEvent e) {
		player.keyReleased(e);
	}

	public void mouseMoved(int[] newPos) {
		player.mouseMoved(newPos);
	}

	// Nếu chuột được nhấn, đạn sẽ được bắn ra
	public void mousePressed() {
		Bullet b = player.shootBullet();
		// Nếu người chơi bắn đạn ra
		if (b != null)
			bullets.add(b);
	}

	// Cập nhật lại đường đi có thể cho địch
	public void loadMap() {
		for (int x = 0; x < 16; x++) {
			for (int y = 0; y < 12; y++) {
				map[x][y] = new Node(x * 50, (y + 1) * 50);
			}
		}
		for (Wall w : walls) {
			int cellX = (int) (w.getX() / 50);
			int cellY = (int) ((w.getY() - 50) / 50);
			map[cellX][cellY] = null;

		}
	}

}

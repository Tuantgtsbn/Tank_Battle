//Tạo giao diện chính của game 

package tankfighter;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class GameFrame extends JFrame implements Runnable {
	// Khởi tạo
	private Sound gameSound = new Sound("tank-battle-13719.wav");
	private Thread gameRun;
	public GameFrame() {
		// Tạo ra giao diện game
		initUI();
	}

	// Tạo 1 giao diện game chính
	private void initUI() {
		// Đặt tên game
		setTitle("Tank Battle");

		// Thêm giao diện chính của game
		add(new DrawingSurface());
		// Thiết lập kích thước cửa sổ hiển thị game
		setSize(816, 689); // Khung bên trong có 800 x 650
		// Khi nhấn nút close thì giao diện game sẽ mất
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// Khi giao diện game hiện ra, thì sẽ xuất hiện ở giữa màn hình máy tính
		setLocationRelativeTo(null);
		gameRun=new Thread(this);
		gameRun.start();
	}
	

	@Override
	public void run() {
		this.setVisible(true);
		gameSound.play();
	}

	public static void main(String[] args) {
		GameFrame a=new GameFrame();

//		Sound gameSound = new Sound("tank-battle-13719.wav");
//
//		Runnable soundRun = new Runnable() {
//			public void run() {
//
//				gameSound.play();
//
//			}
//		};
//
//		// Tạo một thread mới và bắt đầu nó
//		new Thread(soundRun).start();

	}
}

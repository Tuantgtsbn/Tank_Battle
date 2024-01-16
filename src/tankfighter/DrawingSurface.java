package tankfighter;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import javax.swing.JPanel;
import javax.swing.Timer;

public class DrawingSurface extends JPanel implements ActionListener, MouseMotionListener, MouseListener {

	GameStateHandler game;
	// timer giúp ta update game sau một khoảng thời gian cố định
	Timer timer;

	public DrawingSurface() {
		game = new GameStateHandler();
		// đọc bàn phím
		addKeyListener(new TAdapter());
		// theo dõi chuyển động của chuột => xác định vị trí của chuột theo tọa độ x, y
		addMouseMotionListener(this);
		// đọc việc nhấn chuột
		addMouseListener(this);
		setFocusable(true);
		setBackground(Color.WHITE);
		timer = new Timer(10, this);// delay per frame (ms). default: 10
		timer.start();
	}

	// vẽ
	private void doDrawing(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		game.drawGame(g2d, this);
	}

	// class xử lý nhập bàn phím
	private class TAdapter extends KeyAdapter {

		@Override
		public void keyReleased(KeyEvent e) {
			game.keyReleased(e);
		}

		@Override
		public void keyPressed(KeyEvent e) {
			game.keyPressed(e);
		}
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// lấy tọa độ của chuột trên Panel
		int[] pos = { e.getX(), e.getY() };
		game.mouseMoved(pos);
	}

	@Override
	public void mouseDragged(MouseEvent e) {
	}

	@Override
	public void mouseClicked(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e) {
		game.mousePressed();
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		doDrawing(g);
	}

	// khi timer hết thời gian thì update lại game và hiển thị lại
	@Override
	public void actionPerformed(ActionEvent e) {
		game.update();
		repaint();
	}

}
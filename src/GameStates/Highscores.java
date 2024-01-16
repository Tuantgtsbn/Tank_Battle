package GameStates;

import GameObjects.Button;
import GameObjects.Score;
import Resources.ResourceGetter;
import tankfighter.GameStateHandler;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.ImageObserver;
import java.util.ArrayList;
import java.util.Scanner;

public class Highscores extends GameState {

	private String recentPlayerName;
	private int recentPlayerScore;

	// tên của những người ghi điểm cao nhất
	private Button[] top5nameButtons = new Button[6];
	private Button[] top5scoreButtons = new Button[6];

	// ann ArrayList of all the highscores
	private ArrayList<Score> allscores = new ArrayList<>();
	private Score filler = new Score("Player", 0);
	final private Color transparent = new Color(0, 0, 0, 0);
	final private Color semiTransparentGray = new Color(100, 100, 100, 80);

	public Highscores() {
		super();
		setLogoImageFromPath("highscoresLogo.png");
		resetState();
	}

	public void quickSort(ArrayList<Score> n, int left, int right) {
		int i = left;
		int j = right;
		int pivot = n.get((left + right) / 2).getScore(); // giá trị chính giữa
		Score temp = new Score("", 0);
		while (i < j) {
			while (n.get(i).getScore() > pivot) {
				i++;
			}
			while (n.get(j).getScore() < pivot) {
				j--;
			}
			if (i <= j) {
				temp = n.get(i); // temp = số bên trái
				n.set(i, n.get(j)); // số bên trái chuyển thành số bên phải
				n.set(j, temp); // số bên phải = số bên trái trước đó (hoán đổi hoàn tất)
				i++; // di chuyển index sang phải
				j--; // di chuyển index sang trái
			}
		}
		if (left < j) {
			quickSort(n, left, j);
		}
		if (right > i) {
			quickSort(n, i, right);
		}
	}

	public void readHighscores() {
		// xóa điểm cao để nếu người dùng rời khỏi điểm cao và sau đó quay lại thì điểm
		// sẽ không được nhân đôi
		allscores.clear();

		// các biến được sử dụng để giữ các giá trị khi đọc tệp dữ liệu
		String name;
		int score;

		//System.out.println(System.getProperty("user.home") + "\\TankFighter_Highscores" + "\\highscores.txt");
		Scanner s = ResourceGetter.getHighscoresScanner();

		while (s.hasNextLine()) {
			name = s.nextLine();
			score = Integer.parseInt(s.nextLine());
			allscores.add(new Score(name, score)); // thêm điểm vào ArrayList

		}

		// cần thêm 5 điểm phụ để trò chơi có thể
		// tải 5 điểm cao ngay cả khi không có ai chơi game
		for (int i = 0; i < 5; i++) {
			allscores.add(filler);
		}

		// sắp xếp điểm cao theo thứ tự giảm dần
		quickSort(allscores, 0, allscores.size() - 1);

	}

	@Override
	public int update() {
		return GameStateHandler.HIGHSCORES_STATE;// trạng thái trò chơi vẫn ở trạng thái này cho đến khi nhấn nút "trở
													// lại menu"

	}

	@Override
	public void draw(Graphics2D g, ImageObserver io) {
		super.drawLogo(g, io);
		for (Button nameButton : top5nameButtons) {
			nameButton.draw(g);
		}
		for (Button scoreButton : top5scoreButtons) {
			scoreButton.draw(g);
		}
		getButtons().get(0).drawWithShadow(g);
	}

	@Override
	/**
	 * 
	 * Đặt lại trạng thái, bao gồm: -> tính toán lại ArrayList của điểm cao -> tính
	 * toán lại tên và điểm của 5 điểm cao nhất -> nếu người chơi cuối cùng không có
	 * trong top 5 thì xếp họ ở vị trí thứ 5 -> Làm nổi bật tên và điểm của người
	 * chơi cuối cùng
	 */
	public void resetState() {
		// đọc và sắp xếp tất cả các điểm cao
		readHighscores();

		// tải 5 điểm cao nhất vào 5 tên và mảng điểm cao nhất
		// phần tử đầu tiên của mỗi mảng sẽ là tiêu đề
		top5nameButtons[0] = new Button(100, 180, 200, 30, transparent, "Name", 36, Color.BLACK);
		top5scoreButtons[0] = new Button(500, 180, 200, 30, transparent, "Score", 36, Color.BLACK);

		boolean playerInTop5 = false;
		String currName;
		int currScore;
		int currRank = 1;
		for (int i = 1; i < 6; i++) {

			currName = allscores.get(i - 1).getName();
			currScore = allscores.get(i - 1).getScore();
			if (i > 1 && currScore < allscores.get(i - 2).getScore()) {
				currRank++;
			}
			top5nameButtons[i] = new Button(150, 200 + 50 * i, 275, 30, transparent, currRank + ". " + currName, 24,
					Color.BLACK, Button.LEFT_LOCATION);
			top5scoreButtons[i] = new Button(425, 200 + 50 * i, 200, 30, transparent, "" + currScore, 24, Color.BLACK,
					Button.RIGHT_LOCATION);

			// nếu người chơi nằm trong top 5, hãy đánh dấu điểm của họ
			if (recentPlayerName != null && recentPlayerName.equals(currName) && recentPlayerScore == currScore) {
				playerInTop5 = true;
				top5nameButtons[i].setBoxColor(semiTransparentGray);
				top5scoreButtons[i].setBoxColor(semiTransparentGray);
			}
		}

		// nếu người chơi không nằm trong top 5 điểm cao nhất, hiển thị tên của họ ở
		// cuối danh sách kèm theo thứ hạng của họ
		if (recentPlayerName != null && !playerInTop5) {
			// lấy thứ hạng của người chơi
			currRank = 1;// counter for getting their rank
			for (int i = 0; i < allscores.size(); i++) {

				// nếu điểm đã kiểm tra không phải là điểm của người chơi hiện tại, hãy thêm 1
				// vào bộ đếm
				if (i > 0 && allscores.get(i).getScore() < allscores.get(i - 1).getScore()) {
					currRank++;
				}

				// nếu điểm đã kiểm tra là điểm của người chơi hiện tại, hãy dừng tìm kiếm
				if (allscores.get(i).getScore() == recentPlayerScore) {
					break;
				}
			}
			// ghi điểm của người chơi hiện tại lên bảng điểm, cùng với thứ hạng của họ
			top5nameButtons[5] = new Button(150, 450, 275, 30, semiTransparentGray, currRank + ". " + recentPlayerName,
					24, Color.BLACK, Button.LEFT_LOCATION);
			top5scoreButtons[5] = new Button(425, 450, 200, 30, semiTransparentGray, "" + recentPlayerScore, 24,
					Color.BLACK, Button.RIGHT_LOCATION);
		}
		addReturnToMenuButton();
	}

	// setters
	public void setRecentPlayerName(String name) {
		this.recentPlayerName = name;
	}

	public void setRecentPlayerScore(int score) {
		this.recentPlayerScore = score;
	}

}
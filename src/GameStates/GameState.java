package GameStates;

import GameObjects.Button;
import Resources.ResourceGetter;
import tankfighter.GameStateHandler;

import static tankfighter.GameStateHandler.FRAME_HEIGHT;
import static tankfighter.GameStateHandler.FRAME_WIDTH;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.util.ArrayList;

public abstract class GameState {
	// ArrayList các buttom
	private ArrayList<Button> buttons;
	// 1 hình ảnh của logo
	private BufferedImage logoImage;

	// nếu logo là một tập hợp các hình ảnh kết hợp (ví dụ: "Cấp 12" là "Cấp", "1"
	// và "2" kết hợp)
	// sau đó chúng ta sử dụng ArrayList để lưu trữ các hình ảnh thay vì một hình
	// ảnh duy nhất
	protected ArrayList<BufferedImage> logoImages;
	protected ArrayList<Integer> logoWidths;

	// chiều rộng của logo
	private int logoWidth = 0;

	// chiều cao của logo
	public final static int STANDARD_LOGO_HEIGHT = 125;

	// Phương thức khởi tạo
	// khởi tạo arraylist các buttom
	public GameState() {
		buttons = new ArrayList<>();
		logoImages = new ArrayList<>();
		logoWidths = new ArrayList<>();
	}

	// các phương thức trừu tượng (phải được ghi đè trong các lớp con)
	public abstract void draw(Graphics2D g, ImageObserver io);

	public abstract int update();

	public abstract void resetState();

	// vẽ logo
	public void drawLogo(Graphics2D g, ImageObserver io) {

		// nếu chiều rộng của logo bằng 0, có nghĩa là logo chưa được điều chỉnh tỷ lệ.
		if (logoWidth == 0) {

			// nếu chưa được chia tỷ lệ, hãy chia tỷ lệ logo theo kích thước phù hợp
			int oWidth = logoImage.getWidth(io);// chiều rộng ban đầu
			int oHeight = logoImage.getHeight(io);// chiều cao ban đầu

			// chia tỷ lệ sao cho chiều cao cuối cùng là 125;
			double ratio = (double) oHeight / (double) STANDARD_LOGO_HEIGHT;
			logoWidth = (int) ((double) oWidth / ratio);

		}

		// nếu người chơi đang ở trạng thái InGame thì logo được vẽ ở y=0 để nhường chỗ
		// cho vẽ cấp độ
		// ngược lại nó được vẽ tại y = 40
		int y = 40;
		if (this instanceof InGame) {
			y = 0;
		}
		// vẽ logo
		g.drawImage(logoImage, GameStateHandler.FRAME_WIDTH / 2 - logoWidth / 2, y, logoWidth, STANDARD_LOGO_HEIGHT,
				io);
	}

	// trả về arraylist của các ArrayList<Button>
	public ArrayList<Button> getButtons() {
		return buttons;
	}

	// thêm một buttom vào ArrayList của các ArrayList<Button>
	public void addButton(Button button) {
		buttons.add(button);
	}

	// vì hầu hết các trạng thái của trò chơi đều yêu cầu nút này
	// chúng ta có thể tạo một phương thức được sử dụng cụ thể để thêm nó
	public void addReturnToMenuButton() {
		buttons.add(new Button(FRAME_WIDTH / 2 - Button.STANDARD_WIDTH / 2, FRAME_HEIGHT - 70, Button.STANDARD_WIDTH,
				Button.STANDARD_HEIGHT, Button.STANDARD_BOX_COLOR, "Back to Main Menu", Button.STANDARD_FONT_SIZE,
				Button.STANDARD_TEXT_COLOR));

	}

	// set hình ảnh logo thành BufferedImage
	public void setLogoImage(BufferedImage logoImage) {
		this.logoImage = logoImage;
		// đặt lại chiều rộng về 0 để phương thức vẽ tính toán lại chiều rộng mới của
		// logo
		logoWidth = 0;
	}

	// set hình ảnh logo từ đường dẫn tệp
	public void setLogoImageFromPath(String filepath) {
		setLogoImage(imageFromPath(filepath));
	}

	// tạo BufferedImage từ đường dẫn tệp chuỗi.
	public BufferedImage imageFromPath(String filepath) {
		return ResourceGetter.getBufferedImage(filepath);
	}
}
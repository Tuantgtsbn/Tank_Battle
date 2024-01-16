/*
 * A class which can create, draw, and test if a certain position is within a button.
 * a Button consists of a box around the button and the text inside of the button.
 */
package GameObjects;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.font.LineMetrics;
import java.awt.geom.Rectangle2D;

public class Button {

	// các kích thước và màu chuẩn cho một nút trong trò chơi
	final public static int STANDARD_WIDTH = 200;
	final public static int STANDARD_HEIGHT = 50;
	final public static int STANDARD_FONT_SIZE = 20;
	final public static Color STANDARD_BOX_COLOR = new Color(40, 120, 10);
	final public static Color STANDARD_TEXT_COLOR = Color.WHITE;
	// các biến lớp hiển thị số nguyên tương ứng với các vị trí của nút
	final public static int CENTER_LOCATION = 0;
	final public static int LEFT_LOCATION = 1;
	final public static int RIGHT_LOCATION = 2;

	private int x, y; // tọa độ của góc trên bên trái của hộp chứa văn bản
	final private int width, height, textSize; // width và height là kích thước của hộp (không phải là văn bản)
	private String text;
	private Color boxColor, textColor;
	private int textLocation; // nếu văn bản nên ở bên trái, giữa hoặc bên phải của hộp xung quanh

	// constructor
	public Button(int x, int y, int width, int height, Color boxColor, String text, int textSize, Color textColor) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.boxColor = boxColor;
		this.text = text;
		this.textSize = textSize;
		this.textColor = textColor;
		this.textLocation = CENTER_LOCATION;
	}

	public Button(int x, int y, int width, int height, Color boxColor, String text, int textSize, Color textColor,
			int textLocation) {
		this(x, y, width, height, boxColor, text, textSize, textColor);
		this.textLocation = textLocation;
	}

	// getters
	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public String getText() {
		return text;
	}

	// setters
	public void setBoxColor(Color boxColor) {
		this.boxColor = boxColor;
	}

	public void setTextColor(Color textColor) {
		this.textColor = textColor;
	}

	public void setText(String text) {
		this.text = text;
	}

	/**
	 * Draws the button with a black "shadow" to the bottom right of the actual
	 * button position
	 * 
	 * @param g the Graphics2D object to draw with
	 */
	public void drawWithShadow(Graphics2D g) {
		// vẽ bóng đổ
		g.setColor(Color.BLACK);
		g.fillRect(x + 5, y + 5, width, height);
		// vẽ nút thực tế
		draw(g);
	}

	/**
	 * Draws the button
	 * 
	 * @param g the Graphics2D object to draw with
	 */
	public void draw(Graphics2D g) {
		// vẽ hộp bao quanh văn bản
		g.setColor(boxColor);
		g.fillRect(x, y, width, height);

		// vẽ văn bản
		// tạo Font
		Font f = new Font("sansSerif", Font.PLAIN, textSize);
		// tiếp theo, chúng ta lấy một số đo lường về văn bản để đặt văn bản chính xác
		// vào trung tâm của hộp
		// lấy các thông số về dòng để biết chiều cao của font chữ
		LineMetrics lm = f.getLineMetrics(text, g.getFontRenderContext());
		// lấy hình chữ nhật bao quanh văn bản
		Rectangle2D textBound = f.getStringBounds(text, g.getFontRenderContext());
		// lấy chiều rộng của văn bản
		double textWidth = textBound.getWidth();
		// lấy chiều cao của văn bản
		double textHeight = lm.getHeight();
		// lấy vị trí x cuối cùng sẽ vẽ văn bản
		double fontX;
		if (textLocation == CENTER_LOCATION) {
			fontX = x + (width / 2) - (textWidth / 2);
		} else if (textLocation == LEFT_LOCATION) {
			// leading là khoảng cách giữa baseline của một dòng với dòng tiếp theo
			// số này x 5 để xác định khoảng cách giữa đầu của hộp và đầu văn bản
			double leadingSpace = lm.getLeading() * 5;
			fontX = x + leadingSpace;
		} else {// nếu textLocation == RIGHT_LOCATION
			double leadingSpace = lm.getLeading() * 5;
			fontX = x + width - leadingSpace - textWidth;
		}
		// lấy vị trí y cuối cùng sẽ vẽ văn bản
		double fontBaselineY = y + (height / 2) + (textHeight / 2) - lm.getDescent();
		g.setFont(f);
		g.setColor(textColor);
		// vẽ văn bản
		g.drawString(text, (int) fontX, (int) fontBaselineY);

	}

	/**
	 * Checks whether or not a certain position is within the bounds of the button.
	 * Used to check if the user has clicked the button given the mouse position.
	 * 
	 * @param pos the position to check (x, y)
	 * @return true if position pos is within the button, false otherwise
	 */
	public boolean isInBounds(int[] pos) {
		return pos[0] > x && pos[0] < x + width && pos[1] > y && pos[1] < y + height;

	}

	// tạo một bản sao của nút
	public Button clone() {
		return new Button(x, y, width, height, boxColor, text, textSize, textColor);

	}
}
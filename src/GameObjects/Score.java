/*
 * Lớp Score được sử dụng để chứa điểm và tên người chơi, và được sử dụng để tìm kiếm cũng như sắp xếp bảng xếp hạng điểm
 */

package GameObjects;

public class Score {

	private String name;// tên người chơi
	private int score;// điểm người chơi đạt được

	// Khởi tạo
	public Score() {
		name = "";
		score = -1;
	}

	// Khởi tạo điểm
	public Score(String n, int s) {
		name = n;
		score = s;
	}

	// Các phương thức getter
	public String getName() {
		return name;
	}

	public int getScore() {
		return score;
	}

	// Các phương thức setter
	public void setName(String name) {
		this.name = name;
	}

	public void setScore(int score) {
		this.score = score;
	}

}

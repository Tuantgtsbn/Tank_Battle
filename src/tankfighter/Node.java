package tankfighter;

public class Node {
	private int x, y, cellX, cellY; // vị trí tọa độ x,y và vị trí ô cellx, celly
	private Node parent = null; // node cha
	private boolean visited = false;// biến đánh dấu đã thăm hay chưa

	public Node(int x, int y) {
		this.x = x;
		this.y = y;
		cellX = (int) Math.round((double) x / 50.0); // từ tọa độ đưa về thứ tự ô
		cellY = (int) Math.round((double) y / 50.0); // từ tọa độ đưa về thứ tự ô
	}

	public boolean equals(Node n) {
		return n.cellX == cellX && n.cellY == cellY;
	}

	public boolean isVisited() {
		return visited;
	}

	public void setVisited(boolean visited) {
		this.visited = visited;
	}

	public int getCellX() {
		return cellX;
	}

	public int getCellY() {
		return cellY;
	}

	// gán node cha
	public void setParent(Node parent) {
		this.parent = parent;
	}

	// trả về node cha
	public Node getParent() {
		return parent;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public String toString() {
		return "[" + cellX + ", " + cellY + "]";
	}

	public Node clone() {
		return new Node(x, y);
	}
}
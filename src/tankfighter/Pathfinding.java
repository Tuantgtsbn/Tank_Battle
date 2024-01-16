package tankfighter;

import java.util.ArrayList;

public class Pathfinding {

	private Node start;
	private Node end;
	private Node map[][];

	private ArrayList<Node> queue;
	private ArrayList<Node> path;

	// http://gregtrowbridge.com/a-basic-pathfinding-algorithm
	public Pathfinding(int startX, int startY, int endX, int endY, Node[][] map) {
		queue = new ArrayList<>();
		path = new ArrayList<>();
		this.map = map;
		start = new Node(startX, startY);
		end = new Node(endX, endY);
		queue.add(start);
		for (Node[] nodes : this.map) {
			for (Node node : nodes) {
				if (node != null) {
					node.setVisited(false);
				}
			}
		}
	}

	public void findPath() {
		while (queue.size() > 0) {// lặp đến khi nào hàng đợi rỗng thì thôi

			Node currentNode = queue.get(0); // Lấy nốt ở đầu hàng đợi
			queue.remove(0);

			if (currentNode.equals(end)) { // nếu gặp được điểm đích
				path.add(currentNode.getParent()); // thêm các điểm cha vào hàng đợi

				while (path.size() > 0 && currentNode.getParent() != null
						&& path.get(path.size() - 1).getParent() != null) {
					path.add(path.get(path.size() - 1).getParent());
				}
				queue.clear(); // xóa hàng đợi đi để dừng vòng lặp

			}
			// Thêm các nốt xung quanh vào trong hàng đợi.
			checkNeighbours(currentNode);

		}
	}

	public void checkNeighbours(Node current) {

		// vị trí hiện tại
		int cellX = current.getCellX();
		int cellY = current.getCellY() - 1;

		// Kiểm tra phía Bắc
		if (cellY - 1 >= 0 && map[cellX][cellY - 1] != null && !map[cellX][cellY - 1].isVisited()) {

			map[cellX][cellY - 1].setParent(current);
			map[cellX][cellY - 1].setVisited(true);
			queue.add(map[cellX][cellY - 1]);

		}

		// kiểm tra hướng Nam
		if (cellY + 1 < 12 && map[cellX][cellY + 1] != null && !map[cellX][cellY + 1].isVisited()) {

			map[cellX][cellY + 1].setParent(current);
			map[cellX][cellY + 1].setVisited(true);
			queue.add(map[cellX][cellY + 1]);

		}

		// phía Đông
		if (cellX + 1 < 16 && map[cellX + 1][cellY] != null && !map[cellX + 1][cellY].isVisited()) {

			map[cellX + 1][cellY].setParent(current);
			map[cellX + 1][cellY].setVisited(true);
			queue.add(map[cellX + 1][cellY]);

		}

		// phía Tây
		if (cellX - 1 >= 0 && map[cellX - 1][cellY] != null && !map[cellX - 1][cellY].isVisited()) {

			map[cellX - 1][cellY].setParent(current);
			map[cellX - 1][cellY].setVisited(true);
			queue.add(map[cellX - 1][cellY]);

		}

		// Kiểm tra Đông Bắc
		if (cellX + 1 < 16 && cellY - 1 >= 0 && map[cellX + 1][cellY - 1] != null && map[cellX][cellY - 1] != null
				&& map[cellX + 1][cellY] != null && !map[cellX + 1][cellY - 1].isVisited()) {

			map[cellX + 1][cellY - 1].setParent(current);
			map[cellX + 1][cellY - 1].setVisited(true);
			queue.add(map[cellX + 1][cellY - 1]);

		}

		// kiểm tra Đông Nam
		if (cellX + 1 < 16 && cellY + 1 < 12 && map[cellX + 1][cellY + 1] != null && map[cellX][cellY + 1] != null
				&& map[cellX + 1][cellY] != null && !map[cellX + 1][cellY + 1].isVisited()) {

			map[cellX + 1][cellY + 1].setParent(current);
			map[cellX + 1][cellY + 1].setVisited(true);
			queue.add(map[cellX + 1][cellY + 1]);

		}

		// phía Tây Nam
		if (cellX - 1 >= 0 && cellY + 1 < 12 && map[cellX - 1][cellY + 1] != null && map[cellX][cellY + 1] != null
				&& map[cellX - 1][cellY] != null && !map[cellX - 1][cellY + 1].isVisited()) {

			map[cellX - 1][cellY + 1].setParent(current);
			map[cellX - 1][cellY + 1].setVisited(true);
			queue.add(map[cellX - 1][cellY + 1]);

		}

		// phía Tây Bắc
		if (cellX - 1 >= 0 && cellY - 1 >= 0 && map[cellX - 1][cellY - 1] != null && map[cellX - 1][cellY] != null
				&& map[cellX][cellY - 1] != null && !map[cellX - 1][cellY - 1].isVisited()) {

			map[cellX - 1][cellY - 1].setParent(current);
			map[cellX - 1][cellY - 1].setVisited(true);
			queue.add(map[cellX - 1][cellY - 1]);

		}
	}

	public ArrayList<Node> getPath() {
		return path;
	}

}
/*
    This is a vector class that defines and creates vectors with direction. It was created to mathematically simplify
    collision between tanks. Because tanks can be on an angle, they have a direction, as such, this class along with
    it's methods such as dot product, projection, etc.. are used in collision.
 */
package GameObjects;

public class Vector {
	private float x; // trục x
	private float y;// trục y

	// x, y của vector
	public Vector(float x, float y) {
		this.x = x;
		this.y = y;
	}

	// Ghi chú: Các góc của các hình chữ nhật trong mảng phải được sắp xếp theo thứ
	// tự
	// (Chúng không thể đi từ góc này đến góc đối diện)
	/**
	 * 
	 * @param r1 vector of one tanks side
	 * @param r2 vector of other tanks side
	 * @return if they are colliding (true if they are, false if they are not)
	 */
	public static boolean areRotatedRectanglesColliding(Vector[] r1, Vector[] r2) {
		// các trục sẽ chiếu các hình chữ nhật lên
		Vector[] axes = new Vector[4];
		// trục vuông góc với các cạnh của r1
		axes[0] = r1[0].subtract(r1[1]);
		axes[1] = r1[1].subtract(r1[2]);
		// trục vuông góc với các cạnh của r2
		axes[2] = r2[0].subtract(r2[1]);
		axes[3] = r2[1].subtract(r2[2]);

		for (Vector axis : axes) { // với mỗi vector trong các trục
			Vector[] r1Proj = new Vector[4];
			Vector[] r2Proj = new Vector[4];

			// chiếu mỗi vector trong hai mảng tham số lên trục hiện tại (vector từ axes)
			for (int i = 0; i < 4; i++) {
				r1Proj[i] = r1[i].projectOnto(axis);
				r2Proj[i] = r2[i].projectOnto(axis);
			}

			// tìm vector chiếu ngắn nhất và dài nhất trong hai mảng
			Vector r1Shortest = shortestParallelVector(r1Proj);
			Vector r1Longest = longestParallelVector(r1Proj);
			Vector r2Shortest = shortestParallelVector(r2Proj);
			Vector r2Longest = longestParallelVector(r2Proj);

			// nếu vector chiếu dài nhất từ r1 nhỏ hơn vector chiếu ngắn nhất từ r2
			// nếu các giá trị x bằng nhau, kiểm tra y
			if (r1Longest.x < r2Shortest.x || (r1Longest.x == r2Shortest.x && r1Longest.y < r2Shortest.y)) {
				return false; // chúng không chạm nhau
			}
			// nếu vector chiếu dài nhất từ r2 nhỏ hơn vector chiếu ngắn nhất từ r1
			// nếu các giá trị x bằng nhau, kiểm tra y
			if (r2Longest.x < r1Shortest.x || (r2Longest.x == r1Shortest.x && r2Longest.y < r1Shortest.y)) {
				return false; // chúng không chạm nhau
			}

		}
		return true; // chúng chạm vào nhau
	}

	/**
	 * 
	 * @param v array of vectors
	 * @return the longest vector in the array of vectors
	 */
	public static Vector longestParallelVector(Vector[] v) {
		int index = 0;
		float longestX = v[0].x;
		for (int i = 1; i < v.length; i++) { // duyệt mảng
			if (v[i].x > longestX) { // nếu giá trị x ở chỉ số hiện tại lớn hơn giá trị x trước đó
				longestX = v[i].x; // đặt lại giá trị của x
				index = i; // đặt lại chỉ số mới
			} else if (v[i].x == longestX) { // nếu giá trị x giống với giá trị x dài nhất trước đó
				if (v[i].y > v[index].y) { // kiểm tra giá trị y
					index = i; // đặt lại chỉ số mới
				}
			}
		}
		return v[index]; // trả về chỉ số của vector dài nhất trong mảng
	}

	/**
	 * 
	 * @param v takes an array of vectors
	 * @return finds and returns the shortest vector in the array
	 */
	public static Vector shortestParallelVector(Vector[] v) {
		int index = 0;
		float shortestX = v[0].x;
		for (int i = 1; i < v.length; i++) { // duyệt mảng
			if (v[i].x < shortestX) { // tìm giá trị x ngắn nhất
				shortestX = v[i].x;
				index = i; // đặt chỉ số cần trả về
			} else if (v[i].x == shortestX) { // nếu hai giá trị x giống nhau, so sánh y
				if (v[i].y < v[index].y) { // nếu y nhỏ hơn
					index = i; // đặt chỉ số cần trả về là vector mới
				}
			}
		}
		return v[index]; // trả về chỉ số của vector ngắn nhất
	}

	// trừ hai vector
	public Vector subtract(Vector v) {
		return new Vector(x - v.x, y - v.y);
	}

	// nhân vector với một hằng số để tăng kích thước của vector
	public Vector multiplyByScalar(float s) {
		return new Vector(x * s, y * s);
	}

	// chiếu một vector
	public Vector projectOnto(Vector v) {
		return v.multiplyByScalar(this.dotProduct(v) / (float) (Math.pow(v.getMagnitude(), 2)));
	}

	/**
	 * 
	 * @param v takes a vector
	 * @return the dot product (VectA*VectB*CosAngle)
	 */
	public float dotProduct(Vector v) {
		return x * v.x + y * v.y;
	}

	/**
	 * returns the size of the vector, without direction
	 * 
	 * @return the magnitude of the vector
	 */
	public float getMagnitude() {
		return (float) Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
	}

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}

	@Override
	public String toString() {
		return "(" + x + ", " + y + ")";
	}
}
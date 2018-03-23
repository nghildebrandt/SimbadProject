package main.java.softdesign.map;

import javax.vecmath.Point3d;
import main.java.softdesign.Environment;

public class CartesianCoordinate {
	public int x;
	public int z;

	public CartesianCoordinate(int x, int z) {
		this.x = x;
		this.z = z;
	}

	public CartesianCoordinate(Point3d point) {
		this.x = (int) Math.round(point.x) + cartesianOffset();
		this.z = (int) Math.round(point.z) + cartesianOffset();
	}

	private static int cartesianOffset() {
		return (Environment.SIZE - 1) / 2;
	}
}

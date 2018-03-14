package main.java.softdesign.wrappers;

import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

public class Coordinate {

	private static final int HEIGHT = 0;

	private int x;
	private int y;

	public Coordinate(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public Coordinate(Point3d point3d) {
		this.x = (int) point3d.x;
		this.y = (int) point3d.z;
	}

	public Point3d toPoint3d() {
		return new Point3d(x, HEIGHT, y);
	}

	public Vector3d toVector3d() {
		return new Vector3d(x, HEIGHT, y);
	}
}

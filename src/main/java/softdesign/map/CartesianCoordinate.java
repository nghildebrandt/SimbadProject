package main.java.softdesign.map;

import javax.vecmath.Point3d;
import main.java.softdesign.Environment;

public class CartesianCoordinate {

	private static final double GRID_STICKYINESS = 0.0001;

	public int x;
	public int z;

	private int environmentSize;
	private boolean onGrid;

	public CartesianCoordinate(int x, int z) {
		this.x = x;
		this.z = z;
	}

	public CartesianCoordinate(Point3d point, int environmentSize) {
		this.environmentSize = environmentSize;
		double estimatedX = point.x + cartesianOffset();
		double estimatedZ = point.z + cartesianOffset();

		x = (int) Math.round(estimatedX);
		z = (int) Math.round(estimatedZ);

		onGrid = estimatedX - x < GRID_STICKYINESS && estimatedZ - z < GRID_STICKYINESS;
	}

	public boolean isOnGrid() {
		return onGrid;
	}

	private int cartesianOffset() {
		return (this.environmentSize - 1) / 2;
	}
}

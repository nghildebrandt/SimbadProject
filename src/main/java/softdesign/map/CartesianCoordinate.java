package main.java.softdesign.map;

import javax.vecmath.Tuple3d;
import main.java.softdesign.Environment;

public class CartesianCoordinate {

	private static final double GRID_STICKYINESS = 0.0001;

	public int x;
	public int z;

	private int gridSize;
	private boolean onGrid;

	public CartesianCoordinate(int x, int z) {
		this.x = x;
		this.z = z;
	}

	public CartesianCoordinate(Tuple3d point, int gridSize) {
		this.gridSize = gridSize;
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
		return (gridSize - 1) / 2;
	}
}

package main.java.softdesign;

import main.java.softdesign.exceptions.GridIndexOutOfBoundsException;

public class Map {

	public static final int VISITED = 1;
	public static final int UNVISITED = 0;
	public static final int WALL = -1;

	private final int cartesianOffset;

	private int numberOfCoveredPoints;
	private int size;
	private int[][] grid;

	Map() {
		this.numberOfCoveredPoints = 0;
		this.size = Environment.SIZE + 1;
		this.grid = new int[size][size];
		this.cartesianOffset = -Environment.SIZE / 2;
	}

	public void setPoint(int xCoord, int zCoord, int value) {
		this.numberOfCoveredPoints++;
		int x = xCoord - cartesianOffset;
		int z = zCoord - cartesianOffset;

		if (x < 0 || x >= size || z < 0 || z >= size) {
			throw new GridIndexOutOfBoundsException("Cannot set a point outside the grid.");
		}

		grid[x][z] = value;
	}

	public int getPoint(int xCoord, int zCoord) {
		int x = xCoord - cartesianOffset;
		int z = zCoord - cartesianOffset;

		if (x < 0 || x >= size || z < 0 || z >= size) {
			throw new GridIndexOutOfBoundsException("Cannot get a point outside the grid.");
		}

		return grid[x][z];
	}

	public int getNumberOfCoveredPoints() {
		return numberOfCoveredPoints;
	}
}

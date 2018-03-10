package main.java.softdesign;

import main.java.softdesign.exceptions.GridIndexOutOfBoundsException;

public class Map {
	public static final int VISITED = 1;
	public static final int UNVISITED = 0;
	public static final int WALL = -1;
	private int pointsVisited;
	private int[][] grid;
	private int size;
	private int min;

	Map() {
		pointsVisited = 0;
		this.size = Environment.WORLD_SIZE + 1;
		this.grid = new int[size][size];
		this.min = -Environment.WORLD_SIZE / 2;
	}

	public void setPoint(int xCoord, int zCoord, int value) {
		this.pointsVisited++;
		int x = xCoord - min;
		int z = zCoord - min;

		if (x < 0 || x >= size || z < 0 || z >= size) {
			throw new GridIndexOutOfBoundsException("Cannot set a point outside the grid.");
		}

		grid[x][z] = value;
	}

	public int totalNumberOfPointsCovered() {
		return this.pointsVisited;
	}

	public int getPoint(int xCoord, int zCoord) {
		int x = xCoord - min;
		int z = zCoord - min;

		if (x < 0 || x >= size || z < 0 || z >= size) {
			throw new GridIndexOutOfBoundsException("Cannot get a point outside the grid.");
		}

		return grid[x][z];
	}
}

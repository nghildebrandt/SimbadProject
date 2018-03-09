package main.java.softdesign;

import main.java.softdesign.exceptions.GridIndexOutOfBoundsException;

public class Map {

	public static final int WALL = -1;

	private int[][] grid;
	private int size; // TODO rename
	private int min; // TODO rename

	Map() {
		this.size = Environment.WORLD_SIZE + 1;
		this.grid = new int[size][size];
		this.min = -Environment.WORLD_SIZE / 2;
	}

	public void setPoint(int xCoord, int zCoord, int value) {
		int x = xCoord - min;
		int z = zCoord - min;

		if (x < 0 || x >= size || z < 0 || z >= size) {
			throw new GridIndexOutOfBoundsException("Cannot set a point outside the grid.");
		}

		grid[x][z] = value;
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

  

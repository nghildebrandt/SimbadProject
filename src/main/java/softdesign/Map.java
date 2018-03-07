package main.java.softdesign;

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

	public void setPoint(int xCoord, int zCoord, int value) throws Exception {
		int x = xCoord - min;
		int z = zCoord - min;

		if (x < 0 || x >= size || z < 0 || z >= size) {
			throw new Exception("Cannot set a point outside the map.");
		} else {
			grid[x][z] = value;
		}
	}

	public int getPoint(int xCoord, int zCoord) throws Exception {
		int x = xCoord - min;
		int z = zCoord - min;

		if (x < 0 || x >= size || z < 0 || z >= size) {
			throw new Exception("Cannot get a point outside the map.");
		} else {
			return grid[x][z];
		}
	}
}

  

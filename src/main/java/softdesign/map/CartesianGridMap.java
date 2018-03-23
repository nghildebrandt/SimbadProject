package main.java.softdesign.map;

import javax.vecmath.Point3d;

import main.java.softdesign.Environment;

public class CartesianGridMap extends AbstractMap {

	private Tile[][] grid;

	public CartesianGridMap() {
		super();

		int axisPoints = Environment.SIZE;
		this.grid = new Tile[axisPoints][axisPoints];

		for(Tile[] row : grid) {
			java.util.Arrays.fill(row, Tile.EMPTY);
		}
	}

	public double getCoveredRatio() {
		return 0d;
	}

	@Override
	public Tile getTile(CartesianCoordinate coordinate) {
		try {
			return grid[coordinate.x][coordinate.z];
		} catch(ArrayIndexOutOfBoundsException e) {
			return Tile.WALL;
		}
	}

	@Override
	public void setTile(CartesianCoordinate coordinate, Tile tile) {
		try {
			grid[coordinate.x][coordinate.z] = tile;
		} catch(ArrayIndexOutOfBoundsException e) {}
	}

	// TODO definitely remove this before submitting, its ugly af.
	@Override
	public String toString() {
		String s = "\n+";

		for(int j = 0; j < grid[0].length; j++) {
			s += "-";
		}
		s += "+\n";

		for(int z = 0; z < grid.length; z++) {
			s += "|";
			for(int x = 0; x < grid[z].length; x++) {
				if(grid[x][z] == Tile.COVERED) {
					s += "X";
				} else if(grid[x][z] == Tile.ROBOT) {
					s += "e";
				} else if(grid[x][z] == Tile.WALL) {
					s += "O";
				} else {
					s += " ";
				}
			}
			s += "|\n";
		}

		s += "+";
		for(int j = 0; j < grid[grid.length - 1].length; j++) {
			s += "-";
		}
		s += "+\n";

		return s;
	}
}

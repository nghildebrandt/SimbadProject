package main.java.softdesign.map;

import javax.vecmath.Point3d;
import main.java.softdesign.Environment;

public class CartesianGridMap extends AbstractMap {

	private Tile[][] grid;
	private int coveredCount = 0;

	public CartesianGridMap() {
		super();

		int axisPoints = Environment.SIZE;
		this.grid = new Tile[axisPoints][axisPoints];

		for(int i = 0; i < grid.length; i++) {
			for(int j = 0; j < grid[i].length; j++) {
				grid[i][j] = Tile.EMPTY;
			}
		}
	}

	public double getCoveredRatio() {
		return (double) coveredCount / Math.pow(Environment.SIZE + 1, 2);
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
	public void markAsCovered(CartesianCoordinate coordinate) {
		if(getTile(coordinate) != Tile.COVERED) { coveredCount++; }

		setTile(coordinate, Tile.COVERED);
	}

	@Override
	public void markAsWall(CartesianCoordinate coordinate) {
		setTile(coordinate, Tile.WALL);
	}
	
	private void setTile(CartesianCoordinate coordinate, Tile tile) {
		grid[coordinate.x][coordinate.z] = tile;
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

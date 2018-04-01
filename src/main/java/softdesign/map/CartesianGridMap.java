package main.java.softdesign.map;

import java.util.Arrays;

public class CartesianGridMap extends AbstractMap {

	private Tile[][] grid;

	public CartesianGridMap(int size) {
		super(size);

		this.grid = new Tile[size][size];
		for (Tile[] row : grid) {
			Arrays.fill(row, Tile.EMPTY);
		}
	}

	@Override
	public double getCoverage() {
		int covered = 0;
		int toCover = 0;

		for (Tile[] row : grid) {
			for (Tile tile : row) {
				if (tile == Map.Tile.COVERED) covered++;
				if (tile == Map.Tile.EMPTY || tile == Map.Tile.ROBOT) toCover++;
			}
		}

		return (double) covered / (covered + toCover);
	}

	@Override
	public Tile getTile(CartesianCoordinate coordinate) {
		try {
			return grid[coordinate.x][coordinate.z];
		} catch (ArrayIndexOutOfBoundsException e) {
			return Tile.WALL;
		}
	}

	@Override
	public void markAsRobot(CartesianCoordinate coordinate) {
		setTile(coordinate, Tile.ROBOT);
	}

	@Override
	public void markAsWall(CartesianCoordinate coordinate) {
		setTile(coordinate, Tile.WALL);
	}

	@Override
	public void markAsCovered(CartesianCoordinate coordinate) {
		setTile(coordinate, Tile.COVERED);
		incrementNumberOfCoveredPoints();
	}

	private void setTile(CartesianCoordinate coordinate, Tile tile) {
		try {
			grid[coordinate.x][coordinate.z] = tile;
		} catch (ArrayIndexOutOfBoundsException e) {
		}
	}
}

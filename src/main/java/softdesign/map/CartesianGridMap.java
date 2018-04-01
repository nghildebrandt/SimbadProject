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
	public void markAsWall(CartesianCoordinate coordinate) {
		setTile(coordinate, Tile.WALL);
	}

	@Override
	public void markAsCovered(CartesianCoordinate coordinate) {
		setTile(coordinate, Tile.COVERED);
		incrementNumberOfCoveredPoints();
	}

	@Override
	public void markAsRobot(CartesianCoordinate coordinate) {
		setTile(coordinate, Tile.ROBOT);
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
			return grid[coordinate.getX()][coordinate.getZ()];
		} catch (ArrayIndexOutOfBoundsException e) {
			return Tile.WALL;
		}
	}

	private void setTile(CartesianCoordinate coordinate, Tile tile) {
		int x = coordinate.getX();
		int z = coordinate.getZ();

		if (isNonReachableEdge(x) || isNonReachableEdge(z)) {
			return;
		}

		grid[x][z] = tile;
	}

	private boolean isNonReachableEdge(int coordinatePoint) {
		return coordinatePoint == -1 || coordinatePoint == getSize();
	}
}

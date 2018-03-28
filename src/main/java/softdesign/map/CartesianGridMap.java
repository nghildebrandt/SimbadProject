package main.java.softdesign.map;

import javax.vecmath.Point3d;
import java.util.Arrays;

import main.java.softdesign.Environment;

public class CartesianGridMap extends AbstractMap {

	private Tile[][] grid;
	private int size;

	public CartesianGridMap(int size) {
		super();

		this.grid = new Tile[size][size];
		this.size = size;

		for(Tile[] row : grid) {
			Arrays.fill(row, Tile.EMPTY);
		}
	}

	public double getCoveredRatio() {
		int covered = 0;
		int toCover = 0;

		for(Tile[] row : grid) {
			for(Tile tile: row) {
				if(tile == Map.Tile.COVERED) covered++;
				if(tile == Map.Tile.EMPTY || tile == Map.Tile.ROBOT) toCover++;
			}
		}

		return (double) covered / (covered + toCover);
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

	@Override
	public int getSize() {
		return size;
	}

	// TODO definitely remove this before submitting, its ugly af.
	@Override
	public String toString() {
		String s = "\n|";

		for(int j = 0; j < grid[0].length; j++) { s += "-"; } s += "|\n|";

		for(int z = 0; z < grid.length; z++) { for(int x = 0; x < grid[z].length; x++) {
			if(grid[x][z] == Tile.COVERED) { s += "X"; }
			if(grid[x][z] == Tile.ROBOT) { s += "e"; }
			if(grid[x][z] == Tile.WALL) { s += "O"; }
			if(grid[x][z] == Tile.EMPTY) { s += " "; }
		} s += "|\n|"; }

		for(int j = 0; j < grid[grid.length - 1].length; j++) { s += "-"; }

		return s + "|\n";
	}
}

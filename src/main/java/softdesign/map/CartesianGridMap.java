package main.java.softdesign.map;

import javax.vecmath.Point3d;

public class CartesianGridMap extends AbstractMap {

	private final int cartesianOffset;

	private Tile[][] grid;

	public CartesianGridMap(int size) {
		super(size);

		this.cartesianOffset = -(this.size / 2);

		int axisPoints = size + 1;
		this.grid = new Tile[axisPoints][axisPoints];

		for(int i = 0; i < grid.length; i++) {
			for(int j = 0; j < grid[i].length; j++) {
				grid[i][j] = Tile.EMPTY;
      }
    }
	}

	@Override
	public Tile getTile(Point3d coordinate) {
		return getTile((int) Math.round(coordinate.x), (int) Math.round(coordinate.z));
	}

	@Override
	public Tile getTile(int xCoordinate, int zCoordinate) {
		int x = xCoordinate - cartesianOffset;
		int z = zCoordinate - cartesianOffset;
		
    try {
      return grid[x][z];
    } catch(ArrayIndexOutOfBoundsException e) {
      return Tile.WALL;
    }
	}

	@Override
	public void markAsCovered(int xCoordinate, int zCoordinate) {
		setTile(xCoordinate, zCoordinate, Tile.COVERED);
		incrementNumberOfCoveredPoints();
	}

	@Override
	public void markAsWall(int xCoordinate, int zCoordinate) {
		setTile(xCoordinate, zCoordinate, Tile.WALL);
	}
	
	private void setTile(int xCoordinate, int zCoordinate, Tile tile) {
		int x = xCoordinate - cartesianOffset;
		int z = zCoordinate - cartesianOffset;

		grid[x][z] = tile;
	}

	// Also: definitely remove this before submitting, its ugly af.
	@Override
	public String toString() {
		String s = "\n+";

    for(int j = 0; j < grid[0].length; j++) {
      s += "-";
    }
    s += "+\n";

		for(int i = 0; i < grid.length; i++) {
      s += "|";
			for(int j = 0; j < grid[i].length; j++) {
				if(grid[i][j] == Tile.COVERED) {
					s += "X";
				} else if(grid[i][j] == Tile.WALL) {
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

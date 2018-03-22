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
	}

	@Override
  public Tile getTile(Point3d coord) {
    return getTile((int) Math.round(coord.x), (int) Math.round(coord.z));
  }

	@Override
	public Tile getTile(int xCoordinate, int zCoordinate) {
		int x = xCoordinate - cartesianOffset;
		int z = zCoordinate - cartesianOffset;
		
		return grid[x][z];
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
}

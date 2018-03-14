package main.java.softdesign.map;

public class CartisianGridMap extends AbstractMap {

	private final int cartesianOffset;

	private Tile[][] grid;

	public CartisianGridMap(int size) {
		super(size + 1); // TODO why are we incrementing size by 1?
		this.cartesianOffset = -(this.size / 2);
		this.grid = new Tile[size][size];
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

package main.java.softdesign.map;

public interface Map {

	enum Tile {
		COVERED, WALL
	}

	Tile getTile(int xCoordinate, int zCoordinate);

	int getNumberOfCoveredPoints();

	void markAsCovered(int xCoordinate, int zCoordinate);

	void markAsWall(int xCoordinate, int zCoordinate);
}

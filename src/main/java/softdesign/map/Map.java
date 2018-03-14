package main.java.softdesign.map;

public interface Map {

	enum Tile {
		EMPTY, COVERED, WALL
	}

	Tile getTile(int xCoordinate, int yCoordinate);

	int getNumberOfCoveredPoints();

	void markAsCovered(int xCoordinate, int yCoordinate);

	void markAsWall(int xCoordinate, int yCoordinate);
}

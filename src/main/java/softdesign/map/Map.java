package main.java.softdesign.map;

public interface Map {

	enum Tile {
		COVERED, WALL
	}

	Tile getTile(CartesianCoordinate coordinate);

	int getNumberOfCoveredPoints();

	void markAsCovered(CartesianCoordinate coordinate);

	void markAsWall(CartesianCoordinate coordinate);

	double getCoveredRatio();
}

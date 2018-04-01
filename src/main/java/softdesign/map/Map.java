package main.java.softdesign.map;

public interface Map {

	enum Tile {
		COVERED, WALL, EMPTY, ROBOT;

		public boolean isPassable() {
			return this == COVERED || this == EMPTY;
		}
	}

	void markAsRobot(CartesianCoordinate coordinate);

	void markAsWall(CartesianCoordinate coordinate);

	void markAsCovered(CartesianCoordinate coordinate);

	double getCoverage();

	int getNumberOfCoveredPoints();

	Tile getTile(CartesianCoordinate coordinate);

	int getSize();
}

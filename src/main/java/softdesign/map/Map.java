package main.java.softdesign.map;

public interface Map {

	enum Tile {
		COVERED, WALL, EMPTY, ROBOT;

		public boolean isDrivable() {
			return this == COVERED || this == EMPTY;
		}
	}

	Tile getTile(CartesianCoordinate coordinate);

	int getNumberOfCoveredPoints();

	void setTile(CartesianCoordinate coordinate, Tile tile);

	double getCoveredRatio();

	int getSize();
}

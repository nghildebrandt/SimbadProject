package main.java.softdesign.map;

import javax.vecmath.Point3d;

public interface Map {

	enum Tile {
		EMPTY, COVERED, WALL
	}

	Tile getTile(Point3d coord);

	Tile getTile(int xCoordinate, int zCoordinate);

	int getNumberOfCoveredPoints();

	void markAsCovered(int xCoordinate, int zCoordinate);

	void markAsWall(int xCoordinate, int zCoordinate);
}

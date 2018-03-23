package main.java.softdesign.map;

import javax.vecmath.Point3d;

public interface Map {

	enum Tile {
		COVERED, WALL
	}

	Tile getTile(Point3d coordinate);

	Tile getTile(int xCoordinate, int zCoordinate);

	int getNumberOfCoveredPoints();

	void markAsCovered(int xCoordinate, int zCoordinate);

	void markAsWall(int xCoordinate, int zCoordinate);
}

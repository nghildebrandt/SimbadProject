package main.java.softdesign.map;

abstract class AbstractMap implements Map {

	private final int size;
	private final int totalNumberOfPoints;
	private int numberOfCoveredPoints;

	AbstractMap(int size) {
		this.size = size;
		this.totalNumberOfPoints = size * size;
		this.numberOfCoveredPoints = 0;
	}

	void incrementNumberOfCoveredPoints() {
		numberOfCoveredPoints += 1;
	}

	@Override
	public int getSize() {
		return size;
	}

	int getTotalNumberOfPoints() {
		return totalNumberOfPoints;
	}

	int getNumberOfCoveredPoints() {
		return numberOfCoveredPoints;
	}
}

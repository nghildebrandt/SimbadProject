package main.java.softdesign.map;

abstract class AbstractMap implements Map {

	private int numberOfCoveredPoints;

	AbstractMap() {
		this.numberOfCoveredPoints = 0;
	}

	void incrementNumberOfCoveredPoints() {
		numberOfCoveredPoints += 1;
	}

	public int getNumberOfCoveredPoints() {
		return numberOfCoveredPoints;
	}
}

package main.java.softdesign;

import main.java.softdesign.image.ImageArrayListStore;
import main.java.softdesign.image.ImageRepository;

public class CentralBase {

	private static final double COVERAGE_REQUIREMENT = 0.75;

	private final ImageRepository imageRepository;

	private Map map = new Map();

	CentralBase() {
		this.imageRepository = new ImageArrayListStore();

		mapWalls();
		mapImaginaryWalls();
	}

	private void mapWalls() {
		for (int i = 0; i <= Environment.SIZE / 2; i++) {
			map.setPoint(i, Environment.SIZE / 2, Map.WALL);
			map.setPoint(-i, Environment.SIZE / 2, Map.WALL);
			map.setPoint(i, -Environment.SIZE / 2, Map.WALL);
			map.setPoint(-i, -Environment.SIZE / 2, Map.WALL);
			map.setPoint(Environment.SIZE / 2, i, Map.WALL);
			map.setPoint(Environment.SIZE / 2, -i, Map.WALL);
			map.setPoint(-Environment.SIZE / 2, i, Map.WALL);
			map.setPoint(-Environment.SIZE / 2, -i, Map.WALL);
		}
	}

	private void mapImaginaryWalls() {
		for (int i = 0; i <= Environment.SIZE / 2; i++) {
			map.setPoint(0, i, Map.WALL);
			map.setPoint(0, -i, Map.WALL);
			map.setPoint(i, 0, Map.WALL);
			map.setPoint(-i, 0, Map.WALL);
		}
	}

	public boolean isMissionComplete() {
		return map.getNumberOfCoveredPoints() / Environment.TOTAL_NUMBER_OF_POINTS > COVERAGE_REQUIREMENT;
	}

	public Map copyMap() {
		return map;
	}
}

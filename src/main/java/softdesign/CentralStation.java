package main.java.softdesign;

import main.java.softdesign.image.ImageArrayListStore;
import main.java.softdesign.image.ImageRepository;

public class CentralStation {

	private static final CentralStation INSTANCE = new CentralStation();
	private static final double COVERAGE_REQUIREMENT = 0.75;

	private final ImageRepository imageRepository;

	private Map map = new Map();

	private CentralStation() {
		this.imageRepository = new ImageArrayListStore();

		mapWalls();
		divideIntoSections();
	}

	public static CentralStation getInstance() {
		return INSTANCE;
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

	/**
	 * Divides the map into sections with each representing a sub-world for a robot to reside in.
	 *
	 * <p>The division is done by adding "fake" walls into the map.
	 */
	private void divideIntoSections() {
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

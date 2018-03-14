package main.java.softdesign;

import main.java.softdesign.image.ImageRepository;
import main.java.softdesign.image.ImageRepositoryFactory;
import simbad.gui.Simbad;

import javax.vecmath.Vector3d;

public class CentralStation {

	private static final CentralStation INSTANCE = new CentralStation();
	private static final double COVERAGE_REQUIREMENT = 0.75;

	private final ImageRepository imageRepository;

	private Environment environment;
	private Map map;

	private CentralStation() {
		this.imageRepository = ImageRepositoryFactory.get();
		this.map = new Map();

		mapWalls();
		divideIntoSections();
	}

	public static CentralStation getInstance() {
		return INSTANCE;
	}

	public boolean isMissionComplete() {
		return getMissionProgress() > COVERAGE_REQUIREMENT;
	}

	public double getMissionProgress() {
		return map.getNumberOfCoveredPoints() / (double) Environment.TOTAL_NUMBER_OF_POINTS;
	}

	public void startMission(Environment environment) {
		this.environment = environment;
		deployRobots();
		launch();
	}

	private void deployRobots() {
		if (Environment.SIZE <= Environment.LARGE) {
			environment.add(new Robot(new Vector3d(8, 0, 8), "small", map, Robot.SOUTH));
			environment.add(new Robot(new Vector3d(8, 0, -8), "small", map, Robot.SOUTH));
			environment.add(new Robot(new Vector3d(-8, 0, -8), "small", map, Robot.SOUTH));
			environment.add(new Robot(new Vector3d(-8, 0, 8), "small", map, Robot.SOUTH));
		}
	}

	private void launch() {
		new Simbad(environment, false);
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
}

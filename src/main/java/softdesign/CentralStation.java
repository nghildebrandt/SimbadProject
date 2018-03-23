package main.java.softdesign;

import main.java.softdesign.image.ImageRepository;
import main.java.softdesign.image.ImageRepositoryFactory;
import main.java.softdesign.map.CartesianGridMap;
import main.java.softdesign.map.Map;
import simbad.gui.Simbad;

import javax.vecmath.Vector3d;

public class CentralStation {

	private static final CentralStation INSTANCE = new CentralStation();
	private static final double COVERAGE_REQUIREMENT = 0.75;

	private final ImageRepository imageRepository;

	private Map map;

	private CentralStation() {
		this.imageRepository = ImageRepositoryFactory.get();
		this.map = new CartesianGridMap(Environment.SIZE);
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
		deployRobots(environment);
		launch(environment);
	}

	private void deployRobots(Environment environment) {
		environment.add(new Robot(new Vector3d(9, 0, 9), "small", map, Robot.Direction.SOUTH));
	}

	private void launch(Environment environment) {
		new Simbad(environment, false);
	}
}

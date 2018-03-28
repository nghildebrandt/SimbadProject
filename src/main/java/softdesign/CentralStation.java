package main.java.softdesign;

import main.java.softdesign.map.CartesianCoordinate;
import main.java.softdesign.map.CartesianGridMap;
import main.java.softdesign.map.Map;
import main.java.softdesign.image.ImageRepository;
import main.java.softdesign.image.ImageRepositoryFactory;

import simbad.gui.Simbad;

import java.awt.image.BufferedImage;
import javax.vecmath.Vector3d;

public class CentralStation {

	private static final CentralStation INSTANCE = new CentralStation();
	private static final double COVERAGE_REQUIREMENT = 0.75;

	private final ImageRepository imageRepository;
	private Map map;

	private CentralStation() {
		this.imageRepository = ImageRepositoryFactory.get();
	}

	public static CentralStation getInstance() {
		return INSTANCE;
	}

	public boolean isMissionComplete() {
		return getMissionProgress() > COVERAGE_REQUIREMENT;
	}

	public double getMissionProgress() {
		return map.getCoveredRatio();
	}

	public Map getMap() {
		return map;
	}

	public void saveImage(BufferedImage image) {
		imageRepository.save(image);
	}

	public void startMission(Environment environment) {
		setupMap(environment);
		deployRobots(environment);
		launch(environment);
	}

	private void setupMap(Environment environment) {
		map = new CartesianGridMap(environment.getSize());

		for(CartesianCoordinate obstacleCoordinates : environment.getObstacleCoordinates()) {
			map.setTile(obstacleCoordinates, Map.Tile.WALL);
		}
	}

	private void deployRobots(Environment environment) {
		int extremes = environment.getSize() / 2;

		environment.add(new Robot(new Vector3d(extremes, 0, extremes), "small", this));
		environment.add(new Robot(new Vector3d(-extremes, 0, extremes), "small", this));
		environment.add(new Robot(new Vector3d(-extremes, 0, -extremes), "small", this));
		environment.add(new Robot(new Vector3d(extremes, 0, -extremes), "small", this));
	}

	private void launch(Environment environment) {
		new Simbad(environment, false);
	}
}

package main.java.softdesign;

import main.java.softdesign.image.ImageRepository;
import main.java.softdesign.image.ImageRepositoryFactory;
import main.java.softdesign.map.CartesianCoordinate;
import main.java.softdesign.map.CartesianGridMap;
import main.java.softdesign.map.Map;
import main.java.softdesign.map.Map.Tile;
import simbad.gui.Simbad;

import javax.vecmath.Vector3d;
import java.awt.image.BufferedImage;

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

	public void sendImage(BufferedImage image) {
		imageRepository.save(image);
	}

	public Tile requestTile(CartesianCoordinate coordinate) {
		return map.getTile(coordinate);
	}

	public void sendTile(CartesianCoordinate coordinate, Tile tile) {
		map.setTile(coordinate, tile);
	}

	public int requestMapSize() {
		return map.getSize();
	}

	public void startMission(Environment environment) {
		drawMap(environment);
		deployRobots(environment);
		launchMission(environment);
	}

	private void drawMap(Environment environment) {
		map = new CartesianGridMap(environment.getSize());

		for (CartesianCoordinate obstacleCoordinate : environment.getObstacleCoordinates()) {
			map.setTile(obstacleCoordinate, Map.Tile.WALL);
		}
	}

	private void deployRobots(Environment environment) {
		int extremes = environment.getSize() / 2;

		environment.add(new Robot(new Vector3d(extremes, 0, extremes), "small", this));
		environment.add(new Robot(new Vector3d(-extremes, 0, extremes), "small", this));
		if (!environment.isSmall()) {
			environment.add(new Robot(new Vector3d(-extremes, 0, -extremes), "small", this));
			environment.add(new Robot(new Vector3d(extremes, 0, -extremes), "small", this));
		}
	}

	private void launchMission(Environment environment) {
		new Simbad(environment, false);
	}
}

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
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class CentralStation {

	private static final CentralStation INSTANCE = new CentralStation();
	private static final double COVERAGE_REQUIREMENT = 0.75;

	private final ImageRepository imageRepository;
	private final List<Robot> robots;

	private Map map;

	private Instant missionStartTime;
	private Instant missionEndTime;

	private CentralStation() {
		this.imageRepository = ImageRepositoryFactory.get();
		this.robots = new ArrayList<>();
	}

	public static CentralStation getInstance() {
		return INSTANCE;
	}

	public boolean isMissionComplete() {
		return getMissionProgress() > COVERAGE_REQUIREMENT;
	}

	public double getMissionProgress() {
		return map.getCoverage();
	}

	public void sendImage(BufferedImage image) {
		imageRepository.save(image);
	}

	public Tile requestTile(CartesianCoordinate coordinate) {
		return map.getTile(coordinate);
	}

	public void sendCurrentPosition(CartesianCoordinate coordinate) {
		map.markAsRobot(coordinate);
	}

	public void sendCoveredArea(CartesianCoordinate coordinate) {
		map.markAsCovered(coordinate);
	}

	public int requestMapSize() {
		return map.getSize();
	}

	public void startMission(Environment environment) {
		drawMap(environment);
		deployRobots(environment);

		launchMission(environment);
		missionStartTime = Instant.now();
	}

	private void drawMap(Environment environment) {
		map = new CartesianGridMap(environment.getSize());

		for (CartesianCoordinate obstacleCoordinate : environment.getObstacleCoordinates()) {
			map.markAsWall(obstacleCoordinate);
		}
	}

	private void deployRobots(Environment environment) {
		int environmentSize = environment.getSize();
		boolean smallEnvironment = environment.isSmall();
		setupRobots(environmentSize, smallEnvironment);

		robots.forEach(environment::add);
	}

	private void setupRobots(int environmentSize, boolean smallEnvironment) {
		int extremes = environmentSize / 2;

		robots.add(new Robot(new Vector3d(extremes, 0, extremes), "R1", this));
		robots.add(new Robot(new Vector3d(-extremes, 0, extremes), "R2", this));
		if (!smallEnvironment) {
			robots.add(new Robot(new Vector3d(-extremes, 0, -extremes), "R3", this));
			robots.add(new Robot(new Vector3d(extremes, 0, -extremes), "R4", this));
		}
	}

	private void launchMission(Environment environment) {
		new Simbad(environment, false);
	}

	public void endMission() {
		robots.forEach(Robot::moveToStartPosition);

		missionEndTime = Instant.now();
		Duration missionDuration = Duration.between(missionStartTime, missionEndTime);
		System.out.printf("Mission completed in %s!%n", missionDuration);
	}
}

package main.java.softdesign;

import main.java.softdesign.image.ImageRepository;
import main.java.softdesign.image.ImageRepositoryFactory;
import main.java.softdesign.map.CartesianGridMap;
import main.java.softdesign.map.Map;
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
		map = environment.getMap();
		deployRobots(environment);
		launch(environment);
	}

	private void deployRobots(Environment environment) {
		int extremes = map.getSize() / 2;

		environment.add(new Robot(new Vector3d(extremes, 0, extremes), "small", this));
		environment.add(new Robot(new Vector3d(-extremes, 0, extremes), "small", this));
		environment.add(new Robot(new Vector3d(-extremes, 0, -extremes), "small", this));
		environment.add(new Robot(new Vector3d(extremes, 0, -extremes), "small", this));
	}

	private void launch(Environment environment) {
		new Simbad(environment, false);
	}
}

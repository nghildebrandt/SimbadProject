package main.java.softdesign;

import simbad.sim.Box;
import simbad.sim.EnvironmentDescription;
import simbad.sim.Wall;

import javax.vecmath.Color3f;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector3f;
import java.awt.*;
import java.util.concurrent.ThreadLocalRandom;

public class Environment extends EnvironmentDescription {

	private static final Environment INSTANCE = new Environment();

	public static final int SIZE = 201;
	public static final int TOTAL_NUMBER_OF_POINTS = SIZE * SIZE;

	private Environment() {
		light1IsOn = true;
		light2IsOn = true;

		showAxis(true);
		setWorldSize(SIZE);
		initWalls();
	}

	public static Environment getInstance() {
		return INSTANCE;
	}

	private void initWalls() {
		addWall(new Vector3d(-SIZE / 2.0, 0, 0), new Color3f(Color.BLUE), true);
		addWall(new Vector3d(SIZE / 2.0, 0, 0), new Color3f(Color.GREEN), true);
		addWall(new Vector3d(0, 0, SIZE / 2.0), new Color3f(Color.RED), false);
		addWall(new Vector3d(0, 0, -SIZE / 2.0), new Color3f(Color.YELLOW), false);
	}

	private void addWall(Vector3d position, Color3f color, boolean rotate) {
		Wall wall = new Wall(position, SIZE, 2, this);
		wall.setColor(color);
		if(rotate) {
			wall.rotate90(1);
		}
		add(wall);
	}

	public void initObstacles() {
		for(int i = 0; i < 2; i++) {
			Box box = new Box(randomVector(), new Vector3f(1, 1, 1), this);
			box.setColor(new Color3f(Color.ORANGE));
			add(box);
		}
	}

	private Vector3d randomVector() {
		int randomX = ThreadLocalRandom.current().nextInt(-SIZE / 2, SIZE / 2 + 1);
		int randomY = ThreadLocalRandom.current().nextInt(-SIZE / 2, SIZE / 2 + 1);
		return new Vector3d(randomY, 0, randomX);
	}
}

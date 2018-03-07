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

	public static final int WORLD_SIZE = 10;
	public static final int SMALL = 10;
	public static final int LARGE = 30;

	Environment() {
		this.light1IsOn = true;
		this.light2IsOn = true;

		showAxis(true);
		setWorldSize(WORLD_SIZE);
		initWalls();
	}

	/**
	 *  Initializes the walls.
	 *
	 *  The walls are now placed at the limit of the world size and the walls will change in response to the size of
	 *  the world
	 */
	private void initWalls() throws Exception {
		Wall w1 = new Wall(new Vector3d(-WORLD_SIZE / 2, 0, 0), WORLD_SIZE, 2, this);
		w1.setColor(new Color3f(Color.BLUE));
		w1.rotate90(1);
		add(w1);

		Wall w2 = new Wall(new Vector3d(WORLD_SIZE / 2, 0, 0), WORLD_SIZE, 2, this);
		w2.setColor(new Color3f(Color.GREEN));
		w2.rotate90(1);
		add(w2);

		Wall w3 = new Wall(new Vector3d(0, 0, WORLD_SIZE / 2), WORLD_SIZE, 2, this);
		w3.setColor(new Color3f(Color.RED));
		add(w3);

		Wall w4 = new Wall(new Vector3d(0, 0, -WORLD_SIZE / 2), WORLD_SIZE, 2, this);
		w4.setColor(new Color3f(Color.YELLOW));
		add(w4);
	}

	public void initObstacles() {
		Box box1 = new Box(randomVector(), new Vector3f(1, 1, 1), this);
		box1.setColor(new Color3f(Color.ORANGE));
		add(box1);

		Box box2 = new Box(randomVector(), new Vector3f(1, 1, 1), this);
		box2.setColor(new Color3f(Color.ORANGE));
		add(box2);
	}

	private Vector3d randomVector() {
		int randomX = ThreadLocalRandom.current().nextInt(-WORLD_SIZE / 2, WORLD_SIZE / 2 + 1);
		int randomY = ThreadLocalRandom.current().nextInt(-WORLD_SIZE / 2, WORLD_SIZE / 2 + 1);
		return new Vector3d(randomY, 0, randomX);
	}
}

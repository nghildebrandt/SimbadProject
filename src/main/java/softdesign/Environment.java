package main.java.softdesign;

import main.java.softdesign.map.Map;
import main.java.softdesign.map.CartesianGridMap;
import main.java.softdesign.map.CartesianCoordinate;

import simbad.sim.Box;
import simbad.sim.EnvironmentDescription;
import simbad.sim.Wall;

import javax.vecmath.Color3f;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector3f;
import javax.vecmath.Point3d;
import java.awt.*;
import java.util.concurrent.ThreadLocalRandom;

public class Environment extends EnvironmentDescription {

	private static final int SMALL = 5;
	private static final int MEDIUM = 15;
	private static final int LARGE = 25;

	private static final float BOX_SIZE = 1f;

	private static final Environment INSTANCE = new Environment(MEDIUM);

	private int size;
	private Map map;

	private Environment(int size) {
		this.size = size;

		map = new CartesianGridMap(size);

		light1IsOn = true;
		light2IsOn = true;

		showAxis(true);

		setWorldSize(size);
		initWalls();
		initObstacles();
	}

	public Map getMap() {
		return map;
	}

	public static Environment getInstance() {
		return INSTANCE;
	}

	private void initWalls() {
		addWall(new Vector3d(-size / 2.0, 0, 0), new Color3f(Color.BLUE), true);
		addWall(new Vector3d(size / 2.0, 0, 0), new Color3f(Color.GREEN), true);
		addWall(new Vector3d(0, 0, size / 2.0), new Color3f(Color.RED), false);
		addWall(new Vector3d(0, 0, -size / 2.0), new Color3f(Color.YELLOW), false);
	}

	private void addWall(Vector3d position, Color3f color, boolean rotate) {
		Wall wall = new Wall(position, size, 2, this);
		wall.setColor(color);
		if(rotate) {
			wall.rotate90(1);
		}
		add(wall);
	}

	public void initObstacles() {
		for(int i = 0; i < 20; i++) {
			Vector3d location = randomVector();

			Box box = new Box(location, new Vector3f(BOX_SIZE/2, BOX_SIZE/2, BOX_SIZE/2), this);
			map.setTile(new CartesianCoordinate(location, getSize()), Map.Tile.WALL);

			box.setColor(new Color3f(Color.ORANGE));
			add(box);
		}
	}

	public int getSize() {
		return size;
	}

	private Vector3d randomVector() {
		int randomX = ThreadLocalRandom.current().nextInt(-size / 2, size / 2 + 1);
		int randomY = ThreadLocalRandom.current().nextInt(-size / 2, size / 2 + 1);
		return new Vector3d(randomY - BOX_SIZE/2, 0, randomX - BOX_SIZE/2);
	}
}

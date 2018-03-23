package main.java.softdesign;

import main.java.softdesign.map.Map;

import simbad.sim.Agent;
import simbad.sim.CameraSensor;
import simbad.sim.RobotFactory;

import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;
import java.awt.image.BufferedImage;

public class Robot extends Agent {

	public enum Direction {
		SOUTH, WEST, NORTH, EAST;

		public Direction rightBy() {
			switch(this) {
				case SOUTH: return WEST;
				case WEST: return NORTH;
				case NORTH: return EAST;
				case EAST: return SOUTH;
				default: throw new IllegalArgumentException("Unrecognized direction");
			}
		}

		public Direction rightBy(int turns) {
			if(turns == 0) { return this; }

			return rightBy().rightBy(turns - 1);
		}
	}

	private Direction currentDirection;
	private Map map;
	private Point3d coordinate;

	private CameraSensor backCamera;
	private CameraSensor leftCamera;
	private CameraSensor rightCamera;

	Robot(Vector3d position, String name, Map map, Direction startingDirection) {
		super(position, name);

		this.map = map;
		this.currentDirection = startingDirection;
		this.coordinate = new Point3d();

		RobotFactory.addBumperBeltSensor(this, 12);
		RobotFactory.addSonarBeltSensor(this, 4);

		this.leftCamera = RobotFactory.addCameraSensor(this);
		this.backCamera = RobotFactory.addCameraSensor(this);
		this.rightCamera = RobotFactory.addCameraSensor(this);

		this.leftCamera.rotateY(Math.PI / 2);
		this.backCamera.rotateY(Math.PI);
		this.rightCamera.rotateY(-Math.PI / 2);
	}

	@Override
	public void performBehavior() {
		getCoords(coordinate);

		ensureNeighbouringImagesTaken();
		usedForLogginNothingElse();

		if(map.getTile(stepsAhead(currentDirection, 1)) == Map.Tile.WALL) {
			setTranslationalVelocity(0);
			rotateY(-(Math.PI / 2));
			currentDirection = currentDirection.rightBy(1);
		} else {
			setTranslationalVelocity(1);
		}
	}

	public void usedForLogginNothingElse() {
		System.out.println(map.toString());
		System.out.printf(
			"Robot at %d:%d, Looking at %s",
			(int) Math.round(coordinate.x),
			(int) Math.round(coordinate.z),
			currentDirection.name()
		);
	}

	//takes images from the back, left, and right side if not take yet
	private void ensureNeighbouringImagesTaken() {
		takeImageIfNeeded(currentDirection.rightBy(1), rightCamera);
		takeImageIfNeeded(currentDirection.rightBy(2), backCamera);
		takeImageIfNeeded(currentDirection.rightBy(3), leftCamera);
	}

	private void takeImageIfNeeded(Direction direction, CameraSensor camera) {
		Point3d coordinate = stepsAhead(direction, 1);

		if (map.getTile(coordinate) != Map.Tile.EMPTY) { return; }

		camera.copyVisionImage(camera.createCompatibleImage());
		map.markAsCovered((int) Math.round(coordinate.x), (int) Math.round(coordinate.z));
	}

	private Point3d stepsAhead(Direction direction, int steps) {
		switch (direction) {
			case EAST: return new Point3d(coordinate.x, coordinate.y, coordinate.z + steps);
			case WEST: return new Point3d(coordinate.x, coordinate.y, coordinate.z - steps);
			case SOUTH: return new Point3d(coordinate.x + steps, coordinate.y, coordinate.z);
			case NORTH: return new Point3d(coordinate.x - steps, coordinate.y, coordinate.z);
			default: throw new IllegalArgumentException("Unrecognized direction");
		}
	}
}

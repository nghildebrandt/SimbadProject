package main.java.softdesign;

import main.java.softdesign.map.Map;
import main.java.softdesign.map.CartesianCoordinate;

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
	private CartesianCoordinate coordinate;

	private CameraSensor backCamera;
	private CameraSensor leftCamera;
	private CameraSensor rightCamera;

	Robot(Vector3d position, String name, Map map) {
		super(position, name);

		this.map = map;
		this.currentDirection = Direction.EAST;

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
		updateCoordinate();

		ensureNeighbouringImagesTaken();
		usedForLogginNothingElse();

		if(map.getTile(tileAhead(currentDirection)) == Map.Tile.WALL) {
			turnRight();
			return;
		}

		if(Math.random() > 0.01) {
			move();
		} else {
			turnRight();
		}
	}

	private void turnRight() {
		setTranslationalVelocity(0);
		rotateY(-(Math.PI / 2));
		currentDirection = currentDirection.rightBy(1);
	}

	private void move() {
		setTranslationalVelocity(1);
	}

	private void updateCoordinate() {
		Point3d point = new Point3d();
		getCoords(point);
		this.coordinate = new CartesianCoordinate(point);
	}

	private void usedForLogginNothingElse() {
		// System.out.println(map.toString());
	}

	//takes images from the back, left, and right side if not take yet
	private void ensureNeighbouringImagesTaken() {
		takeImageIfNeeded(currentDirection.rightBy(1), rightCamera);
		takeImageIfNeeded(currentDirection.rightBy(2), backCamera);
		takeImageIfNeeded(currentDirection.rightBy(3), leftCamera);
	}

	private void takeImageIfNeeded(Direction direction, CameraSensor camera) {
		CartesianCoordinate coordinate = tileAhead(direction);

		if (map.getTile(coordinate) != Map.Tile.EMPTY) { return; }

		camera.copyVisionImage(camera.createCompatibleImage());
		map.markAsCovered(coordinate);
	}

	private CartesianCoordinate tileAhead(Direction direction) {
		switch (direction) {
			case EAST: return new CartesianCoordinate(coordinate.x + 1, coordinate.z);
			case WEST: return new CartesianCoordinate(coordinate.x - 1, coordinate.z);
			case NORTH: return new CartesianCoordinate(coordinate.x, coordinate.z - 1);
			case SOUTH: return new CartesianCoordinate(coordinate.x, coordinate.z + 1);
			default: throw new IllegalArgumentException("Unrecognized direction");
		}
	}
}

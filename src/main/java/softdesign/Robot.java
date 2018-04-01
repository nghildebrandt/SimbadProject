package main.java.softdesign;

import main.java.softdesign.map.CartesianCoordinate;
import main.java.softdesign.map.Map;
import simbad.sim.Agent;
import simbad.sim.CameraSensor;
import simbad.sim.RobotFactory;

import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;
import java.awt.image.BufferedImage;

public class Robot extends Agent {

	private static double DIRECTION_CHANGE_PROBABILITY = 0.01;
	private static double BREAKDOWN_PROBABILITY = 0.001;

	public enum Direction {

		SOUTH, WEST, NORTH, EAST;

		public Direction rightBy() {
			switch (this) {
				case SOUTH:
					return WEST;
				case WEST:
					return NORTH;
				case NORTH:
					return EAST;
				case EAST:
					return SOUTH;
				default:
					throw new IllegalArgumentException("Unrecognized direction");
			}
		}

		public Direction rightBy(int turns) {
			if (turns == 0) {
				return this;
			}

			return rightBy().rightBy(turns - 1);
		}
	}

	private boolean broken = false;

	private Direction currentDirection;
	private CentralStation centralStation;
	private CartesianCoordinate coordinate;

	private CameraSensor backCamera;
	private CameraSensor leftCamera;
	private CameraSensor rightCamera;

	Robot(Vector3d position, String name, CentralStation centralStation) {
		super(position, name);

		this.centralStation = centralStation;
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
		if (broken) {
			return;
		}

		updateCoordinate();
		ensureNeighbouringImagesTaken();

		centralStation.sendCurrentPosition(coordinate);
		centralStation.sendCoveredArea(tileAhead(currentDirection, -1));

		if (!coordinate.isOnGrid()) {
			return;
		}

		if (Math.random() < BREAKDOWN_PROBABILITY) {
			broken = true;
		} else if (!centralStation.requestTile(tileAhead(currentDirection, 1)).isPassable()) {
			stop();
			turnRight();
		} else if (Math.random() < DIRECTION_CHANGE_PROBABILITY) {
			stop();
			turnRight();
		} else {
			moveInCurrentDirection();
		}
	}

	private void updateCoordinate() {
		Point3d point = new Point3d();
		getCoords(point);
		coordinate = new CartesianCoordinate(point, centralStation.requestMapSize());
	}

	//takes images from the back, left, and right side if not take yet
	private void ensureNeighbouringImagesTaken() {
		takeImageIfNeeded(currentDirection.rightBy(1), rightCamera);
		takeImageIfNeeded(currentDirection.rightBy(2), backCamera);
		takeImageIfNeeded(currentDirection.rightBy(3), leftCamera);
	}

	private void takeImageIfNeeded(Direction direction, CameraSensor camera) {
		CartesianCoordinate coordinate = tileAhead(direction, 1);

		if (centralStation.requestTile(coordinate) == Map.Tile.EMPTY) {
			BufferedImage image = camera.createCompatibleImage();
			centralStation.sendImage(image);
			camera.copyVisionImage(image);

			centralStation.sendCoveredArea(coordinate);
		}
	}

	private CartesianCoordinate tileAhead(Direction direction, int steps) {
		switch (direction) {
			case EAST:
				return new CartesianCoordinate(coordinate.getX() + steps, coordinate.getZ());
			case WEST:
				return new CartesianCoordinate(coordinate.getX() - steps, coordinate.getZ());
			case NORTH:
				return new CartesianCoordinate(coordinate.getX(), coordinate.getZ() - steps);
			case SOUTH:
				return new CartesianCoordinate(coordinate.getX(), coordinate.getZ() + steps);
			default:
				throw new IllegalArgumentException("Unrecognized direction");
		}
	}

	private void stop() {
		setTranslationalVelocity(0);
	}

	private void turnRight() {
		rotateY(-(Math.PI / 2));
		currentDirection = currentDirection.rightBy(1);
	}

	private void moveInCurrentDirection() {
		setTranslationalVelocity(1);
	}
}

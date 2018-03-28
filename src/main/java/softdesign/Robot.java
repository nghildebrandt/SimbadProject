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
		updateCoordinate();

		ensureNeighbouringImagesTaken();

		getMap().setTile(coordinate, Map.Tile.ROBOT);
		getMap().setTile(tileAhead(currentDirection, -1), Map.Tile.COVERED);

		if(!coordinate.isOnGrid()) {
			return;
		} else if(!getMap().getTile(tileAhead(currentDirection, 1)).isPassable()) {
			turnRight();
		} else if (Math.random() > 0.01) {
			setTranslationalVelocity(1);
		} else {
			turnRight();
		}
	}

	private void turnRight() {
		setTranslationalVelocity(0);
		rotateY(-(Math.PI / 2));
		currentDirection = currentDirection.rightBy(1);
	}

	private void updateCoordinate() {
		Point3d point = new Point3d();
		getCoords(point);
		coordinate = new CartesianCoordinate(point, getMap().getSize());
	}

	//takes images from the back, left, and right side if not take yet
	private void ensureNeighbouringImagesTaken() {
		takeImageIfNeeded(currentDirection.rightBy(1), rightCamera);
		takeImageIfNeeded(currentDirection.rightBy(2), backCamera);
		takeImageIfNeeded(currentDirection.rightBy(3), leftCamera);
	}

	private Map getMap() {
		return centralStation.getMap();
	}

	private void takeImageIfNeeded(Direction direction, CameraSensor camera) {
		CartesianCoordinate coordinate = tileAhead(direction, 1);

		if(getMap().getTile(coordinate) == Map.Tile.EMPTY) {
			BufferedImage image = camera.createCompatibleImage();
			centralStation.saveImage(image);
			camera.copyVisionImage(image);

			getMap().setTile(coordinate, Map.Tile.COVERED);
		}
	}

	private CartesianCoordinate tileAhead(Direction direction, int steps) {
		switch (direction) {
			case EAST:
				return new CartesianCoordinate(coordinate.x + steps, coordinate.z);
			case WEST:
				return new CartesianCoordinate(coordinate.x - steps, coordinate.z);
			case NORTH:
				return new CartesianCoordinate(coordinate.x, coordinate.z - steps);
			case SOUTH:
				return new CartesianCoordinate(coordinate.x, coordinate.z + steps);
			default:
				throw new IllegalArgumentException("Unrecognized direction");
		}
	}
}

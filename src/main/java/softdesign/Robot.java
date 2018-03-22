package main.java.softdesign;

import main.java.softdesign.map.Map;
import simbad.sim.Agent;
import simbad.sim.CameraSensor;
import simbad.sim.RobotFactory;

import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;
import java.awt.image.BufferedImage;

public class Robot extends Agent {

	private static final String UNRECOGNIZED_DIRECTION_EXCEPTION = "Unrecognized direction %s";

	enum Direction {
		NORTH, EAST, SOUTH, WEST
	}

	private String currentMode;
	private Direction currentDirection;
	private Map map;
	private Point3d coordinate;

	private CameraSensor camera;
	private CameraSensor camera2;
	private CameraSensor camera3;
	private BufferedImage cameraImage;

	Robot(Vector3d position, String name, Map map, Direction startingDirection) {
		super(position, name);
		this.map = map;
		this.currentDirection = startingDirection;
		this.coordinate = new Point3d();

		RobotFactory.addBumperBeltSensor(this, 12);
		RobotFactory.addSonarBeltSensor(this, 4);
		initCameras();
	}

	private void initCameras() {
		camera = RobotFactory.addCameraSensor(this);
		camera.rotateY(Math.PI / 2);

		camera2 = RobotFactory.addCameraSensor(this);
		camera2.rotateY(-(Math.PI / 2));

		camera3 = RobotFactory.addCameraSensor(this);
		camera3.rotateY(Math.PI);

		cameraImage = camera.createCompatibleImage();
	}

	@Override
	public void performBehavior() {
		getCoords(coordinate);

		// perform the following actions every 5 virtual seconds
		if (getCounter() % 5 == 0) {
			takeImages();

			if (isNearWall() ^ isNearCovered()) {
				currentMode = "avoidObstacle";
			} else {
				currentMode = "goAround";
			}

			if ("goAround".equals(currentMode)) {
				setTranslationalVelocity(0.5);
			} else {
				rotateY(-(Math.PI / 2));
				setDirection();
			}
		}

		setTranslationalVelocity(0.5);
	}

	//takes images from the back, left, and right side in relation to the robot
	private void takeImages() {
		switch (currentDirection) {
			case SOUTH:
				checkDirection(camera, camera2, camera3);
				break;
			case WEST:
				checkDirection(camera3, camera2, camera);
				break;
			case NORTH:
				checkDirection(camera2, camera, camera3);
				break;
		}
	}

	//the direction at which the camera points to changes as the robot moves, this method makes such that depending on the current direction of the robot that each camera ttached to the robot will point to the right global direction - EAST, WEST, NORTH, SOUTH
	private void checkDirection(CameraSensor camera, CameraSensor camera2, CameraSensor camera3) {
		switch (currentDirection) {
			case NORTH:
				checkCameraInDirections(Direction.WEST, Direction.EAST, Direction.SOUTH);
				break;
			case SOUTH:
				checkCameraInDirections(Direction.EAST, Direction.WEST, Direction.NORTH);
				break;
			case WEST:
				checkCameraInDirections(Direction.SOUTH, Direction.NORTH, Direction.EAST);
				break;
			case EAST:
				checkCameraInDirections(Direction.NORTH, Direction.SOUTH, Direction.WEST);
				break;
		}
	}

	//checks if the points left, right, and back in relation to the robot has been taken a picture of, if an image has not been taken, then an image will be taken
	private void checkCameraInDirections(Direction left, Direction right, Direction back) {
		if (isUnvisited(hasPointVisited(left))) {
			coverAndTrack(camera, hasPointVisited(left));
		}
		if (isUnvisited(hasPointVisited(right))) {
			coverAndTrack(camera2, hasPointVisited(right));
		}
		if (isUnvisited(hasPointVisited(back))) {
			coverAndTrack(camera3, hasPointVisited(back));
		}
	}

	// this method returns a new coordinate
	private Point3d hasPointVisited(Direction direction) {
		Point3d result;

		switch (direction) {
			case EAST:
				result = new Point3d(coordinate.x, coordinate.y, coordinate.z - 1);
				break;
			case WEST:
				result = new Point3d(coordinate.x, coordinate.y, coordinate.z + 1);
				break;
			case SOUTH:
				result = new Point3d(coordinate.x + 1, coordinate.y, coordinate.z);
				break;
			case NORTH:
				result = new Point3d(coordinate.x - 1, coordinate.y, coordinate.z);
				break;
			default:
				throw new IllegalArgumentException(String.format(UNRECOGNIZED_DIRECTION_EXCEPTION, direction));
		}

		return result;
	}

	//takes picture of point and marks the point as visited
	private void coverAndTrack(CameraSensor camera, Point3d coord) {
		camera.copyVisionImage(cameraImage);
		isVisited(coord);
	}

	// returns if the coordinate two points in front of the currentDirection has been visited
	private boolean isNearCovered() {
		boolean result;

		switch (currentDirection) {
			case SOUTH:
				result = getValueDouble(coordinate.x + 2, coordinate.z) == Map.Tile.COVERED;
				break;
			case NORTH:
				result = getValueDouble(coordinate.x - 2, coordinate.z) == Map.Tile.COVERED;
				break;
			case WEST:
				result = getValueDouble(coordinate.x, coordinate.z + 2) == Map.Tile.COVERED;
				break;
			case EAST:
				result = getValueDouble(coordinate.x, coordinate.z - 2) == Map.Tile.COVERED;
				break;
			default:
				throw new IllegalArgumentException(String.format(UNRECOGNIZED_DIRECTION_EXCEPTION, currentDirection));
		}

		return result;
	}

	private boolean isNearWall() {
		boolean result;

		switch (currentDirection) {
			case SOUTH:
				result = getValueCoord(toPoint3d(Direction.SOUTH)) == Map.Tile.WALL;
				break;
			case NORTH:
				result = getValueCoord(toPoint3d(Direction.NORTH)) == Map.Tile.WALL;
				break;
			case WEST:
				result = getValueCoord(toPoint3d(Direction.WEST)) == Map.Tile.WALL;
				break;
			case EAST:
				result = getValueCoord(toPoint3d(Direction.EAST)) == Map.Tile.WALL;
				break;
			default:
				throw new IllegalArgumentException(String.format(UNRECOGNIZED_DIRECTION_EXCEPTION, currentDirection));
		}

		return result;
	}

	private Map.Tile getValueCoord(Point3d coord) {
		return map.getTile((int) Math.round(coord.x), (int) Math.round(coord.z));
	}

	// returns new coordinate which the coordinate +/- 2 point ahead/behind the current direction
	private Point3d toPoint3d(Direction direction) {
		Point3d result;

		switch (direction) {
			case EAST:
				result = new Point3d(coordinate.x, coordinate.y, coordinate.z - 2);
				break;
			case WEST:
				result = new Point3d(coordinate.x, coordinate.y, coordinate.z + 2);
				break;
			case SOUTH:
				result = new Point3d(coordinate.x + 2, coordinate.y, coordinate.z);
				break;
			case NORTH:
				result = new Point3d(coordinate.x - 2, coordinate.y, coordinate.z);
				break;
			default:
				throw new IllegalArgumentException(String.format(UNRECOGNIZED_DIRECTION_EXCEPTION, direction));
		}

		return result;
	}

	private Map.Tile getValueDouble(double x, double z) {
		return map.getTile((int) Math.round(x), (int) Math.round(z));
	}

	private void setDirection() {
		switch (currentDirection) {
			case SOUTH:
				currentDirection = Direction.WEST;
				break;
			case WEST:
				currentDirection = Direction.NORTH;
				break;
			case NORTH:
				currentDirection = Direction.EAST;
				break;
			case EAST:
				currentDirection = Direction.SOUTH;
				break;
			default:
				throw new IllegalArgumentException(String.format(UNRECOGNIZED_DIRECTION_EXCEPTION, currentDirection));
		}
	}

	private boolean isUnvisited(Point3d coord) {
		return getValueCoord(coord) == Map.Tile.EMPTY;
	}

	private void isVisited(Point3d coord) {
		map.markAsCovered((int) Math.round(coord.x), (int) Math.round(coord.z));
	}
}

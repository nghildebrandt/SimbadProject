package main.java.softdesign;

import main.java.softdesign.map.Map;
import simbad.sim.Agent;
import simbad.sim.CameraSensor;
import simbad.sim.RobotFactory;

import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;
import java.awt.image.BufferedImage;

public class Robot extends Agent {

	public static final int SOUTH = 1;
	public static final int WEST = 2;
	public static final int NORTH = 3;
	public static final int EAST = 4;

	private String currentMode;
	private int currentDirection;
	private Map map;
	private Point3d coordinate;

	private CameraSensor camera;
	private CameraSensor camera2;
	private CameraSensor camera3;
	private BufferedImage cameraImage;

	Robot(Vector3d position, String name, Map map, int currentDirection) {
		super(position, name);
		this.map = map;
		this.currentDirection = currentDirection;
		this.coordinate = new Point3d();
		int directionCamera;
		int directionCamera2;
		int directionCamera3;

		RobotFactory.addBumperBeltSensor(this, 12);
		RobotFactory.addSonarBeltSensor(this, 4);
		initCameras();
	}

	private void initCameras() {
		camera = RobotFactory.addCameraSensor(this);
		camera2 = RobotFactory.addCameraSensor(this);
		camera3 = RobotFactory.addCameraSensor(this);
		cameraImage = camera.createCompatibleImage();
		cameraImage = camera2.createCompatibleImage();
		cameraImage = camera3.createCompatibleImage();
		camera.rotateY(Math.PI / 2);
		camera2.rotateY(-(Math.PI / 2));
		camera3.rotateY(Math.PI);
	}

	@Override
	public void performBehavior() {
		this.getCoords(coordinate);

		// perform the following actions every 5 virtual seconds
		if (this.getCounter() % 5 == 0) {
			takeImages();
			if (this.isNearWall() ^ this.isNearCovered()) {
				this.currentMode = "avoidObstacle";
			} else {
				this.currentMode = "goAround";
			}
			if (this.currentMode == "goAround") {
				this.setTranslationalVelocity(0.5);
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
				checkCameraInDirections(WEST, EAST, SOUTH);
				break;
			case SOUTH:
				checkCameraInDirections(EAST, WEST, NORTH);
				break;
			case WEST:
				checkCameraInDirections(SOUTH, NORTH, EAST);
				break;
			case EAST:
				checkCameraInDirections(NORTH, SOUTH, WEST);
				break;
		}
	}

	//checks if the points left, right, and back in relation to the robot has been taken a picture of, if an image has not been taken, then an image will be taken
	private void checkCameraInDirections(int left, int right, int back) {
		if (isUnvisited(hasPointVisited(left))) {
			coverAndTrack(this.camera, hasPointVisited(left));
		}
		if (isUnvisited(hasPointVisited(right))) {
			coverAndTrack(this.camera2, hasPointVisited(right));
		}
		if (isUnvisited(hasPointVisited(back))) {
			coverAndTrack(this.camera3, hasPointVisited(back));
		}
	}

	// this method returns a new coordinate
	private Point3d hasPointVisited(int direction) {
		switch (direction) {
			case EAST:
				return new Point3d(coordinate.x, coordinate.y, coordinate.z - 1);
			case WEST:
				return new Point3d(coordinate.x, coordinate.y, coordinate.z + 1);
			case SOUTH:
				return new Point3d(coordinate.x + 1, coordinate.y, coordinate.z);
			case NORTH:
				return new Point3d(coordinate.x - 1, coordinate.y, coordinate.z);
		}
		return null;
	}

	//takes picture of point and marks the point as visited
	private void coverAndTrack(CameraSensor camera, Point3d coord) {
		camera.copyVisionImage(cameraImage);
		isVisited(coord);
	}

	// returns if the coordinate two points in front of the currentDirection has been visited
	private boolean isNearCovered() {
		switch (currentDirection) {
			case SOUTH:
				return getValueDouble(coordinate.x + 2, coordinate.z) == Map.Tile.COVERED;
			case NORTH:
				return getValueDouble(coordinate.x - 2, coordinate.z) == Map.Tile.COVERED;
			case WEST:
				return getValueDouble(coordinate.x, coordinate.z + 2) == Map.Tile.COVERED;
			case EAST:
				return getValueDouble(coordinate.x, coordinate.z - 2) == Map.Tile.COVERED;
		}
		return false;
	}

	private boolean isNearWall() {
		switch (currentDirection) {
			case SOUTH:
				return getValueCoord(toPoint3d(SOUTH)) == Map.Tile.WALL;
			case NORTH:
				return getValueCoord(toPoint3d(NORTH)) == Map.Tile.WALL;
			case WEST:
				return getValueCoord(toPoint3d(WEST)) == Map.Tile.WALL;
			case EAST:
				return getValueCoord(toPoint3d(EAST)) == Map.Tile.WALL;
		}
		return false;
	}

	private Map.Tile getValueCoord(Point3d coord) {
		return map.getTile((int) Math.round(coord.x), (int) Math.round(coord.z));
	}

	// returns new coordinate which the coordinate +/- 2 point ahead/behind the current direction
	private Point3d toPoint3d(int direction) {
		switch (direction) {
			case EAST:
				return new Point3d(coordinate.x, coordinate.y, coordinate.z - 2);
			case WEST:
				return new Point3d(coordinate.x, coordinate.y, coordinate.z + 2);
			case SOUTH:
				return new Point3d(coordinate.x + 2, coordinate.y, coordinate.z);
			case NORTH:
				return new Point3d(coordinate.x - 2, coordinate.y, coordinate.z);
		}
		return null;
	}

	private Map.Tile getValueDouble(double x, double z) {
		return map.getTile((int) Math.round(x), (int) Math.round(z));
	}

	private void setDirection() {
		switch (currentDirection) {
			case SOUTH:
				currentDirection = WEST;
				break;
			case WEST:
				currentDirection = NORTH;
				break;
			case NORTH:
				currentDirection = EAST;
				break;
			case EAST:
				currentDirection = SOUTH;
				break;
			default:
				break;
		}
	}

	private boolean isUnvisited(Point3d coord) {
		return getValueCoord(coord) == Map.Tile.EMPTY;
	}

	public void isVisited(Point3d coord) {
		map.markAsCovered((int) Math.round(coord.x), (int) Math.round(coord.z));
	}
}

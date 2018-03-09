package main.java.softdesign;

import simbad.sim.Agent;
import simbad.sim.CameraSensor;
import simbad.sim.RobotFactory;

import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;
import java.awt.image.BufferedImage;

public class Robot extends Agent {

	private static final int UNVISITED = 0;
  private static final int VISITED = 1;
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
	private CameraSensor camera4;
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
		//camera4 = RobotFactory.addCameraSensor(this);
		cameraImage = camera.createCompatibleImage();
		cameraImage = camera2.createCompatibleImage();
		cameraImage = camera3.createCompatibleImage();
		//cameraImage = camera4.createCompatibleImage();
    camera.rotateY(Math.PI/2);
    camera2.rotateY(-(Math.PI/2));
    camera3.rotateY(Math.PI);
	}

	@Override
	public void performBehavior() {

		this.getCoords(coordinate);

		// perform the following actions every 5 virtual seconds
		if (this.getCounter() % 5 == 0) {
      takeImages();
			if (this.isNearWall() || this.isNearCovered()) {
				this.currentMode = "avoidObstacle";
			} else {
				this.currentMode = "goAround";
			}
			if (this.currentMode == "goAround") {
				this.setTranslationalVelocity(0.5);
			} else if (this.currentMode == "isNearPoint") {
        rotateY(-(Math.PI/2));
        setDirection();
      } else {
        rotateY(-(Math.PI/2));
        setDirection();
			}
		}
	}

  private void takeImages() {
    switch(currentDirection) {
      case SOUTH: image(camera, camera2, camera3); break;
      case WEST: image(camera3, camera2, camera); break;
      case NORTH: image(camera2, camera, camera3); break;
    }
  }

  private boolean isNearCovered() {
    switch(currentDirection) {
      case SOUTH:
        return getValueCoordMove(coordinate.x + 2, coordinate.z) == VISITED;
      case NORTH:
        return getValueCoordMove(coordinate.x - 2, coordinate.z) == VISITED;
      case WEST:
        return getValueCoordMove(coordinate.x, coordinate.z + 2) == VISITED;
      case EAST:
        return getValueCoordMove(coordinate.x, coordinate.z - 2) == VISITED;
    }
    return false;
  }

  private void image(CameraSensor camera, CameraSensor camera2, CameraSensor camera3) {
    switch (currentDirection) {
      case NORTH: move(WEST, EAST, SOUTH); break;
      case SOUTH: move(EAST, WEST, NORTH); break;
      case WEST: move(SOUTH, NORTH, EAST); break;
      case EAST: move(NORTH, SOUTH, WEST); break;
    }
  }

  private void move (int left, int right, int back) {
    if(isUnvisited(direction(left))) {
      coverAndTrack(this.camera, direction(left));
    }
    if(isUnvisited(direction(right))) {
      coverAndTrack(this.camera2, direction(right));
    }
    if(isUnvisited(direction(back))) {
      coverAndTrack(this.camera3, direction(back));
    }
  }


  private Point3d direction(int direction) {
    switch(direction) {
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

  private void coverAndTrack(CameraSensor camera, Point3d coord) {
    camera.copyVisionImage(cameraImage);
    isVisited(coord);
  }

  private boolean isNearWall() {
    switch(currentDirection) {
      case SOUTH:
        return getValueCoord(direction(SOUTH)) == Map.WALL;
      case NORTH:
        return getValueCoord(direction(NORTH)) == Map.WALL;
      case WEST:
        return getValueCoord(direction(WEST)) == Map.WALL;
      case EAST:
        return getValueCoord(direction(EAST)) == Map.WALL;
    }
    return false;
  }

	private int getValueCoordMove(double x, double z) {
		return map.getPoint((int) Math.round(x), (int) Math.round(z));
	}

  private void setDirection() {
    switch(currentDirection) {
      case SOUTH: currentDirection = WEST; break;
      case WEST: currentDirection = NORTH; break;
      case NORTH: currentDirection = EAST; break;
      case EAST: currentDirection = SOUTH; break;
      default: break;
    }
  }

	private int getValueCoord(Point3d coord) {
		return map.getPoint((int) Math.round(coord.x), (int) Math.round(coord.z));
	}
      
	private boolean isUnvisited(Point3d coord) {
		return getValueCoord(coord) == UNVISITED;
	}

	public void isVisited(Point3d coord) {
    map.setPoint((int) Math.round(coord.x), (int) Math.round(coord.z), VISITED);
  }

	/**
	 * This method is called by the simulator engine on reset.
	 */
	@Override
	public void initBehavior() {
	}
}

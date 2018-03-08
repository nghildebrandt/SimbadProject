package main.java.softdesign;

import simbad.sim.Agent;
import simbad.sim.CameraSensor;
import simbad.sim.RobotFactory;

import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;
import java.awt.image.BufferedImage;

public class Robot extends Agent {

	private static final int UNVISITED = 0;
	public static final int SOUTH = 1;
	public static final int WEST = 2;
	public static final int NORTH = 3;
	public static final int EAST = 4;

	private String currentMode;
  private int currentDirection;
	private Map map;
	private Point3d coordinate;
  private Point3d east;

	private CameraSensor camera;
	private CameraSensor camera2;
	private BufferedImage cameraImage;

	Robot(Vector3d position, String name, Map map, int currentDirection) {
		super(position, name);
		this.map = map;
    this.currentDirection = currentDirection;
		this.coordinate = new Point3d();

		RobotFactory.addBumperBeltSensor(this, 12);
		RobotFactory.addSonarBeltSensor(this, 4);
    initCameras();
	}

	private void initCameras() {
		camera = RobotFactory.addCameraSensor(this);
		//camera3 = RobotFactory.addCameraSensor(this);
		//camera4 = RobotFactory.addCameraSensor(this);
		cameraImage = camera.createCompatibleImage();
    camera.rotateY(Math.PI/2);
	}

	@Override
	public void performBehavior() {

		this.getCoords(coordinate);

		// perform the following actions every 5 virtual seconds
		if (this.getCounter() % 5 == 0) {
      takeImages();
			if (this.isNearWall()) {
				this.currentMode = "avoidObstacle";
			} else if (this.collisionDetected()) {
				this.currentMode = "collisionDetected";
			} else {
				this.currentMode = "goAround";
			}
			if (this.currentMode == "goAround") {
				this.setTranslationalVelocity(0.5);
			} else {
        rotateY(-(Math.PI/2));
        setDirection();
			}
		}
	}

  private void takeImages() {
    switch(currentDirection) {
      case SOUTH:
        if(isUnvisited(direction(EAST))) {
          coverAndTrack(camera, direction(EAST));
        }
        if(isUnvisited(direction(WEST))) {
          coverAndTrack(camera, direction(WEST));
        }
        if(isUnvisited(direction(NORTH))) {
          coverAndTrack(camera, direction(NORTH));
        }
        if(isUnvisited(direction(SOUTH))) {
          coverAndTrack(camera, direction(SOUTH));
        }
        break;
    }

  }

  private Point3d direction(int direction) {
    switch(direction) {
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

  private void coverAndTrack(CameraSensor camera, Point3d coord) {
    camera.copyVisionImage(cameraImage);
    isVisited(coord.x, coord.z);
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
		return getValue(coord.x, coord.z) == UNVISITED;
	}

	public void isVisited(double x, double z) {
    map.setPoint((int) Math.round(x), (int) Math.round(z), 1);
  }

	/**
	 * This method is called by the simulator engine on reset.
	 */
	@Override
	public void initBehavior() {
	}
}

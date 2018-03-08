package main.java.softdesign;

import simbad.sim.Agent;
import simbad.sim.CameraSensor;
import simbad.sim.RobotFactory;

import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;
import java.awt.image.BufferedImage;

public class Robot extends Agent {

	private static final int UNVISITED = 0;
	private static final int NORTH = 1;
	private static final int SOUTH = 2;
	private static final int WEST = 3;
	private static final int EAST = 4;

	private String currentMode;
  private int currentDirection;
	private Map map;
	private Point3d coordinate;
  private Point3d east;

	private CameraSensor camera;
	private CameraSensor camera2;
	private CameraSensor camera3;
	private CameraSensor camera4;
	private BufferedImage cameraImage;

	Robot(Vector3d position, String name, Map map) {
		super(position, name);
		this.map = map;
		this.coordinate = new Point3d();

		RobotFactory.addBumperBeltSensor(this, 12);
		RobotFactory.addSonarBeltSensor(this, 4);
    initCameras();
	}

	private void initCameras() {
		camera = RobotFactory.addCameraSensor(this);
		camera2 = RobotFactory.addCameraSensor(this);
		camera3 = RobotFactory.addCameraSensor(this);
		camera4 = RobotFactory.addCameraSensor(this);
		cameraImage = camera.createCompatibleImage();
		cameraImage = camera2.createCompatibleImage();
		cameraImage = camera3.createCompatibleImage();
		cameraImage = camera4.createCompatibleImage();
    camera.rotateY(Math.PI/2);
    camera2.rotateY(-(Math.PI/2));
    camera3.rotateY(-(Math.PI));
	}

	//intended to use to check if the image the camera is about to take has already been visited


	@Override
	public void performBehavior() {
    System.out.println(currentDirection);

		this.getCoords(coordinate);

		// perform the following actions every 5 virtual seconds
		if (this.getCounter() % 5 == 0) {
			try {
        whatDirection();
        takeImages();
				if (this.isNearWall()) {
					this.currentMode = "avoidObstacle";
				} else if (this.collisionDetected()) {
					this.currentMode = "collisionDetected";
				} else {
					this.currentMode = "goAround";
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (this.currentMode == "goAround") {
				this.setTranslationalVelocity(0.5);
			} else {
        this.rotateY(Math.PI/2);
			}
		}
	}


  private void takeImages() throws Exception{
    switch(currentDirection) {
      case SOUTH:
        if(isUnvisited(direction(EAST))) {
          coverAndTrack(camera, direction(EAST));
        }
        if(isUnvisited(direction(WEST))) {
          coverAndTrack(camera2, direction(WEST));
        }
        if(isUnvisited(direction(NORTH))) {
          coverAndTrack(camera3, direction(NORTH));
        }
        if(isUnvisited(direction(SOUTH))) {
          coverAndTrack(camera4, direction(SOUTH));
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

  private void coverAndTrack(CameraSensor camera, Point3d coord) throws Exception {
    camera.copyVisionImage(cameraImage);
    isVisited(coord.x, coord.z);
  }

	private boolean isNearWall() throws Exception {
		return getValue(coordinate.x + 1, coordinate.z) == Map.WALL || getValue(coordinate.x - 1, coordinate.z) == Map.WALL
				|| getValue(coordinate.x, coordinate.z + 1) == Map.WALL || getValue(coordinate.x, coordinate.z - 1) == Map.WALL;
	}

  private void whatDirection() throws Exception {
    if(getValue(coordinate.x + 1, coordinate.z) == Map.WALL) {
      currentDirection = SOUTH;
    } else if(getValue(coordinate.x - 1, coordinate.z) == Map.WALL) {
      currentDirection = NORTH;
    } else if (getValue(coordinate.x, coordinate.z + 1) == Map.WALL) {
      currentDirection = WEST;
    } else if (getValue(coordinate.x, coordinate.z - 1) == Map.WALL) {
      currentDirection = EAST;
    }
  }

	private int getValueCoord(Point3d coord) throws Exception {
		return map.getPoint((int) Math.round(coord.x), (int) Math.round(coord.z));
	}
      
	private boolean isUnvisited(Point3d coord) throws Exception {
		return getValue(coord.x, coord.z) == UNVISITED;
	}

	public void isVisited(double x, double z) throws Exception {
    map.setPoint((int) Math.round(x), (int) Math.round(z), 1);
	}

	private int getValue(Double x, Double z) throws Exception {
		return map.getPoint((int) Math.round(x), (int) Math.round(z));
	}

	/**
	 * This method is called by the simulator engine on reset.
	 */
	@Override
	public void initBehavior() {
    currentDirection = SOUTH;

	}
}

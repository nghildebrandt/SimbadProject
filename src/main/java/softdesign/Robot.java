package main.java.softdesign;

import simbad.sim.Agent;
import simbad.sim.CameraSensor;
import simbad.sim.RobotFactory;

import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;
import java.awt.image.BufferedImage;

public class Robot extends Agent {

	private static final int UNVISITED = 0;

	private String currentMode;
	private Map map;
	private Point3d coordinates;

	private CameraSensor camera;
	private CameraSensor camera2;
	private CameraSensor camera3;
	private CameraSensor camera4;
	private BufferedImage cameraImage;

	Robot(Vector3d position, String name, Map map) {
		super(position, name);
		this.map = map;
		this.coordinates = new Point3d();

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
	}

	//intended to use to check if the image the camera is about to take has already been visited
	public void hasCovered(Point3d coord) throws Exception {
		if (isUnvisited(coord)) {
			camera.copyVisionImage(cameraImage);
		}
	}

	private boolean isUnvisited(Point3d coord) throws Exception {
		return getValue(coord.x, coord.z) == UNVISITED;
	}

	@Override
	public void performBehavior() {
		camera.copyVisionImage(cameraImage);
		this.getCoords(coordinates);
		// perform the following actions every 5 virtual seconds
		if (this.getCounter() % 5 == 0) {
			try {
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
				this.setRotationalVelocity(0);
			} else if (this.currentMode == "collisionDetected") {
				this.setTranslationalVelocity(-0.5);
				setRotationalVelocity(Math.PI / 2);
			} else {
				this.setTranslationalVelocity(0.5);
				rotateY(90);
			}
		}
	}

	private boolean isNearWall() throws Exception {
		return getValue(coordinates.x + 1, coordinates.z) == Map.WALL || getValue(coordinates.x - 1, coordinates.z) == Map.WALL
				|| getValue(coordinates.x, coordinates.z + 1) == Map.WALL || getValue(coordinates.x, coordinates.z - 1) == Map.WALL;
	}

	private int getValue(Double x, Double z) throws Exception {
		return map.getPoint((int) Math.round(x), (int) Math.round(z));
	}


	/**
	 * This method is called by the simulator engine on reset.
	 */
	@Override
	public void initBehavior() {
		camera2.rotateY(90);
		camera3.rotateY(180);
		camera4.rotateY(270);
	}
}

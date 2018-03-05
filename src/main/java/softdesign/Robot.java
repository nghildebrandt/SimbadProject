package main.java.softdesign;

import java.awt.image.BufferedImage;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;
import simbad.sim.Agent;
import simbad.sim.CameraSensor;
import simbad.sim.RobotFactory;

public class Robot extends Agent {

	private String currentMode;
  public static int UNVISITED = 0;
  private int wall = CentralBase.WALL;
	CameraSensor camera;
  CameraSensor camera2;
  CameraSensor camera3;
  CameraSensor camera4;
	Map map = new Map();
	BufferedImage cameraImage;
	Point3d coords = new Point3d();

	public Robot(Vector3d position, String name, Map map) {
		super(position, name);
    this.map = map;
		camera = RobotFactory.addCameraSensor(this);
		camera2 = RobotFactory.addCameraSensor(this);
		camera3 = RobotFactory.addCameraSensor(this);
		camera4 = RobotFactory.addCameraSensor(this);
		cameraImage = camera.createCompatibleImage();
		cameraImage = camera2.createCompatibleImage();
		cameraImage = camera3.createCompatibleImage();
		cameraImage = camera4.createCompatibleImage();

		RobotFactory.addBumperBeltSensor(this, 12);
		RobotFactory.addSonarBeltSensor(this, 4);
	}


	/** This method is called by the simulator engine on reset. */
	public void initBehavior() {
    camera2.rotateY(90);
    camera3.rotateY(180);
    camera4.rotateY(270);
		try {
		} catch (Exception e) {
			e.printStackTrace();
		}
  }

  public void hasCovered(Point3d coord) throws Exception {
    if(isUnvisited(coord)) {
      camera.copyVisionImage(cameraImage);
    } else { }
  }
  
  public boolean isUnvisited(Point3d coord) throws Exception {
    return getValue(coord.x, coord.z) == UNVISITED;
  }

	public boolean isNearWall() throws Exception {
    return getValue(coords.x + 1, coords.z) == wall || getValue(coords.x - 1, coords.z) == wall || getValue(coords.x , coords.z + 1) == wall || getValue(coords.x, coords.z - 1) == wall;
	}

  private int getValue (Double x,Double z) throws Exception {
    return map.getPoint((int) Math.round(x),(int) Math.round(z));
  }

	public void performBehavior() {
		camera.copyVisionImage(cameraImage);
		camera2.copyVisionImage(cameraImage);
		this.getCoords(coords);
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
        setRotationalVelocity(Math.PI/2);
      } else {
        this.setTranslationalVelocity(0.5);
        rotateY(90);
        System.out.println("rotation" + this.getRotationalVelocity());
      }
    }
  }
}

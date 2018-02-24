package main.java.softdesign;

import java.awt.image.BufferedImage;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;
import simbad.sim.Agent;
import simbad.sim.CameraSensor;
import simbad.sim.RobotFactory;

public class Robot extends Agent {

	public static final int WORLD_SIZE = Environment.WORLD_SIZE;

	private String currentMode;
  private int wall = CentralBase.WALL;
	CameraSensor camera;
	Map map = new Map();
	BufferedImage cameraImage;
	Point3d coords = new Point3d();

	public Robot(Vector3d position, String name, Map map) {
		super(position, name);
    this.map = map;
		camera = RobotFactory.addCameraSensor(this);
		cameraImage = camera.createCompatibleImage();

		RobotFactory.addBumperBeltSensor(this, 12);
		RobotFactory.addSonarBeltSensor(this, 4);
	}

  //working on implementation of iterative deepening.  So,  robot is at (4,1), you would just check if (5,1),(3,1),(4,2)(4,0) has not yet been visited and there are no obstacles or walls
  public void itDeep() throws Exception {
    for(double i = 0; i < Math.floor(Math.sqrt(WORLD_SIZE)); i ++) {
      if(getValue(coords.x + i, coords.z) == wall) {
        setRotationalVelocity(Math.PI/2);
      } else if(getValue(coords.x, coords.z + i) == wall) {
        setRotationalVelocity(Math.PI/2);
      } else if(getValue(coords.x - 1, coords.z) == wall) {
        setRotationalVelocity(Math.PI/2);
      } else if(getValue(coords.x, coords.z -1) == wall) {
        setRotationalVelocity(Math.PI/2);
      }
    }
  }
  //will return the value of a point in the grid to see if its a wall or has been visited
  public int getValue (Double x,Double z) throws Exception {
    return map.getPoint((int) Math.round(x),(int) Math.round(z));
  }

	/** This method is called by the simulator engine on reset. */
	public void initBehavior() {
		try {
      System.out.println(map.getPoint(-5,1));
		} catch (Exception e) {
			e.printStackTrace();
		}
  }

	public boolean isNearWall() {
    return (Math.abs(Math.round(coords.x)) == WORLD_SIZE / 2 - 1 || Math.abs(Math.round(coords.z)) == WORLD_SIZE / 2 - 1);
	}

	public void performBehavior() {
		camera.copyVisionImage(cameraImage);
		this.getCoords(coords);
		// perform the following actions every 5 virtual seconds
		if (this.getCounter() % 5 == 0) {
			if (this.isNearWall()) {
        this.currentMode = "avoidObstacle";
      } else if (this.collisionDetected()) {
        this.currentMode = "collisionDetected";
      } else {
        this.currentMode = "goAround";
      }
      if (this.currentMode == "goAround") {
        this.setTranslationalVelocity(0.5);
        this.setRotationalVelocity(0);
      } else if (this.currentMode == "collisionDetected") {
        this.setTranslationalVelocity(0);
        setRotationalVelocity(0);
      } else {
        this.setTranslationalVelocity(0.5);
        setRotationalVelocity(Math.PI / 4);
      }
    }
  }
}

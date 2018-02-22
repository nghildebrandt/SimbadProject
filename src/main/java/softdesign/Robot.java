package main.java.softdesign;

import java.awt.image.BufferedImage;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

import simbad.sim.Agent;
import simbad.sim.CameraSensor;
import simbad.sim.RobotFactory;

public class Robot extends Agent {

	private String currentMode;
	 CameraSensor camera;
   Map map = new Map(10);
   BufferedImage cameraImage;
   Point3d coords = new Point3d();

  public Robot(Vector3d position, String name) {
  super(position, name);
  camera = RobotFactory.addCameraSensor(this);
  cameraImage = camera.createCompatibleImage();

  // Add bumpers
  RobotFactory.addBumperBeltSensor(this, 12);
  // Add sonars
  RobotFactory.addSonarBeltSensor(this, 4);
  }

  /** This method is called by the simulator engine on reset. */
  public void initBehavior() {
  }

  /** This method is call cyclically (20 times per second) by the simulator engine. 
 * @throws Exception */
  public void getCoordinates() throws Exception {
  this.getCoords(coords);
  System.out.println("X: " + Math.round(coords.x));
  System.out.println("Y: " + Math.round(coords.y));
  System.out.println("Z: " + Math.round(coords.z));
  }

  public void performBehavior() {
  camera.copyVisionImage(cameraImage);

  // perform the following actions every 5 virtual seconds
 	if(this.getCounter() % 20 == 0) {
 		try {
			getCoordinates();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
  	if(this.collisionDetected()) {

  		this.currentMode = "avoidObstacle";
  		
  	} else {
  		this.currentMode = "goAround";
  	}
  	if(this.currentMode == "goAround") {
  		// the robot's speed is always 0.5 m/s
    this.setTranslationalVelocity(0.5);

  		// frequently change orientation
  if ((getCounter() % 100) == 0) {
	    setRotationalVelocity(Math.PI / 2 * (0.5 - Math.random()));
	    }  
	  } else {
	  	// don't move
	  	this.setTranslationalVelocity(0);
	  	// rotate only until obstacle is not there
	  	setRotationalVelocity(Math.PI / 2);
	  }
  	}
  	
  }
}

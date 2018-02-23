package main.java.softdesign;

import java.awt.image.BufferedImage;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

import simbad.sim.Agent;
import simbad.sim.CameraSensor;
import simbad.sim.RobotFactory;

public class Robot extends Agent {

  public static final int WORLD_SIZE =  Environment.WORLD_SIZE;
  private String currentMode;
  CameraSensor camera;
  Map map = new Map();
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
    try {
		  mapWalls();
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  public void mapWalls () throws Exception {
    for(int i = 0; i < WORLD_SIZE/2; i++) {
      map.setPoint(i, WORLD_SIZE/2, -1);
      map.setPoint(i, -WORLD_SIZE/2, -1);
      map.setPoint(WORLD_SIZE/2, i, -1);
      map.setPoint(-WORLD_SIZE/2, i, -1);
    }
  }

  public void avoidWalls () {
    this.getCoords(coords);
    if(Math.round(coords.x) == 4) {
      System.out.println("The wall is near x+");
      setRotationalVelocity(3.14 /2);
    } else if (Math.round(coords.x) == -4) {
      System.out.println("The wall is near x-");
    } else if (Math.round(coords.z) == -4) {
      System.out.println("The wall is near z-");
    } else if (Math.round(coords.x) == -4) {
      System.out.println("The wall is near x-");
    }
  }

  public void performBehavior() {
    camera.copyVisionImage(cameraImage);
    // perform the following actions every 5 virtual seconds
    if(this.getCounter() % 5 == 0) {
      this.getCoords(coords);
      if(Math.round(coords.x) == 4) {
        this.currentMode = "avoidObstacle";
      } else {
        this.currentMode = "goAround";
      }

      if (this.currentMode == "goAround") {
        this.setTranslationalVelocity(0.5);
      } else {
        this.setTranslationalVelocity(-1);
        setRotationalVelocity(Math.PI / 2);
      }
    }
  }
}

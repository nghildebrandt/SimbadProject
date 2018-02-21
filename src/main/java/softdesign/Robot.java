package main.java.softdesign;

import javax.vecmath.Vector3d;
import simbad.sim.Agent;
import simbad.sim.RobotFactory;

public class Robot extends Agent {
  public Robot(Vector 3d position, String name) {
    super(position, name);

    RobotFactory.addCameraSensor(this);
  }
}

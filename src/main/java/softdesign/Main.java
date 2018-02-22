package main.java.softdesign;

import simbad.gui.*;
import simbad.sim.*;
import simbad.sim.Agent;
import simbad.sim.EnvironmentDescription;
import java.util.ArrayList;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

/**
 *
 * Start working on this file
 *
 * You can use as many source files and packages
 * as you like, as long as everything is below
 * main.java.softdesign
 *
 * Some resources:
 *
 * - https://github.com/jimmikaelkael/simbad/tree/master/src
 * - https://www.ibm.com/developerworks/java/library/j-robots/
 * - https://github.com/VU-SoftwareDesign/SimbadMultiRobot
 * - https://github.com/VU-SoftwareDesign/SimbadHelloWorld
 *
 */

public class Main {
  static Point3d coords = new Point3d();

  public void start() {
    Environment environment = new Environment();

    ArrayList<Agent> swarm = new ArrayList<Agent>();

    if(environment.size() <= 10) {
      for(int i = 0; i < 2; i ++) {
        swarm.add(new Robot(environment.coords(0,i), "small"));
      }
    } else if (environment.size() > 10 && environment.size() < 30) {
        for(int i = 1; i <= 2; i ++) {
          swarm.add(new Robot(environment.coords(0,i), "medium"));
          swarm.add(new Robot(environment.coords(-i,0), "medium"));
        }
    } else if (environment.size() >= 30) {
      for(int i = 1; i <= 4; i ++) {
          swarm.add(new Robot(environment.coords(0,i), "large"));
          swarm.add(new Robot(environment.coords(i,0), "large"));
      }
    }

    for (Agent robot:swarm)
      environment.add(robot);
    
    new Simbad(environment, false);
  }

  public static void main(String[] args) {
	new Main().start();
  }
}

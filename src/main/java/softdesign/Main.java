package main.java.softdesign;

import simbad.gui.*;
import simbad.sim.*;
import simbad.sim.Agent;
import simbad.sim.EnvironmentDescription;
import java.util.ArrayList;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

public class Main {
  static Point3d coords = new Point3d();

  public void start() {
    Environment environment = new Environment();


    ArrayList<Agent> swarm = new ArrayList<Agent>();

    if(Environment.WORLD_SIZE <= 10) {
      swarm.add(new Robot(environment.coords(0,0), "small"));
    } else if (Environment.WORLD_SIZE > 10 && Environment.WORLD_SIZE < 30) {
      for(int i = 1; i <= 2; i ++) {
        swarm.add(new Robot(environment.coords(0,i), "large"));
        swarm.add(new Robot(environment.coords(i,0), "large"));
      }
    } else if (Environment.WORLD_SIZE >= 30) {
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

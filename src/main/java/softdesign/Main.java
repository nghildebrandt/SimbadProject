package main.java.softdesign;

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

	public static void main(String[] args) {
    System.setProperty("j3d.implicitAntialiasing", "true");
    
    EnvironmentDescription environment = new Environment;
    
    robot robot1 = new robot(new Vector3d(0, 0, 0), "Robot 1");
    robot robot2 = new robot(new Vector3d(-2, 0, -2), "Robot 2");

    environment.add(robot1);
    environment.add(robot2);
    
    // here we create an instance of the whole Simbad simulator and we assign the newly created environment 
    Simbad frame = new Simbad(environment, false);
    frame.update(frame.getGraphics());
	}
}

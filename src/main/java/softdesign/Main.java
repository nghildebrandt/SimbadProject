package main.java.softdesign;

import simbad.gui.Simbad;
import simbad.sim.Agent;

import javax.vecmath.Vector3d;
import java.util.ArrayList;
import java.util.List;

public class Main {

	private void start() {
		CentralStation centralStation = CentralStation.getInstance();
		Map map = centralStation.copyMap();
		List<Agent> swarm = new ArrayList<>();

		if (Environment.SIZE <= Environment.LARGE) {
			swarm.add(new Robot(new Vector3d(8, 0, 8), "small", map, Robot.SOUTH));
			swarm.add(new Robot(new Vector3d(8, 0, -8), "small", map, Robot.SOUTH));
			swarm.add(new Robot(new Vector3d(-8, 0, -8), "small", map, Robot.SOUTH));
			swarm.add(new Robot(new Vector3d(-8, 0, 8), "small", map, Robot.SOUTH));

			Environment environment = Environment.getInstance();
			swarm.forEach(environment::add);

			new Simbad(environment, false);
		}
	}

	public static void main(String[] args) {
		new Main().start();
	}
}

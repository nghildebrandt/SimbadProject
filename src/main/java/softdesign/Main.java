package main.java.softdesign;

import simbad.gui.Simbad;
import simbad.sim.Agent;

import javax.vecmath.Vector3d;
import java.util.ArrayList;
import java.util.List;

public class Main {

	private void start() {
		CentralBase centralBase = new CentralBase();
		Map map = centralBase.sendMap();
		List<Agent> swarm = new ArrayList<>();

		if (Environment.WORLD_SIZE <= Environment.SMALL) {
			swarm.add(new Robot(new Vector3d(2, 0, 2), "small", map));
		} else if (Environment.WORLD_SIZE > Environment.SMALL && Environment.WORLD_SIZE < 30) {
			swarm.add(new Robot(new Vector3d(5, 0, 5), "small", map));
			swarm.add(new Robot(new Vector3d(5, 0, -5), "small", map));
			swarm.add(new Robot(new Vector3d(-5, 0, -5), "small", map));
			swarm.add(new Robot(new Vector3d(-5, 0, 5), "small", map));
		}

		Environment environment = new Environment();
		swarm.forEach(environment::add);

		new Simbad(environment, false);
	}

	public static void main(String[] args) {
		new Main().start();
	}
}

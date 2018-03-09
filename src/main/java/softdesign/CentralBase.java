package main.java.softdesign;

public class CentralBase {

	private Map map = new Map();
  public double coverageRequirement;
  public static final int WALL = -1;

	CentralBase() {
		mapWalls();
    coverageRequirement = 0.75;
	}

	private void mapWalls() {
		for (int i = 0; i <= Environment.WORLD_SIZE / 2; i++) {
			map.setPoint(i, Environment.WORLD_SIZE / 2, WALL);
			map.setPoint(-i, Environment.WORLD_SIZE / 2, WALL);
			map.setPoint(i, -Environment.WORLD_SIZE / 2, WALL);
			map.setPoint(-i, -Environment.WORLD_SIZE / 2, WALL);
			map.setPoint(Environment.WORLD_SIZE / 2, i, WALL);
			map.setPoint(Environment.WORLD_SIZE / 2, -i, WALL);
			map.setPoint(-Environment.WORLD_SIZE / 2, i, WALL);
			map.setPoint(-Environment.WORLD_SIZE / 2, -i, WALL);
		}
	}

	private void mapImaginaryWalls() {
		for (int i = 0; i <= Environment.WORLD_SIZE / 2; i++) {
			map.setPoint(0, i, -1);
			map.setPoint(0, -i, -1);
			map.setPoint(i, 0, -1);
			map.setPoint(-i, 0, -1);
		}
	}

	public boolean isMissionComplete() {
		return map.totalNumberOfPointsCovered()/Environment.WORLD_SIZE *2 > coverageRequirement;
	}

	//used to give copy to robots
	public Map sendMap() {
		return map;
	}
}

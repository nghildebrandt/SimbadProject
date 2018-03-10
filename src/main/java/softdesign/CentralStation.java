package main.java.softdesign;

public class CentralStation {

	private Map map = new Map();
	public double coverageRequirement;

	CentralStation() {
		mapWalls();
		coverageRequirement = 0.75;
		mapImaginaryWalls();
	}

	private void mapWalls() {
		for (int i = 0; i <= Environment.WORLD_SIZE / 2; i++) {
			map.setPoint(i, Environment.WORLD_SIZE / 2, Map.WALL);
			map.setPoint(-i, Environment.WORLD_SIZE / 2, Map.WALL);
			map.setPoint(i, -Environment.WORLD_SIZE / 2, Map.WALL);
			map.setPoint(-i, -Environment.WORLD_SIZE / 2, Map.WALL);
			map.setPoint(Environment.WORLD_SIZE / 2, i, Map.WALL);
			map.setPoint(Environment.WORLD_SIZE / 2, -i, Map.WALL);
			map.setPoint(-Environment.WORLD_SIZE / 2, i, Map.WALL);
			map.setPoint(-Environment.WORLD_SIZE / 2, -i, Map.WALL);
		}
	}

	private void mapImaginaryWalls() {
		for (int i = 0; i <= Environment.WORLD_SIZE / 2; i++) {
			map.setPoint(0, i, Map.WALL);
			map.setPoint(0, -i, Map.WALL);
			map.setPoint(i, 0, Map.WALL);
			map.setPoint(-i, 0, Map.WALL);
		}
	}

	public boolean isMissionComplete() {
		return map.totalNumberOfPointsCovered() / Environment.WORLD_SIZE * 2 > coverageRequirement;
	}

	//used to give copy to robots
	public Map sendMap() {
		return map;
	}
}

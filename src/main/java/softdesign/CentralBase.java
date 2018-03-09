package main.java.softdesign;

public class CentralBase {

	private Map map = new Map();

	CentralBase() {
		mapWalls();
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
			map.setPoint(0, i, -1);
			map.setPoint(0, -i, -1);
			map.setPoint(i, 0, -1);
			map.setPoint(-i, 0, -1);
		}
	}

	public boolean isMissionComplete() {
		return true;
	}

	//used to give copy to robots
	public Map sendMap() {
		return map;
	}
}

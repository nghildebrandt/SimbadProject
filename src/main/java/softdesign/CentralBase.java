package main.java.softdesign;

public class CentralBase {
  Map map = new Map();
  private static int world_size = Environment.WORLD_SIZE;
  public static final int WALL = -1;

  public CentralBase() {
		try {
			mapWalls();
      mapImaginaryWalls();
		} catch (Exception e) {
			e.printStackTrace();
		}
  }

	public void mapWalls() throws Exception {
		for (int i = 0; i <= world_size/2; i++) {
			map.setPoint(i, world_size/2, WALL);
			map.setPoint(-i, world_size/2, WALL);
			map.setPoint(i, -world_size/2, WALL);
			map.setPoint(-i, -world_size/2, WALL);
			map.setPoint(world_size/2, i, WALL);
			map.setPoint(world_size/2, -i, WALL);
			map.setPoint(-world_size/2, i, WALL);
			map.setPoint(-world_size/2, -i, WALL);
		}
	}

  public void mapImaginaryWalls() throws Exception {
    for (int i = 0; i <= world_size/2; i ++) {
      map.setPoint(0, i, -1);
      map.setPoint(0, -i, -1);
      map.setPoint(i, 0, -1);
      map.setPoint(-i, 0, -1 );
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

package main.java.softdesign;

public class Map {
  private int [][] map;
  private int min; //better names
  private int size; //better name
  private int world_size = Environment.WORLD_SIZE;

  public Map() {
    this.min = -5;
    this.size = world_size + 1;
    map = new int[size][size];
  }

  public void setPoint(int xCoord, int zCoord, int value) throws Exception {
    int x = xCoord - min;
    int z = zCoord - min;

    if (x < 0 || x >= size || z < 0 || z >= size) {
        throw new Exception("Cannot set a point outside the map.");
    } else {
        map[x][z] = value;
    }
  }

  public void getMap () {
    for (int i = 0; i < map.length; i++) {
      for(int j = 0; i < map[i].length; j++) {
        System.out.print(map[i][j]);
      }
    }
  }
  
  public int getPoint(int xCoord, int zCoord) throws Exception {
    int x = xCoord - min;
    int z = zCoord - min;

    if (x < 0 || x >= size || z < 0 || z >= size) {
        throw new Exception("Cannot get a point outside the map.");
    } else {
        return map[x][z];
    }
  }
}

  

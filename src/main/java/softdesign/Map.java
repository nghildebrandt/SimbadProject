package main.java.softdesign;

public class Map {
  private int [][] map;
  private int min; //better names
  private int size; //better name

  public Map(int world_size) {
    this.min = - (world_size/2);
    map = new int[world_size][world_size];
  }

  public void setPoint(int xCoord, int zCoord, int value) throws Exception {
    int x = xCoord - min;
    int z = zCoord - min;

    if (x < 0 || x >= size || z < 0 || z >= size) {
        throw new Exception("Cannot set a point outside the grid.");
    } else {
        map[x][z] = value;
    }
  }
  
  public int getPoint(int xCoord, int zCoord, int value) throws Exception {
    int x = xCoord - min;
    int z = zCoord - min;

    if (x < 0 || x >= size || z < 0 || z >= size) {
        throw new Exception("Cannot set a point outside the grid.");
    } else {
        return map[x][z];
    }
  }
}

  

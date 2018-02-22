package main.java.softdesign;

public class Map {
  private int [][] map;
  private int min; //better names
  private int size; //better name
  private int world_size = Environment.WORLD_SIZE;

  public Map() {
    this.min = -5;
    this.size = world_size;
    map = new int[world_size+1][world_size+1];
  }

  public void setPoint(int xCoord, int zCoord, int value) throws Exception {
    int x = xCoord - min;
    int z = zCoord - min;
    System.out.println("X: " + x);
    System.out.println("Z: " + z);

    if (x < 0 || x > size || z < 0 || z > size) {
        throw new Exception("Cannot set a point outside the map.");
    } else {
        map[x][z] = value;
    }
  }
  
  public int getPoint(int xCoord, int zCoord) throws Exception {
    int x = xCoord - min;
    int z = zCoord - min;

    if (x <= 0 || x > size || z <= 0 || z > size) {
        throw new Exception("Cannot get a point outside the map.");
    } else {
        return map[x][z];
    }
  }
}

  

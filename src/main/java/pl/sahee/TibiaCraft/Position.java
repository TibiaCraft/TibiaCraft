package pl.sahee.TibiaCraft;

import com.google.gson.JsonObject;
import org.bukkit.Bukkit;
import org.bukkit.Location;

public class Position {
  public int x;
  public int y;
  public int z;
  public Location location;

  public Position(int x, int y, int z) {
    this.x = x;
    this.y = y;
    this.z = z;
    
    location = new Location(Bukkit.getWorlds().get(0), this.x, getMinecraftYLocation(), this.z);
  }

  public Position(JsonObject o) {
    this.x = o.get("x").getAsInt();
    this.y = o.get("y").getAsInt();
    this.z = o.get("z").getAsInt();

    location = new Location(Bukkit.getWorlds().get(0), this.x, getMinecraftYLocation(), this.z);
  }

  public int getMinecraftYLocation() {
    switch (y) {
      case 0:
        return 28;
      case 1:
        return 24;
      case 2:
        return 20;
      case 3:
        return 16;
      case 4:
        return 12;
      case 5:
        return 8;
      case 6:
        return 4;
      case 7:
        return 0;
      case 8:
        return -5;
      case 9:
        return -10;
      case 10:
        return -15;
      case 11:
        return -20;
      case 12:
        return -25;
      case 13:
        return -30;
      case 14:
        return -35;
      case 15:
        return -40;
      case 16:
        return -45;
    }

    Bukkit.getLogger().info("Invalid MC Y value " + y);
    return 60;
  }
}

package pl.sahee.TibiaCraft.Builder;

import org.bukkit.Location;
import pl.sahee.TibiaCraft.Tiles.Tile;

import java.util.List;


public abstract class Buildable {
  String name;

  Buildable(String name) {
    this.name = name;
  }

  public abstract boolean match(Tile tile);

  public abstract List<Block> getBlocks(Tile tile);

  protected Location getGroundTileLocation(Tile tile) {
    Location groundTile = tile.position.location;

    return groundTile;
  }
}

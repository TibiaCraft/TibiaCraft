package pl.sahee.TibiaCraft.Builder;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import pl.sahee.TibiaCraft.Tiles.Item;
import pl.sahee.TibiaCraft.Tiles.Tile;

import java.util.ArrayList;
import java.util.List;

class WallConfig {
  String name;
  private String material;

  public WallConfig(JsonObject o) {
    this.name = o.get("name").getAsString();
    this.material = o.get("material").getAsString();
    Bukkit.getLogger().warning("WallConfig[" + this.name + "] loaded material " + this.material);
  }

  public String removeParentheses(final String str) {
    String updated = str.replaceAll("\\([^()]*\\)", "");
    if (updated.contains("(")) updated = removeParentheses(updated);
    return updated;
  }

  public boolean match(Item item) {
    String itemName = removeParentheses(item.getName.trim()).trim();
    return itemName.equals(this.name);
  }

  public Material getMaterial() {
    return Material.getMaterial(this.material);
  }
}

public class WallBuilder extends Buildable {
  private List<String> schema = new ArrayList<>();
  private List<WallConfig> wallConfigs = new ArrayList<>();

  protected WallBuilder(JsonObject o) {
    super(o.get("name").getAsString());
    JsonArray schemaData;

    schemaData = o.get("schema").getAsJsonArray();
    for (JsonElement schemaDatum : schemaData) {
      if (schemaDatum.isJsonNull()) {
        Bukkit.getLogger().warning("WallBuilder[" + this.name + "] schema contains null");
        continue;
      }
      schema.add(schemaDatum.getAsString());
    }
    Bukkit.getLogger().info("WallBuilder[" + this.name + "] schema contains " + schema.size() + " items.");

    schemaData = o.get("config").getAsJsonArray();
    for (JsonElement schemaDatum : schemaData) {
      if (!schemaDatum.isJsonObject()) {
        Bukkit.getLogger().warning("WallBuilder[" + this.name + "] config !isJsonObject");
        continue;
      }
      wallConfigs.add(new WallConfig(schemaDatum.getAsJsonObject()));
    }
    Bukkit.getLogger().info("WallBuilder[" + this.name + "] wallConfigs contains " + wallConfigs.size() + " items.");
  }

  public boolean match(Tile tile) {
    boolean matchResult = this.getWallConfig(tile) != null;
    return matchResult;
  }

  @Override
  public List<Block> getBlocks(Tile tile) {
    List<Block> blocks = new ArrayList<>();

    Location groundTile = getGroundTileLocation(tile).clone();
    groundTile.setY(groundTile.getY() - 1);

    WallConfig wallConfig = getWallConfig(tile);
    if (wallConfig == null) {
      return blocks;
    }

    for (String materialType : schema) {
      groundTile.setY(groundTile.getY() + 1);
      if (materialType.startsWith("SKIP")) {
        continue;
      }

      if (materialType.startsWith("FOUND_MATERIAL")) {
        Material configFoundMaterial = wallConfig.getMaterial();
        if (configFoundMaterial == null) {
          Bukkit.getLogger().info("Could not find materialType[" + materialType + "] in builder: " + this.name + " in wall config" + wallConfig.name);
          continue;
        }

        blocks.add(new Block(groundTile.getBlockX(), groundTile.getBlockY(), groundTile.getBlockZ(), configFoundMaterial, null));
        continue;
      }

      Material blockMaterial = Material.getMaterial(materialType);

      if (blockMaterial == null) {
        Bukkit.getLogger().info("Could not find materialType[" + materialType + "] in builder: " + this.name);
        continue;
      }

      blocks.add(new Block(groundTile.getBlockX(), groundTile.getBlockY(), groundTile.getBlockZ(), blockMaterial, null));
    }

    return blocks;
  }

  private WallConfig getWallConfig(Tile tile) {
    for (Item item : tile.items) {
      for (WallConfig wallConfig : wallConfigs) {
        if (wallConfig.match(item)) {
          return wallConfig;
        }
      }
    }
    return null;
  }

}

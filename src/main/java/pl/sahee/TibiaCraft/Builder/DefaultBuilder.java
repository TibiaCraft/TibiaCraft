package pl.sahee.TibiaCraft.Builder;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.bukkit.Location;
import org.bukkit.Material;
import pl.sahee.TibiaCraft.Tiles.Tile;

import java.util.ArrayList;
import java.util.List;


public class DefaultBuilder {
  String name;
  List<Matcher> matchers = new ArrayList<>();
  List<String> schema = new ArrayList<>();


  protected DefaultBuilder(JsonObject o) {
    name = o.get("name").getAsString();

    JsonElement matcherElements = o.get("matchers");
    if (matcherElements != null && !matcherElements.isJsonNull() && matcherElements.isJsonArray()) {
      JsonArray matcherObjects = matcherElements.getAsJsonArray();

      for (int i = 0; i < matcherObjects.size(); i++) {
        JsonElement matcherObject = matcherObjects.get(i);
        if (!matcherObject.isJsonNull() && matcherObject.isJsonObject()) {
          matchers.add(new Matcher(matcherObject.getAsJsonObject()));
        }
      }
    }
    JsonElement schemasElement = o.get("schema");
    if (schemasElement != null && !schemasElement.isJsonNull() && schemasElement.isJsonArray()) {
      JsonArray schemaArray = schemasElement.getAsJsonArray();
      for (int i = 0; i < schemaArray.size(); i++) {
        JsonElement schemaElement = schemaArray.get(i);
        if (!schemaElement.isJsonNull() && schemaElement.getAsString() != null) {
          schema.add(schemaElement.getAsString());
        }
      }
    }
  }

  public String getName() {
    return name;
  }

  public List<Block> getBlocks(Tile tile) {
    List<Block> blocks = new ArrayList<>();

    for (Matcher matcher : matchers) {
      if (matcher.match(tile)) {
        blocks.addAll(getBuilderBlocks(tile, matcher));
      }
    }

    return blocks;
  }

  private List<Block> getBuilderBlocks(Tile tile, Matcher matcher) {
    List<Block> blocks = new ArrayList<>();
    Location groundTile = tile.getGroundLocation();
    int minecraftY = tile.getMinecraftY();
    int limit = minecraftY <= 7 ? 4 : 5;

    groundTile.setY(groundTile.getY() - 1);
    for (int i = 0; i < schema.size() && i < limit; i++) {
      String schemaMaterial = schema.get(i);

      groundTile.setY(groundTile.getY() + 1);
      if (schemaMaterial.equals("SKIP")) {
        continue;
      }
      Material blockMaterial = null;
      if (schemaMaterial.equals("MATERIAL")) {
        if (matcher.getMaterial() != null) {
          blockMaterial = Material.getMaterial(matcher.getMaterial());
        } else {
          blockMaterial = Material.REDSTONE_BLOCK;
        }
      }

      if (blockMaterial == null) {
        blockMaterial = Material.getMaterial(schemaMaterial);
      }
      if (blockMaterial == null) {
        blockMaterial = Material.BEDROCK;
      }

      blocks.add(new Block(groundTile.getBlockX(), groundTile.getBlockY(), groundTile.getBlockZ(), blockMaterial, null));
    }

    return blocks;
  }


}

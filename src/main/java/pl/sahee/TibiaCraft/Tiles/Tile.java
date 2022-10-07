package pl.sahee.TibiaCraft.Tiles;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.bukkit.Location;
import pl.sahee.TibiaCraft.Position;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class Tile {
  public Position position;
  @Nullable
  public Item ground;
  public List<Item> items = new ArrayList<>();

  public Tile(Position position, Item ground, List<Item> items) {
    this.position = position;
    this.ground = ground;
    this.items = items;
  }

  public Tile(JsonObject o) throws Exception {

    JsonElement positionElement = o.get("position");
    if (positionElement == null || positionElement.isJsonNull()) {
      throw new Exception("Failed to create tile. positionElement is null");
    }
    JsonObject positionObject = positionElement.getAsJsonObject();
    if (positionObject == null || positionObject.isJsonNull()) {
      throw new Exception("Failed to create tile. positionObject is null");
    }

    JsonElement groundElement = o.get("ground");
    if (groundElement != null && !groundElement.isJsonNull()) {
      JsonObject groundObject = groundElement.getAsJsonObject();
      if (groundObject != null && !groundObject.isJsonNull()) {
        ground = new Item(groundObject);
      }
    }


    JsonElement itemsElement = o.get("items");
    if (itemsElement == null || itemsElement.isJsonNull()) {
      throw new Exception("Failed to create tile. itemsElement is null");
    }
    JsonArray itemsObject = itemsElement.getAsJsonArray();
    if (itemsObject == null || itemsObject.isJsonNull()) {
      throw new Exception("Failed to create tile. itemsObject is null");
    }


    position = new Position(positionObject);

    for (JsonElement itemElement : itemsObject) {
      if (itemElement == null || itemElement.isJsonNull()) {
        continue;
      }
      JsonObject itemObject = itemElement.getAsJsonObject();
      if (itemObject == null || itemObject.isJsonNull()) {
        continue;
      }

      items.add(new Item(itemObject));
    }
  }

  public Location getGroundLocation() {
    return position.location.clone();
  }

  public int getMinecraftY() {
    return position.y;
  }
}

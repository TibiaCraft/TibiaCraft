package pl.sahee.TibiaCraft.Builder;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import pl.sahee.TibiaCraft.Tiles.Item;
import pl.sahee.TibiaCraft.Tiles.Tile;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class Matcher {
  public String material;
  private List<String> groundNames = new ArrayList<>();
  private List<String> itemNames = new ArrayList<>();
  private List<Number> groundIds = new ArrayList<>();
  private List<Number> itemIds = new ArrayList<>();

  public Matcher(JsonObject o) {
    JsonElement materialElement = o.get("material");
    if (materialElement != null && !materialElement.isJsonNull()) {
      material = o.get("material").getAsString();
    }
    JsonElement groundNamesElement = o.get("groundNames");
    if (groundNamesElement != null && !groundNamesElement.isJsonNull() && groundNamesElement.isJsonArray()) {
      JsonArray groundNamesObject = groundNamesElement.getAsJsonArray();
      for (int i = 0; i < groundNamesObject.size(); i++) {
        JsonElement groundNameElement = groundNamesObject.get(i);
        if (groundNameElement != null && !groundNameElement.isJsonNull() && groundNameElement.getAsString() != null) {
          groundNames.add(groundNameElement.getAsString());
        }
      }
    }

    JsonElement itemNamesElement = o.get("itemNames");
    if (itemNamesElement != null && !itemNamesElement.isJsonNull() && itemNamesElement.isJsonArray()) {
      JsonArray itemNamesObject = itemNamesElement.getAsJsonArray();
      for (int i = 0; i < itemNamesObject.size(); i++) {
        JsonElement itemNameElement = itemNamesObject.get(i);
        if (itemNameElement != null && !itemNameElement.isJsonNull() && itemNameElement.getAsString() != null) {
          itemNames.add(itemNameElement.getAsString());
        }
      }
    }

    JsonElement itemIdsElement = o.get("itemIds");
    if (itemIdsElement != null && !itemIdsElement.isJsonNull() && itemIdsElement.isJsonArray()) {
      JsonArray itemIdsObjects = itemIdsElement.getAsJsonArray();
      for (int i = 0; i < itemIdsObjects.size(); i++) {
        JsonElement itemIdElement = itemIdsObjects.get(i);
        if (itemIdElement != null && !itemIdElement.isJsonNull()) {
          itemIds.add(itemIdElement.getAsNumber());
        }
      }
    }

    JsonElement groundIdsElement = o.get("groundIds");
    if (groundIdsElement != null && !groundIdsElement.isJsonNull() && groundIdsElement.isJsonArray()) {
      JsonArray groundIdsObjects = groundIdsElement.getAsJsonArray();
      for (int i = 0; i < groundIdsObjects.size(); i++) {
        JsonElement groundIdElement = groundIdsObjects.get(i);
        if (groundIdElement != null && !groundIdElement.isJsonNull() && groundIdElement.getAsNumber() != null) {
          groundIds.add(groundIdElement.getAsNumber());
        }
      }
    }
  }

  public boolean match(Tile tile) {
    if (tile.ground != null && groundIds.contains(tile.ground.id)) return true;
    if (tile.ground != null && groundNames.contains(tile.ground.getName)) return true;

    for (Item item : tile.items) {
      if (itemIds.contains(item.id)) return true;
      if (itemNames.contains(item.getName)) return true;
    }

    return false;
  }

  @Nullable
  public String getMaterial() {
    return material;
  }

  @Override
  public String toString() {
    return "[Matcher] itemNames:" + itemNames + " itemIds: " + itemIds + " groundNames:" + groundNames + " groundIds" + groundIds;
  }
}

package pl.sahee.TibiaCraft.Tiles;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.List;

public class TilesLoader {
  public List<Tile> getTiles(JsonArray floor) {
    List<Tile> tiles = new ArrayList<>();
    int tilesLoaded = 0;
    int tilesFailed = 0;
    for (JsonElement jsonElement : floor) {
      try {
        JsonObject obj = jsonElement.getAsJsonObject();
        if (obj == null || obj.isJsonNull()) {
          continue;
        }

        if (obj.isJsonObject()) {
          tiles.add(new Tile(obj));
          tilesLoaded++;
        } else {
          Bukkit.getLogger().info("Found tileObject that is not object");
        }
      } catch (Exception e) {
        tilesFailed++;
        Bukkit.getLogger().info("Erro " + e.getMessage());
        Bukkit.getLogger().info(jsonElement.toString());
      }
    }
    Bukkit.getLogger().info("Tiles loaded: " + tilesLoaded + " Tiles failed to load: " + tilesFailed);

    return tiles;
  }
}

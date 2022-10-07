package pl.sahee.TibiaCraft.JsonLoader;

import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import org.bukkit.Bukkit;

import java.io.File;
import java.io.FileReader;

public class JsonFileLoader {
  public JsonArray loadFile(String name) {

    try {
      File dataFolder = (Bukkit.getPluginManager().getPlugin("TibiaCraft")).getDataFolder();
      File configFile = new File(dataFolder, name);
      JsonParser parser = new JsonParser();

      return parser.parse(new FileReader(configFile)).getAsJsonArray();
    } catch (Exception e) {
      Bukkit.getLogger().info("Failed to load " + name + " " + e.getMessage());
    }
    return new JsonArray();
  }
}

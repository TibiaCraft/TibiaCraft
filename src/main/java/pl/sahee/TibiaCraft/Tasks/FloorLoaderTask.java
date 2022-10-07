package pl.sahee.TibiaCraft.Tasks;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.bukkit.scheduler.BukkitRunnable;
import pl.sahee.TibiaCraft.Builder.DefaultBuilder;
import pl.sahee.TibiaCraft.Helpers.Progress;
import pl.sahee.TibiaCraft.Position;
import pl.sahee.TibiaCraft.Queue.BlockBuilderQueue;
import pl.sahee.TibiaCraft.Tiles.Item;
import pl.sahee.TibiaCraft.Tiles.Tile;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;


public class FloorLoaderTask extends BukkitRunnable {
  private final List<DefaultBuilder> builders;
  private BlockBuilderQueue queue;
  private String floor;
  private Logger logger;

  public FloorLoaderTask(String floor, BlockBuilderQueue queue, Logger logger, List<DefaultBuilder> builders) {
    super();
    this.floor = floor;
    this.queue = queue;
    this.logger = logger;
    this.builders = builders;
  }

  @Override
  public void run() {
    Connection conn = null;
    String url = "jdbc:sqlite:plugins/TibiaCraft/map.db";

    try {
      conn = DriverManager.getConnection(url);
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }

    try (Statement stmt = conn.createStatement()) {
      String query = "select DISTINCT chunk from ITEMS";
      ResultSet chunksResultSet = stmt.executeQuery(query);
      List<String> chunks = new ArrayList<>();
      while (chunksResultSet.next()) {
        String chunkName = chunksResultSet.getString("chunk");
        chunks.add(chunkName);
      }

      Progress stmntProgress = new Progress("Loading tiles from database", chunks.size());
      int done = chunks.size();

      for (String chunkName : chunks) {
        ResultSet rs = stmt.executeQuery("SELECT X, Z, Y, items, ground from ITEMS where chunk = '" + chunkName + "';");
        List<Tile> tiles = new ArrayList<>();
        while (rs.next()) {
          try {
            int x = rs.getInt("X");
            int y = rs.getInt("y");
            int z = rs.getInt("z");
            String items = rs.getString("items");
            String ground = rs.getString("ground");
            Item groundItem = null;
            if (ground != null && !ground.equals("")) {
              JsonParser parser = new JsonParser();
              JsonObject json = (JsonObject) parser.parse(ground);
              groundItem = new Item(json);
            }

            JsonParser parser = new JsonParser();
            List<Item> tileItems = new ArrayList<>();
            JsonArray itemsObject = parser.parse(items).getAsJsonArray();

            for (JsonElement itemElement : itemsObject) {
              if (itemElement == null || itemElement.isJsonNull()) {
                continue;
              }
              JsonObject itemObject = itemElement.getAsJsonObject();
              if (itemObject == null || itemObject.isJsonNull()) {
                continue;
              }

              tileItems.add(new Item(itemObject));
            }
            tiles.add(new Tile(new Position(x, y, z), groundItem, tileItems));
          } catch (Exception e) {
            logger.info("errorr");
            logger.info(e.getMessage());
          }
        }

        ChunkBlock chunkBlock = new ChunkBlock();
        for (Tile tile : tiles) {
          for (DefaultBuilder builder : builders) {
            chunkBlock.add(builder.getBlocks(tile));
          }
        }
        this.queue.add(chunkBlock);
        stmntProgress.update(done--);
      }
      stmntProgress.done();
    } catch (SQLException e) {
      logger.info("FAILLLL");
      logger.info(e.getMessage());
    }
  }
}

package pl.sahee.TibiaCraft.Tiles;

import com.google.gson.JsonObject;

public class Item {
  public int id;
  public String getName;

  public Item(JsonObject o) {
    this.id = o.get("id").getAsInt();
    this.getName = o.get("name").getAsString();
  }
}

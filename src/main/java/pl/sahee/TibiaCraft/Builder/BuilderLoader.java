package pl.sahee.TibiaCraft.Builder;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import java.util.ArrayList;
import java.util.List;

public class BuilderLoader {

  public List<DefaultBuilder> getBuilders(JsonArray config, List<String> builderFilter) {
    List<DefaultBuilder> buildable = new ArrayList<>();

    for (JsonElement element : config) {
      if (element != null && !element.isJsonNull() && element.isJsonObject()) {
        DefaultBuilder b = new DefaultBuilder(element.getAsJsonObject());
        if (builderFilter.contains("all")) {
          buildable.add(b);
        } else if (builderFilter.contains(b.getName())) {
          buildable.add(b);
        }
      }
    }

    return buildable;
  }
}

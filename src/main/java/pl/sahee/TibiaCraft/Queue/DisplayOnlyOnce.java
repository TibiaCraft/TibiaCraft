package pl.sahee.TibiaCraft.Queue;

import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.List;

public class DisplayOnlyOnce<T> {

  List<T> displayed = new ArrayList();
  private String title;

  public DisplayOnlyOnce(String title) {
    this.title = title;
  }

  public void display(T data) {
    if (!displayed.contains(data)) {
      Bukkit.broadcastMessage(title + " " + data);
      displayed.add(data);
    }
  }

  public void displayAll() {
    String all = title + ": ";
    for (T t : displayed) {
      all += t.toString() + ", ";
    }
    Bukkit.broadcastMessage(all);
  }

}

package pl.sahee.TibiaCraft.Helpers;

import org.bukkit.Bukkit;

public class Progress {
  private int total;
  private String name;

  private long nextDisplay;
  private long displayEvery = 5000;

  public Progress(String name, int total) {
    this.name = name;
    this.setTotal(total);
    this.nextDisplay = System.currentTimeMillis() + displayEvery;
  }

  public void update(int currentValue) {
    if (nextDisplay < System.currentTimeMillis() && total > 0) {
      int progressPercent = (100 - ((currentValue * 100) / total));
      Bukkit.broadcastMessage("[Progress] " + this.name + " " + progressPercent + "%" + ". TODO:" + currentValue + "/" + total + ".");
      nextDisplay = System.currentTimeMillis() + displayEvery;
    } else if (nextDisplay < System.currentTimeMillis() && total == 0) {
      Bukkit.broadcastMessage("[Progress] " + this.name + " total not set.");
      nextDisplay = System.currentTimeMillis() + displayEvery;
    }
  }

  public void setTotal(int total) {
    this.total = total;
  }

  public void done() {
    Bukkit.broadcastMessage("[Progress] " + this.name + " finished.");
  }
}

package pl.sahee.TibiaCraft.Queue;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;
import pl.sahee.TibiaCraft.Builder.Block;
import pl.sahee.TibiaCraft.Tasks.ChunkBlock;

import java.util.Deque;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.logging.Logger;


public class BlockBuilderQueue {
  private final World world;
  BukkitTask task;
  private Deque<ChunkBlock> chunkBlocksQueue = new ConcurrentLinkedDeque<>();
  private Logger logger;
  private Long delay = 20L;
  private Long period = 20L;

  public BlockBuilderQueue(final World world, Logger logger) {
    this.logger = logger;
    this.world = world;

    Plugin plugin = Bukkit.getPluginManager().getPlugin("TibiaCraft");
    Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
      if (chunkBlocksQueue.size() > 0)
        Bukkit.broadcastMessage("Chunks to build length: " + chunkBlocksQueue.size());
    }, 20L * 5, 20L * 5);
  }

  private void restartBuilder() {
    if (task != null) {
      task.cancel();
    }
    Plugin plugin = Bukkit.getPluginManager().getPlugin("TibiaCraft");

    task = Bukkit.getScheduler().runTaskTimer(plugin, () -> {
      long start = System.currentTimeMillis();
      ChunkBlock chunkBlock = chunkBlocksQueue.poll();
      if (chunkBlock == null) {
        return;
      }
      Block block;
      while ((block = chunkBlock.queue.poll()) != null) {
        org.bukkit.block.Block mcBlock = world.getBlockAt(block.x, block.y, block.z);

        Chunk chunk = world.getChunkAt(mcBlock);
        if (!chunk.isLoaded()) {
          chunk.load();
        }
        mcBlock.setType(block.material, false);
        for (Player player : Bukkit.getOnlinePlayers()) {
          if (player.isSneaking()) {
            player.teleport(mcBlock.getLocation());
          }
        }
      }
      long end = System.currentTimeMillis();
      long elapsedTime = end - start;
      logger.info("Elapsed time: " + elapsedTime);
    }, delay, period);
  }

  public void add(ChunkBlock chunkBLock) {
    chunkBlocksQueue.add(chunkBLock);
  }

  public void stop() {
    this.logger.info("Builder queue stopped.");
    this.task.cancel();
  }

  public void start() {
    this.logger.info("Builder queue started.");
    restartBuilder();
  }

  public void setSpeed(Integer delay, Integer period) {
    this.delay = delay.longValue();
    this.period = period.longValue();
    restartBuilder();
  }
}
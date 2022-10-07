package pl.sahee.TibiaCraft;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.block.BlockGrowEvent;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.plugin.java.JavaPlugin;
import pl.sahee.TibiaCraft.Commands.BuildCommand;
import pl.sahee.TibiaCraft.Commands.QueueCommand;
import pl.sahee.TibiaCraft.Queue.BlockBuilderQueue;

import java.util.logging.Logger;


public class TibiaCraft extends JavaPlugin implements Listener {

  @Override
  public void onEnable() {
    Logger logger = getLogger();
    BlockBuilderQueue blockBuilderQueue = new BlockBuilderQueue(Bukkit.getWorlds().get(0), logger);

    this.getCommand("b").setExecutor(new BuildCommand(blockBuilderQueue, logger));
    this.getCommand("q").setExecutor(new QueueCommand(blockBuilderQueue, logger));
    getServer().getPluginManager().registerEvents(this, this);

    logger.info("TibiaCraft is enabled!");
  }

  @EventHandler
  public void onBlockFromTo(BlockFromToEvent event) {
    event.setCancelled(true);
  }

  public void onVinesGrow(BlockGrowEvent e) {
    if (e.getBlock().getType() == Material.VINE) {
      e.setCancelled(true);
    }
  }

  @EventHandler
  public void onPhysicsChange(BlockPhysicsEvent e) {
    if (e.getBlock().getType() == Material.VINE
        || e.getBlock().getType() == Material.CACTUS
        || e.getBlock().getType() == Material.SUGAR_CANE) {
      e.setCancelled(true);
    }
  }

  @EventHandler(priority = EventPriority.NORMAL)
  public void EntityChangeBlock(EntityChangeBlockEvent event) {
    if (event.getEntityType() == EntityType.FALLING_BLOCK) {
      event.setCancelled(true);
    }
  }
}

package pl.sahee.TibiaCraft.Commands;

import com.google.gson.JsonArray;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import pl.sahee.TibiaCraft.Builder.BuilderLoader;
import pl.sahee.TibiaCraft.Builder.DefaultBuilder;
import pl.sahee.TibiaCraft.JsonLoader.JsonFileLoader;
import pl.sahee.TibiaCraft.Queue.BlockBuilderQueue;
import pl.sahee.TibiaCraft.Tasks.FloorLoaderTask;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

public class BuildCommand implements CommandExecutor {
  private BlockBuilderQueue queue;
  private Logger logger;

  public BuildCommand(BlockBuilderQueue queue, Logger logger) {
    this.queue = queue;
    this.logger = logger;
  }

  @Override
  public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
    if (strings.length != 2) {
      commandSender.sendMessage("Use /build  config floors/floor_7_0");
      return false;
    }

    String configFileName = "config.json";
    JsonArray configData = new JsonFileLoader().loadFile(configFileName);

    if (configData.size() == 0) {
      logger.info("Config: " + configFileName + " not found or empty.");
      return false;
    }

    List<String> builderFilter = new ArrayList<>(Arrays.asList(strings[1].split(",")));
    List<DefaultBuilder> builders = new BuilderLoader().getBuilders(configData, builderFilter);
    if (builders.size() == 0) {
      Bukkit.getLogger().info("Builders list is empty.");
      return false;
    }

    Plugin plugin = Bukkit.getPluginManager().getPlugin("TibiaCraft");

    new FloorLoaderTask(strings[0], queue, logger, builders).runTaskAsynchronously(plugin);

    return true;
  }

}

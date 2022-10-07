package pl.sahee.TibiaCraft.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import pl.sahee.TibiaCraft.Queue.BlockBuilderQueue;

import java.util.logging.Logger;

public class QueueCommand implements CommandExecutor {
  private BlockBuilderQueue queue;
  private Logger logger;

  public QueueCommand(BlockBuilderQueue queue, Logger logger) {
    this.queue = queue;
    this.logger = logger;
  }

  @Override
  public boolean onCommand(CommandSender commandSender, Command command, String s, String[] params) {
    if (params.length < 1) {
      commandSender.sendMessage("Use '/q start|stop|speed [params]'");
      return false;
    }
    if (params[0].equals("speed")) {
      if (params.length != 3) {
        commandSender.sendMessage("Use '/q speed 20 20' (ticks)");
        return false;
      }
      try {
        Integer delay = Integer.parseInt(params[1]);
        Integer period = Integer.parseInt(params[2]);
        queue.setSpeed(delay, period);
        commandSender.sendMessage("Speed updated");
        return true;
      } catch (Exception e) {
        commandSender.sendMessage("Second parameter should be type of number");
        return false;
      }


    }
    if (params[0].equals("start")) {
      this.queue.start();
      commandSender.sendMessage("Queue started");
      return true;
    }
    if (params[0].equals("stop")) {
      this.queue.stop();
      commandSender.sendMessage("Queue stopped");
      return true;
    }

    this.logger.warning("Use /q start|stop|speed");
    return false;
  }
}

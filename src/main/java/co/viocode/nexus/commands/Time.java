package co.viocode.nexus.commands;

import co.viocode.nexus.Nexus;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Time implements CommandExecutor {

	private Nexus plugin;
	public Time(Nexus plugin) {
		this.plugin = plugin;
	}

	public boolean onCommand(CommandSender sender, Command command, String alias, String[] args) {

		// check if player
		if (!(sender instanceof Player)) {
			sender.sendMessage(ChatColor.RED + "[Nexus] Command must be issued within game.");
			return true;
		}

		// invalid args
		if (args.length > 1)
			return false;

		// init vars
		Player player = (Player) sender;
		World world = player.getWorld();

		// <command>
		if (args.length == 0) {

			// check permission
			if (!Nexus.checkPermission("nexus.time", player))
				return true;

			// format time
			int tick = (int) world.getTime();
			int hour = tick / 1000;
			hour += 6;
			if (hour > 23)
				hour -= 24;
			int min = (tick - ((int) (tick / 1000) * 1000)) / (1000 / 60);

			// display time
			if (min < 10)
				player.sendMessage(ChatColor.GREEN + "Time: " + ChatColor.WHITE + hour + ":0" + min);
			else
				player.sendMessage(ChatColor.GREEN + "Time: " + ChatColor.WHITE + hour + ":" + min);
			return true;
		}

		// <command> [dawn|day|dusk|night|lock]
		if (args.length == 1) {

			// check permission
			if (!Nexus.checkPermission("nexus.time.set", player))
				return true;

			// set world time
			String time = args[0].toLowerCase();
			if (time.equals("dawn")) {
				world.setTime(22200);
				return true;
			} else if (time.equals("day")) {
				world.setTime(0);
				return true;
			} else if (time.equals("dusk")) {
				world.setTime(12000);
				return true;
			} else if (time.equals("night")) {
				world.setTime(13800);
				return true;
			}

			// lock world time
			if (time.equals("lock")) {

			}
			return false;
		}

		// end of command
		return false;
	}
}
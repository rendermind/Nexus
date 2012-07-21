package co.viocode.nexus.commands;

import co.viocode.nexus.Nexus;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Weather implements CommandExecutor {

	private Nexus plugin;
	public Weather(Nexus plugin) {
		this.plugin = plugin;
	}

	public boolean onCommand(CommandSender sender, Command command, String alias, String[] args) {

		// check if player
		if (!Nexus.checkPlayer(sender))
			return true;

		// invalid args
		if (args.length > 1)
			return false;

		// init vars
		Player player = (Player) sender;
		World world = player.getWorld();

		// <command>
		if (args.length == 0) {

			// check permission
			if (!Nexus.checkPermission("nexus.weather", player))
				return true;

			// format weather
			int tick = (int) world.getWeatherDuration();
			int min = (tick / 20) / 60;

			// display weather
			if (world.hasStorm())
				player.sendMessage(ChatColor.GREEN + "Weather: " + ChatColor.WHITE + "Raining for another " + min + " minute(s)");
			else
				player.sendMessage(ChatColor.GREEN + "Weather: " + ChatColor.WHITE + "Clear for another " + min + " minute(s)");
			return true;
		}

		// <command> [clear|rain|storm|lock]
		if (args.length == 1) {

			// permission check
			if (!Nexus.checkPermission("nexus.weather.set", player))
				return true;

			// set world weather
			String weather = args[0].toLowerCase();
			if (weather.equals("clear")) {
				world.setStorm(false);
				world.setThundering(false);
				return true;
			} else if (weather.equals("rain")) {
				world.setStorm(true);
				world.setThundering(false);
				return true;
			} else if (weather.equals("storm")) {
				world.setStorm(true);
				world.setThundering(true);
				return true;
			}

			// lock world time
			if (weather.equals("lock")) {

			}
			return false;
		}

		// end of command
		return false;
	}
}
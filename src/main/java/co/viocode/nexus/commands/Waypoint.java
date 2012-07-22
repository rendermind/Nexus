package co.viocode.nexus.commands;

import co.viocode.nexus.Nexus;
import java.util.Set;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Waypoint implements CommandExecutor {

	private Nexus plugin;
	public Waypoint(Nexus plugin) {
		this.plugin = plugin;
	}

	public boolean onCommand(CommandSender sender, Command command, String alias, String[] args) {

		// check if player
		if (!Nexus.checkPlayer(sender))
			return true;

		// invalid args
		if (args.length < 1 || args.length > 2)
			return false;

		// init vars
		Player player = (Player) sender;
		Set <String> waypoints = Nexus.waypointConfig.getConfigurationSection("").getKeys(false);

		// <command> [list]
		if (args.length == 1 && args[0].equalsIgnoreCase("list")) {

			// check permission
			if (!Nexus.checkPermission("nexus.waypoint", player))
				return true;

			// if waypoints do not exist
			if (waypoints.isEmpty()) {
				player.sendMessage(ChatColor.RED + "Waypoints are not set.");
				return true;
			}

			// format waypoints
			String waypointList = "";
			for (String each : waypoints)
				waypointList += each + ", ";
			waypointList = waypointList.substring(0, waypointList.length() - 2);

			// display list of waypoints
			player.sendMessage(ChatColor.GREEN + "Waypoints: " + ChatColor.WHITE + waypointList);
			return true;
		}

		// <command> (waypoint)
		if (args.length == 1) {

			// check permission
			if (!Nexus.checkPermission("nexus.waypoint", player))
				return true;

			// if waypoint exists
			for (String each : waypoints) {
				if (each.toLowerCase().contains(args[0].toLowerCase())) {

					// get waypoint
					Location waypoint = player.getLocation();
					waypoint.setWorld(plugin.getServer().getWorld(Nexus.waypointConfig.getString(each + ".world")));
					waypoint.setX(Nexus.waypointConfig.getDouble(each + ".x"));
					waypoint.setY(Nexus.waypointConfig.getDouble(each + ".y"));
					waypoint.setZ(Nexus.waypointConfig.getDouble(each + ".z"));
					waypoint.setYaw((float) Nexus.waypointConfig.getDouble(each + ".yaw"));
					waypoint.setPitch((float) Nexus.waypointConfig.getDouble(each + ".pitch"));
					player.teleport(waypoint);
					return true;
				}
			}

			// waypoint not found
			player.sendMessage(ChatColor.RED + "Waypoint does not exist.");
			return true;
		}

		// <command> [set] (waypoint)
		if (args.length == 2 && args[0].equalsIgnoreCase("set")) {

			// check permission
			if (!Nexus.checkPermission("nexus.waypoint.set", player))
				return true;

			// if waypoint exists
			for (String each : waypoints) {
				if (each.equalsIgnoreCase(args[1])) {

					// update waypoint
					Nexus.waypointConfig.set(args[1] + ".world", player.getLocation().getWorld().getName());
					Nexus.waypointConfig.set(args[1] + ".x", player.getLocation().getX());
					Nexus.waypointConfig.set(args[1] + ".y", player.getLocation().getY());
					Nexus.waypointConfig.set(args[1] + ".z", player.getLocation().getZ());
					Nexus.waypointConfig.set(args[1] + ".yaw", player.getLocation().getYaw());
					Nexus.waypointConfig.set(args[1] + ".pitch", player.getLocation().getPitch());
					Nexus.saveWaypointConfig();
					player.sendMessage(ChatColor.GREEN + "Waypoint " + args[1] + " reset.");
					return true;
				}
			}

			// create waypoint
			Nexus.waypointConfig.set(args[1] + ".world", player.getLocation().getWorld().getName());
			Nexus.waypointConfig.set(args[1] + ".x", player.getLocation().getX());
			Nexus.waypointConfig.set(args[1] + ".y", player.getLocation().getY());
			Nexus.waypointConfig.set(args[1] + ".z", player.getLocation().getZ());
			Nexus.waypointConfig.set(args[1] + ".yaw", player.getLocation().getYaw());
			Nexus.waypointConfig.set(args[1] + ".pitch", player.getLocation().getPitch());
			Nexus.saveWaypointConfig();
			player.sendMessage(ChatColor.GREEN + "Waypoint " + args[1] + " set.");
			return true;
		}

		// <command> [del] (waypoint)
		if (args.length == 2 && args[0].equalsIgnoreCase("del")) {

			// check permission
			if (!Nexus.checkPermission("nexus.waypoint.set", player))
				return true;

			// if waypoint exists
			for (String each : waypoints) {
				if (each.equalsIgnoreCase(args[1])) {

					// delete waypoint
					Nexus.waypointConfig.set(each, null);
					Nexus.saveWaypointConfig();
					player.sendMessage(ChatColor.RED + "Waypoint " + each + " deleted.");
					return true;
				}
			}

			// waypoint not found
			player.sendMessage(ChatColor.RED + "Waypoint does not exist.");
			return true;
		}

		// end of command
		return false;
	}
}
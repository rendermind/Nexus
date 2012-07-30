package co.viocode.nexus.commands;

import co.viocode.nexus.Nexus;
import java.util.Collections;
import java.util.Set;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Home implements CommandExecutor {

	private Nexus plugin;
	public Home(Nexus plugin) {
		this.plugin = plugin;
	}

	public boolean onCommand(CommandSender sender, Command command, String alias, String[] args) {

		// check if player
		if (!Nexus.checkPlayer(sender))
			return true;

		// invalid args
		if (args.length > 2)
			return false;

		// init vars
		Player player = (Player) sender;
		Set<String> homes = Collections.EMPTY_SET;

		// <command>
		if (args.length == 0) {

			// check permission
			if (!Nexus.checkPermission("nexus.home", player))
				return true;

			// check if default home exists
			if (!Nexus.homeConfig.isConfigurationSection(player.getName() + ".default")) {
				player.sendMessage(ChatColor.RED + "Home default does not exist, type /home [set].");
				return true;
			}

			// teleport player to home
			Location home = player.getLocation();
			home.setWorld(plugin.getServer().getWorld(Nexus.homeConfig.getString(player.getName() + ".default.world")));
			home.setX(Nexus.homeConfig.getDouble(player.getName() + ".default.x"));
			home.setY(Nexus.homeConfig.getDouble(player.getName() + ".default.y"));
			home.setZ(Nexus.homeConfig.getDouble(player.getName() + ".default.z"));
			home.setYaw((float) Nexus.homeConfig.getDouble(player.getName() + ".default.yaw"));
			home.setPitch((float) Nexus.homeConfig.getDouble(player.getName() + ".default.pitch"));
			player.teleport(home);
			return true;
		}

		// <command> [list]
		if (args.length == 1 && args[0].equalsIgnoreCase("list")) {

			// check permission
			if (!Nexus.checkPermission("nexus.home.multi", player))
				return true;

			// check if homes exist
			if (!Nexus.homeConfig.isConfigurationSection(player.getName())) {
				player.sendMessage(ChatColor.RED + "Home default does not exist, type /home [set].");
				return true;
			}

			// get homes
			homes = Nexus.homeConfig.getConfigurationSection(player.getName()).getKeys(false);

			// format homes
			String homeList = "";
			for (String each : homes)
				homeList += each + ", ";
			homeList = homeList.substring(0, homeList.length() - 2);

			// display home list
			player.sendMessage(ChatColor.GREEN + "Homes: " + ChatColor.WHITE + homeList);
			return true;
		}

		// <command> [set]
		if (args.length == 1 && args[0].equalsIgnoreCase("set")) {

			// check permission
			if (!Nexus.checkPermission("nexus.home", player))
				return true;

			// check if home limit reached
			if (Nexus.homeConfig.isConfigurationSection(player.getName())) {
				if (Nexus.homeConfig.getConfigurationSection(player.getName()).getKeys(false).size() ==
						Nexus.homeConfig.getInt("limit")) {
					player.sendMessage(ChatColor.RED + "Home limit already reached.");
					return true;
				}
			}

			// create home
			Nexus.homeConfig.set(player.getName() + ".default.world", player.getLocation().getWorld().getName());
			Nexus.homeConfig.set(player.getName() + ".default.x", player.getLocation().getX());
			Nexus.homeConfig.set(player.getName() + ".default.y", player.getLocation().getY());
			Nexus.homeConfig.set(player.getName() + ".default.z", player.getLocation().getZ());
			Nexus.homeConfig.set(player.getName() + ".default.yaw", player.getLocation().getYaw());
			Nexus.homeConfig.set(player.getName() + ".default.pitch", player.getLocation().getPitch());
			Nexus.saveHomeConfig();
			player.sendMessage(ChatColor.GREEN + "Home default set.");
			return true;
		}

		// <command> (home)
		if (args.length == 1) {

			// check permission
			if (!Nexus.checkPermission("nexus.home.multi", player))
				return true;

			// check if homes exist
			if (!Nexus.homeConfig.isConfigurationSection(player.getName())) {
				player.sendMessage(ChatColor.RED + "Home default does not exist, type /home [set].");
				return true;
			}

			// get homes
			homes = Nexus.homeConfig.getConfigurationSection(player.getName()).getKeys(false);

			// check if home exists
			for (String each : homes) {
				if (each.toLowerCase().contains(args[0].toLowerCase())) {

					// teleport player to home
					Location home = player.getLocation();
					home.setWorld(plugin.getServer().getWorld(Nexus.homeConfig.getString(player.getName() + "." + each + ".world")));
					home.setX(Nexus.homeConfig.getDouble(player.getName() + "." + each + ".x"));
					home.setY(Nexus.homeConfig.getDouble(player.getName() + "." + each + ".y"));
					home.setZ(Nexus.homeConfig.getDouble(player.getName() + "." + each + ".z"));
					home.setYaw((float) Nexus.homeConfig.getDouble(player.getName() + "." + each + ".yaw"));
					home.setPitch((float) Nexus.homeConfig.getDouble(player.getName() + "." + each + ".pitch"));
					player.teleport(home);
					return true;
				}
			}

			// home not found
			player.sendMessage(ChatColor.RED + "Home does not exist.");
			return true;
		}

		// <command> [set] (home)
		if (args.length == 2 && args[0].equalsIgnoreCase("set")) {

			// check permission
			if (!Nexus.checkPermission("nexus.home.multi", player))
				return true;

			// check if home limit reached
			if (Nexus.homeConfig.isConfigurationSection(player.getName())) {
				if (Nexus.homeConfig.getConfigurationSection(player.getName()).getKeys(false).size() ==
						Nexus.homeConfig.getInt("limit")) {
					player.sendMessage(ChatColor.RED + "Home limit already reached.");
					return true;
				}
			}

			// get homes
			if (Nexus.homeConfig.isConfigurationSection(player.getName()))
				homes = Nexus.homeConfig.getConfigurationSection(player.getName()).getKeys(false);

			// check if home exists
			for (String each : homes)
				if (each.equalsIgnoreCase(args[1]))
					args[1] = each;

			// create home
			Nexus.homeConfig.set(player.getName() + "." + args[1] + ".world", player.getLocation().getWorld().getName());
			Nexus.homeConfig.set(player.getName() + "." + args[1] + ".x", player.getLocation().getX());
			Nexus.homeConfig.set(player.getName() + "." + args[1] + ".y", player.getLocation().getY());
			Nexus.homeConfig.set(player.getName() + "." + args[1] + ".z", player.getLocation().getZ());
			Nexus.homeConfig.set(player.getName() + "." + args[1] + ".yaw", player.getLocation().getYaw());
			Nexus.homeConfig.set(player.getName() + "." + args[1] + ".pitch", player.getLocation().getPitch());
			Nexus.saveHomeConfig();
			player.sendMessage(ChatColor.GREEN + "Home " + args[1] +  " set.");
			return true;
		}

		// <command> [del] (home)
		if (args.length == 2 && args[0].equalsIgnoreCase("del")) {

			// check permission
			if (!Nexus.checkPermission("nexus.home.multi", player))
				return true;

			// check if homes exist
			if (!Nexus.homeConfig.isConfigurationSection(player.getName())) {
				player.sendMessage(ChatColor.RED + "Home default does not exist, type /home [set].");
				return true;
			}

			// get homes
			homes = Nexus.homeConfig.getConfigurationSection(player.getName()).getKeys(false);

			// check if home exists
			for (String each : homes) {
				if (each.equalsIgnoreCase(args[1])) {

					// delete home
					Nexus.homeConfig.set(player.getName() + "." + each, null);
					Nexus.saveHomeConfig();
					player.sendMessage(ChatColor.RED + "Home " + each + " deleted.");
					return true;
				}
			}

			// home not found
			player.sendMessage(ChatColor.RED + "Home does not exist.");
			return true;
		}

		// end of command
		return false;
	}
}
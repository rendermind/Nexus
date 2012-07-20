package co.viocode.nexus.commands;

import co.viocode.nexus.Nexus;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Spawn implements CommandExecutor {

	private Nexus plugin;
	public Spawn(Nexus plugin) {
		this.plugin = plugin;
	}

	public boolean onCommand(CommandSender sender, Command command, String alias, String[] args) {

		// check if player
		if (!(sender instanceof Player)) {
			sender.sendMessage("[Nexus] Command must be issued within game.");
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
			if (!Nexus.checkPermission("nexus.spawn", player))
				return true;

			// get spawn location
			Location spawn = player.getLocation();
			if (!Nexus.spawnConfig.isConfigurationSection(player.getWorld().getName()))
				spawn.setWorld(plugin.getServer().getWorlds().get(0));
			spawn.setX(Nexus.spawnConfig.getDouble(spawn.getWorld().getName() + ".x"));
			spawn.setY(Nexus.spawnConfig.getDouble(spawn.getWorld().getName() + ".y"));
			spawn.setZ(Nexus.spawnConfig.getDouble(spawn.getWorld().getName() + ".z"));
			spawn.setYaw((float) Nexus.spawnConfig.getDouble(spawn.getWorld().getName() + ".yaw"));
			spawn.setPitch((float) Nexus.spawnConfig.getDouble(spawn.getWorld().getName() + ".pitch"));

			// teleport to spawn
			player.teleport(spawn);
			return true;
		}

		// <command> [set]
		if (args.length == 1 && args[0].equalsIgnoreCase("set")) {

			// check permission
			if (!Nexus.checkPermission("nexus.spawn.set", player))
				return true;

			// set spawn location
			Location spawn = player.getLocation();
			player.getWorld().setSpawnLocation((int) spawn.getX(), (int) spawn.getY(), (int) spawn.getZ());
			Nexus.spawnConfig.set(spawn.getWorld().getName() + ".x", spawn.getX());
			Nexus.spawnConfig.set(spawn.getWorld().getName() + ".y", spawn.getY());
			Nexus.spawnConfig.set(spawn.getWorld().getName() + ".z", spawn.getZ());
			Nexus.spawnConfig.set(spawn.getWorld().getName() + ".yaw", spawn.getYaw());
			Nexus.spawnConfig.set(spawn.getWorld().getName() + ".pitch", spawn.getPitch());
			Nexus.saveSpawnConfig();

			// display spawn set
			player.sendMessage(ChatColor.GREEN + "Spawn location set.");
			return true;
		}

		// end of command
		return false;
	}
}
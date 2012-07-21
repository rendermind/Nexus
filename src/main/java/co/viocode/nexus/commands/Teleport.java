package co.viocode.nexus.commands;

import co.viocode.nexus.Nexus;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Teleport implements CommandExecutor {

	private Nexus plugin;
	public Teleport(Nexus plugin) {
		this.plugin = plugin;
	}

	public boolean onCommand(CommandSender sender, Command command, String alias, String[] args) {

		// invalid args
		if (args.length < 1 || args.length > 2)
			return false;

		// check if player
		Boolean isPlayer = true;
		if (!(sender instanceof Player))
			isPlayer = false;

		// init vars
		Player player = null;
		if (isPlayer)
			player = (Player) sender;
		Player target = null;

		// check permission
		if (isPlayer)
			if (!Nexus.checkPermission("nexus.teleport", player))
				return true;

		// <command> [toggle]
		if (args.length == 1 && args[0].equalsIgnoreCase("toggle")) {

			// check if player
			if (!isPlayer)
				return false;

			// check permission
			if (!Nexus.checkPermission("nexus.teleport.toggle", player))
				return true;

			// toggle incoming teleports
			if (Nexus.teleportToggle.contains(player)) {
				Nexus.teleportToggle.remove(player);
				player.sendMessage(ChatColor.GREEN + "Incoming teleports enabled.");
			} else {
				Nexus.teleportToggle.add(player);
				player.sendMessage(ChatColor.RED + "Incoming teleports disabled.");
			}
			return true;
		}

		// <command> (player)
		if (args.length == 1) {

			// check if player
			if (!isPlayer)
				return false;

			// get player
			target = Nexus.findOnlinePlayer(args[0]);

			// check if player is offline
			if (target == null) {
				sender.sendMessage(ChatColor.RED + "Player is not online.");
				return true;
			}

			// check teleport toggle
			if (Nexus.teleportToggle.contains(target)) {
				player.sendMessage(ChatColor.RED + "Player has disabled incoming teleports.");
				return true;
			}

			// teleport player
			player.teleport(target);
			return true;
		}

		// <command> (player) (player)
		if (args.length == 2) {

			// get players
			Player fromTarget = Nexus.findOnlinePlayer(args[0]);
			Player toTarget = Nexus.findOnlinePlayer(args[1]);

			// check if player is offline
			if (fromTarget == null || toTarget == null) {
				sender.sendMessage(ChatColor.RED + "Player is not online.");
				return true;
			}

			// check teleport toggle
			if (Nexus.teleportToggle.contains(fromTarget) || Nexus.teleportToggle.contains(toTarget)) {
				player.sendMessage(ChatColor.RED + "Player has disabled incoming teleports.");
				return true;
			}

			// teleport player
			fromTarget.teleport(toTarget);
			sender.sendMessage(ChatColor.GREEN + fromTarget.getName() + " teleported to " + toTarget.getName() + ".");
			return true;
		}

		// end of command
		return false;
	}
}
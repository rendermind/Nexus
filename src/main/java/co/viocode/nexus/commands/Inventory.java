package co.viocode.nexus.commands;

import co.viocode.nexus.Nexus;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Inventory implements CommandExecutor {

	private Nexus plugin;
	public Inventory(Nexus plugin) {
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
		Player target = null;

		// <command> [clear]
		if (args.length == 1 && args[0].equalsIgnoreCase("clear")) {

			// check permission
			if (!Nexus.checkPermission("nexus.inventory.clear", player))
				return true;

			// clear inventory
			player.getInventory().clear();
			return true;
		}

		// <command> [clear] (player)
		if (args.length == 2 && args[0].equalsIgnoreCase("clear")) {

			// check permission
			if (!Nexus.checkPermission("nexus.inventory.clear", player))
				return true;

			// get player
			target = Nexus.findOnlinePlayer(args[1]);

			// check if player is offline
			if (target == null) {
				player.sendMessage(ChatColor.RED + "Player is not online.");
				return true;
			}

			// check if inventory is blocked
			if (!player.hasPermission("nexus.inventory.bypass")) {
				if (target.hasPermission("nexus.inventory.block")) {
					sender.sendMessage(ChatColor.RED + "Player inventory cannot be cleared.");
					return true;
				}
			}

			// clear inventory
			target.getInventory().clear();

			// display message to player
			target.sendMessage(ChatColor.RED + player.getName() + " cleared your inventory.");
			player.sendMessage(ChatColor.RED + target.getName() + " inventory cleared.");
			return true;
		}

		// <command> [edit] (player)
		if (args.length == 2 && args[0].equalsIgnoreCase("edit")) {

			// check permission
			if (!Nexus.checkPermission("nexus.inventory.edit", player))
				return true;

			// get player
			target = Nexus.findOnlinePlayer(args[1]);

			// check if player is offline
			if (target == null) {
				player.sendMessage(ChatColor.RED + "Player is not online.");
				return true;
			}

			// check if inventory is blocked
			if (!player.hasPermission("nexus.inventory.bypass")) {
				if (target.hasPermission("nexus.inventory.block")) {
					sender.sendMessage(ChatColor.RED + "Player inventory cannot be edited.");
					return true;
				}
			}

			// edit inventory
			player.openInventory(target.getInventory());
			return true;
		}

		// end of command
		return false;
	}
}
package co.viocode.nexus.commands;

import co.viocode.nexus.Nexus;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Unban implements CommandExecutor {

	private Nexus plugin;
	public Unban(Nexus plugin) {
		this.plugin = plugin;
	}

	public boolean onCommand(CommandSender sender, Command command, String alias, String[] args) {

		// invalid args
		if (args.length > 1)
			return false;

		// check if player
		Boolean isPlayer = true;
		if (!(sender instanceof Player))
			isPlayer = false;

		// init vars
		Player player = null;
		if (isPlayer)
			player = (Player) sender;
		OfflinePlayer target = Nexus.findOfflinePlayer(args[0]);

		// check permission
		if (isPlayer)
			if (!Nexus.checkPermission("nexus.ban", player))
				return true;

		// <command> (player)
		if (args.length == 1) {

			// check if player exists
			if (target == null) {
				sender.sendMessage(ChatColor.RED + "Player does not exist.");
				return true;
			}

			// check if player is banned
			if (!target.isBanned()) {
				sender.sendMessage(ChatColor.RED + "Player is not banned.");
				return true;
			}

			// unban player
			target.setBanned(false);
			plugin.getServer().broadcastMessage(ChatColor.GREEN + target.getName() + " unbanned from server!");
			return true;
		}

		// end of command
		return false;
	}

}
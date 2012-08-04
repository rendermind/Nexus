package co.viocode.nexus.commands;

import co.viocode.nexus.Nexus;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Ban implements CommandExecutor {

	private Nexus plugin;
	public Ban(Nexus plugin) {
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
		Player target = Nexus.findOnlinePlayer(args[0]);
		OfflinePlayer offlineTarget = Nexus.findOfflinePlayer(args[0]);

		// check permission
		if (isPlayer)
			if (!Nexus.checkPermission("nexus.ban", player))
				return true;

		// <command> (player)
		if (args.length == 1) {

			// ban online player
			if (target != null) {

				// check if banned
				if (target.isBanned()) {
					sender.sendMessage(ChatColor.RED + "Player already banned.");
					return true;
				}

				// check if ban is blocked
				if (target.hasPermission("nexus.ban.block")) {
					sender.sendMessage(ChatColor.RED + "Player cannot be banned.");
					return true;
				}

				// ban player
				target.setBanned(true);
				target.kickPlayer("Banned from server!");
				plugin.getServer().broadcastMessage(ChatColor.RED + target.getName() + " banned from server!");
				return true;
			}

			// ban offline player
			if (offlineTarget != null) {

				// check if banned
				if (offlineTarget.isBanned()) {
					sender.sendMessage(ChatColor.RED + "Player already banned.");
					return true;
				}

				// check if ban is blocked
				if (target.hasPermission("nexus.ban.block")) {
					sender.sendMessage(ChatColor.RED + "Player cannot be banned.");
					return true;
				}

				// ban player
				offlineTarget.setBanned(true);
				plugin.getServer().broadcastMessage(ChatColor.RED + offlineTarget.getName() + " banned from server!");
				return true;
			}

			// player not exist
			sender.sendMessage(ChatColor.RED + "Player does not exist.");
			return true;
		}

		// <command> (player) (reason)
		if (args.length == 2) {

			// format reason
			String reason = "";
			for (int i = 1; i < args.length; i ++)
				reason += args[i] + " ";
			reason = reason.substring(0, reason.length() - 1);

			// ban online player
			if (target != null) {

				// check if banned
				if (target.isBanned()) {
					sender.sendMessage(ChatColor.RED + "Player already banned.");
					return true;
				}

				// check if ban is blocked
				if (target.hasPermission("nexus.ban.block")) {
					sender.sendMessage(ChatColor.RED + "Player cannot be banned.");
					return true;
				}

				// ban player
				target.setBanned(true);
				target.kickPlayer("Banned from server!  Reason: " + reason);
				plugin.getServer().broadcastMessage(ChatColor.RED + target.getName() + " banned from server!  Reason: " + reason);
				return true;
			}

			// ban offline player
			if (offlineTarget != null) {

				// check if banned
				if (offlineTarget.isBanned()) {
					sender.sendMessage(ChatColor.RED + "Player already banned.");
					return true;
				}

				// check if ban is blocked
				if (target.hasPermission("nexus.ban.block")) {
					sender.sendMessage(ChatColor.RED + "Player cannot be banned.");
					return true;
				}

				// ban player
				offlineTarget.setBanned(true);
				plugin.getServer().broadcastMessage(ChatColor.RED + offlineTarget.getName() + " banned from server!  Reason: " + reason);
				return true;
			}

			// player not exist
			sender.sendMessage(ChatColor.RED + "Player does not exist.");
			return true;
		}

		// end of command
		return false;
	}
}
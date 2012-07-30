package co.viocode.nexus.commands;

import co.viocode.nexus.Nexus;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Kick implements CommandExecutor {

	private Nexus plugin;
	public Kick(Nexus plugin) {
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

		// check permission
		if (isPlayer)
			if (!Nexus.checkPermission("nexus.kick", player))
				return true;

		// <command> (player)
		if (args.length == 1) {

			// check if player is offline
			if (target == null) {
				sender.sendMessage(ChatColor.RED + "Player is not online.");
				return true;
			}

			// kick player
			target.kickPlayer("Kicked from server!");
			plugin.getServer().broadcastMessage(ChatColor.RED + target.getName() + " kicked from server!");
			return true;
		}

		// <command> (player) (reason)
		if (args.length == 2) {

			// check if player is offline
			if (target == null) {
				sender.sendMessage(ChatColor.RED + "Player is not online.");
				return true;
			}

			// format reason
			String reason = "";
			for (int i = 1; i < args.length; i ++)
				reason += args[i] + " ";
			reason = reason.substring(0, reason.length() - 1);

			// kick player
			target.kickPlayer("Kicked from server!  Reason: " + reason);
			plugin.getServer().broadcastMessage(ChatColor.RED + target.getName() + " kicked from server!  Reason: " + reason);
			return true;
		}

		// end of command
		return false;
	}
}
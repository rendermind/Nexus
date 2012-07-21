package co.viocode.nexus.commands;

import co.viocode.nexus.Nexus;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Whisper implements CommandExecutor {

	private Nexus plugin;
	public Whisper(Nexus plugin) {
		this.plugin = plugin;
	}

	public boolean onCommand(CommandSender sender, Command command, String alias, String[] args) {

		// check if player
		if (!(sender instanceof Player)) {
			sender.sendMessage(ChatColor.RED + "[Nexus] Command must be issued within game.");
			return true;
		}

		// invalid args
		if (args.length < 2)
			return false;

		// init vars
		Player player = (Player) sender;
		Player target = Nexus.findOnlinePlayer(args[0]);

		// check permission
		if (!Nexus.checkPermission("nexus.whisper", player))
			return true;

		// check if player is muted


		// format message
		String message = "";
		for (int i = 1; i < args.length; i ++)
			message += args[i] + " ";
		message = message.substring(0, message.length() - 1);

		// check if player is offline
		if (target == null) {
			player.sendMessage(ChatColor.RED + "Player is not online.");
			return true;
		}

		// display message to both players
		target.sendMessage(ChatColor.GREEN + "[" + player.getName() + " -> Me] " + ChatColor.WHITE + message);
		player.sendMessage(ChatColor.GREEN + "[Me -> " + target.getName() + "] " + ChatColor.WHITE + message);

		// store player to/from
		Nexus.reply.put(target, player);

		// end of command
		return true;
	}
}

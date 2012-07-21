package co.viocode.nexus.commands;

import co.viocode.nexus.Nexus;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Reply implements CommandExecutor {

	private Nexus plugin;
	public Reply(Nexus plugin) {
		this.plugin = plugin;
	}

	public boolean onCommand(CommandSender sender, Command command, String alias, String[] args) {

		// check if player
		if (!Nexus.checkPlayer(sender))
			return true;

		// invalid args
		if (args.length < 1)
			return false;

		// init vars
		Player player = (Player) sender;
		Player target = null;

		// check permission
		if (!Nexus.checkPermission("nexus.whisper", player))
			return true;

		// check if player is muted


		// format message
		String message = "";
		for (String each : args)
			message += each + " ";
		message = message.substring(0, message.length() - 1);

		// check for prior whisper
		if (!Nexus.reply.containsKey(player)) {
			player.sendMessage(ChatColor.RED + "You have not received a whisper.");
			return true;
		}

		// get player
		target = Nexus.reply.get(player);

		// check if player is offline
		if (target == null) {
			sender.sendMessage(ChatColor.RED + "Player is not online.");
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
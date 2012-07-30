package co.viocode.nexus.commands;

import co.viocode.nexus.Nexus;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Broadcast implements CommandExecutor {

	private Nexus plugin;
	public Broadcast(Nexus plugin) {
		this.plugin = plugin;
	}

	public boolean onCommand(CommandSender sender, Command command, String alias, String[] args) {

		// invalid args
		if (args.length == 0)
			return false;

		// check if player
		Boolean isPlayer = true;
		if (!(sender instanceof Player))
			isPlayer = false;

		// init vars
		Player player = null;
		if (isPlayer)
			player = (Player) sender;

		if (isPlayer)
			if (!Nexus.checkPermission("nexus.broadcast", player))
				return true;

		// check mute


		// format message
		String message = "";
		for (String each : args)
			message += each + " ";
		message = message.substring(0, message.length() - 1);

		// display message
		plugin.getServer().broadcastMessage(ChatColor.RED + "[Broadcast] " + ChatColor.WHITE + message);

		// end of command
		return true;
	}
}
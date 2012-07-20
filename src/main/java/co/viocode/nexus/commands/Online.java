package co.viocode.nexus.commands;

import co.viocode.nexus.Nexus;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Online implements CommandExecutor {

	private Nexus plugin;
	public Online(Nexus plugin) {
		this.plugin = plugin;
	}

	public boolean onCommand(CommandSender sender, Command command, String alias, String[] args) {

		// invalid args
		if (args.length > 0)
			return false;

		// check if player
		Boolean isPlayer = true;
		if (!(sender instanceof Player))
			isPlayer = false;

		// init vars
		Player player = null;
		if (isPlayer)
			player = (Player) sender;

		// check permission
		if (isPlayer)
			if (!Nexus.checkPermission("nexus.online", player))
				return true;

		// get online players
		String playerList = "";
		for (Player each : plugin.getServer().getOnlinePlayers())
			playerList += each.getName() + ", ";
		if (plugin.getServer().getOnlinePlayers().length != 0)
			playerList = playerList.substring(0, playerList.length() - 2);
		else
			playerList = "";

		// display online players
		sender.sendMessage(ChatColor.GREEN + "Online Players [" + plugin.getServer().getOnlinePlayers().length + '/' +
				plugin.getServer().getMaxPlayers() + "]: " + ChatColor.WHITE + playerList);

		// end of command
		return true;
	}
}
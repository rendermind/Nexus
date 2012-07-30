package co.viocode.nexus.commands;

import co.viocode.nexus.Nexus;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Back implements CommandExecutor {

	private Nexus plugin;
	public Back(Nexus plugin) {
		this.plugin = plugin;
	}

	public boolean onCommand(CommandSender sender, Command command, String alias, String[] args) {

		// check if player
		if (!Nexus.checkPlayer(sender))
			return true;

		// invalid args
		if (args.length > 1)
			return false;

		// init vars
		Player player = (Player) sender;

		// no prior location
		if (!Nexus.teleportBack.containsKey(player)) {
			player.sendMessage(ChatColor.RED + "You do not have a prior location.");
			return true;
		}

		// teleport player to prior location
		player.teleport(Nexus.teleportBack.get(player));
		return true;
	}
}

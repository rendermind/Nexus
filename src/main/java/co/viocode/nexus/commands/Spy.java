package co.viocode.nexus.commands;

import co.viocode.nexus.Nexus;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Spy implements CommandExecutor {

	private Nexus plugin;
	public Spy(Nexus plugin) {
		this.plugin = plugin;
	}

	public boolean onCommand(CommandSender sender, Command command, String alias, String[] args) {

		// check if player
		if (!Nexus.checkPlayer(sender))
			return true;

		// invalid args
		if (args.length > 0)
			return false;

		// init vars
		Player player = (Player) sender;

		// check permission
		if (!Nexus.checkPermission("nexus.whisper.spy", player))
			return true;

		// toggle spy
		if (Nexus.spyToggle.contains(player)) {
			Nexus.spyToggle.remove(player);
			player.sendMessage(ChatColor.GREEN + "Spying enabled.");
		} else {
			Nexus.spyToggle.add(player);
			player.sendMessage(ChatColor.RED + "Spying disabled.");
		}

		// end of command
		return true;
	}
}

package co.viocode.nexus.commands;

import co.viocode.nexus.Nexus;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Heal implements CommandExecutor {

	private Nexus plugin;
	public Heal(Nexus plugin) {
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
		Player target = null;

		// <command>
		if (args.length == 0) {

			// check if player
			if (!isPlayer)
				return false;

			// check permission
			if (isPlayer)
				if (!Nexus.checkPermission("nexus.heal", player))
					return true;

			player.setFoodLevel(20);
			player.setHealth(player.getMaxHealth());
			return true;
		}

		// <command> (player)
		if (args.length == 1) {

			// check permission
			if (isPlayer)
				if (!Nexus.checkPermission("nexus.heal.others", player))
					return true;

			// get player
			target = Nexus.findOnlinePlayer(args[0]);

			// check if player is offline
			if (target == null) {
				sender.sendMessage(ChatColor.RED + "Player is not online.");
				return true;
			}

			// heal player
			target.setFoodLevel(20);
			target.setHealth(target.getMaxHealth());
			target.sendMessage(ChatColor.GREEN + sender.getName() + " healed you.");
			sender.sendMessage(ChatColor.GREEN + target.getName() + " is healed.");
			return true;
		}

		// end of command
		return false;
	}
}
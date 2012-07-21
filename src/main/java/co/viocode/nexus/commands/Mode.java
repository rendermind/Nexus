package co.viocode.nexus.commands;

import co.viocode.nexus.Nexus;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Mode implements CommandExecutor {

	private Nexus plugin;
	public Mode(Nexus plugin) {
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

		// <command>
		if (args.length == 0) {

			// check if player
			if (!isPlayer)
				return false;

			// check permission
			if (!Nexus.checkPermission("nexus.mode", player))
				return true;

			// toggle mode
			if (player.getGameMode().equals(GameMode.SURVIVAL))
				player.setGameMode(GameMode.CREATIVE);
			else
				player.setGameMode(GameMode.SURVIVAL);
			return true;
		}

		// <command> (player)
		if (args.length == 1) {

			// check permission
			if (isPlayer)
				if (!Nexus.checkPermission("nexus.mode.others", player))
					return true;

			// init vars
			Player target = Nexus.findOnlinePlayer(args[0].toLowerCase());

			// check if player is offline
			if (target == null) {
				sender.sendMessage(ChatColor.RED + "Player is not online.");
				return true;
			}

			// toggle mode
			if (target.getGameMode().equals(GameMode.SURVIVAL)) {
				target.setGameMode(GameMode.CREATIVE);
				target.sendMessage(ChatColor.GREEN + sender.getName() + " set your game mode to creative.");
				sender.sendMessage(ChatColor.GREEN + target.getName() + " game mode set to creative.");
			} else {
				target.setGameMode(GameMode.SURVIVAL);
				target.sendMessage(ChatColor.GREEN + sender.getName() + " set your game mode to survival.");
				sender.sendMessage(ChatColor.GREEN + target.getName() + " game mode set to survival.");
			}
			return true;
		}

		// end of command
		return false;
	}
}
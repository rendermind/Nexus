package co.viocode.nexus.commands;

import co.viocode.nexus.Nexus;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Level implements CommandExecutor {

	private Nexus plugin;
	public Level(Nexus plugin) {
		this.plugin = plugin;
	}

	public boolean onCommand(CommandSender sender, Command command, String alias, String[] args) {

		// invalid args
		if (args.length > 2)
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
			if (!Nexus.checkPermission("nexus.level", player))
				return true;

			// format level
			int playerLevel = player.getLevel() + 1;
			int nextLevel = (int) (playerLevel * 17 + Math.max(playerLevel - 16, 0) * Math.max(playerLevel - 15, 0) * 1.5 +
					Math.max(playerLevel - 31, 0) * Math.max(playerLevel - 30, 0) * 2);

			// display level
			player.sendMessage(ChatColor.GREEN + "Level: " + ChatColor.WHITE + player.getLevel() + " " + ChatColor.GREEN + "Exp: " +
					ChatColor.WHITE + player.getTotalExperience() + "/" + nextLevel);
			return true;
		}

		// <command> (level)
		if (args.length == 1) {

			// check if player
			if (!isPlayer)
				return false;

			// check permission
			if (!Nexus.checkPermission("nexus.level.set", player))
				return true;

			// init vars
			int level;
			try {
				level = Integer.parseInt(args[0]);
			} catch (Exception e) {
				return false;
			}

			// check max level
			if (level > 30) {
				player.sendMessage(ChatColor.RED + "Maximum level is 30.");
				return true;
			}

			// format level
			int experience = (int) (level * 17 + Math.max(level - 16, 0) * Math.max(level - 15, 0) * 1.5 +
					Math.max(level - 31, 0) * Math.max(level - 30, 0) * 2);

			// set player level
			player.setTotalExperience(experience);
			player.setLevel(level);
			return true;
		}

		// <command> (player) (level)
		if (args.length == 2) {

			// check permission
			if (isPlayer)
				if (!Nexus.checkPermission("nexus.level.set", player))
					return true;

			// init vars
			Player target = Nexus.findOnlinePlayer(args[0].toLowerCase());
			int level = Integer.parseInt(args[1]);

			// check if player is offline
			if (target == null) {
				sender.sendMessage(ChatColor.RED + "Player is not online.");
				return true;
			}

			// check max level
			if (level > 30) {
				player.sendMessage(ChatColor.RED + "Maximum level is 30.");
				return true;
			}

			// format level
			int experience = (int) (level * 17 + Math.max(level - 16, 0) * Math.max(level - 15, 0) * 1.5 +
					Math.max(level - 31, 0) * Math.max(level - 30, 0) * 2);

			// set player level
			target.setTotalExperience(experience);
			target.setLevel(level);

			// display
			target.sendMessage(ChatColor.GREEN + sender.getName() + " set your level to " + level + ".");
			sender.sendMessage(ChatColor.GREEN + target.getName() + " set to level " + level + ".");
			return true;
		}

		// end of command
		return false;
	}
}
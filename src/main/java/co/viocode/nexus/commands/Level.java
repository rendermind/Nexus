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
			int expNext = (int) (7 + (int) (player.getLevel() * 3.5));
			int actualNext = expNext - (int) (player.getExp() * expNext) + player.getTotalExperience();

			// display level
			player.sendMessage(ChatColor.GREEN + "Level: " + ChatColor.WHITE + player.getLevel() + "  " + ChatColor.GREEN + "Exp: " +
					ChatColor.WHITE + player.getTotalExperience() + "/" + actualNext);
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
			if (level > 50) {
				player.sendMessage(ChatColor.RED + "Maximum level is 50.");
				return true;
			}

			// set player level
			player.setLevel(0);
			player.setExp(0);
			player.setTotalExperience(0);
			for (int i = 0; i < level; i ++) {
				player.setTotalExperience(player.getTotalExperience() + (int) (7 + (int) (player.getLevel() * 3.5)));
				player.setLevel(player.getLevel() + 1);
			}
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
			if (level > 50) {
				player.sendMessage(ChatColor.RED + "Maximum level is 50.");
				return true;
			}

			// set player level
			target.setLevel(0);
			target.setExp(0);
			target.setTotalExperience(0);
			for (int i = 0; i < level; i ++) {
				target.setTotalExperience(target.getTotalExperience() + (int) (7 + (int) (target.getLevel() * 3.5)));
				target.setLevel(target.getLevel() + 1);
			}

			// display
			target.sendMessage(ChatColor.GREEN + sender.getName() + " set your level to " + level + ".");
			sender.sendMessage(ChatColor.GREEN + target.getName() + " set to level " + level + ".");
			return true;
		}

		// end of command
		return false;
	}
}
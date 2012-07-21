package co.viocode.nexus.commands;

import co.viocode.nexus.Nexus;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TeleportC implements CommandExecutor {

	private Nexus plugin;
	public TeleportC(Nexus plugin) {
		this.plugin = plugin;
	}

	public boolean onCommand(CommandSender sender, Command command, String alias, String[] args) {

		// check if player
		if (!Nexus.checkPlayer(sender))
			return true;

		// invalid args
		if (args.length < 2 || args.length > 3)
			return false;

		// init vars
		Player player = (Player) sender;
		Location teleport = player.getLocation();

		// check permission
		if (!Nexus.checkPermission("nexus.teleport.coords", player))
			return true;

		// <command> (x) (z)
		if (args.length == 2) {

			// get coords
			double x = Double.parseDouble(args[0]);
			double z = Double.parseDouble(args[1]);

			// teleport player
			teleport.setX(x);
			teleport.setZ(z);
			teleport.setY(teleport.getWorld().getHighestBlockYAt(teleport));
			player.teleport(teleport);
			return true;
		}

		// <command> (x) (y) (z)
		if (args.length == 3) {

			// get coords
			double x = Double.parseDouble(args[0]);
			double y = Double.parseDouble(args[1]);
			double z = Double.parseDouble(args[2]);

			// teleport player
			teleport.setX(x);
			teleport.setY(y);
			teleport.setZ(z);
			player.teleport(teleport);
			return true;
		}

		// end of command
		return false;
	}
}
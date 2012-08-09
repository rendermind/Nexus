package co.viocode.nexus.commands;

import co.viocode.nexus.Nexus;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TeleportR implements CommandExecutor {

	private Nexus plugin;
	public TeleportR(Nexus plugin) {
		this.plugin = plugin;
	}

	public boolean onCommand(CommandSender sender, Command command, String alias, String[] args) {

		// check if player
		if (!Nexus.checkPlayer(sender))
			return true;

		// invalid args
		if (args.length < 1 || args.length > 2)
			return false;

		// init vars
		Player player = (Player) sender;
		Player target = null;

		// check permission
		if (!Nexus.checkPermission("nexus.teleport.request", player))
			return true;

		// <command> [list]
		if (args.length == 1 && args[0].equalsIgnoreCase("list")) {

			// check for teleport requests
			if (!Nexus.teleportRequest.containsValue(player)) {
				player.sendMessage(ChatColor.RED + "You have not received a teleport request.");
				return true;
			}

			// get & format teleport requests
			String requestList = "";
			for (Player each : findTeleportRequests(player))
				requestList += each.getName() + ", ";
			requestList = requestList.substring(0, requestList.length() - 2);

			// display teleport requests
			player.sendMessage(ChatColor.GREEN + "Requests: " + ChatColor.WHITE + requestList);
			return true;
		}

		// <command> [accept]
		if (args.length == 1 && args[0].equalsIgnoreCase("accept")) {

			// check for teleport requests
			if (!Nexus.teleportRequest.containsValue(player)) {
				player.sendMessage(ChatColor.RED + "You have not received a teleport request.");
				return true;
			}

			// too many teleport requests
			if (findTeleportRequests(player).size() > 1) {
				player.sendMessage(ChatColor.RED + "Specify which request to accept.");
				return true;
			}

			// get player
			for (Player each : findTeleportRequests(player))
				target = each;

			// accept teleport request
			Nexus.teleportRequest.remove(target);
			target.teleport(player);

			return true;
		}

		// <command> [deny]
		if (args.length == 1 && args[0].equalsIgnoreCase("deny")) {

			// check for teleport requests
			if (!Nexus.teleportRequest.containsValue(player)) {
				player.sendMessage(ChatColor.RED + "You have not received a teleport request.");
				return true;
			}

			// too many teleport requests
			if (findTeleportRequests(player).size() > 1) {
				player.sendMessage(ChatColor.RED + "Specify which request to deny.");
				return true;
			}

			// get player
			for (Player each : findTeleportRequests(player))
				target = each;

			// deny teleport request
			Nexus.teleportRequest.remove(target);
			target.sendMessage(ChatColor.RED + "Your teleport request is denied.");
			player.sendMessage(ChatColor.RED + "Denied teleport request.");
			return true;
		}

		// <command> [cancel]
		if (args.length == 1 && args[0].equalsIgnoreCase("cancel")) {

			// check for teleport requests
			if (!Nexus.teleportRequest.containsKey(player)) {
				player.sendMessage(ChatColor.RED + "You have not sent a teleport request.");
				return true;
			}

			// cancel teleport request
			Nexus.teleportRequest.remove(player);
			player.sendMessage(ChatColor.RED + "Teleport request cancelled.");
			return true;
		}

		// <command> (player)
		if (args.length == 1) {

			// get player
			target = Nexus.findOnlinePlayer(args[0]);

			// check if player is offline
			if (target == null) {
				sender.sendMessage(ChatColor.RED + "Player is not online.");
				return true;
			}

			// check teleport toggle
			if (!player.hasPermission("nexus.teleport.bypass")) {
				if (Nexus.teleportToggle.contains(target)) {
					player.sendMessage(ChatColor.RED + "Player has disabled incoming teleports.");
					return true;
				}
			}

			// create teleport request
			Nexus.teleportRequest.put(player, target);
			target.sendMessage(ChatColor.GREEN + "Received teleport request from " + player.getName() + ".");
			player.sendMessage(ChatColor.GREEN + "Sent teleport request to " + target.getName() + ".");
			return true;
		}

		// <command> [accept] (player)
		if (args.length == 2 && args[0].equalsIgnoreCase("accept")) {

			// check for teleport requests
			if (!Nexus.teleportRequest.containsValue(player)) {
				player.sendMessage(ChatColor.RED + "You have not received a teleport request.");
				return true;
			}

			// get teleport requests
			for (Player each : findTeleportRequests(player)) {
				if (each.getName().toLowerCase().contains(args[1].toLowerCase())) {

					// accept teleport request
					Nexus.teleportRequest.remove(each);
					each.teleport(player);
					return true;
				}
			}

			// teleport request not found
			player.sendMessage(ChatColor.RED + "Player has not set a teleport request.");
			return true;
		}

		// <command> [deny] (player)
		if (args.length == 2 && args[0].equalsIgnoreCase("deny")) {

			// check for teleport requests
			if (!Nexus.teleportRequest.containsValue(player)) {
				player.sendMessage(ChatColor.RED + "You have not received a teleport request.");
				return true;
			}

			// get teleport requests
			for (Player each : findTeleportRequests(player)) {
				if (each.getName().toLowerCase().contains(args[1].toLowerCase())) {

					// deny teleport request
					Nexus.teleportRequest.remove(each);
					each.sendMessage(ChatColor.RED + "Your teleport request is denied.");
					player.sendMessage(ChatColor.RED + "Denied teleport request.");
					return true;
				}
			}

			// teleport request not found
			player.sendMessage(ChatColor.RED + "Player has not set a teleport request.");
			return true;
		}

		// end of command
		return false;
	}

	// get teleport requests
	public Set<Player> findTeleportRequests(Player player) {
		Set<Player> requests = new HashSet<Player>(200);
		for (Map.Entry<Player,Player> entry : Nexus.teleportRequest.entrySet()) {
			if (entry.getValue().equals(player))
				requests.add(entry.getKey());
		}
		return requests;
	}
}
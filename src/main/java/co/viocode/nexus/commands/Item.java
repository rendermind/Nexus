package co.viocode.nexus.commands;

import co.viocode.nexus.Nexus;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Item implements CommandExecutor {

	private Nexus plugin;
	public Item(Nexus plugin) {
		this.plugin = plugin;
	}

	public boolean onCommand(CommandSender sender, Command command, String alias, String[] args) {

		// invalid args
		if (args.length < 1 || args.length > 3)
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
		ItemStack item = new ItemStack(0, 1);

		// check permission
		if (isPlayer)
			if (!Nexus.checkPermission("nexus.item", player))
				return true;

		// <command> (id)
		if (args.length == 1) {

			// check if player
			if (!Nexus.checkPlayer(sender))
				return true;

			// check item name
			if (Nexus.itemList.containsKey(args[0].toLowerCase()))
				args[0] = Nexus.itemList.get(args[0].toLowerCase()).toString();

			// check metadata
			try {
				if (args[0].contains(":")) {
					item.setTypeId(Integer.parseInt(args[0].substring(0, args[0].indexOf(":"))));
					item.setDurability(Short.parseShort(args[0].substring(args[0].indexOf(":") + 1)));
				} else {
					item.setTypeId(Integer.parseInt(args[0]));
				}
			} catch (Exception e) {
				player.sendMessage(ChatColor.RED + "Item name or ID does not exist.");
				return true;
			}

			// give item to player
			player.getInventory().addItem(item);
			return true;
		}

		// <command> (player) (id)
		if (args.length == 2) {

			// get player
			target = Nexus.findOnlinePlayer(args[0]);

			// check if player is offline
			if (target == null) {
				sender.sendMessage(ChatColor.RED + "Player is not online.");
				return true;
			}

			// check item names
			if (Nexus.itemList.containsKey(args[1].toLowerCase()))
				args[1] = Nexus.itemList.get(args[1].toLowerCase().toString());

			// check metadata
			try {
				if (args[1].contains(":")) {
					item.setTypeId(Integer.parseInt(args[1].substring(0, args[1].indexOf(":"))));
					item.setDurability(Short.parseShort(args[1].substring(args[1].indexOf(":") + 1)));
				} else {
					item.setTypeId(Integer.parseInt(args[1]));
				}
			} catch (Exception e) {
				sender.sendMessage(ChatColor.RED + "Item name or ID does not exist.");
				return true;
			}

			// give item to player
			target.getInventory().addItem(item);
			target.sendMessage(ChatColor.GREEN + sender.getName() + " gave you item ID " + item.getTypeId() + "(x1).");
			sender.sendMessage(ChatColor.GREEN + " Gave + " + target.getName() + " item ID " + item.getTypeId() + "(x1).");
			return true;
		}

		// <command> (id) (qty)
		if (args.length == 2) {

			// check if player
			if (!Nexus.checkPlayer(sender))
				return true;

			// check item names
			if (Nexus.itemList.containsKey(args[0].toLowerCase()))
				args[0] = Nexus.itemList.get(args[0].toLowerCase().toString());

			// check metadata
			try {
				if (args[0].contains(":")) {
					item.setTypeId(Integer.parseInt(args[0].substring(0, args[0].indexOf(":"))));
					item.setDurability(Short.parseShort(args[0].substring(args[0].indexOf(":") + 1)));
				} else {
					item.setTypeId(Integer.parseInt(args[0]));
				}
			} catch (Exception e) {
				player.sendMessage(ChatColor.RED + "Item name or ID does not exist.");
				return true;
			}

			// give item to player
			item.setAmount(Integer.parseInt(args[1]));
			player.getInventory().addItem(item);
			return true;
		}

		// <command> (player) (id) (qty)
		if (args.length == 3) {

			// get player
			target = Nexus.findOnlinePlayer(args[0]);

			// check if player is offline
			if (target == null) {
				sender.sendMessage(ChatColor.RED + "Player is not online.");
				return true;
			}

			// check item names
			if (Nexus.itemList.containsKey(args[1].toLowerCase()))
				args[1] = Nexus.itemList.get(args[1].toLowerCase().toString());

			// check metadata
			try {
				if (args[1].contains(":")) {
					item.setTypeId(Integer.parseInt(args[1].substring(0, args[1].indexOf(":"))));
					item.setDurability(Short.parseShort(args[1].substring(args[1].indexOf(":") + 1)));
				} else {
					item.setTypeId(Integer.parseInt(args[1]));
				}
			} catch (Exception e) {
				sender.sendMessage(ChatColor.RED + "Item name or ID does not exist.");
				return true;
			}

			// give item to player
			item.setAmount(Integer.parseInt(args[2]));
			target.getInventory().addItem(item);
			target.sendMessage(ChatColor.GREEN + sender.getName() + " gave you item ID " + item.getTypeId() +
					" (x" + item.getAmount() + ").");
			sender.sendMessage(ChatColor.GREEN + "Gave " + target.getName() + " item ID " + item.getTypeId() +
					" (x" + item.getAmount() + ").");
			return true;
		}

		// end of command
		return false;
	}
}
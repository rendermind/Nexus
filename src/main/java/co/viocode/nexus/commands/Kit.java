package co.viocode.nexus.commands;

import co.viocode.nexus.Nexus;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Kit implements CommandExecutor {

	private Nexus plugin;
	public Kit(Nexus plugin) {
		this.plugin = plugin;
	}

	public boolean onCommand(CommandSender sender, Command command, String alias, String[] args) {

		// check if player
		if (!Nexus.checkPlayer(sender))
			return true;

		// invalid args
		if (args.length != 1)
			return false;

		// init vars
		Player player = (Player) sender;
		Set<String> kits = Nexus.kitConfig.getConfigurationSection("").getKeys(false);
		List<String> items = Collections.EMPTY_LIST;

		// <command> [list]
		if (args.length == 1 && args[0].equalsIgnoreCase("list")) {

			// if kits do not exist
			if (kits.isEmpty()) {
				player.sendMessage(ChatColor.RED + "Kits are not set.");
				return true;
			}

			// format kits
			String kitList = "";
			for (String each : kits)
				kitList += each + ", ";
			kitList = kitList.substring(0, kitList.length() - 2);

			// display list of kits
			player.sendMessage(ChatColor.GREEN + "Kits: " + ChatColor.WHITE + kitList);
			return true;
		}

		// <command> (kit)
		if (args.length == 1) {

			// init vars
			ItemStack item = new ItemStack(0, 1);

			// get items
			for (String each : kits)
				if (each.toLowerCase().contains(args[0].toLowerCase()))
					items = Nexus.kitConfig.getStringList(each);

			// cycle through each item
			for (String each : items) {

				// parse item id & qty
				String id = each.substring(0, each.indexOf(","));
				int qty = Integer.parseInt(each.substring(each.indexOf(",") + 1));

				// check metadata
				if (each.contains(":")) {
					item.setTypeId(Integer.parseInt(id.substring(0, id.indexOf(":"))));
					item.setDurability(Short.parseShort(id.substring(id.indexOf(":") + 1)));
				} else {
					item.setTypeId(Integer.parseInt(id));
				}

				// give item to player
				item.setAmount(qty);
				player.getInventory().addItem(item);
			}
			return true;
		}

		// end of command
		return false;
	}
}

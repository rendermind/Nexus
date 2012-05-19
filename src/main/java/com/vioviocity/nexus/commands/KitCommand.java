package com.vioviocity.nexus.commands;

import com.vioviocity.nexus.Nexus;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class KitCommand implements CommandExecutor{
    
    private Nexus plugin;
    public KitCommand(Nexus plugin) {
        this.plugin = plugin;
    }
    
    public boolean onCommand(CommandSender sender, Command command, String alias, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("[Nexus] Command must be issued within game.");
            return true;
        }
        
        // initialize variables
        Player player = (Player) sender;
        Set<String> kits = Collections.EMPTY_SET;
        List<String> items = Collections.EMPTY_LIST;
        
        // command handler
        String cmd = command.getName().toLowerCase();
        if (cmd.equals("kit")) {
	    
            // check permission
            if (!Nexus.checkPermission("nexus.kit", player, true))
                return true;
	    
            // invalid args
            if (args.length > 1)
                return false;
            
            // load kits
            if (Nexus.kitConfig.isConfigurationSection("nexus.kit"))
                kits = Nexus.kitConfig.getConfigurationSection("nexus.kit").getKeys(false);
            
            // <command> [list], <command> (kit)
            if (args.length == 1) {
		
		// kits not set
                if (kits.isEmpty()) {
		    player.sendMessage(ChatColor.RED + "Kits are not set.");
		    return true;
		}
		
                // <command> [list]
                if (args[0].equalsIgnoreCase("list")) {
                    String kitList = "";
                    for (String each : kits)
                        kitList += each + ", ";
                    kitList = kitList.substring(0, kitList.length() - 2);
                    player.sendMessage(ChatColor.GREEN + "Kits: " + ChatColor.WHITE + kitList);
                    return true;
                }
                
                // initialize variables
                String kitName = args[0].toLowerCase();
		ItemStack item = new ItemStack(0, 1);
                
                // <command> (kit)
                for (String each : kits) {
                    if (each.equalsIgnoreCase(kitName)) {
                        items = Nexus.kitConfig.getStringList("nexus.kit." + each);
			for (String another : items) {
			    
			    // parse item id & qty
			    String id = another.substring(0, another.indexOf(','));
			    int qty = Integer.parseInt(another.substring(another.indexOf(',') + 1));
			    
			    // check metadata
			    if (each.contains(":")) {
				item.setTypeId(Integer.parseInt(id.substring(0, id.indexOf(":"))));
				item.setDurability(Short.parseShort(id.substring(id.indexOf(":") + 1)));
			    } else {
				item.setTypeId(Integer.parseInt(id));
			    }
			    
			    // give item
			    item.setAmount(qty);
			    player.getInventory().addItem(item);
			}
			return true;
                    }
                }
                
                // kit not found
                player.sendMessage(ChatColor.RED + "Kit does not exist.");
                return true;
            }
        }
        
        // end of command
        return false;
    }
}
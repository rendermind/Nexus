package com.vioviocity.nexus.commands;

import com.vioviocity.nexus.Nexus;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ItemCommand implements CommandExecutor{
    
    private Nexus plugin;
    public ItemCommand(Nexus plugin) {
        this.plugin = plugin;
    }

    public boolean onCommand(CommandSender sender, Command command, String alias, String[] args) {
        if (!(sender instanceof Player))
            return true;
        
        // initialize core variables
        Player player = (Player) sender;
        Player onlinePlayers[] = plugin.getServer().getOnlinePlayers();
        
        // command handler
        String cmd = command.getName().toLowerCase();
        if (cmd.equals("item")) {
            // check if enabled
            if (!Nexus.commandConfig.getBoolean("nexus.command.item"))
                return true;
            // check permission
            if (!Nexus.checkPermission("nexus.item", player, true))
                return true;
            // invalid args
            if (args.length < 1 || args.length > 3)
                return false;
            
            // item (id)
            if (args.length == 1) {
                int id = Integer.parseInt(args[0]);
                ItemStack item = new ItemStack(id, 64);
                player.getInventory().addItem(item);
                return true;
            }
            
            // item (id) (qty) -or- item (player) (id)
            if (args.length == 2) {
                // initial variables
                int id;
                ItemStack item;
                
                // item (player) (id)
                String playerName = args[0].toLowerCase();
                id = Integer.parseInt(args[1]);
                item = new ItemStack(id, 64);
                for (Player each : onlinePlayers) {
                    if (each.getName().toLowerCase().contains(playerName)) {
                        each.getInventory().addItem(item);
                        each.sendMessage(ChatColor.GREEN + player.getName() + " gave you item ID " + id + " (x64).");
                        player.sendMessage(ChatColor.GREEN + "Gave " + each.getName() + " item ID " + id + " (x64).");
                        return true;
                    }
                }
                
                // item (id) (qty)
                id = Integer.parseInt(args[0]);
                int qty = Integer.parseInt(args[1]);
                item = new ItemStack(id, qty);
                player.getInventory().addItem(item);
                return true;
            }
            
            // item (player) (id) (qty)
            String playerName = args[0].toLowerCase();
            int id = Integer.parseInt(args[1]);
            int qty = Integer.parseInt(args[2]);
            ItemStack item = new ItemStack(id, qty);
            for (Player each : onlinePlayers) {
                if (each.getName().toLowerCase().contains(playerName)) {
                    each.getInventory().addItem(item);
                    each.sendMessage(ChatColor.GREEN + player.getName() + " gave you item ID " + id + " (x" + qty + ").");
                    player.sendMessage(ChatColor.GREEN + "Gave " + each.getName() + " item ID " + id + " (x" + qty + ").");
                    return true;
                }
            }
            
            // player not found
            player.sendMessage(ChatColor.RED + playerName + " is not online");
            return true;
        }
        
        // end of command
        return false;
    }
}
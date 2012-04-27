package com.vioviocity.nexus.commands;

import com.vioviocity.nexus.Nexus;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class InvCommand implements CommandExecutor{

    private Nexus plugin;
    public InvCommand(Nexus plugin) {
        this.plugin = plugin;
    }
    
    public boolean onCommand(CommandSender sender, Command command, String alias, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Command must be issued within game.");
            return true;
        }
        
        // initialize core variables
        Player player = (Player) sender;
        Player onlinePlayers[] = plugin.getServer().getOnlinePlayers();
        
        // command handler
        String cmd = command.getName().toLowerCase();
        if (cmd.equals("inv")) {
            // invalid args
            if (args.length < 1 || args.length > 2)
                return false;
            
            // inv [clear]
            if (args.length == 1) {
                
                // inv [clear]
                if (args[0].equalsIgnoreCase("clear")) {
                    // check permission
                    if (!Nexus.checkPermission("nexus.inv.clear", player, true))
                        return true;
                
                    player.getInventory().clear();
                    return true;
                }
            }
            
            // inv [clear] (player)
            if (args[0].equalsIgnoreCase("clear") && args.length == 2) {
                // check permission
                if (!Nexus.checkPermission("nexus.inv.clear", player, true))
                    return true;
                
                // initialize variables
                String playerName = args[1];
                    
                // clear inventory
                for (Player each : onlinePlayers) {
                    if (each.getName().toLowerCase().contains(playerName)) {
                        each.getInventory().clear();
                        each.sendMessage(ChatColor.RED + player.getName() + " cleared your inventory.");
                        player.sendMessage(ChatColor.RED + each.getName() + "'s inventory has been cleared.");
                        return true;
                    }
                }
                    
                // player not found
                player.sendMessage(ChatColor.RED + playerName + " is not online.");
                return true;
            }
            
            // inv [see] (player)
            if (args[0].equalsIgnoreCase("see") && args.length == 2) {
                // check permission
                if (!Nexus.checkPermission("nexus.inv.see", player, true))
                    return true;
                
                // initialize variables
                String playerName = args[1];
                
                // see inventory
                for (Player each : onlinePlayers) {
                    if (each.getName().toLowerCase().contains(playerName)) {
                        player.openInventory(each.getInventory());
                        return true;
                    }
                }
                
                // player not found
                player.sendMessage(ChatColor.RED + playerName + " is not online.");
                return true;
            }
        }
        
        // end of command
        return false;
    }
}
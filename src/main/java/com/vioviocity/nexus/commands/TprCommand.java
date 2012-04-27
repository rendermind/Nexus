package com.vioviocity.nexus.commands;

import com.vioviocity.nexus.Nexus;
import java.util.HashMap;
import java.util.Map;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TprCommand implements CommandExecutor{
    
    static public Map<Player,Player> tpRequest = new HashMap<Player,Player>(200);
    
    private Nexus plugin;
    public TprCommand(Nexus plugin) {
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
        if (cmd.equals("tpr")) {
            // check permission
            if (!Nexus.checkPermission("nexus.tpr", player, true))
                return true;
            // invalid args
            if (args.length != 1)
                return false;
            
            // initialize variable
            String tpr = args[0].toLowerCase();
                
            // tpr [accept]
            if (tpr.equals("accept")) {
                
                // check request
                for (Map.Entry<Player,Player> entry : tpRequest.entrySet()) {
                    if (entry.getValue().equals(player)) {
                        entry.getKey().teleport(player);
                        player.sendMessage(ChatColor.GREEN + "Accepted teleport request.");
                        tpRequest.remove(entry.getKey());
                        return true;
                    }
                }
                
                // request not found
                player.sendMessage(ChatColor.RED + "You do not have a pending teleport request.");
                return true;
            }
           
            // tpr [deny]
            if (tpr.equals("deny")) {
                
                // check request
                for (Map.Entry<Player,Player> entry : tpRequest.entrySet()) {
                    if (entry.getValue().equals(player)) {
                        player.sendMessage(ChatColor.RED + "Denied teleport request.");
                        entry.getKey().sendMessage(ChatColor.RED + "Your teleport request has been denied.");
                        tpRequest.remove(entry.getKey());
                        return true;
                    }
                }
                
                // request not found
                player.sendMessage(ChatColor.RED + "You do not have a pending teleport request.");
                return true;
            }
            
            // tpr [cancel]
            if (tpr.equals("cancel")) {
                
                // check request
                if (tpRequest.containsKey(player)) {
                    player.sendMessage(ChatColor.RED + "Cancelled teleport request.");
                    tpRequest.remove(player);
                    return true;
                }
                
                // request not found
                player.sendMessage(ChatColor.RED + "You have not send a teleport request.");
                return true;
            }
            
            // tpr (player)
            for (Player each : onlinePlayers) {
                if (each.getName().toLowerCase().contains(tpr)) {
                    
                    // check toggle
                    if (TpCommand.tpToggle.containsKey(each)) {
                        if (!TpCommand.tpToggle.get(each)) {
                            player.sendMessage(ChatColor.RED + each.getName() + " has teleporting disabled.");
                            return true;
                        }
                    }
                    
                    // create request
                    tpRequest.put(player, each);
                    player.sendMessage(ChatColor.GREEN + "Teleport request send to " + each.getName() + '.');
                    each.sendMessage(ChatColor.RED + "Received teleport request from " + player.getName() + '.');
                    return true;
                }
            }
            
            // player not found
            player.sendMessage(ChatColor.RED + tpr + " is not online.");
            return true;
        }
        
        // end of command
        return false;
    }
}
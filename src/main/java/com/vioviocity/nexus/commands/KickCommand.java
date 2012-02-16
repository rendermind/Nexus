package com.vioviocity.nexus.commands;

import com.vioviocity.nexus.Nexus;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class KickCommand implements CommandExecutor{
    
    private Nexus plugin;
    public KickCommand(Nexus plugin) {
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
        if (cmd.equals("kick")) {
            // check if enabled
            if (!Nexus.commandConfig.getBoolean("nexus.command.kick"))
                return true;
            // check permission
            if (!Nexus.checkPermission("nexus.kick", player, true))
                return true;
            // invalid args
            if (args.length < 1 || args.length > 1)
                return false;
            
            // kick (player)
            if (args.length == 1) {
                String name = args[0].toLowerCase();
                for (Player each : onlinePlayers) {
                    if (each.getName().toLowerCase().contains(name)) {
                        each.kickPlayer("You have been kicked.");
                        plugin.getServer().broadcastMessage(ChatColor.RED + each.getName() + " has been kicked.");
                        return true;
                    }
                }
                
                // player not online
                player.sendMessage(ChatColor.RED + name + " is not online.");
                return true;
            }
        }
        
        // end of command
        return false;
    }
}
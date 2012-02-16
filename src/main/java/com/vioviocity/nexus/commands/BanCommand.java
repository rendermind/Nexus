package com.vioviocity.nexus.commands;

import com.vioviocity.nexus.Nexus;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BanCommand implements CommandExecutor{
    
    private Nexus plugin;
    public BanCommand(Nexus plugin) {
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
        if (cmd.equals("ban")) {
            // check if enabled
            if (!Nexus.commandConfig.getBoolean("nexus.command.ban"))
                return true;
            // check permission
            if (!Nexus.checkPermission("nexus.ban", player, true))
                return true;
            // invalid args
            if (args.length < 1 || args.length > 1)
                return false;
            
            // ban (player)
            if (args.length == 1) {
                String playerName = args[0].toLowerCase();
                for (Player each : onlinePlayers) {
                    if (each.getName().toLowerCase().contains(playerName)) {
                        each.setBanned(true);
                        each.kickPlayer("You have been banned.");
                        plugin.getServer().broadcastMessage(ChatColor.RED + each.getName() + " has been banned.");
                        return true;
                    }
                }
                
                // player not online
                player.sendMessage(ChatColor.RED + playerName + " is not online.");
                return true;
            }
        }
        
        // end of command
        return false;
    }
}
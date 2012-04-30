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
	
	// check if player
	Boolean isPlayer = true;
	if (!(sender instanceof Player))
	    isPlayer = false;
        
        // initialize core variables
	Player player = null;
	if (isPlayer)
	    player = (Player) sender;
        Player onlinePlayers[] = plugin.getServer().getOnlinePlayers();
        
        // command handler
        String cmd = command.getName().toLowerCase();
        if (cmd.equals("kick")) {
            // check permission
	    if (isPlayer)
		if (!Nexus.checkPermission("nexus.kick", player, true))
		    return true;
            // invalid args
            if (args.length < 1)
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
            
            // kick (player) (reason)
            if (args.length > 1) {
                String name = args[0].toLowerCase();
                String reason = "";
                for (int i = 1; i < args.length; i ++)
                    reason += args[i] + ' ';
                reason = reason.substring(0, reason.length() - 1);
                for (Player each : onlinePlayers) {
                    if (each.getName().toLowerCase().contains(name)) {
                        each.kickPlayer("You have been kicked.  Reason: " + reason);
                        return true;
                    }
                }
            }
        }
        
        // end of command
        return false;
    }
}
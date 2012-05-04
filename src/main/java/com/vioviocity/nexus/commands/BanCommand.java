package com.vioviocity.nexus.commands;

import com.vioviocity.nexus.Nexus;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
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
        if (cmd.equals("ban")) {
            // check permission
	    if (isPlayer)
		if (!Nexus.checkPermission("nexus.ban", player, true))
		    return true;
            // invalid args
            if (args.length < 1)
                return false;
            
            // ban (player)
            if (args.length == 1) {
                String playerName = args[0].toLowerCase();
                
		// search online players
		for (Player each : onlinePlayers) {
                    if (each.getName().toLowerCase().contains(playerName)) {
                        each.setBanned(true);
                        each.kickPlayer("You have been banned.");
                        plugin.getServer().broadcastMessage(ChatColor.RED + each.getName() + " has been banned.");
                        return true;
                    }
                }
		
		// search offline players
		for (OfflinePlayer each : plugin.getServer().getOfflinePlayers()) {
		    if (each.getName().toLowerCase().contains(playerName)) {
			each.setBanned(true);
			plugin.getServer().broadcastMessage(ChatColor.RED + each.getName() + " offline player has been banned.");
			return true;
		    }
		}
                
                // player not online
                player.sendMessage(ChatColor.RED + playerName + " is not found.");
                return true;
            }
            
            // ban (player) (reason)
            if (args.length > 1) {
                String name = args[0].toLowerCase();
                String reason = "";
                for (int i = 1; i < args.length; i ++)
                    reason += args[i] + ' ';
                reason = reason.substring(0, reason.length() - 1);
                for (Player each : onlinePlayers) {
                    if (each.getName().toLowerCase().contains(name)) {
                        each.setBanned(true);
                        each.kickPlayer("You have been banned.  Reason: " + reason);
                        return true;
                    }
                }
            }
        }
        
        // end of command
        return false;
    }
}
package com.vioviocity.nexus.commands;

import com.vioviocity.nexus.Nexus;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class UnbanCommand implements CommandExecutor{
    
    private Nexus plugin;
    public UnbanCommand(Nexus plugin) {
        this.plugin = plugin;
    }

    public boolean onCommand(CommandSender sender, Command command, String alias, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Command must be issued within game.");
            return true;
        }
        
        // initialize core variables
        Player player = (Player) sender;
        OfflinePlayer offlinePlayers[] = plugin.getServer().getOfflinePlayers();
        
        // command handler
        String cmd = command.getName().toLowerCase();
        if (cmd.equals("unban")) {
            // check permission
            if (!Nexus.checkPermission("nexus.ban", player, true))
                return true;
            // invalid args
            if (args.length < 1 || args.length > 1)
                return false;
            
            // unban (player)
            String playerName = args[0].toLowerCase();
            for (OfflinePlayer each : offlinePlayers) {
                if (each.getName().contains(playerName)) {
                    
                    // player not banned
                    if (!each.isBanned()) {
                        player.sendMessage(ChatColor.RED + each.getName() + " is not banned.");
                        return true;
                    }
                    
                    // unban player
                    each.setBanned(false);
                    plugin.getServer().broadcastMessage(ChatColor.GREEN + each.getName() + " has been unbanned.");
                    return true;
                }
            }
            
            // player not found
            player.sendMessage(ChatColor.RED + playerName + " is not banned.");
            return true;
        }
        
        // end of command
        return false;
    }
}
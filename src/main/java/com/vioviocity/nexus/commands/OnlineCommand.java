package com.vioviocity.nexus.commands;

import com.vioviocity.nexus.Nexus;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class OnlineCommand implements CommandExecutor {

    private Nexus plugin;
    public OnlineCommand(Nexus plugin) {
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
        if (cmd.equals("online")) {
            // check if enabled
            if (!Nexus.commandConfig.getBoolean("nexus.command.online"))
                return true;
            // check permission
            if (!Nexus.checkPermission("nexus.online", player))
                return true;
            // invalid args
            if (args.length > 0)
                return false;
            
            String playerList = "";
            for (Player each : onlinePlayers) {
                playerList += each.getName() + ", ";
            }
            playerList = playerList.substring(0, playerList.length() - 2);
            player.sendMessage(ChatColor.GREEN + "Online Players [" + onlinePlayers.length + '/' +
                    plugin.getServer().getMaxPlayers() + "]: " + ChatColor.WHITE + playerList);
            return true;
        }
        
        // end of command
        return false;
    }
}
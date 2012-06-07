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
	// check if player
	Boolean isPlayer = true;
	if (!(sender instanceof Player))
	    isPlayer = false;
        
        // initialize variables
	Player player = null;
	if (isPlayer)
	    player = (Player) sender;
        
        // command handler
        String cmd = command.getName().toLowerCase();
        if (cmd.equals("online")) {
	    
            // check permission
	    if (isPlayer)
		if (!Nexus.checkPermission("nexus.online", player, true))
		    return true;
	    
            // invalid args
            if (args.length > 0)
                return false;
            
	    // display online players
            String playerList = "";
            for (Player each : plugin.getServer().getOnlinePlayers())
                playerList += each.getName() + ", ";
            if (plugin.getServer().getOnlinePlayers().length != 0)
                playerList = playerList.substring(0, playerList.length() - 2);
            else
                playerList = "";
            sender.sendMessage(ChatColor.GREEN + "Online Players [" + plugin.getServer().getOnlinePlayers().length + '/' +
                    plugin.getServer().getMaxPlayers() + "]: " + ChatColor.WHITE + playerList);
            return true;
        }
        
        // end of command
        return false;
    }
}
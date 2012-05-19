package com.vioviocity.nexus.commands;

import com.vioviocity.nexus.Nexus;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ModeCommand implements CommandExecutor{
    
    private Nexus plugin;
    public ModeCommand(Nexus plugin) {
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
        if (cmd.equals("mode")) {
	    
            // invalid args
            if (args.length > 1)
                return false;
            
            // <command>
	    if (isPlayer) {
		if (args.length == 0) {
		    
		    // check permission
		    if (!Nexus.checkPermission("nexus.mode", player, true))
			return true;
		
		    // change mode
		    if (player.getGameMode() == GameMode.SURVIVAL) {
			player.setGameMode(GameMode.CREATIVE);
		    } else {
			player.setGameMode(GameMode.SURVIVAL);
		    }
		    return true;
		}
            }
            
            // <command> (player)
            if (args.length == 1) {
		
		// check permission
		if (isPlayer)
		    if (!Nexus.checkPermission("nexus.mode.others", player, true))
			return true;
		
		// change mode
                String playerName = args[0].toLowerCase();
                for (Player each : plugin.getServer().getOnlinePlayers()) {
                    if (each.getName().toLowerCase().contains(playerName)) {
                        if (each.getGameMode() == GameMode.SURVIVAL) {
                            each.setGameMode(GameMode.CREATIVE);
                            sender.sendMessage(ChatColor.GREEN + each.getName() + " game mode set to creative.");
                        } else {
                            each.setGameMode(GameMode.SURVIVAL);
                            sender.sendMessage(ChatColor.GREEN + each.getName() + " game mode set to survival.");
                        }
                        return true;
                    }
                }
                
                // player not online
                sender.sendMessage(ChatColor.RED + "Player is not online.");
                return true;
            }
        }
        
        // end of command
        return false;
    }
}
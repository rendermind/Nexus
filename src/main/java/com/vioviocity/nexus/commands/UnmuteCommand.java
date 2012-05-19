package com.vioviocity.nexus.commands;

import com.vioviocity.nexus.Nexus;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class UnmuteCommand implements CommandExecutor{
    
    private Nexus plugin;
    public UnmuteCommand(Nexus plugin) {
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
        
        // command handler
        String cmd = command.getName().toLowerCase();
        if (cmd.equals("unmute")) {
            // check permission
	    if (isPlayer)
		if (!Nexus.checkPermission("nexus.mute", player, true))
		    return true;
            // invalid args
            if (args.length < 1 || args.length > 1)
                return false;
            
            // unmute (player)
            if (args.length == 1) {
                String playerName = args[0];
                for (Player each : plugin.getServer().getOnlinePlayers()) {
                    if (each.getName().toLowerCase().contains(playerName)) {
                        
                        // check mute
                        if (!MuteCommand.msgMute.contains(each)) {
                            sender.sendMessage(ChatColor.RED + each.getName() + " is not muted.");
                            return true;
                        }
                        
                        // unmute player
                        if (MuteCommand.msgMute.contains(each)) {
                            each.sendMessage(ChatColor.GREEN + "You are now unmuted.");
                            sender.sendMessage(ChatColor.GREEN + each.getName() + " is now unmuted.");
                            MuteCommand.msgMute.remove(each);
                            return true;
                        }
                    }
                }
                
                // player not found
                sender.sendMessage(ChatColor.RED + "Player is not online.");
            }
        }
        
        // end of command
        return false;
    }
}
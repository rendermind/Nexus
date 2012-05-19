package com.vioviocity.nexus.commands;

import com.vioviocity.nexus.Nexus;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class KillCommand implements CommandExecutor{
    
    private Nexus plugin;
    public KillCommand(Nexus plugin) {
        this.plugin = plugin;
    }

    public boolean onCommand(CommandSender sender, Command command, String alias, String[] args) {
	Boolean isPlayer = true;
	if (!(sender instanceof Player))
	    isPlayer = false;
        
        // initialize core variables
	Player player = null;
        if (isPlayer)
	    player = (Player) sender;
        
        // command handler
        String cmd = command.getName().toLowerCase();
        if (cmd.equals("kill")) {
            // check permission
	    if (isPlayer)
		if (!Nexus.checkPermission("nexus.kill", player, true))
		    return true;
            // invalid args
            if (args.length > 1)
                return false;
            
            // kill (no args)
	    if (isPlayer) {
		if (args.length == 0) {
		    player.setHealth(0);
		    return true;
		}
            }
            
            // kill (player)
            if (args.length == 1) {
                String playerName = args[0].toLowerCase();
                for (Player each : plugin.getServer().getOnlinePlayers()) {
                    if (each.getName().toLowerCase().contains(playerName)) {
                        each.setHealth(0);
                        each.sendMessage(ChatColor.RED + sender.getName() + " killed you.");
			sender.sendMessage(ChatColor.RED + "You killed " + each.getName() + ".");
                        return true;
                    }
                }
                
                // player not found
                sender.sendMessage(ChatColor.RED + "Player is not online.");
                return true;
            }
        }
        
        // end of command
        return false;
    }
}
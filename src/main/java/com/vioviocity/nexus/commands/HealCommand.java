package com.vioviocity.nexus.commands;

import com.vioviocity.nexus.Nexus;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class HealCommand implements CommandExecutor{
    
    private Nexus plugin;
    public HealCommand(Nexus plugin) {
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
        if (cmd.equals("heal")) {
	    // check permission
	    if (isPlayer)
		if (!Nexus.checkPermission("nexus.heal", player, true))
		    return true;
            // invalid args
            if (args.length > 1)
                return false;
            
            // heal (no args)
	    if (isPlayer) {
		if (args.length == 0) {
		    player.setHealth(player.getMaxHealth());
		    player.setFoodLevel(20);
		    return true;
		}
	    }
            
            // heal (player)
            if (args.length == 1) {
                String playerName = args[0].toLowerCase();
                for (Player each : plugin.getServer().getOnlinePlayers()) {
                    if (each.getName().toLowerCase().contains(playerName)) {
                        each.setHealth(each.getMaxHealth());
                        each.setFoodLevel(20);
                        each.sendMessage(ChatColor.GREEN + sender.getName() + " healed you.");
			sender.sendMessage(ChatColor.GREEN + "You healed " + each.getName() + ".");
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
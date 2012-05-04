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
        if (!(sender instanceof Player)) {
            sender.sendMessage("[Nexus] Command must be issued within game.");
            return true;
        }
        
        // initialize core variables
        Player player = (Player) sender;
        Player onlinePlayers[] = plugin.getServer().getOnlinePlayers();
        
        // command handler
        String cmd = command.getName().toLowerCase();
        if (cmd.equals("heal")) {
	    // check permission
            if (!Nexus.checkPermission("nexus.heal", player, true))
                return true;
            // invalid args
            if (args.length > 1)
                return false;
            
            // heal (no args)
            if (args.length == 0) {
                player.setHealth(player.getMaxHealth());
                player.setFoodLevel(20);
                return true;
            }
            
            // heal (player)
            if (args.length == 1) {
                String playerName = args[0].toLowerCase();
                for (Player each : onlinePlayers) {
                    if (each.getName().toLowerCase().contains(playerName)) {
                        each.setHealth(each.getMaxHealth());
                        each.setFoodLevel(20);
                        each.sendMessage(ChatColor.GREEN + player.getName() + " healed you.");
                        player.sendMessage(ChatColor.GREEN + "You healed " + each.getName() + '.');
                        return true;
                    }
                }
                
                // player not found
                player.sendMessage(ChatColor.RED + "Player is not online.");
                return true;
            }
        }
        
        // end of command
        return false;
    }
}
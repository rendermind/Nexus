package com.vioviocity.nexus.commands;

import com.vioviocity.nexus.Nexus;
import org.bukkit.ChatColor;
import org.bukkit.World;
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
        if (!(sender instanceof Player)) {
            sender.sendMessage("[Nexus] Command must be issued within game.");
            return true;
        }
        
        // initialize core variables
        Player player = (Player) sender;
        Player onlinePlayers[] = plugin.getServer().getOnlinePlayers();
        
        // command handler
        String cmd = command.getName().toLowerCase();
        if (cmd.equals("kill")) {
            // check permission
            if (!Nexus.checkPermission("nexus.kill", player, true))
                return true;
            // invalid args
            if (args.length > 1)
                return false;
            
            // kill (no args)
            if (args.length == 0) {
                player.setHealth(0);
                return true;
            }
            
            // kill (player)
            if (args.length == 1) {
                String playerName = args[0].toLowerCase();
                for (Player each : onlinePlayers) {
                    if (each.getName().toLowerCase().contains(playerName)) {
                        each.setHealth(0);
                        each.sendMessage(ChatColor.RED + player.getName() + " killed you.");
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
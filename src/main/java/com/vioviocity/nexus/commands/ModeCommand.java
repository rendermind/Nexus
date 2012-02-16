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
        if (!(sender instanceof Player))
            return true;
        
        // initialize core variables
        Player player = (Player) sender;
        Player onlinePlayers[] = plugin.getServer().getOnlinePlayers();
        
        // command handler
        String cmd = command.getName().toLowerCase();
        if (cmd.equals("mode")) {
            // check if enabled
            if (!Nexus.commandConfig.getBoolean("nexus.command.mode"))
                return true;
            if (!Nexus.checkPermission("nexus.mode", player, true))
                return true;
            // invalid args
            if (args.length > 1)
                return false;
            
            // mode (no args)
            if (args.length == 0) {
                if (player.getGameMode() == GameMode.SURVIVAL) {
                    player.setGameMode(GameMode.CREATIVE);
                } else {
                    player.setGameMode(GameMode.SURVIVAL);
                }
                return true;
            }
            
            // mode (player)
            if (args.length == 1) {
                String playerName = args[0].toLowerCase();
                for (Player each : onlinePlayers) {
                    if (each.getName().toLowerCase().contains(playerName)) {
                        if (each.getGameMode() == GameMode.SURVIVAL) {
                            each.setGameMode(GameMode.CREATIVE);
                            player.sendMessage(ChatColor.GREEN + each.getName() + " game mode set to creative.");
                        } else {
                            each.setGameMode(GameMode.SURVIVAL);
                            player.sendMessage(ChatColor.GREEN + each.getName() + " game mode set to survival.");
                        }
                        return true;
                    }
                }
                
                // player not online
                player.sendMessage(ChatColor.RED + playerName + " is not online.");
                return true;
            }
        }
        
        // end of command
        return false;
    }
}
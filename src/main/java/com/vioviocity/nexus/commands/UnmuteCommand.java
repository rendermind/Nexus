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
        if (!(sender instanceof Player)) {
            sender.sendMessage("Command must be issued within game.");
            return true;
        }
        
        // initialize core variables
        Player player = (Player) sender;
        Player onlinePlayers[] = plugin.getServer().getOnlinePlayers();
        
        // command handler
        String cmd = command.getName().toLowerCase();
        if (cmd.equals("unmute")) {
            // check permission
            if (!Nexus.checkPermission("nexus.mute", player, true))
                return true;
            // invalid args
            if (args.length < 1 || args.length > 1)
                return false;
            
            // unmute (player)
            if (args.length == 1) {
                String playerName = args[0];
                for (Player each : onlinePlayers) {
                    if (each.getName().toLowerCase().contains(playerName)) {
                        
                        // check mute
                        if (!MuteCommand.msgMute.contains(each)) {
                            player.sendMessage(ChatColor.RED + each.getName() + " is not muted.");
                            return true;
                        }
                        
                        // unmute player
                        if (MuteCommand.msgMute.contains(each)) {
                            each.sendMessage(ChatColor.GREEN + "You are now unmuted.");
                            player.sendMessage(ChatColor.GREEN + each.getName() + " is now unmuted.");
                            MuteCommand.msgMute.remove(each);
                            return true;
                        }
                    }
                }
                
                // player not found
                player.sendMessage(ChatColor.RED + playerName + " is not online.");
            }
        }
        
        // end of command
        return false;
    }
}
package com.vioviocity.nexus.commands;

import com.vioviocity.nexus.Nexus;
import java.util.HashMap;
import java.util.Map;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class UnmuteCommand implements CommandExecutor{
    
    static public Map<Player,Boolean> msgMute = new HashMap<Player,Boolean>(200);
    
    private Nexus plugin;
    public UnmuteCommand(Nexus plugin) {
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
        if (cmd.equals("unmute")) {
            // check if enabled
            if (!Nexus.commandConfig.getBoolean("nexus.command.mute"))
                return true;
            // check permission
            if (!Nexus.checkPermission("nexus.mute", player))
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
                        if (!msgMute.containsKey(each)) {
                            player.sendMessage(ChatColor.RED + each.getName() + " is not muted.");
                            return true;
                        }
                        
                        // unmute player
                        if (msgMute.get(each)) {
                            each.sendMessage(ChatColor.GREEN + "You are now unmuted.");
                            player.sendMessage(ChatColor.GREEN + each.getName() + " is now unmuted.");
                            msgMute.remove(each);
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
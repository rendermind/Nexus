package com.vioviocity.nexus.commands;

import com.vioviocity.nexus.Nexus;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MuteCommand implements CommandExecutor{
    
    static public List<Player> msgMute = new ArrayList<Player>();
    
    private Nexus plugin;
    public MuteCommand(Nexus plugin) {
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
        if (cmd.equals("mute")) {
            // check permission
            if (!Nexus.checkPermission("nexus.mute", player, true))
                return true;
            // invalid args
            if (args.length < 1 || args.length > 1)
                return false;
            
            //mute (player)
            if (args.length == 1) {
                String playerName = args[0];
                for (Player each : onlinePlayers) {
                    if (each.getName().toLowerCase().contains(playerName)) {
                        
                        //check mute
                        if (msgMute.contains(each)) {
                            player.sendMessage(ChatColor.RED + each.getName() + " is already muted.");
                            return true;
                        }
                        
                        // mute player
                        msgMute.add(each);
                        each.sendMessage(ChatColor.RED + "You are now muted.");
                        player.sendMessage(ChatColor.RED + each.getName() + " is now muted.");
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
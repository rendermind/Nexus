package com.vioviocity.nexus.commands;

import com.vioviocity.nexus.Nexus;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BroadcastCommand implements CommandExecutor{
    
    private Nexus plugin;
    public BroadcastCommand(Nexus plugin) {
        this.plugin = plugin;
    }

    public boolean onCommand(CommandSender sender, Command command, String alias, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Command must be issued within game.");
            return true;
        }
        
        // initialize core variables
        Player player = (Player) sender;
        
        // command handler
        String cmd = command.getName().toLowerCase();
        if (cmd.equals("broadcast")) {
            // check if enabled
            if (!Nexus.commandConfig.getBoolean("nexus.command.broadcast"))
                return true;
            // check permission
            if (!Nexus.checkPermission("nexus.broadcast", player, true))
                return true;
            // invalid args
            if (args.length == 0)
                return false;
            
            // check mute
            if (MuteCommand.msgMute.contains(player)) {
                player.sendMessage(ChatColor.RED + "You are muted.");
                return true;
            }
            
            //broadcast (message)
            String message = "";
            for (String each : args)
                message += each + ' ';
            message = message.substring(0, message.length() - 1);
            plugin.getServer().broadcastMessage(ChatColor.RED + "[Broadcast] " + ChatColor.WHITE + message);
            return true;
        }
        
        // end of command
        return false;
    }
}
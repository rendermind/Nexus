package com.vioviocity.nexus.commands;

import com.vioviocity.nexus.Nexus;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MsgCommand implements CommandExecutor{
    
    private Nexus plugin;
    public MsgCommand(Nexus plugin) {
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
        if (cmd.equals("msg")) {
            // check if enabled
            if (!Nexus.commandConfig.getBoolean("nexus.command.msg"))
                return true;
            // check permission
            if (!Nexus.checkPermission("nexus.msg", player))
                return true;
            // invalid args
            if (args.length < 2)
                return false;
            
            // check mute
            if (MuteCommand.msgMute.contains(player)) {
                player.sendMessage(ChatColor.RED + "You are muted.");
                return true;
            }
            
            // msg (player) (message)
            String playerName = args[0];
            String message = "";
            for (int i = 1; i < args.length; i ++)
                message += args[i] + ' ';
            message = message.substring(0, message.length() - 1);
            for (Player each : onlinePlayers) {
                if (each.getName().toLowerCase().contains(playerName)) {
                    each.sendMessage(ChatColor.GREEN + "[" + player.getName() + " -> me] " + ChatColor.WHITE + message);
                    player.sendMessage(ChatColor.GREEN + "[me -> " + each.getName() + "] " + ChatColor.WHITE + message);
                    ReplyCommand.msgReply.put(player, each);
                    return true;
                }
            }
            
            // player not found
            player.sendMessage(ChatColor.RED + playerName + " is not online.");
            return true;
        }
        
        // end of command
        return false;
    }
}
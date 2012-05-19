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
        if (!(sender instanceof Player)) {
            sender.sendMessage("[Nexus] Command must be issued within game.");
            return true;
        }
        
        // initialize core variables
        Player player = (Player) sender;
        
        // command handler
        String cmd = command.getName().toLowerCase();
        if (cmd.equals("msg")) {
            // check permission
            if (!Nexus.checkPermission("nexus.msg", player, true))
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
	    for (String each : args)
                message += each + " ";
            message = message.substring(0, message.length() - 1);
            for (Player each : plugin.getServer().getOnlinePlayers()) {
                if (each.getName().toLowerCase().contains(playerName)) {
                    each.sendMessage(ChatColor.GREEN + "[" + player.getName() + " -> me] " + ChatColor.WHITE + message);
                    player.sendMessage(ChatColor.GREEN + "[me -> " + each.getName() + "] " + ChatColor.WHITE + message);
                    ReplyCommand.msgReply.put(player, each);
                    return true;
                }
            }
            
            // player not found
            player.sendMessage(ChatColor.RED + "Player is not online.");
            return true;
        }
        
        // end of command
        return false;
    }
}
package com.vioviocity.nexus.commands;

import com.vioviocity.nexus.Nexus;
import java.util.HashMap;
import java.util.Map;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ReplyCommand implements CommandExecutor{
    
    static public Map<Player,Player> msgReply = new HashMap<Player,Player>(200);
    
    private Nexus plugin;
    public ReplyCommand(Nexus plugin) {
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
        if (cmd.equals("reply")) {
            // check if enabled
            if (!Nexus.commandConfig.getBoolean("nexus.command.msg"))
                return true;
            // check permission
            if (!Nexus.checkPermission("nexus.msg", player))
                return true;
            
            // check mute
            if (MuteCommand.msgMute.containsKey(player)) {
                if (MuteCommand.msgMute.get(player)) {
                    player.sendMessage(ChatColor.RED + "You are muted.");
                    return true;
                }
            }
            
            // reply (no args)
            String message = "";
            for (int i = 0; i < args.length; i ++) {
                message += args[i] + " ";
            }
            message = message.substring(0, message.length() - 1);
            
            // check request
            for (Map.Entry<Player,Player> entry : msgReply.entrySet()) {
                if (entry.getValue().equals(player)) {
                    
                    // check online
                    if (entry.getKey().isOnline() == false) {
                        player.sendMessage(ChatColor.RED + entry.getKey().getName() + " is not online.");
                        return true;
                    }
                    
                    // send message
                    entry.getKey().sendMessage(ChatColor.GREEN + "[" + player.getName() + " -> me] " + ChatColor.WHITE + message);
                    player.sendMessage(ChatColor.GREEN + "[me -> " + entry.getKey().getName() + "] " + ChatColor.WHITE + message);
                    msgReply.put(player, entry.getKey());
                    return true;
                }
            }
            
            // player not found
            player.sendMessage(ChatColor.RED + "You have not received a message.");
            return true;
        }
        
        // end of command
        return false;
    }
}
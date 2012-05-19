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
	// check if player
	Boolean isPlayer = true;
	if (!(sender instanceof Player))
	    isPlayer = false;
        
        // initialize core variables
	Player player = null;
	if (isPlayer)
	    player = (Player) sender;
        
        // command handler
        String cmd = command.getName().toLowerCase();
        if (cmd.equals("mute")) {
	    
            // check permission
	    if (isPlayer)
		if (!Nexus.checkPermission("nexus.mute", player, true))
		    return true;
	    
            // invalid args
            if (args.length < 1 || args.length > 1)
                return false;
            
            // <command> (player)
            if (args.length == 1) {
                String playerName = args[0];
                for (Player each : plugin.getServer().getOnlinePlayers()) {
                    if (each.getName().toLowerCase().contains(playerName)) {
                        
                        //check mute
                        if (msgMute.contains(each)) {
                            sender.sendMessage(ChatColor.RED + each.getName() + " is already muted.");
                            return true;
                        }
                        
                        // mute player
                        msgMute.add(each);
                        each.sendMessage(ChatColor.RED + "You are now muted.");
                        sender.sendMessage(ChatColor.RED + each.getName() + " is now muted.");
                        return true;
                    }
                }
                
                // player not found
                sender.sendMessage(ChatColor.RED + "Player is not online.");
                return true;
            }
        }
        
        // end of command
        return false;
    }
}
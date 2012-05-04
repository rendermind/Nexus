package com.vioviocity.nexus.commands;

import com.vioviocity.nexus.Nexus;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class IpCommand implements CommandExecutor {
    
    private Nexus plugin;
    public IpCommand(Nexus plugin) {
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
	if (cmd.equals("ip")) {
	    // check permission
	    if (!Nexus.checkPermission("nexus.back", player, true))
		return true;
	    // invalid args
	    if (args.length > 1)
		return false;
	    
	    // ip (no args)
	    if (args.length == 0) {
		String ip = player.getAddress().toString();
		ip = ip.substring(1, ip.indexOf(':'));
		player.sendMessage(ChatColor.GREEN + "Your IP address is " + ip + '.');
		return true;
	    }
	    
	    // ip (player)
	    if (args.length == 1) {
		String playerName = args[0].toLowerCase();
		for (Player each : plugin.getServer().getOnlinePlayers()) {
		    if (each.getName().toLowerCase().contains(playerName)) {
			String ip = each.getAddress().toString();
			ip = ip.substring(1, ip.indexOf(':'));
			player.sendMessage(ChatColor.GREEN + "IP address of " + each.getName() + "is" + ip + '.');
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

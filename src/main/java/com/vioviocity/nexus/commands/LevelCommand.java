package com.vioviocity.nexus.commands;

import com.vioviocity.nexus.Nexus;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LevelCommand implements CommandExecutor{
    
    private Nexus plugin;
    public LevelCommand(Nexus plugin) {
        this.plugin = plugin;
    }

    public boolean onCommand(CommandSender sender, Command command, String alias, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("[Nexus] Command must be issued within game.");
            return true;
        }
        
        // initialize variables
        Player player = (Player) sender;
        
        // command handler
        String cmd = command.getName().toLowerCase();
        if (cmd.equals("level")) {
	    
            // invalid args
            if (args.length > 2)
                return false;
            
            // <command>
            if (args.length == 0) {
                // check permission
                if (!Nexus.checkPermission("nexus.level", player, true))
                    return true;
                
                // level display
                int expNext = (int) (7 + (int) (player.getLevel() * 3.5));
                int actualNext = expNext - (int) (player.getExp() * expNext) + player.getTotalExperience();
                player.sendMessage(ChatColor.GREEN + "Level: " + player.getLevel() + ", Exp: " +
                        player.getTotalExperience() + '/' + actualNext);
                return true;
            }
            
            // <command> (level)
            if (args.length == 1) {
		
                // check permission
                if (!Nexus.checkPermission("nexus.level.set", player, true))
                    return true;
                
                // initialize variable
                int level;
                try {
                    level = Integer.parseInt(args[0]);
                } catch (Exception e) {
                    return false;
                }
                
                // check max level
                if (level > 10000) {
                    player.sendMessage(ChatColor.RED + "Maximium level is 10,000.");
                    return true;
                }
                
                // set player level
                player.setLevel(0);
                player.setExp(0);
                player.setTotalExperience(0);
                for (int i = 0; i < level; i ++) {
                    player.setTotalExperience(player.getTotalExperience() + (int) (7 + (int) (player.getLevel() * 3.5)));
                    player.setLevel(player.getLevel() + 1);
                }
                return true;
            }
            
            // <command> (player) (level)
            if (args.length == 2) {
		
                // check permission
                if (!Nexus.checkPermission("nexus.level.set", player, true))
                    return true;
                
                // initialize variables
                String playerName = args[0].toLowerCase();
                int level = Integer.parseInt(args[1]);
                
                // check max level
                if (level > 10000) {
                    player.sendMessage(ChatColor.RED + "Maximium level is 10,000.");
                    return true;
                }
                
                // set player level
                for (Player each : plugin.getServer().getOnlinePlayers()) {
                    if (each.getName().toLowerCase().contains(playerName)) {
                        each.setLevel(0);
                        each.setExp(0);
                        each.setTotalExperience(0);
                        for (int i = 0; i < level; i ++) {
                            each.setTotalExperience(each.getTotalExperience() + (int) (7 + (int) (each.getLevel() * 3.5)));
                            each.setLevel(each.getLevel() + 1);
                        }
                        each.sendMessage(ChatColor.GREEN + player.getName() + " set your level to " + level);
                        player.sendMessage(ChatColor.GREEN + playerName + " set to level " + level);
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
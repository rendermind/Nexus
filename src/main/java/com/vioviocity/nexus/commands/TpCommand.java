package com.vioviocity.nexus.commands;

import com.vioviocity.nexus.Nexus;
import java.util.HashMap;
import java.util.Map;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TpCommand implements CommandExecutor{
    
    static public Map<Player,Boolean> tpToggle = new HashMap<Player,Boolean>(200);
    
    private Nexus plugin;
    public TpCommand(Nexus plugin) {
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
        if (cmd.equals("tp")) {
            // invalid args
            if (args.length < 1 || args.length > 2)
                return false;
            
            // tp [toggle] -or- tp (player)
            if (args.length == 1) {
                
                // tp [toggle]
                if (args[0].equalsIgnoreCase("toggle")) {
                    // check permission
                    if (!Nexus.checkPermission("nexus.tp.toggle", player, true))
                        return true;
                    
                    // toggle true
                    if (tpToggle.containsKey(player)) {
                        if (!tpToggle.get(player)) {
                            tpToggle.put(player, Boolean.TRUE);
                            player.sendMessage(ChatColor.GREEN + "Teleporting has been enabled.");
                            return true;
                        }
                    }
                    
                    // toggle false
                    tpToggle.put(player, Boolean.FALSE);
                    player.sendMessage(ChatColor.RED + "Teleporting has been disabled.");
                    return true;
                }
                
                // check permission
                if (!Nexus.checkPermission("nexus.tp", player, true))
                    return true;
                
                // tp (player)
                String playerName = args[0].toLowerCase();
                for (Player each : onlinePlayers) {
                    if (each.getName().toLowerCase().contains(playerName)) {
                        
                        // check toggle
                        if (tpToggle.containsKey(each)) {
                            if (!tpToggle.get(each)) {
                                player.sendMessage(ChatColor.RED + each.getName() + " has teleporting disabled.");
                                return true;
                            }
                        }
                        
                        // teleport player
                        player.teleport(each);
                        return true;
                    }
                }
                
                // player not found
                player.sendMessage(ChatColor.RED + playerName + " is not online.");
                return true;
            }
            
            // check permission
            if (!Nexus.checkPermission("nexus.tp", player, true))
                return true;
            
            // tp (player) (player)
            if (args.length == 2) {
                String playerNameFrom = args[0].toLowerCase();
                String playerNameTo = args[1].toLowerCase();
                for (Player eachFrom : onlinePlayers) {
                    if (eachFrom.getName().toLowerCase().contains(playerNameFrom)) {
                        for (Player eachTo : onlinePlayers) {
                            if (eachTo.getName().toLowerCase().contains(playerNameTo)) {
                                
                                // check toggle
                                if (tpToggle.containsKey(eachTo)) {
                                    if (!tpToggle.get(eachTo)) {
                                        player.sendMessage(ChatColor.RED + eachTo.getName() + " has teleporting disabled.");
                                        return true;
                                    }
                                }
                                
                                // teleport player
                                eachFrom.teleport(eachTo);
                                player.sendMessage(ChatColor.GREEN + eachFrom.getName() + " was teleported to " +
                                        eachTo.getName() + '.');
                                return true;
                            }
                        }
                    }
                }
                
                // player not found
                player.sendMessage(ChatColor.RED + playerNameFrom + " or " + playerNameTo + " is not online.");
                return true;
            }
        }
        
        // end of command
        return false;
    }
}
package com.vioviocity.nexus.commands;

import com.vioviocity.nexus.Nexus;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class KitCommand implements CommandExecutor{
    
    private Nexus plugin;
    public KitCommand(Nexus plugin) {
        this.plugin = plugin;
    }
    
    public boolean onCommand(CommandSender sender, Command command, String alias, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Command must be issued within game.");
            return true;
        }
        
        // initialize core variables
        Player player = (Player) sender;
        Set <String> kits = Collections.EMPTY_SET;
        List <String> items = Collections.EMPTY_LIST;
        
        // command handler
        String cmd = command.getName().toLowerCase();
        if (cmd.equals("kit")) {
            // check if enabled
            if (!Nexus.commandConfig.getBoolean("nexus.command.kit"))
                return true;
            // check permission
            if (!Nexus.checkPermission("nexus.kit", player, true))
                return true;
            // invalid args
            if (args.length > 1)
                return false;
            
            // load kits
            if (Nexus.kitConfig.isConfigurationSection("nexus.kit"))
                kits = Nexus.kitConfig.getConfigurationSection("nexus.kit").getKeys(false);
            
            // kits not set
            if (kits.isEmpty()) {
                player.sendMessage(ChatColor.RED + "Kits have not been set.");
                return true;
            }
            
            // kit [list], kit (kit)
            if (args.length == 1) {
                
                // kit [list]
                if (args[0].equalsIgnoreCase("list")) {
                    String kitList = "";
                    for (String each : kits)
                        kitList += each + ", ";
                    kitList = kitList.substring(0, kitList.length() - 2);
                    player.sendMessage(ChatColor.GREEN + "Kits: " + ChatColor.WHITE + kitList);
                    return true;
                }
                
                // kit (kit)
                
            }
        }
        
        // end of command
        return false;
    }
}
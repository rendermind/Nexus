package com.vioviocity.nexus;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class NexusCommands implements CommandExecutor {

    // initialize global variables
    static List<String> tpRequest = new ArrayList<String>();
    static List<String> tpToggle = new ArrayList<String>();
    static List<String> tpBack = new ArrayList<String>();
    static List<String> msgReply = new ArrayList<String>();
    static List<String> msgMute = new ArrayList<String>();
    
    private Nexus plugin;
    public NexusCommands(Nexus plugin) {
        this.plugin = plugin;
    }
    
    public boolean onCommand(CommandSender sender, Command cmd, String alias, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Command must be called by player.");
            return true;
        }

        // initialize core variables
        Player player = (Player) sender;
        World world = player.getWorld();
        Player onlinePlayers[];
        OfflinePlayer bannedPlayers[];
        onlinePlayers = plugin.getServer().getOnlinePlayers();
        bannedPlayers = plugin.getServer().getOfflinePlayers();

        
        if (cmd.getName().equalsIgnoreCase("test")) {
            if (args.length == 1) {
                //int id = Integer.parseInt(args[0]);
                String item = args[0].toLowerCase();
                player.sendMessage(ChatColor.GOLD + "-> " + Nexus.itemList.get(item));
                return true;
            }
            
            return false;
        }
        
        // ----- BACK -----
        if (cmd.getName().equalsIgnoreCase("back") && Nexus.commandConfig.getBoolean("nexus.command.back")) {
            // permission check
            if (!checkPermission("nexus.back", player))
                return true;
            
            // invalid args
            if (args.length > 0)
                return false;
            
            String tpName;
            Location tpBackLocation = player.getLocation();
            //Location tpTempLocation = player.getLocation();
            for (String each: tpBack) {
                tpName = each.substring(0, each.indexOf(','));
                if (player.getName().equalsIgnoreCase(tpName)) {
                    String worldName = each.substring(each.indexOf("map:") + 4, each.indexOf("x:"));
                    double x = Double.parseDouble(each.substring(each.indexOf("x:") + 2, each.indexOf("y:")));
                    double y = Double.parseDouble(each.substring(each.indexOf("y:") + 2, each.indexOf("z:")));
                    double z = Double.parseDouble(each.substring(each.indexOf("z:") + 2, each.indexOf("yaw:")));
                    float yaw = Float.parseFloat(each.substring(each.indexOf("yaw:") + 4, each.indexOf("pitch:")));
                    float pitch = Float.parseFloat(each.substring(each.indexOf("pitch:") + 6));
                    tpBackLocation.setX(x);
                    tpBackLocation.setY(y);
                    tpBackLocation.setZ(z);
                    tpBackLocation.setYaw(yaw);
                    tpBackLocation.setPitch(pitch);
                    for (World each2: plugin.getServer().getWorlds()) {
                        if (each2.getName().equals(worldName)) {
                            tpBackLocation.setWorld(each2);
                        }
                    }
                    player.teleport(tpBackLocation);
                    return true;
                }
            }
            
            // no tp back
            player.sendMessage(ChatColor.RED + "You do not have a prior location.");
            return true;
        }
        
        // end of commands
        return false;
    }
    
    static boolean checkPermission(String permission, Player player) {
        if (!player.hasPermission(permission)) {
            player.sendMessage(ChatColor.RED + "You do not have permission.");
            return false;
        } else {
            return true;
        }
    }
    
    private boolean checkTpToggle(Player player) {
        for (String each : tpToggle) {
            String tpName = each.substring(0, each.indexOf(','));
            String tpAble = each.substring(each.indexOf(',') + 1);
            if (player.getName().equalsIgnoreCase(tpName)) {
                if (tpAble.equals("false")) {
                    return false;
                }
            }
        }
        
        return true;
    }
    
    private boolean checkMsgMute(String player) {
        for (String each : msgMute) {
            if (each.toLowerCase().contains(player.toLowerCase()))
                return true;
        }
        return false;
    }
    
    private double round(double value) {
        DecimalFormat newFormat = new DecimalFormat("#.#");
        return Double.valueOf(newFormat.format(value));
    }
}
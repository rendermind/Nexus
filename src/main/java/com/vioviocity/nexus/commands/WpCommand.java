package com.vioviocity.nexus.commands;

import com.vioviocity.nexus.Nexus;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class WpCommand implements CommandExecutor {
    
    private Nexus plugin;
    public WpCommand(Nexus plugin) {
        this.plugin = plugin;
    }
    
    public boolean onCommand(CommandSender sender, Command command, String alias, String[] args) {
        if (!(sender instanceof Player))
            return true;
        
        // initialize core variables
        Player player = (Player) sender;
        
        // command handler
        String cmd = command.getName().toLowerCase();
        if (cmd.equals("wp")) {
            // check if enabled
            if (!Nexus.commandConfig.getBoolean("nexus.command.wp"))
                return true;
            // invalid args
            if (args.length > 2)
                return false;
            
            // wp [list], wp (player)
            if (args.length == 1) {
                // check permission
                if (!Nexus.checkPermission("nexus.wp", player))
                    return true;
                
                // wp [list]
                if (args[0].equalsIgnoreCase("list")) {
                    List <String> waypoints = Nexus.waypointConfig.getStringList("nexus.waypoint.list");
                    
                    // waypoints not set
                    if (waypoints.isEmpty()) {
                        player.sendMessage(ChatColor.RED + "Waypoints have not been set.");
                        return true;
                    }
                    
                    // display list of waypoints
                    String waypointList = "";
                    for (String each : waypoints)
                        waypointList += each + ", ";
                    waypointList = waypointList.substring(0, waypointList.length() - 2);
                    player.sendMessage(ChatColor.GREEN + "Waypoints: " + ChatColor.WHITE + waypointList);
                    return true;
                }
                
                // initialize variables
                String waypointName = args[0].toLowerCase();
                List <String> waypoints = Nexus.waypointConfig.getStringList("nexus.waypoint.list");
                String path = "nexus.waypoint." + waypointName + '.';
                
                // wp (waypoint)
                for (String each : waypoints) {
                    if (waypointName.equalsIgnoreCase(each)) {
                        
                        // initialize variables
                        Location waypoint = player.getLocation();
                        
                        // teleport to waypoint
                        waypoint.setWorld(plugin.getServer().getWorld(Nexus.waypointConfig.getString(path + "world")));
                        waypoint.setX(Nexus.waypointConfig.getDouble(path + "x"));
                        waypoint.setY(Nexus.waypointConfig.getDouble(path + "y"));
                        waypoint.setZ(Nexus.waypointConfig.getDouble(path + "z"));
                        waypoint.setYaw((float) Nexus.waypointConfig.getDouble(path + "yaw"));
                        waypoint.setPitch((float) Nexus.waypointConfig.getDouble(path + "pitch"));
                        player.teleport(waypoint);
                        return true;
                    }
                }
                
                // waypoint not found
                player.sendMessage(ChatColor.RED + "Waypoint does not exist.");
                return true;
            }
            
            // wp (waypoint) [set|delete]
            if (args.length == 2) {
                // check permission
                if (!Nexus.checkPermission("nexus.wp.set", player))
                    return true;
                
                // initialize variables
                String waypointName = args[0];
                
                // wp (waypoint) [set]
                if (args[1].equalsIgnoreCase("set")) {
                    
                    // initialize variables
                    List <String> waypoints = Nexus.waypointConfig.getStringList("nexus.waypoint.list");
                    String path = "nexus.waypoint." + waypointName.toLowerCase() + '.';
                    
                    // check waypoint
                    for (String each : waypoints) {
                        if (waypointName.equalsIgnoreCase(each)) {
                            
                            // save waypoint
                            waypoints.set(waypoints.indexOf(each), waypointName);
                            Nexus.waypointConfig.set("nexus.waypoint.list", waypoints);
                            Nexus.waypointConfig.set(path + "world", player.getLocation().getWorld().getName());
                            Nexus.waypointConfig.set(path + "x", player.getLocation().getX());
                            Nexus.waypointConfig.set(path + "y", player.getLocation().getY());
                            Nexus.waypointConfig.set(path + "z", player.getLocation().getZ());
                            Nexus.waypointConfig.set(path + "yaw", player.getLocation().getYaw());
                            Nexus.waypointConfig.set(path + "pitch", player.getLocation().getPitch());
                            Nexus.saveWaypointConfig();
                            player.sendMessage(ChatColor.GREEN + "Waypoint " + waypointName + " reset.");
                            return true;
                        }
                    }
                    
                    // create waypoint
                    waypoints.add(waypointName);
                    Nexus.waypointConfig.set("nexus.waypoint.list", waypoints);
                    Nexus.waypointConfig.set(path + "world", player.getLocation().getWorld().getName());
                    Nexus.waypointConfig.set(path + "x", player.getLocation().getX());
                    Nexus.waypointConfig.set(path + "y", player.getLocation().getY());
                    Nexus.waypointConfig.set(path + "z", player.getLocation().getZ());
                    Nexus.waypointConfig.set(path + "yaw", player.getLocation().getYaw());
                    Nexus.waypointConfig.set(path + "pitch", player.getLocation().getPitch());
                    Nexus.saveWaypointConfig();
                    player.sendMessage(ChatColor.GREEN + "Waypoint " + waypointName + " set.");
                    return true;
                }
                
                // wp (waypoint) [delete]
                if (args[1].equalsIgnoreCase("delete")) {
                    
                    // initialize variables
                    List <String> waypoints = Nexus.waypointConfig.getStringList("nexus.waypoint.list");
                    
                    // check waypoint
                    for (String each : waypoints) {
                        if (waypointName.equalsIgnoreCase(each)) {
                            
                            // delete waypoint
                            waypoints.remove(each);
                            Nexus.waypointConfig.set("nexus.waypoint.list", waypoints);
                            Nexus.saveWaypointConfig();
                            player.sendMessage(ChatColor.RED + "Waypoint " + each + " deleted.");
                            return true;
                        }
                    }
                    
                    // waypoint not found
                    player.sendMessage(ChatColor.RED + "Waypoint does not exist.");
                    return true;
                }
            }
        }
        
        // end of command
        return false;
    }
}
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
        
        // ----- WP -----
        if (cmd.getName().equalsIgnoreCase("wp") && Nexus.commandConfig.getBoolean("nexus.command.wp")) {
            // wp (waypoint), wp [list]
            if (args.length == 1) {
                // permission check
                if (!checkPermission("nexus.wp", player))
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
                    for (String each : waypoints) {
                        waypointList += each + ", ";
                    }
                    waypointList = waypointList.substring(0, waypointList.length() - 2);
                    player.sendMessage(ChatColor.GREEN + "Waypoints: " + ChatColor.WHITE + waypointList);
                    return true;
                    
                // wp (waypoint)
                } else {
                    String wpName = args[0].toLowerCase();
                    List <String> waypoints = Nexus.waypointConfig.getStringList("nexus.waypoint.list");
                    
                    // teleport to waypoint
                    for (String each : waypoints) {
                        if (wpName.equalsIgnoreCase(each)) {
                            String path = "nexus.waypoint." + wpName + ".";
                            Location waypoint = player.getLocation();
                            
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
            } else if (args.length == 2) {
                // permission check
                if (!checkPermission("nexus.wp.set", player))
                    return true;
                
                String wpName = args[0];
                
                // wp (waypoint) [set]
                if (args[1].equalsIgnoreCase("set")) {
                    List <String> waypoints = Nexus.waypointConfig.getStringList("nexus.waypoint.list");
                    
                    // check if waypoint exists
                    for (String each : waypoints) {
                        if (wpName.equalsIgnoreCase(each)) {
                            waypoints.set(waypoints.indexOf(each), wpName);
                            Nexus.waypointConfig.set("nexus.waypoint.list", waypoints);
                            String path = "nexus.waypoint." + wpName.toLowerCase() + ".";
                            Nexus.waypointConfig.set(path + "world", player.getLocation().getWorld().getName());
                            Nexus.waypointConfig.set(path + "x", player.getLocation().getX());
                            Nexus.waypointConfig.set(path + "y", player.getLocation().getY());
                            Nexus.waypointConfig.set(path + "z", player.getLocation().getZ());
                            Nexus.waypointConfig.set(path + "yaw", player.getLocation().getYaw());
                            Nexus.waypointConfig.set(path + "pitch", player.getLocation().getPitch());
                            Nexus.saveWaypointConfig();
                            player.sendMessage(ChatColor.GREEN + "Waypoint " + wpName + " reset.");
                            return true;
                        }
                    }
                    
                    // create new waypoint
                    waypoints.add(wpName);
                    Nexus.waypointConfig.set("nexus.waypoint.list", waypoints);
                    String path = "nexus.waypoint." + wpName.toLowerCase() + ".";
                    Nexus.waypointConfig.set(path + "world", player.getLocation().getWorld().getName());
                    Nexus.waypointConfig.set(path + "x", player.getLocation().getX());
                    Nexus.waypointConfig.set(path + "y", player.getLocation().getY());
                    Nexus.waypointConfig.set(path + "z", player.getLocation().getZ());
                    Nexus.waypointConfig.set(path + "yaw", player.getLocation().getYaw());
                    Nexus.waypointConfig.set(path + "pitch", player.getLocation().getPitch());
                    Nexus.saveWaypointConfig();
                    player.sendMessage(ChatColor.GREEN + "Waypoint " + wpName + " set.");
                    return true;
                    
                // wp (waypoint) [delete]
                } else if (args[1].equalsIgnoreCase("delete")) {
                    List <String> waypoints = Nexus.waypointConfig.getStringList("nexus.waypoint.list");
                    
                    for (String each : waypoints) {
                        if (wpName.equalsIgnoreCase(each)) {
                            waypoints.remove(each);
                            Nexus.waypointConfig.set("nexus.waypoint.list", waypoints);
                            Nexus.saveWaypointConfig();
                            player.sendMessage(ChatColor.RED + "Waypoint " + each + " deleted.");
                            return true;
                        }
                    }
                    
                    // waypoint no found
                    player.sendMessage(ChatColor.RED + "Waypoint does not exist.");
                    return true;
                }
                
                return false;
                
            // invalid args
            } else {
                return false;
            }
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
        
        // ----- LEVEL -----
        if (cmd.getName().equalsIgnoreCase("level") && Nexus.commandConfig.getBoolean("nexus.command.level")) {
            // level (no args)
            if (args.length == 0) {
                // permission check
                if (!checkPermission("nexus.level", player))
                    return true;
                
                int expNext = (int) (7 + (int) (player.getLevel() * 3.5));
                int actualNext = expNext - (int) (player.getExp() * expNext) + player.getTotalExperience();
                player.sendMessage(ChatColor.GREEN + "Level: " + player.getLevel() + ", Exp: " +
                        player.getTotalExperience() + "/" + actualNext);
            
            // level (level)
            } else if (args.length == 1) {
                // permission check
                if (!checkPermission("nexus.level.set", player))
                    return true;
                int level = Integer.parseInt(args[0]);
                
                // check max level
                if (level > 10000) {
                    player.sendMessage(ChatColor.RED + "Maxiumum level is 10,000.");
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
                
            // level (player) (level)
            } else if (args.length == 2) {
                // permission check
                if (!checkPermission("nexus.level.set", player))
                    return true;
                
                String levelName = args[0].toLowerCase();
                int level = Integer.parseInt(args[1]);
                
                // check max level
                if (level > 10000) {
                    player.sendMessage(ChatColor.RED + "Maximium level is 10,000.");
                    return true;
                }
                
                for (Player each : onlinePlayers) {
                    if (each.getName().toLowerCase().contains(levelName)) {
                        
                        // set player level
                        each.setLevel(0);
                        each.setExp(0);
                        each.setTotalExperience(0);
                        for (int i = 0; i < level; i ++) {
                            each.setTotalExperience(each.getTotalExperience() + (int) (7 + (int) (each.getLevel() * 3.5)));
                            each.setLevel(each.getLevel() + 1);
                        }
                        
                        each.sendMessage(ChatColor.GREEN + player.getName() + " has set your level to " + level);
                        player.sendMessage(ChatColor.GREEN + levelName + " set to level " + level);
                        return true;
                    }
                }
                
                // player not found
                player.sendMessage(ChatColor.RED + levelName + " is not online.");
                
            // invalid args
            } else {
                return false;
            }
            
            return true;
        }
        
        // ----- MSG -----
        if (cmd.getName().equalsIgnoreCase("msg") && Nexus.commandConfig.getBoolean("nexus.command.msg")) {
            // permission check
            if (!checkPermission("nexus.msg", player))
                return true;
            
            // invalid args
            if (args.length == 0)
                return false;
            
            // check for mute
            if (checkMsgMute(player.getName())) {
                player.sendMessage(ChatColor.RED + "You are muted.");
                return true;
            }
            
            // msg (player) (message)
            String msgName = args[0];
            String message = "";
            for (int i = 1; i < args.length; i ++) {
                message += args[i] + " ";
            }
            message = message.substring(0, message.length() - 1);
            for (Player each : onlinePlayers) {
                if (each.getName().toLowerCase().contains(msgName)) {
                    each.sendMessage(ChatColor.GREEN + "[" + player.getName() + " -> me] " + ChatColor.WHITE + message);
                    player.sendMessage(ChatColor.GREEN + "[me -> " + each.getName() + "] " + ChatColor.WHITE + message);
                        
                    for (String each2 : msgReply) {
                        String msgFrom = each2.substring(0, each2.indexOf(','));
                        String msgTo = each2.substring(each2.indexOf(',') + 1);
                        if (player.getName().equals(msgFrom)) {
                            msgReply.remove(msgFrom + ',' + msgTo);
                            msgReply.add(player.getName() + ',' + each.getName());
                            return true;
                        }
                    }
                        
                    msgReply.add(player.getName() + ',' + each.getName());
                    return true;
                }
            }
                
            // player not found
            player.sendMessage(ChatColor.RED + msgName + " is not online.");
            return true;
        }
        
        // ----- REPLY -----
        if (cmd.getName().equalsIgnoreCase("reply") && Nexus.commandConfig.getBoolean("nexus.command.msg")) {
            // check permission
            if (!checkPermission("nexus.msg", player))
                return true;
            
            // invalid args
            if (args.length == 0)
                return false;
            
            // check for mute
            if (checkMsgMute(player.getName())) {
                player.sendMessage(ChatColor.RED + "You are muted.");
                return true;
            }
            
            // reply (message)
            String message = "";
            for (int i = 0; i < args.length; i ++) {
                message += args[i] + " ";
            }
            message = message.substring(0, message.length() - 1);
            for (String each : msgReply) {
                String msgFrom = each.substring(0, each.indexOf(','));
                String msgTo = each.substring(each.indexOf(',') + 1);
                if (player.getName().equals(msgTo)) {
                    for (Player each2 : onlinePlayers) {
                        if (each2.getName().equals(msgFrom)) {
                            each2.sendMessage(ChatColor.GREEN + "[" + player.getName() + " -> me] " + ChatColor.WHITE + message);
                            player.sendMessage(ChatColor.GREEN + "[me -> " + each2.getName() + "] " + ChatColor.WHITE + message);
                            
                            for (String each3 : msgReply) {
                                if (player.getName().equals(msgTo)) {
                                    msgReply.remove(msgTo);
                                    msgReply.add(player.getName() + ',' + each2.getName());
                                    return true;
                                }
                            }
                            
                            msgReply.add(player.getName() + ',' + each2.getName());
                            return true;
                        }
                    }
                    
                    // player not found
                    player.sendMessage(ChatColor.RED + msgFrom + " is not online.");
                    return true;
                }
            }
            
            // player not found
            player.sendMessage(ChatColor.RED + "You have not received a message.");
            return true;
        }
        
        // ----- MUTE -----
        if (cmd.getName().equalsIgnoreCase("mute") && Nexus.commandConfig.getBoolean("nexus.command.mute")) {
            //permission check
            if (!checkPermission("nexus.mute", player))
                return true;
            
            // mute (player)
            if (args.length == 1) {
                String muteName = args[0];
                for (String each : msgMute) {
                    if (each.toLowerCase().contains(muteName.toLowerCase())) {
                        player.sendMessage(ChatColor.RED + each + " is already muted.");
                        return true;
                    }
                }
                for (Player each : onlinePlayers) {
                    if (each.getName().toLowerCase().contains(muteName)) {
                        msgMute.add(each.getName());
                        each.sendMessage(ChatColor.RED + "You are now muted.");
                        player.sendMessage(ChatColor.RED + each.getName() + " is now muted.");
                        return true;
                    }
                }
                
                // player not found
                player.sendMessage(ChatColor.RED + muteName + " is not online.");
                return true;
                
            // invalid args
            } else {
                return false;
            }
        }
        
        // ----- UNMUTE -----
        if (cmd.getName().equalsIgnoreCase("unmute") && Nexus.commandConfig.getBoolean("nexus.command.mute")) {
            // permission check
            if (!checkPermission("nexus.mute", player))
                return true;
            
            if (args.length == 1) {
                String muteName = args[0];
                for (String each : msgMute) {
                    if (each.toLowerCase().contains(muteName.toLowerCase())) {
                        msgMute.remove(each);
                        for (Player each2 : onlinePlayers) {
                            if (each2.getName().toLowerCase().contains(muteName.toLowerCase()))
                                each2.sendMessage(ChatColor.GREEN + "You are now unmuted.");
                        }
                        player.sendMessage(ChatColor.GREEN + each + " is now unmuted.");
                        return true;
                    }
                }
                for (Player each : onlinePlayers) {
                    if (each.getName().toLowerCase().contains(muteName.toLowerCase())) {
                        player.sendMessage(ChatColor.RED + each.getName() + " is not muted.");
                        return true;
                    }
                }
                                
                // player not found
                player.sendMessage(ChatColor.RED + muteName + " is not online");
                return true;
                
            // invalid args
            } else {
                return false;
            }
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
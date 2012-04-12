package com.vioviocity.nexus.commands;

import com.vioviocity.nexus.Nexus;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class HomeCommand implements CommandExecutor {
    
    private Nexus plugin;
    public HomeCommand(Nexus plugin) {
        this.plugin = plugin;
    }
    
    public boolean onCommand(CommandSender sender, Command command, String alias, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Command must be issued within game.");
            return true;
        }
        
        // initialize core variables
        Player player = (Player) sender;
        Set <String> homes = Collections.EMPTY_SET;
        Set <String> multiHomes = Collections.EMPTY_SET;
        
        // command handler
        String cmd = command.getName().toLowerCase();
        if (cmd.equals("home")) {
            // check if enabled
            if (!Nexus.commandConfig.getBoolean("nexus.command.home"))
                return true;
            // invalid args
            if (args.length < 0 || args.length > 2)
                return false;
            
            // initialize variables
            String path = "nexus.player." + player.getName() + '.';
            
            // load homes
            if (Nexus.homeConfig.isConfigurationSection("nexus.player"))
                homes = Nexus.homeConfig.getConfigurationSection("nexus.player").getKeys(false);
            
            // home
            if (args.length == 0) {
                // check permission
                if (!Nexus.checkPermission("nexus.home", player, true))
                    return true;
                
                // check player
                for (String each : homes) {
                    if (player.getName().equals(each)) {

                        // load multiple homes
                        if (Nexus.homeConfig.isConfigurationSection("nexus.player." + each))
                            multiHomes = Nexus.homeConfig.getConfigurationSection("nexus.player." + each).getKeys(false);
                        
                        // check homes
                        for (String another : multiHomes) {
                            if (another.equalsIgnoreCase("default")) {
                    
                                // initialize variables
                                Location home = player.getLocation();
                    
                                // teleport to home
                                home.setWorld(plugin.getServer().getWorld(Nexus.homeConfig.getString(path + "default.world")));
                                home.setX(Nexus.homeConfig.getDouble(path + "default.x"));
                                home.setY(Nexus.homeConfig.getDouble(path + "default.y"));
                                home.setZ(Nexus.homeConfig.getDouble(path + "default.z"));
                                home.setYaw((float) Nexus.homeConfig.getDouble(path + "default.yaw"));
                                home.setPitch((float) Nexus.homeConfig.getDouble(path + "default.pitch"));
                                player.teleport(home);
                                return true;
                            }
                        }
                    }
                }
                
                // home not found
                player.sendMessage(ChatColor.RED + "Home does not exist.");
                return true;
            }
            
            // home [set]
            if (args.length == 1 && args[0].equalsIgnoreCase("set")) {
                // check permission
                if (!Nexus.checkPermission("nexus.home.set", player, true))
                    return true;
                
                /*
                // check home
                for (String each : homes) {
                    if (player.getName().equals(each)) {
                        
                        // save home
                        Nexus.homeConfig.set(path + "default.world", player.getLocation().getWorld().getName());
                        Nexus.homeConfig.set(path + "default.x", player.getLocation().getX());
                        Nexus.homeConfig.set(path + "default.y", player.getLocation().getY());
                        Nexus.homeConfig.set(path + "default.z", player.getLocation().getZ());
                        Nexus.homeConfig.set(path + "default.yaw", player.getLocation().getYaw());
                        Nexus.homeConfig.set(path + "default.pitch", player.getLocation().getPitch());
                        Nexus.saveHomeConfig();
                        player.sendMessage(ChatColor.GREEN + "Home reset.");
                        return true;
                    }
                }
                */
                
                // create home
                Nexus.homeConfig.set(path + "default.world", player.getLocation().getWorld().getName());
                Nexus.homeConfig.set(path + "default.x", player.getLocation().getX());
                Nexus.homeConfig.set(path + "default.y", player.getLocation().getY());
                Nexus.homeConfig.set(path + "default.z", player.getLocation().getZ());
                Nexus.homeConfig.set(path + "default.yaw", player.getLocation().getYaw());
                Nexus.homeConfig.set(path + "default.pitch", player.getLocation().getPitch());
                Nexus.saveHomeConfig();
                player.sendMessage(ChatColor.GREEN + "Home set.");
                return true;
            }
            
            if (args.length == 1 && args[0].equalsIgnoreCase("list")) {
                // check permission
                if (!Nexus.checkPermission("nexus.home.multi", player, true))
                    return true;
                
                // load multiple homes
                if (Nexus.homeConfig.isConfigurationSection("nexus.player." + player.getName()))
                    multiHomes = Nexus.homeConfig.getConfigurationSection("nexus.player." + player.getName()).getKeys(false);
                
                // homes not set
                if (multiHomes.isEmpty()) {
                    player.sendMessage(ChatColor.RED + "Homes have not been set.");
                    return true;
                }
                
                // display list of homes
                String homeList = "";
                for (String each : multiHomes)
                    homeList += each + ", ";
                homeList = homeList.substring(0, homeList.length() - 2);
                player.sendMessage(ChatColor.GREEN + "Homes: " + ChatColor.WHITE + homeList);
                return true;
            }
            
            // home (home)
            if (args.length == 1) {
                // check permission
                if (!Nexus.checkPermission("nexus.home.multi", player, true))
                    return true;
                
                // initialize variables
                String homeName = args[0];
                
                // check player
                for (String each : homes) {
                    if (player.getName().equals(each)) {
                        
                        // load multiple homes
                        if (Nexus.homeConfig.isConfigurationSection("nexus.player." + each))
                            multiHomes = Nexus.homeConfig.getConfigurationSection("nexus.player." + each).getKeys(false);
                        
                        // check home
                        for (String another : multiHomes) {
                            if (homeName.equalsIgnoreCase(another)) {
                                
                                // initialize variables
                                Location home = player.getLocation();
                    
                                // teleport to home
                                home.setWorld(plugin.getServer().getWorld(Nexus.homeConfig.getString(path + another + ".world")));
                                home.setX(Nexus.homeConfig.getDouble(path + another + ".x"));
                                home.setY(Nexus.homeConfig.getDouble(path + another + ".y"));
                                home.setZ(Nexus.homeConfig.getDouble(path + another + ".z"));
                                home.setYaw((float) Nexus.homeConfig.getDouble(path + another + ".yaw"));
                                home.setPitch((float) Nexus.homeConfig.getDouble(path + another + ".pitch"));
                                player.teleport(home);
                                return true;
                            }
                        }
                    }
                }
                
                // home not found
                player.sendMessage(ChatColor.RED + "Home does not exist.");
                return true;
            }
            
            // home [set] (home)
            if (args.length == 2 & args[0].equalsIgnoreCase("set")) {
                // check permission
                if (!Nexus.checkPermission("nexus.home.multi", player, true))
                    return true;
                
                // initialize variables
                String homeName = args[1];
                
                // check player
                for (String each : homes) {
                    if (player.getName().equals(each)) {
                        
                        // load multiple homes
                        if (Nexus.homeConfig.isConfigurationSection("nexus.player." + each))
                            multiHomes = Nexus.homeConfig.getConfigurationSection("nexus.player." + each).getKeys(false);
                        
                        // check home
                        for (String another : multiHomes) {
                            if (homeName.equalsIgnoreCase(another)) {
                            
                                // save home
                                Nexus.homeConfig.set(path + another + ".world", player.getLocation().getWorld().getName());
                                Nexus.homeConfig.set(path + another + ".x", player.getLocation().getX());
                                Nexus.homeConfig.set(path + another + ".y", player.getLocation().getY());
                                Nexus.homeConfig.set(path + another + ".z", player.getLocation().getZ());
                                Nexus.homeConfig.set(path + another + ".yaw", player.getLocation().getYaw());
                                Nexus.homeConfig.set(path + another + ".pitch", player.getLocation().getPitch());
                                Nexus.saveHomeConfig();
                                player.sendMessage(ChatColor.GREEN + "Home " + homeName + " reset.");
                                return true;
                            }
                        }
                        
                        // create home
                        Nexus.homeConfig.set(path + homeName + ".world", player.getLocation().getWorld().getName());
                        Nexus.homeConfig.set(path + homeName + ".x", player.getLocation().getX());
                        Nexus.homeConfig.set(path + homeName + ".y", player.getLocation().getY());
                        Nexus.homeConfig.set(path + homeName + ".z", player.getLocation().getZ());
                        Nexus.homeConfig.set(path + homeName + ".yaw", player.getLocation().getYaw());
                        Nexus.homeConfig.set(path + homeName + ".pitch", player.getLocation().getPitch());
                        Nexus.saveHomeConfig();
                        player.sendMessage(ChatColor.GREEN + "Home " + homeName + " set.");
                        return true;
                    }
                }
                
                // create home if no default exists
                Nexus.homeConfig.set(path + homeName + ".world", player.getLocation().getWorld().getName());
                Nexus.homeConfig.set(path + homeName + ".x", player.getLocation().getX());
                Nexus.homeConfig.set(path + homeName + ".y", player.getLocation().getY());
                Nexus.homeConfig.set(path + homeName + ".z", player.getLocation().getZ());
                Nexus.homeConfig.set(path + homeName + ".yaw", player.getLocation().getYaw());
                Nexus.homeConfig.set(path + homeName + ".pitch", player.getLocation().getPitch());
                Nexus.saveHomeConfig();
                player.sendMessage(ChatColor.GREEN + "Home " + homeName + " reset.");
                return true;
            }
            
            
            // home [del] (home)
            if (args.length == 2 & args[0].equalsIgnoreCase("del")) {
                // check permission
                if (!Nexus.checkPermission("nexus.home.multi", player, true))
                    return true;
                
                // initialize variables
                String homeName = args[1];
                
                // check player
                for (String each : homes) {
                    if (player.getName().equals(each)) {
                        
                        // load multiple homes
                        if (Nexus.homeConfig.isConfigurationSection("nexus.player." + each))
                            multiHomes = Nexus.homeConfig.getConfigurationSection("nexus.player." + each).getKeys(false);
                        
                        // check home
                        for (String another : multiHomes) {
                            if (homeName.equalsIgnoreCase(another)) {
                                
                                // delete home
                                Nexus.homeConfig.set(path + another, null);
                                Nexus.saveHomeConfig();
                                player.sendMessage(ChatColor.GREEN + "Home " + homeName + " deleted.");
                                return true;
                            }
                        }
                        
                        // home not found
                        player.sendMessage(ChatColor.RED + "Home does not exist.");
                        return true;
                    }
                }
                
                // home not found
                player.sendMessage(ChatColor.RED + "Home does not exist.");
                return true;
            }
        }
        
        // end of command
        return false;
    }
}

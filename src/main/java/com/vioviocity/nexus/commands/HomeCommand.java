package com.vioviocity.nexus.commands;

import com.vioviocity.nexus.Nexus;
import java.util.List;
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
        if (!(sender instanceof Player))
            return true;
        
        // initialize core variables
        Player player = (Player) sender;
        
        // command handler
        String cmd = command.getName().toLowerCase();
        if (cmd.equals("home")) {
            // check if enabled
            if (!Nexus.commandConfig.getBoolean("nexus.command.home"))
                return true;
            // invalid args
            if (args.length < 0 || args.length > 1)
                return false;
            
            // initialize variables
            List <String> homes = Nexus.homeConfig.getStringList("nexus.players");
            String path = "nexus." + player.getName() + '.';
            
            // home
            if (args.length == 0) {
                // check permission
                if (!Nexus.checkPermission("nexus.home", player, true))
                    return true;
                
                for (String each : homes) {
                    if (player.getName().equals(each)) {
                    
                        // initialize variables
                        Location home = player.getLocation();
                    
                        // teleport to home
                        home.setWorld(plugin.getServer().getWorld(Nexus.homeConfig.getString(path + "world")));
                        home.setX(Nexus.homeConfig.getDouble(path + "x"));
                        home.setY(Nexus.homeConfig.getDouble(path + "y"));
                        home.setZ(Nexus.homeConfig.getDouble(path + "z"));
                        home.setYaw((float) Nexus.homeConfig.getDouble(path + "yaw"));
                        home.setPitch((float) Nexus.homeConfig.getDouble(path + "pitch"));
                        player.teleport(home);
                        return true;
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
                
                // check home
                for (String each : homes) {
                    if (player.getName().equals(each)) {
                        
                        // save home
                        homes.set(homes.indexOf(each), player.getName());
                        Nexus.homeConfig.set("nexus.players", homes);
                        Nexus.homeConfig.set(path + "world", player.getLocation().getWorld().getName());
                        Nexus.homeConfig.set(path + "x", player.getLocation().getX());
                        Nexus.homeConfig.set(path + "y", player.getLocation().getY());
                        Nexus.homeConfig.set(path + "z", player.getLocation().getZ());
                        Nexus.homeConfig.set(path + "yaw", player.getLocation().getYaw());
                        Nexus.homeConfig.set(path + "pitch", player.getLocation().getPitch());
                        Nexus.saveHomeConfig();
                        player.sendMessage(ChatColor.GREEN + "Home reset.");
                        return true;
                    }
                }
                
                // create home
                homes.add(player.getName());
                Nexus.homeConfig.set("nexus.players", homes);
                Nexus.homeConfig.set(path + "world", player.getLocation().getWorld().getName());
                Nexus.homeConfig.set(path + "x", player.getLocation().getX());
                Nexus.homeConfig.set(path + "y", player.getLocation().getY());
                Nexus.homeConfig.set(path + "z", player.getLocation().getZ());
                Nexus.homeConfig.set(path + "yaw", player.getLocation().getYaw());
                Nexus.homeConfig.set(path + "pitch", player.getLocation().getPitch());
                Nexus.saveHomeConfig();
                player.sendMessage(ChatColor.GREEN + "Home set.");
                return true;
            }
        }
        
        // end of command
        return false;
    }
}

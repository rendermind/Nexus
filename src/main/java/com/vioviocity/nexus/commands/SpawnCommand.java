package com.vioviocity.nexus.commands;

import com.vioviocity.nexus.Nexus;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SpawnCommand implements CommandExecutor{
    
    private Nexus plugin;
    public SpawnCommand(Nexus plugin) {
        this.plugin = plugin;
    }

    public boolean onCommand(CommandSender sender, Command command, String alias, String[] args) {
        if (!(sender instanceof Player))
            return true;
        
        // initialize core variables
        Player player = (Player) sender;
        World world = player.getWorld();
        
        // command handler
        String cmd = command.getName().toLowerCase();
        if (cmd.equals("spawn")) {
            // check if enabled
            if (!Nexus.commandConfig.getBoolean("nexus.command.spawn"))
                return true;
            // invalid args
            if (args.length > 1)
                return false;
            
            // spawn (no args)
            if (args.length == 0) {
                // permission check
                if (!Nexus.checkPermission("nexus.spawn", player))
                    return true;
                
                Location spawn = player.getLocation();
                spawn.setWorld(plugin.getServer().getWorld(Nexus.spawnConfig.getString("nexus.spawn.world")));
                spawn.setX(Nexus.spawnConfig.getDouble("nexus.spawn.x"));
                spawn.setY(Nexus.spawnConfig.getDouble("nexus.spawn.y"));
                spawn.setZ(Nexus.spawnConfig.getDouble("nexus.spawn.z"));
                spawn.setYaw((float) Nexus.spawnConfig.getDouble("nexus.spawn.yaw"));
                spawn.setPitch((float) Nexus.spawnConfig.getDouble("nexus.spawn.pitch"));
                player.teleport(spawn);
                return true;
            }
            
            // spawn [set]
            if (args.length == 1) {
                // permission check
                if (!Nexus.checkPermission("nexus.spawn.set", player))
                    return true;
                
                Location spawn = player.getLocation();
                world.setSpawnLocation((int) spawn.getX(), (int) spawn.getY(), (int) spawn.getZ());
                Nexus.spawnConfig.set("nexus.spawn.world", spawn.getWorld().getName());
                Nexus.spawnConfig.set("nexus.spawn.x", spawn.getX());
                Nexus.spawnConfig.set("nexus.spawn.y", spawn.getY());
                Nexus.spawnConfig.set("nexus.spawn.yaw", spawn.getYaw());
                Nexus.spawnConfig.set("nexus.spawn.pitcher", spawn.getPitch());
                Nexus.saveSpawnConfig();
                player.sendMessage(ChatColor.GREEN + "Spawn location set.");
                return true;
            }
        }
        
        // end of command
        return false;
    }
}
package com.vioviocity.nexus;

import com.vioviocity.nexus.commands.BackCommand;
import com.vioviocity.nexus.commands.MuteCommand;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

public class NexusPlayerListener implements Listener{
    
    @EventHandler
    public void onPlayerChat(PlayerChatEvent event) {
        // initialize variables
        Player player = event.getPlayer();
        
        // check mute
        if (MuteCommand.msgMute.contains(player)) {
            player.sendMessage(ChatColor.RED + "You are muted.");
            event.setCancelled(true);
        }
    }
    
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        // initialize variables
        Player player = event.getPlayer();
        
        // new player to spawn
        if (!player.hasPlayedBefore()) {
            Location spawn = player.getLocation();
            spawn.setWorld(Bukkit.getServer().getWorld(Nexus.spawnConfig.getString("nexus.spawn.world")));
            spawn.setX(Nexus.spawnConfig.getDouble("nexus.spawn.x"));
            spawn.setY(Nexus.spawnConfig.getDouble("nexus.spawn.y"));
            spawn.setZ(Nexus.spawnConfig.getDouble("nexus.spawn.z"));
            spawn.setYaw((float) Nexus.spawnConfig.getDouble("nexus.spawn.yaw"));
            spawn.setPitch((float) Nexus.spawnConfig.getDouble("nexus.spawn.pitch"));
            player.teleport(spawn);
        }
    }

    @EventHandler
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        // initialize variables
        Player player = event.getPlayer();
        
        // check teleport cause
        if (!event.getCause().toString().equals("UNKNOWN"))
            BackCommand.tpBack.put(player, event.getFrom());
    }
    
    @EventHandler
    public void onPlayerDeath(EntityDeathEvent event) {
        // check if player
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            
            // back on death
            if (Nexus.checkPermission("nexus.back.death", player, false))
                BackCommand.tpBack.put(player, player.getLocation());
        }
    }
    
    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        // initialize variables
        Player player = event.getPlayer();
        Location respawn = event.getRespawnLocation();
        Location worldSpawn = event.getPlayer().getWorld().getSpawnLocation();
        
        // teleport to spawn
        respawn.setX((int) respawn.getX());
        respawn.setY((int) respawn.getY());
        respawn.setZ((int) respawn.getZ());
        worldSpawn.setX((int) worldSpawn.getX());
        worldSpawn.setY((int) worldSpawn.getY());
        worldSpawn.setZ((int) worldSpawn.getZ());
        if (respawn.getX() == worldSpawn.getX() && respawn.getY() == worldSpawn.getY() && respawn.getZ() == worldSpawn.getZ()) {
            Location spawn = player.getLocation();
            spawn.setWorld(Bukkit.getServer().getWorld(Nexus.spawnConfig.getString("nexus.spawn.world")));
            spawn.setX(Nexus.spawnConfig.getDouble("nexus.spawn.x"));
            spawn.setY(Nexus.spawnConfig.getDouble("nexus.spawn.y"));
            spawn.setZ(Nexus.spawnConfig.getDouble("nexus.spawn.z"));
            spawn.setYaw((float) Nexus.spawnConfig.getDouble("nexus.spawn.yaw"));
            spawn.setPitch((float) Nexus.spawnConfig.getDouble("nexus.spawn.pitch"));
            event.setRespawnLocation(spawn);
        }
    }
}
package com.vioviocity.nexus;

import java.text.DecimalFormat;
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
        Player player = event.getPlayer();
        for (String each : NexusCommands.msgMute) {
            if (each.equals(player.getName())) {
                player.sendMessage(ChatColor.RED + "You are muted.");
                event.setCancelled(true);
            }
        }
    }
    
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
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
        // ensure proper teleport cause
        if (!event.getCause().toString().equals("UNKNOWN"))
            setTpBack(event.getPlayer(), event.getFrom());
    }
    
    @EventHandler
    public void onPlayerDeath(EntityDeathEvent event) {
        // check if entity is player
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            if (NexusCommands.checkPermission("nexus.back.death", player))
                setTpBack(player, player.getLocation());
        }
    }
    
    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        Location respawn = event.getRespawnLocation();
        Location worldSpawn = event.getPlayer().getWorld().getSpawnLocation();
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
    
    private void setTpBack(Player player, Location locale) {
        // player current location
        String worldName = locale.getWorld().getName();
        double x = round(locale.getX());
        double y = round(locale.getY());
        double z = round(locale.getZ());
        float yaw = (float) round(locale.getYaw());
        float pitch = (float) round(locale.getPitch());
        
        // change existing location
        String tpName;
        for (String each : NexusCommands.tpBack) {
            tpName = each.substring(0, each.indexOf(','));
            if (player.getName().equals(tpName)) {
                NexusCommands.tpBack.set(NexusCommands.tpBack.indexOf(each), player.getName() + ',' + "map:" +
                        worldName + "x:" + x + "y:" + y + "z:" + z + "yaw:" + yaw + "pitch:" + pitch);
                return;
            }
        }
        
        // add new location
        NexusCommands.tpBack.add(player.getName() + ',' + "map:" + worldName + "x:" + x + "y:" + y + "z:" + z +
                "yaw:" + yaw + "pitch:" + pitch);
    }
    
    private double round(double value) {
        DecimalFormat newFormat = new DecimalFormat("#.#");
        return Double.valueOf(newFormat.format(value));
    }
}
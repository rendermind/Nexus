package co.viocode.nexus;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

public class EventListener implements Listener {

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {

		// init vars
		Player player = event.getPlayer();

		// teleport new player to spawn
		if (!player.hasPlayedBefore()) {
			Location spawn = player.getLocation();
			spawn.setX(Nexus.spawnConfig.getDouble(player.getWorld().getName() + ".x"));
			spawn.setY(Nexus.spawnConfig.getDouble(player.getWorld().getName() + ".y"));
			spawn.setZ(Nexus.spawnConfig.getDouble(player.getWorld().getName() + ".z"));
			spawn.setYaw((float) Nexus.spawnConfig.getDouble(player.getWorld().getName() + ".yaw"));
			spawn.setPitch((float) Nexus.spawnConfig.getDouble(player.getWorld().getName() + ".pitch"));
			player.teleport(spawn);
		}
	}

	@EventHandler
	public void onPlayerRespawn(PlayerRespawnEvent event) {

		// init vars
		Player player = event.getPlayer();
		Location spawn = player.getLocation();

		// check for prior death location
		if (Nexus.deathWorld.containsKey(player)) {
			spawn.setWorld(Nexus.deathWorld.get(player));
			Nexus.deathWorld.remove(player);
		}

		// check if spawn exists
		if (!Nexus.spawnConfig.isConfigurationSection(spawn.getWorld().getName()))
			spawn.setWorld(Bukkit.getServer().getWorlds().get(0));

		// teleport player to spawn
		spawn.setX(Nexus.spawnConfig.getDouble(spawn.getWorld().getName() + ".x"));
		spawn.setY(Nexus.spawnConfig.getDouble(spawn.getWorld().getName() + ".y"));
		spawn.setZ(Nexus.spawnConfig.getDouble(spawn.getWorld().getName() + ".z"));
		spawn.setYaw((float) Nexus.spawnConfig.getDouble(spawn.getWorld().getName() + ".yaw"));
		spawn.setPitch((float) Nexus.spawnConfig.getDouble(spawn.getWorld().getName() + ".pitch"));
		event.setRespawnLocation(spawn);
	}

	@EventHandler
	public void onPlayerTeleport(PlayerTeleportEvent event) {

		// init vars
		Player player = event.getPlayer();

		// check teleport cause
		if (!event.getCause().toString().equals("UNKNOWN"))
			Nexus.teleportBack.put(player, event.getFrom());
	}

	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent event) {

		// init vars
		Player player = event.getEntity();

		// store death world
		Nexus.deathWorld.put(player, player.getLocation().getWorld());

		// store death location
		if (Nexus.checkPermission("nexus.back.death", player))
			Nexus.teleportBack.put(player, player.getLocation());
	}

	@EventHandler
	public void onPlayerChat(PlayerChatEvent event) {

		// init vars
		Player player = event.getPlayer();

		// check if player is muted
		if (Nexus.mute.contains(player)) {
			player.sendMessage(ChatColor.RED + "You are muted.");
			event.setCancelled(true);
		}
	}
}
package co.viocode.nexus;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

public class EventListener implements Listener {

	@EventHandler
	public void onPlayerJoin (PlayerJoinEvent event) {

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

		// teleport player to spawn
		spawn.setX(Nexus.spawnConfig.getDouble(spawn.getWorld().getName() + ".x"));
		spawn.setY(Nexus.spawnConfig.getDouble(spawn.getWorld().getName() + ".y"));
		spawn.setZ(Nexus.spawnConfig.getDouble(spawn.getWorld().getName() + ".z"));
		spawn.setYaw((float) Nexus.spawnConfig.getDouble(spawn.getWorld().getName() + ".yaw"));
		spawn.setPitch((float) Nexus.spawnConfig.getDouble(spawn.getWorld().getName() + ".pitch"));
		event.setRespawnLocation(spawn);
	}

	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent event) {

		// init vars
		Player player = event.getEntity();

		// store death world
		Nexus.deathWorld.put(player, player.getLocation().getWorld());
	}
}
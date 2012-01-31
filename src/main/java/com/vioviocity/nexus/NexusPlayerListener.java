package com.vioviocity.nexus;

import java.text.DecimalFormat;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;

public class NexusPlayerListener implements Listener{

    @EventHandler
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        if (event.getCause().toString().equals("UNKNOWN"))
            return;
        
        Player player = event.getPlayer();
        
        // player current location
        String worldName = event.getFrom().getWorld().getName();
        double x = round(event.getFrom().getX());
        double y = round(event.getFrom().getY());
        double z = round(event.getFrom().getZ());
        float yaw = (float) round(event.getFrom().getYaw());
        float pitch = (float) round(event.getFrom().getPitch());
        
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

package com.vioviocity.nexus.commands;

import com.vioviocity.nexus.Nexus;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TpcCommand implements CommandExecutor{
    
    private Nexus plugin;
    public TpcCommand(Nexus plugin) {
        this.plugin = plugin;
    }

    public boolean onCommand(CommandSender sender, Command command, String alias, String[] args) {
        if (!(sender instanceof Player))
            return true;
        
        // initialize core variables
        Player player = (Player) sender;
        
        // command handler
        String cmd = command.getName().toLowerCase();
        if (cmd.equals("tpc")) {
            // check if enabled
            if (!Nexus.commandConfig.getBoolean("nexus.command.tpc"))
                return true;
            // check permission
            if (!Nexus.checkPermission("nexus.tpc", player, true))
                return true;
            // invalid args
            if (args.length < 2 || args.length > 2)
                return false;
            
            // tpc (x) (z)
            Location teleport = player.getLocation();
            double x = Double.parseDouble(args[0]);
            double z = Double.parseDouble(args[1]);
            teleport.setX(x);
            teleport.setZ(z);
            teleport.setY(teleport.getWorld().getHighestBlockYAt(teleport));
            player.teleport(teleport);
            return true;
        }
        
        // end of command
        return false;
    }
}
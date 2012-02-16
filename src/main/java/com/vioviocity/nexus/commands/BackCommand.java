package com.vioviocity.nexus.commands;

import com.vioviocity.nexus.Nexus;
import java.util.HashMap;
import java.util.Map;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BackCommand implements CommandExecutor{
    
    static public Map<Player,Location> tpBack = new HashMap<Player,Location>(200);
    
    private Nexus plugin;
    public BackCommand(Nexus plugin) {
        this.plugin = plugin;
    }
    
    public boolean onCommand(CommandSender sender, Command command, String alias, String[] args) {
        if (!(sender instanceof Player))
            return true;
        
        // initialize core variables
        Player player = (Player) sender;
        
        // command handler
        String cmd = command.getName().toLowerCase();
        if (cmd.equals("back")) {
            // check if enabled
            if (!Nexus.commandConfig.getBoolean("nexus.command.back"))
                return true;
            // check permission
            if (!Nexus.checkPermission("nexus.back", player))
                return true;
            // invalid args
            if (args.length > 0)
                return false;
            
            // initialize variables
            String playerName;
            Location backLocation = player.getLocation();
            
            // back (no args)
            
        }
        
        // end of command
        return false;
    }
}

package com.vioviocity.nexus.commands;

import com.vioviocity.nexus.Nexus;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TimeCommand implements CommandExecutor{
    
    private Nexus plugin;
    public TimeCommand(Nexus plugin) {
        this.plugin = plugin;
    }

    public boolean onCommand(CommandSender sender, Command command, String alias, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Command must be issued within game.");
            return true;
        }
        
        // initialize core variables
        Player player = (Player) sender;
        World world = player.getWorld();
        
        // command handler
        String cmd = command.getName().toLowerCase();
        if (cmd.equals("time")) {
            // check if enabled
            if (!Nexus.commandConfig.getBoolean("nexus.command.time"))
                return true;
            // invalid args
            if (args.length > 1)
                return false;
            
            // time (no args)
            if (args.length == 0) {
                // check permission
                if (!Nexus.checkPermission("nexus.time", player, true))
                    return true;
                
                int tick = (int) world.getTime();
                int hour = tick / 1000;
                hour += 6;
                if (hour > 23)
                    hour -= 24;
                int min = (tick - ((int) (tick / 1000) * 1000)) / (1000 / 60);
                if (min < 10) {
                    player.sendMessage(ChatColor.GREEN + "Time: " + hour + ":0" + min + " , Tick: " + tick);
                } else {
                    player.sendMessage(ChatColor.GREEN + "Time: " + hour + ":" + min + " , Tick: " + tick);
                }
                return true;
            }
            
            // time [dawn|day|dusk|night]
            if (args.length == 1) {
                // check permission
                if (!Nexus.checkPermission("nexus.time.set", player, true))
                    return true;
                
                String time = args[0].toLowerCase();
                if (time.equals("dawn")) {
                    world.setTime(22200);
                    return true;
                } else if (time.equals("day")) {
                    world.setTime(0);
                    return true;
                } else if (time.equals("dusk")) {
                    world.setTime(12000);
                    return true;
                } else if (time.equals("night")) {
                    world.setTime(13800);
                    return true;
                }
                return false;
            }
        }
        
        // end of command
        return false;
    }
}

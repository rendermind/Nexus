package com.vioviocity.nexus.commands;

import com.vioviocity.nexus.Nexus;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class WeatherCommand implements CommandExecutor{
    
    private Nexus plugin;
    public WeatherCommand(Nexus plugin) {
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
        if (cmd.equals("weather")) {
            // check if enabled
            if (!Nexus.commandConfig.getBoolean("nexus.command.weather"))
                return true;
            // invalid args
            if (args.length > 1)
                return false;
            
            // weather (no args)
            if (args.length == 0) {
                //permission check
                if (!Nexus.checkPermission("nexus.weather", player, true))
                    return true;
                
                int tick = (int) world.getWeatherDuration();
                int min = (tick / 20) / 60;
                if (world.hasStorm()) {
                    player.sendMessage(ChatColor.GREEN + "Weather: Storming, Duration: " + min + " minute(s)");
                } else {
                    player.sendMessage(ChatColor.GREEN + "Weather: Clear, Duration: " + min + " minute(s)");
                }
                return true;
            }
            
            // weather [clear|storm|thunder]
            if (args.length == 1) {
                // permission check
                if (!Nexus.checkPermission("nexus.weather.set", player, true))
                    return true;
                
                String weather = args[0].toLowerCase();
                if (weather.equals("clear")) {
                    world.setStorm(false);
                    world.setThundering(false);
                    return true;
                } else if (weather.equals("storm")) {
                    world.setStorm(true);
                    world.setThundering(false);
                    return true;
                } else if (weather.equals("thunder")) {
                    world.setStorm(true);
                    world.setThundering(true);
                    return true;
                }
                return false;
            }
        }
        
        // end of command
        return false;
    }
}
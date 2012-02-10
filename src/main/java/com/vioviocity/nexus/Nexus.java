package com.vioviocity.nexus;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Logger;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class Nexus extends JavaPlugin {
    
    private NexusCommands myExecutor;
    static Logger log = Logger.getLogger("Nexus");
    
    static FileConfiguration commandConfig = null;
    static File commandConfigFile = null;
    static FileConfiguration spawnConfig = null;
    static File spawnConfigFile = null;
    static FileConfiguration waypointConfig = null;
    static File waypointConfigFile = null;
    
    public void onDisable() {
        log.info(this + " is now disabled.");
    }

    public void onEnable() {
        // register events
        getServer().getPluginManager().registerEvents(new NexusPlayerListener(), this);
        
        // setup config files
        loadCommandConfig();
        saveCommandConfig();
        loadSpawnConfig();
        saveSpawnConfig();
        loadWaypointConfig();
        saveWaypointConfig();
        
        // register commands based on config
        myExecutor = new NexusCommands(this);
        getCommand("test").setExecutor(myExecutor);
        if (commandConfig.getBoolean("nexus.command.time"))
            getCommand("time").setExecutor(myExecutor);
        if (commandConfig.getBoolean("nexus.command.weather"))
            getCommand("weather").setExecutor(myExecutor);
        if (commandConfig.getBoolean("nexus.command.spawn"))
            getCommand("spawn").setExecutor(myExecutor);
        if (commandConfig.getBoolean("nexus.command.mode"))
            getCommand("mode").setExecutor(myExecutor);
        if (commandConfig.getBoolean("nexus.command.online"))
            getCommand("online").setExecutor(myExecutor);
        if (commandConfig.getBoolean("nexus.command.kick"))
            getCommand("kick").setExecutor(myExecutor);
        if (commandConfig.getBoolean("nexus.command.ban")) {
            getCommand("ban").setExecutor(myExecutor);
            getCommand("unban").setExecutor(myExecutor);
        }
        if (commandConfig.getBoolean("nexus.command.tp"))
            getCommand("tp").setExecutor(myExecutor);
        if (commandConfig.getBoolean("nexus.command.tpr"))
            getCommand("tpr").setExecutor(myExecutor);
        if (commandConfig.getBoolean("nexus.command.tpc"))
            getCommand("tpc").setExecutor(myExecutor);
        if (commandConfig.getBoolean("nexus.command.wp"))
            getCommand("wp").setExecutor(myExecutor);
        if (commandConfig.getBoolean("nexus.command.back"))
            getCommand("back").setExecutor(myExecutor);
        if (commandConfig.getBoolean("nexus.command.heal"))
            getCommand("heal").setExecutor(myExecutor);
        if (commandConfig.getBoolean("nexus.command.kill"))
            getCommand("kill").setExecutor(myExecutor);
        if (commandConfig.getBoolean("nexus.command.level"))
            getCommand("level").setExecutor(myExecutor);
        if (commandConfig.getBoolean("nexus.command.item"))
            getCommand("item").setExecutor(myExecutor);
        if (commandConfig.getBoolean("nexus.command.broadcast"))
            getCommand("broadcast").setExecutor(myExecutor);
        if (commandConfig.getBoolean("nexus.command.msg")) {
            getCommand("msg").setExecutor(myExecutor);
            getCommand("reply").setExecutor(myExecutor);
        }
        if (commandConfig.getBoolean("nexus.command.mute")) {
            getCommand("mute").setExecutor(myExecutor);
            getCommand("unmute").setExecutor(myExecutor);
        }
        
        //check yaml versions
        try {
            if (!commandConfig.getString("nexus.version").equals("b")) {
                log.warning("Nexus\\commands.yml is out of date!.");
                log.warning("- Backup file data, delete .yml file, then restart server.");
            }
        } catch (NullPointerException e) {
            log.warning("Nexus\\commands.yml is out of date!");
            log.warning("- Backup file data, delete .yml file, then restart server.");
        }
        
        // plugin enabled
        log.info(this + " is now enabled.");
    }
    
    public FileConfiguration loadCommandConfig() {
        if (commandConfig == null) {
            if (commandConfigFile == null)
                commandConfigFile = new File(this.getDataFolder(), "commands.yml");
            if (commandConfigFile.exists()) {
                commandConfig = YamlConfiguration.loadConfiguration(commandConfigFile);
            } else {
                InputStream defConfigStream = getResource("commands.yml");
                commandConfig = YamlConfiguration.loadConfiguration(defConfigStream);
            }
        }
        return commandConfig;
    }
    
    public void saveCommandConfig() {
        if (commandConfig == null || commandConfigFile == null)
            return;
        try {
            commandConfig.save(commandConfigFile);
        } catch (IOException e) {
            log.severe("Unable to save command config to " + commandConfigFile + ".");
        }
    }
    
    public FileConfiguration loadSpawnConfig() {
        if (spawnConfig == null) {
            if (spawnConfigFile == null)
                spawnConfigFile = new File(this.getDataFolder(), "spawn.yml");
            if (spawnConfigFile.exists()) {
                spawnConfig = YamlConfiguration.loadConfiguration(spawnConfigFile);
            } else {
                InputStream defConfigStream = getResource("spawn.yml");
                spawnConfig = YamlConfiguration.loadConfiguration(defConfigStream);
                
                Location spawn = getServer().getWorlds().get(0).getSpawnLocation();
                spawnConfig.set("nexus.spawn.world", spawn.getWorld().getName());
                spawnConfig.set("nexus.spawn.x", spawn.getX());
                spawnConfig.set("nexus.spawn.y", spawn.getWorld().getHighestBlockAt(spawn).getY());
                spawnConfig.set("nexus.spawn.z", spawn.getZ());
            }
        }
        return spawnConfig;
    }
    
    static public void saveSpawnConfig() {
        if (spawnConfig == null || spawnConfigFile == null)
            return;
        try {
            spawnConfig.save(spawnConfigFile);
        } catch (IOException e) {
            log.severe("Unable to save spawn config to " + spawnConfigFile + ".");
        }
    }
    
    public FileConfiguration loadWaypointConfig() {
        if (waypointConfig == null) {
            if (waypointConfigFile == null)
                waypointConfigFile = new File(this.getDataFolder(), "waypoints.yml");
            if (waypointConfigFile.exists()) {
                waypointConfig = YamlConfiguration.loadConfiguration(waypointConfigFile);
            } else {
                InputStream defConfigStream = getResource("waypoints.yml");
                waypointConfig = YamlConfiguration.loadConfiguration(defConfigStream);
            }
        }
        return waypointConfig;
    }
    
    static public void saveWaypointConfig() {
        if (waypointConfig == null || waypointConfigFile == null)
            return;
        try {
            waypointConfig.save(waypointConfigFile);
        } catch (IOException e) {
            log.severe("Unable to save waypoint config to " + waypointConfigFile + ".");
        }
    }
}
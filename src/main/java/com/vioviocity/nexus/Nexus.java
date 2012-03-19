package com.vioviocity.nexus;

import com.vioviocity.nexus.commands.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class Nexus extends JavaPlugin {
    
    static Logger log = Logger.getLogger("Nexus");
    
    static public FileConfiguration commandConfig = null;
    static File commandConfigFile = null;
    static public FileConfiguration spawnConfig = null;
    static File spawnConfigFile = null;
    static public FileConfiguration waypointConfig = null;
    static File waypointConfigFile = null;
    static public FileConfiguration itemConfig = null;
    static File itemConfigFile = null;
    
    static public Map<String,String> itemList = new HashMap<String,String>(500);
    
    @Override
    public void onDisable() {
        log.info(this + " is now disabled.");
    }

    @Override
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
        
        // load item list
        loadItemConfig();
        saveItemConfig();
        for (String each : itemConfig.getStringList("nexus.items")) {
            String id = each.substring(0, each.indexOf(","));
            String item = each.substring(each.indexOf(",") + 1);
            itemList.put(item, id);
        }
        
        // register commands based on config
        if (commandConfig.getBoolean("nexus.command.time"))
            getCommand("time").setExecutor(new TimeCommand(this));
        if (commandConfig.getBoolean("nexus.command.weather"))
            getCommand("weather").setExecutor(new WeatherCommand(this));
        if (commandConfig.getBoolean("nexus.command.spawn"))
            getCommand("spawn").setExecutor(new SpawnCommand(this));
        if (commandConfig.getBoolean("nexus.command.mode"))
            getCommand("mode").setExecutor(new ModeCommand(this));
        if (commandConfig.getBoolean("nexus.command.online"))
            getCommand("online").setExecutor(new OnlineCommand(this));
        if (commandConfig.getBoolean("nexus.command.kick"))
            getCommand("kick").setExecutor(new KickCommand(this));
        if (commandConfig.getBoolean("nexus.command.ban")) {
            getCommand("ban").setExecutor(new BanCommand(this));
            getCommand("unban").setExecutor(new UnbanCommand(this));
        }
        if (commandConfig.getBoolean("nexus.command.tp"))
            getCommand("tp").setExecutor(new TpCommand(this));
        if (commandConfig.getBoolean("nexus.command.tpr"))
            getCommand("tpr").setExecutor(new TprCommand(this));
        if (commandConfig.getBoolean("nexus.command.tpc"))
            getCommand("tpc").setExecutor(new TpcCommand(this));
        if (commandConfig.getBoolean("nexus.command.wp"))
            getCommand("wp").setExecutor(new WpCommand(this));
        if (commandConfig.getBoolean("nexus.command.back"))
            getCommand("back").setExecutor(new BackCommand(this));
        if (commandConfig.getBoolean("nexus.command.heal"))
            getCommand("heal").setExecutor(new HealCommand(this));
        if (commandConfig.getBoolean("nexus.command.kill"))
            getCommand("kill").setExecutor(new KillCommand(this));
        if (commandConfig.getBoolean("nexus.command.level"))
            getCommand("level").setExecutor(new LevelCommand(this));
        if (commandConfig.getBoolean("nexus.command.item"))
            getCommand("item").setExecutor(new ItemCommand(this));
        if (commandConfig.getBoolean("nexus.command.broadcast"))
            getCommand("broadcast").setExecutor(new BroadcastCommand(this));
        if (commandConfig.getBoolean("nexus.command.msg")) {
            getCommand("msg").setExecutor(new MsgCommand(this));
            getCommand("reply").setExecutor(new ReplyCommand(this));
        }
        if (commandConfig.getBoolean("nexus.command.mute")) {
            getCommand("mute").setExecutor(new MuteCommand(this));
            getCommand("unmute").setExecutor(new UnmuteCommand(this));
        }
        if (commandConfig.getBoolean("nexus.command.inv"))
            getCommand("inv").setExecutor(new InvCommand(this));
        
        //check yaml versions
        try {
            if (!commandConfig.getString("nexus.version").equals("c")) {
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
    
    //
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
    
    //
    public void saveCommandConfig() {
        if (commandConfig == null || commandConfigFile == null)
            return;
        try {
            commandConfig.save(commandConfigFile);
        } catch (IOException e) {
            log.severe("Unable to save command config to " + commandConfigFile + '.');
        }
    }
    
    //
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
    
    //
    static public void saveSpawnConfig() {
        if (spawnConfig == null || spawnConfigFile == null)
            return;
        try {
            spawnConfig.save(spawnConfigFile);
        } catch (IOException e) {
            log.severe("Unable to save spawn config to " + spawnConfigFile + '.');
        }
    }
    
    //
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
    
    //
    static public void saveWaypointConfig() {
        if (waypointConfig == null || waypointConfigFile == null)
            return;
        try {
            waypointConfig.save(waypointConfigFile);
        } catch (IOException e) {
            log.severe("Unable to save waypoint config to " + waypointConfigFile + '.');
        }
    }
    
    //
    public FileConfiguration loadItemConfig() {
        if (itemConfig == null) {
            if (itemConfigFile == null)
                itemConfigFile = new File(this.getDataFolder(), "items.yml");
            if (itemConfigFile.exists()) {
                itemConfig = YamlConfiguration.loadConfiguration(itemConfigFile);
            } else {
                InputStream defConfigStream = getResource("items.yml");
                itemConfig = YamlConfiguration.loadConfiguration(defConfigStream);
            }
        }
        return itemConfig;
    }
    
    //
    public void saveItemConfig() {
        if (itemConfig == null || itemConfigFile == null)
            return;
        try {
            itemConfig.save(itemConfigFile);
        } catch (IOException e) {
            log.severe("Unable to save item config to " + itemConfigFile + '.');
        }
    }
    
    //
    static public boolean checkPermission(String permission, Player player, Boolean notify) {
        if (!player.hasPermission(permission)) {
            if (notify)
                player.sendMessage(ChatColor.RED + "You do not have permission.");
            return false;
        } else {
            return true;
        }
    }
}
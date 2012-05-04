package com.vioviocity.nexus;

import com.vioviocity.nexus.commands.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class Nexus extends JavaPlugin {
    
    static public Logger log = Logger.getLogger("Nexus");
    
    static public FileConfiguration commandConfig = null;
    static File commandConfigFile = null;
    static public FileConfiguration spawnConfig = null;
    static File spawnConfigFile = null;
    static public FileConfiguration waypointConfig = null;
    static File waypointConfigFile = null;
    static public FileConfiguration homeConfig = null;
    static File homeConfigFile = null;
    static public FileConfiguration itemConfig = null;
    static File itemConfigFile = null;
    static public FileConfiguration kitConfig = null;
    static File kitConfigFile = null;
    
    static public Map<String,String> itemList = new HashMap<String,String>(1000);
    
    /*
     * Special thanks to Dark_Balor for "getPrivateField()"
     */
    
    // get private field of another class
    private Object getPrivateField(Object object, String field) throws SecurityException,
            NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
        Class<?> clazz = object.getClass();
        Field objectField = clazz.getDeclaredField(field);
        objectField.setAccessible(true);
        Object result = objectField.get(object);
        objectField.setAccessible(false);
        return result;
    }
    
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
        loadHomeConfig();
        saveHomeConfig();
        loadKitConfig();
        saveKitConfig();
	
        // load item list
        loadItemConfig();
        saveItemConfig();
        for (String each : itemConfig.getStringList("nexus.items")) {
            String id = each.substring(0, each.indexOf(","));
            String item = each.substring(each.indexOf(",") + 1);
            itemList.put(item, id);
        }
	
        // register commands based on config
	getCommand("back").setExecutor(new BackCommand(this));
	getCommand("ban").setExecutor(new BanCommand(this));
	getCommand("broadcast").setExecutor(new BroadcastCommand(this));
	getCommand("heal").setExecutor(new HealCommand(this));
	getCommand("home").setExecutor(new HomeCommand(this));
	getCommand("inv").setExecutor(new InvCommand(this));
	getCommand("ip").setExecutor(new IpCommand(this));
	getCommand("item").setExecutor(new ItemCommand(this));
	getCommand("kick").setExecutor(new KickCommand(this));
	getCommand("kill").setExecutor(new KillCommand(this));
	getCommand("kit").setExecutor(new KitCommand(this));
	getCommand("level").setExecutor(new LevelCommand(this));
	getCommand("mode").setExecutor(new ModeCommand(this));
	getCommand("msg").setExecutor(new MsgCommand(this));
	getCommand("mute").setExecutor(new MuteCommand(this));
	getCommand("online").setExecutor(new OnlineCommand(this));
	getCommand("reply").setExecutor(new ReplyCommand(this));
	getCommand("spawn").setExecutor(new SpawnCommand(this));
        getCommand("time").setExecutor(new TimeCommand(this));
	getCommand("tp").setExecutor(new TpCommand(this));
	getCommand("tpc").setExecutor(new TpcCommand(this));
	getCommand("tpr").setExecutor(new TprCommand(this));
	getCommand("unban").setExecutor(new UnbanCommand(this));
	getCommand("unmute").setExecutor(new UnmuteCommand(this));
        getCommand("weather").setExecutor(new WeatherCommand(this));
        getCommand("wp").setExecutor(new WpCommand(this));

	// unregister commands based on config
	Set<String> commandList = Collections.EMPTY_SET;
	if (commandConfig.isConfigurationSection("nexus.command"))
	    commandList = commandConfig.getConfigurationSection("nexus.command").getKeys(false);
	for (String each : commandList) {
	    if (!commandConfig.getBoolean("nexus.command." + each))
		unRegisterCommand(getServer().getPluginCommand(each));
	}
	
        // check yaml versions
        try {
            if (!commandConfig.getString("nexus.version").equals("g")) {
                log.warning("Nexus\\commands.yml is out of date!.");
                log.warning("- Backup file data, delete .yml file, then restart server.");
            }
        } catch (NullPointerException e) {
            log.warning("Nexus\\commands.yml is out of date!");
            log.warning("- Backup file data, delete .yml file, then restart server.");
        }
        
        // metrics
        try {
            Metrics metrics = new Metrics(this);
            metrics.start();
        } catch (IOException e) {
            log.warning("[Nexus] Failed to submit metrics.");
        }
        
        // plugin enabled
        log.info(this + " is now enabled.");
    }
    
    // load command configuration
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
    
    // save command configuration
    public void saveCommandConfig() {
        if (commandConfig == null || commandConfigFile == null)
            return;
        try {
            commandConfig.save(commandConfigFile);
        } catch (IOException e) {
            log.severe("Unable to save command config to " + commandConfigFile + '.');
        }
    }
    
    // load spawn configuration
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
    
    // save spawn configuration
    static public void saveSpawnConfig() {
        if (spawnConfig == null || spawnConfigFile == null)
            return;
        try {
            spawnConfig.save(spawnConfigFile);
        } catch (IOException e) {
            log.severe("Unable to save spawn config to " + spawnConfigFile + '.');
        }
    }
    
    // load waypoint configuration
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
    
    // save waypoint configuration
    static public void saveWaypointConfig() {
        if (waypointConfig == null || waypointConfigFile == null)
            return;
        try {
            waypointConfig.save(waypointConfigFile);
        } catch (IOException e) {
            log.severe("Unable to save waypoint config to " + waypointConfigFile + '.');
        }
    }
    
    // load home configuration
    public FileConfiguration loadHomeConfig() {
        if (homeConfig == null) {
            if (homeConfigFile == null)
                homeConfigFile = new File(this.getDataFolder(), "homes.yml");
            if (homeConfigFile.exists()) {
                homeConfig = YamlConfiguration.loadConfiguration(homeConfigFile);
            } else {
                InputStream defConfigStream = getResource("homes.yml");
                homeConfig = YamlConfiguration.loadConfiguration(defConfigStream);
            }
        }
        return homeConfig;
    }
    
    // save home configuration
    static public void saveHomeConfig() {
        if (homeConfig == null || homeConfigFile == null)
            return;
        try {
            homeConfig.save(homeConfigFile);
        } catch (IOException e) {
            log.severe("Unable to save home config to " + homeConfigFile + '.');
        }
    }
    
    // load item configuration
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
    
    // save item configuration
    public void saveItemConfig() {
        if (itemConfig == null || itemConfigFile == null)
            return;
        try {
            itemConfig.save(itemConfigFile);
        } catch (IOException e) {
            log.severe("Unable to save item config to " + itemConfigFile + '.');
        }
    }
    
    // load kit configuration
    public FileConfiguration loadKitConfig() {
        if (kitConfig == null) {
            if (kitConfigFile == null)
                kitConfigFile = new File(this.getDataFolder(), "kits.yml");
            if (kitConfigFile.exists()) {
                kitConfig = YamlConfiguration.loadConfiguration(kitConfigFile);
            } else {
                InputStream defConfigStream = getResource("kits.yml");
                kitConfig = YamlConfiguration.loadConfiguration(defConfigStream);
            }
        }
        return kitConfig;
    }    
    
    // save kit configuration
    public void saveKitConfig() {
        if (kitConfig == null || kitConfigFile == null)
            return;
        try {
            kitConfig.save(kitConfigFile);
        } catch (IOException e) {
            log.severe("Unable to save kit config to " + kitConfigFile + '.');
        }
    }
    
    // check permission node
    static public boolean checkPermission(String permission, Player player, Boolean notify) {
        if (!player.hasPermission(permission)) {
            if (notify) {
                player.sendMessage(ChatColor.RED + "You do not have permission.");
                log.info("[Nexus] " + player.getName() + " was denied permission to " + permission + '.');
            }
            return false;
        } else {
            return true;
        }
    }
    
    /*
     * special thanks to Dark_Balor for "unRegisterCommand()"
     */
    
    // disable command
    private void unRegisterCommand(PluginCommand cmd) {
        try {
            Object result = getPrivateField(getServer().getPluginManager(), "commandMap");
            SimpleCommandMap commandMap = (SimpleCommandMap) result;
            Object map = getPrivateField(commandMap, "knownCommands");
            @SuppressWarnings("unchecked")
            HashMap<String, Command> knownCommands = (HashMap<String, Command>) map;
            knownCommands.remove(cmd.getName());
            for (String alias : cmd.getAliases())
                knownCommands.remove(alias);
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
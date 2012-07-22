package co.viocode.nexus;

import co.viocode.nexus.commands.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.*;
import java.util.logging.Logger;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class Nexus extends JavaPlugin implements Listener {

	// init logger
	public static final Logger log = Logger.getLogger("Nexus");

	// init configs
    public static FileConfiguration commandConfig = null;
    static File commandConfigFile = null;
    public static FileConfiguration spawnConfig = null;
    static File spawnConfigFile = null;

	// init global vars
	static public Map<Player,World> deathWorld = new HashMap<Player,World>(20);
	static public Map<Player,Player> teleportRequest = new HashMap<Player,Player>(200);
	static public Set<Player> teleportToggle = new HashSet<Player>(200);
	static public Map<Player,Player> reply = new HashMap<Player,Player>(200);
	static public Set<Player> mute = new HashSet<Player>(200);

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

		// plugin disabled
        log.warning("[Nexus] Now disabled!");
    }

    @Override
    public void onEnable() {

		// setup config files
		loadCommandConfig();
		saveCommandConfig();
		loadSpawnConfig();
		saveSpawnConfig();

        // register events
        getServer().getPluginManager().registerEvents(new EventListener(), this);

		// register commands
		//getCommand("back").setExecutor(new Back(this));
		getCommand("ban").setExecutor(new Ban(this));
		getCommand("broadcast").setExecutor(new Broadcast(this));
		getCommand("heal").setExecutor(new Heal(this));
		//getCommand("home").setExecutor(new Home(this));
		getCommand("inv").setExecutor(new Inventory(this));
		//getCommand("item").setExecutor(new Item(this));
		getCommand("kick").setExecutor(new Kick(this));
		getCommand("kill").setExecutor(new Kill(this));
		//getCommand("kit").setExecutor(new Kit(this));
		getCommand("level").setExecutor(new Level(this));
		getCommand("mode").setExecutor(new Mode(this));
		getCommand("mute").setExecutor(new Mute(this));
		getCommand("online").setExecutor(new Online(this));
		getCommand("r").setExecutor(new Reply(this));
		getCommand("spawn").setExecutor(new Spawn(this));
		getCommand("time").setExecutor(new Time(this));
		getCommand("tp").setExecutor(new Teleport(this));
		getCommand("tpc").setExecutor(new TeleportC(this));
		getCommand("tpr").setExecutor(new TeleportR(this));
		getCommand("unban").setExecutor(new Unban(this));
		getCommand("unmute").setExecutor(new Unmute(this));
		getCommand("w").setExecutor(new Whisper(this));
		getCommand("weather").setExecutor(new Weather(this));
		//getCommand("wp").setExecutor(new Waypoint(this));

		// unregister commands based on config
		Set <String> commandList = Collections.EMPTY_SET;
		if (commandConfig.isConfigurationSection("command"))
			commandList = commandConfig.getConfigurationSection("command").getKeys(false);
		for (String each : commandList)
			if (!commandConfig.getBoolean("command." + each))
				unRegisterCommand(getServer().getPluginCommand(each));

		// check commands.yml version
		try {
			if (!commandConfig.getString("version").equals("0.4.0")) {
				log.warning("[Nexus] \\commands.yml is out of date!");
				log.warning("[Nexus] Delete the file and restart server!");
			}
		} catch (NullPointerException e) {
			log.warning("[Nexus] \\commands.yml is out of date!");
			log.warning("[Nexus] Delete the file and restart server!");
		}

		// metrics
		try {
			Metrics metrics = new Metrics(this);
			metrics.start();
			log.info("[Nexus] This plugin collects anonymous statistical data.");
		} catch (IOException e) {
			log.warning("[Nexus] Failed to submit metrics.");
		}

		// plugin enabled
        log.info("[Nexus] Now enabled!");
    }

	// load command config
	public FileConfiguration loadCommandConfig() {
		if (commandConfig == null) {
			if (commandConfigFile == null)
				commandConfigFile = new File(this.getDataFolder(), "commands.yml");
			if (commandConfigFile.exists()) {
				commandConfig = YamlConfiguration.loadConfiguration(commandConfigFile);
			} else {
				InputStream defaultStream = getResource("commands.yml");
				commandConfig = YamlConfiguration.loadConfiguration(defaultStream);
			}
		}
		return commandConfig;
	}

	// save command config
	public void saveCommandConfig() {
		if (commandConfig == null || commandConfigFile == null)
			return;
		try {
			commandConfig.save(commandConfigFile);
		} catch (IOException e) {
			log.severe("[Nexus] Unable to save command config to " + commandConfigFile);
		}
	}

	// load spawn config
	public FileConfiguration loadSpawnConfig() {
		if (spawnConfig == null) {
			if (spawnConfigFile == null)
				spawnConfigFile = new File(this.getDataFolder(), "spawn.yml");
			if (spawnConfigFile.exists()) {
				spawnConfig = YamlConfiguration.loadConfiguration(spawnConfigFile);
			} else {
				InputStream defaultStream = getResource("spawn.yml");
				spawnConfig = YamlConfiguration.loadConfiguration(defaultStream);

				Location spawn = getServer().getWorlds().get(0).getSpawnLocation();
				spawnConfig.set(spawn.getWorld().getName() + ".x", spawn.getX());
				spawnConfig.set(spawn.getWorld().getName() + ".y", spawn.getY());
				spawnConfig.set(spawn.getWorld().getName() + ".z", spawn.getZ());
			}
		}
		return spawnConfig;
	}

	// save spawn config
	static public void saveSpawnConfig() {
		if (spawnConfig == null || spawnConfigFile == null)
			return;
		try {
			spawnConfig.save(spawnConfigFile);
		} catch (IOException e) {
			log.severe("[Nexus] Unable to save spawn config to " + spawnConfigFile);
		}
	}

	// check permission
	static public boolean checkPermission(String permission, Player player) {
		if (!player.hasPermission(permission)) {
			player.sendMessage(ChatColor.RED + "You do not have permission.");
			log.info("[Nexus] " + player.getName() + " was denied permission to " + permission);
			return false;
		}
		return true;
	}

	// check if player
	static public boolean checkPlayer(CommandSender sender) {
		if (sender instanceof Player)
			return true;
		else
			sender.sendMessage(ChatColor.RED + "[Nexus] Command must be issued within game.");
		return false;
	}

	// find online player
	static public Player findOnlinePlayer(String name) {
		for (Player each : Bukkit.getOnlinePlayers())
			if (each.getName().toLowerCase().contains(name.toLowerCase()))
				return each;
		return null;
	}

	// fine offline player
	static public OfflinePlayer findOfflinePlayer(String name) {
		for (OfflinePlayer each : Bukkit.getOfflinePlayers())
			if (each.getName().toLowerCase().contains(name.toLowerCase()))
				return each;
		return null;
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
        } catch (IllegalArgumentException e) {
        } catch (NoSuchFieldException e) {
        } catch (IllegalAccessException e) {
        }
    }
}
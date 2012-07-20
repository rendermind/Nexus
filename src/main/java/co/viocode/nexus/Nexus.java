package co.viocode.nexus;

import co.viocode.nexus.commands.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.Collections;
import java.util.HashMap;
import java.util.Set;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class Nexus extends JavaPlugin implements Listener {

	public static final Logger log = Logger.getLogger("Nexus");

    public static FileConfiguration commandConfig = null;
    static File commandConfigFile = null;

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
        log.warning("[Nexus] Disabled!");
    }

    @Override
    public void onEnable() {

		// setup config files
		loadCommandConfig();
		saveCommandConfig();

        // register events
        getServer().getPluginManager().registerEvents(new EventListener(), this);

		// register commands
		//getCommand("back").setExecutor(new Back(this));
		//getCommand("ban").setExecutor(new Ban(this));
		getCommand("broadcast").setExecutor(new Broadcast(this));
		//getCommand("heal").setExecutor(new Heal(this));
		//getCommand("home").setExecutor(new Home(this));
		//getCommand("inv").setExecutor(new Inventory(this));
		//getCommand("item").setExecutor(new Item(this));
		//getCommand("kick").setExecutor(new Kick(this));
		//getCommand("kill").setExecutor(new Kill(this));
		//getCommand("kit").setExecutor(new Kit(this));
		//getCommand("level").setExecutor(new Level(this));
		getCommand("mode").setExecutor(new Mode(this));
		//getCommand("msg").setExecutor(new Msg(this));
		//getCommand("mute").setExecutor(new Mute(this));
		getCommand("online").setExecutor(new Online(this));
		//getCommand("r").setExecutor(new Reply(this));
		//getCommand("spawn").setExecutor(new Spawn(this));
		getCommand("time").setExecutor(new Time(this));
		//getCommand("tp").setExecutor(new Teleport(this));
		//getCommand("unban").setExecutor(new Unban(this));
		//getCommand("unmute").setExecutor(new Unmute(this));
		getCommand("weather").setExecutor(new Weather(this));
		//getCommand("wp").setExecutor(new Waypoint(this));

		// unregister commands based on config
		Set <String> commandList = Collections.EMPTY_SET;
		if (commandConfig.isConfigurationSection("nexus.command"))
			commandList = commandConfig.getConfigurationSection("nexus.command").getKeys(false);
		for (String each : commandList)
			if (!commandConfig.getBoolean("nexus.command." + each))
				unRegisterCommand(getServer().getPluginCommand(each));

		// check commands.yml version
		try {
			if (!commandConfig.getString("nexus.version").equals("0.4.0")) {
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
		} catch (IOException e) {
			log.warning("[Nexus] Failed to submit metrics.");
		}

		// plugin enabled
        log.info("[Nexus] Enabled!");
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

	// check permission
	static public boolean checkPermission(String permission, Player player) {
		if (!player.hasPermission(permission)) {
			player.sendMessage(ChatColor.RED + "You do not have permission.");
			log.info("[Nexus] " + player.getName() + " was denied permission to " + permission);
			return false;
		}
		return true;
	}

	// find player
	static public Player findPlayer(String name) {
		for (Player each : Bukkit.getOnlinePlayers())
			if (each.getName().toLowerCase().contains(name))
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
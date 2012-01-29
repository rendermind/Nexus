package com.vioviocity.nexus;

import java.util.logging.Logger;
import org.bukkit.plugin.java.JavaPlugin;

public class Nexus extends JavaPlugin {
    
    Logger log = Logger.getLogger("Nexus");
    private NexusCommands myExecutor;
    
    public void onDisable() {
        log.info(this + " is now disabled.");
    }

    public void onEnable() {
        log.info(this + " is now enabled.");
        
        myExecutor = new NexusCommands(this);
        getCommand("test").setExecutor(myExecutor);
        getCommand("time").setExecutor(myExecutor);
        getCommand("weather").setExecutor(myExecutor);
        getCommand("mode").setExecutor(myExecutor);
        getCommand("online").setExecutor(myExecutor);
        getCommand("kick").setExecutor(myExecutor);
        getCommand("ban").setExecutor(myExecutor);
        getCommand("unban").setExecutor(myExecutor);
        getCommand("tp").setExecutor(myExecutor);
        getCommand("tpr").setExecutor(myExecutor);
    }
}

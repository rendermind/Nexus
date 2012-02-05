package com.vioviocity.nexus;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class NexusCommands implements CommandExecutor {

    // initialize global variables
    static List<String> tpRequest = new ArrayList<String>();
    static List<String> tpToggle = new ArrayList<String>();
    static List<String> tpBack = new ArrayList<String>();
    
    private Nexus plugin;
    public NexusCommands(Nexus plugin) {
        this.plugin = plugin;
    }
    
    public boolean onCommand(CommandSender sender, Command cmd, String alias, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Command must be called by player.");
            return true;
        }

        // initialize core variables
        Player player = (Player) sender;
        World world = player.getWorld();
        Player onlinePlayers[];
        OfflinePlayer bannedPlayers[];
        onlinePlayers = plugin.getServer().getOnlinePlayers();
        bannedPlayers = plugin.getServer().getOfflinePlayers();
        
        // ----- HELP -----
        /*if (cmd.getName().equalsIgnoreCase("help")) {
            // permission check
            if (!checkPermission("nexus.help", player))
                return true;
            
            // help (no args)
            if (args.length == 0) {
                plugin.getDescription().getCommands();
                
            // help (command)
            } else if (args.length == 1) {
                
                
            // invalid args
            } else {
                return false;
            }
            
            return true;
        }*/
        
        // ----- TIME -----
        if (cmd.getName().equalsIgnoreCase("time") && Nexus.commandConfig.getBoolean("nexus.commands.time")) {
            // permission check
            if (!checkPermission("nexus.time.check", player))
                return true;
            
            // time (no args)
            if (args.length == 0) {
                int tick = (int) player.getWorld().getTime();
                int hour = tick / 1000;
                // calculate true hour
                hour += 6;
                if (hour > 23) {
                    hour -= 24;
                }
                int minute = (tick - ((int)(tick / 1000) * 1000)) / (1000 / 60);
                if (minute < 10) {
                    player.sendMessage(ChatColor.GREEN + "Time: " + hour + ":0" + minute + " / Tick: " + tick);
                } else {
                    player.sendMessage(ChatColor.GREEN + "Time: " + hour + ":" + minute + " / Tick: " + tick);
                }
                
            // permission check
            if (!checkPermission("nexus.time.set", player))
                return true;
                        
            // time [dawn|day|dusk|night]
            } else if (args.length == 1) {
                String time = args[0].toLowerCase();
                if (time.equals("dawn")) {
                    player.getWorld().setTime(22200);
                } else if (time.equals("day")) {
                    player.getWorld().setTime(0);
                } else if (time.equals("dusk")) {
                    player.getWorld().setTime(12000);
                } else if (time.equals("night")) {
                    player.getWorld().setTime(13800);
                    
                // invalid args
                } else {
                    return false;
                }
                
            // invalid args
            } else {
                return false;
            }
            
            return true;
        }
        
        // ----- WEATHER -----
        if (cmd.getName().equalsIgnoreCase("weather") && Nexus.commandConfig.getBoolean("nexus.commands.weather")) {
            // permission check
            if (!checkPermission("nexus.weather.check", player))
                return true;
            
            // weather (no args)
            if (args.length == 0) {
                int tick = (int) player.getWorld().getWeatherDuration();
                int minutes = (tick / 20) / 60;
                if (world.hasStorm()) {
                    player.sendMessage(ChatColor.GREEN + "Weather: Storming,  Duration: " + minutes + " minute(s)");
                } else {
                    player.sendMessage(ChatColor.GREEN + "Weather: Clear,  Duration: " + minutes + " minute(s)");
                }
                
            // permission check
            if (!checkPermission("nexus.weather.set", player))
                return true;
            
            // weather [clear|storm|thunder]
            } else if (args.length == 1) {
                String weather = args[0].toLowerCase();
                if (weather.equals("clear")) {
                    world.setStorm(false);
                    world.setThundering(false);
                } else if (weather.equals("storm")) {
                    world.setStorm(true);
                    world.setThundering(false);
                } else if (weather.equals("thunder")) {
                    world.setStorm(true);
                    world.setThundering(true);
                    
                // invalid args
                } else {
                    return false;
                }
                
            // invalid args
            } else {
                return false;
            }
            
            return true;
        }
        
        // ----- SPAWN -----
        if (cmd.getName().equalsIgnoreCase("spawn") && Nexus.commandConfig.getBoolean("nexus.commands.spawn")) {
            // permission check
            if (!checkPermission("nexus.spawn", player))
                return true;
            
            // spawn (no args)
            if (args.length == 0) {
                Location spawn = player.getLocation();
                spawn.setWorld(plugin.getServer().getWorld(Nexus.spawnConfig.getString("nexus.spawn.world")));
                spawn.setX(Nexus.spawnConfig.getDouble("nexus.spawn.x"));
                spawn.setY(Nexus.spawnConfig.getDouble("nexus.spawn.y"));
                spawn.setZ(Nexus.spawnConfig.getDouble("nexus.spawn.z"));
                spawn.setYaw((float) Nexus.spawnConfig.getDouble("nexus.spawn.yaw"));
                spawn.setPitch((float) Nexus.spawnConfig.getDouble("nexus.spawn.pitch"));
                player.teleport(spawn);
                return true;
                
            // spawn [set]
            } else if (args.length == 1) {
                Location spawn = player.getLocation();
                Nexus.spawnConfig.set("nexus.spawn.world", spawn.getWorld().getName());
                Nexus.spawnConfig.set("nexus.spawn.x", spawn.getX());
                Nexus.spawnConfig.set("nexus.spawn.y", spawn.getY());
                Nexus.spawnConfig.set("nexus.spawn.z", spawn.getZ());
                Nexus.spawnConfig.set("nexus.spawn.yaw", spawn.getYaw());
                Nexus.spawnConfig.set("nexus.spawn.pitch", spawn.getPitch());
                Nexus.saveSpawnConfig();
                player.sendMessage(ChatColor.GREEN + "Spawn location set.");
                return true;
                
            // invalid args
            } else {
                return false;
            }
        }
        
        // ----- MODE -----
        if (cmd.getName().equalsIgnoreCase("mode") && Nexus.commandConfig.getBoolean("nexus.commands.mode")) {
            // permission check
            if (!checkPermission("nexus.mode", player))
                return true;
            
            // mode (no args)
            if (args.length == 0) {
                if (player.getGameMode() == GameMode.SURVIVAL) {
                    player.setGameMode(GameMode.CREATIVE);
                } else {
                    player.setGameMode(GameMode.SURVIVAL);
                }
                
                return true;
                
            // mode (player)            
            } else if (args.length == 1) {
                String modeName = args[0].toLowerCase();
                for (Player each : onlinePlayers) {
                    if (each.getName().toLowerCase().contains(modeName)) {
                        if (each.getGameMode() == GameMode.SURVIVAL) {
                            each.setGameMode(GameMode.CREATIVE);
                            player.sendMessage(ChatColor.GREEN + each.getName() + " game mode set to creative.");
                        } else {
                            each.setGameMode(GameMode.SURVIVAL);
                            player.sendMessage(ChatColor.GREEN + each.getName() + " game mode set to survival.");
                        }
                        
                        return true;
                    }
                }
                
                // player not online
                player.sendMessage(ChatColor.RED + modeName + " is not online.");
            
            // invalid args
            } else {
                return false;
            }
            
            return true;
        }
        
        // ----- ONLINE -----
        if (cmd.getName().equalsIgnoreCase("online") && Nexus.commandConfig.getBoolean("nexus.commands.online")) {
            // permission check
            if (!checkPermission("nexus.online", player))
                return true;
            
            // invalid args
            if (args.length > 0)
                return false;
            
            // online
            String playerList = "";
            for (Player each : onlinePlayers) {
                playerList += each.getName() + ", ";
            }
            playerList = playerList.substring(0, playerList.length() - 2);
            player.sendMessage(ChatColor.GREEN + "Online Players [" + onlinePlayers.length + '/' +
                    plugin.getServer().getMaxPlayers() + "]: " + ChatColor.WHITE + playerList);
            
            return true;
        }
        
        // ----- KICK -----
        if (cmd.getName().equalsIgnoreCase("kick") && Nexus.commandConfig.getBoolean("nexus.commands.kick")) {
            // permission check
            if (!checkPermission("nexus.kick", player))
                return true;
            
            // kick (player)
            if (args.length == 1) {
                String kickName = args[0].toLowerCase();
                for (Player each : onlinePlayers) {
                    if (each.getName().toLowerCase().contains(kickName)) {
                        each.kickPlayer("You have been kicked.");
                        plugin.getServer().broadcastMessage(ChatColor.RED + each.getName() + " has been kicked.");
                        
                        return true;
                    }
                }
                
                // player not online
                player.sendMessage(ChatColor.RED + kickName + " is not online.");
                
            // invalid args
            } else {
                return false;
            }
            
            return true;
        }
         
        // ----- BAN -----
        if (cmd.getName().equalsIgnoreCase("ban") && Nexus.commandConfig.getBoolean("nexus.commands.ban")) {
            // permission check
            if (!checkPermission("nexus.ban", player))
                return true;
            
            // ban (player)
            if (args.length == 1) {
                String banName = args[0].toLowerCase();
                for (Player each : onlinePlayers) {
                    if (each.getName().contains(banName)) {
                        each.setBanned(true);
                        each.kickPlayer("You have been banned.");
                        plugin.getServer().broadcastMessage(ChatColor.RED + each.getName() + " has been banned.");
                        
                        return true;
                    }
                }
            
                // player not online
                player.sendMessage(ChatColor.RED + banName + " is not online.");
                
            // invalid args
            } else {
                return false;
            }
                
            return true;
        }
        
        // ----- UNBAN -----
        if (cmd.getName().equalsIgnoreCase("unban") && Nexus.commandConfig.getBoolean("nexus.commands.ban")) {
            // permission check
            if (!checkPermission("nexus.ban", player))
                return true;
            
            // unban (player)
            if (args.length == 1) {
                String banName = args[0].toLowerCase();
                for (OfflinePlayer each : bannedPlayers) {
                    if (each.getName().contains(banName)) {
                        each.setBanned(false);
                        plugin.getServer().broadcastMessage(ChatColor.GREEN + each.getName() + " has been unbanned.");
                        
                        return true;
                    }
                }
            
                // player not banned
                player.sendMessage(ChatColor.RED + banName + " is not banned.");
                    
            // invalid args
            } else {
                return false;
            }
                
            return true;
        }
        
        // ----- TP -----
        if (cmd.getName().equalsIgnoreCase("tp") && Nexus.commandConfig.getBoolean("nexus.commands.tp")) {
            // permission check
            if (!checkPermission("nexus.tp", player))
                return true;
            
            if (args.length == 1) {
                //tp [toggle]
                if (args[0].equalsIgnoreCase("toggle")) {
                    // permission check
                    if (!checkPermission("nexus.tp.toggle", player))
                        return true;

                    // check for existing toggle
                    for (String each : tpToggle) {
                        String tpName = each.substring(0, each.indexOf(","));
                        String tpAble = each.substring(each.indexOf(",") + 1);
                        if (player.getName().equalsIgnoreCase(tpName)) {
                            if (tpAble.equals("true")) {
                                tpToggle.set(tpToggle.indexOf(each), player.getName() + ",false");
                                player.sendMessage(ChatColor.RED + "Teleporting has been disabled.");
                            } else {
                                tpToggle.set(tpToggle.indexOf(each), player.getName() + ",true");
                                player.sendMessage(ChatColor.GREEN + "Teleporting has been enabled.");
                            }

                            return true;
                        }
                    }
                    
                    // create toggle
                    tpToggle.add(player.getName() + ",false");
                    player.sendMessage(ChatColor.RED + "Teleporting has been disabled.");
                    
                    return true;
                }
                
                //tp (player)
                String tpName = args[0].toLowerCase();
                for (Player each : onlinePlayers) {
                    if (each.getName().toLowerCase().contains(tpName)) {
                        
                        // check tp toggle
                        if (!checkTpToggle(each)) {
                            player.sendMessage(ChatColor.RED + each.getName() + " has teleporting disabled.");
                            return true;
                        }
                        
                        // create tp toggle
                        player.teleport(each);
                        
                        return true;
                    }
                }
                
                // player not online
                player.sendMessage(ChatColor.RED + tpName + " is not online.");
                
            // tp (player) (player)
            } else if (args.length == 2) {
                String tp1Name = args[0].toLowerCase();
                String tp2Name = args[1].toLowerCase();
                for (Player each1 : onlinePlayers) {
                    if (each1.getName().toLowerCase().contains(tp1Name)) {
                        for (Player each2 : onlinePlayers) {
                            if (each2.getName().toLowerCase().contains(tp2Name)) {
                                
                                // check tp toggle
                                if (!checkTpToggle(each2)) {
                                    player.sendMessage(ChatColor.RED + each2.getName() + " has teleporting disabled.");
                                    return true;
                                }
                                
                                each1.teleport(each2);
                                player.sendMessage(ChatColor.GREEN + each1.getName() + " was teleported to " +
                                        each2.getName() + '.');
                                
                                return true;
                            }
                        }
                    }
                }
                
                // player not online
                player.sendMessage(ChatColor.RED + tp1Name + " or " + tp2Name + " is not online.");
                
            // invalid args
            } else {
                return false;
            }
            
            return true;
        }
        
        // ----- TPR -----
        if (cmd.getName().equalsIgnoreCase("tpr") && Nexus.commandConfig.getBoolean("nexus.commands.tpr")) {
            // permission check
            if (!checkPermission("nexus.tpr", player))
                return true;
            
            if (args.length == 1) {
                String tpr = args[0].toLowerCase();
                // tpr [accept]
                if (tpr.equals("accept")) {
                    
                    // check tp request
                    String tpToName;
                    String tpFromName;
                    for (String each : tpRequest) {
                        tpToName = each.substring(each.indexOf(',') + 1);
                        if (player.getName().equals(tpToName)) {
                            tpFromName = each.substring(0, each.indexOf(','));
                            for (Player each2 : onlinePlayers) {
                                if (each2.getName().equals(tpFromName)) {
                                    each2.teleport(player);
                                    player.sendMessage(ChatColor.GREEN + "Accepted teleport request.");
                                    tpRequest.remove(tpFromName + ',' + tpToName);
                                    return true;
                                }
                            }
                            break;
                        }
                    }
                    
                    // no pending request
                    player.sendMessage(ChatColor.RED + "You do not have a pending teleport request.");
                    return true;
                    
                // tpr [deny]
                } else if (tpr.equals("deny")) {
                    String tpToName;
                    String tpFromName;
                    for (String each : tpRequest) {
                        tpToName = each.substring(each.indexOf(',') + 1);
                        if (player.getName().equals(tpToName)){
                            tpFromName = each.substring(0, each.indexOf(','));
                            for (Player each2 : onlinePlayers) {
                                if (each2.getName().equals(tpFromName)) {
                                    player.sendMessage(ChatColor.RED + "Denied teleport request.");
                                    each2.sendMessage(ChatColor.RED + "Your teleport request has been denied.");
                                    tpRequest.remove(tpFromName + ',' + tpToName);
                                    return true;
                                }
                            }
                        }
                    }
                    
                    // no pending request
                    player.sendMessage(ChatColor.RED + "You do not have a pending teleport request.");
                    return true;
                    
                // tpr [cancel]
                } else if (tpr.equals("cancel")) {
                    
                    String tpToName;
                    String tpFromName;
                    for (String each : tpRequest) {
                        tpToName = each.substring(0, each.indexOf(','));
                        if (player.getName().equalsIgnoreCase(tpToName)) {
                            tpFromName = each.substring(each.indexOf(',') + 1);
                            player.sendMessage(ChatColor.RED + "Cancelled teleport request.");
                            tpRequest.remove(tpToName + ',' + tpFromName);
                            return true;
                        }
                    }
                    
                    // no pending request
                    player.sendMessage(ChatColor.RED + "You have not sent a teleport request.");
                    return true;
                }
                
                // tpr (player)
                String tpToName = args[0].toLowerCase();
                String tpFromName;
                for (Player each : onlinePlayers) {
                    if (each.getName().toLowerCase().contains(tpToName)) {
                        
                        // check tp toggle
                        if (!checkTpToggle(each)) {
                            player.sendMessage(ChatColor.RED + each.getName() + " has teleporting disabled");
                            return true;
                        }
                        
                        for (String each2 : tpRequest) {
                            tpFromName = each2.substring(0, each2.indexOf(','));
                            if (player.getName().equalsIgnoreCase(tpFromName)) {
                                tpRequest.set(tpRequest.indexOf(each2), player.getName() + ',' + each.getName());
                                player.sendMessage(ChatColor.GREEN + "Teleport request sent to " + each.getName() + '.');
                                each.sendMessage(ChatColor.RED + "Received teleport request from " + player.getName() + '.');
                                return true;
                            }
                        }
                        tpRequest.add(player.getName() + ',' + each.getName());
                        player.sendMessage(ChatColor.GREEN + "Teleport request send to " + each.getName() + '.');
                        each.sendMessage(ChatColor.RED + "Received teleport request from " + player.getName() + '.');
                        return true;
                    }
                }
                
                // player not online
                player.sendMessage(ChatColor.RED + tpToName + " is not online.");
                
            // invalid args
            } else {
                return false;
            }
            
            return true;
        }
        
        // ----- BACK -----
        if (cmd.getName().equalsIgnoreCase("back") && Nexus.commandConfig.getBoolean("nexus.commands.back")) {
            // permission check
            if (!checkPermission("nexus.back", player))
                return false;
            
            // invalid args
            if (args.length > 0)
                return false;
            
            String tpName;
            Location tpBackLocation = player.getLocation();
            //Location tpTempLocation = player.getLocation();
            for (String each: tpBack) {
                tpName = each.substring(0, each.indexOf(','));
                if (player.getName().equalsIgnoreCase(tpName)) {
                    String worldName = each.substring(each.indexOf("map:") + 4, each.indexOf("x:"));
                    double x = Double.parseDouble(each.substring(each.indexOf("x:") + 2, each.indexOf("y:")));
                    double y = Double.parseDouble(each.substring(each.indexOf("y:") + 2, each.indexOf("z:")));
                    double z = Double.parseDouble(each.substring(each.indexOf("z:") + 2, each.indexOf("yaw:")));
                    float yaw = Float.parseFloat(each.substring(each.indexOf("yaw:") + 4, each.indexOf("pitch:")));
                    float pitch = Float.parseFloat(each.substring(each.indexOf("pitch:") + 6));
                    tpBackLocation.setX(x);
                    tpBackLocation.setY(y);
                    tpBackLocation.setZ(z);
                    tpBackLocation.setYaw(yaw);
                    tpBackLocation.setPitch(pitch);
                    for (World each2: plugin.getServer().getWorlds()) {
                        if (each2.getName().equals(worldName)) {
                            tpBackLocation.setWorld(each2);
                        }
                    }
                    player.teleport(tpBackLocation);
                    return true;
                }
            }
            
            // no tp back
            player.sendMessage(ChatColor.RED + "You do not have a prior location.");
            return true;
        }
        
        // ----- HEAL -----
        if (cmd.getName().equalsIgnoreCase("heal") && Nexus.commandConfig.getBoolean("nexus.commands.heal")) {
            // check permission
            if (!checkPermission("nexus.heal", player))
                return false;
            
            // heal (no args)
            if (args.length == 0) {
                player.setHealth(player.getMaxHealth());
                player.setFoodLevel(20);
                
            // heal (player)
            } else if (args.length == 1) {
                String healName = args[0].toLowerCase();
                for (Player each : onlinePlayers) {
                    if (each.getName().toLowerCase().contains(healName)) {
                        each.setHealth(each.getMaxHealth());
                        each.setFoodLevel(20);
                        each.sendMessage(ChatColor.GREEN + player.getName() + " has healed you.");
                        player.sendMessage(ChatColor.GREEN + "You have healed " + each.getName() + ".");
                        return true;
                    }
                }
                
                // player not online
                player.sendMessage(ChatColor.RED + healName + " is not online.");
                return true;
                
            // invalid args
            } else {
                return false;
            }
            
            return true;
        }
        
        // ----- KILL -----
        if (cmd.getName().equalsIgnoreCase("kill") && Nexus.commandConfig.getBoolean("nexus.commands.kill")) {
            // check permission
            if (!checkPermission("nexus.kill", player))
                return false;

            // kill (player)
            if (args.length == 1) {
                String killName = args[0].toLowerCase();
                for (Player each : onlinePlayers) {
                    if (each.getName().toLowerCase().contains(killName)) {
                        each.setHealth(0);
                        each.sendMessage(ChatColor.RED + player.getName() + " has killed you.");
                        return true;
                    }
                }
                
                // player not online
                player.sendMessage(ChatColor.RED + killName + " is not online.");
                return true;
                
            // invalid args
            } else {
                return false;
            }
        }
        
        // ----- LEVEL -----
        if (cmd.getName().equalsIgnoreCase("level") && Nexus.commandConfig.getBoolean("nexus.commands.level")) {
            // level (no args)
            if (args.length == 0) {
                // permission check
                if (!checkPermission("nexus.level.check", player))
                    return false;
                
                int expNext = (int) (7 + (int) (player.getLevel() * 3.5));
                int actualNext = expNext - (int) (player.getExp() * expNext) + player.getTotalExperience();
                player.sendMessage(ChatColor.GREEN + "Level: " + player.getLevel() + ", Exp: " +
                        player.getTotalExperience() + "/" + actualNext);
            
            // level (level)
            } else if (args.length == 1) {
                // permission check
                if (!checkPermission("nexus.level.set", player))
                    return false;
                int level = Integer.parseInt(args[0]);
                
                // check max level
                if (level > 10000) {
                    player.sendMessage(ChatColor.RED + "Maxiumum level is 10,000.");
                    return true;
                }

                // set player level
                player.setLevel(0);
                player.setExp(0);
                player.setTotalExperience(0);
                for (int i = 0; i < level; i ++) {
                     player.setTotalExperience(player.getTotalExperience() + (int) (7 + (int) (player.getLevel() * 3.5)));
                     player.setLevel(player.getLevel() + 1);
                }
                
            // level (player) (level)
            } else if (args.length == 2) {
                // permission check
                if (!checkPermission("nexus.level.set", player))
                    return false;
                
                String levelName = args[0].toLowerCase();
                int level = Integer.parseInt(args[1]);
                
                // check max level
                if (level > 10000) {
                    player.sendMessage(ChatColor.RED + "Maximium level is 10,000.");
                    return true;
                }
                
                for (Player each : onlinePlayers) {
                    if (each.getName().toLowerCase().contains(levelName)) {
                        
                        // set player level
                        player.setLevel(0);
                        player.setExp(0);
                        player.setTotalExperience(0);
                        for (int i = 0; i < level; i ++) {
                            player.setTotalExperience(player.getTotalExperience() + (int) (7 + (int) (player.getLevel() * 3.5)));
                            player.setLevel(player.getLevel() + 1);
                        }
                        
                        each.sendMessage(ChatColor.GREEN + player.getName() + " has set your level to " + level);
                        player.sendMessage(ChatColor.GREEN + levelName + " set to level " + level);
                        return true;
                    }
                }
                
                // player not found
                player.sendMessage(ChatColor.RED + levelName + " is not online.");
                
            // invalid args
            } else {
                return false;
            }
            
            return true;
        }
        
        return false;
    }
    
    static boolean checkPermission(String permission, Player player) {
        if (!player.hasPermission(permission)) {
            player.sendMessage(ChatColor.RED + "You do not have permission.");
            return false;
        } else {
            return true;
        }
    }
    
    private boolean checkTpToggle(Player player) {
        for (String every : tpToggle) {
            String tpName = every.substring(0, every.indexOf(','));
            String tpAble = every.substring(every.indexOf(',') + 1);
            if (player.getName().equalsIgnoreCase(tpName)) {
                if (tpAble.equals("false")) {
                    return false;
                }
            }
        }
        
        return true;
    }
    
    private double round(double value) {
        DecimalFormat newFormat = new DecimalFormat("#.#");
        return Double.valueOf(newFormat.format(value));
    }
}
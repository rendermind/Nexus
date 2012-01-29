package com.vioviocity.nexus;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

class RequestPlayer {
    public Player player;
    public int index;
}

public class NexusCommands implements CommandExecutor {

    // initialize global variables
    List<String> tpRequest = new ArrayList<String>();
    List<String> tpToggle = new ArrayList<String>();
    RequestPlayer tpRequestPlayer = new RequestPlayer();
    
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
        
        // test, runs pre-coded test
        if (cmd.getName().equalsIgnoreCase("test")) {
            for (String each : tpRequest) {
                player.sendMessage(each);
            }
            
            return true;
        }
        
        if (cmd.getName().equalsIgnoreCase("time")) {
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
        
        if (cmd.getName().equalsIgnoreCase("weather")) {
            // permission check
            if (!checkPermission("nexus.weather.check", player))
                return true;
            
            // weather (no args)
            if (args.length == 0) {
                int tick = (int) player.getWorld().getWeatherDuration();
                int minutes = (tick / 20) / 60;
                if (world.hasStorm()) {
                    player.sendMessage(ChatColor.GREEN + "Weather: Storming / Duration: " + minutes + " minute(s)");
                } else {
                    player.sendMessage(ChatColor.GREEN + "Weather: Clear / Duration: " + minutes + " minute(s)");
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
        
        if (cmd.getName().equalsIgnoreCase("mode")) {
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
        
        if (cmd.getName().equalsIgnoreCase("online")) {
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
            player.sendMessage(ChatColor.GREEN + "Online Players [" + onlinePlayers.length + "/" +
                    plugin.getServer().getMaxPlayers() + "]: " + ChatColor.WHITE + playerList);
            
            return true;
        }
        
        if (cmd.getName().equalsIgnoreCase("kick")) {
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
            
        if (cmd.getName().equalsIgnoreCase("ban")) {
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
        
        if (cmd.getName().equalsIgnoreCase("unban")) {
            // permission check
            if (!checkPermission("nexus.unban", player))
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
        
        if (cmd.getName().equalsIgnoreCase("tp")) {
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
                                        each2.getName() + ".");
                                
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
        
        if (cmd.getName().equalsIgnoreCase("tpr")) {
            // permission check
            if (!checkPermission("nexus.tpr", player))
                return true;
            
            if (args.length == 1) {
                String tpr = args[0].toLowerCase();
                // tpr [accept]
                if (tpr.equals("accept")) {
                    
                    // check tp request
                    int index = checkTpRequest(player, false);
                    if (index > -1) {
                        
                        // accept tp request
                        String tpName = tpRequest.get(index).substring(tpRequest.get(index).indexOf(",") + 1);
                        for (Player each : onlinePlayers) {
                            if (each.getName().toLowerCase().contains(tpName)) {
                                each.teleport(player);
                                break;
                            }
                        }
                        tpRequest.remove(index);
                        return true;
                        
                    // request not found
                    } else {
                        player.sendMessage(ChatColor.RED + "You do not have a pending teleport request.");
                        return true;
                    }
                    
                // tpr [deny]
                } else if (tpr.equals("deny")) {
                    
                    // check tp request
                    int index = checkTpRequest(player, false);
                    if (index > -1) {
                        
                        // deny tp request
                        String tpName = tpRequest.get(index).substring(tpRequest.get(index).indexOf(",") + 1);
                        for (Player each : onlinePlayers) {
                            if (each.getName().toLowerCase().contains(tpName)) {
                                each.sendMessage(ChatColor.RED + "Your teleport request has been denied.");
                                break;
                            }
                        }
                        tpRequest.remove(index);
                        return true;
                        
                    // request not found
                    } else {
                        player.sendMessage(ChatColor.RED + "You do not have a pending teleport request.");
                        return true;
                    }
                    
                // tpr [cancel]
                } else if (tpr.equals("cancel")) {
                    
                    // check tp request
                    int index = checkTpRequest(player, true);
                    if (index > -1) {
                        
                        // cancel tp request
                        tpRequest.remove(index);
                        return true;
                        
                    // request not found
                    } else {
                        player.sendMessage(ChatColor.RED + "You have not sent a teleport request.");
                        return true;
                    }
                }
                
                // tpr (player)
                String tpName = args[0].toLowerCase();
                for (Player each : onlinePlayers) {
                    if (each.getName().toLowerCase().contains(tpName)) {
                        
                        // check tp toggle
                        if (!checkTpToggle(each)) {
                            player.sendMessage(ChatColor.RED + each.getName() + " has teleporting disabled");
                            return true;
                        }
                        
                        int index = checkTpRequest(player, true);
                        if (index > -1) {
                            tpRequest.set(index, player.getName() + "," + each.getName());
                            player.sendMessage(ChatColor.GREEN + "Teleport request sent to " + each.getName() + ".");
                            each.sendMessage(ChatColor.RED + "Received teleport request from " + player.getName() + ".");
                        } else {
                            tpRequest.add(player.getName() + "," + each.getName());
                            player.sendMessage(ChatColor.GREEN + "Teleport request sent to " + each.getName() + ".");
                            each.sendMessage(ChatColor.RED + "Received teleport request from " + player.getName() + ".");
                        }
                        
                        return true;
                    }
                }
                
                // player not online
                player.sendMessage(ChatColor.RED + tpName + " is not online.");
                
            // invalid args
            } else {
                return false;
            }
            
            return true;
        }
        
        return false;
    }
    
    private boolean checkPermission(String permission, Player player) {
        if (!player.hasPermission(permission)) {
            player.sendMessage(ChatColor.RED + "You do not have permission.");
            return false;
        } else {
            return true;
        }
    }
    
    private boolean checkTpToggle(Player player) {
        for (String every : tpToggle) {
            String tpName = every.substring(0, every.indexOf(","));
            String tpAble = every.substring(every.indexOf(",") + 1);
            if (player.getName().equalsIgnoreCase(tpName)) {
                if (tpAble.equals("false")) {
                    return false;
                }
            }
        }
        
        return true;
    }
    
    private int checkTpRequest(Player player, boolean toPlayer) {
        String tpName;
        for (String each: tpRequest) {
            if (toPlayer) {
                tpName = each.substring(0, each.indexOf(','));
            } else {
                tpName = each.substring(each.indexOf(",") + 1);
            }
            if (player.getName().equalsIgnoreCase(tpName))
                return tpRequest.indexOf(each);
        }
        
        return -1;
    }
    
}
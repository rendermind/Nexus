package com.vioviocity.nexus.commands;

import com.vioviocity.nexus.Nexus;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ItemCommand implements CommandExecutor{
    
    private Nexus plugin;
    public ItemCommand(Nexus plugin) {
        this.plugin = plugin;
    }

    public boolean onCommand(CommandSender sender, Command command, String alias, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("[Nexus] Command must be issued within game.");
            return true;
        }
        
        // initialize core variables
        Player player = (Player) sender;
        Player onlinePlayers[] = plugin.getServer().getOnlinePlayers();
        
        // command handler
        String cmd = command.getName().toLowerCase();
        if (cmd.equals("item")) {
            // check permission
            if (!Nexus.checkPermission("nexus.item", player, true))
                return true;
            // invalid args
            if (args.length < 1 || args.length > 3)
                return false;
            
            // item (id)
            if (args.length == 1) {
                
                // initialize variables
                ItemStack item = new ItemStack(0, 1);
                
                // check item names
                if (Nexus.itemList.containsKey(args[0].toLowerCase()))
                    args[0] = Nexus.itemList.get(args[0].toLowerCase()).toString();
                
                // check metadata
                try {
                    if (args[0].contains(":")) {
                        item.setTypeId(Integer.parseInt(args[0].substring(0, args[0].indexOf(":"))));
                        item.setDurability(Short.parseShort(args[0].substring(args[0].indexOf(":") + 1)));
                    } else {
                        item.setTypeId(Integer.parseInt(args[0]));
                    }
                } catch (Exception e) {
                    player.sendMessage("Item name or ID does not exist.");
                    return true;
                }
                
                // add item
                player.getInventory().addItem(item);
                return true;
            }
            
            // item (id) (qty) -or- item (player) (id)
            if (args.length == 2) {
                
                // initial variables
                ItemStack item = new ItemStack(0, 1);
                String playerName = args[0].toLowerCase();
                
                // item (player) (id)
                for (Player each : onlinePlayers) {
                    if (each.getName().toLowerCase().contains(playerName)) {
                        
                        // check item names
                        if (Nexus.itemList.containsKey(args[1].toLowerCase()))
                        args[1] = Nexus.itemList.get(args[1].toLowerCase()).toString();
                        
                        // check metadata
                        try {
                            if (args[1].contains(":")) {
                                item.setTypeId(Integer.parseInt(args[1].substring(0, args[1].indexOf(":"))));
                                item.setDurability(Short.parseShort(args[1].substring(args[1].indexOf(":") + 1)));
                            } else {
                                item.setTypeId(Integer.parseInt(args[1]));
                            }
                        } catch (Exception e) {
                            player.sendMessage("Item name or ID does not exist.");
                            return true;
                        }
                        
                        // add item
                        each.getInventory().addItem(item);
                        each.sendMessage(ChatColor.GREEN + player.getName() + " gave you item ID " + item.getTypeId() + " (x64).");
                        player.sendMessage(ChatColor.GREEN + "Gave " + each.getName() + " item ID " + item.getTypeId() + " (x64).");
                        return true;
                    }
                }
                
                // check item names
                if (Nexus.itemList.containsKey(args[0].toLowerCase()))
                    args[0] = Nexus.itemList.get(args[0].toLowerCase()).toString();
                
                // check metadata
                try {
                    if (args[0].contains(":")) {
                        item.setTypeId(Integer.parseInt(args[0].substring(0, args[0].indexOf(":"))));
                        item.setDurability(Short.parseShort(args[0].substring(args[0].indexOf(":") + 1)));
                    } else {
                        item.setTypeId(Integer.parseInt(args[0]));
                    }
                } catch (Exception e) {
                    player.sendMessage("Item name or ID does not exist.");
                    return true;
                }
                
                // item (id) (qty)
                item.setAmount(Integer.parseInt(args[1]));
                player.getInventory().addItem(item);
                return true;
            }
            
            // item (player) (id) (qty)
            
            // initialize variables
            String playerName = args[0].toLowerCase();
            ItemStack item = new ItemStack(0, 1);
            
            // item (player) (id) (qty)
            for (Player each : onlinePlayers) {
                if (each.getName().toLowerCase().contains(playerName)) {
                    
                    // check item names
                    if (Nexus.itemList.containsKey(args[1].toLowerCase()))
                    args[1] = Nexus.itemList.get(args[1].toLowerCase()).toString();
                    
                    // check metadata
                    try {
                        if (args[1].contains(":")) {
                            item.setTypeId(Integer.parseInt(args[1].substring(0, args[1].indexOf(":"))));
                            item.setDurability(Short.parseShort(args[1].substring(args[1].indexOf(":") + 1)));
                        } else {
                            item.setTypeId(Integer.parseInt(args[1]));
                        }
                    } catch (Exception e) {
                        player.sendMessage("Item name or ID does not exist.");
                        return true;
                    }
                    
                    // add item
                    item.setAmount(Integer.parseInt(args[2]));
                    each.getInventory().addItem(item);
                    each.sendMessage(ChatColor.GREEN + player.getName() + " gave you item ID " + item.getTypeId() +
                            " (x" + item.getAmount() + ").");
                    player.sendMessage(ChatColor.GREEN + "Gave " + each.getName() + " item ID " + item.getTypeId() +
                            " (x" + item.getAmount() + ").");
                    return true;
                }
            }
            
            // player not found
            player.sendMessage(ChatColor.RED + "Player is not online.");
            return true;
        }
        
        // end of command
        return false;
    }
}
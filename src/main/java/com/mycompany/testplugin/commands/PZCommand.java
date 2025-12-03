package com.mycompany.testplugin.commands;

import com.mycompany.testplugin.PaintZone;
import com.mycompany.testplugin.arena.ArenaManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class PZCommand implements CommandExecutor {

    private final PaintZone plugin;

    public PZCommand(PaintZone plugin) {
        this.plugin = plugin;
    }

    private String c(String s) { return ChatColor.translateAlternateColorCodes('&', s); }

    private boolean isAdmin(CommandSender sender) {
        return (sender.isOp() || sender.hasPermission("pz.admin"));
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        // /pz
        if (args.length == 0) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(c("&cOnly players can use /pz"));
                return true;
            }
            Player p = (Player) sender;

            Location lobby = plugin.getLobbyLocation();
            if (lobby == null) {
                p.sendMessage(c("&cLobby is not set. Ask an admin to set with /pz admin set lobby"));
                return true;
            }

            // Teleport first (so we don't clear player's inventory while they are elsewhere).
            p.teleport(lobby);

            // Clear inventory and set selectors in slots 0 and 1 (per your request).
            p.getInventory().clear();

            // Ensure ItemManager has latest items (in case config changed)
            plugin.getItemManager().loadItems();

            ItemStack arenaSelector = plugin.getItemManager().getArenaSelector();
            ItemStack kitSelector = plugin.getItemManager().getKitSelector();

            // Place selectors in slots 0 and 1
            p.getInventory().setItem(0, arenaSelector);
            p.getInventory().setItem(1, kitSelector);

            p.sendMessage(c("&aTeleported to PaintZone lobby."));
            return true;
        }

        // /pz help
        if (args[0].equalsIgnoreCase("help")) {
            sendHelp(sender);
            return true;
        }

        // admin group
        if (args[0].equalsIgnoreCase("admin")) {
            if (!isAdmin(sender)) {
                sender.sendMessage(c("&cYou do not have permission to run admin commands."));
                return true;
            }

            if (args.length == 1) {
                sender.sendMessage(c("&cUsage: /pz admin <arena|setjoinitem|setkitselectitem|set> ..."));
                return true;
            }

            String sub = args[1].toLowerCase();

            // /pz admin set lobby
            if (sub.equals("set") && args.length >= 3 && args[2].equalsIgnoreCase("lobby")) {
                if (!(sender instanceof Player)) {
                    sender.sendMessage(c("&cOnly a player can set the lobby."));
                    return true;
                }
                Player p = (Player) sender;
                Location loc = p.getLocation();
                FileConfiguration cfg = plugin.getConfig();
                cfg.set("lobby.world", loc.getWorld().getName());
                cfg.set("lobby.x", loc.getX());
                cfg.set("lobby.y", loc.getY());
                cfg.set("lobby.z", loc.getZ());
                cfg.set("lobby.yaw", loc.getYaw());
                cfg.set("lobby.pitch", loc.getPitch());
                plugin.saveConfig();
                p.sendMessage(c("&aLobby set to your current location."));
                return true;
            }

            // /pz admin setjoinitem (sets arena selector)
            if (sub.equalsIgnoreCase("setjoinitem")) {
                if (!(sender instanceof Player)) {
                    sender.sendMessage(c("&cOnly players can set the join item."));
                    return true;
                }
                Player p = (Player) sender;
                ItemStack hand = p.getInventory().getItemInMainHand();
                if (hand == null || hand.getType() == Material.AIR) {
                    p.sendMessage(c("&cHold an item in your hand to set as the join item."));
                    return true;
                }
                FileConfiguration cfg = plugin.getConfig();
                cfg.set("items.join.material", hand.getType().name());
                cfg.set("items.arenaSelector.material", hand.getType().name());
                if (hand.hasItemMeta() && hand.getItemMeta().hasDisplayName()) {
                    cfg.set("items.join.name", hand.getItemMeta().getDisplayName());
                    cfg.set("items.arenaSelector.name", hand.getItemMeta().getDisplayName());
                } else {
                    cfg.set("items.join.name", "");
                    cfg.set("items.arenaSelector.name", "");
                }
                plugin.saveConfig();
                plugin.getItemManager().loadItems();
                p.sendMessage(c("&aJoin (arena selector) item saved to config.yml"));
                return true;
            }

            // /pz admin setkitselectitem (sets kit selector)
            if (sub.equalsIgnoreCase("setkitselectitem")) {
                if (!(sender instanceof Player)) {
                    sender.sendMessage(c("&cOnly players can set the kit selector item."));
                    return true;
                }
                Player p = (Player) sender;
                ItemStack hand = p.getInventory().getItemInMainHand();
                if (hand == null || hand.getType() == Material.AIR) {
                    p.sendMessage(c("&cHold an item in your hand to set as the kit selector item."));
                    return true;
                }
                FileConfiguration cfg = plugin.getConfig();
                cfg.set("items.kit.material", hand.getType().name());
                cfg.set("items.kitSelector.material", hand.getType().name());
                if (hand.hasItemMeta() && hand.getItemMeta().hasDisplayName()) {
                    cfg.set("items.kit.name", hand.getItemMeta().getDisplayName());
                    cfg.set("items.kitSelector.name", hand.getItemMeta().getDisplayName());
                } else {
                    cfg.set("items.kit.name", "");
                    cfg.set("items.kitSelector.name", "");
                }
                plugin.saveConfig();
                plugin.getItemManager().loadItems();
                p.sendMessage(c("&aKit selector item saved to config.yml"));
                return true;
            }

            // /pz admin arena ...
            if (sub.equalsIgnoreCase("arena")) {
                if (args.length < 4) {
                    sender.sendMessage(c("&cUsage: /pz admin arena <name> create|delete|set <red|blue> <spawn|flag>|set spectate"));
                    return true;
                }

                String arenaName = args[2];
                String action = args[3].toLowerCase();
                ArenaManager am = plugin.getArenaManager();

                // create
                if (action.equals("create")) {
                    am.createArena(arenaName);
                    sender.sendMessage(c("&aArena '&f" + arenaName + "&a' created."));
                    return true;
                }

                // delete
                if (action.equals("delete")) {
                    am.deleteArena(arenaName);
                    sender.sendMessage(c("&aArena '&f" + arenaName + "&a' deleted."));
                    return true;
                }

                // set ...
                if (action.equals("set")) {
                    if (args.length < 5) {
                        sender.sendMessage(c("&cUsage: /pz admin arena <name> set <red|blue|spectate> <spawn|flag>"));
                        return true;
                    }

                    if (!(sender instanceof Player)) {
                        sender.sendMessage(c("&cOnly players can set locations or flags."));
                        return true;
                    }

                    Player p = (Player) sender;
                    String target = args[4].toLowerCase();

                    // set spectate: /pz admin arena <name> set spectate
                    if (target.equals("spectate")) {
                        plugin.getArenaManager().setSpectate(arenaName, p.getLocation());
                        p.sendMessage(c("&aSpectate location saved for arena &f" + arenaName));
                        return true;
                    }

                    // for spawn/flag we need a team and which
                    if (args.length < 6) {
                        sender.sendMessage(c("&cUsage: /pz admin arena <name> set <red|blue> <spawn|flag>"));
                        return true;
                    }

                    String team = target; // args[4] was the team
                    String which = args[5].toLowerCase(); // spawn or flag

                    com.mycompany.testplugin.arena.Arena arena = am.getArena(arenaName);
                    if (arena == null) {
                        p.sendMessage(c("&cArena '" + arenaName + "' does not exist. Create it with /pz admin arena " + arenaName + " create"));
                        return true;
                    }

                    if (which.equals("spawn")) {
                        if (team.equals("red")) {
                            am.setRedSpawn(arenaName, p.getLocation());
                            p.sendMessage(c("&aRed spawn set for arena &f" + arenaName));
                        } else if (team.equals("blue")) {
                            am.setBlueSpawn(arenaName, p.getLocation());
                            p.sendMessage(c("&aBlue spawn set for arena &f" + arenaName));
                        } else {
                            p.sendMessage(c("&cTeam must be 'red' or 'blue'"));
                        }
                        return true;
                    } else if (which.equals("flag")) {
                        // set flag uses the block the player is looking at
                        org.bukkit.block.Block targetBlock = p.getTargetBlockExact(6);
                        if (targetBlock == null) {
                            p.sendMessage(c("&cPlease look at the wool block within 6 blocks to set the flag."));
                            return true;
                        }
                        Location blockLoc = targetBlock.getLocation();
                        // ensure player is looking at wool
                        Material mat = targetBlock.getType();
                        if (!(mat == Material.BLUE_WOOL || mat == Material.RED_WOOL || mat == Material.WHITE_WOOL || mat.toString().endsWith("_WOOL"))) {
                            p.sendMessage(c("&cPlease look at a wool block to set as a flag."));
                            return true;
                        }

                        if (team.equals("red")) {
                            am.setRedFlag(arenaName, blockLoc);
                            p.sendMessage(c("&aRed flag set for arena &f" + arenaName + " &aat " + shortLoc(blockLoc)));
                        } else if (team.equals("blue")) {
                            am.setBlueFlag(arenaName, blockLoc);
                            p.sendMessage(c("&aBlue flag set for arena &f" + arenaName + " &aat " + shortLoc(blockLoc)));
                        } else {
                            p.sendMessage(c("&cTeam must be 'red' or 'blue'"));
                        }
                        return true;
                    } else {
                        p.sendMessage(c("&cUnknown option: " + which + " (use spawn or flag)"));
                        return true;
                    }
                }

                sender.sendMessage(c("&cUnknown arena action: " + action));
                return true;
            }

            sender.sendMessage(c("&cUnknown admin command. Use &f/pz help &cfor usage."));
            return true;
        }

        // fallback
        sender.sendMessage(c("&cUnknown command. Use &f/pz help"));
        return true;
    }

    private void sendHelp(CommandSender sender) {
        sender.sendMessage("");
        sender.sendMessage(c("&b&lPaintZone Commands"));
        sender.sendMessage(c("&f/pz &7- Teleport to lobby and receive selector items"));
        sender.sendMessage(c("&f/pz help &7- Show this help"));
        sender.sendMessage("");
        if (isAdmin(sender)) {
            sender.sendMessage(c("&c&lAdmin Commands"));
            sender.sendMessage(c("&f/pz admin arena <name> create"));
            sender.sendMessage(c("&f/pz admin arena <name> delete"));
            sender.sendMessage(c("&f/pz admin arena <name> set <red|blue> spawn"));
            sender.sendMessage(c("&f/pz admin arena <name> set <red|blue> flag"));
            sender.sendMessage(c("&f/pz admin arena <name> set spectate"));
            sender.sendMessage(c("&f/pz admin set lobby"));
            sender.sendMessage(c("&f/pz admin setjoinitem"));
            sender.sendMessage(c("&f/pz admin setkitselectitem"));
        }
        sender.sendMessage("");
    }

    private String shortLoc(Location l) {
        return String.format("%s %.1f %.1f %.1f", l.getWorld().getName(), l.getX(), l.getY(), l.getZ());
    }
}
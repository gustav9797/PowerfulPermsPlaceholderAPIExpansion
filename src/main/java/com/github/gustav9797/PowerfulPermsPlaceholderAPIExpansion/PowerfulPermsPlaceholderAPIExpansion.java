package com.github.gustav9797.PowerfulPermsPlaceholderAPIExpansion;

import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.github.gustav9797.PowerfulPermsAPI.CachedGroup;
import com.github.gustav9797.PowerfulPermsAPI.Group;
import com.github.gustav9797.PowerfulPermsAPI.Permission;
import com.github.gustav9797.PowerfulPermsAPI.PermissionPlayer;
import com.github.gustav9797.PowerfulPermsAPI.PowerfulPermsPlugin;

import me.clip.placeholderapi.PlaceholderAPI;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;

public class PowerfulPermsPlaceholderAPIExpansion extends PlaceholderExpansion {

    private PowerfulPermsPlugin plugin;

    @Override
    public boolean canRegister() {
        return Bukkit.getPluginManager().getPlugin(getPlugin()) != null;
    }

    @Override
    public boolean register() {
        if (!canRegister()) {
            return false;
        }

        plugin = (PowerfulPermsPlugin) Bukkit.getPluginManager().getPlugin(getPlugin());

        if (plugin == null) {
            return false;
        }

        return PlaceholderAPI.registerPlaceholderHook(getIdentifier(), this);
    }

    @Override
    public String getAuthor() {
        return "gustav9797";
    }

    @Override
    public String getIdentifier() {
        return "powerfulperms";
    }

    @Override
    public String getPlugin() {
        return "PowerfulPerms";
    }

    @Override
    public String getVersion() {
        return "1.3.0";
    }

    @Override
    public String onPlaceholderRequest(Player player, String identifier) {
        if (identifier.startsWith("player_")) {
            identifier = identifier.replaceFirst("player_", "");
            String[] params = identifier.split("_");
            if (params.length == 0)
                return null;
            String playerName = params[0];
            identifier = identifier.replaceFirst(playerName + "_", "");
            return parsePlayerPlaceholder(plugin.getPermissionManager().getPermissionPlayer(playerName), identifier);

        } else if (identifier.startsWith("group_")) {
            identifier = identifier.replaceFirst("group_", "");
            String[] params = identifier.split("_");
            if (params.length == 0)
                return null;
            String groupName = params[0];
            identifier = identifier.replaceFirst(groupName + "_", "");
            return parseGroupPlaceholder(plugin.getPermissionManager().getGroup(groupName), identifier);
        } else {
            if (player == null)
                return "";
            return parsePlayerPlaceholder(plugin.getPermissionManager().getPermissionPlayer(player.getUniqueId()), identifier);
        }

    }

    private String parsePlayerPlaceholder(PermissionPlayer permissionPlayer, String identifier) {
        if (permissionPlayer == null)
            return "INVALID_PLAYER";

        if (identifier.equals("prefix")) {
            String out = permissionPlayer.getPrefix();
            if (out != null)
                return out;
            return "";
        } else if (identifier.equals("suffix")) {
            String out = permissionPlayer.getSuffix();
            if (out != null)
                return out;
            return "";
        } else if (identifier.equals("ownprefix")) {
            String out = permissionPlayer.getOwnPrefix();
            if (out != null)
                return out;
            return "";
        } else if (identifier.equals("ownsuffix")) {
            String out = permissionPlayer.getOwnSuffix();
            if (out != null)
                return out;
            return "";
        } else if (identifier.equals("primarygroup")) {
            Group group = permissionPlayer.getPrimaryGroup();
            if (group != null)
                return group.getName();
            return "";
        } else if (identifier.startsWith("prefix_")) {
            String ladder = identifier.replace("prefix_", "");
            String out = permissionPlayer.getPrefix(ladder);
            if (out != null)
                return out;
            return "";
        } else if (identifier.startsWith("firstprefix_")) {
            String laddersString = identifier.replace("firstprefix_", "");
            String[] ladders = laddersString.split("_");
            for (String ladder : ladders) {
                String out = permissionPlayer.getPrefix(ladder);
                if (out != null && !out.isEmpty())
                    return out;

            }
            return "";
        } else if (identifier.startsWith("suffix_")) {
            String ladder = identifier.replace("suffix_", "");
            String out = permissionPlayer.getSuffix(ladder);
            if (out != null)
                return out;
            return "";
        } else if (identifier.startsWith("firstsuffix_")) {
            String laddersString = identifier.replace("firstsuffix_", "");
            String[] ladders = laddersString.split("_");
            for (String ladder : ladders) {
                String out = permissionPlayer.getSuffix(ladder);
                if (out != null && !out.isEmpty())
                    return out;

            }
            return "";
        } else if (identifier.startsWith("groupname_")) {
            String ladder = identifier.replace("groupname_", "");
            Group group = permissionPlayer.getGroup(ladder);
            if (group != null)
                return group.getName();
            return "";
        } else if (identifier.startsWith("firstgroup_")) {
            String laddersString = identifier.replace("firstgroup_", "");
            String[] ladders = laddersString.split("_");
            for (String ladder : ladders) {
                Group group = permissionPlayer.getGroup(ladder);
                if (group != null)
                    return group.getName();
            }
            return "";
        } else if (identifier.startsWith("permexpirytime_")) {
            String permission = identifier.replace("permexpirytime_", "");
            List<Permission> perms = permissionPlayer.getAllPermissions();
            for (Permission perm : perms) {
                if (perm.getPermissionString().equalsIgnoreCase(permission)) {
                    return (perm.getExpirationDate() == null ? "Never" : getTimeLeftString(perm.getExpirationDate()));
                }
            }
            return "";
        } else if (identifier.startsWith("permexpirydate_")) {
            String permission = identifier.replace("permexpirydate_", "");
            List<Permission> perms = permissionPlayer.getAllPermissions();
            for (Permission perm : perms) {

                if (perm.getPermissionString().equalsIgnoreCase(permission)) {
                    java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    return (perm.getExpirationDate() == null ? "Never" : dateFormat.format(perm.getExpirationDate()));
                }
            }
            return "";
        } else if (identifier.startsWith("ownpermexpirytime_")) {
            String permission = identifier.replace("ownpermexpirytime_", "");
            List<Permission> perms = permissionPlayer.getPermissions();
            for (Permission perm : perms) {
                if (perm.getPermissionString().equalsIgnoreCase(permission)) {
                    return (perm.getExpirationDate() == null ? "Never" : getTimeLeftString(perm.getExpirationDate()));
                }
            }
            return "";
        } else if (identifier.startsWith("ownpermexpirydate_")) {
            String permission = identifier.replace("ownpermexpirydate_", "");
            List<Permission> perms = permissionPlayer.getPermissions();
            for (Permission perm : perms) {
                if (perm.getPermissionString().equalsIgnoreCase(permission)) {
                    java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    return (perm.getExpirationDate() == null ? "Never" : dateFormat.format(perm.getExpirationDate()));
                }
            }
            return "";
        } else if (identifier.startsWith("groupexpirytime_")) {
            String groupName = identifier.replace("groupexpirytime_", "");
            Group group = plugin.getPermissionManager().getGroup(groupName);
            if (group != null) {
                LinkedHashMap<String, List<CachedGroup>> cachedGroups = permissionPlayer.getCachedGroups();
                Iterator<List<CachedGroup>> it = cachedGroups.values().iterator();
                while (it.hasNext()) {
                    List<CachedGroup> list = it.next();
                    for (CachedGroup cachedGroup : list) {
                        if (cachedGroup.getGroupId() == group.getId())
                            return (cachedGroup.getExpirationDate() == null ? "Never" : getTimeLeftString(cachedGroup.getExpirationDate()));
                    }
                }
            }
            return "";
        } else if (identifier.startsWith("groupexpirydate_")) {
            String groupName = identifier.replace("groupexpirydate_", "");
            Group group = plugin.getPermissionManager().getGroup(groupName);
            if (group != null) {
                LinkedHashMap<String, List<CachedGroup>> cachedGroups = permissionPlayer.getCachedGroups();
                Iterator<List<CachedGroup>> it = cachedGroups.values().iterator();
                while (it.hasNext()) {
                    List<CachedGroup> list = it.next();
                    for (CachedGroup cachedGroup : list) {
                        if (cachedGroup.getGroupId() == group.getId()) {
                            java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            return (cachedGroup.getExpirationDate() == null ? "Never" : dateFormat.format(cachedGroup.getExpirationDate()));
                        }
                    }
                }
            }
            return "";
        }
        return null;
    }

    private String parseGroupPlaceholder(Group group, String identifier) {
        if (group == null)
            return "INVALID_GROUP";

        if (identifier.equals("id")) {
            return group.getId() + "";
        } else if (identifier.equals("ladder")) {
            String out = group.getLadder();
            if (out != null)
                return out;
            return "";
        } else if (identifier.equals("rank")) {
            return group.getRank() + "";
        } else if (identifier.startsWith("permexpirytime_")) {
            String permission = identifier.replace("permexpirytime_", "");
            List<Permission> perms = group.getPermissions();
            for (Permission perm : perms) {
                if (perm.getPermissionString().equalsIgnoreCase(permission)) {
                    return (perm.getExpirationDate() == null ? "Never" : getTimeLeftString(perm.getExpirationDate()));
                }
            }
            return "";
        } else if (identifier.startsWith("permexpirydate_")) {
            String permission = identifier.replace("permexpirydate_", "");
            List<Permission> perms = group.getPermissions();
            for (Permission perm : perms) {
                if (perm.getPermissionString().equalsIgnoreCase(permission)) {
                    java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    return (perm.getExpirationDate() == null ? "Never" : dateFormat.format(perm.getExpirationDate()));
                }
            }
            return "";
        }
        return null;
    }

    private static String getTimeLeftString(Date date) {
        if (date == null)
            return null;

        Date fromDate = new Date();
        Date toDate = date;

        long diff = toDate.getTime() - fromDate.getTime();

        long years = (long) Math.floor(diff / (1000 * 60 * 60 * 24 * 365));
        diff -= years * 1000 * 60 * 60 * 24 * 365;

        long days = (long) Math.floor(diff / (1000 * 60 * 60 * 24));
        diff -= days * 1000 * 60 * 60 * 24;

        long hours = (long) Math.floor(diff / (1000 * 60 * 60));
        diff -= hours * 1000 * 60 * 60;

        long minutes = (long) Math.floor(diff / (1000 * 60));
        diff -= minutes * 1000 * 60;

        long seconds = (long) Math.floor(diff / 1000);

        return (years > 0 ? years + "y " : "") + (days > 0 ? days + "d " : "") + (hours > 0 ? hours + "h " : "") + (minutes > 0 ? minutes + "min " : "") + seconds + "s";
    }

}

package com.github.cheesesoftware.PowerfulPermsPlaceholderAPIExpansion;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.github.cheesesoftware.PowerfulPermsAPI.Group;
import com.github.cheesesoftware.PowerfulPermsAPI.Permission;
import com.github.cheesesoftware.PowerfulPermsAPI.PermissionPlayer;
import com.github.cheesesoftware.PowerfulPermsAPI.PowerfulPermsPlugin;

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
            identifier = identifier.substring("player_".length());
            String[] params = identifier.split("_");
            if (params.length == 0)
                return null;
            String playerName = params[0];
            if (identifier.length() < playerName.length() + 2)
                return null;
            identifier = identifier.substring(playerName.length());
            return parsePlayerPlaceholder(plugin.getPermissionManager().getPermissionPlayer(playerName), identifier);

        } else if (identifier.startsWith("group_")) {
            identifier = identifier.substring("group_".length());
            String[] params = identifier.split("_");
            if (params.length == 0)
                return null;
            String groupName = params[0];
            if (identifier.length() < groupName.length() + 2)
                return null;
            identifier = identifier.substring(groupName.length() + 2);
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
        } else if (identifier.startsWith("permexpirydate_")) {
            String permission = identifier.replace("permexpirydate_", "");
            List<Permission> perms = permissionPlayer.getAllPermissions();
            for (Permission perm : perms) {
                if (perm.getPermissionString().equalsIgnoreCase(permission)) {
                    java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    return (perm.getExpirationDate() == null ? "Never" : dateFormat.format(perm.getExpirationDate()));
                }
            }
        } else if (identifier.startsWith("ownpermexpirytime_")) {
            String permission = identifier.replace("ownpermexpirytime_", "");
            List<Permission> perms = permissionPlayer.getPermissions();
            for (Permission perm : perms) {
                if (perm.getPermissionString().equalsIgnoreCase(permission)) {
                    return (perm.getExpirationDate() == null ? "Never" : getTimeLeftString(perm.getExpirationDate()));
                }
            }
        } else if (identifier.startsWith("ownpermexpirydate_")) {
            String permission = identifier.replace("ownpermexpirydate_", "");
            List<Permission> perms = permissionPlayer.getPermissions();
            for (Permission perm : perms) {
                if (perm.getPermissionString().equalsIgnoreCase(permission)) {
                    java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    return (perm.getExpirationDate() == null ? "Never" : dateFormat.format(perm.getExpirationDate()));
                }
            }
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
        } else if (identifier.startsWith("permexpirydate_")) {
            String permission = identifier.replace("permexpirydate_", "");
            List<Permission> perms = group.getPermissions();
            for (Permission perm : perms) {
                if (perm.getPermissionString().equalsIgnoreCase(permission)) {
                    java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    return (perm.getExpirationDate() == null ? "Never" : dateFormat.format(perm.getExpirationDate()));
                }
            }
        }
        return null;
    }

    private static String getTimeLeftString(Date date) {
        if (date == null)
            return null;
        return (TimeUnit.MILLISECONDS.toDays(date.getTime()) > 0 ? TimeUnit.MILLISECONDS.toDays(date.getTime()) + "d " : "") + (date.getHours() > 0 ? date.getHours() + "h " : "")
                + (date.getMinutes() > 0 ? date.getMinutes() + "min " : "") + date.getSeconds() + "s ";
    }

}

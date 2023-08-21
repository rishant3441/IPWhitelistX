package com.github.rishant3441.ipwhitelistx.listeners;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import com.github.rishant3441.ipwhitelistx.IPWhitelistX;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class JoinLeaveListener implements Listener {

    private static <T, E> T getKeyByValue(HashMap<T,E> map, E value)
    {
        for (Map.Entry<T, E> entry : map.entrySet()) {
            if (Objects.equals(value, entry.getValue())) {
                return entry.getKey();
            }
        }
        return null;
    }
    @EventHandler
    public void onPreLogin(PlayerLoginEvent e)
    {
        String ip = e.getAddress().getHostAddress();
        String username = e.getPlayer().getName();

        if (IPWhitelistX.instance.ipDatabase.containsKey(username))
        {
            if (IPWhitelistX.instance.ipDatabase.get(username) != null && IPWhitelistX.instance.ipDatabase.get(username).equals(""))
                IPWhitelistX.instance.ipDatabase.put(username, ip);
            else if (!IPWhitelistX.instance.ipDatabase.get(username).equals(ip))
            {
                if (IPWhitelistX.instance.enabled)
                {
                    Bukkit.getConsoleSender().sendMessage("[IPWhitelistX] Attempting to kick player...");
                    e.disallow(PlayerLoginEvent.Result.KICK_WHITELIST, "You are not allowed to join this server");
                    return;
                }
            }

            e.allow();
        } else {
            if (IPWhitelistX.instance.enabled)
            {
                Bukkit.getConsoleSender().sendMessage("[IPWhitelistX] Attempting to kick player...");
                e.disallow(PlayerLoginEvent.Result.KICK_WHITELIST, "You are not allowed to join this server");
            }
        }
    }
}

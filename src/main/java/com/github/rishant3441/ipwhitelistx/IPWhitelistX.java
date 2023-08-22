package com.github.rishant3441.ipwhitelistx;

import com.github.rishant3441.ipwhitelistx.commands.IPWhitelistXCommand;
import com.github.rishant3441.ipwhitelistx.listeners.JoinLeaveListener;
import com.google.gson.reflect.TypeToken;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import com.google.gson.*;
import com.google.gson.TypeAdapter;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.HashMap;

public class IPWhitelistX extends JavaPlugin {
    public static IPWhitelistX instance;

    // Username, IP
    public HashMap<String, String> ipDatabase = new HashMap<>();

    private File dataFile = new File(getDataFolder(), "players.json");
    private File enabledFile = new File(getDataFolder(), "enabled.json");

    public boolean enabled = false;
    private GsonBuilder builder = new GsonBuilder();

    TypeToken<HashMap<String, String>> mapType = new TypeToken<HashMap<String, String>>(){};

    @Override
    public void onEnable()
    {
        instance = this;

        if (!getDataFolder().exists())
        {
            Bukkit.getConsoleSender().sendMessage("[IPWhitelistX] - Data Folder does not exist yet! Creating new data folder...");
            getDataFolder().mkdirs();
        }

        if (!dataFile.exists())
        {
            try {
                dataFile.createNewFile();
                enabledFile.createNewFile();
                saveIPs();
                Bukkit.getConsoleSender().sendMessage("[IPWhitelistX] - Data file does not exist! Creating new data file...");
            }
            catch (Exception e){
                Bukkit.getConsoleSender().sendMessage("[IPWhitelistX] - Could not create a new file.");
                e.printStackTrace();
            }
        }

        Bukkit.getPluginCommand("ipwhitelistx").setExecutor(new IPWhitelistXCommand());
        Bukkit.getPluginManager().registerEvents(new JoinLeaveListener(), this);

        try {
            readIPs();
            Bukkit.getConsoleSender().sendMessage("[IPWhitelistX] Successfully loaded " + ipDatabase.size() + " IPs!");
        } catch (IOException e) {
            Bukkit.getConsoleSender().sendMessage("[IPWhitelistX] Unable to open datafile!");
            e.printStackTrace();
        }
    }

    @Override
    public void onDisable()
    {
        try {
            saveIPs();
            Bukkit.getConsoleSender().sendMessage("[IPWhitelistX] Successfully saved " + ipDatabase.size() +" IPs!");
        } catch (IOException e) {
            Bukkit.getConsoleSender().sendMessage("[IPWhitelistX] Unable to open datafile to save IPs!");
            e.printStackTrace();
        }
    }

    private void saveIPs() throws IOException
    {
        builder.setPrettyPrinting();

        Gson gson = builder.create();

        String json = gson.toJson(ipDatabase);
        String enabledJson = gson.toJson(enabled);

        Files.writeString(dataFile.toPath(), json, StandardCharsets.UTF_8);
        Files.writeString(enabledFile.toPath(), enabledJson, StandardCharsets.UTF_8);
    }

    private void readIPs() throws IOException
    {
        Gson gson = builder.create();
        String json = new String(Files.readAllBytes(dataFile.toPath()));
        String enabledJson = new String(Files.readAllBytes(enabledFile.toPath()));

        ipDatabase = gson.fromJson(json, mapType);
        enabled = gson.fromJson(enabledJson, Boolean.class);
    }
}

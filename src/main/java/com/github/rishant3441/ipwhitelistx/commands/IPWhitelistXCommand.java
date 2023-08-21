package com.github.rishant3441.ipwhitelistx.commands;

import com.github.rishant3441.ipwhitelistx.IPWhitelistX;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.Command;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class IPWhitelistXCommand implements CommandExecutor {

    private static void printHelpMessage(CommandSender commandSender)
    {
        commandSender.sendMessage("§aIP Whitelist X commands");
        commandSender.sendMessage("§7---------------------");
        commandSender.sendMessage("§a/" + "ipwhitelistx" + " enable");
        commandSender.sendMessage("§a/" + "ipwhitelistx" + " disable");
        commandSender.sendMessage("§a/" + "ipwhitelistx" + " addplayer <Username>");
        commandSender.sendMessage("§a/" + "ipwhitelistx" + " removeplayer <Username>");
        commandSender.sendMessage("§a/" + "ipwhitelistx" + " addip <IP> <Username>");
        commandSender.sendMessage("§a/" + "ipwhitelistx" + " removeip <IP>");
        commandSender.sendMessage("§a/" + "ipwhitelistx" + " lists");
        commandSender.sendMessage("§7---------------------");
    }

    private static <T, E> T getKeyByValue(HashMap<T,E> map, E value)
    {
        for (Map.Entry<T, E> entry : map.entrySet()) {
            if (Objects.equals(value, entry.getValue())) {
                return entry.getKey();
            }
        }
        return null;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String string, String[] args)
    {
        if (commandSender instanceof ConsoleCommandSender)
        {
            if (args.length < 1) {
                printHelpMessage(commandSender);
                return true;
            }

            switch (args[0].toLowerCase())
            {
                case "enable":
                    if (IPWhitelistX.instance.enabled)
                    {
                        commandSender.sendMessage("[IPWhitelistX] IP Whitelist is already enabled!");
                        break;
                    }
                    IPWhitelistX.instance.enabled = true;
                    commandSender.sendMessage("[IPWhitelistX] IP Whitelist was successfully enabled!");
                    break;
                case "disable":
                    if (!IPWhitelistX.instance.enabled)
                    {
                        commandSender.sendMessage("[IPWhitelistX] IP Whitelist is already disabled!");
                        break;
                    }
                    IPWhitelistX.instance.enabled = false;
                    commandSender.sendMessage("[IPWhitelistX] IP Whitelist was successfully disabled!");
                    break;
                case "addplayer":
                    if (args.length == 2)
                    {
                        if (!IPWhitelistX.instance.ipDatabase.containsKey(args[1]))
                        {
                            IPWhitelistX.instance.ipDatabase.put(args[1], "");
                            commandSender.sendMessage(("[IPWhitelistX] " + args[1] +" has been added to the whitelist!"));
                        } else {
                            commandSender.sendMessage(("[IPWhitelistX] " + args[1] +" has already been added to the whitelist!"));
                        }
                    } else
                        printHelpMessage(commandSender);

                    break;
                case "removeplayer":

                    if (args.length == 2)
                    {
                        if (IPWhitelistX.instance.ipDatabase.containsKey(args[1]))
                        {
                            IPWhitelistX.instance.ipDatabase.remove(args[1]);
                            commandSender.sendMessage(("[IPWhitelistX] " + args[1] +" has been removed from the whitelist!"));
                        } else {
                            commandSender.sendMessage(("[IPWhitelistX] " + args[1] +" is not on IP Whitelist!"));
                        }
                    } else
                        printHelpMessage(commandSender);

                    break;

                case "addip":
                    if (args.length == 3)
                    {
                        if (!IPWhitelistX.instance.ipDatabase.containsKey(args[2]))
                        {
                            IPWhitelistX.instance.ipDatabase.put(args[2], args[1]);
                            commandSender.sendMessage(("[IPWhitelistX] " + args[1] +" has been added to the whitelist!"));
                        } else {
                            commandSender.sendMessage(("[IPWhitelistX] " + args[1] +" has already been added to the whitelist!"));
                        }
                    } else
                        printHelpMessage(commandSender);

                    break;

                case "removeip":
                    if (args.length == 2)
                    {
                        if (IPWhitelistX.instance.ipDatabase.containsValue(args[1]))
                        {
                            IPWhitelistX.instance.ipDatabase.remove(getKeyByValue(IPWhitelistX.instance.ipDatabase, args[1]));
                            commandSender.sendMessage(("[IPWhitelistX] " + args[1] +" has been removed from the whitelist!"));
                        } else {
                            commandSender.sendMessage(("[IPWhitelistX] " + args[1] +" is not on IP Whitelist!"));
                        }
                    } else
                        printHelpMessage(commandSender);

                    break;
                default:
                    printHelpMessage(commandSender);
                    break;
            }
            return true;
        }
        else
        {
            printHelpMessage(commandSender);
            return false;
        }
    }
}

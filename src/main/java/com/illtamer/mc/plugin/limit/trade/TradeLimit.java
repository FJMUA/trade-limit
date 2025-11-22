package com.illtamer.mc.plugin.limit.trade;

import com.illtamer.mc.plugin.limit.trade.cmd.CommandHandler;
import com.illtamer.mc.plugin.limit.trade.cmd.TabHandler;
import com.illtamer.mc.plugin.limit.trade.config.ConfigFile;
import com.illtamer.mc.plugin.limit.trade.listener.VillagerListener;
import java.util.LinkedList;
import java.util.List;

import org.bukkit.command.PluginCommand;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class TradeLimit
extends JavaPlugin {
    private static TradeLimit instance;
    private final List<ItemStack> limitItems = new LinkedList<>();
    private ConfigFile configFile;

    public void onEnable() {
        instance = this;
        this.configFile = new ConfigFile("config.yml", instance);
        PluginCommand command = this.getCommand("TradeLimit");
        command.setExecutor(new CommandHandler());
        command.setTabCompleter(new TabHandler());
        this.getServer().getPluginManager().registerEvents(new VillagerListener(), instance);
        this.limitItems.addAll((List<ItemStack>) this.configFile.getConfig().getList("limit-items"));
    }

    public void onDisable() {
        HandlerList.unregisterAll(instance);
        this.saveConfig();
        instance = null;
    }

    public void saveConfig() {
        this.configFile.getConfig().set("limit-items", this.limitItems);
        this.configFile.save();
    }

    public ConfigFile getConfigFile() {
        return this.configFile;
    }

    public List<ItemStack> getLimitItems() {
        return this.limitItems;
    }

    public static TradeLimit getInstance() {
        return instance;
    }
}

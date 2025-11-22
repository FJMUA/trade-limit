package com.illtamer.mc.plugin.limit.trade.listener;

import com.illtamer.mc.plugin.limit.trade.TradeLimit;
import java.util.Arrays;
import java.util.List;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.VillagerAcquireTradeEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MerchantRecipe;
import org.bukkit.inventory.meta.ItemMeta;

public class VillagerListener
implements Listener {
    @EventHandler
    public void onVillagerAcquireTrade(VillagerAcquireTradeEvent event) {
        if (!(event.getEntity() instanceof Villager)) {
            return;
        }
        MerchantRecipe recipe = event.getRecipe();
        if (this.checkProhibitedItem(recipe.getResult())) {
            event.setCancelled(true);
            recipe.setMaxUses(0);
            TradeLimit.getInstance().getLogger().info("村民生成的高价值交易已被禁用: " + recipe.getResult());
        }
    }

    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        if (event.getRightClicked() instanceof Villager) {
            Villager villager = (Villager)event.getRightClicked();
            Player player = event.getPlayer();
            for (MerchantRecipe recipe : villager.getRecipes()) {
                if (!this.checkProhibitedItem(recipe.getResult())) continue;
                recipe.setMaxUses(0);
                player.sendMessage("§c此村民提供的部分交易被禁用");
            }
        }
    }

    private MerchantRecipe generateRandomRecipe(int villagerLevel) {
        ItemStack replace = new ItemStack(Material.APPLE, 1);
        ItemMeta meta = replace.getItemMeta();
        meta.setDisplayName("§a黄金苹果");
        meta.setLore(Arrays.asList("§e因为该村民的某项交易产物被禁用而作为代替的物品", "§e这是苹果，它很可爱ava"));
        replace.setItemMeta(meta);
        return new MerchantRecipe(replace, villagerLevel);
    }

    private boolean checkProhibitedItem(ItemStack item) {
        List<ItemStack> limitItems = TradeLimit.getInstance().getLimitItems();
        for (ItemStack limitItem : limitItems) {
            if (!limitItem.getType().equals((Object)item.getType())) continue;
            if (!limitItem.hasItemMeta() && !item.hasItemMeta()) {
                return true;
            }
            if (!limitItem.hasItemMeta() || !limitItem.getItemMeta().equals(item.getItemMeta())) continue;
            return true;
        }
        return false;
    }
}

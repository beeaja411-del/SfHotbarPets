package io.github.thebusybiscuit.hotbarpets.listeners;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.hotbarpets.HotbarPet;
import io.github.thebusybiscuit.hotbarpets.HotbarPets;
import io.github.thebusybiscuit.slimefun4.utils.SlimefunUtils;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import org.bukkit.inventory.meta.ItemMeta;

public class SoulPieListener implements Listener {

    private final HotbarPet eyamaz;

    public SoulPieListener(HotbarPets plugin) {
        eyamaz = (HotbarPet) SlimefunItem.getById("HOTBAR_PET_EYAMAZ");

        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onSoulHarvest(EntityDeathEvent e) {
        if (e.getEntity().getKiller() != null) {
            Player p = e.getEntity().getKiller();

            for (int i = 0; i < 9; ++i) {
                ItemStack item = p.getInventory().getItem(i);

                if (eyamaz != null && SlimefunUtils.isItemSimilar(item, eyamaz.getItem(), true)) {
                    ItemStack pie = new ItemStack(Material.PUMPKIN_PIE);
                    ItemMeta meta = pie.getItemMeta();
                    meta.setDisplayName(org.bukkit.ChatColor.translateAlternateColorCodes('&', "&bSoul Pie"));
                    pie.setItemMeta(meta);
                    e.getEntity().getLocation().getWorld().dropItemNaturally(e.getEntity().getLocation(), pie);
                }
            }
        }

    }

}

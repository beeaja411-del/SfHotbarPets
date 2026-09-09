package io.github.thebusybiscuit.hotbarpets;

import org.bstats.bukkit.Metrics;
import org.bukkit.NamespacedKey;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import io.github.thebusybiscuit.hotbarpets.groups.BossMobs;
import io.github.thebusybiscuit.hotbarpets.groups.FarmAnimals;
import io.github.thebusybiscuit.hotbarpets.groups.HostileMobs;
import io.github.thebusybiscuit.hotbarpets.groups.PassiveMobs;
import io.github.thebusybiscuit.hotbarpets.groups.PeacefulAnimals;
import io.github.thebusybiscuit.hotbarpets.groups.SpecialPets;
import io.github.thebusybiscuit.hotbarpets.groups.UtilityPets;
import io.github.thebusybiscuit.hotbarpets.listeners.DamageListener;
import io.github.thebusybiscuit.hotbarpets.listeners.FoodListener;
import io.github.thebusybiscuit.hotbarpets.listeners.GeneralListener;
import io.github.thebusybiscuit.hotbarpets.listeners.PhantomListener;
import io.github.thebusybiscuit.hotbarpets.listeners.ProjectileListener;
import io.github.thebusybiscuit.hotbarpets.listeners.SoulPieListener;
import io.github.thebusybiscuit.hotbarpets.listeners.TNTListener;
import io.github.thebusybiscuit.slimefun4.api.SlimefunAddon;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.libraries.dough.config.Config;
import io.github.thebusybiscuit.slimefun4.libraries.dough.updater.GitHubBuildsUpdater;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class HotbarPets extends JavaPlugin implements Listener, SlimefunAddon {

    private ItemGroup itemGroup;

    @Override
    public void onEnable() {
        Config cfg = new Config(this);

        // Setting up bStats
        new Metrics(this, 4859);

        if (cfg.getBoolean("options.auto-update") && getDescription().getVersion().startsWith("DEV - ")) {
            new GitHubBuildsUpdater(this, getFile(), "TheBusyBiscuit/HotbarPets/master").start();
        }

        ItemStack categoryItem = PetTexture.CATEGORY.getAsItem().clone();
        ItemMeta meta = categoryItem.getItemMeta();
        meta.setDisplayName(org.bukkit.ChatColor.translateAlternateColorCodes('&', "&dHotbar Pets"));
        java.util.List<String> lore = new java.util.ArrayList<>();
        lore.add(org.bukkit.ChatColor.translateAlternateColorCodes('&', "&a> Click to open"));
        meta.setLore(lore);
        categoryItem.setItemMeta(meta);
        itemGroup = new ItemGroup(new NamespacedKey(this, "pets"), categoryItem);

        // Add all the Pets via their Group class
        new FarmAnimals(this);
        new PeacefulAnimals(this);
        new PassiveMobs(this);
        new HostileMobs(this);
        new BossMobs(this);
        new UtilityPets(this);
        new SpecialPets(this);

        // Registering the Listeners
        new DamageListener(this);
        new FoodListener(this);
        new GeneralListener(this);
        new PhantomListener(this);
        new ProjectileListener(this);
        new SoulPieListener(this);
        new TNTListener(this);

        // Registering our task
        getServer().getScheduler().scheduleSyncRepeatingTask(this, new HotbarPetsRunnable(), 0L, 2000L);
    }

    public ItemGroup getItemGroup() {
        return itemGroup;
    }

    @Override
    public String getBugTrackerURL() {
        return "https://github.com/TheBusyBiscuit/HotbarPets/issues";
    }

    @Override
    public JavaPlugin getJavaPlugin() {
        return this;
    }

    public static org.bukkit.inventory.ItemStack convert(Object sfItem) {
        if (sfItem == null) return null;
        try {
            java.lang.reflect.Method m = sfItem.getClass().getMethod("serialize");
            java.util.Map<String, Object> map = (java.util.Map<String, Object>) m.invoke(sfItem);
            return org.bukkit.inventory.ItemStack.deserialize(map);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}

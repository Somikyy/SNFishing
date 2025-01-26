package org.snetwork.snfishing.fishing;

import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.snetwork.snfishing.SNFishing;

public class HologramManager {

    public static void spawnHologram(Location location, String text) {
        ArmorStand hologram = (ArmorStand) location.getWorld().spawnEntity(location, EntityType.ARMOR_STAND);
        hologram.setVisible(false);
        hologram.setGravity(false);
        hologram.setCustomNameVisible(true);
        hologram.setCustomName(text);

        // Удаляем голограмму через 2 секунды
        new org.bukkit.scheduler.BukkitRunnable() {
            @Override
            public void run() {
                hologram.remove();
            }
        }.runTaskLater(SNFishing.getInstance(), 40L); // 40 тиков = 2 секунды
    }
}
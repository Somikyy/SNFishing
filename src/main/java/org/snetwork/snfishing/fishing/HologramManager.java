package org.snetwork.snfishing.fishing;

import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.snetwork.snfishing.SNFishing;

public class HologramManager {

    public static ArmorStand spawnHologram(Location location, String text) {
        ArmorStand hologram = (ArmorStand) location.getWorld().spawnEntity(location, EntityType.ARMOR_STAND);
        hologram.setVisible(false);
        hologram.setGravity(false);
        hologram.setCustomNameVisible(true);
        hologram.setCustomName(text);
        hologram.setSmall(true);
        hologram.setMarker(true);
        return hologram;
    }
}
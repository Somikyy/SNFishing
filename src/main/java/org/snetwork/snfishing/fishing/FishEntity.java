package org.snetwork.snfishing.fishing;

import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.util.Vector;

public class FishEntity {

    private final ArmorStand fishEntity;

    public FishEntity(Location location) {
        this.fishEntity = (ArmorStand) location.getWorld().spawnEntity(location, EntityType.ARMOR_STAND);
        fishEntity.setVisible(false);
        fishEntity.setGravity(false);
        fishEntity.setCustomNameVisible(true);
        fishEntity.setCustomName("§cFish");
    }

    public void move(Vector direction) {
        fishEntity.setVelocity(direction);
    }

    public void remove() {
        fishEntity.remove();
    }

    public Location getLocation() {
        return fishEntity.getLocation();
    }
}
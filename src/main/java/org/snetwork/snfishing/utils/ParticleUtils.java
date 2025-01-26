package org.snetwork.snfishing.utils;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;

public class ParticleUtils {

    public static void spawnParticle(Player player, Particle particle, Location location, int count) {
        player.spawnParticle(particle, location, count);
    }
}
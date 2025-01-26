package org.snetwork.snfishing.fishing;

import org.bukkit.inventory.ItemStack;
import org.snetwork.snfishing.SNFishing;
import org.snetwork.snfishing.config.DropConfig;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.Map;
import java.util.Random;

public class FishingMechanics {

    private final SNFishing plugin;
    private final DropConfig dropConfig;
    private final Random random = new Random();

    public FishingMechanics(SNFishing plugin) {
        this.plugin = plugin;
        this.dropConfig = plugin.getDropConfig();
    }

    public void startFishing(Player player) {
        Location fishLocation = player.getLocation().add(0, -1, 0); // Рыба появляется под игроком
        FishEntity fishEntity = new FishEntity(fishLocation);

        new BukkitRunnable() {
            int time = 0;
            final int maxTime = 100; // Время до появления "!!!"

            @Override
            public void run() {
                if (time >= maxTime) {
                    // Показываем голограмму "!!!"
                    HologramManager.spawnHologram(player.getLocation().add(0, 2, 0), "§c!!!");
                    giveReward(player);
                    fishEntity.remove();
                    this.cancel();
                } else {
                    // Двигаем рыбу
                    fishEntity.move(new Vector(random.nextDouble() - 0.5, 0, random.nextDouble() - 0.5).multiply(0.1));
                    time++;
                }
            }
        }.runTaskTimer(plugin, 0L, 1L);
    }

    private void giveReward(Player player) {
        if (random.nextDouble() < 0.5) {
            giveRandomItem(player);
        } else {
            spawnRandomMob(player);
        }
    }

    private void giveRandomItem(Player player) {
        Map<String, ItemStack> itemDrops = dropConfig.getItemDrops();
        if (!itemDrops.isEmpty()) {
            String randomKey = (String) itemDrops.keySet().toArray()[random.nextInt(itemDrops.size())];
            ItemStack item = itemDrops.get(randomKey);
            player.getInventory().addItem(item);
        }
    }

    private void spawnRandomMob(Player player) {
        Map<String, Double> mobDrops = dropConfig.getMobDrops();
        if (!mobDrops.isEmpty()) {
            for (Map.Entry<String, Double> entry : mobDrops.entrySet()) {
                if (random.nextDouble() < entry.getValue()) {
                    player.getWorld().spawnEntity(player.getLocation(), org.bukkit.entity.EntityType.valueOf(entry.getKey()));
                    break;
                }
            }
        }
    }
}
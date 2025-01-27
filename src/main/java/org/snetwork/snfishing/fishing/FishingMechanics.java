package org.snetwork.snfishing.fishing;

import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.util.Vector;
import org.snetwork.snfishing.SNFishing;
import org.snetwork.snfishing.utils.ParticleUtils;
import org.snetwork.snfishing.utils.SoundUtils;

import java.util.Map;
import java.util.Optional;
import java.util.Random;

public class FishingMechanics {
    private final SNFishing plugin;
    private final Random random = new Random();

    public FishingMechanics(SNFishing plugin) {
        this.plugin = plugin;
    }

    public void handleFishing(PlayerFishEvent event) {
        Player player = event.getPlayer();
        FishHook hook = event.getHook();

        switch (event.getState()) {
            case BITE:
                // Отменяем ванильную анимацию прыжка поплавка
                event.setCancelled(true);

                // Запускаем таймер для голограммы
                startCatchWindow(player, hook);
                break;

            case REEL_IN:
                // Проверяем, можно ли сейчас поймать
                if (hook.hasMetadata("fishing_bite")) {
                    event.setCancelled(true);
                    hook.removeMetadata("fishing_bite", plugin);

                    // Запоминаем локацию поплавка перед удалением
                    Location hookLocation = hook.getLocation();

                    // Удаляем поплавок
                    hook.remove();

                    // Выдаем награду
                    if (random.nextBoolean()) {
                        throwRandomItem(player, hookLocation);
                    } else {
                        spawnRandomMob(player, hookLocation);
                    }
                }
                break;

            case FISHING:
                // Просто позволяем забросить удочку
                break;

            case CAUGHT_FISH:
                // Отменяем ванильное поведение
                event.setCancelled(true);
                break;
        }
    }

    private void startCatchWindow(Player player, FishHook hook) {
        // Генерируем случайное время (от 0.45 до 1.05 секунд)
        int catchWindowTicks = random.nextInt(7) + 9;

        // Спавним голограмму и помечаем поплавок
        Location hookLoc = hook.getLocation().add(0, 0.3, 0);
        ArmorStand hologram = HologramManager.spawnHologram(hookLoc, "§c!!!");
        hook.setMetadata("fishing_bite", new FixedMetadataValue(plugin, Optional.of(true)));

        // Звуковые и визуальные эффекты
        SoundUtils.playSound(player, Sound.ENTITY_FISHING_BOBBER_SPLASH, 1.0f, 1.0f);
        ParticleUtils.spawnParticle(player, Particle.WATER_WAKE, hookLoc, 20);

        // Запускаем таймер на удаление голограммы и возможности поймать
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            if (hologram != null && !hologram.isDead()) {
                hologram.remove();
            }
            if (hook != null && !hook.isDead() && hook.hasMetadata("fishing_bite")) {
                hook.removeMetadata("fishing_bite", plugin);
                hook.remove(); // Убираем поплавок если игрок не успел
                SoundUtils.playSound(player, Sound.BLOCK_NOTE_BLOCK_BASS, 1.0f, 0.5f); // Звук неудачи
            }
        }, catchWindowTicks);
    }

    private void throwRandomItem(Player player, Location hookLocation) {
        Map<String, ItemStack> items = plugin.getDropConfig().getItemDrops();
        if (!items.isEmpty()) {
            String[] keys = items.keySet().toArray(new String[0]);
            String randomKey = keys[random.nextInt(keys.length)];
            ItemStack itemStack = items.get(randomKey).clone();

            // Спавним предмет в мире
            Item item = hookLocation.getWorld().dropItem(hookLocation, itemStack);

            // Создаем вектор от поплавка к позиции игрока + 2 блока вверх
            Location targetLoc = player.getLocation().add(0, 2, 0);
            Vector direction = targetLoc.subtract(hookLocation).toVector();

            // Нормализуем и устанавливаем нужную скорость
            direction.normalize().multiply(1.25);

            // Применяем вектор к предмету
            item.setVelocity(direction);
            item.setPickupDelay(0);

            // Эффекты
            ParticleUtils.spawnParticle(player, Particle.WATER_SPLASH, hookLocation, 30);
            SoundUtils.playSound(player, Sound.ENTITY_FISHING_BOBBER_RETRIEVE, 1.0f, 1.0f);

            // Увеличиваем комбо
            plugin.getComboSystem().incrementCombo(player);
        }
    }

    private void spawnRandomMob(Player player, Location hookLocation) {
        Map<String, Double> mobDrops = plugin.getDropConfig().getMobDrops();
        if (!mobDrops.isEmpty()) {
            String[] mobTypes = mobDrops.keySet().toArray(new String[0]);
            String randomMobType = mobTypes[random.nextInt(mobTypes.length)];

            try {
                Entity mob = hookLocation.getWorld().spawnEntity(hookLocation, EntityType.valueOf(randomMobType));

                // Создаем вектор от поплавка к позиции игрока + 2 блока вверх
                Location targetLoc = player.getLocation().add(0, 2, 0);
                Vector direction = targetLoc.subtract(hookLocation).toVector();

                // Нормализуем и устанавливаем нужную скорость
                direction.normalize().multiply(1.25);

                mob.setVelocity(direction);

                // Эффекты
                ParticleUtils.spawnParticle(player, Particle.WATER_SPLASH, hookLocation, 30);
                SoundUtils.playSound(player, Sound.ENTITY_FISHING_BOBBER_SPLASH, 1.0f, 1.0f);

                // Увеличиваем комбо
                plugin.getComboSystem().incrementCombo(player);
            } catch (IllegalArgumentException e) {
                plugin.getLogger().warning("Invalid mob type: " + randomMobType);
            }
        }
    }
}
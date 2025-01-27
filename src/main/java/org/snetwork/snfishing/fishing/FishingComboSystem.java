package org.snetwork.snfishing.fishing;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.snetwork.snfishing.SNFishing;
import org.snetwork.snfishing.utils.MessageUtils;
import org.snetwork.snfishing.utils.ParticleUtils;
import org.snetwork.snfishing.utils.SoundUtils;
import org.bukkit.Particle;
import org.bukkit.Sound;

import java.util.HashMap;
import java.util.Map;

public class FishingComboSystem {
    private final SNFishing plugin;
    private final Map<Player, Integer> comboCounts = new HashMap<>();
    private final Map<Player, BukkitRunnable> comboTimers = new HashMap<>();
    private static final int COMBO_TIMEOUT_SECONDS = 10;
    private static final int MAX_COMBO = 5;

    public FishingComboSystem(SNFishing plugin) {
        this.plugin = plugin;
    }

    public void incrementCombo(Player player) {
        Integer currentCombo = comboCounts.getOrDefault(player, Integer.valueOf(0));
        comboCounts.put(player, Integer.valueOf(currentCombo.intValue() + 1));

        // Отменяем существующий таймер если есть
        if (comboTimers.containsKey(player)) {
            comboTimers.get(player).cancel();
        }

        // Показываем эффекты комбо
        showComboEffects(player, currentCombo.intValue() + 1);

        // Запускаем новый таймер
        BukkitRunnable comboTimer = new BukkitRunnable() {
            @Override
            public void run() {
                resetCombo(player);
            }
        };
        comboTimer.runTaskLater(plugin, COMBO_TIMEOUT_SECONDS * 20L);
        comboTimers.put(player, comboTimer);
    }

    private void showComboEffects(Player player, int combo) {
        String comboMessage = getComboMessage(combo);
        MessageUtils.sendSuccessMessage(player, comboMessage);

        // Эффекты частиц в зависимости от уровня комбо
        ParticleUtils.spawnParticle(player, getComboParticle(combo), player.getLocation(), 20);

        // Звуковые эффекты
        SoundUtils.playSound(player, Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 0.5f + (combo * 0.1f));
    }

    private String getComboMessage(int combo) {
        switch (combo) {
            case 1:
                return "§aНеплохо! x1";
            case 2:
                return "§eОтлично! x2";
            case 3:
                return "§6Потрясающе! x3";
            case 4:
                return "§cНевероятно! x4";
            case 5:
                return "§4§lЛЕГЕНДАРНО! x5";
            default:
                return "§aКомбо x" + combo;
        }
    }

    private Particle getComboParticle(int combo) {
        switch (combo) {
            case 1:
                return Particle.VILLAGER_HAPPY;
            case 2:
                return Particle.END_ROD;
            case 3:
                return Particle.FLAME;
            case 4:
                return Particle.DRAGON_BREATH;
            case 5:
                return Particle.TOTEM;
            default:
                return Particle.VILLAGER_HAPPY;
        }
    }

    public void resetCombo(Player player) {
        comboCounts.remove(player);
        if (comboTimers.containsKey(player)) {
            comboTimers.get(player).cancel();
            comboTimers.remove(player);
        }
        MessageUtils.sendMessage(player, "§cКомбо сброшено!", null);
    }

    public int getCurrentCombo(Player player) {
        return comboCounts.getOrDefault(player, Integer.valueOf(0)).intValue();
    }
}
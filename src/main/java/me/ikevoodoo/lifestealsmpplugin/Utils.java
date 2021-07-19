package me.ikevoodoo.lifestealsmpplugin;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class Utils {

    public static AttributeInstance getMaxHealth(Player player) {
        return player.getAttribute(Attribute.GENERIC_MAX_HEALTH);
    }

    public static void modifyHealth(Player player, int scale) {
        AttributeInstance maxHp = getMaxHealth(player);
        maxHp.setBaseValue(maxHp.getValue() + scale);
    }

    public static boolean shouldEliminate(Player player) {
        return getMaxHealth(player).getValue() == 2;
    }

    public static void eliminate(Player player, Player killer) {
        if(!Configuration.shouldEliminate()) {
            return;
        }

        if(Configuration.shouldBan()) {
            Configuration.addElimination(player, killer.getUniqueId());
            if(Configuration.shouldBroadcastBan()) {
                Bukkit.broadcast(getFromText(Configuration.getBroadcastMessage().replace("%player%", player.getName())));
            }

            Configuration.banID(player.getUniqueId(), Configuration.getBanMessage().replace("%player%", killer.getName()));
            player.kick(getFromText(Configuration.getBanMessage().replace("%player%", killer.getName())));
        } else if (Configuration.shouldSpectate()) {
            player.setGameMode(GameMode.SPECTATOR);
            player.setSpectatorTarget(killer);
            Configuration.addElimination(player, killer.getUniqueId());
        }
    }

    public static TextComponent getFromText(String text) {
        return Component.text(
                ChatColor.translateAlternateColorCodes('&',
                        text
                )
        );
    }


}
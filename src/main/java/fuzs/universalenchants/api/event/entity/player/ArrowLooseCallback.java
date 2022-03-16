package fuzs.universalenchants.api.event.entity.player;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

@FunctionalInterface
public interface ArrowLooseCallback {
    Event<ArrowLooseCallback> EVENT = EventFactory.createArrayBacked(ArrowLooseCallback.class, listeners -> (Player player, ItemStack bow, Level world, int charge, boolean hasAmmo) -> {
        for (ArrowLooseCallback event : listeners) {
            if (!event.onArrowLoose(player, bow, world, charge, hasAmmo)) {
                return false;
            }
        }
        return true;
    });

    /**
     * called before firing a bow
     * @param player the player firing the bow
     * @param bow the bow item stack
     * @param level   the level
     * @param charge  charge of the bow
     * @param hasAmmo does the player have ammo, is in creative, or has the infinity enchantment
     * @return is firing the bow permitted
     */
    boolean onArrowLoose(Player player, ItemStack bow, Level level, int charge, boolean hasAmmo);
}

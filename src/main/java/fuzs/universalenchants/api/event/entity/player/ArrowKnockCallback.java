package fuzs.universalenchants.api.event.entity.player;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

@FunctionalInterface
public interface ArrowKnockCallback {
    Event<ArrowKnockCallback> EVENT = EventFactory.createArrayBacked(ArrowKnockCallback.class, listeners -> (Player player, ItemStack item, InteractionHand hand, Level level, boolean hasAmmo) -> {
        for (ArrowKnockCallback event : listeners) {
            InteractionResultHolder<ItemStack> resultHolder = event.onArrowKnock(player, item, hand, level, hasAmmo);
            if (resultHolder != null) {
                return resultHolder;
            }
        }
        return null;
    });

    /**
     * fired when the player starts using a bow item
     * @param player the player using the bow
     * @param item bow item stack
     * @param hand hand the bow is used in
     * @param level the level
     * @param hasAmmo has a projectile been found in the player's inventory
     * @return null to continue vanilla behavior (e.g. not having a valid arrow stack will fail to use the bow)
     */
    InteractionResultHolder<ItemStack> onArrowKnock(Player player, ItemStack item, InteractionHand hand, Level level, boolean hasAmmo);
}

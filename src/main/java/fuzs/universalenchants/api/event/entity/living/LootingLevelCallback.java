package fuzs.universalenchants.api.event.entity.living;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;

import javax.annotation.Nullable;

@FunctionalInterface
public interface LootingLevelCallback {
    Event<LootingLevelCallback> EVENT = EventFactory.createArrayBacked(LootingLevelCallback.class, listeners -> (LivingEntity entity, @Nullable DamageSource damageSource, int lootingLevel) -> {
        for (LootingLevelCallback event : listeners) {
            lootingLevel = event.onLootingLevel(entity, damageSource, lootingLevel);
        }
        return lootingLevel;
    });

    /**
     * allows modifying used looting level for calculating bonus drops when an entity is killed
     * @param entity the target entity
     * @param damageSource the damage source the target is killed by, contains killer
     * @param lootingLevel vanilla looting level
     * @return new looting level or <code>lootingLevel</code> if nothing changed
     */
    int onLootingLevel(LivingEntity entity, @Nullable DamageSource damageSource, int lootingLevel);
}

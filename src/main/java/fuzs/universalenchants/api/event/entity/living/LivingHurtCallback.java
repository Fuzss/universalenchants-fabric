package fuzs.universalenchants.api.event.entity.living;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;

@FunctionalInterface
public interface LivingHurtCallback {
    Event<LivingHurtCallback> EVENT = EventFactory.createArrayBacked(LivingHurtCallback.class, listeners -> (LivingEntity entity, DamageSource source, float amount) -> {
        for (LivingHurtCallback event : listeners) {
            if (!event.onLivingHurt(entity, source, amount)) {
                return false;
            }
        }
        return true;
    });

    /**
     * called right before any reduction on damage due to e.g. armor are done, cancelling prevents any damage/armor durability being taken
     * @param entity the entity being hurt
     * @param source damage source entity is hurt by
     * @param amount amount hurt
     * @return false to prevent this entity from being hurt, otherwise vanilla will continue to execute
     */
    boolean onLivingHurt(LivingEntity entity, DamageSource source, float amount);
}

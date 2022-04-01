package fuzs.universalenchants.api.event.world;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

@FunctionalInterface
public interface FarmlandTrampleCallback {
    Event<FarmlandTrampleCallback> EVENT = EventFactory.createArrayBacked(FarmlandTrampleCallback.class, listeners -> (Level level, BlockPos pos, BlockState state, float fallDistance, Entity entity) -> {
        for (FarmlandTrampleCallback event : listeners) {
            if (!event.onFarmlandTrample(level, pos, state, fallDistance, entity)) {
                return false;
            }
        }
        return true;
    });

    /**
     * @param level level farmland block is trampled in
     * @param pos farmland block position
     * @param state blockstate farmland will be converted to after trampling
     * @param fallDistance fall distance of the entity
     * @param entity the entity falling on the farmland block
     * @return is trampling allowed, returning <code>false</code> cancels vanilla behavior
     */
    boolean onFarmlandTrample(Level level, BlockPos pos, BlockState state, float fallDistance, Entity entity);
}

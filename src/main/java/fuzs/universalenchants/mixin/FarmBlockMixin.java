package fuzs.universalenchants.mixin;

import fuzs.universalenchants.api.event.world.FarmlandTrampleCallback;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FarmBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FarmBlock.class)
public abstract class FarmBlockMixin extends Block {
    public FarmBlockMixin(Properties properties) {
        super(properties);
    }

    @Inject(method = "fallOn", at = @At("HEAD"), cancellable = true)
    public void fallOn(Level level, BlockState blockState, BlockPos blockPos, Entity entity, float fallDistance, CallbackInfo callbackInfo) {
        if (!FarmlandTrampleCallback.EVENT.invoker().onFarmlandTrample(level, blockPos, Blocks.DIRT.defaultBlockState(), fallDistance, entity)) {
            super.fallOn(level, blockState, blockPos, entity, fallDistance);
            callbackInfo.cancel();
        }
    }
}

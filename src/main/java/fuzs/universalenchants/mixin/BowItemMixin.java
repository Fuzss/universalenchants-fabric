package fuzs.universalenchants.mixin;

import fuzs.universalenchants.api.event.entity.player.ArrowKnockCallback;
import fuzs.universalenchants.api.event.entity.player.ArrowLooseCallback;
import fuzs.universalenchants.handler.ItemCompatHandler;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ProjectileWeaponItem;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(BowItem.class)
public abstract class BowItemMixin extends ProjectileWeaponItem {
    public BowItemMixin(Properties p_43009_) {
        super(p_43009_);
    }

    @ModifyVariable(method = "releaseUsing", at = @At("STORE"), ordinal = 0)
    public AbstractArrow releaseUsing$storeArrow(AbstractArrow arrow, ItemStack stack) {
        ItemCompatHandler.applyPiercingEnchantment(arrow, stack);
        ItemCompatHandler.applyLootingEnchantment(arrow, stack);
        return arrow;
    }

    @Inject(method = "releaseUsing", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/BowItem;getPowerForTime(I)F"), cancellable = true, locals = LocalCapture.CAPTURE_FAILHARD)
    public void releaseUsing$invokeGetUseDuration(ItemStack bow, Level level, LivingEntity livingEntity, int useDuration, CallbackInfo callbackInfo, Player player, boolean hasInfiniteAmmo, ItemStack arrows, int charge) {
        if (!ArrowLooseCallback.EVENT.invoker().onArrowLoose(player, bow, level, charge, !arrows.isEmpty() || hasInfiniteAmmo)) callbackInfo.cancel();
    }

    @Inject(method = "use", at = @At("HEAD"), cancellable = true)
    public void use$head(Level level, Player player, InteractionHand interactionHand, CallbackInfoReturnable<InteractionResultHolder<ItemStack>> callbackInfo) {
        ItemStack itemstack = player.getItemInHand(interactionHand);
        boolean hasAmmo = !player.getProjectile(itemstack).isEmpty();
        InteractionResultHolder<ItemStack> resultHolder = ArrowKnockCallback.EVENT.invoker().onArrowKnock(player, itemstack, interactionHand, level, hasAmmo);
        if (resultHolder != null) callbackInfo.setReturnValue(resultHolder);
    }
}

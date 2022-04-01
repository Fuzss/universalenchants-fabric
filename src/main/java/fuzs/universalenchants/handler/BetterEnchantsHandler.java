package fuzs.universalenchants.handler;

import fuzs.universalenchants.UniversalEnchants;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;

public class BetterEnchantsHandler {
    public InteractionResultHolder<ItemStack> onArrowNock(Player player, ItemStack item, InteractionHand hand, Level level, boolean hasAmmo) {
        if (!UniversalEnchants.CONFIG.server().trueInfinity) return null;
        // true infinity for bows
        if (EnchantmentHelper.getItemEnchantmentLevel(Enchantments.INFINITY_ARROWS, item) > 0) {
            player.startUsingItem(hand);
            return InteractionResultHolder.consume(item);
        }
        return null;
    }

    public InteractionResultHolder<ItemStack> onArrowNock(Player player, Level world, InteractionHand hand) {
        if (!UniversalEnchants.CONFIG.server().trueInfinity) return InteractionResultHolder.pass(player.getItemInHand(hand));
        // true infinity for bows
        ItemStack stack = player.getItemInHand(hand);
        if (stack.getItem() instanceof BowItem && EnchantmentHelper.getItemEnchantmentLevel(Enchantments.INFINITY_ARROWS, stack) > 0) {
            player.startUsingItem(hand);
            // important to return success even on client since this is how the fabric event works
            return InteractionResultHolder.success(stack);
        }
        return InteractionResultHolder.pass(stack);
    }

    public InteractionResultHolder<ItemStack> onRightClickItem(Player player, Level world, InteractionHand hand) {
        if (!UniversalEnchants.CONFIG.server().trueInfinity) return InteractionResultHolder.pass(player.getItemInHand(hand));
        // true infinity for crossbows
        ItemStack stack = player.getItemInHand(hand);
        if (stack.getItem() instanceof CrossbowItem && EnchantmentHelper.getItemEnchantmentLevel(Enchantments.INFINITY_ARROWS, stack) > 0) {
            if (!CrossbowItem.isCharged(stack)) {
                // resetting startSoundPlayed and midLoadSoundPlayed is not required as they're reset in CrossbowItem#func_219972_a anyways
                player.startUsingItem(hand);
                // important to return success even on client since this is how the fabric event works
                return InteractionResultHolder.success(stack);
            }
        }
        return InteractionResultHolder.pass(stack);
    }

    public void onLivingHurt(LivingEntity entity, DamageSource source, float amount) {
        if (!UniversalEnchants.CONFIG.server().noProjectileImmunity) return;
        // immediately reset damage immunity after being hit by any projectile, fixes multishot
        if (!(entity instanceof Player) && source.isProjectile()) {
            entity.invulnerableTime = 0;
        }
    }

    public boolean onFarmlandTrample(Level level, BlockPos pos, BlockState state, float fallDistance, Entity entity) {
        if (!UniversalEnchants.CONFIG.server().noFarmlandTrample) return true;
        if (entity instanceof LivingEntity entity1) {
            if (EnchantmentHelper.getEnchantmentLevel(Enchantments.FALL_PROTECTION, entity1) > 0) {
                return false;
            }
        }
        return true;
    }

    public int onLivingExperienceDrop(LivingEntity entity, @Nullable Player attackingPlayer, int originalExperience, int droppedExperience) {
        if (!UniversalEnchants.CONFIG.server().lootingBoostsXp) return droppedExperience;
        // very basic hack for multiplying xp by looting level
        // e.g. our code for looting on ranged weapons will not trigger as the damage source is not correct
        // (it will still trigger though when they ranged weapon is still in the main hand, since vanilla checks the main hand enchantments)
        // unfortunately the original damage source is not obtainable in this context
        int level = attackingPlayer != null ? EnchantmentHelper.getMobLooting(entity) : 0;
        if (level > 0) return this.getDroppedXp(droppedExperience, level);
        return droppedExperience;
    }

    private int getDroppedXp(int droppedXp, int level) {
        float multiplier = (level * (level + 1)) / 10.0F;
        return droppedXp + Math.min(50, (int) (droppedXp * multiplier));
    }
}

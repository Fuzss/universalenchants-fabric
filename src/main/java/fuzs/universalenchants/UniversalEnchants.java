package fuzs.universalenchants;

import fuzs.puzzleslib.config.AbstractConfig;
import fuzs.puzzleslib.config.ConfigHolder;
import fuzs.puzzleslib.config.ConfigHolderImpl;
import fuzs.universalenchants.api.event.entity.living.LivingEntityUseItemEvents;
import fuzs.universalenchants.api.event.entity.living.LivingExperienceDropCallback;
import fuzs.universalenchants.api.event.entity.living.LivingHurtCallback;
import fuzs.universalenchants.api.event.entity.living.LootingLevelCallback;
import fuzs.universalenchants.api.event.entity.player.ArrowLooseCallback;
import fuzs.universalenchants.config.ServerConfig;
import fuzs.universalenchants.handler.BetterEnchantsHandler;
import fuzs.universalenchants.handler.EnchantCompatManager;
import fuzs.universalenchants.handler.ItemCompatHandler;
import fuzs.universalenchants.handler.ItemCompatManager;
import fuzs.universalenchants.registry.ModRegistry;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class UniversalEnchants implements ModInitializer {
    public static final String MOD_ID = "universalenchants";
    public static final String MOD_NAME = "Universal Enchants";
    public static final Logger LOGGER = LogManager.getLogger(MOD_NAME);

    @SuppressWarnings("Convert2MethodRef")
    public static final ConfigHolder<AbstractConfig, ServerConfig> CONFIG = ConfigHolder.server(() -> new ServerConfig());

    public static void onConstructMod() {
        ((ConfigHolderImpl<?, ?>) CONFIG).addConfigs(MOD_ID);
        ModRegistry.touch();
        registerHandlers();
        CONFIG.addServerCallback(EnchantCompatManager.INSTANCE::init);
        CONFIG.addServerCallback(ItemCompatManager.INSTANCE::buildData);
    }

    private static void registerHandlers() {
        ItemCompatHandler itemCompatHandler = new ItemCompatHandler();
        ArrowLooseCallback.EVENT.register((Player player, ItemStack bow, Level level, int charge, boolean hasAmmo) -> {
            itemCompatHandler.onArrowLoose(player, bow, level, charge, hasAmmo);
            return true;
        });
        LivingEntityUseItemEvents.TICK.register(itemCompatHandler::onItemUseTick);
        LootingLevelCallback.EVENT.register(itemCompatHandler::onLootingLevel);
        BetterEnchantsHandler betterEnchantsHandler = new BetterEnchantsHandler();
        UseItemCallback.EVENT.register(betterEnchantsHandler::onArrowNock);
        UseItemCallback.EVENT.register(betterEnchantsHandler::onRightClickItem);
        LivingHurtCallback.EVENT.register((LivingEntity entity, DamageSource source, float amount) -> {
            betterEnchantsHandler.onLivingHurt(entity, source, amount);
            return true;
        });
        LivingExperienceDropCallback.EVENT.register(betterEnchantsHandler::onLivingExperienceDrop);
    }

    @Override
    public void onInitialize() {
        onConstructMod();
    }
}

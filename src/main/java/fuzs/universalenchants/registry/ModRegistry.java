package fuzs.universalenchants.registry;

import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import fuzs.puzzleslib.capability.CapabilityController;
import fuzs.universalenchants.UniversalEnchants;
import fuzs.universalenchants.capability.ArrowLootingCapability;
import fuzs.universalenchants.capability.ArrowLootingCapabilityImpl;
import net.minecraft.world.entity.projectile.AbstractArrow;

public class ModRegistry {
    private static final CapabilityController CAPABILITIES = CapabilityController.of(UniversalEnchants.MOD_ID);
    public static final ComponentKey<ArrowLootingCapability> ARROW_LOOTING_CAPABILITY = CAPABILITIES.registerEntityCapability("arrow_looting", ArrowLootingCapability.class, entity -> new ArrowLootingCapabilityImpl(), AbstractArrow.class);

    public static void touch() {

    }
}

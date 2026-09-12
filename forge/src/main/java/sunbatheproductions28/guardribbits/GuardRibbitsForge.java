package sunbatheproductions28.guardribbits;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.bus.BusGroup;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.registries.RegisterEvent;
import sunbatheproductions28.guardribbits.client.GuardRibbitsForgeClient;
import sunbatheproductions28.guardribbits.entity.GuardRibbitEntity;
import sunbatheproductions28.guardribbits.module.EntityTypeModule;
import sunbatheproductions28.guardribbits.registry.DeferredRegistry;
import sunbatheproductions28.guardribbits.registry.GuardRibbitsRegistries;

@Mod(GuardRibbitsCommon.MOD_ID)
public class GuardRibbitsForge {

    public GuardRibbitsForge(FMLJavaModLoadingContext context) {
        BusGroup modBus = context.getModBusGroup();

        GuardRibbitsRegistries.bootstrap();

        RegisterEvent.getBus(modBus).addListener(GuardRibbitsForge::onRegister);
        EntityAttributeCreationEvent.BUS.addListener(GuardRibbitsForge::onEntityAttributeCreation);
        // Registries are only populated once RegisterEvent has run for each of them.
        FMLCommonSetupEvent.getBus(modBus).addListener(event -> event.enqueueWork(GuardRibbitsCommon::init));

        if (FMLEnvironment.dist == Dist.CLIENT) {
            GuardRibbitsForgeClient.init(modBus);
        }
    }

    private static void onRegister(RegisterEvent event) {
        for (DeferredRegistry<?> registry : GuardRibbitsRegistries.all()) {
            register(event, registry);
        }
    }

    /**
     * Captures the registry's element type so the entries can be handed to the event, which ignores
     * everything whose registry key does not match the one currently being filled.
     */
    private static <T> void register(RegisterEvent event, DeferredRegistry<T> registry) {
        registry.forEach((id, value) -> event.register(registry.registryKey(), id, () -> value.get()));
    }

    private static void onEntityAttributeCreation(EntityAttributeCreationEvent event) {
        event.put(EntityTypeModule.GUARD_RIBBIT.get(), GuardRibbitEntity.createGuardRibbitAttributes().build());
    }
}

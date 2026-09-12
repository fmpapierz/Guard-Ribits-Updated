package sunbatheproductions28.guardribbits;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.registries.RegisterEvent;
import sunbatheproductions28.guardribbits.entity.GuardRibbitEntity;
import sunbatheproductions28.guardribbits.module.EntityTypeModule;
import sunbatheproductions28.guardribbits.registry.DeferredRegistry;
import sunbatheproductions28.guardribbits.registry.GuardRibbitsRegistries;

@Mod(value = GuardRibbitsCommon.MOD_ID)
public class GuardRibbitsNeoForge {

    public GuardRibbitsNeoForge(IEventBus eventBus, ModContainer container) {
        GuardRibbitsRegistries.bootstrap();

        eventBus.addListener(GuardRibbitsNeoForge::onRegister);
        eventBus.addListener(GuardRibbitsNeoForge::onEntityAttributeCreation);
        // Registries are only populated once RegisterEvent has run for each of them.
        eventBus.addListener((FMLCommonSetupEvent event) -> event.enqueueWork(GuardRibbitsCommon::init));
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

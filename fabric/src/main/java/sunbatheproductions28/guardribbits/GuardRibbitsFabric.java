package sunbatheproductions28.guardribbits;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import sunbatheproductions28.guardribbits.entity.GuardRibbitEntity;
import sunbatheproductions28.guardribbits.module.EntityTypeModule;
import sunbatheproductions28.guardribbits.registry.DeferredRegistry;
import sunbatheproductions28.guardribbits.registry.GuardRibbitsRegistries;

public class GuardRibbitsFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        // Fabric registries are open during mod init, so the pending entries go straight in.
        GuardRibbitsRegistries.bootstrap();
        GuardRibbitsRegistries.all().forEach(DeferredRegistry::registerAll);
        FabricDefaultAttributeRegistry.register(EntityTypeModule.GUARD_RIBBIT.get(), GuardRibbitEntity.createGuardRibbitAttributes());

        GuardRibbitsCommon.init();
    }
}

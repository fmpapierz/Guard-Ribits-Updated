package sunbatheproductions28.guardribbits.client;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.renderer.entity.EntityRenderers;
import sunbatheproductions28.guardribbits.client.render.GuardRibbitRenderer;
import sunbatheproductions28.guardribbits.module.EntityTypeModule;

public class GuardRibbitsFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        GuardRibbitsCommonClient.init();
        EntityRenderers.register(EntityTypeModule.GUARD_RIBBIT.get(), GuardRibbitRenderer::new);
    }
}

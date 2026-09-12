package sunbatheproductions28.guardribbits.client;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import sunbatheproductions28.guardribbits.GuardRibbitsCommon;
import sunbatheproductions28.guardribbits.client.render.GuardRibbitRenderer;
import sunbatheproductions28.guardribbits.module.EntityTypeModule;

@EventBusSubscriber(modid = GuardRibbitsCommon.MOD_ID, value = Dist.CLIENT)
public class GuardRibbitsNeoForgeClient {

    @SubscribeEvent
    private static void clientSetup(final FMLClientSetupEvent event) {
        GuardRibbitsCommonClient.init();
    }

    @SubscribeEvent
    private static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(EntityTypeModule.GUARD_RIBBIT.get(), GuardRibbitRenderer::new);
    }
}

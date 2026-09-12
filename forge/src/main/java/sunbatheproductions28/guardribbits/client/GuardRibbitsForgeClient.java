package sunbatheproductions28.guardribbits.client;

import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.bus.BusGroup;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import sunbatheproductions28.guardribbits.client.render.GuardRibbitRenderer;
import sunbatheproductions28.guardribbits.module.EntityTypeModule;

public class GuardRibbitsForgeClient {
    public static void init(BusGroup modBus) {
        FMLClientSetupEvent.getBus(modBus).addListener(event -> GuardRibbitsCommonClient.init());
        EntityRenderersEvent.RegisterRenderers.BUS.addListener(GuardRibbitsForgeClient::registerRenderers);
    }

    private static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(EntityTypeModule.GUARD_RIBBIT.get(), GuardRibbitRenderer::new);
    }
}

package sunbatheproductions28.guardribbits.client.render;

import com.geckolib.renderer.GeoEntityRenderer;
import com.geckolib.renderer.base.GeoRenderState;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.resources.Identifier;
import sunbatheproductions28.guardribbits.client.model.GuardRibbitModel;
import sunbatheproductions28.guardribbits.entity.GuardRibbitEntity;

public class GuardRibbitRenderer<R extends LivingEntityRenderState & GeoRenderState> extends GeoEntityRenderer<GuardRibbitEntity, R> {

    public GuardRibbitRenderer(EntityRendererProvider.Context context) {
        super(context, new GuardRibbitModel());
    }

    @Override
    public RenderType getRenderType(R renderState, Identifier texture) {
        return RenderTypes.entityCutout(texture);
    }

    @Override
    public Identifier getTextureLocation(R renderState) {
        return super.getTextureLocation(renderState);
    }

    @Override
    public float getMotionAnimThreshold(GuardRibbitEntity animatable) {
        return 0.0005f;
    }
}

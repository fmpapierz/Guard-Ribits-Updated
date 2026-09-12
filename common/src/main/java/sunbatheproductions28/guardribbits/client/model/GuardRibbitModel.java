package sunbatheproductions28.guardribbits.client.model;

import com.geckolib.model.GeoModel;
import com.geckolib.renderer.base.GeoRenderState;
import net.minecraft.resources.Identifier;
import sunbatheproductions28.guardribbits.GuardRibbitsCommon;
import sunbatheproductions28.guardribbits.entity.GuardRibbitEntity;

public class GuardRibbitModel extends GeoModel<GuardRibbitEntity> {
    private static final Identifier MODEL = GuardRibbitsCommon.id("geo/guard_ribbit.geo.json");
    private static final Identifier TEXTURE = GuardRibbitsCommon.id("textures/entity/guard_ribbit.png");
    private static final Identifier ANIMATION = GuardRibbitsCommon.id("animations/guard_ribbit.animation.json");

    @Override
    public Identifier getModelResource(GeoRenderState renderState) {
        return MODEL;
    }

    @Override
    public Identifier getTextureResource(GeoRenderState renderState) {
        return TEXTURE;
    }

    @Override
    public Identifier getAnimationResource(GuardRibbitEntity animatable) {
        return ANIMATION;
    }
}

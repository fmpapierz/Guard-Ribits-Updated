package sunbatheproductions28.guardribbits.module;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import sunbatheproductions28.guardribbits.GuardRibbitsCommon;
import sunbatheproductions28.guardribbits.entity.GuardRibbitEntity;
import sunbatheproductions28.guardribbits.registry.GuardRibbitsRegistries;
import sunbatheproductions28.guardribbits.registry.RegistrySupplier;

public class EntityTypeModule {
    public static final ResourceKey<EntityType<?>> GUARD_RIBBIT_KEY =
            ResourceKey.create(Registries.ENTITY_TYPE, GuardRibbitsCommon.id("guard_ribbit"));

    public static final RegistrySupplier<EntityType<GuardRibbitEntity>> GUARD_RIBBIT =
            GuardRibbitsRegistries.ENTITY_TYPES.add(
                    "guard_ribbit",
                    () -> EntityType.Builder
                            .of(GuardRibbitEntity::new, MobCategory.CREATURE)
                            .sized(0.5f, 0.75f)
                            .build(GUARD_RIBBIT_KEY));
}

package sunbatheproductions28.guardribbits.module;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SpawnEggItem;
import sunbatheproductions28.guardribbits.GuardRibbitsCommon;
import sunbatheproductions28.guardribbits.registry.GuardRibbitsRegistries;
import sunbatheproductions28.guardribbits.registry.RegistrySupplier;

public class ItemModule {
    private static final ResourceKey<Item> SPAWN_EGG_KEY =
            ResourceKey.create(Registries.ITEM, GuardRibbitsCommon.id("ribbit_guard_spawn_egg"));

    public static final RegistrySupplier<Item> RIBBIT_GUARD_SPAWN_EGG = GuardRibbitsRegistries.ITEMS.add(
            "ribbit_guard_spawn_egg",
            () -> new SpawnEggItem(new Item.Properties()
                    .spawnEgg(EntityTypeModule.GUARD_RIBBIT.get())
                    .setId(SPAWN_EGG_KEY)));
}

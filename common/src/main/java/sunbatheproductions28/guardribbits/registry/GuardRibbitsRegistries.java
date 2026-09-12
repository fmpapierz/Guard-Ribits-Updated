package sunbatheproductions28.guardribbits.registry;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import sunbatheproductions28.guardribbits.module.CreativeTabModule;
import sunbatheproductions28.guardribbits.module.EntityTypeModule;
import sunbatheproductions28.guardribbits.module.ItemModule;

import java.util.List;

/**
 * Every registry this mod contributes to, in the order the entries have to be created.
 */
public final class GuardRibbitsRegistries {
    public static final DeferredRegistry<EntityType<?>> ENTITY_TYPES =
            DeferredRegistry.of(BuiltInRegistries.ENTITY_TYPE, Registries.ENTITY_TYPE);
    public static final DeferredRegistry<Item> ITEMS =
            DeferredRegistry.of(BuiltInRegistries.ITEM, Registries.ITEM);
    public static final DeferredRegistry<CreativeModeTab> CREATIVE_TABS =
            DeferredRegistry.of(BuiltInRegistries.CREATIVE_MODE_TAB, Registries.CREATIVE_MODE_TAB);

    private static final List<DeferredRegistry<?>> ALL = List.of(ENTITY_TYPES, ITEMS, CREATIVE_TABS);

    private GuardRibbitsRegistries() {
    }

    /**
     * All registries, in creation order.
     */
    public static List<DeferredRegistry<?>> all() {
        return ALL;
    }

    /**
     * Loads the module classes so their static fields queue themselves up. Registration itself is
     * driven by the loader.
     */
    public static void bootstrap() {
        touch(EntityTypeModule.class, ItemModule.class, CreativeTabModule.class);
    }

    private static void touch(Class<?>... classes) {
        for (Class<?> clazz : classes) {
            try {
                Class.forName(clazz.getName(), true, clazz.getClassLoader());
            } catch (ClassNotFoundException e) {
                throw new IllegalStateException("Unable to load " + clazz.getName(), e);
            }
        }
    }
}

package sunbatheproductions28.guardribbits.module;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import sunbatheproductions28.guardribbits.registry.GuardRibbitsRegistries;
import sunbatheproductions28.guardribbits.registry.RegistrySupplier;

public class CreativeTabModule {
    public static final RegistrySupplier<CreativeModeTab> TAB = GuardRibbitsRegistries.CREATIVE_TABS.add(
            "general",
            () -> CreativeModeTab.builder(CreativeModeTab.Row.TOP, 0)
                    .title(Component.translatable("itemGroup.guardribbits.general"))
                    .icon(() -> new ItemStack(ItemModule.RIBBIT_GUARD_SPAWN_EGG.get()))
                    .displayItems((parameters, output) -> output.accept(ItemModule.RIBBIT_GUARD_SPAWN_EGG.get()))
                    .build());
}

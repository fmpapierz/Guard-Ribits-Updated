package sunbatheproductions28.guardribbits.registry;

import sunbatheproductions28.guardribbits.GuardRibbitsCommon;
import net.minecraft.core.Registry;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

/**
 * Collects entries for one vanilla registry so that they can be handed to whichever registration
 * mechanism the current loader uses. Fabric and Quilt write straight into the registry, while Forge
 * and NeoForge replay the entries during their own register event.
 */
public final class DeferredRegistry<T> {
    private final Registry<T> registry;
    private final ResourceKey<? extends Registry<T>> registryKey;
    private final List<RegistrySupplier<? extends T>> entries = new ArrayList<>();

    private DeferredRegistry(Registry<T> registry, ResourceKey<? extends Registry<T>> registryKey) {
        this.registry = registry;
        this.registryKey = registryKey;
    }

    public static <T> DeferredRegistry<T> of(Registry<T> registry, ResourceKey<? extends Registry<T>> registryKey) {
        return new DeferredRegistry<>(registry, registryKey);
    }

    public <R extends T> RegistrySupplier<R> add(String name, Supplier<R> factory) {
        RegistrySupplier<R> entry = new RegistrySupplier<>(GuardRibbitsCommon.id(name), factory);
        this.entries.add(entry);
        return entry;
    }

    public Registry<T> registry() {
        return this.registry;
    }

    public ResourceKey<? extends Registry<T>> registryKey() {
        return this.registryKey;
    }

    public List<RegistrySupplier<? extends T>> entries() {
        return Collections.unmodifiableList(this.entries);
    }

    /** Writes every pending entry straight into the vanilla registry. Used on Fabric and Quilt. */
    public void registerAll() {
        for (RegistrySupplier<? extends T> entry : this.entries) {
            Registry.register(this.registry, entry.id(), entry.create());
        }
    }

    /** Hands every pending entry to {@code consumer}. Used on Forge and NeoForge. */
    public void forEach(EntryConsumer<T> consumer) {
        for (RegistrySupplier<? extends T> entry : this.entries) {
            consumer.accept(entry.id(), entry::create);
        }
    }

    @FunctionalInterface
    public interface EntryConsumer<T> {
        void accept(Identifier id, Supplier<? extends T> value);
    }
}

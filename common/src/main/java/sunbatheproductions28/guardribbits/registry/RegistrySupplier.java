package sunbatheproductions28.guardribbits.registry;

import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.function.Supplier;

/**
 * A single pending registry entry.
 * <p>
 * The value is not built when the entry is declared, because the registries are not open at class
 * initialization time on every loader. It is built once, when the owning {@link DeferredRegistry} is
 * drained, and can be read back afterwards through {@link #get()}.
 */
public final class RegistrySupplier<T> implements Supplier<T> {
    private final Identifier id;
    private final Supplier<? extends T> factory;
    private @Nullable T value;

    RegistrySupplier(Identifier id, Supplier<? extends T> factory) {
        this.id = Objects.requireNonNull(id, "id");
        this.factory = Objects.requireNonNull(factory, "factory");
    }

    public Identifier id() {
        return this.id;
    }

    /**
     * Builds the value if it has not been built yet, and returns it. Called by the loader-specific
     * registration code; mod code should use {@link #get()}.
     */
    T create() {
        if (this.value == null) {
            this.value = Objects.requireNonNull(this.factory.get(), () -> "Factory for " + this.id + " returned null");
        }
        return this.value;
    }

    /**
     * @throws IllegalStateException if the entry has not been registered yet.
     */
    @Override
    public T get() {
        if (this.value == null) {
            throw new IllegalStateException(this.id + " was accessed before it was registered");
        }
        return this.value;
    }

    public boolean isBound() {
        return this.value != null;
    }

    @Override
    public String toString() {
        return "RegistrySupplier[" + this.id + "]";
    }
}

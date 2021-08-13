package net.minecraft.server.dedicated;

import java.util.function.Supplier;
import org.apache.logging.log4j.LogManager;
import net.minecraft.core.RegistryAccess;
import java.util.Map;
import java.util.Objects;
import java.util.function.UnaryOperator;
import com.google.common.base.MoreObjects;
import javax.annotation.Nullable;
import java.util.function.IntFunction;
import java.util.function.Function;
import java.io.OutputStream;
import java.io.InputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.util.Properties;
import org.apache.logging.log4j.Logger;

public abstract class Settings<T extends Settings<T>> {
    private static final Logger LOGGER;
    private final Properties properties;
    
    public Settings(final Properties properties) {
        this.properties = properties;
    }
    
    public static Properties loadFromFile(final Path path) {
        final Properties properties2 = new Properties();
        try (final InputStream inputStream3 = Files.newInputStream(path, new OpenOption[0])) {
            properties2.load(inputStream3);
        }
        catch (IOException iOException3) {
            Settings.LOGGER.error(new StringBuilder().append("Failed to load properties from file: ").append(path).toString());
        }
        return properties2;
    }
    
    public void store(final Path path) {
        try (final OutputStream outputStream3 = Files.newOutputStream(path, new OpenOption[0])) {
            this.properties.store(outputStream3, "Minecraft server properties");
        }
        catch (IOException iOException3) {
            Settings.LOGGER.error(new StringBuilder().append("Failed to store properties to file: ").append(path).toString());
        }
    }
    
    private static <V extends Number> Function<String, V> wrapNumberDeserializer(final Function<String, V> function) {
        return (Function<String, V>)(string -> {
            try {
                return (Number)function.apply(string);
            }
            catch (NumberFormatException numberFormatException3) {
                return null;
            }
        });
    }
    
    protected static <V> Function<String, V> dispatchNumberOrString(final IntFunction<V> intFunction, final Function<String, V> function) {
        return (Function<String, V>)(string -> {
            try {
                return intFunction.apply(Integer.parseInt(string));
            }
            catch (NumberFormatException numberFormatException4) {
                return function.apply(string);
            }
        });
    }
    
    @Nullable
    private String getStringRaw(final String string) {
        return (String)this.properties.get(string);
    }
    
    @Nullable
    protected <V> V getLegacy(final String string, final Function<String, V> function) {
        final String string2 = this.getStringRaw(string);
        if (string2 == null) {
            return null;
        }
        this.properties.remove(string);
        return (V)function.apply(string2);
    }
    
    protected <V> V get(final String string, final Function<String, V> function2, final Function<V, String> function3, final V object) {
        final String string2 = this.getStringRaw(string);
        final V object2 = (V)MoreObjects.firstNonNull((string2 != null) ? function2.apply(string2) : null, object);
        this.properties.put(string, function3.apply(object2));
        return object2;
    }
    
    protected <V> MutableValue<V> getMutable(final String string, final Function<String, V> function2, final Function<V, String> function3, final V object) {
        final String string2 = this.getStringRaw(string);
        final V object2 = (V)MoreObjects.firstNonNull((string2 != null) ? function2.apply(string2) : null, object);
        this.properties.put(string, function3.apply(object2));
        return new MutableValue<V>(string, object2, (Function)function3);
    }
    
    protected <V> V get(final String string, final Function<String, V> function2, final UnaryOperator<V> unaryOperator, final Function<V, String> function4, final V object) {
        return this.<V>get(string, (java.util.function.Function<String, V>)(string -> {
            final Object object4 = function2.apply(string);
            return (object4 != null) ? unaryOperator.apply(object4) : null;
        }), function4, object);
    }
    
    protected <V> V get(final String string, final Function<String, V> function, final V object) {
        return this.<V>get(string, function, (java.util.function.Function<V, String>)Objects::toString, object);
    }
    
    protected <V> MutableValue<V> getMutable(final String string, final Function<String, V> function, final V object) {
        return this.<V>getMutable(string, function, (java.util.function.Function<V, String>)Objects::toString, object);
    }
    
    protected String get(final String string1, final String string2) {
        return this.<String>get(string1, (java.util.function.Function<String, String>)Function.identity(), (java.util.function.Function<String, String>)Function.identity(), string2);
    }
    
    @Nullable
    protected String getLegacyString(final String string) {
        return this.<String>getLegacy(string, (java.util.function.Function<String, String>)Function.identity());
    }
    
    protected int get(final String string, final int integer) {
        return this.<Integer>get(string, (java.util.function.Function<String, Integer>)Settings.<V>wrapNumberDeserializer((java.util.function.Function<String, V>)Integer::parseInt), integer);
    }
    
    protected MutableValue<Integer> getMutable(final String string, final int integer) {
        return this.<Integer>getMutable(string, (java.util.function.Function<String, Integer>)Settings.<V>wrapNumberDeserializer((java.util.function.Function<String, V>)Integer::parseInt), integer);
    }
    
    protected int get(final String string, final UnaryOperator<Integer> unaryOperator, final int integer) {
        return this.<Integer>get(string, (java.util.function.Function<String, Integer>)Settings.<V>wrapNumberDeserializer((java.util.function.Function<String, V>)Integer::parseInt), unaryOperator, (java.util.function.Function<Integer, String>)Objects::toString, integer);
    }
    
    protected long get(final String string, final long long2) {
        return this.<Long>get(string, (java.util.function.Function<String, Long>)Settings.<V>wrapNumberDeserializer((java.util.function.Function<String, V>)Long::parseLong), long2);
    }
    
    protected boolean get(final String string, final boolean boolean2) {
        return this.<Boolean>get(string, (java.util.function.Function<String, Boolean>)Boolean::valueOf, boolean2);
    }
    
    protected MutableValue<Boolean> getMutable(final String string, final boolean boolean2) {
        return this.<Boolean>getMutable(string, (java.util.function.Function<String, Boolean>)Boolean::valueOf, boolean2);
    }
    
    @Nullable
    protected Boolean getLegacyBoolean(final String string) {
        return this.<Boolean>getLegacy(string, (java.util.function.Function<String, Boolean>)Boolean::valueOf);
    }
    
    protected Properties cloneProperties() {
        final Properties properties2 = new Properties();
        properties2.putAll((Map)this.properties);
        return properties2;
    }
    
    protected abstract T reload(final RegistryAccess gn, final Properties properties);
    
    static {
        LOGGER = LogManager.getLogger();
    }
    
    public class MutableValue<V> implements Supplier<V> {
        private final String key;
        private final V value;
        private final Function<V, String> serializer;
        
        private MutableValue(final String string, final V object, final Function<V, String> function) {
            this.key = string;
            this.value = object;
            this.serializer = function;
        }
        
        public V get() {
            return this.value;
        }
        
        public T update(final RegistryAccess gn, final V object) {
            final Properties properties4 = Settings.this.cloneProperties();
            properties4.put(this.key, this.serializer.apply(object));
            return Settings.this.reload(gn, properties4);
        }
    }
}

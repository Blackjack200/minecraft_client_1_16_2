package net.minecraft.world.level;

import com.mojang.datafixers.kinds.Applicative;
import com.mojang.datafixers.kinds.App;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Collection;
import com.google.common.collect.ImmutableList;
import java.util.List;
import com.mojang.serialization.Codec;

public class DataPackConfig {
    public static final DataPackConfig DEFAULT;
    public static final Codec<DataPackConfig> CODEC;
    private final List<String> enabled;
    private final List<String> disabled;
    
    public DataPackConfig(final List<String> list1, final List<String> list2) {
        this.enabled = (List<String>)ImmutableList.copyOf((Collection)list1);
        this.disabled = (List<String>)ImmutableList.copyOf((Collection)list2);
    }
    
    public List<String> getEnabled() {
        return this.enabled;
    }
    
    public List<String> getDisabled() {
        return this.disabled;
    }
    
    static {
        DEFAULT = new DataPackConfig((List<String>)ImmutableList.of("vanilla"), (List<String>)ImmutableList.of());
        CODEC = RecordCodecBuilder.create(instance -> instance.group((App)Codec.STRING.listOf().fieldOf("Enabled").forGetter(brh -> brh.enabled), (App)Codec.STRING.listOf().fieldOf("Disabled").forGetter(brh -> brh.disabled)).apply((Applicative)instance, DataPackConfig::new));
    }
}

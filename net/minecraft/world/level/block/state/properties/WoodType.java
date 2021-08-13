package net.minecraft.world.level.block.state.properties;

import it.unimi.dsi.fastutil.objects.ObjectArraySet;
import java.util.stream.Stream;
import java.util.Set;

public class WoodType {
    private static final Set<WoodType> VALUES;
    public static final WoodType OAK;
    public static final WoodType SPRUCE;
    public static final WoodType BIRCH;
    public static final WoodType ACACIA;
    public static final WoodType JUNGLE;
    public static final WoodType DARK_OAK;
    public static final WoodType CRIMSON;
    public static final WoodType WARPED;
    private final String name;
    
    protected WoodType(final String string) {
        this.name = string;
    }
    
    private static WoodType register(final WoodType cfn) {
        WoodType.VALUES.add(cfn);
        return cfn;
    }
    
    public static Stream<WoodType> values() {
        return (Stream<WoodType>)WoodType.VALUES.stream();
    }
    
    public String name() {
        return this.name;
    }
    
    static {
        VALUES = (Set)new ObjectArraySet();
        OAK = register(new WoodType("oak"));
        SPRUCE = register(new WoodType("spruce"));
        BIRCH = register(new WoodType("birch"));
        ACACIA = register(new WoodType("acacia"));
        JUNGLE = register(new WoodType("jungle"));
        DARK_OAK = register(new WoodType("dark_oak"));
        CRIMSON = register(new WoodType("crimson"));
        WARPED = register(new WoodType("warped"));
    }
}

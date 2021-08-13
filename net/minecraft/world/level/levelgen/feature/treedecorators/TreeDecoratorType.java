package net.minecraft.world.level.levelgen.feature.treedecorators;

import net.minecraft.core.Registry;
import com.mojang.serialization.Codec;

public class TreeDecoratorType<P extends TreeDecorator> {
    public static final TreeDecoratorType<TrunkVineDecorator> TRUNK_VINE;
    public static final TreeDecoratorType<LeaveVineDecorator> LEAVE_VINE;
    public static final TreeDecoratorType<CocoaDecorator> COCOA;
    public static final TreeDecoratorType<BeehiveDecorator> BEEHIVE;
    public static final TreeDecoratorType<AlterGroundDecorator> ALTER_GROUND;
    private final Codec<P> codec;
    
    private static <P extends TreeDecorator> TreeDecoratorType<P> register(final String string, final Codec<P> codec) {
        return Registry.<TreeDecoratorType<P>>register(Registry.TREE_DECORATOR_TYPES, string, new TreeDecoratorType<P>(codec));
    }
    
    private TreeDecoratorType(final Codec<P> codec) {
        this.codec = codec;
    }
    
    public Codec<P> codec() {
        return this.codec;
    }
    
    static {
        TRUNK_VINE = TreeDecoratorType.<TrunkVineDecorator>register("trunk_vine", TrunkVineDecorator.CODEC);
        LEAVE_VINE = TreeDecoratorType.<LeaveVineDecorator>register("leave_vine", LeaveVineDecorator.CODEC);
        COCOA = TreeDecoratorType.<CocoaDecorator>register("cocoa", CocoaDecorator.CODEC);
        BEEHIVE = TreeDecoratorType.<BeehiveDecorator>register("beehive", BeehiveDecorator.CODEC);
        ALTER_GROUND = TreeDecoratorType.<AlterGroundDecorator>register("alter_ground", AlterGroundDecorator.CODEC);
    }
}

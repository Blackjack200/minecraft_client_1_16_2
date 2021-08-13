package net.minecraft.world.level.levelgen.flat;

import com.mojang.datafixers.kinds.Applicative;
import net.minecraft.world.level.block.Blocks;
import com.mojang.datafixers.kinds.App;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Registry;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import com.mojang.serialization.Codec;

public class FlatLayerInfo {
    public static final Codec<FlatLayerInfo> CODEC;
    private final BlockState blockState;
    private final int height;
    private int start;
    
    public FlatLayerInfo(final int integer, final Block bul) {
        this.height = integer;
        this.blockState = bul.defaultBlockState();
    }
    
    public int getHeight() {
        return this.height;
    }
    
    public BlockState getBlockState() {
        return this.blockState;
    }
    
    public int getStart() {
        return this.start;
    }
    
    public void setStart(final int integer) {
        this.start = integer;
    }
    
    public String toString() {
        return new StringBuilder().append((this.height != 1) ? new StringBuilder().append(this.height).append("*").toString() : "").append(Registry.BLOCK.getKey(this.blockState.getBlock())).toString();
    }
    
    static {
        CODEC = RecordCodecBuilder.create(instance -> instance.group((App)Codec.intRange(0, 256).fieldOf("height").forGetter(FlatLayerInfo::getHeight), (App)Registry.BLOCK.fieldOf("block").orElse(Blocks.AIR).forGetter(cpb -> cpb.getBlockState().getBlock())).apply((Applicative)instance, FlatLayerInfo::new));
    }
}

package net.minecraft.world.level.levelgen.feature.configurations;

import com.mojang.datafixers.kinds.Applicative;
import com.mojang.datafixers.kinds.App;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.List;
import net.minecraft.world.level.block.state.BlockState;
import com.mojang.serialization.Codec;

public class SimpleBlockConfiguration implements FeatureConfiguration {
    public static final Codec<SimpleBlockConfiguration> CODEC;
    public final BlockState toPlace;
    public final List<BlockState> placeOn;
    public final List<BlockState> placeIn;
    public final List<BlockState> placeUnder;
    
    public SimpleBlockConfiguration(final BlockState cee, final List<BlockState> list2, final List<BlockState> list3, final List<BlockState> list4) {
        this.toPlace = cee;
        this.placeOn = list2;
        this.placeIn = list3;
        this.placeUnder = list4;
    }
    
    static {
        CODEC = RecordCodecBuilder.create(instance -> instance.group((App)BlockState.CODEC.fieldOf("to_place").forGetter(cmq -> cmq.toPlace), (App)BlockState.CODEC.listOf().fieldOf("place_on").forGetter(cmq -> cmq.placeOn), (App)BlockState.CODEC.listOf().fieldOf("place_in").forGetter(cmq -> cmq.placeIn), (App)BlockState.CODEC.listOf().fieldOf("place_under").forGetter(cmq -> cmq.placeUnder)).apply((Applicative)instance, SimpleBlockConfiguration::new));
    }
}

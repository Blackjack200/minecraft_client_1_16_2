package net.minecraft.world.level.levelgen.structure;

import java.util.stream.Collectors;
import java.util.Arrays;
import java.util.function.Function;
import java.util.function.Supplier;
import javax.annotation.Nullable;
import java.util.Map;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import java.util.Random;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.core.RegistryAccess;
import com.mojang.serialization.Codec;
import net.minecraft.world.level.levelgen.feature.configurations.OceanRuinConfiguration;
import net.minecraft.world.level.levelgen.feature.StructureFeature;

public class OceanRuinFeature extends StructureFeature<OceanRuinConfiguration> {
    public OceanRuinFeature(final Codec<OceanRuinConfiguration> codec) {
        super(codec);
    }
    
    @Override
    public StructureStartFactory<OceanRuinConfiguration> getStartFactory() {
        return OceanRuinStart::new;
    }
    
    public static class OceanRuinStart extends StructureStart<OceanRuinConfiguration> {
        public OceanRuinStart(final StructureFeature<OceanRuinConfiguration> ckx, final int integer2, final int integer3, final BoundingBox cqx, final int integer5, final long long6) {
            super(ckx, integer2, integer3, cqx, integer5, long6);
        }
        
        @Override
        public void generatePieces(final RegistryAccess gn, final ChunkGenerator cfv, final StructureManager cst, final int integer4, final int integer5, final Biome bss, final OceanRuinConfiguration cmf) {
            final int integer6 = integer4 * 16;
            final int integer7 = integer5 * 16;
            final BlockPos fx11 = new BlockPos(integer6, 90, integer7);
            final Rotation bzj12 = Rotation.getRandom(this.random);
            OceanRuinPieces.addPieces(cst, fx11, bzj12, this.pieces, this.random, cmf);
            this.calculateBoundingBox();
        }
    }
    
    public enum Type implements StringRepresentable {
        WARM("warm"), 
        COLD("cold");
        
        public static final Codec<Type> CODEC;
        private static final Map<String, Type> BY_NAME;
        private final String name;
        
        private Type(final String string3) {
            this.name = string3;
        }
        
        public String getName() {
            return this.name;
        }
        
        @Nullable
        public static Type byName(final String string) {
            return (Type)Type.BY_NAME.get(string);
        }
        
        public String getSerializedName() {
            return this.name;
        }
        
        static {
            CODEC = StringRepresentable.<Type>fromEnum((java.util.function.Supplier<Type[]>)Type::values, (java.util.function.Function<? super String, ? extends Type>)Type::byName);
            BY_NAME = (Map)Arrays.stream((Object[])values()).collect(Collectors.toMap(Type::getName, b -> b));
        }
    }
}

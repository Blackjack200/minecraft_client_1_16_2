package net.minecraft.world.level.levelgen.feature;

import java.util.Iterator;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import java.util.Random;
import net.minecraft.world.level.levelgen.structure.MineShaftPieces;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import java.util.stream.Collectors;
import java.util.Arrays;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.Map;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.chunk.ChunkGenerator;
import com.mojang.serialization.Codec;
import net.minecraft.world.level.levelgen.feature.configurations.MineshaftConfiguration;

public class MineshaftFeature extends StructureFeature<MineshaftConfiguration> {
    public MineshaftFeature(final Codec<MineshaftConfiguration> codec) {
        super(codec);
    }
    
    @Override
    protected boolean isFeatureChunk(final ChunkGenerator cfv, final BiomeSource bsv, final long long3, final WorldgenRandom chu, final int integer5, final int integer6, final Biome bss, final ChunkPos bra, final MineshaftConfiguration cmb) {
        chu.setLargeFeatureSeed(long3, integer5, integer6);
        final double double12 = cmb.probability;
        return chu.nextDouble() < double12;
    }
    
    @Override
    public StructureStartFactory<MineshaftConfiguration> getStartFactory() {
        return MineShaftStart::new;
    }
    
    public enum Type implements StringRepresentable {
        NORMAL("normal"), 
        MESA("mesa");
        
        public static final Codec<Type> CODEC;
        private static final Map<String, Type> BY_NAME;
        private final String name;
        
        private Type(final String string3) {
            this.name = string3;
        }
        
        public String getName() {
            return this.name;
        }
        
        private static Type byName(final String string) {
            return (Type)Type.BY_NAME.get(string);
        }
        
        public static Type byId(final int integer) {
            if (integer < 0 || integer >= values().length) {
                return Type.NORMAL;
            }
            return values()[integer];
        }
        
        public String getSerializedName() {
            return this.name;
        }
        
        static {
            CODEC = StringRepresentable.<Type>fromEnum((java.util.function.Supplier<Type[]>)Type::values, (java.util.function.Function<? super String, ? extends Type>)Type::byName);
            BY_NAME = (Map)Arrays.stream((Object[])values()).collect(Collectors.toMap(Type::getName, b -> b));
        }
    }
    
    public static class MineShaftStart extends StructureStart<MineshaftConfiguration> {
        public MineShaftStart(final StructureFeature<MineshaftConfiguration> ckx, final int integer2, final int integer3, final BoundingBox cqx, final int integer5, final long long6) {
            super(ckx, integer2, integer3, cqx, integer5, long6);
        }
        
        @Override
        public void generatePieces(final RegistryAccess gn, final ChunkGenerator cfv, final StructureManager cst, final int integer4, final int integer5, final Biome bss, final MineshaftConfiguration cmb) {
            final MineShaftPieces.MineShaftRoom d9 = new MineShaftPieces.MineShaftRoom(0, this.random, (integer4 << 4) + 2, (integer5 << 4) + 2, cmb.type);
            this.pieces.add(d9);
            d9.addChildren(d9, this.pieces, this.random);
            this.calculateBoundingBox();
            if (cmb.type == Type.MESA) {
                final int integer6 = -5;
                final int integer7 = cfv.getSeaLevel() - this.boundingBox.y1 + this.boundingBox.getYSpan() / 2 + 5;
                this.boundingBox.move(0, integer7, 0);
                for (final StructurePiece crr13 : this.pieces) {
                    crr13.move(0, integer7, 0);
                }
            }
            else {
                this.moveBelowSeaLevel(cfv.getSeaLevel(), this.random, 10);
            }
        }
    }
}

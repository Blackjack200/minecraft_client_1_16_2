package net.minecraft.world.level.levelgen.feature;

import java.util.Arrays;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.Map;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.core.Vec3i;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.Util;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import net.minecraft.world.level.block.state.BlockState;
import java.util.Iterator;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.levelgen.Heightmap;
import java.util.stream.Collectors;
import java.util.List;
import com.google.common.collect.ImmutableList;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.RuinedPortalPiece;
import net.minecraft.world.level.chunk.ChunkGenerator;
import java.util.Random;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.core.BlockPos;
import com.mojang.serialization.Codec;
import net.minecraft.world.level.levelgen.feature.configurations.RuinedPortalConfiguration;

public class RuinedPortalFeature extends StructureFeature<RuinedPortalConfiguration> {
    private static final String[] STRUCTURE_LOCATION_PORTALS;
    private static final String[] STRUCTURE_LOCATION_GIANT_PORTALS;
    
    public RuinedPortalFeature(final Codec<RuinedPortalConfiguration> codec) {
        super(codec);
    }
    
    @Override
    public StructureStartFactory<RuinedPortalConfiguration> getStartFactory() {
        return FeatureStart::new;
    }
    
    private static boolean isCold(final BlockPos fx, final Biome bss) {
        return bss.getTemperature(fx) < 0.15f;
    }
    
    private static int findSuitableY(final Random random, final ChunkGenerator cfv, final RuinedPortalPiece.VerticalPlacement b, final boolean boolean4, final int integer5, final int integer6, final BoundingBox cqx) {
        int integer7;
        if (b == RuinedPortalPiece.VerticalPlacement.IN_NETHER) {
            if (boolean4) {
                integer7 = randomIntInclusive(random, 32, 100);
            }
            else if (random.nextFloat() < 0.5f) {
                integer7 = randomIntInclusive(random, 27, 29);
            }
            else {
                integer7 = randomIntInclusive(random, 29, 100);
            }
        }
        else if (b == RuinedPortalPiece.VerticalPlacement.IN_MOUNTAIN) {
            final int integer8 = integer5 - integer6;
            integer7 = getRandomWithinInterval(random, 70, integer8);
        }
        else if (b == RuinedPortalPiece.VerticalPlacement.UNDERGROUND) {
            final int integer8 = integer5 - integer6;
            integer7 = getRandomWithinInterval(random, 15, integer8);
        }
        else if (b == RuinedPortalPiece.VerticalPlacement.PARTLY_BURIED) {
            integer7 = integer5 - integer6 + randomIntInclusive(random, 2, 8);
        }
        else {
            integer7 = integer5;
        }
        final List<BlockPos> list9 = (List<BlockPos>)ImmutableList.of(new BlockPos(cqx.x0, 0, cqx.z0), new BlockPos(cqx.x1, 0, cqx.z0), new BlockPos(cqx.x0, 0, cqx.z1), new BlockPos(cqx.x1, 0, cqx.z1));
        final List<BlockGetter> list10 = (List<BlockGetter>)list9.stream().map(fx -> cfv.getBaseColumn(fx.getX(), fx.getZ())).collect(Collectors.toList());
        final Heightmap.Types a11 = (b == RuinedPortalPiece.VerticalPlacement.ON_OCEAN_FLOOR) ? Heightmap.Types.OCEAN_FLOOR_WG : Heightmap.Types.WORLD_SURFACE_WG;
        final BlockPos.MutableBlockPos a12 = new BlockPos.MutableBlockPos();
        int integer9 = 0;
    Label_0385:
        for (integer9 = integer7; integer9 > 15; --integer9) {
            int integer10 = 0;
            a12.set(0, integer9, 0);
            for (final BlockGetter bqz16 : list10) {
                final BlockState cee17 = bqz16.getBlockState(a12);
                if (cee17 != null && a11.isOpaque().test(cee17) && ++integer10 == 3) {
                    break Label_0385;
                }
            }
        }
        return integer9;
    }
    
    private static int randomIntInclusive(final Random random, final int integer2, final int integer3) {
        return random.nextInt(integer3 - integer2 + 1) + integer2;
    }
    
    private static int getRandomWithinInterval(final Random random, final int integer2, final int integer3) {
        if (integer2 < integer3) {
            return randomIntInclusive(random, integer2, integer3);
        }
        return integer3;
    }
    
    static {
        STRUCTURE_LOCATION_PORTALS = new String[] { "ruined_portal/portal_1", "ruined_portal/portal_2", "ruined_portal/portal_3", "ruined_portal/portal_4", "ruined_portal/portal_5", "ruined_portal/portal_6", "ruined_portal/portal_7", "ruined_portal/portal_8", "ruined_portal/portal_9", "ruined_portal/portal_10" };
        STRUCTURE_LOCATION_GIANT_PORTALS = new String[] { "ruined_portal/giant_portal_1", "ruined_portal/giant_portal_2", "ruined_portal/giant_portal_3" };
    }
    
    public static class FeatureStart extends StructureStart<RuinedPortalConfiguration> {
        protected FeatureStart(final StructureFeature<RuinedPortalConfiguration> ckx, final int integer2, final int integer3, final BoundingBox cqx, final int integer5, final long long6) {
            super(ckx, integer2, integer3, cqx, integer5, long6);
        }
        
        @Override
        public void generatePieces(final RegistryAccess gn, final ChunkGenerator cfv, final StructureManager cst, final int integer4, final int integer5, final Biome bss, final RuinedPortalConfiguration cmo) {
            final RuinedPortalPiece.Properties a10 = new RuinedPortalPiece.Properties();
            RuinedPortalPiece.VerticalPlacement b9;
            if (cmo.portalType == Type.DESERT) {
                b9 = RuinedPortalPiece.VerticalPlacement.PARTLY_BURIED;
                a10.airPocket = false;
                a10.mossiness = 0.0f;
            }
            else if (cmo.portalType == Type.JUNGLE) {
                b9 = RuinedPortalPiece.VerticalPlacement.ON_LAND_SURFACE;
                a10.airPocket = (this.random.nextFloat() < 0.5f);
                a10.mossiness = 0.8f;
                a10.overgrown = true;
                a10.vines = true;
            }
            else if (cmo.portalType == Type.SWAMP) {
                b9 = RuinedPortalPiece.VerticalPlacement.ON_OCEAN_FLOOR;
                a10.airPocket = false;
                a10.mossiness = 0.5f;
                a10.vines = true;
            }
            else if (cmo.portalType == Type.MOUNTAIN) {
                final boolean boolean11 = this.random.nextFloat() < 0.5f;
                b9 = (boolean11 ? RuinedPortalPiece.VerticalPlacement.IN_MOUNTAIN : RuinedPortalPiece.VerticalPlacement.ON_LAND_SURFACE);
                a10.airPocket = (boolean11 || this.random.nextFloat() < 0.5f);
            }
            else if (cmo.portalType == Type.OCEAN) {
                b9 = RuinedPortalPiece.VerticalPlacement.ON_OCEAN_FLOOR;
                a10.airPocket = false;
                a10.mossiness = 0.8f;
            }
            else if (cmo.portalType == Type.NETHER) {
                b9 = RuinedPortalPiece.VerticalPlacement.IN_NETHER;
                a10.airPocket = (this.random.nextFloat() < 0.5f);
                a10.mossiness = 0.0f;
                a10.replaceWithBlackstone = true;
            }
            else {
                final boolean boolean11 = this.random.nextFloat() < 0.5f;
                b9 = (boolean11 ? RuinedPortalPiece.VerticalPlacement.UNDERGROUND : RuinedPortalPiece.VerticalPlacement.ON_LAND_SURFACE);
                a10.airPocket = (boolean11 || this.random.nextFloat() < 0.5f);
            }
            ResourceLocation vk11;
            if (this.random.nextFloat() < 0.05f) {
                vk11 = new ResourceLocation(RuinedPortalFeature.STRUCTURE_LOCATION_GIANT_PORTALS[this.random.nextInt(RuinedPortalFeature.STRUCTURE_LOCATION_GIANT_PORTALS.length)]);
            }
            else {
                vk11 = new ResourceLocation(RuinedPortalFeature.STRUCTURE_LOCATION_PORTALS[this.random.nextInt(RuinedPortalFeature.STRUCTURE_LOCATION_PORTALS.length)]);
            }
            final StructureTemplate csy12 = cst.getOrCreate(vk11);
            final Rotation bzj13 = Util.<Rotation>getRandom(Rotation.values(), this.random);
            final Mirror byd14 = (this.random.nextFloat() < 0.5f) ? Mirror.NONE : Mirror.FRONT_BACK;
            final BlockPos fx15 = new BlockPos(csy12.getSize().getX() / 2, 0, csy12.getSize().getZ() / 2);
            final BlockPos fx16 = new ChunkPos(integer4, integer5).getWorldPosition();
            final BoundingBox cqx17 = csy12.getBoundingBox(fx16, bzj13, fx15, byd14);
            final Vec3i gr18 = cqx17.getCenter();
            final int integer6 = gr18.getX();
            final int integer7 = gr18.getZ();
            final int integer8 = cfv.getBaseHeight(integer6, integer7, RuinedPortalPiece.getHeightMapType(b9)) - 1;
            final int integer9 = findSuitableY(this.random, cfv, b9, a10.airPocket, integer8, cqx17.getYSpan(), cqx17);
            final BlockPos fx17 = new BlockPos(fx16.getX(), integer9, fx16.getZ());
            if (cmo.portalType == Type.MOUNTAIN || cmo.portalType == Type.OCEAN || cmo.portalType == Type.STANDARD) {
                a10.cold = isCold(fx17, bss);
            }
            this.pieces.add(new RuinedPortalPiece(fx17, b9, a10, vk11, csy12, bzj13, byd14, fx15));
            this.calculateBoundingBox();
        }
    }
    
    public enum Type implements StringRepresentable {
        STANDARD("standard"), 
        DESERT("desert"), 
        JUNGLE("jungle"), 
        SWAMP("swamp"), 
        MOUNTAIN("mountain"), 
        OCEAN("ocean"), 
        NETHER("nether");
        
        public static final Codec<Type> CODEC;
        private static final Map<String, Type> BY_NAME;
        private final String name;
        
        private Type(final String string3) {
            this.name = string3;
        }
        
        public String getName() {
            return this.name;
        }
        
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

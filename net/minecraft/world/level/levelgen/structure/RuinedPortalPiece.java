package net.minecraft.world.level.levelgen.structure;

import net.minecraft.world.level.block.state.StateHolder;
import java.util.stream.Collectors;
import java.util.Arrays;
import java.util.Map;
import com.mojang.datafixers.kinds.Applicative;
import com.mojang.datafixers.kinds.App;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.mojang.serialization.Codec;
import org.apache.logging.log4j.LogManager;
import net.minecraft.nbt.Tag;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockMatchTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.AlwaysTrueTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.RandomBlockMatchTest;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.VineBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.core.Direction;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.core.Vec3i;
import net.minecraft.world.level.ChunkPos;
import java.util.Random;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.StructureFeatureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlackstoneReplaceProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.LavaSubmergedBlockProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockAgeProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.ProcessorRule;
import java.util.List;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.block.Blocks;
import com.google.common.collect.Lists;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockIgnoreProcessor;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.Dynamic;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;
import net.minecraft.world.level.levelgen.feature.StructurePieceType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.resources.ResourceLocation;
import org.apache.logging.log4j.Logger;

public class RuinedPortalPiece extends TemplateStructurePiece {
    private static final Logger LOGGER;
    private final ResourceLocation templateLocation;
    private final Rotation rotation;
    private final Mirror mirror;
    private final VerticalPlacement verticalPlacement;
    private final Properties properties;
    
    public RuinedPortalPiece(final BlockPos fx1, final VerticalPlacement b, final Properties a, final ResourceLocation vk, final StructureTemplate csy, final Rotation bzj, final Mirror byd, final BlockPos fx8) {
        super(StructurePieceType.RUINED_PORTAL, 0);
        this.templatePosition = fx1;
        this.templateLocation = vk;
        this.rotation = bzj;
        this.mirror = byd;
        this.verticalPlacement = b;
        this.properties = a;
        this.loadTemplate(csy, fx8);
    }
    
    public RuinedPortalPiece(final StructureManager cst, final CompoundTag md) {
        super(StructurePieceType.RUINED_PORTAL, md);
        this.templateLocation = new ResourceLocation(md.getString("Template"));
        this.rotation = Rotation.valueOf(md.getString("Rotation"));
        this.mirror = Mirror.valueOf(md.getString("Mirror"));
        this.verticalPlacement = VerticalPlacement.byName(md.getString("VerticalPlacement"));
        this.properties = (Properties)Properties.CODEC.parse(new Dynamic((DynamicOps)NbtOps.INSTANCE, md.get("Properties"))).getOrThrow(true, RuinedPortalPiece.LOGGER::error);
        final StructureTemplate csy4 = cst.getOrCreate(this.templateLocation);
        this.loadTemplate(csy4, new BlockPos(csy4.getSize().getX() / 2, 0, csy4.getSize().getZ() / 2));
    }
    
    @Override
    protected void addAdditionalSaveData(final CompoundTag md) {
        super.addAdditionalSaveData(md);
        md.putString("Template", this.templateLocation.toString());
        md.putString("Rotation", this.rotation.name());
        md.putString("Mirror", this.mirror.name());
        md.putString("VerticalPlacement", this.verticalPlacement.getName());
        Properties.CODEC.encodeStart((DynamicOps)NbtOps.INSTANCE, this.properties).resultOrPartial(RuinedPortalPiece.LOGGER::error).ifPresent(mt -> md.put("Properties", mt));
    }
    
    private void loadTemplate(final StructureTemplate csy, final BlockPos fx) {
        final BlockIgnoreProcessor csb4 = this.properties.airPocket ? BlockIgnoreProcessor.STRUCTURE_BLOCK : BlockIgnoreProcessor.STRUCTURE_AND_AIR;
        final List<ProcessorRule> list5 = (List<ProcessorRule>)Lists.newArrayList();
        list5.add(getBlockReplaceRule(Blocks.GOLD_BLOCK, 0.3f, Blocks.AIR));
        list5.add(this.getLavaProcessorRule());
        if (!this.properties.cold) {
            list5.add(getBlockReplaceRule(Blocks.NETHERRACK, 0.07f, Blocks.MAGMA_BLOCK));
        }
        final StructurePlaceSettings csu6 = new StructurePlaceSettings().setRotation(this.rotation).setMirror(this.mirror).setRotationPivot(fx).addProcessor(csb4).addProcessor(new RuleProcessor(list5)).addProcessor(new BlockAgeProcessor(this.properties.mossiness)).addProcessor(new LavaSubmergedBlockProcessor());
        if (this.properties.replaceWithBlackstone) {
            csu6.addProcessor(BlackstoneReplaceProcessor.INSTANCE);
        }
        this.setup(csy, this.templatePosition, csu6);
    }
    
    private ProcessorRule getLavaProcessorRule() {
        if (this.verticalPlacement == VerticalPlacement.ON_OCEAN_FLOOR) {
            return getBlockReplaceRule(Blocks.LAVA, Blocks.MAGMA_BLOCK);
        }
        if (this.properties.cold) {
            return getBlockReplaceRule(Blocks.LAVA, Blocks.NETHERRACK);
        }
        return getBlockReplaceRule(Blocks.LAVA, 0.2f, Blocks.MAGMA_BLOCK);
    }
    
    @Override
    public boolean postProcess(final WorldGenLevel bso, final StructureFeatureManager bsk, final ChunkGenerator cfv, final Random random, final BoundingBox cqx, final ChunkPos bra, final BlockPos fx) {
        if (!cqx.isInside(this.templatePosition)) {
            return true;
        }
        cqx.expand(this.template.getBoundingBox(this.placeSettings, this.templatePosition));
        final boolean boolean9 = super.postProcess(bso, bsk, cfv, random, cqx, bra, fx);
        this.spreadNetherrack(random, bso);
        this.addNetherrackDripColumnsBelowPortal(random, bso);
        if (this.properties.vines || this.properties.overgrown) {
            BlockPos.betweenClosedStream(this.getBoundingBox()).forEach(fx -> {
                if (this.properties.vines) {
                    this.maybeAddVines(random, bso, fx);
                }
                if (this.properties.overgrown) {
                    this.maybeAddLeavesAbove(random, bso, fx);
                }
            });
        }
        return boolean9;
    }
    
    @Override
    protected void handleDataMarker(final String string, final BlockPos fx, final ServerLevelAccessor bsh, final Random random, final BoundingBox cqx) {
    }
    
    private void maybeAddVines(final Random random, final LevelAccessor brv, final BlockPos fx) {
        final BlockState cee5 = brv.getBlockState(fx);
        if (cee5.isAir() || cee5.is(Blocks.VINE)) {
            return;
        }
        final Direction gc6 = Direction.Plane.HORIZONTAL.getRandomDirection(random);
        final BlockPos fx2 = fx.relative(gc6);
        final BlockState cee6 = brv.getBlockState(fx2);
        if (!cee6.isAir()) {
            return;
        }
        if (!Block.isFaceFull(cee5.getCollisionShape(brv, fx), gc6)) {
            return;
        }
        final BooleanProperty cev9 = VineBlock.getPropertyForFace(gc6.getOpposite());
        brv.setBlock(fx2, ((StateHolder<O, BlockState>)Blocks.VINE.defaultBlockState()).<Comparable, Boolean>setValue((Property<Comparable>)cev9, true), 3);
    }
    
    private void maybeAddLeavesAbove(final Random random, final LevelAccessor brv, final BlockPos fx) {
        if (random.nextFloat() < 0.5f && brv.getBlockState(fx).is(Blocks.NETHERRACK) && brv.getBlockState(fx.above()).isAir()) {
            brv.setBlock(fx.above(), ((StateHolder<O, BlockState>)Blocks.JUNGLE_LEAVES.defaultBlockState()).<Comparable, Boolean>setValue((Property<Comparable>)LeavesBlock.PERSISTENT, true), 3);
        }
    }
    
    private void addNetherrackDripColumnsBelowPortal(final Random random, final LevelAccessor brv) {
        for (int integer4 = this.boundingBox.x0 + 1; integer4 < this.boundingBox.x1; ++integer4) {
            for (int integer5 = this.boundingBox.z0 + 1; integer5 < this.boundingBox.z1; ++integer5) {
                final BlockPos fx6 = new BlockPos(integer4, this.boundingBox.y0, integer5);
                if (brv.getBlockState(fx6).is(Blocks.NETHERRACK)) {
                    this.addNetherrackDripColumn(random, brv, fx6.below());
                }
            }
        }
    }
    
    private void addNetherrackDripColumn(final Random random, final LevelAccessor brv, final BlockPos fx) {
        final BlockPos.MutableBlockPos a5 = fx.mutable();
        this.placeNetherrackOrMagma(random, brv, a5);
        int integer6 = 8;
        while (integer6 > 0 && random.nextFloat() < 0.5f) {
            a5.move(Direction.DOWN);
            --integer6;
            this.placeNetherrackOrMagma(random, brv, a5);
        }
    }
    
    private void spreadNetherrack(final Random random, final LevelAccessor brv) {
        final boolean boolean4 = this.verticalPlacement == VerticalPlacement.ON_LAND_SURFACE || this.verticalPlacement == VerticalPlacement.ON_OCEAN_FLOOR;
        final Vec3i gr5 = this.boundingBox.getCenter();
        final int integer6 = gr5.getX();
        final int integer7 = gr5.getZ();
        final float[] arr8 = { 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 0.9f, 0.9f, 0.8f, 0.7f, 0.6f, 0.4f, 0.2f };
        final int integer8 = arr8.length;
        final int integer9 = (this.boundingBox.getXSpan() + this.boundingBox.getZSpan()) / 2;
        final int integer10 = random.nextInt(Math.max(1, 8 - integer9 / 2));
        final int integer11 = 3;
        final BlockPos.MutableBlockPos a13 = BlockPos.ZERO.mutable();
        for (int integer12 = integer6 - integer8; integer12 <= integer6 + integer8; ++integer12) {
            for (int integer13 = integer7 - integer8; integer13 <= integer7 + integer8; ++integer13) {
                final int integer14 = Math.abs(integer12 - integer6) + Math.abs(integer13 - integer7);
                final int integer15 = Math.max(0, integer14 + integer10);
                if (integer15 < integer8) {
                    final float float18 = arr8[integer15];
                    if (random.nextDouble() < float18) {
                        final int integer16 = getSurfaceY(brv, integer12, integer13, this.verticalPlacement);
                        final int integer17 = boolean4 ? integer16 : Math.min(this.boundingBox.y0, integer16);
                        a13.set(integer12, integer17, integer13);
                        if (Math.abs(integer17 - this.boundingBox.y0) <= 3 && this.canBlockBeReplacedByNetherrackOrMagma(brv, a13)) {
                            this.placeNetherrackOrMagma(random, brv, a13);
                            if (this.properties.overgrown) {
                                this.maybeAddLeavesAbove(random, brv, a13);
                            }
                            this.addNetherrackDripColumn(random, brv, a13.below());
                        }
                    }
                }
            }
        }
    }
    
    private boolean canBlockBeReplacedByNetherrackOrMagma(final LevelAccessor brv, final BlockPos fx) {
        final BlockState cee4 = brv.getBlockState(fx);
        return !cee4.is(Blocks.AIR) && !cee4.is(Blocks.OBSIDIAN) && !cee4.is(Blocks.CHEST) && (this.verticalPlacement == VerticalPlacement.IN_NETHER || !cee4.is(Blocks.LAVA));
    }
    
    private void placeNetherrackOrMagma(final Random random, final LevelAccessor brv, final BlockPos fx) {
        if (!this.properties.cold && random.nextFloat() < 0.07f) {
            brv.setBlock(fx, Blocks.MAGMA_BLOCK.defaultBlockState(), 3);
        }
        else {
            brv.setBlock(fx, Blocks.NETHERRACK.defaultBlockState(), 3);
        }
    }
    
    private static int getSurfaceY(final LevelAccessor brv, final int integer2, final int integer3, final VerticalPlacement b) {
        return brv.getHeight(getHeightMapType(b), integer2, integer3) - 1;
    }
    
    public static Heightmap.Types getHeightMapType(final VerticalPlacement b) {
        return (b == VerticalPlacement.ON_OCEAN_FLOOR) ? Heightmap.Types.OCEAN_FLOOR_WG : Heightmap.Types.WORLD_SURFACE_WG;
    }
    
    private static ProcessorRule getBlockReplaceRule(final Block bul1, final float float2, final Block bul3) {
        return new ProcessorRule(new RandomBlockMatchTest(bul1, float2), AlwaysTrueTest.INSTANCE, bul3.defaultBlockState());
    }
    
    private static ProcessorRule getBlockReplaceRule(final Block bul1, final Block bul2) {
        return new ProcessorRule(new BlockMatchTest(bul1), AlwaysTrueTest.INSTANCE, bul2.defaultBlockState());
    }
    
    static {
        LOGGER = LogManager.getLogger();
    }
    
    public static class Properties {
        public static final Codec<Properties> CODEC;
        public boolean cold;
        public float mossiness;
        public boolean airPocket;
        public boolean overgrown;
        public boolean vines;
        public boolean replaceWithBlackstone;
        
        public Properties() {
            this.mossiness = 0.2f;
        }
        
        public <T> Properties(final boolean boolean1, final float float2, final boolean boolean3, final boolean boolean4, final boolean boolean5, final boolean boolean6) {
            this.mossiness = 0.2f;
            this.cold = boolean1;
            this.mossiness = float2;
            this.airPocket = boolean3;
            this.overgrown = boolean4;
            this.vines = boolean5;
            this.replaceWithBlackstone = boolean6;
        }
        
        static {
            CODEC = RecordCodecBuilder.create(instance -> instance.group((App)Codec.BOOL.fieldOf("cold").forGetter(a -> a.cold), (App)Codec.FLOAT.fieldOf("mossiness").forGetter(a -> a.mossiness), (App)Codec.BOOL.fieldOf("air_pocket").forGetter(a -> a.airPocket), (App)Codec.BOOL.fieldOf("overgrown").forGetter(a -> a.overgrown), (App)Codec.BOOL.fieldOf("vines").forGetter(a -> a.vines), (App)Codec.BOOL.fieldOf("replace_with_blackstone").forGetter(a -> a.replaceWithBlackstone)).apply((Applicative)instance, Properties::new));
        }
    }
    
    public enum VerticalPlacement {
        ON_LAND_SURFACE("on_land_surface"), 
        PARTLY_BURIED("partly_buried"), 
        ON_OCEAN_FLOOR("on_ocean_floor"), 
        IN_MOUNTAIN("in_mountain"), 
        UNDERGROUND("underground"), 
        IN_NETHER("in_nether");
        
        private static final Map<String, VerticalPlacement> BY_NAME;
        private final String name;
        
        private VerticalPlacement(final String string3) {
            this.name = string3;
        }
        
        public String getName() {
            return this.name;
        }
        
        public static VerticalPlacement byName(final String string) {
            return (VerticalPlacement)VerticalPlacement.BY_NAME.get(string);
        }
        
        static {
            BY_NAME = (Map)Arrays.stream((Object[])values()).collect(Collectors.toMap(VerticalPlacement::getName, b -> b));
        }
    }
}

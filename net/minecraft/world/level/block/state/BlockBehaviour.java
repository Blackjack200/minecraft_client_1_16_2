package net.minecraft.world.level.block.state;

import java.util.Arrays;
import net.minecraft.world.level.EmptyBlockGetter;
import net.minecraft.world.level.block.SupportType;
import java.util.function.Predicate;
import net.minecraft.world.phys.Vec3;
import com.mojang.serialization.MapCodec;
import net.minecraft.world.level.block.state.properties.Property;
import com.google.common.collect.ImmutableMap;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.entity.EntityType;
import java.util.function.ToIntFunction;
import java.util.function.Function;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.core.Registry;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.entity.Entity;
import java.util.Random;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.core.Vec3i;
import net.minecraft.util.Mth;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import java.util.Collections;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.item.ItemStack;
import java.util.List;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.network.protocol.game.DebugPackets;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.tags.Tag;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelAccessor;
import javax.annotation.Nullable;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.Material;
import net.minecraft.core.Direction;

public abstract class BlockBehaviour {
    protected static final Direction[] UPDATE_SHAPE_ORDER;
    protected final Material material;
    protected final boolean hasCollision;
    protected final float explosionResistance;
    protected final boolean isRandomlyTicking;
    protected final SoundType soundType;
    protected final float friction;
    protected final float speedFactor;
    protected final float jumpFactor;
    protected final boolean dynamicShape;
    protected final Properties properties;
    @Nullable
    protected ResourceLocation drops;
    
    public BlockBehaviour(final Properties c) {
        this.material = c.material;
        this.hasCollision = c.hasCollision;
        this.drops = c.drops;
        this.explosionResistance = c.explosionResistance;
        this.isRandomlyTicking = c.isRandomlyTicking;
        this.soundType = c.soundType;
        this.friction = c.friction;
        this.speedFactor = c.speedFactor;
        this.jumpFactor = c.jumpFactor;
        this.dynamicShape = c.dynamicShape;
        this.properties = c;
    }
    
    @Deprecated
    public void updateIndirectNeighbourShapes(final BlockState cee, final LevelAccessor brv, final BlockPos fx, final int integer4, final int integer5) {
    }
    
    @Deprecated
    public boolean isPathfindable(final BlockState cee, final BlockGetter bqz, final BlockPos fx, final PathComputationType cxb) {
        switch (cxb) {
            case LAND: {
                return !cee.isCollisionShapeFullBlock(bqz, fx);
            }
            case WATER: {
                return bqz.getFluidState(fx).is(FluidTags.WATER);
            }
            case AIR: {
                return !cee.isCollisionShapeFullBlock(bqz, fx);
            }
            default: {
                return false;
            }
        }
    }
    
    @Deprecated
    public BlockState updateShape(final BlockState cee1, final Direction gc, final BlockState cee3, final LevelAccessor brv, final BlockPos fx5, final BlockPos fx6) {
        return cee1;
    }
    
    @Deprecated
    public boolean skipRendering(final BlockState cee1, final BlockState cee2, final Direction gc) {
        return false;
    }
    
    @Deprecated
    public void neighborChanged(final BlockState cee, final Level bru, final BlockPos fx3, final Block bul, final BlockPos fx5, final boolean boolean6) {
        DebugPackets.sendNeighborsUpdatePacket(bru, fx3);
    }
    
    @Deprecated
    public void onPlace(final BlockState cee1, final Level bru, final BlockPos fx, final BlockState cee4, final boolean boolean5) {
    }
    
    @Deprecated
    public void onRemove(final BlockState cee1, final Level bru, final BlockPos fx, final BlockState cee4, final boolean boolean5) {
        if (this.isEntityBlock() && !cee1.is(cee4.getBlock())) {
            bru.removeBlockEntity(fx);
        }
    }
    
    @Deprecated
    public InteractionResult use(final BlockState cee, final Level bru, final BlockPos fx, final Player bft, final InteractionHand aoq, final BlockHitResult dcg) {
        return InteractionResult.PASS;
    }
    
    @Deprecated
    public boolean triggerEvent(final BlockState cee, final Level bru, final BlockPos fx, final int integer4, final int integer5) {
        return false;
    }
    
    @Deprecated
    public RenderShape getRenderShape(final BlockState cee) {
        return RenderShape.MODEL;
    }
    
    @Deprecated
    public boolean useShapeForLightOcclusion(final BlockState cee) {
        return false;
    }
    
    @Deprecated
    public boolean isSignalSource(final BlockState cee) {
        return false;
    }
    
    @Deprecated
    public PushReaction getPistonPushReaction(final BlockState cee) {
        return this.material.getPushReaction();
    }
    
    @Deprecated
    public FluidState getFluidState(final BlockState cee) {
        return Fluids.EMPTY.defaultFluidState();
    }
    
    @Deprecated
    public boolean hasAnalogOutputSignal(final BlockState cee) {
        return false;
    }
    
    public OffsetType getOffsetType() {
        return OffsetType.NONE;
    }
    
    @Deprecated
    public BlockState rotate(final BlockState cee, final Rotation bzj) {
        return cee;
    }
    
    @Deprecated
    public BlockState mirror(final BlockState cee, final Mirror byd) {
        return cee;
    }
    
    @Deprecated
    public boolean canBeReplaced(final BlockState cee, final BlockPlaceContext bnv) {
        return this.material.isReplaceable() && (bnv.getItemInHand().isEmpty() || bnv.getItemInHand().getItem() != this.asItem());
    }
    
    @Deprecated
    public boolean canBeReplaced(final BlockState cee, final Fluid cut) {
        return this.material.isReplaceable() || !this.material.isSolid();
    }
    
    @Deprecated
    public List<ItemStack> getDrops(final BlockState cee, final LootContext.Builder a) {
        final ResourceLocation vk4 = this.getLootTable();
        if (vk4 == BuiltInLootTables.EMPTY) {
            return (List<ItemStack>)Collections.emptyList();
        }
        final LootContext cys5 = a.<BlockState>withParameter(LootContextParams.BLOCK_STATE, cee).create(LootContextParamSets.BLOCK);
        final ServerLevel aag6 = cys5.getLevel();
        final LootTable cyv7 = aag6.getServer().getLootTables().get(vk4);
        return cyv7.getRandomItems(cys5);
    }
    
    @Deprecated
    public long getSeed(final BlockState cee, final BlockPos fx) {
        return Mth.getSeed(fx);
    }
    
    @Deprecated
    public VoxelShape getOcclusionShape(final BlockState cee, final BlockGetter bqz, final BlockPos fx) {
        return cee.getShape(bqz, fx);
    }
    
    @Deprecated
    public VoxelShape getBlockSupportShape(final BlockState cee, final BlockGetter bqz, final BlockPos fx) {
        return this.getCollisionShape(cee, bqz, fx, CollisionContext.empty());
    }
    
    @Deprecated
    public VoxelShape getInteractionShape(final BlockState cee, final BlockGetter bqz, final BlockPos fx) {
        return Shapes.empty();
    }
    
    @Deprecated
    public int getLightBlock(final BlockState cee, final BlockGetter bqz, final BlockPos fx) {
        if (cee.isSolidRender(bqz, fx)) {
            return bqz.getMaxLightLevel();
        }
        return cee.propagatesSkylightDown(bqz, fx) ? 0 : 1;
    }
    
    @Nullable
    @Deprecated
    public MenuProvider getMenuProvider(final BlockState cee, final Level bru, final BlockPos fx) {
        return null;
    }
    
    @Deprecated
    public boolean canSurvive(final BlockState cee, final LevelReader brw, final BlockPos fx) {
        return true;
    }
    
    @Deprecated
    public float getShadeBrightness(final BlockState cee, final BlockGetter bqz, final BlockPos fx) {
        return cee.isCollisionShapeFullBlock(bqz, fx) ? 0.2f : 1.0f;
    }
    
    @Deprecated
    public int getAnalogOutputSignal(final BlockState cee, final Level bru, final BlockPos fx) {
        return 0;
    }
    
    @Deprecated
    public VoxelShape getShape(final BlockState cee, final BlockGetter bqz, final BlockPos fx, final CollisionContext dcp) {
        return Shapes.block();
    }
    
    @Deprecated
    public VoxelShape getCollisionShape(final BlockState cee, final BlockGetter bqz, final BlockPos fx, final CollisionContext dcp) {
        return this.hasCollision ? cee.getShape(bqz, fx) : Shapes.empty();
    }
    
    @Deprecated
    public VoxelShape getVisualShape(final BlockState cee, final BlockGetter bqz, final BlockPos fx, final CollisionContext dcp) {
        return this.getCollisionShape(cee, bqz, fx, dcp);
    }
    
    @Deprecated
    public void randomTick(final BlockState cee, final ServerLevel aag, final BlockPos fx, final Random random) {
        this.tick(cee, aag, fx, random);
    }
    
    @Deprecated
    public void tick(final BlockState cee, final ServerLevel aag, final BlockPos fx, final Random random) {
    }
    
    @Deprecated
    public float getDestroyProgress(final BlockState cee, final Player bft, final BlockGetter bqz, final BlockPos fx) {
        final float float6 = cee.getDestroySpeed(bqz, fx);
        if (float6 == -1.0f) {
            return 0.0f;
        }
        final int integer7 = bft.hasCorrectToolForDrops(cee) ? 30 : 100;
        return bft.getDestroySpeed(cee) / float6 / integer7;
    }
    
    @Deprecated
    public void spawnAfterBreak(final BlockState cee, final ServerLevel aag, final BlockPos fx, final ItemStack bly) {
    }
    
    @Deprecated
    public void attack(final BlockState cee, final Level bru, final BlockPos fx, final Player bft) {
    }
    
    @Deprecated
    public int getSignal(final BlockState cee, final BlockGetter bqz, final BlockPos fx, final Direction gc) {
        return 0;
    }
    
    @Deprecated
    public void entityInside(final BlockState cee, final Level bru, final BlockPos fx, final Entity apx) {
    }
    
    @Deprecated
    public int getDirectSignal(final BlockState cee, final BlockGetter bqz, final BlockPos fx, final Direction gc) {
        return 0;
    }
    
    public final boolean isEntityBlock() {
        return this instanceof EntityBlock;
    }
    
    public final ResourceLocation getLootTable() {
        if (this.drops == null) {
            final ResourceLocation vk2 = Registry.BLOCK.getKey(this.asBlock());
            this.drops = new ResourceLocation(vk2.getNamespace(), "blocks/" + vk2.getPath());
        }
        return this.drops;
    }
    
    @Deprecated
    public void onProjectileHit(final Level bru, final BlockState cee, final BlockHitResult dcg, final Projectile bgj) {
    }
    
    public abstract Item asItem();
    
    protected abstract Block asBlock();
    
    public MaterialColor defaultMaterialColor() {
        return (MaterialColor)this.properties.materialColor.apply(this.asBlock().defaultBlockState());
    }
    
    static {
        UPDATE_SHAPE_ORDER = new Direction[] { Direction.WEST, Direction.EAST, Direction.NORTH, Direction.SOUTH, Direction.DOWN, Direction.UP };
    }
    
    public enum OffsetType {
        NONE, 
        XZ, 
        XYZ;
    }
    
    public static class Properties {
        private Material material;
        private Function<BlockState, MaterialColor> materialColor;
        private boolean hasCollision;
        private SoundType soundType;
        private ToIntFunction<BlockState> lightEmission;
        private float explosionResistance;
        private float destroyTime;
        private boolean requiresCorrectToolForDrops;
        private boolean isRandomlyTicking;
        private float friction;
        private float speedFactor;
        private float jumpFactor;
        private ResourceLocation drops;
        private boolean canOcclude;
        private boolean isAir;
        private StateArgumentPredicate<EntityType<?>> isValidSpawn;
        private StatePredicate isRedstoneConductor;
        private StatePredicate isSuffocating;
        private StatePredicate isViewBlocking;
        private StatePredicate hasPostProcess;
        private StatePredicate emissiveRendering;
        private boolean dynamicShape;
        
        private Properties(final Material cux, final MaterialColor cuy) {
            this(cux, (Function<BlockState, MaterialColor>)(cee -> cuy));
        }
        
        private Properties(final Material cux, final Function<BlockState, MaterialColor> function) {
            this.hasCollision = true;
            this.soundType = SoundType.STONE;
            this.lightEmission = (ToIntFunction<BlockState>)(cee -> 0);
            this.friction = 0.6f;
            this.speedFactor = 1.0f;
            this.jumpFactor = 1.0f;
            this.canOcclude = true;
            this.isValidSpawn = ((cee, bqz, fx, aqb) -> cee.isFaceSturdy(bqz, fx, Direction.UP) && cee.getLightEmission() < 14);
            this.isRedstoneConductor = ((cee, bqz, fx) -> cee.getMaterial().isSolidBlocking() && cee.isCollisionShapeFullBlock(bqz, fx));
            this.isSuffocating = ((cee, bqz, fx) -> this.material.blocksMotion() && cee.isCollisionShapeFullBlock(bqz, fx));
            this.isViewBlocking = this.isSuffocating;
            this.hasPostProcess = ((cee, bqz, fx) -> false);
            this.emissiveRendering = ((cee, bqz, fx) -> false);
            this.material = cux;
            this.materialColor = function;
        }
        
        public static Properties of(final Material cux) {
            return of(cux, cux.getColor());
        }
        
        public static Properties of(final Material cux, final DyeColor bku) {
            return of(cux, bku.getMaterialColor());
        }
        
        public static Properties of(final Material cux, final MaterialColor cuy) {
            return new Properties(cux, cuy);
        }
        
        public static Properties of(final Material cux, final Function<BlockState, MaterialColor> function) {
            return new Properties(cux, function);
        }
        
        public static Properties copy(final BlockBehaviour ced) {
            final Properties c2 = new Properties(ced.material, ced.properties.materialColor);
            c2.material = ced.properties.material;
            c2.destroyTime = ced.properties.destroyTime;
            c2.explosionResistance = ced.properties.explosionResistance;
            c2.hasCollision = ced.properties.hasCollision;
            c2.isRandomlyTicking = ced.properties.isRandomlyTicking;
            c2.lightEmission = ced.properties.lightEmission;
            c2.materialColor = ced.properties.materialColor;
            c2.soundType = ced.properties.soundType;
            c2.friction = ced.properties.friction;
            c2.speedFactor = ced.properties.speedFactor;
            c2.dynamicShape = ced.properties.dynamicShape;
            c2.canOcclude = ced.properties.canOcclude;
            c2.isAir = ced.properties.isAir;
            c2.requiresCorrectToolForDrops = ced.properties.requiresCorrectToolForDrops;
            return c2;
        }
        
        public Properties noCollission() {
            this.hasCollision = false;
            this.canOcclude = false;
            return this;
        }
        
        public Properties noOcclusion() {
            this.canOcclude = false;
            return this;
        }
        
        public Properties friction(final float float1) {
            this.friction = float1;
            return this;
        }
        
        public Properties speedFactor(final float float1) {
            this.speedFactor = float1;
            return this;
        }
        
        public Properties jumpFactor(final float float1) {
            this.jumpFactor = float1;
            return this;
        }
        
        public Properties sound(final SoundType cab) {
            this.soundType = cab;
            return this;
        }
        
        public Properties lightLevel(final ToIntFunction<BlockState> toIntFunction) {
            this.lightEmission = toIntFunction;
            return this;
        }
        
        public Properties strength(final float float1, final float float2) {
            this.destroyTime = float1;
            this.explosionResistance = Math.max(0.0f, float2);
            return this;
        }
        
        public Properties instabreak() {
            return this.strength(0.0f);
        }
        
        public Properties strength(final float float1) {
            this.strength(float1, float1);
            return this;
        }
        
        public Properties randomTicks() {
            this.isRandomlyTicking = true;
            return this;
        }
        
        public Properties dynamicShape() {
            this.dynamicShape = true;
            return this;
        }
        
        public Properties noDrops() {
            this.drops = BuiltInLootTables.EMPTY;
            return this;
        }
        
        public Properties dropsLike(final Block bul) {
            this.drops = bul.getLootTable();
            return this;
        }
        
        public Properties air() {
            this.isAir = true;
            return this;
        }
        
        public Properties isValidSpawn(final StateArgumentPredicate<EntityType<?>> d) {
            this.isValidSpawn = d;
            return this;
        }
        
        public Properties isRedstoneConductor(final StatePredicate e) {
            this.isRedstoneConductor = e;
            return this;
        }
        
        public Properties isSuffocating(final StatePredicate e) {
            this.isSuffocating = e;
            return this;
        }
        
        public Properties isViewBlocking(final StatePredicate e) {
            this.isViewBlocking = e;
            return this;
        }
        
        public Properties hasPostProcess(final StatePredicate e) {
            this.hasPostProcess = e;
            return this;
        }
        
        public Properties emissiveRendering(final StatePredicate e) {
            this.emissiveRendering = e;
            return this;
        }
        
        public Properties requiresCorrectToolForDrops() {
            this.requiresCorrectToolForDrops = true;
            return this;
        }
    }
    
    public abstract static class BlockStateBase extends StateHolder<Block, BlockState> {
        private final int lightEmission;
        private final boolean useShapeForLightOcclusion;
        private final boolean isAir;
        private final Material material;
        private final MaterialColor materialColor;
        private final float destroySpeed;
        private final boolean requiresCorrectToolForDrops;
        private final boolean canOcclude;
        private final StatePredicate isRedstoneConductor;
        private final StatePredicate isSuffocating;
        private final StatePredicate isViewBlocking;
        private final StatePredicate hasPostProcess;
        private final StatePredicate emissiveRendering;
        @Nullable
        protected Cache cache;
        
        protected BlockStateBase(final Block bul, final ImmutableMap<Property<?>, Comparable<?>> immutableMap, final MapCodec<BlockState> mapCodec) {
            super(bul, immutableMap, mapCodec);
            final Properties c5 = bul.properties;
            this.lightEmission = c5.lightEmission.applyAsInt(this.asState());
            this.useShapeForLightOcclusion = bul.useShapeForLightOcclusion(this.asState());
            this.isAir = c5.isAir;
            this.material = c5.material;
            this.materialColor = (MaterialColor)c5.materialColor.apply(this.asState());
            this.destroySpeed = c5.destroyTime;
            this.requiresCorrectToolForDrops = c5.requiresCorrectToolForDrops;
            this.canOcclude = c5.canOcclude;
            this.isRedstoneConductor = c5.isRedstoneConductor;
            this.isSuffocating = c5.isSuffocating;
            this.isViewBlocking = c5.isViewBlocking;
            this.hasPostProcess = c5.hasPostProcess;
            this.emissiveRendering = c5.emissiveRendering;
        }
        
        public void initCache() {
            if (!this.getBlock().hasDynamicShape()) {
                this.cache = new Cache(this.asState());
            }
        }
        
        public Block getBlock() {
            return (Block)this.owner;
        }
        
        public Material getMaterial() {
            return this.material;
        }
        
        public boolean isValidSpawn(final BlockGetter bqz, final BlockPos fx, final EntityType<?> aqb) {
            return this.getBlock().properties.isValidSpawn.test(this.asState(), bqz, fx, aqb);
        }
        
        public boolean propagatesSkylightDown(final BlockGetter bqz, final BlockPos fx) {
            if (this.cache != null) {
                return this.cache.propagatesSkylightDown;
            }
            return this.getBlock().propagatesSkylightDown(this.asState(), bqz, fx);
        }
        
        public int getLightBlock(final BlockGetter bqz, final BlockPos fx) {
            if (this.cache != null) {
                return this.cache.lightBlock;
            }
            return this.getBlock().getLightBlock(this.asState(), bqz, fx);
        }
        
        public VoxelShape getFaceOcclusionShape(final BlockGetter bqz, final BlockPos fx, final Direction gc) {
            if (this.cache != null && this.cache.occlusionShapes != null) {
                return this.cache.occlusionShapes[gc.ordinal()];
            }
            return Shapes.getFaceShape(this.getOcclusionShape(bqz, fx), gc);
        }
        
        public VoxelShape getOcclusionShape(final BlockGetter bqz, final BlockPos fx) {
            return this.getBlock().getOcclusionShape(this.asState(), bqz, fx);
        }
        
        public boolean hasLargeCollisionShape() {
            return this.cache == null || this.cache.largeCollisionShape;
        }
        
        public boolean useShapeForLightOcclusion() {
            return this.useShapeForLightOcclusion;
        }
        
        public int getLightEmission() {
            return this.lightEmission;
        }
        
        public boolean isAir() {
            return this.isAir;
        }
        
        public MaterialColor getMapColor(final BlockGetter bqz, final BlockPos fx) {
            return this.materialColor;
        }
        
        public BlockState rotate(final Rotation bzj) {
            return this.getBlock().rotate(this.asState(), bzj);
        }
        
        public BlockState mirror(final Mirror byd) {
            return this.getBlock().mirror(this.asState(), byd);
        }
        
        public RenderShape getRenderShape() {
            return this.getBlock().getRenderShape(this.asState());
        }
        
        public boolean emissiveRendering(final BlockGetter bqz, final BlockPos fx) {
            return this.emissiveRendering.test(this.asState(), bqz, fx);
        }
        
        public float getShadeBrightness(final BlockGetter bqz, final BlockPos fx) {
            return this.getBlock().getShadeBrightness(this.asState(), bqz, fx);
        }
        
        public boolean isRedstoneConductor(final BlockGetter bqz, final BlockPos fx) {
            return this.isRedstoneConductor.test(this.asState(), bqz, fx);
        }
        
        public boolean isSignalSource() {
            return this.getBlock().isSignalSource(this.asState());
        }
        
        public int getSignal(final BlockGetter bqz, final BlockPos fx, final Direction gc) {
            return this.getBlock().getSignal(this.asState(), bqz, fx, gc);
        }
        
        public boolean hasAnalogOutputSignal() {
            return this.getBlock().hasAnalogOutputSignal(this.asState());
        }
        
        public int getAnalogOutputSignal(final Level bru, final BlockPos fx) {
            return this.getBlock().getAnalogOutputSignal(this.asState(), bru, fx);
        }
        
        public float getDestroySpeed(final BlockGetter bqz, final BlockPos fx) {
            return this.destroySpeed;
        }
        
        public float getDestroyProgress(final Player bft, final BlockGetter bqz, final BlockPos fx) {
            return this.getBlock().getDestroyProgress(this.asState(), bft, bqz, fx);
        }
        
        public int getDirectSignal(final BlockGetter bqz, final BlockPos fx, final Direction gc) {
            return this.getBlock().getDirectSignal(this.asState(), bqz, fx, gc);
        }
        
        public PushReaction getPistonPushReaction() {
            return this.getBlock().getPistonPushReaction(this.asState());
        }
        
        public boolean isSolidRender(final BlockGetter bqz, final BlockPos fx) {
            if (this.cache != null) {
                return this.cache.solidRender;
            }
            final BlockState cee4 = this.asState();
            return cee4.canOcclude() && Block.isShapeFullBlock(cee4.getOcclusionShape(bqz, fx));
        }
        
        public boolean canOcclude() {
            return this.canOcclude;
        }
        
        public boolean skipRendering(final BlockState cee, final Direction gc) {
            return this.getBlock().skipRendering(this.asState(), cee, gc);
        }
        
        public VoxelShape getShape(final BlockGetter bqz, final BlockPos fx) {
            return this.getShape(bqz, fx, CollisionContext.empty());
        }
        
        public VoxelShape getShape(final BlockGetter bqz, final BlockPos fx, final CollisionContext dcp) {
            return this.getBlock().getShape(this.asState(), bqz, fx, dcp);
        }
        
        public VoxelShape getCollisionShape(final BlockGetter bqz, final BlockPos fx) {
            if (this.cache != null) {
                return this.cache.collisionShape;
            }
            return this.getCollisionShape(bqz, fx, CollisionContext.empty());
        }
        
        public VoxelShape getCollisionShape(final BlockGetter bqz, final BlockPos fx, final CollisionContext dcp) {
            return this.getBlock().getCollisionShape(this.asState(), bqz, fx, dcp);
        }
        
        public VoxelShape getBlockSupportShape(final BlockGetter bqz, final BlockPos fx) {
            return this.getBlock().getBlockSupportShape(this.asState(), bqz, fx);
        }
        
        public VoxelShape getVisualShape(final BlockGetter bqz, final BlockPos fx, final CollisionContext dcp) {
            return this.getBlock().getVisualShape(this.asState(), bqz, fx, dcp);
        }
        
        public VoxelShape getInteractionShape(final BlockGetter bqz, final BlockPos fx) {
            return this.getBlock().getInteractionShape(this.asState(), bqz, fx);
        }
        
        public final boolean entityCanStandOn(final BlockGetter bqz, final BlockPos fx, final Entity apx) {
            return this.entityCanStandOnFace(bqz, fx, apx, Direction.UP);
        }
        
        public final boolean entityCanStandOnFace(final BlockGetter bqz, final BlockPos fx, final Entity apx, final Direction gc) {
            return Block.isFaceFull(this.getCollisionShape(bqz, fx, CollisionContext.of(apx)), gc);
        }
        
        public Vec3 getOffset(final BlockGetter bqz, final BlockPos fx) {
            final OffsetType b4 = this.getBlock().getOffsetType();
            if (b4 == OffsetType.NONE) {
                return Vec3.ZERO;
            }
            final long long5 = Mth.getSeed(fx.getX(), 0, fx.getZ());
            return new Vec3(((long5 & 0xFL) / 15.0f - 0.5) * 0.5, (b4 == OffsetType.XYZ) ? (((long5 >> 4 & 0xFL) / 15.0f - 1.0) * 0.2) : 0.0, ((long5 >> 8 & 0xFL) / 15.0f - 0.5) * 0.5);
        }
        
        public boolean triggerEvent(final Level bru, final BlockPos fx, final int integer3, final int integer4) {
            return this.getBlock().triggerEvent(this.asState(), bru, fx, integer3, integer4);
        }
        
        public void neighborChanged(final Level bru, final BlockPos fx2, final Block bul, final BlockPos fx4, final boolean boolean5) {
            this.getBlock().neighborChanged(this.asState(), bru, fx2, bul, fx4, boolean5);
        }
        
        public final void updateNeighbourShapes(final LevelAccessor brv, final BlockPos fx, final int integer) {
            this.updateNeighbourShapes(brv, fx, integer, 512);
        }
        
        public final void updateNeighbourShapes(final LevelAccessor brv, final BlockPos fx, final int integer3, final int integer4) {
            this.getBlock();
            final BlockPos.MutableBlockPos a6 = new BlockPos.MutableBlockPos();
            for (final Direction gc10 : BlockBehaviour.UPDATE_SHAPE_ORDER) {
                a6.setWithOffset(fx, gc10);
                final BlockState cee11 = brv.getBlockState(a6);
                final BlockState cee12 = cee11.updateShape(gc10.getOpposite(), this.asState(), brv, a6, fx);
                Block.updateOrDestroy(cee11, cee12, brv, a6, integer3, integer4);
            }
        }
        
        public final void updateIndirectNeighbourShapes(final LevelAccessor brv, final BlockPos fx, final int integer) {
            this.updateIndirectNeighbourShapes(brv, fx, integer, 512);
        }
        
        public void updateIndirectNeighbourShapes(final LevelAccessor brv, final BlockPos fx, final int integer3, final int integer4) {
            this.getBlock().updateIndirectNeighbourShapes(this.asState(), brv, fx, integer3, integer4);
        }
        
        public void onPlace(final Level bru, final BlockPos fx, final BlockState cee, final boolean boolean4) {
            this.getBlock().onPlace(this.asState(), bru, fx, cee, boolean4);
        }
        
        public void onRemove(final Level bru, final BlockPos fx, final BlockState cee, final boolean boolean4) {
            this.getBlock().onRemove(this.asState(), bru, fx, cee, boolean4);
        }
        
        public void tick(final ServerLevel aag, final BlockPos fx, final Random random) {
            this.getBlock().tick(this.asState(), aag, fx, random);
        }
        
        public void randomTick(final ServerLevel aag, final BlockPos fx, final Random random) {
            this.getBlock().randomTick(this.asState(), aag, fx, random);
        }
        
        public void entityInside(final Level bru, final BlockPos fx, final Entity apx) {
            this.getBlock().entityInside(this.asState(), bru, fx, apx);
        }
        
        public void spawnAfterBreak(final ServerLevel aag, final BlockPos fx, final ItemStack bly) {
            this.getBlock().spawnAfterBreak(this.asState(), aag, fx, bly);
        }
        
        public List<ItemStack> getDrops(final LootContext.Builder a) {
            return this.getBlock().getDrops(this.asState(), a);
        }
        
        public InteractionResult use(final Level bru, final Player bft, final InteractionHand aoq, final BlockHitResult dcg) {
            return this.getBlock().use(this.asState(), bru, dcg.getBlockPos(), bft, aoq, dcg);
        }
        
        public void attack(final Level bru, final BlockPos fx, final Player bft) {
            this.getBlock().attack(this.asState(), bru, fx, bft);
        }
        
        public boolean isSuffocating(final BlockGetter bqz, final BlockPos fx) {
            return this.isSuffocating.test(this.asState(), bqz, fx);
        }
        
        public boolean isViewBlocking(final BlockGetter bqz, final BlockPos fx) {
            return this.isViewBlocking.test(this.asState(), bqz, fx);
        }
        
        public BlockState updateShape(final Direction gc, final BlockState cee, final LevelAccessor brv, final BlockPos fx4, final BlockPos fx5) {
            return this.getBlock().updateShape(this.asState(), gc, cee, brv, fx4, fx5);
        }
        
        public boolean isPathfindable(final BlockGetter bqz, final BlockPos fx, final PathComputationType cxb) {
            return this.getBlock().isPathfindable(this.asState(), bqz, fx, cxb);
        }
        
        public boolean canBeReplaced(final BlockPlaceContext bnv) {
            return this.getBlock().canBeReplaced(this.asState(), bnv);
        }
        
        public boolean canBeReplaced(final Fluid cut) {
            return this.getBlock().canBeReplaced(this.asState(), cut);
        }
        
        public boolean canSurvive(final LevelReader brw, final BlockPos fx) {
            return this.getBlock().canSurvive(this.asState(), brw, fx);
        }
        
        public boolean hasPostProcess(final BlockGetter bqz, final BlockPos fx) {
            return this.hasPostProcess.test(this.asState(), bqz, fx);
        }
        
        @Nullable
        public MenuProvider getMenuProvider(final Level bru, final BlockPos fx) {
            return this.getBlock().getMenuProvider(this.asState(), bru, fx);
        }
        
        public boolean is(final Tag<Block> aej) {
            return this.getBlock().is(aej);
        }
        
        public boolean is(final Tag<Block> aej, final Predicate<BlockStateBase> predicate) {
            return this.getBlock().is(aej) && predicate.test(this);
        }
        
        public boolean is(final Block bul) {
            return this.getBlock().is(bul);
        }
        
        public FluidState getFluidState() {
            return this.getBlock().getFluidState(this.asState());
        }
        
        public boolean isRandomlyTicking() {
            return this.getBlock().isRandomlyTicking(this.asState());
        }
        
        public long getSeed(final BlockPos fx) {
            return this.getBlock().getSeed(this.asState(), fx);
        }
        
        public SoundType getSoundType() {
            return this.getBlock().getSoundType(this.asState());
        }
        
        public void onProjectileHit(final Level bru, final BlockState cee, final BlockHitResult dcg, final Projectile bgj) {
            this.getBlock().onProjectileHit(bru, cee, dcg, bgj);
        }
        
        public boolean isFaceSturdy(final BlockGetter bqz, final BlockPos fx, final Direction gc) {
            return this.isFaceSturdy(bqz, fx, gc, SupportType.FULL);
        }
        
        public boolean isFaceSturdy(final BlockGetter bqz, final BlockPos fx, final Direction gc, final SupportType caq) {
            if (this.cache != null) {
                return this.cache.isFaceSturdy(gc, caq);
            }
            return caq.isSupporting(this.asState(), bqz, fx, gc);
        }
        
        public boolean isCollisionShapeFullBlock(final BlockGetter bqz, final BlockPos fx) {
            if (this.cache != null) {
                return this.cache.isCollisionShapeFullBlock;
            }
            return Block.isShapeFullBlock(this.getCollisionShape(bqz, fx));
        }
        
        protected abstract BlockState asState();
        
        public boolean requiresCorrectToolForDrops() {
            return this.requiresCorrectToolForDrops;
        }
        
        static final class Cache {
            private static final Direction[] DIRECTIONS;
            private static final int SUPPORT_TYPE_COUNT;
            protected final boolean solidRender;
            private final boolean propagatesSkylightDown;
            private final int lightBlock;
            @Nullable
            private final VoxelShape[] occlusionShapes;
            protected final VoxelShape collisionShape;
            protected final boolean largeCollisionShape;
            private final boolean[] faceSturdy;
            protected final boolean isCollisionShapeFullBlock;
            
            private Cache(final BlockState cee) {
                final Block bul3 = cee.getBlock();
                this.solidRender = cee.isSolidRender(EmptyBlockGetter.INSTANCE, BlockPos.ZERO);
                this.propagatesSkylightDown = bul3.propagatesSkylightDown(cee, EmptyBlockGetter.INSTANCE, BlockPos.ZERO);
                this.lightBlock = bul3.getLightBlock(cee, EmptyBlockGetter.INSTANCE, BlockPos.ZERO);
                if (!cee.canOcclude()) {
                    this.occlusionShapes = null;
                }
                else {
                    this.occlusionShapes = new VoxelShape[Cache.DIRECTIONS.length];
                    final VoxelShape dde4 = bul3.getOcclusionShape(cee, EmptyBlockGetter.INSTANCE, BlockPos.ZERO);
                    for (final Direction gc8 : Cache.DIRECTIONS) {
                        this.occlusionShapes[gc8.ordinal()] = Shapes.getFaceShape(dde4, gc8);
                    }
                }
                this.collisionShape = bul3.getCollisionShape(cee, EmptyBlockGetter.INSTANCE, BlockPos.ZERO, CollisionContext.empty());
                this.largeCollisionShape = Arrays.stream((Object[])Direction.Axis.values()).anyMatch(a -> this.collisionShape.min(a) < 0.0 || this.collisionShape.max(a) > 1.0);
                this.faceSturdy = new boolean[Cache.DIRECTIONS.length * Cache.SUPPORT_TYPE_COUNT];
                for (final Direction gc9 : Cache.DIRECTIONS) {
                    for (final SupportType caq11 : SupportType.values()) {
                        this.faceSturdy[getFaceSupportIndex(gc9, caq11)] = caq11.isSupporting(cee, EmptyBlockGetter.INSTANCE, BlockPos.ZERO, gc9);
                    }
                }
                this.isCollisionShapeFullBlock = Block.isShapeFullBlock(cee.getCollisionShape(EmptyBlockGetter.INSTANCE, BlockPos.ZERO));
            }
            
            public boolean isFaceSturdy(final Direction gc, final SupportType caq) {
                return this.faceSturdy[getFaceSupportIndex(gc, caq)];
            }
            
            private static int getFaceSupportIndex(final Direction gc, final SupportType caq) {
                return gc.ordinal() * Cache.SUPPORT_TYPE_COUNT + caq.ordinal();
            }
            
            static {
                DIRECTIONS = Direction.values();
                SUPPORT_TYPE_COUNT = SupportType.values().length;
            }
        }
    }
    
    public interface StateArgumentPredicate<A> {
        boolean test(final BlockState cee, final BlockGetter bqz, final BlockPos fx, final A object);
    }
    
    public interface StatePredicate {
        boolean test(final BlockState cee, final BlockGetter bqz, final BlockPos fx);
    }
}

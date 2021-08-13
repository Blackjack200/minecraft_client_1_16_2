package net.minecraft.world.level.block.entity;

import com.google.common.collect.ImmutableSet;
import net.minecraft.world.level.block.Blocks;
import org.apache.logging.log4j.LogManager;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.Util;
import net.minecraft.util.datafix.fixes.References;
import javax.annotation.Nullable;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import com.mojang.datafixers.types.Type;
import net.minecraft.world.level.block.Block;
import java.util.Set;
import java.util.function.Supplier;
import net.minecraft.world.level.block.piston.PistonMovingBlockEntity;
import org.apache.logging.log4j.Logger;

public class BlockEntityType<T extends BlockEntity> {
    private static final Logger LOGGER;
    public static final BlockEntityType<FurnaceBlockEntity> FURNACE;
    public static final BlockEntityType<ChestBlockEntity> CHEST;
    public static final BlockEntityType<TrappedChestBlockEntity> TRAPPED_CHEST;
    public static final BlockEntityType<EnderChestBlockEntity> ENDER_CHEST;
    public static final BlockEntityType<JukeboxBlockEntity> JUKEBOX;
    public static final BlockEntityType<DispenserBlockEntity> DISPENSER;
    public static final BlockEntityType<DropperBlockEntity> DROPPER;
    public static final BlockEntityType<SignBlockEntity> SIGN;
    public static final BlockEntityType<SpawnerBlockEntity> MOB_SPAWNER;
    public static final BlockEntityType<PistonMovingBlockEntity> PISTON;
    public static final BlockEntityType<BrewingStandBlockEntity> BREWING_STAND;
    public static final BlockEntityType<EnchantmentTableBlockEntity> ENCHANTING_TABLE;
    public static final BlockEntityType<TheEndPortalBlockEntity> END_PORTAL;
    public static final BlockEntityType<BeaconBlockEntity> BEACON;
    public static final BlockEntityType<SkullBlockEntity> SKULL;
    public static final BlockEntityType<DaylightDetectorBlockEntity> DAYLIGHT_DETECTOR;
    public static final BlockEntityType<HopperBlockEntity> HOPPER;
    public static final BlockEntityType<ComparatorBlockEntity> COMPARATOR;
    public static final BlockEntityType<BannerBlockEntity> BANNER;
    public static final BlockEntityType<StructureBlockEntity> STRUCTURE_BLOCK;
    public static final BlockEntityType<TheEndGatewayBlockEntity> END_GATEWAY;
    public static final BlockEntityType<CommandBlockEntity> COMMAND_BLOCK;
    public static final BlockEntityType<ShulkerBoxBlockEntity> SHULKER_BOX;
    public static final BlockEntityType<BedBlockEntity> BED;
    public static final BlockEntityType<ConduitBlockEntity> CONDUIT;
    public static final BlockEntityType<BarrelBlockEntity> BARREL;
    public static final BlockEntityType<SmokerBlockEntity> SMOKER;
    public static final BlockEntityType<BlastFurnaceBlockEntity> BLAST_FURNACE;
    public static final BlockEntityType<LecternBlockEntity> LECTERN;
    public static final BlockEntityType<BellBlockEntity> BELL;
    public static final BlockEntityType<JigsawBlockEntity> JIGSAW;
    public static final BlockEntityType<CampfireBlockEntity> CAMPFIRE;
    public static final BlockEntityType<BeehiveBlockEntity> BEEHIVE;
    private final Supplier<? extends T> factory;
    private final Set<Block> validBlocks;
    private final Type<?> dataType;
    
    @Nullable
    public static ResourceLocation getKey(final BlockEntityType<?> cch) {
        return Registry.BLOCK_ENTITY_TYPE.getKey(cch);
    }
    
    private static <T extends BlockEntity> BlockEntityType<T> register(final String string, final Builder<T> a) {
        if (((Builder<BlockEntity>)a).validBlocks.isEmpty()) {
            BlockEntityType.LOGGER.warn("Block entity type {} requires at least one valid block to be defined!", string);
        }
        final Type<?> type3 = Util.fetchChoiceType(References.BLOCK_ENTITY, string);
        return Registry.<BlockEntityType<T>>register(Registry.BLOCK_ENTITY_TYPE, string, a.build(type3));
    }
    
    public BlockEntityType(final Supplier<? extends T> supplier, final Set<Block> set, final Type<?> type) {
        this.factory = supplier;
        this.validBlocks = set;
        this.dataType = type;
    }
    
    @Nullable
    public T create() {
        return (T)this.factory.get();
    }
    
    public boolean isValid(final Block bul) {
        return this.validBlocks.contains(bul);
    }
    
    @Nullable
    public T getBlockEntity(final BlockGetter bqz, final BlockPos fx) {
        final BlockEntity ccg4 = bqz.getBlockEntity(fx);
        if (ccg4 == null || ccg4.getType() != this) {
            return null;
        }
        return (T)ccg4;
    }
    
    static {
        LOGGER = LogManager.getLogger();
        FURNACE = BlockEntityType.<FurnaceBlockEntity>register("furnace", Builder.<FurnaceBlockEntity>of((java.util.function.Supplier<? extends FurnaceBlockEntity>)FurnaceBlockEntity::new, new Block[] { Blocks.FURNACE }));
        CHEST = BlockEntityType.<ChestBlockEntity>register("chest", Builder.<ChestBlockEntity>of((java.util.function.Supplier<? extends ChestBlockEntity>)ChestBlockEntity::new, new Block[] { Blocks.CHEST }));
        TRAPPED_CHEST = BlockEntityType.<TrappedChestBlockEntity>register("trapped_chest", Builder.<TrappedChestBlockEntity>of((java.util.function.Supplier<? extends TrappedChestBlockEntity>)TrappedChestBlockEntity::new, new Block[] { Blocks.TRAPPED_CHEST }));
        ENDER_CHEST = BlockEntityType.<EnderChestBlockEntity>register("ender_chest", Builder.<EnderChestBlockEntity>of((java.util.function.Supplier<? extends EnderChestBlockEntity>)EnderChestBlockEntity::new, new Block[] { Blocks.ENDER_CHEST }));
        JUKEBOX = BlockEntityType.<JukeboxBlockEntity>register("jukebox", Builder.<JukeboxBlockEntity>of((java.util.function.Supplier<? extends JukeboxBlockEntity>)JukeboxBlockEntity::new, new Block[] { Blocks.JUKEBOX }));
        DISPENSER = BlockEntityType.<DispenserBlockEntity>register("dispenser", Builder.<DispenserBlockEntity>of((java.util.function.Supplier<? extends DispenserBlockEntity>)DispenserBlockEntity::new, new Block[] { Blocks.DISPENSER }));
        DROPPER = BlockEntityType.<DropperBlockEntity>register("dropper", Builder.<DropperBlockEntity>of((java.util.function.Supplier<? extends DropperBlockEntity>)DropperBlockEntity::new, new Block[] { Blocks.DROPPER }));
        SIGN = BlockEntityType.<SignBlockEntity>register("sign", Builder.<SignBlockEntity>of((java.util.function.Supplier<? extends SignBlockEntity>)SignBlockEntity::new, new Block[] { Blocks.OAK_SIGN, Blocks.SPRUCE_SIGN, Blocks.BIRCH_SIGN, Blocks.ACACIA_SIGN, Blocks.JUNGLE_SIGN, Blocks.DARK_OAK_SIGN, Blocks.OAK_WALL_SIGN, Blocks.SPRUCE_WALL_SIGN, Blocks.BIRCH_WALL_SIGN, Blocks.ACACIA_WALL_SIGN, Blocks.JUNGLE_WALL_SIGN, Blocks.DARK_OAK_WALL_SIGN, Blocks.CRIMSON_SIGN, Blocks.CRIMSON_WALL_SIGN, Blocks.WARPED_SIGN, Blocks.WARPED_WALL_SIGN }));
        MOB_SPAWNER = BlockEntityType.<SpawnerBlockEntity>register("mob_spawner", Builder.<SpawnerBlockEntity>of((java.util.function.Supplier<? extends SpawnerBlockEntity>)SpawnerBlockEntity::new, new Block[] { Blocks.SPAWNER }));
        PISTON = BlockEntityType.<PistonMovingBlockEntity>register("piston", Builder.<PistonMovingBlockEntity>of((java.util.function.Supplier<? extends PistonMovingBlockEntity>)PistonMovingBlockEntity::new, new Block[] { Blocks.MOVING_PISTON }));
        BREWING_STAND = BlockEntityType.<BrewingStandBlockEntity>register("brewing_stand", Builder.<BrewingStandBlockEntity>of((java.util.function.Supplier<? extends BrewingStandBlockEntity>)BrewingStandBlockEntity::new, new Block[] { Blocks.BREWING_STAND }));
        ENCHANTING_TABLE = BlockEntityType.<EnchantmentTableBlockEntity>register("enchanting_table", Builder.<EnchantmentTableBlockEntity>of((java.util.function.Supplier<? extends EnchantmentTableBlockEntity>)EnchantmentTableBlockEntity::new, new Block[] { Blocks.ENCHANTING_TABLE }));
        END_PORTAL = BlockEntityType.<TheEndPortalBlockEntity>register("end_portal", Builder.<TheEndPortalBlockEntity>of((java.util.function.Supplier<? extends TheEndPortalBlockEntity>)TheEndPortalBlockEntity::new, new Block[] { Blocks.END_PORTAL }));
        BEACON = BlockEntityType.<BeaconBlockEntity>register("beacon", Builder.<BeaconBlockEntity>of((java.util.function.Supplier<? extends BeaconBlockEntity>)BeaconBlockEntity::new, new Block[] { Blocks.BEACON }));
        SKULL = BlockEntityType.<SkullBlockEntity>register("skull", Builder.<SkullBlockEntity>of((java.util.function.Supplier<? extends SkullBlockEntity>)SkullBlockEntity::new, new Block[] { Blocks.SKELETON_SKULL, Blocks.SKELETON_WALL_SKULL, Blocks.CREEPER_HEAD, Blocks.CREEPER_WALL_HEAD, Blocks.DRAGON_HEAD, Blocks.DRAGON_WALL_HEAD, Blocks.ZOMBIE_HEAD, Blocks.ZOMBIE_WALL_HEAD, Blocks.WITHER_SKELETON_SKULL, Blocks.WITHER_SKELETON_WALL_SKULL, Blocks.PLAYER_HEAD, Blocks.PLAYER_WALL_HEAD }));
        DAYLIGHT_DETECTOR = BlockEntityType.<DaylightDetectorBlockEntity>register("daylight_detector", Builder.<DaylightDetectorBlockEntity>of((java.util.function.Supplier<? extends DaylightDetectorBlockEntity>)DaylightDetectorBlockEntity::new, new Block[] { Blocks.DAYLIGHT_DETECTOR }));
        HOPPER = BlockEntityType.<HopperBlockEntity>register("hopper", Builder.<HopperBlockEntity>of((java.util.function.Supplier<? extends HopperBlockEntity>)HopperBlockEntity::new, new Block[] { Blocks.HOPPER }));
        COMPARATOR = BlockEntityType.<ComparatorBlockEntity>register("comparator", Builder.<ComparatorBlockEntity>of((java.util.function.Supplier<? extends ComparatorBlockEntity>)ComparatorBlockEntity::new, new Block[] { Blocks.COMPARATOR }));
        BANNER = BlockEntityType.<BannerBlockEntity>register("banner", Builder.<BannerBlockEntity>of((java.util.function.Supplier<? extends BannerBlockEntity>)BannerBlockEntity::new, new Block[] { Blocks.WHITE_BANNER, Blocks.ORANGE_BANNER, Blocks.MAGENTA_BANNER, Blocks.LIGHT_BLUE_BANNER, Blocks.YELLOW_BANNER, Blocks.LIME_BANNER, Blocks.PINK_BANNER, Blocks.GRAY_BANNER, Blocks.LIGHT_GRAY_BANNER, Blocks.CYAN_BANNER, Blocks.PURPLE_BANNER, Blocks.BLUE_BANNER, Blocks.BROWN_BANNER, Blocks.GREEN_BANNER, Blocks.RED_BANNER, Blocks.BLACK_BANNER, Blocks.WHITE_WALL_BANNER, Blocks.ORANGE_WALL_BANNER, Blocks.MAGENTA_WALL_BANNER, Blocks.LIGHT_BLUE_WALL_BANNER, Blocks.YELLOW_WALL_BANNER, Blocks.LIME_WALL_BANNER, Blocks.PINK_WALL_BANNER, Blocks.GRAY_WALL_BANNER, Blocks.LIGHT_GRAY_WALL_BANNER, Blocks.CYAN_WALL_BANNER, Blocks.PURPLE_WALL_BANNER, Blocks.BLUE_WALL_BANNER, Blocks.BROWN_WALL_BANNER, Blocks.GREEN_WALL_BANNER, Blocks.RED_WALL_BANNER, Blocks.BLACK_WALL_BANNER }));
        STRUCTURE_BLOCK = BlockEntityType.<StructureBlockEntity>register("structure_block", Builder.<StructureBlockEntity>of((java.util.function.Supplier<? extends StructureBlockEntity>)StructureBlockEntity::new, new Block[] { Blocks.STRUCTURE_BLOCK }));
        END_GATEWAY = BlockEntityType.<TheEndGatewayBlockEntity>register("end_gateway", Builder.<TheEndGatewayBlockEntity>of((java.util.function.Supplier<? extends TheEndGatewayBlockEntity>)TheEndGatewayBlockEntity::new, new Block[] { Blocks.END_GATEWAY }));
        COMMAND_BLOCK = BlockEntityType.<CommandBlockEntity>register("command_block", Builder.<CommandBlockEntity>of((java.util.function.Supplier<? extends CommandBlockEntity>)CommandBlockEntity::new, new Block[] { Blocks.COMMAND_BLOCK, Blocks.CHAIN_COMMAND_BLOCK, Blocks.REPEATING_COMMAND_BLOCK }));
        SHULKER_BOX = BlockEntityType.<ShulkerBoxBlockEntity>register("shulker_box", Builder.<ShulkerBoxBlockEntity>of((java.util.function.Supplier<? extends ShulkerBoxBlockEntity>)ShulkerBoxBlockEntity::new, new Block[] { Blocks.SHULKER_BOX, Blocks.BLACK_SHULKER_BOX, Blocks.BLUE_SHULKER_BOX, Blocks.BROWN_SHULKER_BOX, Blocks.CYAN_SHULKER_BOX, Blocks.GRAY_SHULKER_BOX, Blocks.GREEN_SHULKER_BOX, Blocks.LIGHT_BLUE_SHULKER_BOX, Blocks.LIGHT_GRAY_SHULKER_BOX, Blocks.LIME_SHULKER_BOX, Blocks.MAGENTA_SHULKER_BOX, Blocks.ORANGE_SHULKER_BOX, Blocks.PINK_SHULKER_BOX, Blocks.PURPLE_SHULKER_BOX, Blocks.RED_SHULKER_BOX, Blocks.WHITE_SHULKER_BOX, Blocks.YELLOW_SHULKER_BOX }));
        BED = BlockEntityType.<BedBlockEntity>register("bed", Builder.<BedBlockEntity>of((java.util.function.Supplier<? extends BedBlockEntity>)BedBlockEntity::new, new Block[] { Blocks.RED_BED, Blocks.BLACK_BED, Blocks.BLUE_BED, Blocks.BROWN_BED, Blocks.CYAN_BED, Blocks.GRAY_BED, Blocks.GREEN_BED, Blocks.LIGHT_BLUE_BED, Blocks.LIGHT_GRAY_BED, Blocks.LIME_BED, Blocks.MAGENTA_BED, Blocks.ORANGE_BED, Blocks.PINK_BED, Blocks.PURPLE_BED, Blocks.WHITE_BED, Blocks.YELLOW_BED }));
        CONDUIT = BlockEntityType.<ConduitBlockEntity>register("conduit", Builder.<ConduitBlockEntity>of((java.util.function.Supplier<? extends ConduitBlockEntity>)ConduitBlockEntity::new, new Block[] { Blocks.CONDUIT }));
        BARREL = BlockEntityType.<BarrelBlockEntity>register("barrel", Builder.<BarrelBlockEntity>of((java.util.function.Supplier<? extends BarrelBlockEntity>)BarrelBlockEntity::new, new Block[] { Blocks.BARREL }));
        SMOKER = BlockEntityType.<SmokerBlockEntity>register("smoker", Builder.<SmokerBlockEntity>of((java.util.function.Supplier<? extends SmokerBlockEntity>)SmokerBlockEntity::new, new Block[] { Blocks.SMOKER }));
        BLAST_FURNACE = BlockEntityType.<BlastFurnaceBlockEntity>register("blast_furnace", Builder.<BlastFurnaceBlockEntity>of((java.util.function.Supplier<? extends BlastFurnaceBlockEntity>)BlastFurnaceBlockEntity::new, new Block[] { Blocks.BLAST_FURNACE }));
        LECTERN = BlockEntityType.<LecternBlockEntity>register("lectern", Builder.<LecternBlockEntity>of((java.util.function.Supplier<? extends LecternBlockEntity>)LecternBlockEntity::new, new Block[] { Blocks.LECTERN }));
        BELL = BlockEntityType.<BellBlockEntity>register("bell", Builder.<BellBlockEntity>of((java.util.function.Supplier<? extends BellBlockEntity>)BellBlockEntity::new, new Block[] { Blocks.BELL }));
        JIGSAW = BlockEntityType.<JigsawBlockEntity>register("jigsaw", Builder.<JigsawBlockEntity>of((java.util.function.Supplier<? extends JigsawBlockEntity>)JigsawBlockEntity::new, new Block[] { Blocks.JIGSAW }));
        CAMPFIRE = BlockEntityType.<CampfireBlockEntity>register("campfire", Builder.<CampfireBlockEntity>of((java.util.function.Supplier<? extends CampfireBlockEntity>)CampfireBlockEntity::new, new Block[] { Blocks.CAMPFIRE, Blocks.SOUL_CAMPFIRE }));
        BEEHIVE = BlockEntityType.<BeehiveBlockEntity>register("beehive", Builder.<BeehiveBlockEntity>of((java.util.function.Supplier<? extends BeehiveBlockEntity>)BeehiveBlockEntity::new, new Block[] { Blocks.BEE_NEST, Blocks.BEEHIVE }));
    }
    
    public static final class Builder<T extends BlockEntity> {
        private final Supplier<? extends T> factory;
        private final Set<Block> validBlocks;
        
        private Builder(final Supplier<? extends T> supplier, final Set<Block> set) {
            this.factory = supplier;
            this.validBlocks = set;
        }
        
        public static <T extends BlockEntity> Builder<T> of(final Supplier<? extends T> supplier, final Block... arr) {
            return new Builder<T>(supplier, (Set<Block>)ImmutableSet.copyOf((Object[])arr));
        }
        
        public BlockEntityType<T> build(final Type<?> type) {
            return new BlockEntityType<T>(this.factory, this.validBlocks, type);
        }
    }
    
    public static final class Builder<T extends BlockEntity> {
        private final Supplier<? extends T> factory;
        private final Set<Block> validBlocks;
        
        private Builder(final Supplier<? extends T> supplier, final Set<Block> set) {
            this.factory = supplier;
            this.validBlocks = set;
        }
        
        public static <T extends BlockEntity> Builder<T> of(final Supplier<? extends T> supplier, final Block... arr) {
            return new Builder<T>(supplier, (Set<Block>)ImmutableSet.copyOf((Object[])arr));
        }
        
        public BlockEntityType<T> build(final Type<?> type) {
            return new BlockEntityType<T>(this.factory, this.validBlocks, type);
        }
    }
}

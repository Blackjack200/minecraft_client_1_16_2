package net.minecraft.data.models;

import javax.annotation.Nullable;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.core.FrontAndTop;
import net.minecraft.world.level.block.state.properties.StructureMode;
import net.minecraft.world.level.block.state.properties.PistonType;
import java.util.Arrays;
import java.util.function.UnaryOperator;
import com.google.common.collect.ImmutableList;
import net.minecraft.world.level.block.state.properties.ComparatorMode;
import net.minecraft.world.level.block.state.properties.RedstoneSide;
import net.minecraft.world.level.block.state.properties.BellAttachType;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.data.models.model.TextureSlot;
import net.minecraft.world.level.block.state.properties.BambooLeaves;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.List;
import java.util.function.BiFunction;
import net.minecraft.world.level.block.state.properties.RailShape;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.properties.SlabType;
import java.util.function.Function;
import net.minecraft.data.models.model.ModelTemplate;
import net.minecraft.world.level.block.state.properties.StairsShape;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.level.block.state.properties.WallSide;
import net.minecraft.data.models.blockstates.Condition;
import net.minecraft.data.models.blockstates.MultiPartGenerator;
import net.minecraft.world.level.block.state.properties.DoorHingeSide;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.AttachFace;
import net.minecraft.data.models.model.TexturedModel;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.data.models.blockstates.MultiVariantGenerator;
import net.minecraft.data.models.blockstates.VariantProperties;
import net.minecraft.data.models.blockstates.Variant;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.data.models.blockstates.PropertyDispatch;
import net.minecraft.world.item.Items;
import net.minecraft.data.models.model.TextureMapping;
import net.minecraft.data.models.model.ModelTemplates;
import net.minecraft.data.models.model.DelegatedModel;
import net.minecraft.data.models.model.ModelLocationUtils;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.item.Item;
import com.google.gson.JsonElement;
import java.util.function.Supplier;
import net.minecraft.resources.ResourceLocation;
import java.util.function.BiConsumer;
import net.minecraft.data.models.blockstates.BlockStateGenerator;
import java.util.function.Consumer;

public class BlockModelGenerators {
    private final Consumer<BlockStateGenerator> blockStateOutput;
    private final BiConsumer<ResourceLocation, Supplier<JsonElement>> modelOutput;
    private final Consumer<Item> skippedAutoModelsOutput;
    
    public BlockModelGenerators(final Consumer<BlockStateGenerator> consumer1, final BiConsumer<ResourceLocation, Supplier<JsonElement>> biConsumer, final Consumer<Item> consumer3) {
        this.blockStateOutput = consumer1;
        this.modelOutput = biConsumer;
        this.skippedAutoModelsOutput = consumer3;
    }
    
    private void skipAutoItemBlock(final Block bul) {
        this.skippedAutoModelsOutput.accept(bul.asItem());
    }
    
    private void delegateItemModel(final Block bul, final ResourceLocation vk) {
        this.modelOutput.accept(ModelLocationUtils.getModelLocation(bul.asItem()), new DelegatedModel(vk));
    }
    
    private void delegateItemModel(final Item blu, final ResourceLocation vk) {
        this.modelOutput.accept(ModelLocationUtils.getModelLocation(blu), new DelegatedModel(vk));
    }
    
    private void createSimpleFlatItemModel(final Item blu) {
        ModelTemplates.FLAT_ITEM.create(ModelLocationUtils.getModelLocation(blu), TextureMapping.layer0(blu), this.modelOutput);
    }
    
    private void createSimpleFlatItemModel(final Block bul) {
        final Item blu3 = bul.asItem();
        if (blu3 != Items.AIR) {
            ModelTemplates.FLAT_ITEM.create(ModelLocationUtils.getModelLocation(blu3), TextureMapping.layer0(bul), this.modelOutput);
        }
    }
    
    private void createSimpleFlatItemModel(final Block bul, final String string) {
        final Item blu4 = bul.asItem();
        ModelTemplates.FLAT_ITEM.create(ModelLocationUtils.getModelLocation(blu4), TextureMapping.layer0(TextureMapping.getBlockTexture(bul, string)), this.modelOutput);
    }
    
    private static PropertyDispatch createHorizontalFacingDispatch() {
        return PropertyDispatch.<Comparable>property((Property<Comparable>)BlockStateProperties.HORIZONTAL_FACING).select((Comparable)Direction.EAST, Variant.variant().<VariantProperties.Rotation>with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90)).select((Comparable)Direction.SOUTH, Variant.variant().<VariantProperties.Rotation>with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180)).select((Comparable)Direction.WEST, Variant.variant().<VariantProperties.Rotation>with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270)).select((Comparable)Direction.NORTH, Variant.variant());
    }
    
    private static PropertyDispatch createHorizontalFacingDispatchAlt() {
        return PropertyDispatch.<Comparable>property((Property<Comparable>)BlockStateProperties.HORIZONTAL_FACING).select((Comparable)Direction.SOUTH, Variant.variant()).select((Comparable)Direction.WEST, Variant.variant().<VariantProperties.Rotation>with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90)).select((Comparable)Direction.NORTH, Variant.variant().<VariantProperties.Rotation>with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180)).select((Comparable)Direction.EAST, Variant.variant().<VariantProperties.Rotation>with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270));
    }
    
    private static PropertyDispatch createTorchHorizontalDispatch() {
        return PropertyDispatch.<Comparable>property((Property<Comparable>)BlockStateProperties.HORIZONTAL_FACING).select((Comparable)Direction.EAST, Variant.variant()).select((Comparable)Direction.SOUTH, Variant.variant().<VariantProperties.Rotation>with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90)).select((Comparable)Direction.WEST, Variant.variant().<VariantProperties.Rotation>with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180)).select((Comparable)Direction.NORTH, Variant.variant().<VariantProperties.Rotation>with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270));
    }
    
    private static PropertyDispatch createFacingDispatch() {
        return PropertyDispatch.<Comparable>property((Property<Comparable>)BlockStateProperties.FACING).select((Comparable)Direction.DOWN, Variant.variant().<VariantProperties.Rotation>with(VariantProperties.X_ROT, VariantProperties.Rotation.R90)).select((Comparable)Direction.UP, Variant.variant().<VariantProperties.Rotation>with(VariantProperties.X_ROT, VariantProperties.Rotation.R270)).select((Comparable)Direction.NORTH, Variant.variant()).select((Comparable)Direction.SOUTH, Variant.variant().<VariantProperties.Rotation>with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180)).select((Comparable)Direction.WEST, Variant.variant().<VariantProperties.Rotation>with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270)).select((Comparable)Direction.EAST, Variant.variant().<VariantProperties.Rotation>with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90));
    }
    
    private static MultiVariantGenerator createRotatedVariant(final Block bul, final ResourceLocation vk) {
        return MultiVariantGenerator.multiVariant(bul, createRotatedVariants(vk));
    }
    
    private static Variant[] createRotatedVariants(final ResourceLocation vk) {
        return new Variant[] { Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, vk), Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, vk).<VariantProperties.Rotation>with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90), Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, vk).<VariantProperties.Rotation>with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180), Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, vk).<VariantProperties.Rotation>with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270) };
    }
    
    private static MultiVariantGenerator createRotatedVariant(final Block bul, final ResourceLocation vk2, final ResourceLocation vk3) {
        return MultiVariantGenerator.multiVariant(bul, Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, vk2), Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, vk3), Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, vk2).<VariantProperties.Rotation>with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180), Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, vk3).<VariantProperties.Rotation>with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180));
    }
    
    private static PropertyDispatch createBooleanModelDispatch(final BooleanProperty cev, final ResourceLocation vk2, final ResourceLocation vk3) {
        return PropertyDispatch.<Comparable>property((Property<Comparable>)cev).select((Comparable)true, Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, vk2)).select((Comparable)false, Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, vk3));
    }
    
    private void createRotatedMirroredVariantBlock(final Block bul) {
        final ResourceLocation vk3 = TexturedModel.CUBE.create(bul, this.modelOutput);
        final ResourceLocation vk4 = TexturedModel.CUBE_MIRRORED.create(bul, this.modelOutput);
        this.blockStateOutput.accept(createRotatedVariant(bul, vk3, vk4));
    }
    
    private void createRotatedVariantBlock(final Block bul) {
        final ResourceLocation vk3 = TexturedModel.CUBE.create(bul, this.modelOutput);
        this.blockStateOutput.accept(createRotatedVariant(bul, vk3));
    }
    
    private static BlockStateGenerator createButton(final Block bul, final ResourceLocation vk2, final ResourceLocation vk3) {
        return MultiVariantGenerator.multiVariant(bul).with(PropertyDispatch.<Comparable>property((Property<Comparable>)BlockStateProperties.POWERED).select((Comparable)false, Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, vk2)).select((Comparable)true, Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, vk3))).with(PropertyDispatch.<AttachFace, Comparable>properties(BlockStateProperties.ATTACH_FACE, (Property<Comparable>)BlockStateProperties.HORIZONTAL_FACING).select(AttachFace.FLOOR, (Comparable)Direction.EAST, Variant.variant().<VariantProperties.Rotation>with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90)).select(AttachFace.FLOOR, (Comparable)Direction.WEST, Variant.variant().<VariantProperties.Rotation>with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270)).select(AttachFace.FLOOR, (Comparable)Direction.SOUTH, Variant.variant().<VariantProperties.Rotation>with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180)).select(AttachFace.FLOOR, (Comparable)Direction.NORTH, Variant.variant()).select(AttachFace.WALL, (Comparable)Direction.EAST, Variant.variant().<VariantProperties.Rotation>with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90).<VariantProperties.Rotation>with(VariantProperties.X_ROT, VariantProperties.Rotation.R90).<Boolean>with(VariantProperties.UV_LOCK, true)).select(AttachFace.WALL, (Comparable)Direction.WEST, Variant.variant().<VariantProperties.Rotation>with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270).<VariantProperties.Rotation>with(VariantProperties.X_ROT, VariantProperties.Rotation.R90).<Boolean>with(VariantProperties.UV_LOCK, true)).select(AttachFace.WALL, (Comparable)Direction.SOUTH, Variant.variant().<VariantProperties.Rotation>with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180).<VariantProperties.Rotation>with(VariantProperties.X_ROT, VariantProperties.Rotation.R90).<Boolean>with(VariantProperties.UV_LOCK, true)).select(AttachFace.WALL, (Comparable)Direction.NORTH, Variant.variant().<VariantProperties.Rotation>with(VariantProperties.X_ROT, VariantProperties.Rotation.R90).<Boolean>with(VariantProperties.UV_LOCK, true)).select(AttachFace.CEILING, (Comparable)Direction.EAST, Variant.variant().<VariantProperties.Rotation>with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270).<VariantProperties.Rotation>with(VariantProperties.X_ROT, VariantProperties.Rotation.R180)).select(AttachFace.CEILING, (Comparable)Direction.WEST, Variant.variant().<VariantProperties.Rotation>with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90).<VariantProperties.Rotation>with(VariantProperties.X_ROT, VariantProperties.Rotation.R180)).select(AttachFace.CEILING, (Comparable)Direction.SOUTH, Variant.variant().<VariantProperties.Rotation>with(VariantProperties.X_ROT, VariantProperties.Rotation.R180)).select(AttachFace.CEILING, (Comparable)Direction.NORTH, Variant.variant().<VariantProperties.Rotation>with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180).<VariantProperties.Rotation>with(VariantProperties.X_ROT, VariantProperties.Rotation.R180)));
    }
    
    private static PropertyDispatch.C4<Direction, DoubleBlockHalf, DoorHingeSide, Boolean> configureDoorHalf(final PropertyDispatch.C4<Direction, DoubleBlockHalf, DoorHingeSide, Boolean> d, final DoubleBlockHalf cfa, final ResourceLocation vk3, final ResourceLocation vk4) {
        return d.select(Direction.EAST, cfa, DoorHingeSide.LEFT, false, Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, vk3)).select(Direction.SOUTH, cfa, DoorHingeSide.LEFT, false, Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, vk3).<VariantProperties.Rotation>with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90)).select(Direction.WEST, cfa, DoorHingeSide.LEFT, false, Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, vk3).<VariantProperties.Rotation>with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180)).select(Direction.NORTH, cfa, DoorHingeSide.LEFT, false, Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, vk3).<VariantProperties.Rotation>with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270)).select(Direction.EAST, cfa, DoorHingeSide.RIGHT, false, Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, vk4)).select(Direction.SOUTH, cfa, DoorHingeSide.RIGHT, false, Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, vk4).<VariantProperties.Rotation>with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90)).select(Direction.WEST, cfa, DoorHingeSide.RIGHT, false, Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, vk4).<VariantProperties.Rotation>with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180)).select(Direction.NORTH, cfa, DoorHingeSide.RIGHT, false, Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, vk4).<VariantProperties.Rotation>with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270)).select(Direction.EAST, cfa, DoorHingeSide.LEFT, true, Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, vk4).<VariantProperties.Rotation>with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90)).select(Direction.SOUTH, cfa, DoorHingeSide.LEFT, true, Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, vk4).<VariantProperties.Rotation>with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180)).select(Direction.WEST, cfa, DoorHingeSide.LEFT, true, Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, vk4).<VariantProperties.Rotation>with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270)).select(Direction.NORTH, cfa, DoorHingeSide.LEFT, true, Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, vk4)).select(Direction.EAST, cfa, DoorHingeSide.RIGHT, true, Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, vk3).<VariantProperties.Rotation>with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270)).select(Direction.SOUTH, cfa, DoorHingeSide.RIGHT, true, Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, vk3)).select(Direction.WEST, cfa, DoorHingeSide.RIGHT, true, Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, vk3).<VariantProperties.Rotation>with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90)).select(Direction.NORTH, cfa, DoorHingeSide.RIGHT, true, Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, vk3).<VariantProperties.Rotation>with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180));
    }
    
    private static BlockStateGenerator createDoor(final Block bul, final ResourceLocation vk2, final ResourceLocation vk3, final ResourceLocation vk4, final ResourceLocation vk5) {
        return MultiVariantGenerator.multiVariant(bul).with(configureDoorHalf(configureDoorHalf(PropertyDispatch.properties((Property<Comparable>)BlockStateProperties.HORIZONTAL_FACING, BlockStateProperties.DOUBLE_BLOCK_HALF, BlockStateProperties.DOOR_HINGE, (Property<Comparable>)BlockStateProperties.OPEN), DoubleBlockHalf.LOWER, vk2, vk3), DoubleBlockHalf.UPPER, vk4, vk5));
    }
    
    private static BlockStateGenerator createFence(final Block bul, final ResourceLocation vk2, final ResourceLocation vk3) {
        return MultiPartGenerator.multiPart(bul).with(Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, vk2)).with(Condition.condition().<Boolean>term(BlockStateProperties.NORTH, true), Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, vk3).<Boolean>with(VariantProperties.UV_LOCK, true)).with(Condition.condition().<Boolean>term(BlockStateProperties.EAST, true), Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, vk3).<VariantProperties.Rotation>with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90).<Boolean>with(VariantProperties.UV_LOCK, true)).with(Condition.condition().<Boolean>term(BlockStateProperties.SOUTH, true), Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, vk3).<VariantProperties.Rotation>with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180).<Boolean>with(VariantProperties.UV_LOCK, true)).with(Condition.condition().<Boolean>term(BlockStateProperties.WEST, true), Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, vk3).<VariantProperties.Rotation>with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270).<Boolean>with(VariantProperties.UV_LOCK, true));
    }
    
    private static BlockStateGenerator createWall(final Block bul, final ResourceLocation vk2, final ResourceLocation vk3, final ResourceLocation vk4) {
        return MultiPartGenerator.multiPart(bul).with(Condition.condition().<Boolean>term(BlockStateProperties.UP, true), Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, vk2)).with(Condition.condition().<WallSide>term(BlockStateProperties.NORTH_WALL, WallSide.LOW), Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, vk3).<Boolean>with(VariantProperties.UV_LOCK, true)).with(Condition.condition().<WallSide>term(BlockStateProperties.EAST_WALL, WallSide.LOW), Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, vk3).<VariantProperties.Rotation>with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90).<Boolean>with(VariantProperties.UV_LOCK, true)).with(Condition.condition().<WallSide>term(BlockStateProperties.SOUTH_WALL, WallSide.LOW), Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, vk3).<VariantProperties.Rotation>with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180).<Boolean>with(VariantProperties.UV_LOCK, true)).with(Condition.condition().<WallSide>term(BlockStateProperties.WEST_WALL, WallSide.LOW), Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, vk3).<VariantProperties.Rotation>with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270).<Boolean>with(VariantProperties.UV_LOCK, true)).with(Condition.condition().<WallSide>term(BlockStateProperties.NORTH_WALL, WallSide.TALL), Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, vk4).<Boolean>with(VariantProperties.UV_LOCK, true)).with(Condition.condition().<WallSide>term(BlockStateProperties.EAST_WALL, WallSide.TALL), Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, vk4).<VariantProperties.Rotation>with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90).<Boolean>with(VariantProperties.UV_LOCK, true)).with(Condition.condition().<WallSide>term(BlockStateProperties.SOUTH_WALL, WallSide.TALL), Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, vk4).<VariantProperties.Rotation>with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180).<Boolean>with(VariantProperties.UV_LOCK, true)).with(Condition.condition().<WallSide>term(BlockStateProperties.WEST_WALL, WallSide.TALL), Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, vk4).<VariantProperties.Rotation>with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270).<Boolean>with(VariantProperties.UV_LOCK, true));
    }
    
    private static BlockStateGenerator createFenceGate(final Block bul, final ResourceLocation vk2, final ResourceLocation vk3, final ResourceLocation vk4, final ResourceLocation vk5) {
        return MultiVariantGenerator.multiVariant(bul, Variant.variant().<Boolean>with(VariantProperties.UV_LOCK, true)).with(createHorizontalFacingDispatchAlt()).with(PropertyDispatch.<Comparable, Comparable>properties((Property<Comparable>)BlockStateProperties.IN_WALL, (Property<Comparable>)BlockStateProperties.OPEN).select((Comparable)false, (Comparable)false, Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, vk3)).select((Comparable)true, (Comparable)false, Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, vk5)).select((Comparable)false, (Comparable)true, Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, vk2)).select((Comparable)true, (Comparable)true, Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, vk4)));
    }
    
    private static BlockStateGenerator createStairs(final Block bul, final ResourceLocation vk2, final ResourceLocation vk3, final ResourceLocation vk4) {
        return MultiVariantGenerator.multiVariant(bul).with(PropertyDispatch.<Comparable, Half, StairsShape>properties((Property<Comparable>)BlockStateProperties.HORIZONTAL_FACING, BlockStateProperties.HALF, BlockStateProperties.STAIRS_SHAPE).select((Comparable)Direction.EAST, Half.BOTTOM, StairsShape.STRAIGHT, Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, vk3)).select((Comparable)Direction.WEST, Half.BOTTOM, StairsShape.STRAIGHT, Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, vk3).<VariantProperties.Rotation>with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180).<Boolean>with(VariantProperties.UV_LOCK, true)).select((Comparable)Direction.SOUTH, Half.BOTTOM, StairsShape.STRAIGHT, Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, vk3).<VariantProperties.Rotation>with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90).<Boolean>with(VariantProperties.UV_LOCK, true)).select((Comparable)Direction.NORTH, Half.BOTTOM, StairsShape.STRAIGHT, Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, vk3).<VariantProperties.Rotation>with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270).<Boolean>with(VariantProperties.UV_LOCK, true)).select((Comparable)Direction.EAST, Half.BOTTOM, StairsShape.OUTER_RIGHT, Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, vk4)).select((Comparable)Direction.WEST, Half.BOTTOM, StairsShape.OUTER_RIGHT, Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, vk4).<VariantProperties.Rotation>with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180).<Boolean>with(VariantProperties.UV_LOCK, true)).select((Comparable)Direction.SOUTH, Half.BOTTOM, StairsShape.OUTER_RIGHT, Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, vk4).<VariantProperties.Rotation>with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90).<Boolean>with(VariantProperties.UV_LOCK, true)).select((Comparable)Direction.NORTH, Half.BOTTOM, StairsShape.OUTER_RIGHT, Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, vk4).<VariantProperties.Rotation>with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270).<Boolean>with(VariantProperties.UV_LOCK, true)).select((Comparable)Direction.EAST, Half.BOTTOM, StairsShape.OUTER_LEFT, Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, vk4).<VariantProperties.Rotation>with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270).<Boolean>with(VariantProperties.UV_LOCK, true)).select((Comparable)Direction.WEST, Half.BOTTOM, StairsShape.OUTER_LEFT, Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, vk4).<VariantProperties.Rotation>with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90).<Boolean>with(VariantProperties.UV_LOCK, true)).select((Comparable)Direction.SOUTH, Half.BOTTOM, StairsShape.OUTER_LEFT, Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, vk4)).select((Comparable)Direction.NORTH, Half.BOTTOM, StairsShape.OUTER_LEFT, Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, vk4).<VariantProperties.Rotation>with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180).<Boolean>with(VariantProperties.UV_LOCK, true)).select((Comparable)Direction.EAST, Half.BOTTOM, StairsShape.INNER_RIGHT, Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, vk2)).select((Comparable)Direction.WEST, Half.BOTTOM, StairsShape.INNER_RIGHT, Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, vk2).<VariantProperties.Rotation>with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180).<Boolean>with(VariantProperties.UV_LOCK, true)).select((Comparable)Direction.SOUTH, Half.BOTTOM, StairsShape.INNER_RIGHT, Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, vk2).<VariantProperties.Rotation>with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90).<Boolean>with(VariantProperties.UV_LOCK, true)).select((Comparable)Direction.NORTH, Half.BOTTOM, StairsShape.INNER_RIGHT, Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, vk2).<VariantProperties.Rotation>with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270).<Boolean>with(VariantProperties.UV_LOCK, true)).select((Comparable)Direction.EAST, Half.BOTTOM, StairsShape.INNER_LEFT, Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, vk2).<VariantProperties.Rotation>with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270).<Boolean>with(VariantProperties.UV_LOCK, true)).select((Comparable)Direction.WEST, Half.BOTTOM, StairsShape.INNER_LEFT, Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, vk2).<VariantProperties.Rotation>with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90).<Boolean>with(VariantProperties.UV_LOCK, true)).select((Comparable)Direction.SOUTH, Half.BOTTOM, StairsShape.INNER_LEFT, Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, vk2)).select((Comparable)Direction.NORTH, Half.BOTTOM, StairsShape.INNER_LEFT, Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, vk2).<VariantProperties.Rotation>with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180).<Boolean>with(VariantProperties.UV_LOCK, true)).select((Comparable)Direction.EAST, Half.TOP, StairsShape.STRAIGHT, Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, vk3).<VariantProperties.Rotation>with(VariantProperties.X_ROT, VariantProperties.Rotation.R180).<Boolean>with(VariantProperties.UV_LOCK, true)).select((Comparable)Direction.WEST, Half.TOP, StairsShape.STRAIGHT, Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, vk3).<VariantProperties.Rotation>with(VariantProperties.X_ROT, VariantProperties.Rotation.R180).<VariantProperties.Rotation>with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180).<Boolean>with(VariantProperties.UV_LOCK, true)).select((Comparable)Direction.SOUTH, Half.TOP, StairsShape.STRAIGHT, Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, vk3).<VariantProperties.Rotation>with(VariantProperties.X_ROT, VariantProperties.Rotation.R180).<VariantProperties.Rotation>with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90).<Boolean>with(VariantProperties.UV_LOCK, true)).select((Comparable)Direction.NORTH, Half.TOP, StairsShape.STRAIGHT, Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, vk3).<VariantProperties.Rotation>with(VariantProperties.X_ROT, VariantProperties.Rotation.R180).<VariantProperties.Rotation>with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270).<Boolean>with(VariantProperties.UV_LOCK, true)).select((Comparable)Direction.EAST, Half.TOP, StairsShape.OUTER_RIGHT, Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, vk4).<VariantProperties.Rotation>with(VariantProperties.X_ROT, VariantProperties.Rotation.R180).<VariantProperties.Rotation>with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90).<Boolean>with(VariantProperties.UV_LOCK, true)).select((Comparable)Direction.WEST, Half.TOP, StairsShape.OUTER_RIGHT, Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, vk4).<VariantProperties.Rotation>with(VariantProperties.X_ROT, VariantProperties.Rotation.R180).<VariantProperties.Rotation>with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270).<Boolean>with(VariantProperties.UV_LOCK, true)).select((Comparable)Direction.SOUTH, Half.TOP, StairsShape.OUTER_RIGHT, Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, vk4).<VariantProperties.Rotation>with(VariantProperties.X_ROT, VariantProperties.Rotation.R180).<VariantProperties.Rotation>with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180).<Boolean>with(VariantProperties.UV_LOCK, true)).select((Comparable)Direction.NORTH, Half.TOP, StairsShape.OUTER_RIGHT, Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, vk4).<VariantProperties.Rotation>with(VariantProperties.X_ROT, VariantProperties.Rotation.R180).<Boolean>with(VariantProperties.UV_LOCK, true)).select((Comparable)Direction.EAST, Half.TOP, StairsShape.OUTER_LEFT, Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, vk4).<VariantProperties.Rotation>with(VariantProperties.X_ROT, VariantProperties.Rotation.R180).<Boolean>with(VariantProperties.UV_LOCK, true)).select((Comparable)Direction.WEST, Half.TOP, StairsShape.OUTER_LEFT, Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, vk4).<VariantProperties.Rotation>with(VariantProperties.X_ROT, VariantProperties.Rotation.R180).<VariantProperties.Rotation>with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180).<Boolean>with(VariantProperties.UV_LOCK, true)).select((Comparable)Direction.SOUTH, Half.TOP, StairsShape.OUTER_LEFT, Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, vk4).<VariantProperties.Rotation>with(VariantProperties.X_ROT, VariantProperties.Rotation.R180).<VariantProperties.Rotation>with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90).<Boolean>with(VariantProperties.UV_LOCK, true)).select((Comparable)Direction.NORTH, Half.TOP, StairsShape.OUTER_LEFT, Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, vk4).<VariantProperties.Rotation>with(VariantProperties.X_ROT, VariantProperties.Rotation.R180).<VariantProperties.Rotation>with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270).<Boolean>with(VariantProperties.UV_LOCK, true)).select((Comparable)Direction.EAST, Half.TOP, StairsShape.INNER_RIGHT, Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, vk2).<VariantProperties.Rotation>with(VariantProperties.X_ROT, VariantProperties.Rotation.R180).<VariantProperties.Rotation>with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90).<Boolean>with(VariantProperties.UV_LOCK, true)).select((Comparable)Direction.WEST, Half.TOP, StairsShape.INNER_RIGHT, Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, vk2).<VariantProperties.Rotation>with(VariantProperties.X_ROT, VariantProperties.Rotation.R180).<VariantProperties.Rotation>with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270).<Boolean>with(VariantProperties.UV_LOCK, true)).select((Comparable)Direction.SOUTH, Half.TOP, StairsShape.INNER_RIGHT, Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, vk2).<VariantProperties.Rotation>with(VariantProperties.X_ROT, VariantProperties.Rotation.R180).<VariantProperties.Rotation>with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180).<Boolean>with(VariantProperties.UV_LOCK, true)).select((Comparable)Direction.NORTH, Half.TOP, StairsShape.INNER_RIGHT, Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, vk2).<VariantProperties.Rotation>with(VariantProperties.X_ROT, VariantProperties.Rotation.R180).<Boolean>with(VariantProperties.UV_LOCK, true)).select((Comparable)Direction.EAST, Half.TOP, StairsShape.INNER_LEFT, Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, vk2).<VariantProperties.Rotation>with(VariantProperties.X_ROT, VariantProperties.Rotation.R180).<Boolean>with(VariantProperties.UV_LOCK, true)).select((Comparable)Direction.WEST, Half.TOP, StairsShape.INNER_LEFT, Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, vk2).<VariantProperties.Rotation>with(VariantProperties.X_ROT, VariantProperties.Rotation.R180).<VariantProperties.Rotation>with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180).<Boolean>with(VariantProperties.UV_LOCK, true)).select((Comparable)Direction.SOUTH, Half.TOP, StairsShape.INNER_LEFT, Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, vk2).<VariantProperties.Rotation>with(VariantProperties.X_ROT, VariantProperties.Rotation.R180).<VariantProperties.Rotation>with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90).<Boolean>with(VariantProperties.UV_LOCK, true)).select((Comparable)Direction.NORTH, Half.TOP, StairsShape.INNER_LEFT, Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, vk2).<VariantProperties.Rotation>with(VariantProperties.X_ROT, VariantProperties.Rotation.R180).<VariantProperties.Rotation>with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270).<Boolean>with(VariantProperties.UV_LOCK, true)));
    }
    
    private static BlockStateGenerator createOrientableTrapdoor(final Block bul, final ResourceLocation vk2, final ResourceLocation vk3, final ResourceLocation vk4) {
        return MultiVariantGenerator.multiVariant(bul).with(PropertyDispatch.<Comparable, Half, Comparable>properties((Property<Comparable>)BlockStateProperties.HORIZONTAL_FACING, BlockStateProperties.HALF, (Property<Comparable>)BlockStateProperties.OPEN).select((Comparable)Direction.NORTH, Half.BOTTOM, (Comparable)false, Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, vk3)).select((Comparable)Direction.SOUTH, Half.BOTTOM, (Comparable)false, Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, vk3).<VariantProperties.Rotation>with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180)).select((Comparable)Direction.EAST, Half.BOTTOM, (Comparable)false, Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, vk3).<VariantProperties.Rotation>with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90)).select((Comparable)Direction.WEST, Half.BOTTOM, (Comparable)false, Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, vk3).<VariantProperties.Rotation>with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270)).select((Comparable)Direction.NORTH, Half.TOP, (Comparable)false, Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, vk2)).select((Comparable)Direction.SOUTH, Half.TOP, (Comparable)false, Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, vk2).<VariantProperties.Rotation>with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180)).select((Comparable)Direction.EAST, Half.TOP, (Comparable)false, Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, vk2).<VariantProperties.Rotation>with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90)).select((Comparable)Direction.WEST, Half.TOP, (Comparable)false, Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, vk2).<VariantProperties.Rotation>with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270)).select((Comparable)Direction.NORTH, Half.BOTTOM, (Comparable)true, Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, vk4)).select((Comparable)Direction.SOUTH, Half.BOTTOM, (Comparable)true, Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, vk4).<VariantProperties.Rotation>with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180)).select((Comparable)Direction.EAST, Half.BOTTOM, (Comparable)true, Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, vk4).<VariantProperties.Rotation>with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90)).select((Comparable)Direction.WEST, Half.BOTTOM, (Comparable)true, Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, vk4).<VariantProperties.Rotation>with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270)).select((Comparable)Direction.NORTH, Half.TOP, (Comparable)true, Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, vk4).<VariantProperties.Rotation>with(VariantProperties.X_ROT, VariantProperties.Rotation.R180).<VariantProperties.Rotation>with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180)).select((Comparable)Direction.SOUTH, Half.TOP, (Comparable)true, Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, vk4).<VariantProperties.Rotation>with(VariantProperties.X_ROT, VariantProperties.Rotation.R180).<VariantProperties.Rotation>with(VariantProperties.Y_ROT, VariantProperties.Rotation.R0)).select((Comparable)Direction.EAST, Half.TOP, (Comparable)true, Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, vk4).<VariantProperties.Rotation>with(VariantProperties.X_ROT, VariantProperties.Rotation.R180).<VariantProperties.Rotation>with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270)).select((Comparable)Direction.WEST, Half.TOP, (Comparable)true, Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, vk4).<VariantProperties.Rotation>with(VariantProperties.X_ROT, VariantProperties.Rotation.R180).<VariantProperties.Rotation>with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90)));
    }
    
    private static BlockStateGenerator createTrapdoor(final Block bul, final ResourceLocation vk2, final ResourceLocation vk3, final ResourceLocation vk4) {
        return MultiVariantGenerator.multiVariant(bul).with(PropertyDispatch.<Comparable, Half, Comparable>properties((Property<Comparable>)BlockStateProperties.HORIZONTAL_FACING, BlockStateProperties.HALF, (Property<Comparable>)BlockStateProperties.OPEN).select((Comparable)Direction.NORTH, Half.BOTTOM, (Comparable)false, Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, vk3)).select((Comparable)Direction.SOUTH, Half.BOTTOM, (Comparable)false, Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, vk3)).select((Comparable)Direction.EAST, Half.BOTTOM, (Comparable)false, Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, vk3)).select((Comparable)Direction.WEST, Half.BOTTOM, (Comparable)false, Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, vk3)).select((Comparable)Direction.NORTH, Half.TOP, (Comparable)false, Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, vk2)).select((Comparable)Direction.SOUTH, Half.TOP, (Comparable)false, Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, vk2)).select((Comparable)Direction.EAST, Half.TOP, (Comparable)false, Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, vk2)).select((Comparable)Direction.WEST, Half.TOP, (Comparable)false, Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, vk2)).select((Comparable)Direction.NORTH, Half.BOTTOM, (Comparable)true, Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, vk4)).select((Comparable)Direction.SOUTH, Half.BOTTOM, (Comparable)true, Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, vk4).<VariantProperties.Rotation>with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180)).select((Comparable)Direction.EAST, Half.BOTTOM, (Comparable)true, Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, vk4).<VariantProperties.Rotation>with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90)).select((Comparable)Direction.WEST, Half.BOTTOM, (Comparable)true, Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, vk4).<VariantProperties.Rotation>with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270)).select((Comparable)Direction.NORTH, Half.TOP, (Comparable)true, Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, vk4)).select((Comparable)Direction.SOUTH, Half.TOP, (Comparable)true, Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, vk4).<VariantProperties.Rotation>with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180)).select((Comparable)Direction.EAST, Half.TOP, (Comparable)true, Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, vk4).<VariantProperties.Rotation>with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90)).select((Comparable)Direction.WEST, Half.TOP, (Comparable)true, Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, vk4).<VariantProperties.Rotation>with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270)));
    }
    
    private static MultiVariantGenerator createSimpleBlock(final Block bul, final ResourceLocation vk) {
        return MultiVariantGenerator.multiVariant(bul, Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, vk));
    }
    
    private static PropertyDispatch createRotatedPillar() {
        return PropertyDispatch.<Direction.Axis>property(BlockStateProperties.AXIS).select(Direction.Axis.Y, Variant.variant()).select(Direction.Axis.Z, Variant.variant().<VariantProperties.Rotation>with(VariantProperties.X_ROT, VariantProperties.Rotation.R90)).select(Direction.Axis.X, Variant.variant().<VariantProperties.Rotation>with(VariantProperties.X_ROT, VariantProperties.Rotation.R90).<VariantProperties.Rotation>with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90));
    }
    
    private static BlockStateGenerator createAxisAlignedPillarBlock(final Block bul, final ResourceLocation vk) {
        return MultiVariantGenerator.multiVariant(bul, Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, vk)).with(createRotatedPillar());
    }
    
    private void createAxisAlignedPillarBlockCustomModel(final Block bul, final ResourceLocation vk) {
        this.blockStateOutput.accept(createAxisAlignedPillarBlock(bul, vk));
    }
    
    private void createAxisAlignedPillarBlock(final Block bul, final TexturedModel.Provider a) {
        final ResourceLocation vk4 = a.create(bul, this.modelOutput);
        this.blockStateOutput.accept(createAxisAlignedPillarBlock(bul, vk4));
    }
    
    private void createHorizontallyRotatedBlock(final Block bul, final TexturedModel.Provider a) {
        final ResourceLocation vk4 = a.create(bul, this.modelOutput);
        this.blockStateOutput.accept(MultiVariantGenerator.multiVariant(bul, Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, vk4)).with(createHorizontalFacingDispatch()));
    }
    
    private static BlockStateGenerator createRotatedPillarWithHorizontalVariant(final Block bul, final ResourceLocation vk2, final ResourceLocation vk3) {
        return MultiVariantGenerator.multiVariant(bul).with(PropertyDispatch.<Direction.Axis>property(BlockStateProperties.AXIS).select(Direction.Axis.Y, Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, vk2)).select(Direction.Axis.Z, Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, vk3).<VariantProperties.Rotation>with(VariantProperties.X_ROT, VariantProperties.Rotation.R90)).select(Direction.Axis.X, Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, vk3).<VariantProperties.Rotation>with(VariantProperties.X_ROT, VariantProperties.Rotation.R90).<VariantProperties.Rotation>with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90)));
    }
    
    private void createRotatedPillarWithHorizontalVariant(final Block bul, final TexturedModel.Provider a2, final TexturedModel.Provider a3) {
        final ResourceLocation vk5 = a2.create(bul, this.modelOutput);
        final ResourceLocation vk6 = a3.create(bul, this.modelOutput);
        this.blockStateOutput.accept(createRotatedPillarWithHorizontalVariant(bul, vk5, vk6));
    }
    
    private ResourceLocation createSuffixedVariant(final Block bul, final String string, final ModelTemplate ix, final Function<ResourceLocation, TextureMapping> function) {
        return ix.createWithSuffix(bul, string, (TextureMapping)function.apply(TextureMapping.getBlockTexture(bul, string)), this.modelOutput);
    }
    
    private static BlockStateGenerator createPressurePlate(final Block bul, final ResourceLocation vk2, final ResourceLocation vk3) {
        return MultiVariantGenerator.multiVariant(bul).with(createBooleanModelDispatch(BlockStateProperties.POWERED, vk3, vk2));
    }
    
    private static BlockStateGenerator createSlab(final Block bul, final ResourceLocation vk2, final ResourceLocation vk3, final ResourceLocation vk4) {
        return MultiVariantGenerator.multiVariant(bul).with(PropertyDispatch.<SlabType>property(BlockStateProperties.SLAB_TYPE).select(SlabType.BOTTOM, Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, vk2)).select(SlabType.TOP, Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, vk3)).select(SlabType.DOUBLE, Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, vk4)));
    }
    
    private void createTrivialCube(final Block bul) {
        this.createTrivialBlock(bul, TexturedModel.CUBE);
    }
    
    private void createTrivialBlock(final Block bul, final TexturedModel.Provider a) {
        this.blockStateOutput.accept(createSimpleBlock(bul, a.create(bul, this.modelOutput)));
    }
    
    private void createTrivialBlock(final Block bul, final TextureMapping iz, final ModelTemplate ix) {
        final ResourceLocation vk5 = ix.create(bul, iz, this.modelOutput);
        this.blockStateOutput.accept(createSimpleBlock(bul, vk5));
    }
    
    private BlockFamilyProvider family(final Block bul, final TexturedModel jb) {
        return new BlockFamilyProvider(jb.getMapping()).fullBlock(bul, jb.getTemplate());
    }
    
    private BlockFamilyProvider family(final Block bul, final TexturedModel.Provider a) {
        final TexturedModel jb4 = a.get(bul);
        return new BlockFamilyProvider(jb4.getMapping()).fullBlock(bul, jb4.getTemplate());
    }
    
    private BlockFamilyProvider family(final Block bul) {
        return this.family(bul, TexturedModel.CUBE);
    }
    
    private BlockFamilyProvider family(final TextureMapping iz) {
        return new BlockFamilyProvider(iz);
    }
    
    private void createDoor(final Block bul) {
        final TextureMapping iz3 = TextureMapping.door(bul);
        final ResourceLocation vk4 = ModelTemplates.DOOR_BOTTOM.create(bul, iz3, this.modelOutput);
        final ResourceLocation vk5 = ModelTemplates.DOOR_BOTTOM_HINGE.create(bul, iz3, this.modelOutput);
        final ResourceLocation vk6 = ModelTemplates.DOOR_TOP.create(bul, iz3, this.modelOutput);
        final ResourceLocation vk7 = ModelTemplates.DOOR_TOP_HINGE.create(bul, iz3, this.modelOutput);
        this.createSimpleFlatItemModel(bul.asItem());
        this.blockStateOutput.accept(createDoor(bul, vk4, vk5, vk6, vk7));
    }
    
    private void createOrientableTrapdoor(final Block bul) {
        final TextureMapping iz3 = TextureMapping.defaultTexture(bul);
        final ResourceLocation vk4 = ModelTemplates.ORIENTABLE_TRAPDOOR_TOP.create(bul, iz3, this.modelOutput);
        final ResourceLocation vk5 = ModelTemplates.ORIENTABLE_TRAPDOOR_BOTTOM.create(bul, iz3, this.modelOutput);
        final ResourceLocation vk6 = ModelTemplates.ORIENTABLE_TRAPDOOR_OPEN.create(bul, iz3, this.modelOutput);
        this.blockStateOutput.accept(createOrientableTrapdoor(bul, vk4, vk5, vk6));
        this.delegateItemModel(bul, vk5);
    }
    
    private void createTrapdoor(final Block bul) {
        final TextureMapping iz3 = TextureMapping.defaultTexture(bul);
        final ResourceLocation vk4 = ModelTemplates.TRAPDOOR_TOP.create(bul, iz3, this.modelOutput);
        final ResourceLocation vk5 = ModelTemplates.TRAPDOOR_BOTTOM.create(bul, iz3, this.modelOutput);
        final ResourceLocation vk6 = ModelTemplates.TRAPDOOR_OPEN.create(bul, iz3, this.modelOutput);
        this.blockStateOutput.accept(createTrapdoor(bul, vk4, vk5, vk6));
        this.delegateItemModel(bul, vk5);
    }
    
    private WoodProvider woodProvider(final Block bul) {
        return new WoodProvider(TextureMapping.logColumn(bul));
    }
    
    private void createNonTemplateModelBlock(final Block bul) {
        this.createNonTemplateModelBlock(bul, bul);
    }
    
    private void createNonTemplateModelBlock(final Block bul1, final Block bul2) {
        this.blockStateOutput.accept(createSimpleBlock(bul1, ModelLocationUtils.getModelLocation(bul2)));
    }
    
    private void createCrossBlockWithDefaultItem(final Block bul, final TintState c) {
        this.createSimpleFlatItemModel(bul);
        this.createCrossBlock(bul, c);
    }
    
    private void createCrossBlockWithDefaultItem(final Block bul, final TintState c, final TextureMapping iz) {
        this.createSimpleFlatItemModel(bul);
        this.createCrossBlock(bul, c, iz);
    }
    
    private void createCrossBlock(final Block bul, final TintState c) {
        final TextureMapping iz4 = TextureMapping.cross(bul);
        this.createCrossBlock(bul, c, iz4);
    }
    
    private void createCrossBlock(final Block bul, final TintState c, final TextureMapping iz) {
        final ResourceLocation vk5 = c.getCross().create(bul, iz, this.modelOutput);
        this.blockStateOutput.accept(createSimpleBlock(bul, vk5));
    }
    
    private void createPlant(final Block bul1, final Block bul2, final TintState c) {
        this.createCrossBlockWithDefaultItem(bul1, c);
        final TextureMapping iz5 = TextureMapping.plant(bul1);
        final ResourceLocation vk6 = c.getCrossPot().create(bul2, iz5, this.modelOutput);
        this.blockStateOutput.accept(createSimpleBlock(bul2, vk6));
    }
    
    private void createCoralFans(final Block bul1, final Block bul2) {
        final TexturedModel jb4 = TexturedModel.CORAL_FAN.get(bul1);
        final ResourceLocation vk5 = jb4.create(bul1, this.modelOutput);
        this.blockStateOutput.accept(createSimpleBlock(bul1, vk5));
        final ResourceLocation vk6 = ModelTemplates.CORAL_WALL_FAN.create(bul2, jb4.getMapping(), this.modelOutput);
        this.blockStateOutput.accept(MultiVariantGenerator.multiVariant(bul2, Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, vk6)).with(createHorizontalFacingDispatch()));
        this.createSimpleFlatItemModel(bul1);
    }
    
    private void createStems(final Block bul1, final Block bul2) {
        this.createSimpleFlatItemModel(bul1.asItem());
        final TextureMapping iz4 = TextureMapping.stem(bul1);
        final TextureMapping iz5 = TextureMapping.attachedStem(bul1, bul2);
        final ResourceLocation vk6 = ModelTemplates.ATTACHED_STEM.create(bul2, iz5, this.modelOutput);
        this.blockStateOutput.accept(MultiVariantGenerator.multiVariant(bul2, Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, vk6)).with(PropertyDispatch.<Comparable>property((Property<Comparable>)BlockStateProperties.HORIZONTAL_FACING).select((Comparable)Direction.WEST, Variant.variant()).select((Comparable)Direction.SOUTH, Variant.variant().<VariantProperties.Rotation>with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270)).select((Comparable)Direction.NORTH, Variant.variant().<VariantProperties.Rotation>with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90)).select((Comparable)Direction.EAST, Variant.variant().<VariantProperties.Rotation>with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180))));
        this.blockStateOutput.accept(MultiVariantGenerator.multiVariant(bul1).with(PropertyDispatch.<Comparable>property((Property<Comparable>)BlockStateProperties.AGE_7).generate((java.util.function.Function<Comparable, Variant>)(integer -> Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, ModelTemplates.STEMS[integer].create(bul1, iz4, this.modelOutput))))));
    }
    
    private void createCoral(final Block bul1, final Block bul2, final Block bul3, final Block bul4, final Block bul5, final Block bul6, final Block bul7, final Block bul8) {
        this.createCrossBlockWithDefaultItem(bul1, TintState.NOT_TINTED);
        this.createCrossBlockWithDefaultItem(bul2, TintState.NOT_TINTED);
        this.createTrivialCube(bul3);
        this.createTrivialCube(bul4);
        this.createCoralFans(bul5, bul7);
        this.createCoralFans(bul6, bul8);
    }
    
    private void createDoublePlant(final Block bul, final TintState c) {
        this.createSimpleFlatItemModel(bul, "_top");
        final ResourceLocation vk4 = this.createSuffixedVariant(bul, "_top", c.getCross(), (Function<ResourceLocation, TextureMapping>)TextureMapping::cross);
        final ResourceLocation vk5 = this.createSuffixedVariant(bul, "_bottom", c.getCross(), (Function<ResourceLocation, TextureMapping>)TextureMapping::cross);
        this.createDoubleBlock(bul, vk4, vk5);
    }
    
    private void createSunflower() {
        this.createSimpleFlatItemModel(Blocks.SUNFLOWER, "_front");
        final ResourceLocation vk2 = ModelLocationUtils.getModelLocation(Blocks.SUNFLOWER, "_top");
        final ResourceLocation vk3 = this.createSuffixedVariant(Blocks.SUNFLOWER, "_bottom", TintState.NOT_TINTED.getCross(), (Function<ResourceLocation, TextureMapping>)TextureMapping::cross);
        this.createDoubleBlock(Blocks.SUNFLOWER, vk2, vk3);
    }
    
    private void createTallSeagrass() {
        final ResourceLocation vk2 = this.createSuffixedVariant(Blocks.TALL_SEAGRASS, "_top", ModelTemplates.SEAGRASS, (Function<ResourceLocation, TextureMapping>)TextureMapping::defaultTexture);
        final ResourceLocation vk3 = this.createSuffixedVariant(Blocks.TALL_SEAGRASS, "_bottom", ModelTemplates.SEAGRASS, (Function<ResourceLocation, TextureMapping>)TextureMapping::defaultTexture);
        this.createDoubleBlock(Blocks.TALL_SEAGRASS, vk2, vk3);
    }
    
    private void createDoubleBlock(final Block bul, final ResourceLocation vk2, final ResourceLocation vk3) {
        this.blockStateOutput.accept(MultiVariantGenerator.multiVariant(bul).with(PropertyDispatch.<DoubleBlockHalf>property(BlockStateProperties.DOUBLE_BLOCK_HALF).select(DoubleBlockHalf.LOWER, Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, vk3)).select(DoubleBlockHalf.UPPER, Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, vk2))));
    }
    
    private void createPassiveRail(final Block bul) {
        final TextureMapping iz3 = TextureMapping.rail(bul);
        final TextureMapping iz4 = TextureMapping.rail(TextureMapping.getBlockTexture(bul, "_corner"));
        final ResourceLocation vk5 = ModelTemplates.RAIL_FLAT.create(bul, iz3, this.modelOutput);
        final ResourceLocation vk6 = ModelTemplates.RAIL_CURVED.create(bul, iz4, this.modelOutput);
        final ResourceLocation vk7 = ModelTemplates.RAIL_RAISED_NE.create(bul, iz3, this.modelOutput);
        final ResourceLocation vk8 = ModelTemplates.RAIL_RAISED_SW.create(bul, iz3, this.modelOutput);
        this.createSimpleFlatItemModel(bul);
        this.blockStateOutput.accept(MultiVariantGenerator.multiVariant(bul).with(PropertyDispatch.<RailShape>property(BlockStateProperties.RAIL_SHAPE).select(RailShape.NORTH_SOUTH, Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, vk5)).select(RailShape.EAST_WEST, Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, vk5).<VariantProperties.Rotation>with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90)).select(RailShape.ASCENDING_EAST, Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, vk7).<VariantProperties.Rotation>with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90)).select(RailShape.ASCENDING_WEST, Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, vk8).<VariantProperties.Rotation>with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90)).select(RailShape.ASCENDING_NORTH, Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, vk7)).select(RailShape.ASCENDING_SOUTH, Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, vk8)).select(RailShape.SOUTH_EAST, Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, vk6)).select(RailShape.SOUTH_WEST, Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, vk6).<VariantProperties.Rotation>with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90)).select(RailShape.NORTH_WEST, Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, vk6).<VariantProperties.Rotation>with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180)).select(RailShape.NORTH_EAST, Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, vk6).<VariantProperties.Rotation>with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270))));
    }
    
    private void createActiveRail(final Block bul) {
        final ResourceLocation vk3 = this.createSuffixedVariant(bul, "", ModelTemplates.RAIL_FLAT, (Function<ResourceLocation, TextureMapping>)TextureMapping::rail);
        final ResourceLocation vk4 = this.createSuffixedVariant(bul, "", ModelTemplates.RAIL_RAISED_NE, (Function<ResourceLocation, TextureMapping>)TextureMapping::rail);
        final ResourceLocation vk5 = this.createSuffixedVariant(bul, "", ModelTemplates.RAIL_RAISED_SW, (Function<ResourceLocation, TextureMapping>)TextureMapping::rail);
        final ResourceLocation vk6 = this.createSuffixedVariant(bul, "_on", ModelTemplates.RAIL_FLAT, (Function<ResourceLocation, TextureMapping>)TextureMapping::rail);
        final ResourceLocation vk7 = this.createSuffixedVariant(bul, "_on", ModelTemplates.RAIL_RAISED_NE, (Function<ResourceLocation, TextureMapping>)TextureMapping::rail);
        final ResourceLocation vk8 = this.createSuffixedVariant(bul, "_on", ModelTemplates.RAIL_RAISED_SW, (Function<ResourceLocation, TextureMapping>)TextureMapping::rail);
        final PropertyDispatch ip9 = PropertyDispatch.<Comparable, RailShape>properties((Property<Comparable>)BlockStateProperties.POWERED, BlockStateProperties.RAIL_SHAPE_STRAIGHT).generate((java.util.function.BiFunction<Comparable, RailShape, Variant>)((boolean7, cfh) -> {
            switch (cfh) {
                case NORTH_SOUTH: {
                    return Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, boolean7 ? vk6 : vk3);
                }
                case EAST_WEST: {
                    return Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, boolean7 ? vk6 : vk3).<VariantProperties.Rotation>with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90);
                }
                case ASCENDING_EAST: {
                    return Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, boolean7 ? vk7 : vk4).<VariantProperties.Rotation>with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90);
                }
                case ASCENDING_WEST: {
                    return Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, boolean7 ? vk8 : vk5).<VariantProperties.Rotation>with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90);
                }
                case ASCENDING_NORTH: {
                    return Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, boolean7 ? vk7 : vk4);
                }
                case ASCENDING_SOUTH: {
                    return Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, boolean7 ? vk8 : vk5);
                }
                default: {
                    throw new UnsupportedOperationException("Fix you generator!");
                }
            }
        }));
        this.createSimpleFlatItemModel(bul);
        this.blockStateOutput.accept(MultiVariantGenerator.multiVariant(bul).with(ip9));
    }
    
    private BlockEntityModelGenerator blockEntityModels(final ResourceLocation vk, final Block bul) {
        return new BlockEntityModelGenerator(vk, bul);
    }
    
    private BlockEntityModelGenerator blockEntityModels(final Block bul1, final Block bul2) {
        return new BlockEntityModelGenerator(ModelLocationUtils.getModelLocation(bul1), bul2);
    }
    
    private void createAirLikeBlock(final Block bul, final Item blu) {
        final ResourceLocation vk4 = ModelTemplates.PARTICLE_ONLY.create(bul, TextureMapping.particleFromItem(blu), this.modelOutput);
        this.blockStateOutput.accept(createSimpleBlock(bul, vk4));
    }
    
    private void createAirLikeBlock(final Block bul, final ResourceLocation vk) {
        final ResourceLocation vk2 = ModelTemplates.PARTICLE_ONLY.create(bul, TextureMapping.particle(vk), this.modelOutput);
        this.blockStateOutput.accept(createSimpleBlock(bul, vk2));
    }
    
    private void createWoolBlocks(final Block bul1, final Block bul2) {
        this.createTrivialBlock(bul1, TexturedModel.CUBE);
        final ResourceLocation vk4 = TexturedModel.CARPET.get(bul1).create(bul2, this.modelOutput);
        this.blockStateOutput.accept(createSimpleBlock(bul2, vk4));
    }
    
    private void createColoredBlockWithRandomRotations(final TexturedModel.Provider a, final Block... arr) {
        for (final Block bul7 : arr) {
            final ResourceLocation vk8 = a.create(bul7, this.modelOutput);
            this.blockStateOutput.accept(createRotatedVariant(bul7, vk8));
        }
    }
    
    private void createColoredBlockWithStateRotations(final TexturedModel.Provider a, final Block... arr) {
        for (final Block bul7 : arr) {
            final ResourceLocation vk8 = a.create(bul7, this.modelOutput);
            this.blockStateOutput.accept(MultiVariantGenerator.multiVariant(bul7, Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, vk8)).with(createHorizontalFacingDispatchAlt()));
        }
    }
    
    private void createGlassBlocks(final Block bul1, final Block bul2) {
        this.createTrivialCube(bul1);
        final TextureMapping iz4 = TextureMapping.pane(bul1, bul2);
        final ResourceLocation vk5 = ModelTemplates.STAINED_GLASS_PANE_POST.create(bul2, iz4, this.modelOutput);
        final ResourceLocation vk6 = ModelTemplates.STAINED_GLASS_PANE_SIDE.create(bul2, iz4, this.modelOutput);
        final ResourceLocation vk7 = ModelTemplates.STAINED_GLASS_PANE_SIDE_ALT.create(bul2, iz4, this.modelOutput);
        final ResourceLocation vk8 = ModelTemplates.STAINED_GLASS_PANE_NOSIDE.create(bul2, iz4, this.modelOutput);
        final ResourceLocation vk9 = ModelTemplates.STAINED_GLASS_PANE_NOSIDE_ALT.create(bul2, iz4, this.modelOutput);
        final Item blu10 = bul2.asItem();
        ModelTemplates.FLAT_ITEM.create(ModelLocationUtils.getModelLocation(blu10), TextureMapping.layer0(bul1), this.modelOutput);
        this.blockStateOutput.accept(MultiPartGenerator.multiPart(bul2).with(Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, vk5)).with(Condition.condition().<Boolean>term(BlockStateProperties.NORTH, true), Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, vk6)).with(Condition.condition().<Boolean>term(BlockStateProperties.EAST, true), Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, vk6).<VariantProperties.Rotation>with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90)).with(Condition.condition().<Boolean>term(BlockStateProperties.SOUTH, true), Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, vk7)).with(Condition.condition().<Boolean>term(BlockStateProperties.WEST, true), Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, vk7).<VariantProperties.Rotation>with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90)).with(Condition.condition().<Boolean>term(BlockStateProperties.NORTH, false), Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, vk8)).with(Condition.condition().<Boolean>term(BlockStateProperties.EAST, false), Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, vk9)).with(Condition.condition().<Boolean>term(BlockStateProperties.SOUTH, false), Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, vk9).<VariantProperties.Rotation>with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90)).with(Condition.condition().<Boolean>term(BlockStateProperties.WEST, false), Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, vk8).<VariantProperties.Rotation>with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270)));
    }
    
    private void createCommandBlock(final Block bul) {
        final TextureMapping iz3 = TextureMapping.commandBlock(bul);
        final ResourceLocation vk4 = ModelTemplates.COMMAND_BLOCK.create(bul, iz3, this.modelOutput);
        final ResourceLocation vk5 = this.createSuffixedVariant(bul, "_conditional", ModelTemplates.COMMAND_BLOCK, (Function<ResourceLocation, TextureMapping>)(vk -> iz3.copyAndUpdate(TextureSlot.SIDE, vk)));
        this.blockStateOutput.accept(MultiVariantGenerator.multiVariant(bul).with(createBooleanModelDispatch(BlockStateProperties.CONDITIONAL, vk5, vk4)).with(createFacingDispatch()));
    }
    
    private void createAnvil(final Block bul) {
        final ResourceLocation vk3 = TexturedModel.ANVIL.create(bul, this.modelOutput);
        this.blockStateOutput.accept(createSimpleBlock(bul, vk3).with(createHorizontalFacingDispatchAlt()));
    }
    
    private List<Variant> createBambooModels(final int integer) {
        final String string3 = new StringBuilder().append("_age").append(integer).toString();
        return (List<Variant>)IntStream.range(1, 5).mapToObj(integer -> Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, ModelLocationUtils.getModelLocation(Blocks.BAMBOO, integer + string3))).collect(Collectors.toList());
    }
    
    private void createBamboo() {
        this.skipAutoItemBlock(Blocks.BAMBOO);
        this.blockStateOutput.accept(MultiPartGenerator.multiPart(Blocks.BAMBOO).with(Condition.condition().<Integer>term(BlockStateProperties.AGE_1, 0), this.createBambooModels(0)).with(Condition.condition().<Integer>term(BlockStateProperties.AGE_1, 1), this.createBambooModels(1)).with(Condition.condition().<BambooLeaves>term(BlockStateProperties.BAMBOO_LEAVES, BambooLeaves.SMALL), Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, ModelLocationUtils.getModelLocation(Blocks.BAMBOO, "_small_leaves"))).with(Condition.condition().<BambooLeaves>term(BlockStateProperties.BAMBOO_LEAVES, BambooLeaves.LARGE), Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, ModelLocationUtils.getModelLocation(Blocks.BAMBOO, "_large_leaves"))));
    }
    
    private PropertyDispatch createColumnWithFacing() {
        return PropertyDispatch.<Comparable>property((Property<Comparable>)BlockStateProperties.FACING).select((Comparable)Direction.DOWN, Variant.variant().<VariantProperties.Rotation>with(VariantProperties.X_ROT, VariantProperties.Rotation.R180)).select((Comparable)Direction.UP, Variant.variant()).select((Comparable)Direction.NORTH, Variant.variant().<VariantProperties.Rotation>with(VariantProperties.X_ROT, VariantProperties.Rotation.R90)).select((Comparable)Direction.SOUTH, Variant.variant().<VariantProperties.Rotation>with(VariantProperties.X_ROT, VariantProperties.Rotation.R90).<VariantProperties.Rotation>with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180)).select((Comparable)Direction.WEST, Variant.variant().<VariantProperties.Rotation>with(VariantProperties.X_ROT, VariantProperties.Rotation.R90).<VariantProperties.Rotation>with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270)).select((Comparable)Direction.EAST, Variant.variant().<VariantProperties.Rotation>with(VariantProperties.X_ROT, VariantProperties.Rotation.R90).<VariantProperties.Rotation>with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90));
    }
    
    private void createBarrel() {
        final ResourceLocation vk2 = TextureMapping.getBlockTexture(Blocks.BARREL, "_top_open");
        this.blockStateOutput.accept(MultiVariantGenerator.multiVariant(Blocks.BARREL).with(this.createColumnWithFacing()).with(PropertyDispatch.<Comparable>property((Property<Comparable>)BlockStateProperties.OPEN).select((Comparable)false, Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, TexturedModel.CUBE_TOP_BOTTOM.create(Blocks.BARREL, this.modelOutput))).select((Comparable)true, Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, TexturedModel.CUBE_TOP_BOTTOM.get(Blocks.BARREL).updateTextures((Consumer<TextureMapping>)(iz -> iz.put(TextureSlot.TOP, vk2))).createWithSuffix(Blocks.BARREL, "_open", this.modelOutput)))));
    }
    
    private static <T extends Comparable<T>> PropertyDispatch createEmptyOrFullDispatch(final Property<T> cfg, final T comparable, final ResourceLocation vk3, final ResourceLocation vk4) {
        final Variant ir5 = Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, vk3);
        final Variant ir6 = Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, vk4);
        return PropertyDispatch.<T>property(cfg).generate((java.util.function.Function<T, Variant>)(comparable4 -> {
            final boolean boolean5 = comparable4.compareTo(comparable) >= 0;
            return boolean5 ? ir5 : ir6;
        }));
    }
    
    private void createBeeNest(final Block bul, final Function<Block, TextureMapping> function) {
        final TextureMapping iz4 = ((TextureMapping)function.apply(bul)).copyForced(TextureSlot.SIDE, TextureSlot.PARTICLE);
        final TextureMapping iz5 = iz4.copyAndUpdate(TextureSlot.FRONT, TextureMapping.getBlockTexture(bul, "_front_honey"));
        final ResourceLocation vk6 = ModelTemplates.CUBE_ORIENTABLE_TOP_BOTTOM.create(bul, iz4, this.modelOutput);
        final ResourceLocation vk7 = ModelTemplates.CUBE_ORIENTABLE_TOP_BOTTOM.createWithSuffix(bul, "_honey", iz5, this.modelOutput);
        this.blockStateOutput.accept(MultiVariantGenerator.multiVariant(bul).with(createHorizontalFacingDispatch()).with(BlockModelGenerators.<Integer>createEmptyOrFullDispatch(BlockStateProperties.LEVEL_HONEY, 5, vk7, vk6)));
    }
    
    private void createCropBlock(final Block bul, final Property<Integer> cfg, final int... arr) {
        if (cfg.getPossibleValues().size() != arr.length) {
            throw new IllegalArgumentException();
        }
        final Int2ObjectMap<ResourceLocation> int2ObjectMap5 = (Int2ObjectMap<ResourceLocation>)new Int2ObjectOpenHashMap();
        final PropertyDispatch ip6 = PropertyDispatch.<Integer>property(cfg).generate((java.util.function.Function<Integer, Variant>)(integer -> {
            final int integer2 = arr[integer];
            final ResourceLocation vk7 = (ResourceLocation)int2ObjectMap5.computeIfAbsent(integer2, integer3 -> this.createSuffixedVariant(bul, new StringBuilder().append("_stage").append(integer2).toString(), ModelTemplates.CROP, (Function<ResourceLocation, TextureMapping>)TextureMapping::crop));
            return Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, vk7);
        }));
        this.createSimpleFlatItemModel(bul.asItem());
        this.blockStateOutput.accept(MultiVariantGenerator.multiVariant(bul).with(ip6));
    }
    
    private void createBell() {
        final ResourceLocation vk2 = ModelLocationUtils.getModelLocation(Blocks.BELL, "_floor");
        final ResourceLocation vk3 = ModelLocationUtils.getModelLocation(Blocks.BELL, "_ceiling");
        final ResourceLocation vk4 = ModelLocationUtils.getModelLocation(Blocks.BELL, "_wall");
        final ResourceLocation vk5 = ModelLocationUtils.getModelLocation(Blocks.BELL, "_between_walls");
        this.createSimpleFlatItemModel(Items.BELL);
        this.blockStateOutput.accept(MultiVariantGenerator.multiVariant(Blocks.BELL).with(PropertyDispatch.<Comparable, BellAttachType>properties((Property<Comparable>)BlockStateProperties.HORIZONTAL_FACING, BlockStateProperties.BELL_ATTACHMENT).select((Comparable)Direction.NORTH, BellAttachType.FLOOR, Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, vk2)).select((Comparable)Direction.SOUTH, BellAttachType.FLOOR, Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, vk2).<VariantProperties.Rotation>with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180)).select((Comparable)Direction.EAST, BellAttachType.FLOOR, Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, vk2).<VariantProperties.Rotation>with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90)).select((Comparable)Direction.WEST, BellAttachType.FLOOR, Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, vk2).<VariantProperties.Rotation>with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270)).select((Comparable)Direction.NORTH, BellAttachType.CEILING, Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, vk3)).select((Comparable)Direction.SOUTH, BellAttachType.CEILING, Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, vk3).<VariantProperties.Rotation>with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180)).select((Comparable)Direction.EAST, BellAttachType.CEILING, Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, vk3).<VariantProperties.Rotation>with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90)).select((Comparable)Direction.WEST, BellAttachType.CEILING, Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, vk3).<VariantProperties.Rotation>with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270)).select((Comparable)Direction.NORTH, BellAttachType.SINGLE_WALL, Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, vk4).<VariantProperties.Rotation>with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270)).select((Comparable)Direction.SOUTH, BellAttachType.SINGLE_WALL, Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, vk4).<VariantProperties.Rotation>with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90)).select((Comparable)Direction.EAST, BellAttachType.SINGLE_WALL, Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, vk4)).select((Comparable)Direction.WEST, BellAttachType.SINGLE_WALL, Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, vk4).<VariantProperties.Rotation>with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180)).select((Comparable)Direction.SOUTH, BellAttachType.DOUBLE_WALL, Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, vk5).<VariantProperties.Rotation>with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90)).select((Comparable)Direction.NORTH, BellAttachType.DOUBLE_WALL, Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, vk5).<VariantProperties.Rotation>with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270)).select((Comparable)Direction.EAST, BellAttachType.DOUBLE_WALL, Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, vk5)).select((Comparable)Direction.WEST, BellAttachType.DOUBLE_WALL, Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, vk5).<VariantProperties.Rotation>with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180))));
    }
    
    private void createGrindstone() {
        this.blockStateOutput.accept(MultiVariantGenerator.multiVariant(Blocks.GRINDSTONE, Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, ModelLocationUtils.getModelLocation(Blocks.GRINDSTONE))).with(PropertyDispatch.<AttachFace, Comparable>properties(BlockStateProperties.ATTACH_FACE, (Property<Comparable>)BlockStateProperties.HORIZONTAL_FACING).select(AttachFace.FLOOR, (Comparable)Direction.NORTH, Variant.variant()).select(AttachFace.FLOOR, (Comparable)Direction.EAST, Variant.variant().<VariantProperties.Rotation>with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90)).select(AttachFace.FLOOR, (Comparable)Direction.SOUTH, Variant.variant().<VariantProperties.Rotation>with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180)).select(AttachFace.FLOOR, (Comparable)Direction.WEST, Variant.variant().<VariantProperties.Rotation>with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270)).select(AttachFace.WALL, (Comparable)Direction.NORTH, Variant.variant().<VariantProperties.Rotation>with(VariantProperties.X_ROT, VariantProperties.Rotation.R90)).select(AttachFace.WALL, (Comparable)Direction.EAST, Variant.variant().<VariantProperties.Rotation>with(VariantProperties.X_ROT, VariantProperties.Rotation.R90).<VariantProperties.Rotation>with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90)).select(AttachFace.WALL, (Comparable)Direction.SOUTH, Variant.variant().<VariantProperties.Rotation>with(VariantProperties.X_ROT, VariantProperties.Rotation.R90).<VariantProperties.Rotation>with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180)).select(AttachFace.WALL, (Comparable)Direction.WEST, Variant.variant().<VariantProperties.Rotation>with(VariantProperties.X_ROT, VariantProperties.Rotation.R90).<VariantProperties.Rotation>with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270)).select(AttachFace.CEILING, (Comparable)Direction.SOUTH, Variant.variant().<VariantProperties.Rotation>with(VariantProperties.X_ROT, VariantProperties.Rotation.R180)).select(AttachFace.CEILING, (Comparable)Direction.WEST, Variant.variant().<VariantProperties.Rotation>with(VariantProperties.X_ROT, VariantProperties.Rotation.R180).<VariantProperties.Rotation>with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90)).select(AttachFace.CEILING, (Comparable)Direction.NORTH, Variant.variant().<VariantProperties.Rotation>with(VariantProperties.X_ROT, VariantProperties.Rotation.R180).<VariantProperties.Rotation>with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180)).select(AttachFace.CEILING, (Comparable)Direction.EAST, Variant.variant().<VariantProperties.Rotation>with(VariantProperties.X_ROT, VariantProperties.Rotation.R180).<VariantProperties.Rotation>with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270))));
    }
    
    private void createFurnace(final Block bul, final TexturedModel.Provider a) {
        final ResourceLocation vk4 = a.create(bul, this.modelOutput);
        final ResourceLocation vk5 = TextureMapping.getBlockTexture(bul, "_front_on");
        final ResourceLocation vk6 = a.get(bul).updateTextures((Consumer<TextureMapping>)(iz -> iz.put(TextureSlot.FRONT, vk5))).createWithSuffix(bul, "_on", this.modelOutput);
        this.blockStateOutput.accept(MultiVariantGenerator.multiVariant(bul).with(createBooleanModelDispatch(BlockStateProperties.LIT, vk6, vk4)).with(createHorizontalFacingDispatch()));
    }
    
    private void createCampfires(final Block... arr) {
        final ResourceLocation vk3 = ModelLocationUtils.decorateBlockModelLocation("campfire_off");
        for (final Block bul7 : arr) {
            final ResourceLocation vk4 = ModelTemplates.CAMPFIRE.create(bul7, TextureMapping.campfire(bul7), this.modelOutput);
            this.createSimpleFlatItemModel(bul7.asItem());
            this.blockStateOutput.accept(MultiVariantGenerator.multiVariant(bul7).with(createBooleanModelDispatch(BlockStateProperties.LIT, vk4, vk3)).with(createHorizontalFacingDispatchAlt()));
        }
    }
    
    private void createBookshelf() {
        final TextureMapping iz2 = TextureMapping.column(TextureMapping.getBlockTexture(Blocks.BOOKSHELF), TextureMapping.getBlockTexture(Blocks.OAK_PLANKS));
        final ResourceLocation vk3 = ModelTemplates.CUBE_COLUMN.create(Blocks.BOOKSHELF, iz2, this.modelOutput);
        this.blockStateOutput.accept(createSimpleBlock(Blocks.BOOKSHELF, vk3));
    }
    
    private void createRedstoneWire() {
        this.createSimpleFlatItemModel(Items.REDSTONE);
        this.blockStateOutput.accept(MultiPartGenerator.multiPart(Blocks.REDSTONE_WIRE).with(Condition.or(Condition.condition().<RedstoneSide>term(BlockStateProperties.NORTH_REDSTONE, RedstoneSide.NONE).<RedstoneSide>term(BlockStateProperties.EAST_REDSTONE, RedstoneSide.NONE).<RedstoneSide>term(BlockStateProperties.SOUTH_REDSTONE, RedstoneSide.NONE).<RedstoneSide>term(BlockStateProperties.WEST_REDSTONE, RedstoneSide.NONE), Condition.condition().<RedstoneSide>term(BlockStateProperties.NORTH_REDSTONE, RedstoneSide.SIDE, RedstoneSide.UP).<RedstoneSide>term(BlockStateProperties.EAST_REDSTONE, RedstoneSide.SIDE, RedstoneSide.UP), Condition.condition().<RedstoneSide>term(BlockStateProperties.EAST_REDSTONE, RedstoneSide.SIDE, RedstoneSide.UP).<RedstoneSide>term(BlockStateProperties.SOUTH_REDSTONE, RedstoneSide.SIDE, RedstoneSide.UP), Condition.condition().<RedstoneSide>term(BlockStateProperties.SOUTH_REDSTONE, RedstoneSide.SIDE, RedstoneSide.UP).<RedstoneSide>term(BlockStateProperties.WEST_REDSTONE, RedstoneSide.SIDE, RedstoneSide.UP), Condition.condition().<RedstoneSide>term(BlockStateProperties.WEST_REDSTONE, RedstoneSide.SIDE, RedstoneSide.UP).<RedstoneSide>term(BlockStateProperties.NORTH_REDSTONE, RedstoneSide.SIDE, RedstoneSide.UP)), Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, ModelLocationUtils.decorateBlockModelLocation("redstone_dust_dot"))).with(Condition.condition().<RedstoneSide>term(BlockStateProperties.NORTH_REDSTONE, RedstoneSide.SIDE, RedstoneSide.UP), Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, ModelLocationUtils.decorateBlockModelLocation("redstone_dust_side0"))).with(Condition.condition().<RedstoneSide>term(BlockStateProperties.SOUTH_REDSTONE, RedstoneSide.SIDE, RedstoneSide.UP), Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, ModelLocationUtils.decorateBlockModelLocation("redstone_dust_side_alt0"))).with(Condition.condition().<RedstoneSide>term(BlockStateProperties.EAST_REDSTONE, RedstoneSide.SIDE, RedstoneSide.UP), Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, ModelLocationUtils.decorateBlockModelLocation("redstone_dust_side_alt1")).<VariantProperties.Rotation>with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270)).with(Condition.condition().<RedstoneSide>term(BlockStateProperties.WEST_REDSTONE, RedstoneSide.SIDE, RedstoneSide.UP), Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, ModelLocationUtils.decorateBlockModelLocation("redstone_dust_side1")).<VariantProperties.Rotation>with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270)).with(Condition.condition().<RedstoneSide>term(BlockStateProperties.NORTH_REDSTONE, RedstoneSide.UP), Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, ModelLocationUtils.decorateBlockModelLocation("redstone_dust_up"))).with(Condition.condition().<RedstoneSide>term(BlockStateProperties.EAST_REDSTONE, RedstoneSide.UP), Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, ModelLocationUtils.decorateBlockModelLocation("redstone_dust_up")).<VariantProperties.Rotation>with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90)).with(Condition.condition().<RedstoneSide>term(BlockStateProperties.SOUTH_REDSTONE, RedstoneSide.UP), Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, ModelLocationUtils.decorateBlockModelLocation("redstone_dust_up")).<VariantProperties.Rotation>with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180)).with(Condition.condition().<RedstoneSide>term(BlockStateProperties.WEST_REDSTONE, RedstoneSide.UP), Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, ModelLocationUtils.decorateBlockModelLocation("redstone_dust_up")).<VariantProperties.Rotation>with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270)));
    }
    
    private void createComparator() {
        this.createSimpleFlatItemModel(Items.COMPARATOR);
        this.blockStateOutput.accept(MultiVariantGenerator.multiVariant(Blocks.COMPARATOR).with(createHorizontalFacingDispatchAlt()).with(PropertyDispatch.<ComparatorMode, Comparable>properties(BlockStateProperties.MODE_COMPARATOR, (Property<Comparable>)BlockStateProperties.POWERED).select(ComparatorMode.COMPARE, (Comparable)false, Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, ModelLocationUtils.getModelLocation(Blocks.COMPARATOR))).select(ComparatorMode.COMPARE, (Comparable)true, Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, ModelLocationUtils.getModelLocation(Blocks.COMPARATOR, "_on"))).select(ComparatorMode.SUBTRACT, (Comparable)false, Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, ModelLocationUtils.getModelLocation(Blocks.COMPARATOR, "_subtract"))).select(ComparatorMode.SUBTRACT, (Comparable)true, Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, ModelLocationUtils.getModelLocation(Blocks.COMPARATOR, "_on_subtract")))));
    }
    
    private void createSmoothStoneSlab() {
        final TextureMapping iz2 = TextureMapping.cube(Blocks.SMOOTH_STONE);
        final TextureMapping iz3 = TextureMapping.column(TextureMapping.getBlockTexture(Blocks.SMOOTH_STONE_SLAB, "_side"), iz2.get(TextureSlot.TOP));
        final ResourceLocation vk4 = ModelTemplates.SLAB_BOTTOM.create(Blocks.SMOOTH_STONE_SLAB, iz3, this.modelOutput);
        final ResourceLocation vk5 = ModelTemplates.SLAB_TOP.create(Blocks.SMOOTH_STONE_SLAB, iz3, this.modelOutput);
        final ResourceLocation vk6 = ModelTemplates.CUBE_COLUMN.createWithOverride(Blocks.SMOOTH_STONE_SLAB, "_double", iz3, this.modelOutput);
        this.blockStateOutput.accept(createSlab(Blocks.SMOOTH_STONE_SLAB, vk4, vk5, vk6));
        this.blockStateOutput.accept(createSimpleBlock(Blocks.SMOOTH_STONE, ModelTemplates.CUBE_ALL.create(Blocks.SMOOTH_STONE, iz2, this.modelOutput)));
    }
    
    private void createBrewingStand() {
        this.createSimpleFlatItemModel(Items.BREWING_STAND);
        this.blockStateOutput.accept(MultiPartGenerator.multiPart(Blocks.BREWING_STAND).with(Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, TextureMapping.getBlockTexture(Blocks.BREWING_STAND))).with(Condition.condition().<Boolean>term(BlockStateProperties.HAS_BOTTLE_0, true), Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, TextureMapping.getBlockTexture(Blocks.BREWING_STAND, "_bottle0"))).with(Condition.condition().<Boolean>term(BlockStateProperties.HAS_BOTTLE_1, true), Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, TextureMapping.getBlockTexture(Blocks.BREWING_STAND, "_bottle1"))).with(Condition.condition().<Boolean>term(BlockStateProperties.HAS_BOTTLE_2, true), Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, TextureMapping.getBlockTexture(Blocks.BREWING_STAND, "_bottle2"))).with(Condition.condition().<Boolean>term(BlockStateProperties.HAS_BOTTLE_0, false), Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, TextureMapping.getBlockTexture(Blocks.BREWING_STAND, "_empty0"))).with(Condition.condition().<Boolean>term(BlockStateProperties.HAS_BOTTLE_1, false), Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, TextureMapping.getBlockTexture(Blocks.BREWING_STAND, "_empty1"))).with(Condition.condition().<Boolean>term(BlockStateProperties.HAS_BOTTLE_2, false), Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, TextureMapping.getBlockTexture(Blocks.BREWING_STAND, "_empty2"))));
    }
    
    private void createMushroomBlock(final Block bul) {
        final ResourceLocation vk3 = ModelTemplates.SINGLE_FACE.create(bul, TextureMapping.defaultTexture(bul), this.modelOutput);
        final ResourceLocation vk4 = ModelLocationUtils.decorateBlockModelLocation("mushroom_block_inside");
        this.blockStateOutput.accept(MultiPartGenerator.multiPart(bul).with(Condition.condition().<Boolean>term(BlockStateProperties.NORTH, true), Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, vk3)).with(Condition.condition().<Boolean>term(BlockStateProperties.EAST, true), Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, vk3).<VariantProperties.Rotation>with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90).<Boolean>with(VariantProperties.UV_LOCK, true)).with(Condition.condition().<Boolean>term(BlockStateProperties.SOUTH, true), Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, vk3).<VariantProperties.Rotation>with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180).<Boolean>with(VariantProperties.UV_LOCK, true)).with(Condition.condition().<Boolean>term(BlockStateProperties.WEST, true), Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, vk3).<VariantProperties.Rotation>with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270).<Boolean>with(VariantProperties.UV_LOCK, true)).with(Condition.condition().<Boolean>term(BlockStateProperties.UP, true), Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, vk3).<VariantProperties.Rotation>with(VariantProperties.X_ROT, VariantProperties.Rotation.R270).<Boolean>with(VariantProperties.UV_LOCK, true)).with(Condition.condition().<Boolean>term(BlockStateProperties.DOWN, true), Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, vk3).<VariantProperties.Rotation>with(VariantProperties.X_ROT, VariantProperties.Rotation.R90).<Boolean>with(VariantProperties.UV_LOCK, true)).with(Condition.condition().<Boolean>term(BlockStateProperties.NORTH, false), Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, vk4)).with(Condition.condition().<Boolean>term(BlockStateProperties.EAST, false), Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, vk4).<VariantProperties.Rotation>with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90).<Boolean>with(VariantProperties.UV_LOCK, false)).with(Condition.condition().<Boolean>term(BlockStateProperties.SOUTH, false), Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, vk4).<VariantProperties.Rotation>with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180).<Boolean>with(VariantProperties.UV_LOCK, false)).with(Condition.condition().<Boolean>term(BlockStateProperties.WEST, false), Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, vk4).<VariantProperties.Rotation>with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270).<Boolean>with(VariantProperties.UV_LOCK, false)).with(Condition.condition().<Boolean>term(BlockStateProperties.UP, false), Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, vk4).<VariantProperties.Rotation>with(VariantProperties.X_ROT, VariantProperties.Rotation.R270).<Boolean>with(VariantProperties.UV_LOCK, false)).with(Condition.condition().<Boolean>term(BlockStateProperties.DOWN, false), Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, vk4).<VariantProperties.Rotation>with(VariantProperties.X_ROT, VariantProperties.Rotation.R90).<Boolean>with(VariantProperties.UV_LOCK, false)));
        this.delegateItemModel(bul, TexturedModel.CUBE.createWithSuffix(bul, "_inventory", this.modelOutput));
    }
    
    private void createCakeBlock() {
        this.createSimpleFlatItemModel(Items.CAKE);
        this.blockStateOutput.accept(MultiVariantGenerator.multiVariant(Blocks.CAKE).with(PropertyDispatch.<Comparable>property((Property<Comparable>)BlockStateProperties.BITES).select((Comparable)0, Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, ModelLocationUtils.getModelLocation(Blocks.CAKE))).select((Comparable)1, Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, ModelLocationUtils.getModelLocation(Blocks.CAKE, "_slice1"))).select((Comparable)2, Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, ModelLocationUtils.getModelLocation(Blocks.CAKE, "_slice2"))).select((Comparable)3, Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, ModelLocationUtils.getModelLocation(Blocks.CAKE, "_slice3"))).select((Comparable)4, Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, ModelLocationUtils.getModelLocation(Blocks.CAKE, "_slice4"))).select((Comparable)5, Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, ModelLocationUtils.getModelLocation(Blocks.CAKE, "_slice5"))).select((Comparable)6, Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, ModelLocationUtils.getModelLocation(Blocks.CAKE, "_slice6")))));
    }
    
    private void createCartographyTable() {
        final TextureMapping iz2 = new TextureMapping().put(TextureSlot.PARTICLE, TextureMapping.getBlockTexture(Blocks.CARTOGRAPHY_TABLE, "_side3")).put(TextureSlot.DOWN, TextureMapping.getBlockTexture(Blocks.DARK_OAK_PLANKS)).put(TextureSlot.UP, TextureMapping.getBlockTexture(Blocks.CARTOGRAPHY_TABLE, "_top")).put(TextureSlot.NORTH, TextureMapping.getBlockTexture(Blocks.CARTOGRAPHY_TABLE, "_side3")).put(TextureSlot.EAST, TextureMapping.getBlockTexture(Blocks.CARTOGRAPHY_TABLE, "_side3")).put(TextureSlot.SOUTH, TextureMapping.getBlockTexture(Blocks.CARTOGRAPHY_TABLE, "_side1")).put(TextureSlot.WEST, TextureMapping.getBlockTexture(Blocks.CARTOGRAPHY_TABLE, "_side2"));
        this.blockStateOutput.accept(createSimpleBlock(Blocks.CARTOGRAPHY_TABLE, ModelTemplates.CUBE.create(Blocks.CARTOGRAPHY_TABLE, iz2, this.modelOutput)));
    }
    
    private void createSmithingTable() {
        final TextureMapping iz2 = new TextureMapping().put(TextureSlot.PARTICLE, TextureMapping.getBlockTexture(Blocks.SMITHING_TABLE, "_front")).put(TextureSlot.DOWN, TextureMapping.getBlockTexture(Blocks.SMITHING_TABLE, "_bottom")).put(TextureSlot.UP, TextureMapping.getBlockTexture(Blocks.SMITHING_TABLE, "_top")).put(TextureSlot.NORTH, TextureMapping.getBlockTexture(Blocks.SMITHING_TABLE, "_front")).put(TextureSlot.SOUTH, TextureMapping.getBlockTexture(Blocks.SMITHING_TABLE, "_front")).put(TextureSlot.EAST, TextureMapping.getBlockTexture(Blocks.SMITHING_TABLE, "_side")).put(TextureSlot.WEST, TextureMapping.getBlockTexture(Blocks.SMITHING_TABLE, "_side"));
        this.blockStateOutput.accept(createSimpleBlock(Blocks.SMITHING_TABLE, ModelTemplates.CUBE.create(Blocks.SMITHING_TABLE, iz2, this.modelOutput)));
    }
    
    private void createCraftingTableLike(final Block bul1, final Block bul2, final BiFunction<Block, Block, TextureMapping> biFunction) {
        final TextureMapping iz5 = (TextureMapping)biFunction.apply(bul1, bul2);
        this.blockStateOutput.accept(createSimpleBlock(bul1, ModelTemplates.CUBE.create(bul1, iz5, this.modelOutput)));
    }
    
    private void createPumpkins() {
        final TextureMapping iz2 = TextureMapping.column(Blocks.PUMPKIN);
        this.blockStateOutput.accept(createSimpleBlock(Blocks.PUMPKIN, ModelLocationUtils.getModelLocation(Blocks.PUMPKIN)));
        this.createPumpkinVariant(Blocks.CARVED_PUMPKIN, iz2);
        this.createPumpkinVariant(Blocks.JACK_O_LANTERN, iz2);
    }
    
    private void createPumpkinVariant(final Block bul, final TextureMapping iz) {
        final ResourceLocation vk4 = ModelTemplates.CUBE_ORIENTABLE.create(bul, iz.copyAndUpdate(TextureSlot.FRONT, TextureMapping.getBlockTexture(bul)), this.modelOutput);
        this.blockStateOutput.accept(MultiVariantGenerator.multiVariant(bul, Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, vk4)).with(createHorizontalFacingDispatch()));
    }
    
    private void createCauldron() {
        this.createSimpleFlatItemModel(Items.CAULDRON);
        this.blockStateOutput.accept(MultiVariantGenerator.multiVariant(Blocks.CAULDRON).with(PropertyDispatch.<Comparable>property((Property<Comparable>)BlockStateProperties.LEVEL_CAULDRON).select((Comparable)0, Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, ModelLocationUtils.getModelLocation(Blocks.CAULDRON))).select((Comparable)1, Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, ModelLocationUtils.getModelLocation(Blocks.CAULDRON, "_level1"))).select((Comparable)2, Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, ModelLocationUtils.getModelLocation(Blocks.CAULDRON, "_level2"))).select((Comparable)3, Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, ModelLocationUtils.getModelLocation(Blocks.CAULDRON, "_level3")))));
    }
    
    private void createChiseledSandsone(final Block bul1, final Block bul2) {
        final TextureMapping iz4 = new TextureMapping().put(TextureSlot.END, TextureMapping.getBlockTexture(bul2, "_top")).put(TextureSlot.SIDE, TextureMapping.getBlockTexture(bul1));
        this.createTrivialBlock(bul1, iz4, ModelTemplates.CUBE_COLUMN);
    }
    
    private void createChorusFlower() {
        final TextureMapping iz2 = TextureMapping.defaultTexture(Blocks.CHORUS_FLOWER);
        final ResourceLocation vk3 = ModelTemplates.CHORUS_FLOWER.create(Blocks.CHORUS_FLOWER, iz2, this.modelOutput);
        final ResourceLocation vk4 = this.createSuffixedVariant(Blocks.CHORUS_FLOWER, "_dead", ModelTemplates.CHORUS_FLOWER, (Function<ResourceLocation, TextureMapping>)(vk -> iz2.copyAndUpdate(TextureSlot.TEXTURE, vk)));
        this.blockStateOutput.accept(MultiVariantGenerator.multiVariant(Blocks.CHORUS_FLOWER).with(BlockModelGenerators.<Integer>createEmptyOrFullDispatch(BlockStateProperties.AGE_5, 5, vk4, vk3)));
    }
    
    private void createDispenserBlock(final Block bul) {
        final TextureMapping iz3 = new TextureMapping().put(TextureSlot.TOP, TextureMapping.getBlockTexture(Blocks.FURNACE, "_top")).put(TextureSlot.SIDE, TextureMapping.getBlockTexture(Blocks.FURNACE, "_side")).put(TextureSlot.FRONT, TextureMapping.getBlockTexture(bul, "_front"));
        final TextureMapping iz4 = new TextureMapping().put(TextureSlot.SIDE, TextureMapping.getBlockTexture(Blocks.FURNACE, "_top")).put(TextureSlot.FRONT, TextureMapping.getBlockTexture(bul, "_front_vertical"));
        final ResourceLocation vk5 = ModelTemplates.CUBE_ORIENTABLE.create(bul, iz3, this.modelOutput);
        final ResourceLocation vk6 = ModelTemplates.CUBE_ORIENTABLE_VERTICAL.create(bul, iz4, this.modelOutput);
        this.blockStateOutput.accept(MultiVariantGenerator.multiVariant(bul).with(PropertyDispatch.<Comparable>property((Property<Comparable>)BlockStateProperties.FACING).select((Comparable)Direction.DOWN, Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, vk6).<VariantProperties.Rotation>with(VariantProperties.X_ROT, VariantProperties.Rotation.R180)).select((Comparable)Direction.UP, Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, vk6)).select((Comparable)Direction.NORTH, Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, vk5)).select((Comparable)Direction.EAST, Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, vk5).<VariantProperties.Rotation>with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90)).select((Comparable)Direction.SOUTH, Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, vk5).<VariantProperties.Rotation>with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180)).select((Comparable)Direction.WEST, Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, vk5).<VariantProperties.Rotation>with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270))));
    }
    
    private void createEndPortalFrame() {
        final ResourceLocation vk2 = ModelLocationUtils.getModelLocation(Blocks.END_PORTAL_FRAME);
        final ResourceLocation vk3 = ModelLocationUtils.getModelLocation(Blocks.END_PORTAL_FRAME, "_filled");
        this.blockStateOutput.accept(MultiVariantGenerator.multiVariant(Blocks.END_PORTAL_FRAME).with(PropertyDispatch.<Comparable>property((Property<Comparable>)BlockStateProperties.EYE).select((Comparable)false, Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, vk2)).select((Comparable)true, Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, vk3))).with(createHorizontalFacingDispatchAlt()));
    }
    
    private void createChorusPlant() {
        final ResourceLocation vk2 = ModelLocationUtils.getModelLocation(Blocks.CHORUS_PLANT, "_side");
        final ResourceLocation vk3 = ModelLocationUtils.getModelLocation(Blocks.CHORUS_PLANT, "_noside");
        final ResourceLocation vk4 = ModelLocationUtils.getModelLocation(Blocks.CHORUS_PLANT, "_noside1");
        final ResourceLocation vk5 = ModelLocationUtils.getModelLocation(Blocks.CHORUS_PLANT, "_noside2");
        final ResourceLocation vk6 = ModelLocationUtils.getModelLocation(Blocks.CHORUS_PLANT, "_noside3");
        this.blockStateOutput.accept(MultiPartGenerator.multiPart(Blocks.CHORUS_PLANT).with(Condition.condition().<Boolean>term(BlockStateProperties.NORTH, true), Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, vk2)).with(Condition.condition().<Boolean>term(BlockStateProperties.EAST, true), Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, vk2).<VariantProperties.Rotation>with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90).<Boolean>with(VariantProperties.UV_LOCK, true)).with(Condition.condition().<Boolean>term(BlockStateProperties.SOUTH, true), Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, vk2).<VariantProperties.Rotation>with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180).<Boolean>with(VariantProperties.UV_LOCK, true)).with(Condition.condition().<Boolean>term(BlockStateProperties.WEST, true), Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, vk2).<VariantProperties.Rotation>with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270).<Boolean>with(VariantProperties.UV_LOCK, true)).with(Condition.condition().<Boolean>term(BlockStateProperties.UP, true), Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, vk2).<VariantProperties.Rotation>with(VariantProperties.X_ROT, VariantProperties.Rotation.R270).<Boolean>with(VariantProperties.UV_LOCK, true)).with(Condition.condition().<Boolean>term(BlockStateProperties.DOWN, true), Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, vk2).<VariantProperties.Rotation>with(VariantProperties.X_ROT, VariantProperties.Rotation.R90).<Boolean>with(VariantProperties.UV_LOCK, true)).with(Condition.condition().<Boolean>term(BlockStateProperties.NORTH, false), Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, vk3).<Integer>with(VariantProperties.WEIGHT, 2), Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, vk4), Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, vk5), Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, vk6)).with(Condition.condition().<Boolean>term(BlockStateProperties.EAST, false), Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, vk4).<VariantProperties.Rotation>with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90).<Boolean>with(VariantProperties.UV_LOCK, true), Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, vk5).<VariantProperties.Rotation>with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90).<Boolean>with(VariantProperties.UV_LOCK, true), Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, vk6).<VariantProperties.Rotation>with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90).<Boolean>with(VariantProperties.UV_LOCK, true), Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, vk3).<Integer>with(VariantProperties.WEIGHT, 2).<VariantProperties.Rotation>with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90).<Boolean>with(VariantProperties.UV_LOCK, true)).with(Condition.condition().<Boolean>term(BlockStateProperties.SOUTH, false), Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, vk5).<VariantProperties.Rotation>with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180).<Boolean>with(VariantProperties.UV_LOCK, true), Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, vk6).<VariantProperties.Rotation>with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180).<Boolean>with(VariantProperties.UV_LOCK, true), Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, vk3).<Integer>with(VariantProperties.WEIGHT, 2).<VariantProperties.Rotation>with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180).<Boolean>with(VariantProperties.UV_LOCK, true), Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, vk4).<VariantProperties.Rotation>with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180).<Boolean>with(VariantProperties.UV_LOCK, true)).with(Condition.condition().<Boolean>term(BlockStateProperties.WEST, false), Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, vk6).<VariantProperties.Rotation>with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270).<Boolean>with(VariantProperties.UV_LOCK, true), Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, vk3).<Integer>with(VariantProperties.WEIGHT, 2).<VariantProperties.Rotation>with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270).<Boolean>with(VariantProperties.UV_LOCK, true), Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, vk4).<VariantProperties.Rotation>with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270).<Boolean>with(VariantProperties.UV_LOCK, true), Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, vk5).<VariantProperties.Rotation>with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270).<Boolean>with(VariantProperties.UV_LOCK, true)).with(Condition.condition().<Boolean>term(BlockStateProperties.UP, false), Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, vk3).<Integer>with(VariantProperties.WEIGHT, 2).<VariantProperties.Rotation>with(VariantProperties.X_ROT, VariantProperties.Rotation.R270).<Boolean>with(VariantProperties.UV_LOCK, true), Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, vk6).<VariantProperties.Rotation>with(VariantProperties.X_ROT, VariantProperties.Rotation.R270).<Boolean>with(VariantProperties.UV_LOCK, true), Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, vk4).<VariantProperties.Rotation>with(VariantProperties.X_ROT, VariantProperties.Rotation.R270).<Boolean>with(VariantProperties.UV_LOCK, true), Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, vk5).<VariantProperties.Rotation>with(VariantProperties.X_ROT, VariantProperties.Rotation.R270).<Boolean>with(VariantProperties.UV_LOCK, true)).with(Condition.condition().<Boolean>term(BlockStateProperties.DOWN, false), Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, vk6).<VariantProperties.Rotation>with(VariantProperties.X_ROT, VariantProperties.Rotation.R90).<Boolean>with(VariantProperties.UV_LOCK, true), Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, vk5).<VariantProperties.Rotation>with(VariantProperties.X_ROT, VariantProperties.Rotation.R90).<Boolean>with(VariantProperties.UV_LOCK, true), Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, vk4).<VariantProperties.Rotation>with(VariantProperties.X_ROT, VariantProperties.Rotation.R90).<Boolean>with(VariantProperties.UV_LOCK, true), Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, vk3).<Integer>with(VariantProperties.WEIGHT, 2).<VariantProperties.Rotation>with(VariantProperties.X_ROT, VariantProperties.Rotation.R90).<Boolean>with(VariantProperties.UV_LOCK, true)));
    }
    
    private void createComposter() {
        this.blockStateOutput.accept(MultiPartGenerator.multiPart(Blocks.COMPOSTER).with(Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, TextureMapping.getBlockTexture(Blocks.COMPOSTER))).with(Condition.condition().<Integer>term(BlockStateProperties.LEVEL_COMPOSTER, 1), Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, TextureMapping.getBlockTexture(Blocks.COMPOSTER, "_contents1"))).with(Condition.condition().<Integer>term(BlockStateProperties.LEVEL_COMPOSTER, 2), Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, TextureMapping.getBlockTexture(Blocks.COMPOSTER, "_contents2"))).with(Condition.condition().<Integer>term(BlockStateProperties.LEVEL_COMPOSTER, 3), Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, TextureMapping.getBlockTexture(Blocks.COMPOSTER, "_contents3"))).with(Condition.condition().<Integer>term(BlockStateProperties.LEVEL_COMPOSTER, 4), Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, TextureMapping.getBlockTexture(Blocks.COMPOSTER, "_contents4"))).with(Condition.condition().<Integer>term(BlockStateProperties.LEVEL_COMPOSTER, 5), Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, TextureMapping.getBlockTexture(Blocks.COMPOSTER, "_contents5"))).with(Condition.condition().<Integer>term(BlockStateProperties.LEVEL_COMPOSTER, 6), Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, TextureMapping.getBlockTexture(Blocks.COMPOSTER, "_contents6"))).with(Condition.condition().<Integer>term(BlockStateProperties.LEVEL_COMPOSTER, 7), Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, TextureMapping.getBlockTexture(Blocks.COMPOSTER, "_contents7"))).with(Condition.condition().<Integer>term(BlockStateProperties.LEVEL_COMPOSTER, 8), Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, TextureMapping.getBlockTexture(Blocks.COMPOSTER, "_contents_ready"))));
    }
    
    private void createNyliumBlock(final Block bul) {
        final TextureMapping iz3 = new TextureMapping().put(TextureSlot.BOTTOM, TextureMapping.getBlockTexture(Blocks.NETHERRACK)).put(TextureSlot.TOP, TextureMapping.getBlockTexture(bul)).put(TextureSlot.SIDE, TextureMapping.getBlockTexture(bul, "_side"));
        this.blockStateOutput.accept(createSimpleBlock(bul, ModelTemplates.CUBE_BOTTOM_TOP.create(bul, iz3, this.modelOutput)));
    }
    
    private void createDaylightDetector() {
        final ResourceLocation vk2 = TextureMapping.getBlockTexture(Blocks.DAYLIGHT_DETECTOR, "_side");
        final TextureMapping iz3 = new TextureMapping().put(TextureSlot.TOP, TextureMapping.getBlockTexture(Blocks.DAYLIGHT_DETECTOR, "_top")).put(TextureSlot.SIDE, vk2);
        final TextureMapping iz4 = new TextureMapping().put(TextureSlot.TOP, TextureMapping.getBlockTexture(Blocks.DAYLIGHT_DETECTOR, "_inverted_top")).put(TextureSlot.SIDE, vk2);
        this.blockStateOutput.accept(MultiVariantGenerator.multiVariant(Blocks.DAYLIGHT_DETECTOR).with(PropertyDispatch.<Comparable>property((Property<Comparable>)BlockStateProperties.INVERTED).select((Comparable)false, Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, ModelTemplates.DAYLIGHT_DETECTOR.create(Blocks.DAYLIGHT_DETECTOR, iz3, this.modelOutput))).select((Comparable)true, Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, ModelTemplates.DAYLIGHT_DETECTOR.create(ModelLocationUtils.getModelLocation(Blocks.DAYLIGHT_DETECTOR, "_inverted"), iz4, this.modelOutput)))));
    }
    
    private void createRotatableColumn(final Block bul) {
        this.blockStateOutput.accept(MultiVariantGenerator.multiVariant(bul, Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, ModelLocationUtils.getModelLocation(bul))).with(this.createColumnWithFacing()));
    }
    
    private void createFarmland() {
        final TextureMapping iz2 = new TextureMapping().put(TextureSlot.DIRT, TextureMapping.getBlockTexture(Blocks.DIRT)).put(TextureSlot.TOP, TextureMapping.getBlockTexture(Blocks.FARMLAND));
        final TextureMapping iz3 = new TextureMapping().put(TextureSlot.DIRT, TextureMapping.getBlockTexture(Blocks.DIRT)).put(TextureSlot.TOP, TextureMapping.getBlockTexture(Blocks.FARMLAND, "_moist"));
        final ResourceLocation vk4 = ModelTemplates.FARMLAND.create(Blocks.FARMLAND, iz2, this.modelOutput);
        final ResourceLocation vk5 = ModelTemplates.FARMLAND.create(TextureMapping.getBlockTexture(Blocks.FARMLAND, "_moist"), iz3, this.modelOutput);
        this.blockStateOutput.accept(MultiVariantGenerator.multiVariant(Blocks.FARMLAND).with(BlockModelGenerators.<Integer>createEmptyOrFullDispatch(BlockStateProperties.MOISTURE, 7, vk5, vk4)));
    }
    
    private List<ResourceLocation> createFloorFireModels(final Block bul) {
        final ResourceLocation vk3 = ModelTemplates.FIRE_FLOOR.create(ModelLocationUtils.getModelLocation(bul, "_floor0"), TextureMapping.fire0(bul), this.modelOutput);
        final ResourceLocation vk4 = ModelTemplates.FIRE_FLOOR.create(ModelLocationUtils.getModelLocation(bul, "_floor1"), TextureMapping.fire1(bul), this.modelOutput);
        return (List<ResourceLocation>)ImmutableList.of(vk3, vk4);
    }
    
    private List<ResourceLocation> createSideFireModels(final Block bul) {
        final ResourceLocation vk3 = ModelTemplates.FIRE_SIDE.create(ModelLocationUtils.getModelLocation(bul, "_side0"), TextureMapping.fire0(bul), this.modelOutput);
        final ResourceLocation vk4 = ModelTemplates.FIRE_SIDE.create(ModelLocationUtils.getModelLocation(bul, "_side1"), TextureMapping.fire1(bul), this.modelOutput);
        final ResourceLocation vk5 = ModelTemplates.FIRE_SIDE_ALT.create(ModelLocationUtils.getModelLocation(bul, "_side_alt0"), TextureMapping.fire0(bul), this.modelOutput);
        final ResourceLocation vk6 = ModelTemplates.FIRE_SIDE_ALT.create(ModelLocationUtils.getModelLocation(bul, "_side_alt1"), TextureMapping.fire1(bul), this.modelOutput);
        return (List<ResourceLocation>)ImmutableList.of(vk3, vk4, vk5, vk6);
    }
    
    private List<ResourceLocation> createTopFireModels(final Block bul) {
        final ResourceLocation vk3 = ModelTemplates.FIRE_UP.create(ModelLocationUtils.getModelLocation(bul, "_up0"), TextureMapping.fire0(bul), this.modelOutput);
        final ResourceLocation vk4 = ModelTemplates.FIRE_UP.create(ModelLocationUtils.getModelLocation(bul, "_up1"), TextureMapping.fire1(bul), this.modelOutput);
        final ResourceLocation vk5 = ModelTemplates.FIRE_UP_ALT.create(ModelLocationUtils.getModelLocation(bul, "_up_alt0"), TextureMapping.fire0(bul), this.modelOutput);
        final ResourceLocation vk6 = ModelTemplates.FIRE_UP_ALT.create(ModelLocationUtils.getModelLocation(bul, "_up_alt1"), TextureMapping.fire1(bul), this.modelOutput);
        return (List<ResourceLocation>)ImmutableList.of(vk3, vk4, vk5, vk6);
    }
    
    private static List<Variant> wrapModels(final List<ResourceLocation> list, final UnaryOperator<Variant> unaryOperator) {
        return (List<Variant>)list.stream().map(vk -> Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, vk)).map((Function)unaryOperator).collect(Collectors.toList());
    }
    
    private void createFire() {
        final Condition im2 = Condition.condition().<Boolean>term(BlockStateProperties.NORTH, false).<Boolean>term(BlockStateProperties.EAST, false).<Boolean>term(BlockStateProperties.SOUTH, false).<Boolean>term(BlockStateProperties.WEST, false).<Boolean>term(BlockStateProperties.UP, false);
        final List<ResourceLocation> list3 = this.createFloorFireModels(Blocks.FIRE);
        final List<ResourceLocation> list4 = this.createSideFireModels(Blocks.FIRE);
        final List<ResourceLocation> list5 = this.createTopFireModels(Blocks.FIRE);
        this.blockStateOutput.accept(MultiPartGenerator.multiPart(Blocks.FIRE).with(im2, wrapModels(list3, (UnaryOperator<Variant>)(ir -> ir))).with(Condition.or(Condition.condition().<Boolean>term(BlockStateProperties.NORTH, true), im2), wrapModels(list4, (UnaryOperator<Variant>)(ir -> ir))).with(Condition.or(Condition.condition().<Boolean>term(BlockStateProperties.EAST, true), im2), wrapModels(list4, (UnaryOperator<Variant>)(ir -> ir.<VariantProperties.Rotation>with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90)))).with(Condition.or(Condition.condition().<Boolean>term(BlockStateProperties.SOUTH, true), im2), wrapModels(list4, (UnaryOperator<Variant>)(ir -> ir.<VariantProperties.Rotation>with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180)))).with(Condition.or(Condition.condition().<Boolean>term(BlockStateProperties.WEST, true), im2), wrapModels(list4, (UnaryOperator<Variant>)(ir -> ir.<VariantProperties.Rotation>with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270)))).with(Condition.condition().<Boolean>term(BlockStateProperties.UP, true), wrapModels(list5, (UnaryOperator<Variant>)(ir -> ir))));
    }
    
    private void createSoulFire() {
        final List<ResourceLocation> list2 = this.createFloorFireModels(Blocks.SOUL_FIRE);
        final List<ResourceLocation> list3 = this.createSideFireModels(Blocks.SOUL_FIRE);
        this.blockStateOutput.accept(MultiPartGenerator.multiPart(Blocks.SOUL_FIRE).with(wrapModels(list2, (UnaryOperator<Variant>)(ir -> ir))).with(wrapModels(list3, (UnaryOperator<Variant>)(ir -> ir))).with(wrapModels(list3, (UnaryOperator<Variant>)(ir -> ir.<VariantProperties.Rotation>with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90)))).with(wrapModels(list3, (UnaryOperator<Variant>)(ir -> ir.<VariantProperties.Rotation>with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180)))).with(wrapModels(list3, (UnaryOperator<Variant>)(ir -> ir.<VariantProperties.Rotation>with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270)))));
    }
    
    private void createLantern(final Block bul) {
        final ResourceLocation vk3 = TexturedModel.LANTERN.create(bul, this.modelOutput);
        final ResourceLocation vk4 = TexturedModel.HANGING_LANTERN.create(bul, this.modelOutput);
        this.createSimpleFlatItemModel(bul.asItem());
        this.blockStateOutput.accept(MultiVariantGenerator.multiVariant(bul).with(createBooleanModelDispatch(BlockStateProperties.HANGING, vk4, vk3)));
    }
    
    private void createFrostedIce() {
        this.blockStateOutput.accept(MultiVariantGenerator.multiVariant(Blocks.FROSTED_ICE).with(PropertyDispatch.<Comparable>property((Property<Comparable>)BlockStateProperties.AGE_3).select((Comparable)0, Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, this.createSuffixedVariant(Blocks.FROSTED_ICE, "_0", ModelTemplates.CUBE_ALL, (Function<ResourceLocation, TextureMapping>)TextureMapping::cube))).select((Comparable)1, Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, this.createSuffixedVariant(Blocks.FROSTED_ICE, "_1", ModelTemplates.CUBE_ALL, (Function<ResourceLocation, TextureMapping>)TextureMapping::cube))).select((Comparable)2, Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, this.createSuffixedVariant(Blocks.FROSTED_ICE, "_2", ModelTemplates.CUBE_ALL, (Function<ResourceLocation, TextureMapping>)TextureMapping::cube))).select((Comparable)3, Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, this.createSuffixedVariant(Blocks.FROSTED_ICE, "_3", ModelTemplates.CUBE_ALL, (Function<ResourceLocation, TextureMapping>)TextureMapping::cube)))));
    }
    
    private void createGrassBlocks() {
        final ResourceLocation vk2 = TextureMapping.getBlockTexture(Blocks.DIRT);
        final TextureMapping iz3 = new TextureMapping().put(TextureSlot.BOTTOM, vk2).copyForced(TextureSlot.BOTTOM, TextureSlot.PARTICLE).put(TextureSlot.TOP, TextureMapping.getBlockTexture(Blocks.GRASS_BLOCK, "_top")).put(TextureSlot.SIDE, TextureMapping.getBlockTexture(Blocks.GRASS_BLOCK, "_snow"));
        final Variant ir4 = Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, ModelTemplates.CUBE_BOTTOM_TOP.createWithSuffix(Blocks.GRASS_BLOCK, "_snow", iz3, this.modelOutput));
        this.createGrassLikeBlock(Blocks.GRASS_BLOCK, ModelLocationUtils.getModelLocation(Blocks.GRASS_BLOCK), ir4);
        final ResourceLocation vk3 = TexturedModel.CUBE_TOP_BOTTOM.get(Blocks.MYCELIUM).updateTextures((Consumer<TextureMapping>)(iz -> iz.put(TextureSlot.BOTTOM, vk2))).create(Blocks.MYCELIUM, this.modelOutput);
        this.createGrassLikeBlock(Blocks.MYCELIUM, vk3, ir4);
        final ResourceLocation vk4 = TexturedModel.CUBE_TOP_BOTTOM.get(Blocks.PODZOL).updateTextures((Consumer<TextureMapping>)(iz -> iz.put(TextureSlot.BOTTOM, vk2))).create(Blocks.PODZOL, this.modelOutput);
        this.createGrassLikeBlock(Blocks.PODZOL, vk4, ir4);
    }
    
    private void createGrassLikeBlock(final Block bul, final ResourceLocation vk, final Variant ir) {
        final List<Variant> list5 = (List<Variant>)Arrays.asList((Object[])createRotatedVariants(vk));
        this.blockStateOutput.accept(MultiVariantGenerator.multiVariant(bul).with(PropertyDispatch.<Comparable>property((Property<Comparable>)BlockStateProperties.SNOWY).select((Comparable)true, ir).select((Comparable)false, list5)));
    }
    
    private void createCocoa() {
        this.createSimpleFlatItemModel(Items.COCOA_BEANS);
        this.blockStateOutput.accept(MultiVariantGenerator.multiVariant(Blocks.COCOA).with(PropertyDispatch.<Comparable>property((Property<Comparable>)BlockStateProperties.AGE_2).select((Comparable)0, Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, ModelLocationUtils.getModelLocation(Blocks.COCOA, "_stage0"))).select((Comparable)1, Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, ModelLocationUtils.getModelLocation(Blocks.COCOA, "_stage1"))).select((Comparable)2, Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, ModelLocationUtils.getModelLocation(Blocks.COCOA, "_stage2")))).with(createHorizontalFacingDispatchAlt()));
    }
    
    private void createGrassPath() {
        this.blockStateOutput.accept(createRotatedVariant(Blocks.GRASS_PATH, ModelLocationUtils.getModelLocation(Blocks.GRASS_PATH)));
    }
    
    private void createWeightedPressurePlate(final Block bul1, final Block bul2) {
        final TextureMapping iz4 = TextureMapping.defaultTexture(bul2);
        final ResourceLocation vk5 = ModelTemplates.PRESSURE_PLATE_UP.create(bul1, iz4, this.modelOutput);
        final ResourceLocation vk6 = ModelTemplates.PRESSURE_PLATE_DOWN.create(bul1, iz4, this.modelOutput);
        this.blockStateOutput.accept(MultiVariantGenerator.multiVariant(bul1).with(BlockModelGenerators.<Integer>createEmptyOrFullDispatch(BlockStateProperties.POWER, 1, vk6, vk5)));
    }
    
    private void createHopper() {
        final ResourceLocation vk2 = ModelLocationUtils.getModelLocation(Blocks.HOPPER);
        final ResourceLocation vk3 = ModelLocationUtils.getModelLocation(Blocks.HOPPER, "_side");
        this.createSimpleFlatItemModel(Items.HOPPER);
        this.blockStateOutput.accept(MultiVariantGenerator.multiVariant(Blocks.HOPPER).with(PropertyDispatch.<Comparable>property((Property<Comparable>)BlockStateProperties.FACING_HOPPER).select((Comparable)Direction.DOWN, Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, vk2)).select((Comparable)Direction.NORTH, Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, vk3)).select((Comparable)Direction.EAST, Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, vk3).<VariantProperties.Rotation>with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90)).select((Comparable)Direction.SOUTH, Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, vk3).<VariantProperties.Rotation>with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180)).select((Comparable)Direction.WEST, Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, vk3).<VariantProperties.Rotation>with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270))));
    }
    
    private void copyModel(final Block bul1, final Block bul2) {
        final ResourceLocation vk4 = ModelLocationUtils.getModelLocation(bul1);
        this.blockStateOutput.accept(MultiVariantGenerator.multiVariant(bul2, Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, vk4)));
        this.delegateItemModel(bul2, vk4);
    }
    
    private void createIronBars() {
        final ResourceLocation vk2 = ModelLocationUtils.getModelLocation(Blocks.IRON_BARS, "_post_ends");
        final ResourceLocation vk3 = ModelLocationUtils.getModelLocation(Blocks.IRON_BARS, "_post");
        final ResourceLocation vk4 = ModelLocationUtils.getModelLocation(Blocks.IRON_BARS, "_cap");
        final ResourceLocation vk5 = ModelLocationUtils.getModelLocation(Blocks.IRON_BARS, "_cap_alt");
        final ResourceLocation vk6 = ModelLocationUtils.getModelLocation(Blocks.IRON_BARS, "_side");
        final ResourceLocation vk7 = ModelLocationUtils.getModelLocation(Blocks.IRON_BARS, "_side_alt");
        this.blockStateOutput.accept(MultiPartGenerator.multiPart(Blocks.IRON_BARS).with(Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, vk2)).with(Condition.condition().<Boolean>term(BlockStateProperties.NORTH, false).<Boolean>term(BlockStateProperties.EAST, false).<Boolean>term(BlockStateProperties.SOUTH, false).<Boolean>term(BlockStateProperties.WEST, false), Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, vk3)).with(Condition.condition().<Boolean>term(BlockStateProperties.NORTH, true).<Boolean>term(BlockStateProperties.EAST, false).<Boolean>term(BlockStateProperties.SOUTH, false).<Boolean>term(BlockStateProperties.WEST, false), Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, vk4)).with(Condition.condition().<Boolean>term(BlockStateProperties.NORTH, false).<Boolean>term(BlockStateProperties.EAST, true).<Boolean>term(BlockStateProperties.SOUTH, false).<Boolean>term(BlockStateProperties.WEST, false), Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, vk4).<VariantProperties.Rotation>with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90)).with(Condition.condition().<Boolean>term(BlockStateProperties.NORTH, false).<Boolean>term(BlockStateProperties.EAST, false).<Boolean>term(BlockStateProperties.SOUTH, true).<Boolean>term(BlockStateProperties.WEST, false), Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, vk5)).with(Condition.condition().<Boolean>term(BlockStateProperties.NORTH, false).<Boolean>term(BlockStateProperties.EAST, false).<Boolean>term(BlockStateProperties.SOUTH, false).<Boolean>term(BlockStateProperties.WEST, true), Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, vk5).<VariantProperties.Rotation>with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90)).with(Condition.condition().<Boolean>term(BlockStateProperties.NORTH, true), Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, vk6)).with(Condition.condition().<Boolean>term(BlockStateProperties.EAST, true), Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, vk6).<VariantProperties.Rotation>with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90)).with(Condition.condition().<Boolean>term(BlockStateProperties.SOUTH, true), Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, vk7)).with(Condition.condition().<Boolean>term(BlockStateProperties.WEST, true), Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, vk7).<VariantProperties.Rotation>with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90)));
        this.createSimpleFlatItemModel(Blocks.IRON_BARS);
    }
    
    private void createNonTemplateHorizontalBlock(final Block bul) {
        this.blockStateOutput.accept(MultiVariantGenerator.multiVariant(bul, Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, ModelLocationUtils.getModelLocation(bul))).with(createHorizontalFacingDispatch()));
    }
    
    private void createLever() {
        final ResourceLocation vk2 = ModelLocationUtils.getModelLocation(Blocks.LEVER);
        final ResourceLocation vk3 = ModelLocationUtils.getModelLocation(Blocks.LEVER, "_on");
        this.createSimpleFlatItemModel(Blocks.LEVER);
        this.blockStateOutput.accept(MultiVariantGenerator.multiVariant(Blocks.LEVER).with(createBooleanModelDispatch(BlockStateProperties.POWERED, vk2, vk3)).with(PropertyDispatch.<AttachFace, Comparable>properties(BlockStateProperties.ATTACH_FACE, (Property<Comparable>)BlockStateProperties.HORIZONTAL_FACING).select(AttachFace.CEILING, (Comparable)Direction.NORTH, Variant.variant().<VariantProperties.Rotation>with(VariantProperties.X_ROT, VariantProperties.Rotation.R180).<VariantProperties.Rotation>with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180)).select(AttachFace.CEILING, (Comparable)Direction.EAST, Variant.variant().<VariantProperties.Rotation>with(VariantProperties.X_ROT, VariantProperties.Rotation.R180).<VariantProperties.Rotation>with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270)).select(AttachFace.CEILING, (Comparable)Direction.SOUTH, Variant.variant().<VariantProperties.Rotation>with(VariantProperties.X_ROT, VariantProperties.Rotation.R180)).select(AttachFace.CEILING, (Comparable)Direction.WEST, Variant.variant().<VariantProperties.Rotation>with(VariantProperties.X_ROT, VariantProperties.Rotation.R180).<VariantProperties.Rotation>with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90)).select(AttachFace.FLOOR, (Comparable)Direction.NORTH, Variant.variant()).select(AttachFace.FLOOR, (Comparable)Direction.EAST, Variant.variant().<VariantProperties.Rotation>with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90)).select(AttachFace.FLOOR, (Comparable)Direction.SOUTH, Variant.variant().<VariantProperties.Rotation>with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180)).select(AttachFace.FLOOR, (Comparable)Direction.WEST, Variant.variant().<VariantProperties.Rotation>with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270)).select(AttachFace.WALL, (Comparable)Direction.NORTH, Variant.variant().<VariantProperties.Rotation>with(VariantProperties.X_ROT, VariantProperties.Rotation.R90)).select(AttachFace.WALL, (Comparable)Direction.EAST, Variant.variant().<VariantProperties.Rotation>with(VariantProperties.X_ROT, VariantProperties.Rotation.R90).<VariantProperties.Rotation>with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90)).select(AttachFace.WALL, (Comparable)Direction.SOUTH, Variant.variant().<VariantProperties.Rotation>with(VariantProperties.X_ROT, VariantProperties.Rotation.R90).<VariantProperties.Rotation>with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180)).select(AttachFace.WALL, (Comparable)Direction.WEST, Variant.variant().<VariantProperties.Rotation>with(VariantProperties.X_ROT, VariantProperties.Rotation.R90).<VariantProperties.Rotation>with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270))));
    }
    
    private void createLilyPad() {
        this.createSimpleFlatItemModel(Blocks.LILY_PAD);
        this.blockStateOutput.accept(createRotatedVariant(Blocks.LILY_PAD, ModelLocationUtils.getModelLocation(Blocks.LILY_PAD)));
    }
    
    private void createNetherPortalBlock() {
        this.blockStateOutput.accept(MultiVariantGenerator.multiVariant(Blocks.NETHER_PORTAL).with(PropertyDispatch.<Direction.Axis>property(BlockStateProperties.HORIZONTAL_AXIS).select(Direction.Axis.X, Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, ModelLocationUtils.getModelLocation(Blocks.NETHER_PORTAL, "_ns"))).select(Direction.Axis.Z, Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, ModelLocationUtils.getModelLocation(Blocks.NETHER_PORTAL, "_ew")))));
    }
    
    private void createNetherrack() {
        final ResourceLocation vk2 = TexturedModel.CUBE.create(Blocks.NETHERRACK, this.modelOutput);
        this.blockStateOutput.accept(MultiVariantGenerator.multiVariant(Blocks.NETHERRACK, Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, vk2), Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, vk2).<VariantProperties.Rotation>with(VariantProperties.X_ROT, VariantProperties.Rotation.R90), Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, vk2).<VariantProperties.Rotation>with(VariantProperties.X_ROT, VariantProperties.Rotation.R180), Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, vk2).<VariantProperties.Rotation>with(VariantProperties.X_ROT, VariantProperties.Rotation.R270), Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, vk2).<VariantProperties.Rotation>with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90), Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, vk2).<VariantProperties.Rotation>with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90).<VariantProperties.Rotation>with(VariantProperties.X_ROT, VariantProperties.Rotation.R90), Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, vk2).<VariantProperties.Rotation>with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90).<VariantProperties.Rotation>with(VariantProperties.X_ROT, VariantProperties.Rotation.R180), Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, vk2).<VariantProperties.Rotation>with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90).<VariantProperties.Rotation>with(VariantProperties.X_ROT, VariantProperties.Rotation.R270), Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, vk2).<VariantProperties.Rotation>with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180), Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, vk2).<VariantProperties.Rotation>with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180).<VariantProperties.Rotation>with(VariantProperties.X_ROT, VariantProperties.Rotation.R90), Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, vk2).<VariantProperties.Rotation>with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180).<VariantProperties.Rotation>with(VariantProperties.X_ROT, VariantProperties.Rotation.R180), Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, vk2).<VariantProperties.Rotation>with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180).<VariantProperties.Rotation>with(VariantProperties.X_ROT, VariantProperties.Rotation.R270), Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, vk2).<VariantProperties.Rotation>with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270), Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, vk2).<VariantProperties.Rotation>with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270).<VariantProperties.Rotation>with(VariantProperties.X_ROT, VariantProperties.Rotation.R90), Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, vk2).<VariantProperties.Rotation>with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270).<VariantProperties.Rotation>with(VariantProperties.X_ROT, VariantProperties.Rotation.R180), Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, vk2).<VariantProperties.Rotation>with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270).<VariantProperties.Rotation>with(VariantProperties.X_ROT, VariantProperties.Rotation.R270)));
    }
    
    private void createObserver() {
        final ResourceLocation vk2 = ModelLocationUtils.getModelLocation(Blocks.OBSERVER);
        final ResourceLocation vk3 = ModelLocationUtils.getModelLocation(Blocks.OBSERVER, "_on");
        this.blockStateOutput.accept(MultiVariantGenerator.multiVariant(Blocks.OBSERVER).with(createBooleanModelDispatch(BlockStateProperties.POWERED, vk3, vk2)).with(createFacingDispatch()));
    }
    
    private void createPistons() {
        final TextureMapping iz2 = new TextureMapping().put(TextureSlot.BOTTOM, TextureMapping.getBlockTexture(Blocks.PISTON, "_bottom")).put(TextureSlot.SIDE, TextureMapping.getBlockTexture(Blocks.PISTON, "_side"));
        final ResourceLocation vk3 = TextureMapping.getBlockTexture(Blocks.PISTON, "_top_sticky");
        final ResourceLocation vk4 = TextureMapping.getBlockTexture(Blocks.PISTON, "_top");
        final TextureMapping iz3 = iz2.copyAndUpdate(TextureSlot.PLATFORM, vk3);
        final TextureMapping iz4 = iz2.copyAndUpdate(TextureSlot.PLATFORM, vk4);
        final ResourceLocation vk5 = ModelLocationUtils.getModelLocation(Blocks.PISTON, "_base");
        this.createPistonVariant(Blocks.PISTON, vk5, iz4);
        this.createPistonVariant(Blocks.STICKY_PISTON, vk5, iz3);
        final ResourceLocation vk6 = ModelTemplates.CUBE_BOTTOM_TOP.createWithSuffix(Blocks.PISTON, "_inventory", iz2.copyAndUpdate(TextureSlot.TOP, vk4), this.modelOutput);
        final ResourceLocation vk7 = ModelTemplates.CUBE_BOTTOM_TOP.createWithSuffix(Blocks.STICKY_PISTON, "_inventory", iz2.copyAndUpdate(TextureSlot.TOP, vk3), this.modelOutput);
        this.delegateItemModel(Blocks.PISTON, vk6);
        this.delegateItemModel(Blocks.STICKY_PISTON, vk7);
    }
    
    private void createPistonVariant(final Block bul, final ResourceLocation vk, final TextureMapping iz) {
        final ResourceLocation vk2 = ModelTemplates.PISTON.create(bul, iz, this.modelOutput);
        this.blockStateOutput.accept(MultiVariantGenerator.multiVariant(bul).with(createBooleanModelDispatch(BlockStateProperties.EXTENDED, vk, vk2)).with(createFacingDispatch()));
    }
    
    private void createPistonHeads() {
        final TextureMapping iz2 = new TextureMapping().put(TextureSlot.UNSTICKY, TextureMapping.getBlockTexture(Blocks.PISTON, "_top")).put(TextureSlot.SIDE, TextureMapping.getBlockTexture(Blocks.PISTON, "_side"));
        final TextureMapping iz3 = iz2.copyAndUpdate(TextureSlot.PLATFORM, TextureMapping.getBlockTexture(Blocks.PISTON, "_top_sticky"));
        final TextureMapping iz4 = iz2.copyAndUpdate(TextureSlot.PLATFORM, TextureMapping.getBlockTexture(Blocks.PISTON, "_top"));
        this.blockStateOutput.accept(MultiVariantGenerator.multiVariant(Blocks.PISTON_HEAD).with(PropertyDispatch.<Comparable, PistonType>properties((Property<Comparable>)BlockStateProperties.SHORT, BlockStateProperties.PISTON_TYPE).select((Comparable)false, PistonType.DEFAULT, Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, ModelTemplates.PISTON_HEAD.createWithSuffix(Blocks.PISTON, "_head", iz4, this.modelOutput))).select((Comparable)false, PistonType.STICKY, Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, ModelTemplates.PISTON_HEAD.createWithSuffix(Blocks.PISTON, "_head_sticky", iz3, this.modelOutput))).select((Comparable)true, PistonType.DEFAULT, Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, ModelTemplates.PISTON_HEAD_SHORT.createWithSuffix(Blocks.PISTON, "_head_short", iz4, this.modelOutput))).select((Comparable)true, PistonType.STICKY, Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, ModelTemplates.PISTON_HEAD_SHORT.createWithSuffix(Blocks.PISTON, "_head_short_sticky", iz3, this.modelOutput)))).with(createFacingDispatch()));
    }
    
    private void createScaffolding() {
        final ResourceLocation vk2 = ModelLocationUtils.getModelLocation(Blocks.SCAFFOLDING, "_stable");
        final ResourceLocation vk3 = ModelLocationUtils.getModelLocation(Blocks.SCAFFOLDING, "_unstable");
        this.delegateItemModel(Blocks.SCAFFOLDING, vk2);
        this.blockStateOutput.accept(MultiVariantGenerator.multiVariant(Blocks.SCAFFOLDING).with(createBooleanModelDispatch(BlockStateProperties.BOTTOM, vk3, vk2)));
    }
    
    private void createRedstoneLamp() {
        final ResourceLocation vk2 = TexturedModel.CUBE.create(Blocks.REDSTONE_LAMP, this.modelOutput);
        final ResourceLocation vk3 = this.createSuffixedVariant(Blocks.REDSTONE_LAMP, "_on", ModelTemplates.CUBE_ALL, (Function<ResourceLocation, TextureMapping>)TextureMapping::cube);
        this.blockStateOutput.accept(MultiVariantGenerator.multiVariant(Blocks.REDSTONE_LAMP).with(createBooleanModelDispatch(BlockStateProperties.LIT, vk3, vk2)));
    }
    
    private void createNormalTorch(final Block bul1, final Block bul2) {
        final TextureMapping iz4 = TextureMapping.torch(bul1);
        this.blockStateOutput.accept(createSimpleBlock(bul1, ModelTemplates.TORCH.create(bul1, iz4, this.modelOutput)));
        this.blockStateOutput.accept(MultiVariantGenerator.multiVariant(bul2, Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, ModelTemplates.WALL_TORCH.create(bul2, iz4, this.modelOutput))).with(createTorchHorizontalDispatch()));
        this.createSimpleFlatItemModel(bul1);
        this.skipAutoItemBlock(bul2);
    }
    
    private void createRedstoneTorch() {
        final TextureMapping iz2 = TextureMapping.torch(Blocks.REDSTONE_TORCH);
        final TextureMapping iz3 = TextureMapping.torch(TextureMapping.getBlockTexture(Blocks.REDSTONE_TORCH, "_off"));
        final ResourceLocation vk4 = ModelTemplates.TORCH.create(Blocks.REDSTONE_TORCH, iz2, this.modelOutput);
        final ResourceLocation vk5 = ModelTemplates.TORCH.createWithSuffix(Blocks.REDSTONE_TORCH, "_off", iz3, this.modelOutput);
        this.blockStateOutput.accept(MultiVariantGenerator.multiVariant(Blocks.REDSTONE_TORCH).with(createBooleanModelDispatch(BlockStateProperties.LIT, vk4, vk5)));
        final ResourceLocation vk6 = ModelTemplates.WALL_TORCH.create(Blocks.REDSTONE_WALL_TORCH, iz2, this.modelOutput);
        final ResourceLocation vk7 = ModelTemplates.WALL_TORCH.createWithSuffix(Blocks.REDSTONE_WALL_TORCH, "_off", iz3, this.modelOutput);
        this.blockStateOutput.accept(MultiVariantGenerator.multiVariant(Blocks.REDSTONE_WALL_TORCH).with(createBooleanModelDispatch(BlockStateProperties.LIT, vk6, vk7)).with(createTorchHorizontalDispatch()));
        this.createSimpleFlatItemModel(Blocks.REDSTONE_TORCH);
        this.skipAutoItemBlock(Blocks.REDSTONE_WALL_TORCH);
    }
    
    private void createRepeater() {
        this.createSimpleFlatItemModel(Items.REPEATER);
        final StringBuilder stringBuilder4;
        this.blockStateOutput.accept(MultiVariantGenerator.multiVariant(Blocks.REPEATER).with(PropertyDispatch.<Comparable, Comparable, Comparable>properties((Property<Comparable>)BlockStateProperties.DELAY, (Property<Comparable>)BlockStateProperties.LOCKED, (Property<Comparable>)BlockStateProperties.POWERED).generate((integer, boolean2, boolean3) -> {
            stringBuilder4 = new StringBuilder();
            stringBuilder4.append('_').append((Object)integer).append("tick");
            if (boolean3) {
                stringBuilder4.append("_on");
            }
            if (boolean2) {
                stringBuilder4.append("_locked");
            }
            return Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, TextureMapping.getBlockTexture(Blocks.REPEATER, stringBuilder4.toString()));
        })).with(createHorizontalFacingDispatchAlt()));
    }
    
    private void createSeaPickle() {
        this.createSimpleFlatItemModel(Items.SEA_PICKLE);
        this.blockStateOutput.accept(MultiVariantGenerator.multiVariant(Blocks.SEA_PICKLE).with(PropertyDispatch.<Comparable, Comparable>properties((Property<Comparable>)BlockStateProperties.PICKLES, (Property<Comparable>)BlockStateProperties.WATERLOGGED).select((Comparable)1, (Comparable)false, (List<Variant>)Arrays.asList((Object[])createRotatedVariants(ModelLocationUtils.decorateBlockModelLocation("dead_sea_pickle")))).select((Comparable)2, (Comparable)false, (List<Variant>)Arrays.asList((Object[])createRotatedVariants(ModelLocationUtils.decorateBlockModelLocation("two_dead_sea_pickles")))).select((Comparable)3, (Comparable)false, (List<Variant>)Arrays.asList((Object[])createRotatedVariants(ModelLocationUtils.decorateBlockModelLocation("three_dead_sea_pickles")))).select((Comparable)4, (Comparable)false, (List<Variant>)Arrays.asList((Object[])createRotatedVariants(ModelLocationUtils.decorateBlockModelLocation("four_dead_sea_pickles")))).select((Comparable)1, (Comparable)true, (List<Variant>)Arrays.asList((Object[])createRotatedVariants(ModelLocationUtils.decorateBlockModelLocation("sea_pickle")))).select((Comparable)2, (Comparable)true, (List<Variant>)Arrays.asList((Object[])createRotatedVariants(ModelLocationUtils.decorateBlockModelLocation("two_sea_pickles")))).select((Comparable)3, (Comparable)true, (List<Variant>)Arrays.asList((Object[])createRotatedVariants(ModelLocationUtils.decorateBlockModelLocation("three_sea_pickles")))).select((Comparable)4, (Comparable)true, (List<Variant>)Arrays.asList((Object[])createRotatedVariants(ModelLocationUtils.decorateBlockModelLocation("four_sea_pickles"))))));
    }
    
    private void createSnowBlocks() {
        final TextureMapping iz2 = TextureMapping.cube(Blocks.SNOW);
        final ResourceLocation vk3 = ModelTemplates.CUBE_ALL.create(Blocks.SNOW_BLOCK, iz2, this.modelOutput);
        this.blockStateOutput.accept(MultiVariantGenerator.multiVariant(Blocks.SNOW).with(PropertyDispatch.<Comparable>property((Property<Comparable>)BlockStateProperties.LAYERS).generate((java.util.function.Function<Comparable, Variant>)(integer -> Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, (integer < 8) ? ModelLocationUtils.getModelLocation(Blocks.SNOW, new StringBuilder().append("_height").append(integer * 2).toString()) : vk3)))));
        this.delegateItemModel(Blocks.SNOW, ModelLocationUtils.getModelLocation(Blocks.SNOW, "_height2"));
        this.blockStateOutput.accept(createSimpleBlock(Blocks.SNOW_BLOCK, vk3));
    }
    
    private void createStonecutter() {
        this.blockStateOutput.accept(MultiVariantGenerator.multiVariant(Blocks.STONECUTTER, Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, ModelLocationUtils.getModelLocation(Blocks.STONECUTTER))).with(createHorizontalFacingDispatch()));
    }
    
    private void createStructureBlock() {
        final ResourceLocation vk2 = TexturedModel.CUBE.create(Blocks.STRUCTURE_BLOCK, this.modelOutput);
        this.delegateItemModel(Blocks.STRUCTURE_BLOCK, vk2);
        this.blockStateOutput.accept(MultiVariantGenerator.multiVariant(Blocks.STRUCTURE_BLOCK).with(PropertyDispatch.<StructureMode>property(BlockStateProperties.STRUCTUREBLOCK_MODE).generate((java.util.function.Function<StructureMode, Variant>)(cfl -> Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, this.createSuffixedVariant(Blocks.STRUCTURE_BLOCK, "_" + cfl.getSerializedName(), ModelTemplates.CUBE_ALL, (Function<ResourceLocation, TextureMapping>)TextureMapping::cube))))));
    }
    
    private void createSweetBerryBush() {
        this.createSimpleFlatItemModel(Items.SWEET_BERRIES);
        this.blockStateOutput.accept(MultiVariantGenerator.multiVariant(Blocks.SWEET_BERRY_BUSH).with(PropertyDispatch.<Comparable>property((Property<Comparable>)BlockStateProperties.AGE_3).generate((java.util.function.Function<Comparable, Variant>)(integer -> Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, this.createSuffixedVariant(Blocks.SWEET_BERRY_BUSH, new StringBuilder().append("_stage").append((Object)integer).toString(), ModelTemplates.CROSS, (Function<ResourceLocation, TextureMapping>)TextureMapping::cross))))));
    }
    
    private void createTripwire() {
        this.createSimpleFlatItemModel(Items.STRING);
        this.blockStateOutput.accept(MultiVariantGenerator.multiVariant(Blocks.TRIPWIRE).with(PropertyDispatch.<Comparable, Comparable, Comparable, Comparable, Comparable>properties((Property<Comparable>)BlockStateProperties.ATTACHED, (Property<Comparable>)BlockStateProperties.EAST, (Property<Comparable>)BlockStateProperties.NORTH, (Property<Comparable>)BlockStateProperties.SOUTH, (Property<Comparable>)BlockStateProperties.WEST).select((Comparable)false, (Comparable)false, (Comparable)false, (Comparable)false, (Comparable)false, Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, ModelLocationUtils.getModelLocation(Blocks.TRIPWIRE, "_ns"))).select((Comparable)false, (Comparable)true, (Comparable)false, (Comparable)false, (Comparable)false, Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, ModelLocationUtils.getModelLocation(Blocks.TRIPWIRE, "_n")).<VariantProperties.Rotation>with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90)).select((Comparable)false, (Comparable)false, (Comparable)true, (Comparable)false, (Comparable)false, Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, ModelLocationUtils.getModelLocation(Blocks.TRIPWIRE, "_n"))).select((Comparable)false, (Comparable)false, (Comparable)false, (Comparable)true, (Comparable)false, Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, ModelLocationUtils.getModelLocation(Blocks.TRIPWIRE, "_n")).<VariantProperties.Rotation>with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180)).select((Comparable)false, (Comparable)false, (Comparable)false, (Comparable)false, (Comparable)true, Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, ModelLocationUtils.getModelLocation(Blocks.TRIPWIRE, "_n")).<VariantProperties.Rotation>with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270)).select((Comparable)false, (Comparable)true, (Comparable)true, (Comparable)false, (Comparable)false, Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, ModelLocationUtils.getModelLocation(Blocks.TRIPWIRE, "_ne"))).select((Comparable)false, (Comparable)true, (Comparable)false, (Comparable)true, (Comparable)false, Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, ModelLocationUtils.getModelLocation(Blocks.TRIPWIRE, "_ne")).<VariantProperties.Rotation>with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90)).select((Comparable)false, (Comparable)false, (Comparable)false, (Comparable)true, (Comparable)true, Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, ModelLocationUtils.getModelLocation(Blocks.TRIPWIRE, "_ne")).<VariantProperties.Rotation>with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180)).select((Comparable)false, (Comparable)false, (Comparable)true, (Comparable)false, (Comparable)true, Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, ModelLocationUtils.getModelLocation(Blocks.TRIPWIRE, "_ne")).<VariantProperties.Rotation>with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270)).select((Comparable)false, (Comparable)false, (Comparable)true, (Comparable)true, (Comparable)false, Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, ModelLocationUtils.getModelLocation(Blocks.TRIPWIRE, "_ns"))).select((Comparable)false, (Comparable)true, (Comparable)false, (Comparable)false, (Comparable)true, Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, ModelLocationUtils.getModelLocation(Blocks.TRIPWIRE, "_ns")).<VariantProperties.Rotation>with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90)).select((Comparable)false, (Comparable)true, (Comparable)true, (Comparable)true, (Comparable)false, Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, ModelLocationUtils.getModelLocation(Blocks.TRIPWIRE, "_nse"))).select((Comparable)false, (Comparable)true, (Comparable)false, (Comparable)true, (Comparable)true, Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, ModelLocationUtils.getModelLocation(Blocks.TRIPWIRE, "_nse")).<VariantProperties.Rotation>with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90)).select((Comparable)false, (Comparable)false, (Comparable)true, (Comparable)true, (Comparable)true, Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, ModelLocationUtils.getModelLocation(Blocks.TRIPWIRE, "_nse")).<VariantProperties.Rotation>with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180)).select((Comparable)false, (Comparable)true, (Comparable)true, (Comparable)false, (Comparable)true, Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, ModelLocationUtils.getModelLocation(Blocks.TRIPWIRE, "_nse")).<VariantProperties.Rotation>with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270)).select((Comparable)false, (Comparable)true, (Comparable)true, (Comparable)true, (Comparable)true, Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, ModelLocationUtils.getModelLocation(Blocks.TRIPWIRE, "_nsew"))).select((Comparable)true, (Comparable)false, (Comparable)false, (Comparable)false, (Comparable)false, Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, ModelLocationUtils.getModelLocation(Blocks.TRIPWIRE, "_attached_ns"))).select((Comparable)true, (Comparable)false, (Comparable)true, (Comparable)false, (Comparable)false, Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, ModelLocationUtils.getModelLocation(Blocks.TRIPWIRE, "_attached_n"))).select((Comparable)true, (Comparable)false, (Comparable)false, (Comparable)true, (Comparable)false, Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, ModelLocationUtils.getModelLocation(Blocks.TRIPWIRE, "_attached_n")).<VariantProperties.Rotation>with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180)).select((Comparable)true, (Comparable)true, (Comparable)false, (Comparable)false, (Comparable)false, Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, ModelLocationUtils.getModelLocation(Blocks.TRIPWIRE, "_attached_n")).<VariantProperties.Rotation>with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90)).select((Comparable)true, (Comparable)false, (Comparable)false, (Comparable)false, (Comparable)true, Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, ModelLocationUtils.getModelLocation(Blocks.TRIPWIRE, "_attached_n")).<VariantProperties.Rotation>with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270)).select((Comparable)true, (Comparable)true, (Comparable)true, (Comparable)false, (Comparable)false, Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, ModelLocationUtils.getModelLocation(Blocks.TRIPWIRE, "_attached_ne"))).select((Comparable)true, (Comparable)true, (Comparable)false, (Comparable)true, (Comparable)false, Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, ModelLocationUtils.getModelLocation(Blocks.TRIPWIRE, "_attached_ne")).<VariantProperties.Rotation>with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90)).select((Comparable)true, (Comparable)false, (Comparable)false, (Comparable)true, (Comparable)true, Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, ModelLocationUtils.getModelLocation(Blocks.TRIPWIRE, "_attached_ne")).<VariantProperties.Rotation>with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180)).select((Comparable)true, (Comparable)false, (Comparable)true, (Comparable)false, (Comparable)true, Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, ModelLocationUtils.getModelLocation(Blocks.TRIPWIRE, "_attached_ne")).<VariantProperties.Rotation>with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270)).select((Comparable)true, (Comparable)false, (Comparable)true, (Comparable)true, (Comparable)false, Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, ModelLocationUtils.getModelLocation(Blocks.TRIPWIRE, "_attached_ns"))).select((Comparable)true, (Comparable)true, (Comparable)false, (Comparable)false, (Comparable)true, Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, ModelLocationUtils.getModelLocation(Blocks.TRIPWIRE, "_attached_ns")).<VariantProperties.Rotation>with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90)).select((Comparable)true, (Comparable)true, (Comparable)true, (Comparable)true, (Comparable)false, Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, ModelLocationUtils.getModelLocation(Blocks.TRIPWIRE, "_attached_nse"))).select((Comparable)true, (Comparable)true, (Comparable)false, (Comparable)true, (Comparable)true, Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, ModelLocationUtils.getModelLocation(Blocks.TRIPWIRE, "_attached_nse")).<VariantProperties.Rotation>with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90)).select((Comparable)true, (Comparable)false, (Comparable)true, (Comparable)true, (Comparable)true, Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, ModelLocationUtils.getModelLocation(Blocks.TRIPWIRE, "_attached_nse")).<VariantProperties.Rotation>with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180)).select((Comparable)true, (Comparable)true, (Comparable)true, (Comparable)false, (Comparable)true, Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, ModelLocationUtils.getModelLocation(Blocks.TRIPWIRE, "_attached_nse")).<VariantProperties.Rotation>with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270)).select((Comparable)true, (Comparable)true, (Comparable)true, (Comparable)true, (Comparable)true, Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, ModelLocationUtils.getModelLocation(Blocks.TRIPWIRE, "_attached_nsew")))));
    }
    
    private void createTripwireHook() {
        this.createSimpleFlatItemModel(Blocks.TRIPWIRE_HOOK);
        this.blockStateOutput.accept(MultiVariantGenerator.multiVariant(Blocks.TRIPWIRE_HOOK).with(PropertyDispatch.<Comparable, Comparable>properties((Property<Comparable>)BlockStateProperties.ATTACHED, (Property<Comparable>)BlockStateProperties.POWERED).generate((java.util.function.BiFunction<Comparable, Comparable, Variant>)((boolean1, boolean2) -> Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, TextureMapping.getBlockTexture(Blocks.TRIPWIRE_HOOK, new StringBuilder().append(((boolean)boolean1) ? "_attached" : "").append(((boolean)boolean2) ? "_on" : "").toString()))))).with(createHorizontalFacingDispatch()));
    }
    
    private ResourceLocation createTurtleEggModel(final int integer, final String string, final TextureMapping iz) {
        switch (integer) {
            case 1: {
                return ModelTemplates.TURTLE_EGG.create(ModelLocationUtils.decorateBlockModelLocation(string + "turtle_egg"), iz, this.modelOutput);
            }
            case 2: {
                return ModelTemplates.TWO_TURTLE_EGGS.create(ModelLocationUtils.decorateBlockModelLocation("two_" + string + "turtle_eggs"), iz, this.modelOutput);
            }
            case 3: {
                return ModelTemplates.THREE_TURTLE_EGGS.create(ModelLocationUtils.decorateBlockModelLocation("three_" + string + "turtle_eggs"), iz, this.modelOutput);
            }
            case 4: {
                return ModelTemplates.FOUR_TURTLE_EGGS.create(ModelLocationUtils.decorateBlockModelLocation("four_" + string + "turtle_eggs"), iz, this.modelOutput);
            }
            default: {
                throw new UnsupportedOperationException();
            }
        }
    }
    
    private ResourceLocation createTurtleEggModel(final Integer integer1, final Integer integer2) {
        switch (integer2) {
            case 0: {
                return this.createTurtleEggModel((int)integer1, "", TextureMapping.cube(TextureMapping.getBlockTexture(Blocks.TURTLE_EGG)));
            }
            case 1: {
                return this.createTurtleEggModel((int)integer1, "slightly_cracked_", TextureMapping.cube(TextureMapping.getBlockTexture(Blocks.TURTLE_EGG, "_slightly_cracked")));
            }
            case 2: {
                return this.createTurtleEggModel((int)integer1, "very_cracked_", TextureMapping.cube(TextureMapping.getBlockTexture(Blocks.TURTLE_EGG, "_very_cracked")));
            }
            default: {
                throw new UnsupportedOperationException();
            }
        }
    }
    
    private void createTurtleEgg() {
        this.createSimpleFlatItemModel(Items.TURTLE_EGG);
        this.blockStateOutput.accept(MultiVariantGenerator.multiVariant(Blocks.TURTLE_EGG).with(PropertyDispatch.<Comparable, Comparable>properties((Property<Comparable>)BlockStateProperties.EGGS, (Property<Comparable>)BlockStateProperties.HATCH).generateList((java.util.function.BiFunction<Comparable, Comparable, List<Variant>>)((integer1, integer2) -> Arrays.asList((Object[])createRotatedVariants(this.createTurtleEggModel(integer1, integer2)))))));
    }
    
    private void createVine() {
        this.createSimpleFlatItemModel(Blocks.VINE);
        this.blockStateOutput.accept(MultiVariantGenerator.multiVariant(Blocks.VINE).with(PropertyDispatch.<Comparable, Comparable, Comparable, Comparable, Comparable>properties((Property<Comparable>)BlockStateProperties.EAST, (Property<Comparable>)BlockStateProperties.NORTH, (Property<Comparable>)BlockStateProperties.SOUTH, (Property<Comparable>)BlockStateProperties.UP, (Property<Comparable>)BlockStateProperties.WEST).select((Comparable)false, (Comparable)false, (Comparable)false, (Comparable)false, (Comparable)false, Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, ModelLocationUtils.getModelLocation(Blocks.VINE, "_1"))).select((Comparable)false, (Comparable)false, (Comparable)true, (Comparable)false, (Comparable)false, Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, ModelLocationUtils.getModelLocation(Blocks.VINE, "_1"))).select((Comparable)false, (Comparable)false, (Comparable)false, (Comparable)false, (Comparable)true, Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, ModelLocationUtils.getModelLocation(Blocks.VINE, "_1")).<VariantProperties.Rotation>with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90)).select((Comparable)false, (Comparable)true, (Comparable)false, (Comparable)false, (Comparable)false, Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, ModelLocationUtils.getModelLocation(Blocks.VINE, "_1")).<VariantProperties.Rotation>with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180)).select((Comparable)true, (Comparable)false, (Comparable)false, (Comparable)false, (Comparable)false, Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, ModelLocationUtils.getModelLocation(Blocks.VINE, "_1")).<VariantProperties.Rotation>with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270)).select((Comparable)true, (Comparable)true, (Comparable)false, (Comparable)false, (Comparable)false, Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, ModelLocationUtils.getModelLocation(Blocks.VINE, "_2"))).select((Comparable)true, (Comparable)false, (Comparable)true, (Comparable)false, (Comparable)false, Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, ModelLocationUtils.getModelLocation(Blocks.VINE, "_2")).<VariantProperties.Rotation>with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90)).select((Comparable)false, (Comparable)false, (Comparable)true, (Comparable)false, (Comparable)true, Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, ModelLocationUtils.getModelLocation(Blocks.VINE, "_2")).<VariantProperties.Rotation>with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180)).select((Comparable)false, (Comparable)true, (Comparable)false, (Comparable)false, (Comparable)true, Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, ModelLocationUtils.getModelLocation(Blocks.VINE, "_2")).<VariantProperties.Rotation>with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270)).select((Comparable)true, (Comparable)false, (Comparable)false, (Comparable)false, (Comparable)true, Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, ModelLocationUtils.getModelLocation(Blocks.VINE, "_2_opposite"))).select((Comparable)false, (Comparable)true, (Comparable)true, (Comparable)false, (Comparable)false, Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, ModelLocationUtils.getModelLocation(Blocks.VINE, "_2_opposite")).<VariantProperties.Rotation>with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90)).select((Comparable)true, (Comparable)true, (Comparable)true, (Comparable)false, (Comparable)false, Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, ModelLocationUtils.getModelLocation(Blocks.VINE, "_3"))).select((Comparable)true, (Comparable)false, (Comparable)true, (Comparable)false, (Comparable)true, Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, ModelLocationUtils.getModelLocation(Blocks.VINE, "_3")).<VariantProperties.Rotation>with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90)).select((Comparable)false, (Comparable)true, (Comparable)true, (Comparable)false, (Comparable)true, Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, ModelLocationUtils.getModelLocation(Blocks.VINE, "_3")).<VariantProperties.Rotation>with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180)).select((Comparable)true, (Comparable)true, (Comparable)false, (Comparable)false, (Comparable)true, Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, ModelLocationUtils.getModelLocation(Blocks.VINE, "_3")).<VariantProperties.Rotation>with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270)).select((Comparable)true, (Comparable)true, (Comparable)true, (Comparable)false, (Comparable)true, Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, ModelLocationUtils.getModelLocation(Blocks.VINE, "_4"))).select((Comparable)false, (Comparable)false, (Comparable)false, (Comparable)true, (Comparable)false, Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, ModelLocationUtils.getModelLocation(Blocks.VINE, "_u"))).select((Comparable)false, (Comparable)false, (Comparable)true, (Comparable)true, (Comparable)false, Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, ModelLocationUtils.getModelLocation(Blocks.VINE, "_1u"))).select((Comparable)false, (Comparable)false, (Comparable)false, (Comparable)true, (Comparable)true, Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, ModelLocationUtils.getModelLocation(Blocks.VINE, "_1u")).<VariantProperties.Rotation>with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90)).select((Comparable)false, (Comparable)true, (Comparable)false, (Comparable)true, (Comparable)false, Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, ModelLocationUtils.getModelLocation(Blocks.VINE, "_1u")).<VariantProperties.Rotation>with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180)).select((Comparable)true, (Comparable)false, (Comparable)false, (Comparable)true, (Comparable)false, Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, ModelLocationUtils.getModelLocation(Blocks.VINE, "_1u")).<VariantProperties.Rotation>with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270)).select((Comparable)true, (Comparable)true, (Comparable)false, (Comparable)true, (Comparable)false, Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, ModelLocationUtils.getModelLocation(Blocks.VINE, "_2u"))).select((Comparable)true, (Comparable)false, (Comparable)true, (Comparable)true, (Comparable)false, Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, ModelLocationUtils.getModelLocation(Blocks.VINE, "_2u")).<VariantProperties.Rotation>with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90)).select((Comparable)false, (Comparable)false, (Comparable)true, (Comparable)true, (Comparable)true, Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, ModelLocationUtils.getModelLocation(Blocks.VINE, "_2u")).<VariantProperties.Rotation>with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180)).select((Comparable)false, (Comparable)true, (Comparable)false, (Comparable)true, (Comparable)true, Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, ModelLocationUtils.getModelLocation(Blocks.VINE, "_2u")).<VariantProperties.Rotation>with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270)).select((Comparable)true, (Comparable)false, (Comparable)false, (Comparable)true, (Comparable)true, Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, ModelLocationUtils.getModelLocation(Blocks.VINE, "_2u_opposite"))).select((Comparable)false, (Comparable)true, (Comparable)true, (Comparable)true, (Comparable)false, Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, ModelLocationUtils.getModelLocation(Blocks.VINE, "_2u_opposite")).<VariantProperties.Rotation>with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90)).select((Comparable)true, (Comparable)true, (Comparable)true, (Comparable)true, (Comparable)false, Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, ModelLocationUtils.getModelLocation(Blocks.VINE, "_3u"))).select((Comparable)true, (Comparable)false, (Comparable)true, (Comparable)true, (Comparable)true, Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, ModelLocationUtils.getModelLocation(Blocks.VINE, "_3u")).<VariantProperties.Rotation>with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90)).select((Comparable)false, (Comparable)true, (Comparable)true, (Comparable)true, (Comparable)true, Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, ModelLocationUtils.getModelLocation(Blocks.VINE, "_3u")).<VariantProperties.Rotation>with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180)).select((Comparable)true, (Comparable)true, (Comparable)false, (Comparable)true, (Comparable)true, Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, ModelLocationUtils.getModelLocation(Blocks.VINE, "_3u")).<VariantProperties.Rotation>with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270)).select((Comparable)true, (Comparable)true, (Comparable)true, (Comparable)true, (Comparable)true, Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, ModelLocationUtils.getModelLocation(Blocks.VINE, "_4u")))));
    }
    
    private void createMagmaBlock() {
        this.blockStateOutput.accept(createSimpleBlock(Blocks.MAGMA_BLOCK, ModelTemplates.CUBE_ALL.create(Blocks.MAGMA_BLOCK, TextureMapping.cube(ModelLocationUtils.decorateBlockModelLocation("magma")), this.modelOutput)));
    }
    
    private void createShulkerBox(final Block bul) {
        this.createTrivialBlock(bul, TexturedModel.PARTICLE_ONLY);
        ModelTemplates.SHULKER_BOX_INVENTORY.create(ModelLocationUtils.getModelLocation(bul.asItem()), TextureMapping.particle(bul), this.modelOutput);
    }
    
    private void createGrowingPlant(final Block bul1, final Block bul2, final TintState c) {
        this.createCrossBlock(bul1, c);
        this.createCrossBlock(bul2, c);
    }
    
    private void createBedItem(final Block bul1, final Block bul2) {
        ModelTemplates.BED_INVENTORY.create(ModelLocationUtils.getModelLocation(bul1.asItem()), TextureMapping.particle(bul2), this.modelOutput);
    }
    
    private void createInfestedStone() {
        final ResourceLocation vk2 = ModelLocationUtils.getModelLocation(Blocks.STONE);
        final ResourceLocation vk3 = ModelLocationUtils.getModelLocation(Blocks.STONE, "_mirrored");
        this.blockStateOutput.accept(createRotatedVariant(Blocks.INFESTED_STONE, vk2, vk3));
        this.delegateItemModel(Blocks.INFESTED_STONE, vk2);
    }
    
    private void createNetherRoots(final Block bul1, final Block bul2) {
        this.createCrossBlockWithDefaultItem(bul1, TintState.NOT_TINTED);
        final TextureMapping iz4 = TextureMapping.plant(TextureMapping.getBlockTexture(bul1, "_pot"));
        final ResourceLocation vk5 = TintState.NOT_TINTED.getCrossPot().create(bul2, iz4, this.modelOutput);
        this.blockStateOutput.accept(createSimpleBlock(bul2, vk5));
    }
    
    private void createRespawnAnchor() {
        final ResourceLocation vk2 = TextureMapping.getBlockTexture(Blocks.RESPAWN_ANCHOR, "_bottom");
        final ResourceLocation vk3 = TextureMapping.getBlockTexture(Blocks.RESPAWN_ANCHOR, "_top_off");
        final ResourceLocation vk4 = TextureMapping.getBlockTexture(Blocks.RESPAWN_ANCHOR, "_top");
        final ResourceLocation[] arr5 = new ResourceLocation[5];
        for (int integer6 = 0; integer6 < 5; ++integer6) {
            final TextureMapping iz7 = new TextureMapping().put(TextureSlot.BOTTOM, vk2).put(TextureSlot.TOP, (integer6 == 0) ? vk3 : vk4).put(TextureSlot.SIDE, TextureMapping.getBlockTexture(Blocks.RESPAWN_ANCHOR, new StringBuilder().append("_side").append(integer6).toString()));
            arr5[integer6] = ModelTemplates.CUBE_BOTTOM_TOP.createWithSuffix(Blocks.RESPAWN_ANCHOR, new StringBuilder().append("_").append(integer6).toString(), iz7, this.modelOutput);
        }
        this.blockStateOutput.accept(MultiVariantGenerator.multiVariant(Blocks.RESPAWN_ANCHOR).with(PropertyDispatch.<Comparable>property((Property<Comparable>)BlockStateProperties.RESPAWN_ANCHOR_CHARGES).generate((java.util.function.Function<Comparable, Variant>)(integer -> Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, arr5[integer])))));
        this.delegateItemModel(Items.RESPAWN_ANCHOR, arr5[0]);
    }
    
    private Variant applyRotation(final FrontAndTop ge, final Variant ir) {
        switch (ge) {
            case DOWN_NORTH: {
                return ir.<VariantProperties.Rotation>with(VariantProperties.X_ROT, VariantProperties.Rotation.R90);
            }
            case DOWN_SOUTH: {
                return ir.<VariantProperties.Rotation>with(VariantProperties.X_ROT, VariantProperties.Rotation.R90).<VariantProperties.Rotation>with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180);
            }
            case DOWN_WEST: {
                return ir.<VariantProperties.Rotation>with(VariantProperties.X_ROT, VariantProperties.Rotation.R90).<VariantProperties.Rotation>with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270);
            }
            case DOWN_EAST: {
                return ir.<VariantProperties.Rotation>with(VariantProperties.X_ROT, VariantProperties.Rotation.R90).<VariantProperties.Rotation>with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90);
            }
            case UP_NORTH: {
                return ir.<VariantProperties.Rotation>with(VariantProperties.X_ROT, VariantProperties.Rotation.R270).<VariantProperties.Rotation>with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180);
            }
            case UP_SOUTH: {
                return ir.<VariantProperties.Rotation>with(VariantProperties.X_ROT, VariantProperties.Rotation.R270);
            }
            case UP_WEST: {
                return ir.<VariantProperties.Rotation>with(VariantProperties.X_ROT, VariantProperties.Rotation.R270).<VariantProperties.Rotation>with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90);
            }
            case UP_EAST: {
                return ir.<VariantProperties.Rotation>with(VariantProperties.X_ROT, VariantProperties.Rotation.R270).<VariantProperties.Rotation>with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270);
            }
            case NORTH_UP: {
                return ir;
            }
            case SOUTH_UP: {
                return ir.<VariantProperties.Rotation>with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180);
            }
            case WEST_UP: {
                return ir.<VariantProperties.Rotation>with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270);
            }
            case EAST_UP: {
                return ir.<VariantProperties.Rotation>with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90);
            }
            default: {
                throw new UnsupportedOperationException(new StringBuilder().append("Rotation ").append(ge).append(" can't be expressed with existing x and y values").toString());
            }
        }
    }
    
    private void createJigsaw() {
        final ResourceLocation vk2 = TextureMapping.getBlockTexture(Blocks.JIGSAW, "_top");
        final ResourceLocation vk3 = TextureMapping.getBlockTexture(Blocks.JIGSAW, "_bottom");
        final ResourceLocation vk4 = TextureMapping.getBlockTexture(Blocks.JIGSAW, "_side");
        final ResourceLocation vk5 = TextureMapping.getBlockTexture(Blocks.JIGSAW, "_lock");
        final TextureMapping iz6 = new TextureMapping().put(TextureSlot.DOWN, vk4).put(TextureSlot.WEST, vk4).put(TextureSlot.EAST, vk4).put(TextureSlot.PARTICLE, vk2).put(TextureSlot.NORTH, vk2).put(TextureSlot.SOUTH, vk3).put(TextureSlot.UP, vk5);
        final ResourceLocation vk6 = ModelTemplates.CUBE_DIRECTIONAL.create(Blocks.JIGSAW, iz6, this.modelOutput);
        this.blockStateOutput.accept(MultiVariantGenerator.multiVariant(Blocks.JIGSAW, Variant.variant().<ResourceLocation>with(VariantProperties.MODEL, vk6)).with(PropertyDispatch.<FrontAndTop>property(BlockStateProperties.ORIENTATION).generate((java.util.function.Function<FrontAndTop, Variant>)(ge -> this.applyRotation(ge, Variant.variant())))));
    }
    
    public void run() {
        this.createNonTemplateModelBlock(Blocks.AIR);
        this.createNonTemplateModelBlock(Blocks.CAVE_AIR, Blocks.AIR);
        this.createNonTemplateModelBlock(Blocks.VOID_AIR, Blocks.AIR);
        this.createNonTemplateModelBlock(Blocks.BEACON);
        this.createNonTemplateModelBlock(Blocks.CACTUS);
        this.createNonTemplateModelBlock(Blocks.BUBBLE_COLUMN, Blocks.WATER);
        this.createNonTemplateModelBlock(Blocks.DRAGON_EGG);
        this.createNonTemplateModelBlock(Blocks.DRIED_KELP_BLOCK);
        this.createNonTemplateModelBlock(Blocks.ENCHANTING_TABLE);
        this.createNonTemplateModelBlock(Blocks.FLOWER_POT);
        this.createSimpleFlatItemModel(Items.FLOWER_POT);
        this.createNonTemplateModelBlock(Blocks.HONEY_BLOCK);
        this.createNonTemplateModelBlock(Blocks.WATER);
        this.createNonTemplateModelBlock(Blocks.LAVA);
        this.createNonTemplateModelBlock(Blocks.SLIME_BLOCK);
        this.createSimpleFlatItemModel(Items.CHAIN);
        this.createNonTemplateModelBlock(Blocks.POTTED_BAMBOO);
        this.createNonTemplateModelBlock(Blocks.POTTED_CACTUS);
        this.createAirLikeBlock(Blocks.BARRIER, Items.BARRIER);
        this.createSimpleFlatItemModel(Items.BARRIER);
        this.createAirLikeBlock(Blocks.STRUCTURE_VOID, Items.STRUCTURE_VOID);
        this.createSimpleFlatItemModel(Items.STRUCTURE_VOID);
        this.createAirLikeBlock(Blocks.MOVING_PISTON, TextureMapping.getBlockTexture(Blocks.PISTON, "_side"));
        this.createTrivialBlock(Blocks.COAL_ORE, TexturedModel.CUBE);
        this.createTrivialBlock(Blocks.COAL_BLOCK, TexturedModel.CUBE);
        this.createTrivialBlock(Blocks.DIAMOND_ORE, TexturedModel.CUBE);
        this.createTrivialBlock(Blocks.DIAMOND_BLOCK, TexturedModel.CUBE);
        this.createTrivialBlock(Blocks.EMERALD_ORE, TexturedModel.CUBE);
        this.createTrivialBlock(Blocks.EMERALD_BLOCK, TexturedModel.CUBE);
        this.createTrivialBlock(Blocks.GOLD_ORE, TexturedModel.CUBE);
        this.createTrivialBlock(Blocks.NETHER_GOLD_ORE, TexturedModel.CUBE);
        this.createTrivialBlock(Blocks.GOLD_BLOCK, TexturedModel.CUBE);
        this.createTrivialBlock(Blocks.IRON_ORE, TexturedModel.CUBE);
        this.createTrivialBlock(Blocks.IRON_BLOCK, TexturedModel.CUBE);
        this.createTrivialBlock(Blocks.ANCIENT_DEBRIS, TexturedModel.COLUMN);
        this.createTrivialBlock(Blocks.NETHERITE_BLOCK, TexturedModel.CUBE);
        this.createTrivialBlock(Blocks.LAPIS_ORE, TexturedModel.CUBE);
        this.createTrivialBlock(Blocks.LAPIS_BLOCK, TexturedModel.CUBE);
        this.createTrivialBlock(Blocks.NETHER_QUARTZ_ORE, TexturedModel.CUBE);
        this.createTrivialBlock(Blocks.REDSTONE_ORE, TexturedModel.CUBE);
        this.createTrivialBlock(Blocks.REDSTONE_BLOCK, TexturedModel.CUBE);
        this.createTrivialBlock(Blocks.GILDED_BLACKSTONE, TexturedModel.CUBE);
        this.createTrivialBlock(Blocks.BLUE_ICE, TexturedModel.CUBE);
        this.createTrivialBlock(Blocks.CHISELED_NETHER_BRICKS, TexturedModel.CUBE);
        this.createTrivialBlock(Blocks.CLAY, TexturedModel.CUBE);
        this.createTrivialBlock(Blocks.COARSE_DIRT, TexturedModel.CUBE);
        this.createTrivialBlock(Blocks.CRACKED_NETHER_BRICKS, TexturedModel.CUBE);
        this.createTrivialBlock(Blocks.CRACKED_STONE_BRICKS, TexturedModel.CUBE);
        this.createTrivialBlock(Blocks.CRYING_OBSIDIAN, TexturedModel.CUBE);
        this.createTrivialBlock(Blocks.END_STONE, TexturedModel.CUBE);
        this.createTrivialBlock(Blocks.GLOWSTONE, TexturedModel.CUBE);
        this.createTrivialBlock(Blocks.GRAVEL, TexturedModel.CUBE);
        this.createTrivialBlock(Blocks.HONEYCOMB_BLOCK, TexturedModel.CUBE);
        this.createTrivialBlock(Blocks.ICE, TexturedModel.CUBE);
        this.createTrivialBlock(Blocks.JUKEBOX, TexturedModel.CUBE_TOP);
        this.createTrivialBlock(Blocks.LODESTONE, TexturedModel.COLUMN);
        this.createTrivialBlock(Blocks.MELON, TexturedModel.COLUMN);
        this.createTrivialBlock(Blocks.NETHER_WART_BLOCK, TexturedModel.CUBE);
        this.createTrivialBlock(Blocks.NOTE_BLOCK, TexturedModel.CUBE);
        this.createTrivialBlock(Blocks.PACKED_ICE, TexturedModel.CUBE);
        this.createTrivialBlock(Blocks.OBSIDIAN, TexturedModel.CUBE);
        this.createTrivialBlock(Blocks.QUARTZ_BRICKS, TexturedModel.CUBE);
        this.createTrivialBlock(Blocks.SEA_LANTERN, TexturedModel.CUBE);
        this.createTrivialBlock(Blocks.SHROOMLIGHT, TexturedModel.CUBE);
        this.createTrivialBlock(Blocks.SOUL_SAND, TexturedModel.CUBE);
        this.createTrivialBlock(Blocks.SOUL_SOIL, TexturedModel.CUBE);
        this.createTrivialBlock(Blocks.SPAWNER, TexturedModel.CUBE);
        this.createTrivialBlock(Blocks.SPONGE, TexturedModel.CUBE);
        this.createTrivialBlock(Blocks.SEAGRASS, TexturedModel.SEAGRASS);
        this.createSimpleFlatItemModel(Items.SEAGRASS);
        this.createTrivialBlock(Blocks.TNT, TexturedModel.CUBE_TOP_BOTTOM);
        this.createTrivialBlock(Blocks.TARGET, TexturedModel.COLUMN);
        this.createTrivialBlock(Blocks.WARPED_WART_BLOCK, TexturedModel.CUBE);
        this.createTrivialBlock(Blocks.WET_SPONGE, TexturedModel.CUBE);
        this.createTrivialBlock(Blocks.CRACKED_POLISHED_BLACKSTONE_BRICKS, TexturedModel.CUBE);
        this.createTrivialBlock(Blocks.CHISELED_QUARTZ_BLOCK, TexturedModel.COLUMN.updateTexture((Consumer<TextureMapping>)(iz -> iz.put(TextureSlot.SIDE, TextureMapping.getBlockTexture(Blocks.CHISELED_QUARTZ_BLOCK)))));
        this.createTrivialBlock(Blocks.CHISELED_STONE_BRICKS, TexturedModel.CUBE);
        this.createChiseledSandsone(Blocks.CHISELED_SANDSTONE, Blocks.SANDSTONE);
        this.createChiseledSandsone(Blocks.CHISELED_RED_SANDSTONE, Blocks.RED_SANDSTONE);
        this.createTrivialBlock(Blocks.CHISELED_POLISHED_BLACKSTONE, TexturedModel.CUBE);
        this.createWeightedPressurePlate(Blocks.LIGHT_WEIGHTED_PRESSURE_PLATE, Blocks.GOLD_BLOCK);
        this.createWeightedPressurePlate(Blocks.HEAVY_WEIGHTED_PRESSURE_PLATE, Blocks.IRON_BLOCK);
        this.createBookshelf();
        this.createBrewingStand();
        this.createCakeBlock();
        this.createCampfires(Blocks.CAMPFIRE, Blocks.SOUL_CAMPFIRE);
        this.createCartographyTable();
        this.createCauldron();
        this.createChorusFlower();
        this.createChorusPlant();
        this.createComposter();
        this.createDaylightDetector();
        this.createEndPortalFrame();
        this.createRotatableColumn(Blocks.END_ROD);
        this.createFarmland();
        this.createFire();
        this.createSoulFire();
        this.createFrostedIce();
        this.createGrassBlocks();
        this.createCocoa();
        this.createGrassPath();
        this.createGrindstone();
        this.createHopper();
        this.createIronBars();
        this.createLever();
        this.createLilyPad();
        this.createNetherPortalBlock();
        this.createNetherrack();
        this.createObserver();
        this.createPistons();
        this.createPistonHeads();
        this.createScaffolding();
        this.createRedstoneTorch();
        this.createRedstoneLamp();
        this.createRepeater();
        this.createSeaPickle();
        this.createSmithingTable();
        this.createSnowBlocks();
        this.createStonecutter();
        this.createStructureBlock();
        this.createSweetBerryBush();
        this.createTripwire();
        this.createTripwireHook();
        this.createTurtleEgg();
        this.createVine();
        this.createMagmaBlock();
        this.createJigsaw();
        this.createNonTemplateHorizontalBlock(Blocks.LADDER);
        this.createSimpleFlatItemModel(Blocks.LADDER);
        this.createNonTemplateHorizontalBlock(Blocks.LECTERN);
        this.createNormalTorch(Blocks.TORCH, Blocks.WALL_TORCH);
        this.createNormalTorch(Blocks.SOUL_TORCH, Blocks.SOUL_WALL_TORCH);
        this.createCraftingTableLike(Blocks.CRAFTING_TABLE, Blocks.OAK_PLANKS, (BiFunction<Block, Block, TextureMapping>)TextureMapping::craftingTable);
        this.createCraftingTableLike(Blocks.FLETCHING_TABLE, Blocks.BIRCH_PLANKS, (BiFunction<Block, Block, TextureMapping>)TextureMapping::fletchingTable);
        this.createNyliumBlock(Blocks.CRIMSON_NYLIUM);
        this.createNyliumBlock(Blocks.WARPED_NYLIUM);
        this.createDispenserBlock(Blocks.DISPENSER);
        this.createDispenserBlock(Blocks.DROPPER);
        this.createLantern(Blocks.LANTERN);
        this.createLantern(Blocks.SOUL_LANTERN);
        this.createAxisAlignedPillarBlockCustomModel(Blocks.CHAIN, ModelLocationUtils.getModelLocation(Blocks.CHAIN));
        this.createAxisAlignedPillarBlock(Blocks.BASALT, TexturedModel.COLUMN);
        this.createAxisAlignedPillarBlock(Blocks.POLISHED_BASALT, TexturedModel.COLUMN);
        this.createAxisAlignedPillarBlock(Blocks.BONE_BLOCK, TexturedModel.COLUMN);
        this.createRotatedVariantBlock(Blocks.DIRT);
        this.createRotatedVariantBlock(Blocks.SAND);
        this.createRotatedVariantBlock(Blocks.RED_SAND);
        this.createRotatedMirroredVariantBlock(Blocks.BEDROCK);
        this.createRotatedPillarWithHorizontalVariant(Blocks.HAY_BLOCK, TexturedModel.COLUMN, TexturedModel.COLUMN_HORIZONTAL);
        this.createRotatedPillarWithHorizontalVariant(Blocks.PURPUR_PILLAR, TexturedModel.COLUMN_ALT, TexturedModel.COLUMN_HORIZONTAL_ALT);
        this.createRotatedPillarWithHorizontalVariant(Blocks.QUARTZ_PILLAR, TexturedModel.COLUMN_ALT, TexturedModel.COLUMN_HORIZONTAL_ALT);
        this.createHorizontallyRotatedBlock(Blocks.LOOM, TexturedModel.ORIENTABLE);
        this.createPumpkins();
        this.createBeeNest(Blocks.BEE_NEST, (Function<Block, TextureMapping>)TextureMapping::orientableCube);
        this.createBeeNest(Blocks.BEEHIVE, (Function<Block, TextureMapping>)TextureMapping::orientableCubeSameEnds);
        this.createCropBlock(Blocks.BEETROOTS, BlockStateProperties.AGE_3, 0, 1, 2, 3);
        this.createCropBlock(Blocks.CARROTS, BlockStateProperties.AGE_7, 0, 0, 1, 1, 2, 2, 2, 3);
        this.createCropBlock(Blocks.NETHER_WART, BlockStateProperties.AGE_3, 0, 1, 1, 2);
        this.createCropBlock(Blocks.POTATOES, BlockStateProperties.AGE_7, 0, 0, 1, 1, 2, 2, 2, 3);
        this.createCropBlock(Blocks.WHEAT, BlockStateProperties.AGE_7, 0, 1, 2, 3, 4, 5, 6, 7);
        this.blockEntityModels(ModelLocationUtils.decorateBlockModelLocation("banner"), Blocks.OAK_PLANKS).createWithCustomBlockItemModel(ModelTemplates.BANNER_INVENTORY, Blocks.WHITE_BANNER, Blocks.ORANGE_BANNER, Blocks.MAGENTA_BANNER, Blocks.LIGHT_BLUE_BANNER, Blocks.YELLOW_BANNER, Blocks.LIME_BANNER, Blocks.PINK_BANNER, Blocks.GRAY_BANNER, Blocks.LIGHT_GRAY_BANNER, Blocks.CYAN_BANNER, Blocks.PURPLE_BANNER, Blocks.BLUE_BANNER, Blocks.BROWN_BANNER, Blocks.GREEN_BANNER, Blocks.RED_BANNER, Blocks.BLACK_BANNER).createWithoutBlockItem(Blocks.WHITE_WALL_BANNER, Blocks.ORANGE_WALL_BANNER, Blocks.MAGENTA_WALL_BANNER, Blocks.LIGHT_BLUE_WALL_BANNER, Blocks.YELLOW_WALL_BANNER, Blocks.LIME_WALL_BANNER, Blocks.PINK_WALL_BANNER, Blocks.GRAY_WALL_BANNER, Blocks.LIGHT_GRAY_WALL_BANNER, Blocks.CYAN_WALL_BANNER, Blocks.PURPLE_WALL_BANNER, Blocks.BLUE_WALL_BANNER, Blocks.BROWN_WALL_BANNER, Blocks.GREEN_WALL_BANNER, Blocks.RED_WALL_BANNER, Blocks.BLACK_WALL_BANNER);
        this.blockEntityModels(ModelLocationUtils.decorateBlockModelLocation("bed"), Blocks.OAK_PLANKS).createWithoutBlockItem(Blocks.WHITE_BED, Blocks.ORANGE_BED, Blocks.MAGENTA_BED, Blocks.LIGHT_BLUE_BED, Blocks.YELLOW_BED, Blocks.LIME_BED, Blocks.PINK_BED, Blocks.GRAY_BED, Blocks.LIGHT_GRAY_BED, Blocks.CYAN_BED, Blocks.PURPLE_BED, Blocks.BLUE_BED, Blocks.BROWN_BED, Blocks.GREEN_BED, Blocks.RED_BED, Blocks.BLACK_BED);
        this.createBedItem(Blocks.WHITE_BED, Blocks.WHITE_WOOL);
        this.createBedItem(Blocks.ORANGE_BED, Blocks.ORANGE_WOOL);
        this.createBedItem(Blocks.MAGENTA_BED, Blocks.MAGENTA_WOOL);
        this.createBedItem(Blocks.LIGHT_BLUE_BED, Blocks.LIGHT_BLUE_WOOL);
        this.createBedItem(Blocks.YELLOW_BED, Blocks.YELLOW_WOOL);
        this.createBedItem(Blocks.LIME_BED, Blocks.LIME_WOOL);
        this.createBedItem(Blocks.PINK_BED, Blocks.PINK_WOOL);
        this.createBedItem(Blocks.GRAY_BED, Blocks.GRAY_WOOL);
        this.createBedItem(Blocks.LIGHT_GRAY_BED, Blocks.LIGHT_GRAY_WOOL);
        this.createBedItem(Blocks.CYAN_BED, Blocks.CYAN_WOOL);
        this.createBedItem(Blocks.PURPLE_BED, Blocks.PURPLE_WOOL);
        this.createBedItem(Blocks.BLUE_BED, Blocks.BLUE_WOOL);
        this.createBedItem(Blocks.BROWN_BED, Blocks.BROWN_WOOL);
        this.createBedItem(Blocks.GREEN_BED, Blocks.GREEN_WOOL);
        this.createBedItem(Blocks.RED_BED, Blocks.RED_WOOL);
        this.createBedItem(Blocks.BLACK_BED, Blocks.BLACK_WOOL);
        this.blockEntityModels(ModelLocationUtils.decorateBlockModelLocation("skull"), Blocks.SOUL_SAND).createWithCustomBlockItemModel(ModelTemplates.SKULL_INVENTORY, Blocks.CREEPER_HEAD, Blocks.PLAYER_HEAD, Blocks.ZOMBIE_HEAD, Blocks.SKELETON_SKULL, Blocks.WITHER_SKELETON_SKULL).create(Blocks.DRAGON_HEAD).createWithoutBlockItem(Blocks.CREEPER_WALL_HEAD, Blocks.DRAGON_WALL_HEAD, Blocks.PLAYER_WALL_HEAD, Blocks.ZOMBIE_WALL_HEAD, Blocks.SKELETON_WALL_SKULL, Blocks.WITHER_SKELETON_WALL_SKULL);
        this.createShulkerBox(Blocks.SHULKER_BOX);
        this.createShulkerBox(Blocks.WHITE_SHULKER_BOX);
        this.createShulkerBox(Blocks.ORANGE_SHULKER_BOX);
        this.createShulkerBox(Blocks.MAGENTA_SHULKER_BOX);
        this.createShulkerBox(Blocks.LIGHT_BLUE_SHULKER_BOX);
        this.createShulkerBox(Blocks.YELLOW_SHULKER_BOX);
        this.createShulkerBox(Blocks.LIME_SHULKER_BOX);
        this.createShulkerBox(Blocks.PINK_SHULKER_BOX);
        this.createShulkerBox(Blocks.GRAY_SHULKER_BOX);
        this.createShulkerBox(Blocks.LIGHT_GRAY_SHULKER_BOX);
        this.createShulkerBox(Blocks.CYAN_SHULKER_BOX);
        this.createShulkerBox(Blocks.PURPLE_SHULKER_BOX);
        this.createShulkerBox(Blocks.BLUE_SHULKER_BOX);
        this.createShulkerBox(Blocks.BROWN_SHULKER_BOX);
        this.createShulkerBox(Blocks.GREEN_SHULKER_BOX);
        this.createShulkerBox(Blocks.RED_SHULKER_BOX);
        this.createShulkerBox(Blocks.BLACK_SHULKER_BOX);
        this.createTrivialBlock(Blocks.CONDUIT, TexturedModel.PARTICLE_ONLY);
        this.skipAutoItemBlock(Blocks.CONDUIT);
        this.blockEntityModels(ModelLocationUtils.decorateBlockModelLocation("chest"), Blocks.OAK_PLANKS).createWithoutBlockItem(Blocks.CHEST, Blocks.TRAPPED_CHEST);
        this.blockEntityModels(ModelLocationUtils.decorateBlockModelLocation("ender_chest"), Blocks.OBSIDIAN).createWithoutBlockItem(Blocks.ENDER_CHEST);
        this.blockEntityModels(Blocks.END_PORTAL, Blocks.OBSIDIAN).create(Blocks.END_PORTAL, Blocks.END_GATEWAY);
        this.createTrivialCube(Blocks.WHITE_CONCRETE);
        this.createTrivialCube(Blocks.ORANGE_CONCRETE);
        this.createTrivialCube(Blocks.MAGENTA_CONCRETE);
        this.createTrivialCube(Blocks.LIGHT_BLUE_CONCRETE);
        this.createTrivialCube(Blocks.YELLOW_CONCRETE);
        this.createTrivialCube(Blocks.LIME_CONCRETE);
        this.createTrivialCube(Blocks.PINK_CONCRETE);
        this.createTrivialCube(Blocks.GRAY_CONCRETE);
        this.createTrivialCube(Blocks.LIGHT_GRAY_CONCRETE);
        this.createTrivialCube(Blocks.CYAN_CONCRETE);
        this.createTrivialCube(Blocks.PURPLE_CONCRETE);
        this.createTrivialCube(Blocks.BLUE_CONCRETE);
        this.createTrivialCube(Blocks.BROWN_CONCRETE);
        this.createTrivialCube(Blocks.GREEN_CONCRETE);
        this.createTrivialCube(Blocks.RED_CONCRETE);
        this.createTrivialCube(Blocks.BLACK_CONCRETE);
        this.createColoredBlockWithRandomRotations(TexturedModel.CUBE, Blocks.WHITE_CONCRETE_POWDER, Blocks.ORANGE_CONCRETE_POWDER, Blocks.MAGENTA_CONCRETE_POWDER, Blocks.LIGHT_BLUE_CONCRETE_POWDER, Blocks.YELLOW_CONCRETE_POWDER, Blocks.LIME_CONCRETE_POWDER, Blocks.PINK_CONCRETE_POWDER, Blocks.GRAY_CONCRETE_POWDER, Blocks.LIGHT_GRAY_CONCRETE_POWDER, Blocks.CYAN_CONCRETE_POWDER, Blocks.PURPLE_CONCRETE_POWDER, Blocks.BLUE_CONCRETE_POWDER, Blocks.BROWN_CONCRETE_POWDER, Blocks.GREEN_CONCRETE_POWDER, Blocks.RED_CONCRETE_POWDER, Blocks.BLACK_CONCRETE_POWDER);
        this.createTrivialCube(Blocks.TERRACOTTA);
        this.createTrivialCube(Blocks.WHITE_TERRACOTTA);
        this.createTrivialCube(Blocks.ORANGE_TERRACOTTA);
        this.createTrivialCube(Blocks.MAGENTA_TERRACOTTA);
        this.createTrivialCube(Blocks.LIGHT_BLUE_TERRACOTTA);
        this.createTrivialCube(Blocks.YELLOW_TERRACOTTA);
        this.createTrivialCube(Blocks.LIME_TERRACOTTA);
        this.createTrivialCube(Blocks.PINK_TERRACOTTA);
        this.createTrivialCube(Blocks.GRAY_TERRACOTTA);
        this.createTrivialCube(Blocks.LIGHT_GRAY_TERRACOTTA);
        this.createTrivialCube(Blocks.CYAN_TERRACOTTA);
        this.createTrivialCube(Blocks.PURPLE_TERRACOTTA);
        this.createTrivialCube(Blocks.BLUE_TERRACOTTA);
        this.createTrivialCube(Blocks.BROWN_TERRACOTTA);
        this.createTrivialCube(Blocks.GREEN_TERRACOTTA);
        this.createTrivialCube(Blocks.RED_TERRACOTTA);
        this.createTrivialCube(Blocks.BLACK_TERRACOTTA);
        this.createGlassBlocks(Blocks.GLASS, Blocks.GLASS_PANE);
        this.createGlassBlocks(Blocks.WHITE_STAINED_GLASS, Blocks.WHITE_STAINED_GLASS_PANE);
        this.createGlassBlocks(Blocks.ORANGE_STAINED_GLASS, Blocks.ORANGE_STAINED_GLASS_PANE);
        this.createGlassBlocks(Blocks.MAGENTA_STAINED_GLASS, Blocks.MAGENTA_STAINED_GLASS_PANE);
        this.createGlassBlocks(Blocks.LIGHT_BLUE_STAINED_GLASS, Blocks.LIGHT_BLUE_STAINED_GLASS_PANE);
        this.createGlassBlocks(Blocks.YELLOW_STAINED_GLASS, Blocks.YELLOW_STAINED_GLASS_PANE);
        this.createGlassBlocks(Blocks.LIME_STAINED_GLASS, Blocks.LIME_STAINED_GLASS_PANE);
        this.createGlassBlocks(Blocks.PINK_STAINED_GLASS, Blocks.PINK_STAINED_GLASS_PANE);
        this.createGlassBlocks(Blocks.GRAY_STAINED_GLASS, Blocks.GRAY_STAINED_GLASS_PANE);
        this.createGlassBlocks(Blocks.LIGHT_GRAY_STAINED_GLASS, Blocks.LIGHT_GRAY_STAINED_GLASS_PANE);
        this.createGlassBlocks(Blocks.CYAN_STAINED_GLASS, Blocks.CYAN_STAINED_GLASS_PANE);
        this.createGlassBlocks(Blocks.PURPLE_STAINED_GLASS, Blocks.PURPLE_STAINED_GLASS_PANE);
        this.createGlassBlocks(Blocks.BLUE_STAINED_GLASS, Blocks.BLUE_STAINED_GLASS_PANE);
        this.createGlassBlocks(Blocks.BROWN_STAINED_GLASS, Blocks.BROWN_STAINED_GLASS_PANE);
        this.createGlassBlocks(Blocks.GREEN_STAINED_GLASS, Blocks.GREEN_STAINED_GLASS_PANE);
        this.createGlassBlocks(Blocks.RED_STAINED_GLASS, Blocks.RED_STAINED_GLASS_PANE);
        this.createGlassBlocks(Blocks.BLACK_STAINED_GLASS, Blocks.BLACK_STAINED_GLASS_PANE);
        this.createColoredBlockWithStateRotations(TexturedModel.GLAZED_TERRACOTTA, Blocks.WHITE_GLAZED_TERRACOTTA, Blocks.ORANGE_GLAZED_TERRACOTTA, Blocks.MAGENTA_GLAZED_TERRACOTTA, Blocks.LIGHT_BLUE_GLAZED_TERRACOTTA, Blocks.YELLOW_GLAZED_TERRACOTTA, Blocks.LIME_GLAZED_TERRACOTTA, Blocks.PINK_GLAZED_TERRACOTTA, Blocks.GRAY_GLAZED_TERRACOTTA, Blocks.LIGHT_GRAY_GLAZED_TERRACOTTA, Blocks.CYAN_GLAZED_TERRACOTTA, Blocks.PURPLE_GLAZED_TERRACOTTA, Blocks.BLUE_GLAZED_TERRACOTTA, Blocks.BROWN_GLAZED_TERRACOTTA, Blocks.GREEN_GLAZED_TERRACOTTA, Blocks.RED_GLAZED_TERRACOTTA, Blocks.BLACK_GLAZED_TERRACOTTA);
        this.createWoolBlocks(Blocks.WHITE_WOOL, Blocks.WHITE_CARPET);
        this.createWoolBlocks(Blocks.ORANGE_WOOL, Blocks.ORANGE_CARPET);
        this.createWoolBlocks(Blocks.MAGENTA_WOOL, Blocks.MAGENTA_CARPET);
        this.createWoolBlocks(Blocks.LIGHT_BLUE_WOOL, Blocks.LIGHT_BLUE_CARPET);
        this.createWoolBlocks(Blocks.YELLOW_WOOL, Blocks.YELLOW_CARPET);
        this.createWoolBlocks(Blocks.LIME_WOOL, Blocks.LIME_CARPET);
        this.createWoolBlocks(Blocks.PINK_WOOL, Blocks.PINK_CARPET);
        this.createWoolBlocks(Blocks.GRAY_WOOL, Blocks.GRAY_CARPET);
        this.createWoolBlocks(Blocks.LIGHT_GRAY_WOOL, Blocks.LIGHT_GRAY_CARPET);
        this.createWoolBlocks(Blocks.CYAN_WOOL, Blocks.CYAN_CARPET);
        this.createWoolBlocks(Blocks.PURPLE_WOOL, Blocks.PURPLE_CARPET);
        this.createWoolBlocks(Blocks.BLUE_WOOL, Blocks.BLUE_CARPET);
        this.createWoolBlocks(Blocks.BROWN_WOOL, Blocks.BROWN_CARPET);
        this.createWoolBlocks(Blocks.GREEN_WOOL, Blocks.GREEN_CARPET);
        this.createWoolBlocks(Blocks.RED_WOOL, Blocks.RED_CARPET);
        this.createWoolBlocks(Blocks.BLACK_WOOL, Blocks.BLACK_CARPET);
        this.createPlant(Blocks.FERN, Blocks.POTTED_FERN, TintState.TINTED);
        this.createPlant(Blocks.DANDELION, Blocks.POTTED_DANDELION, TintState.NOT_TINTED);
        this.createPlant(Blocks.POPPY, Blocks.POTTED_POPPY, TintState.NOT_TINTED);
        this.createPlant(Blocks.BLUE_ORCHID, Blocks.POTTED_BLUE_ORCHID, TintState.NOT_TINTED);
        this.createPlant(Blocks.ALLIUM, Blocks.POTTED_ALLIUM, TintState.NOT_TINTED);
        this.createPlant(Blocks.AZURE_BLUET, Blocks.POTTED_AZURE_BLUET, TintState.NOT_TINTED);
        this.createPlant(Blocks.RED_TULIP, Blocks.POTTED_RED_TULIP, TintState.NOT_TINTED);
        this.createPlant(Blocks.ORANGE_TULIP, Blocks.POTTED_ORANGE_TULIP, TintState.NOT_TINTED);
        this.createPlant(Blocks.WHITE_TULIP, Blocks.POTTED_WHITE_TULIP, TintState.NOT_TINTED);
        this.createPlant(Blocks.PINK_TULIP, Blocks.POTTED_PINK_TULIP, TintState.NOT_TINTED);
        this.createPlant(Blocks.OXEYE_DAISY, Blocks.POTTED_OXEYE_DAISY, TintState.NOT_TINTED);
        this.createPlant(Blocks.CORNFLOWER, Blocks.POTTED_CORNFLOWER, TintState.NOT_TINTED);
        this.createPlant(Blocks.LILY_OF_THE_VALLEY, Blocks.POTTED_LILY_OF_THE_VALLEY, TintState.NOT_TINTED);
        this.createPlant(Blocks.WITHER_ROSE, Blocks.POTTED_WITHER_ROSE, TintState.NOT_TINTED);
        this.createPlant(Blocks.RED_MUSHROOM, Blocks.POTTED_RED_MUSHROOM, TintState.NOT_TINTED);
        this.createPlant(Blocks.BROWN_MUSHROOM, Blocks.POTTED_BROWN_MUSHROOM, TintState.NOT_TINTED);
        this.createPlant(Blocks.DEAD_BUSH, Blocks.POTTED_DEAD_BUSH, TintState.NOT_TINTED);
        this.createMushroomBlock(Blocks.BROWN_MUSHROOM_BLOCK);
        this.createMushroomBlock(Blocks.RED_MUSHROOM_BLOCK);
        this.createMushroomBlock(Blocks.MUSHROOM_STEM);
        this.createCrossBlockWithDefaultItem(Blocks.GRASS, TintState.TINTED);
        this.createCrossBlock(Blocks.SUGAR_CANE, TintState.TINTED);
        this.createSimpleFlatItemModel(Items.SUGAR_CANE);
        this.createGrowingPlant(Blocks.KELP, Blocks.KELP_PLANT, TintState.TINTED);
        this.createSimpleFlatItemModel(Items.KELP);
        this.skipAutoItemBlock(Blocks.KELP_PLANT);
        this.createGrowingPlant(Blocks.WEEPING_VINES, Blocks.WEEPING_VINES_PLANT, TintState.NOT_TINTED);
        this.createGrowingPlant(Blocks.TWISTING_VINES, Blocks.TWISTING_VINES_PLANT, TintState.NOT_TINTED);
        this.createSimpleFlatItemModel(Blocks.WEEPING_VINES, "_plant");
        this.skipAutoItemBlock(Blocks.WEEPING_VINES_PLANT);
        this.createSimpleFlatItemModel(Blocks.TWISTING_VINES, "_plant");
        this.skipAutoItemBlock(Blocks.TWISTING_VINES_PLANT);
        this.createCrossBlockWithDefaultItem(Blocks.BAMBOO_SAPLING, TintState.TINTED, TextureMapping.cross(TextureMapping.getBlockTexture(Blocks.BAMBOO, "_stage0")));
        this.createBamboo();
        this.createCrossBlockWithDefaultItem(Blocks.COBWEB, TintState.NOT_TINTED);
        this.createDoublePlant(Blocks.LILAC, TintState.NOT_TINTED);
        this.createDoublePlant(Blocks.ROSE_BUSH, TintState.NOT_TINTED);
        this.createDoublePlant(Blocks.PEONY, TintState.NOT_TINTED);
        this.createDoublePlant(Blocks.TALL_GRASS, TintState.TINTED);
        this.createDoublePlant(Blocks.LARGE_FERN, TintState.TINTED);
        this.createSunflower();
        this.createTallSeagrass();
        this.createCoral(Blocks.TUBE_CORAL, Blocks.DEAD_TUBE_CORAL, Blocks.TUBE_CORAL_BLOCK, Blocks.DEAD_TUBE_CORAL_BLOCK, Blocks.TUBE_CORAL_FAN, Blocks.DEAD_TUBE_CORAL_FAN, Blocks.TUBE_CORAL_WALL_FAN, Blocks.DEAD_TUBE_CORAL_WALL_FAN);
        this.createCoral(Blocks.BRAIN_CORAL, Blocks.DEAD_BRAIN_CORAL, Blocks.BRAIN_CORAL_BLOCK, Blocks.DEAD_BRAIN_CORAL_BLOCK, Blocks.BRAIN_CORAL_FAN, Blocks.DEAD_BRAIN_CORAL_FAN, Blocks.BRAIN_CORAL_WALL_FAN, Blocks.DEAD_BRAIN_CORAL_WALL_FAN);
        this.createCoral(Blocks.BUBBLE_CORAL, Blocks.DEAD_BUBBLE_CORAL, Blocks.BUBBLE_CORAL_BLOCK, Blocks.DEAD_BUBBLE_CORAL_BLOCK, Blocks.BUBBLE_CORAL_FAN, Blocks.DEAD_BUBBLE_CORAL_FAN, Blocks.BUBBLE_CORAL_WALL_FAN, Blocks.DEAD_BUBBLE_CORAL_WALL_FAN);
        this.createCoral(Blocks.FIRE_CORAL, Blocks.DEAD_FIRE_CORAL, Blocks.FIRE_CORAL_BLOCK, Blocks.DEAD_FIRE_CORAL_BLOCK, Blocks.FIRE_CORAL_FAN, Blocks.DEAD_FIRE_CORAL_FAN, Blocks.FIRE_CORAL_WALL_FAN, Blocks.DEAD_FIRE_CORAL_WALL_FAN);
        this.createCoral(Blocks.HORN_CORAL, Blocks.DEAD_HORN_CORAL, Blocks.HORN_CORAL_BLOCK, Blocks.DEAD_HORN_CORAL_BLOCK, Blocks.HORN_CORAL_FAN, Blocks.DEAD_HORN_CORAL_FAN, Blocks.HORN_CORAL_WALL_FAN, Blocks.DEAD_HORN_CORAL_WALL_FAN);
        this.createStems(Blocks.MELON_STEM, Blocks.ATTACHED_MELON_STEM);
        this.createStems(Blocks.PUMPKIN_STEM, Blocks.ATTACHED_PUMPKIN_STEM);
        this.family(Blocks.ACACIA_PLANKS).button(Blocks.ACACIA_BUTTON).fence(Blocks.ACACIA_FENCE).fenceGate(Blocks.ACACIA_FENCE_GATE).pressurePlate(Blocks.ACACIA_PRESSURE_PLATE).sign(Blocks.ACACIA_SIGN, Blocks.ACACIA_WALL_SIGN).slab(Blocks.ACACIA_SLAB).stairs(Blocks.ACACIA_STAIRS);
        this.createDoor(Blocks.ACACIA_DOOR);
        this.createOrientableTrapdoor(Blocks.ACACIA_TRAPDOOR);
        this.woodProvider(Blocks.ACACIA_LOG).logWithHorizontal(Blocks.ACACIA_LOG).wood(Blocks.ACACIA_WOOD);
        this.woodProvider(Blocks.STRIPPED_ACACIA_LOG).logWithHorizontal(Blocks.STRIPPED_ACACIA_LOG).wood(Blocks.STRIPPED_ACACIA_WOOD);
        this.createPlant(Blocks.ACACIA_SAPLING, Blocks.POTTED_ACACIA_SAPLING, TintState.NOT_TINTED);
        this.createTrivialBlock(Blocks.ACACIA_LEAVES, TexturedModel.LEAVES);
        this.family(Blocks.BIRCH_PLANKS).button(Blocks.BIRCH_BUTTON).fence(Blocks.BIRCH_FENCE).fenceGate(Blocks.BIRCH_FENCE_GATE).pressurePlate(Blocks.BIRCH_PRESSURE_PLATE).sign(Blocks.BIRCH_SIGN, Blocks.BIRCH_WALL_SIGN).slab(Blocks.BIRCH_SLAB).stairs(Blocks.BIRCH_STAIRS);
        this.createDoor(Blocks.BIRCH_DOOR);
        this.createOrientableTrapdoor(Blocks.BIRCH_TRAPDOOR);
        this.woodProvider(Blocks.BIRCH_LOG).logWithHorizontal(Blocks.BIRCH_LOG).wood(Blocks.BIRCH_WOOD);
        this.woodProvider(Blocks.STRIPPED_BIRCH_LOG).logWithHorizontal(Blocks.STRIPPED_BIRCH_LOG).wood(Blocks.STRIPPED_BIRCH_WOOD);
        this.createPlant(Blocks.BIRCH_SAPLING, Blocks.POTTED_BIRCH_SAPLING, TintState.NOT_TINTED);
        this.createTrivialBlock(Blocks.BIRCH_LEAVES, TexturedModel.LEAVES);
        this.family(Blocks.OAK_PLANKS).button(Blocks.OAK_BUTTON).fence(Blocks.OAK_FENCE).fenceGate(Blocks.OAK_FENCE_GATE).pressurePlate(Blocks.OAK_PRESSURE_PLATE).sign(Blocks.OAK_SIGN, Blocks.OAK_WALL_SIGN).slab(Blocks.OAK_SLAB).slab(Blocks.PETRIFIED_OAK_SLAB).stairs(Blocks.OAK_STAIRS);
        this.createDoor(Blocks.OAK_DOOR);
        this.createTrapdoor(Blocks.OAK_TRAPDOOR);
        this.woodProvider(Blocks.OAK_LOG).logWithHorizontal(Blocks.OAK_LOG).wood(Blocks.OAK_WOOD);
        this.woodProvider(Blocks.STRIPPED_OAK_LOG).logWithHorizontal(Blocks.STRIPPED_OAK_LOG).wood(Blocks.STRIPPED_OAK_WOOD);
        this.createPlant(Blocks.OAK_SAPLING, Blocks.POTTED_OAK_SAPLING, TintState.NOT_TINTED);
        this.createTrivialBlock(Blocks.OAK_LEAVES, TexturedModel.LEAVES);
        this.family(Blocks.SPRUCE_PLANKS).button(Blocks.SPRUCE_BUTTON).fence(Blocks.SPRUCE_FENCE).fenceGate(Blocks.SPRUCE_FENCE_GATE).pressurePlate(Blocks.SPRUCE_PRESSURE_PLATE).sign(Blocks.SPRUCE_SIGN, Blocks.SPRUCE_WALL_SIGN).slab(Blocks.SPRUCE_SLAB).stairs(Blocks.SPRUCE_STAIRS);
        this.createDoor(Blocks.SPRUCE_DOOR);
        this.createOrientableTrapdoor(Blocks.SPRUCE_TRAPDOOR);
        this.woodProvider(Blocks.SPRUCE_LOG).logWithHorizontal(Blocks.SPRUCE_LOG).wood(Blocks.SPRUCE_WOOD);
        this.woodProvider(Blocks.STRIPPED_SPRUCE_LOG).logWithHorizontal(Blocks.STRIPPED_SPRUCE_LOG).wood(Blocks.STRIPPED_SPRUCE_WOOD);
        this.createPlant(Blocks.SPRUCE_SAPLING, Blocks.POTTED_SPRUCE_SAPLING, TintState.NOT_TINTED);
        this.createTrivialBlock(Blocks.SPRUCE_LEAVES, TexturedModel.LEAVES);
        this.family(Blocks.DARK_OAK_PLANKS).button(Blocks.DARK_OAK_BUTTON).fence(Blocks.DARK_OAK_FENCE).fenceGate(Blocks.DARK_OAK_FENCE_GATE).pressurePlate(Blocks.DARK_OAK_PRESSURE_PLATE).sign(Blocks.DARK_OAK_SIGN, Blocks.DARK_OAK_WALL_SIGN).slab(Blocks.DARK_OAK_SLAB).stairs(Blocks.DARK_OAK_STAIRS);
        this.createDoor(Blocks.DARK_OAK_DOOR);
        this.createTrapdoor(Blocks.DARK_OAK_TRAPDOOR);
        this.woodProvider(Blocks.DARK_OAK_LOG).logWithHorizontal(Blocks.DARK_OAK_LOG).wood(Blocks.DARK_OAK_WOOD);
        this.woodProvider(Blocks.STRIPPED_DARK_OAK_LOG).logWithHorizontal(Blocks.STRIPPED_DARK_OAK_LOG).wood(Blocks.STRIPPED_DARK_OAK_WOOD);
        this.createPlant(Blocks.DARK_OAK_SAPLING, Blocks.POTTED_DARK_OAK_SAPLING, TintState.NOT_TINTED);
        this.createTrivialBlock(Blocks.DARK_OAK_LEAVES, TexturedModel.LEAVES);
        this.family(Blocks.JUNGLE_PLANKS).button(Blocks.JUNGLE_BUTTON).fence(Blocks.JUNGLE_FENCE).fenceGate(Blocks.JUNGLE_FENCE_GATE).pressurePlate(Blocks.JUNGLE_PRESSURE_PLATE).sign(Blocks.JUNGLE_SIGN, Blocks.JUNGLE_WALL_SIGN).slab(Blocks.JUNGLE_SLAB).stairs(Blocks.JUNGLE_STAIRS);
        this.createDoor(Blocks.JUNGLE_DOOR);
        this.createOrientableTrapdoor(Blocks.JUNGLE_TRAPDOOR);
        this.woodProvider(Blocks.JUNGLE_LOG).logWithHorizontal(Blocks.JUNGLE_LOG).wood(Blocks.JUNGLE_WOOD);
        this.woodProvider(Blocks.STRIPPED_JUNGLE_LOG).logWithHorizontal(Blocks.STRIPPED_JUNGLE_LOG).wood(Blocks.STRIPPED_JUNGLE_WOOD);
        this.createPlant(Blocks.JUNGLE_SAPLING, Blocks.POTTED_JUNGLE_SAPLING, TintState.NOT_TINTED);
        this.createTrivialBlock(Blocks.JUNGLE_LEAVES, TexturedModel.LEAVES);
        this.family(Blocks.CRIMSON_PLANKS).button(Blocks.CRIMSON_BUTTON).fence(Blocks.CRIMSON_FENCE).fenceGate(Blocks.CRIMSON_FENCE_GATE).pressurePlate(Blocks.CRIMSON_PRESSURE_PLATE).sign(Blocks.CRIMSON_SIGN, Blocks.CRIMSON_WALL_SIGN).slab(Blocks.CRIMSON_SLAB).stairs(Blocks.CRIMSON_STAIRS);
        this.createDoor(Blocks.CRIMSON_DOOR);
        this.createOrientableTrapdoor(Blocks.CRIMSON_TRAPDOOR);
        this.woodProvider(Blocks.CRIMSON_STEM).log(Blocks.CRIMSON_STEM).wood(Blocks.CRIMSON_HYPHAE);
        this.woodProvider(Blocks.STRIPPED_CRIMSON_STEM).log(Blocks.STRIPPED_CRIMSON_STEM).wood(Blocks.STRIPPED_CRIMSON_HYPHAE);
        this.createPlant(Blocks.CRIMSON_FUNGUS, Blocks.POTTED_CRIMSON_FUNGUS, TintState.NOT_TINTED);
        this.createNetherRoots(Blocks.CRIMSON_ROOTS, Blocks.POTTED_CRIMSON_ROOTS);
        this.family(Blocks.WARPED_PLANKS).button(Blocks.WARPED_BUTTON).fence(Blocks.WARPED_FENCE).fenceGate(Blocks.WARPED_FENCE_GATE).pressurePlate(Blocks.WARPED_PRESSURE_PLATE).sign(Blocks.WARPED_SIGN, Blocks.WARPED_WALL_SIGN).slab(Blocks.WARPED_SLAB).stairs(Blocks.WARPED_STAIRS);
        this.createDoor(Blocks.WARPED_DOOR);
        this.createOrientableTrapdoor(Blocks.WARPED_TRAPDOOR);
        this.woodProvider(Blocks.WARPED_STEM).log(Blocks.WARPED_STEM).wood(Blocks.WARPED_HYPHAE);
        this.woodProvider(Blocks.STRIPPED_WARPED_STEM).log(Blocks.STRIPPED_WARPED_STEM).wood(Blocks.STRIPPED_WARPED_HYPHAE);
        this.createPlant(Blocks.WARPED_FUNGUS, Blocks.POTTED_WARPED_FUNGUS, TintState.NOT_TINTED);
        this.createNetherRoots(Blocks.WARPED_ROOTS, Blocks.POTTED_WARPED_ROOTS);
        this.createCrossBlock(Blocks.NETHER_SPROUTS, TintState.NOT_TINTED);
        this.createSimpleFlatItemModel(Items.NETHER_SPROUTS);
        this.family(TextureMapping.cube(Blocks.STONE)).fullBlock((Function<TextureMapping, ResourceLocation>)(iz -> {
            final ResourceLocation vk3 = ModelTemplates.CUBE_ALL.create(Blocks.STONE, iz, this.modelOutput);
            final ResourceLocation vk4 = ModelTemplates.CUBE_MIRRORED_ALL.create(Blocks.STONE, iz, this.modelOutput);
            this.blockStateOutput.accept(createRotatedVariant(Blocks.STONE, vk3, vk4));
            return vk3;
        })).slab(Blocks.STONE_SLAB).pressurePlate(Blocks.STONE_PRESSURE_PLATE).button(Blocks.STONE_BUTTON).stairs(Blocks.STONE_STAIRS);
        this.createDoor(Blocks.IRON_DOOR);
        this.createTrapdoor(Blocks.IRON_TRAPDOOR);
        this.family(Blocks.STONE_BRICKS).wall(Blocks.STONE_BRICK_WALL).stairs(Blocks.STONE_BRICK_STAIRS).slab(Blocks.STONE_BRICK_SLAB);
        this.family(Blocks.MOSSY_STONE_BRICKS).wall(Blocks.MOSSY_STONE_BRICK_WALL).stairs(Blocks.MOSSY_STONE_BRICK_STAIRS).slab(Blocks.MOSSY_STONE_BRICK_SLAB);
        this.family(Blocks.COBBLESTONE).wall(Blocks.COBBLESTONE_WALL).stairs(Blocks.COBBLESTONE_STAIRS).slab(Blocks.COBBLESTONE_SLAB);
        this.family(Blocks.MOSSY_COBBLESTONE).wall(Blocks.MOSSY_COBBLESTONE_WALL).stairs(Blocks.MOSSY_COBBLESTONE_STAIRS).slab(Blocks.MOSSY_COBBLESTONE_SLAB);
        this.family(Blocks.PRISMARINE).wall(Blocks.PRISMARINE_WALL).stairs(Blocks.PRISMARINE_STAIRS).slab(Blocks.PRISMARINE_SLAB);
        this.family(Blocks.PRISMARINE_BRICKS).stairs(Blocks.PRISMARINE_BRICK_STAIRS).slab(Blocks.PRISMARINE_BRICK_SLAB);
        this.family(Blocks.DARK_PRISMARINE).stairs(Blocks.DARK_PRISMARINE_STAIRS).slab(Blocks.DARK_PRISMARINE_SLAB);
        this.family(Blocks.SANDSTONE, TexturedModel.TOP_BOTTOM_WITH_WALL).wall(Blocks.SANDSTONE_WALL).stairs(Blocks.SANDSTONE_STAIRS).slab(Blocks.SANDSTONE_SLAB);
        this.family(Blocks.SMOOTH_SANDSTONE, TexturedModel.createAllSame(TextureMapping.getBlockTexture(Blocks.SANDSTONE, "_top"))).slab(Blocks.SMOOTH_SANDSTONE_SLAB).stairs(Blocks.SMOOTH_SANDSTONE_STAIRS);
        this.family(Blocks.CUT_SANDSTONE, TexturedModel.COLUMN.get(Blocks.SANDSTONE).updateTextures((Consumer<TextureMapping>)(iz -> iz.put(TextureSlot.SIDE, TextureMapping.getBlockTexture(Blocks.CUT_SANDSTONE))))).slab(Blocks.CUT_SANDSTONE_SLAB);
        this.family(Blocks.RED_SANDSTONE, TexturedModel.TOP_BOTTOM_WITH_WALL).wall(Blocks.RED_SANDSTONE_WALL).stairs(Blocks.RED_SANDSTONE_STAIRS).slab(Blocks.RED_SANDSTONE_SLAB);
        this.family(Blocks.SMOOTH_RED_SANDSTONE, TexturedModel.createAllSame(TextureMapping.getBlockTexture(Blocks.RED_SANDSTONE, "_top"))).slab(Blocks.SMOOTH_RED_SANDSTONE_SLAB).stairs(Blocks.SMOOTH_RED_SANDSTONE_STAIRS);
        this.family(Blocks.CUT_RED_SANDSTONE, TexturedModel.COLUMN.get(Blocks.RED_SANDSTONE).updateTextures((Consumer<TextureMapping>)(iz -> iz.put(TextureSlot.SIDE, TextureMapping.getBlockTexture(Blocks.CUT_RED_SANDSTONE))))).slab(Blocks.CUT_RED_SANDSTONE_SLAB);
        this.family(Blocks.BRICKS).wall(Blocks.BRICK_WALL).stairs(Blocks.BRICK_STAIRS).slab(Blocks.BRICK_SLAB);
        this.family(Blocks.NETHER_BRICKS).fence(Blocks.NETHER_BRICK_FENCE).wall(Blocks.NETHER_BRICK_WALL).stairs(Blocks.NETHER_BRICK_STAIRS).slab(Blocks.NETHER_BRICK_SLAB);
        this.family(Blocks.PURPUR_BLOCK).stairs(Blocks.PURPUR_STAIRS).slab(Blocks.PURPUR_SLAB);
        this.family(Blocks.DIORITE).wall(Blocks.DIORITE_WALL).stairs(Blocks.DIORITE_STAIRS).slab(Blocks.DIORITE_SLAB);
        this.family(Blocks.POLISHED_DIORITE).stairs(Blocks.POLISHED_DIORITE_STAIRS).slab(Blocks.POLISHED_DIORITE_SLAB);
        this.family(Blocks.GRANITE).wall(Blocks.GRANITE_WALL).stairs(Blocks.GRANITE_STAIRS).slab(Blocks.GRANITE_SLAB);
        this.family(Blocks.POLISHED_GRANITE).stairs(Blocks.POLISHED_GRANITE_STAIRS).slab(Blocks.POLISHED_GRANITE_SLAB);
        this.family(Blocks.ANDESITE).wall(Blocks.ANDESITE_WALL).stairs(Blocks.ANDESITE_STAIRS).slab(Blocks.ANDESITE_SLAB);
        this.family(Blocks.POLISHED_ANDESITE).stairs(Blocks.POLISHED_ANDESITE_STAIRS).slab(Blocks.POLISHED_ANDESITE_SLAB);
        this.family(Blocks.END_STONE_BRICKS).wall(Blocks.END_STONE_BRICK_WALL).stairs(Blocks.END_STONE_BRICK_STAIRS).slab(Blocks.END_STONE_BRICK_SLAB);
        this.family(Blocks.QUARTZ_BLOCK, TexturedModel.COLUMN).stairs(Blocks.QUARTZ_STAIRS).slab(Blocks.QUARTZ_SLAB);
        this.family(Blocks.SMOOTH_QUARTZ, TexturedModel.createAllSame(TextureMapping.getBlockTexture(Blocks.QUARTZ_BLOCK, "_bottom"))).stairs(Blocks.SMOOTH_QUARTZ_STAIRS).slab(Blocks.SMOOTH_QUARTZ_SLAB);
        this.family(Blocks.RED_NETHER_BRICKS).slab(Blocks.RED_NETHER_BRICK_SLAB).stairs(Blocks.RED_NETHER_BRICK_STAIRS).wall(Blocks.RED_NETHER_BRICK_WALL);
        this.family(Blocks.BLACKSTONE, TexturedModel.COLUMN_WITH_WALL).wall(Blocks.BLACKSTONE_WALL).stairs(Blocks.BLACKSTONE_STAIRS).slab(Blocks.BLACKSTONE_SLAB);
        this.family(Blocks.POLISHED_BLACKSTONE_BRICKS).wall(Blocks.POLISHED_BLACKSTONE_BRICK_WALL).stairs(Blocks.POLISHED_BLACKSTONE_BRICK_STAIRS).slab(Blocks.POLISHED_BLACKSTONE_BRICK_SLAB);
        this.family(Blocks.POLISHED_BLACKSTONE).wall(Blocks.POLISHED_BLACKSTONE_WALL).pressurePlate(Blocks.POLISHED_BLACKSTONE_PRESSURE_PLATE).button(Blocks.POLISHED_BLACKSTONE_BUTTON).stairs(Blocks.POLISHED_BLACKSTONE_STAIRS).slab(Blocks.POLISHED_BLACKSTONE_SLAB);
        this.createSmoothStoneSlab();
        this.createPassiveRail(Blocks.RAIL);
        this.createActiveRail(Blocks.POWERED_RAIL);
        this.createActiveRail(Blocks.DETECTOR_RAIL);
        this.createActiveRail(Blocks.ACTIVATOR_RAIL);
        this.createComparator();
        this.createCommandBlock(Blocks.COMMAND_BLOCK);
        this.createCommandBlock(Blocks.REPEATING_COMMAND_BLOCK);
        this.createCommandBlock(Blocks.CHAIN_COMMAND_BLOCK);
        this.createAnvil(Blocks.ANVIL);
        this.createAnvil(Blocks.CHIPPED_ANVIL);
        this.createAnvil(Blocks.DAMAGED_ANVIL);
        this.createBarrel();
        this.createBell();
        this.createFurnace(Blocks.FURNACE, TexturedModel.ORIENTABLE_ONLY_TOP);
        this.createFurnace(Blocks.BLAST_FURNACE, TexturedModel.ORIENTABLE_ONLY_TOP);
        this.createFurnace(Blocks.SMOKER, TexturedModel.ORIENTABLE);
        this.createRedstoneWire();
        this.createRespawnAnchor();
        this.copyModel(Blocks.CHISELED_STONE_BRICKS, Blocks.INFESTED_CHISELED_STONE_BRICKS);
        this.copyModel(Blocks.COBBLESTONE, Blocks.INFESTED_COBBLESTONE);
        this.copyModel(Blocks.CRACKED_STONE_BRICKS, Blocks.INFESTED_CRACKED_STONE_BRICKS);
        this.copyModel(Blocks.MOSSY_STONE_BRICKS, Blocks.INFESTED_MOSSY_STONE_BRICKS);
        this.createInfestedStone();
        this.copyModel(Blocks.STONE_BRICKS, Blocks.INFESTED_STONE_BRICKS);
        SpawnEggItem.eggs().forEach(bmx -> this.delegateItemModel(bmx, ModelLocationUtils.decorateItemModelLocation("template_spawn_egg")));
    }
    
    class BlockFamilyProvider {
        private final TextureMapping mapping;
        @Nullable
        private ResourceLocation fullBlock;
        
        public BlockFamilyProvider(final TextureMapping iz) {
            this.mapping = iz;
        }
        
        public BlockFamilyProvider fullBlock(final Block bul, final ModelTemplate ix) {
            this.fullBlock = ix.create(bul, this.mapping, BlockModelGenerators.this.modelOutput);
            BlockModelGenerators.this.blockStateOutput.accept(createSimpleBlock(bul, this.fullBlock));
            return this;
        }
        
        public BlockFamilyProvider fullBlock(final Function<TextureMapping, ResourceLocation> function) {
            this.fullBlock = (ResourceLocation)function.apply(this.mapping);
            return this;
        }
        
        public BlockFamilyProvider button(final Block bul) {
            final ResourceLocation vk3 = ModelTemplates.BUTTON.create(bul, this.mapping, BlockModelGenerators.this.modelOutput);
            final ResourceLocation vk4 = ModelTemplates.BUTTON_PRESSED.create(bul, this.mapping, BlockModelGenerators.this.modelOutput);
            BlockModelGenerators.this.blockStateOutput.accept(createButton(bul, vk3, vk4));
            final ResourceLocation vk5 = ModelTemplates.BUTTON_INVENTORY.create(bul, this.mapping, BlockModelGenerators.this.modelOutput);
            BlockModelGenerators.this.delegateItemModel(bul, vk5);
            return this;
        }
        
        public BlockFamilyProvider wall(final Block bul) {
            final ResourceLocation vk3 = ModelTemplates.WALL_POST.create(bul, this.mapping, BlockModelGenerators.this.modelOutput);
            final ResourceLocation vk4 = ModelTemplates.WALL_LOW_SIDE.create(bul, this.mapping, BlockModelGenerators.this.modelOutput);
            final ResourceLocation vk5 = ModelTemplates.WALL_TALL_SIDE.create(bul, this.mapping, BlockModelGenerators.this.modelOutput);
            BlockModelGenerators.this.blockStateOutput.accept(createWall(bul, vk3, vk4, vk5));
            final ResourceLocation vk6 = ModelTemplates.WALL_INVENTORY.create(bul, this.mapping, BlockModelGenerators.this.modelOutput);
            BlockModelGenerators.this.delegateItemModel(bul, vk6);
            return this;
        }
        
        public BlockFamilyProvider fence(final Block bul) {
            final ResourceLocation vk3 = ModelTemplates.FENCE_POST.create(bul, this.mapping, BlockModelGenerators.this.modelOutput);
            final ResourceLocation vk4 = ModelTemplates.FENCE_SIDE.create(bul, this.mapping, BlockModelGenerators.this.modelOutput);
            BlockModelGenerators.this.blockStateOutput.accept(createFence(bul, vk3, vk4));
            final ResourceLocation vk5 = ModelTemplates.FENCE_INVENTORY.create(bul, this.mapping, BlockModelGenerators.this.modelOutput);
            BlockModelGenerators.this.delegateItemModel(bul, vk5);
            return this;
        }
        
        public BlockFamilyProvider fenceGate(final Block bul) {
            final ResourceLocation vk3 = ModelTemplates.FENCE_GATE_OPEN.create(bul, this.mapping, BlockModelGenerators.this.modelOutput);
            final ResourceLocation vk4 = ModelTemplates.FENCE_GATE_CLOSED.create(bul, this.mapping, BlockModelGenerators.this.modelOutput);
            final ResourceLocation vk5 = ModelTemplates.FENCE_GATE_WALL_OPEN.create(bul, this.mapping, BlockModelGenerators.this.modelOutput);
            final ResourceLocation vk6 = ModelTemplates.FENCE_GATE_WALL_CLOSED.create(bul, this.mapping, BlockModelGenerators.this.modelOutput);
            BlockModelGenerators.this.blockStateOutput.accept(createFenceGate(bul, vk3, vk4, vk5, vk6));
            return this;
        }
        
        public BlockFamilyProvider pressurePlate(final Block bul) {
            final ResourceLocation vk3 = ModelTemplates.PRESSURE_PLATE_UP.create(bul, this.mapping, BlockModelGenerators.this.modelOutput);
            final ResourceLocation vk4 = ModelTemplates.PRESSURE_PLATE_DOWN.create(bul, this.mapping, BlockModelGenerators.this.modelOutput);
            BlockModelGenerators.this.blockStateOutput.accept(createPressurePlate(bul, vk3, vk4));
            return this;
        }
        
        public BlockFamilyProvider sign(final Block bul1, final Block bul2) {
            final ResourceLocation vk4 = ModelTemplates.PARTICLE_ONLY.create(bul1, this.mapping, BlockModelGenerators.this.modelOutput);
            BlockModelGenerators.this.blockStateOutput.accept(createSimpleBlock(bul1, vk4));
            BlockModelGenerators.this.blockStateOutput.accept(createSimpleBlock(bul2, vk4));
            BlockModelGenerators.this.createSimpleFlatItemModel(bul1.asItem());
            BlockModelGenerators.this.skipAutoItemBlock(bul2);
            return this;
        }
        
        public BlockFamilyProvider slab(final Block bul) {
            if (this.fullBlock == null) {
                throw new IllegalStateException("Full block not generated yet");
            }
            final ResourceLocation vk3 = ModelTemplates.SLAB_BOTTOM.create(bul, this.mapping, BlockModelGenerators.this.modelOutput);
            final ResourceLocation vk4 = ModelTemplates.SLAB_TOP.create(bul, this.mapping, BlockModelGenerators.this.modelOutput);
            BlockModelGenerators.this.blockStateOutput.accept(createSlab(bul, vk3, vk4, this.fullBlock));
            return this;
        }
        
        public BlockFamilyProvider stairs(final Block bul) {
            final ResourceLocation vk3 = ModelTemplates.STAIRS_INNER.create(bul, this.mapping, BlockModelGenerators.this.modelOutput);
            final ResourceLocation vk4 = ModelTemplates.STAIRS_STRAIGHT.create(bul, this.mapping, BlockModelGenerators.this.modelOutput);
            final ResourceLocation vk5 = ModelTemplates.STAIRS_OUTER.create(bul, this.mapping, BlockModelGenerators.this.modelOutput);
            BlockModelGenerators.this.blockStateOutput.accept(createStairs(bul, vk3, vk4, vk5));
            return this;
        }
    }
    
    class WoodProvider {
        private final TextureMapping logMapping;
        
        public WoodProvider(final TextureMapping iz) {
            this.logMapping = iz;
        }
        
        public WoodProvider wood(final Block bul) {
            final TextureMapping iz3 = this.logMapping.copyAndUpdate(TextureSlot.END, this.logMapping.get(TextureSlot.SIDE));
            final ResourceLocation vk4 = ModelTemplates.CUBE_COLUMN.create(bul, iz3, BlockModelGenerators.this.modelOutput);
            BlockModelGenerators.this.blockStateOutput.accept(createAxisAlignedPillarBlock(bul, vk4));
            return this;
        }
        
        public WoodProvider log(final Block bul) {
            final ResourceLocation vk3 = ModelTemplates.CUBE_COLUMN.create(bul, this.logMapping, BlockModelGenerators.this.modelOutput);
            BlockModelGenerators.this.blockStateOutput.accept(createAxisAlignedPillarBlock(bul, vk3));
            return this;
        }
        
        public WoodProvider logWithHorizontal(final Block bul) {
            final ResourceLocation vk3 = ModelTemplates.CUBE_COLUMN.create(bul, this.logMapping, BlockModelGenerators.this.modelOutput);
            final ResourceLocation vk4 = ModelTemplates.CUBE_COLUMN_HORIZONTAL.create(bul, this.logMapping, BlockModelGenerators.this.modelOutput);
            BlockModelGenerators.this.blockStateOutput.accept(createRotatedPillarWithHorizontalVariant(bul, vk3, vk4));
            return this;
        }
    }
    
    enum TintState {
        TINTED, 
        NOT_TINTED;
        
        public ModelTemplate getCross() {
            return (this == TintState.TINTED) ? ModelTemplates.TINTED_CROSS : ModelTemplates.CROSS;
        }
        
        public ModelTemplate getCrossPot() {
            return (this == TintState.TINTED) ? ModelTemplates.TINTED_FLOWER_POT_CROSS : ModelTemplates.FLOWER_POT_CROSS;
        }
    }
    
    class BlockEntityModelGenerator {
        private final ResourceLocation baseModel;
        
        public BlockEntityModelGenerator(final ResourceLocation vk, final Block bul) {
            this.baseModel = ModelTemplates.PARTICLE_ONLY.create(vk, TextureMapping.particle(bul), BlockModelGenerators.this.modelOutput);
        }
        
        public BlockEntityModelGenerator create(final Block... arr) {
            for (final Block bul6 : arr) {
                BlockModelGenerators.this.blockStateOutput.accept(createSimpleBlock(bul6, this.baseModel));
            }
            return this;
        }
        
        public BlockEntityModelGenerator createWithoutBlockItem(final Block... arr) {
            for (final Block bul6 : arr) {
                BlockModelGenerators.this.skipAutoItemBlock(bul6);
            }
            return this.create(arr);
        }
        
        public BlockEntityModelGenerator createWithCustomBlockItemModel(final ModelTemplate ix, final Block... arr) {
            for (final Block bul7 : arr) {
                ix.create(ModelLocationUtils.getModelLocation(bul7.asItem()), TextureMapping.particle(bul7), BlockModelGenerators.this.modelOutput);
            }
            return this.create(arr);
        }
    }
}

package net.minecraft.client.renderer;

import java.util.Comparator;
import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.function.Function;
import com.google.common.collect.ImmutableList;
import java.util.stream.Stream;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.entity.EnderChestBlockEntity;
import net.minecraft.world.level.block.entity.TrappedChestBlockEntity;
import net.minecraft.world.level.block.state.properties.ChestType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BannerPattern;
import java.util.function.Consumer;
import net.minecraft.world.level.block.state.properties.WoodType;
import java.util.Map;
import java.util.List;
import net.minecraft.client.resources.model.Material;
import net.minecraft.resources.ResourceLocation;

public class Sheets {
    public static final ResourceLocation SHULKER_SHEET;
    public static final ResourceLocation BED_SHEET;
    public static final ResourceLocation BANNER_SHEET;
    public static final ResourceLocation SHIELD_SHEET;
    public static final ResourceLocation SIGN_SHEET;
    public static final ResourceLocation CHEST_SHEET;
    private static final RenderType SHULKER_BOX_SHEET_TYPE;
    private static final RenderType BED_SHEET_TYPE;
    private static final RenderType BANNER_SHEET_TYPE;
    private static final RenderType SHIELD_SHEET_TYPE;
    private static final RenderType SIGN_SHEET_TYPE;
    private static final RenderType CHEST_SHEET_TYPE;
    private static final RenderType SOLID_BLOCK_SHEET;
    private static final RenderType CUTOUT_BLOCK_SHEET;
    private static final RenderType TRANSLUCENT_ITEM_CULL_BLOCK_SHEET;
    private static final RenderType TRANSLUCENT_CULL_BLOCK_SHEET;
    public static final Material DEFAULT_SHULKER_TEXTURE_LOCATION;
    public static final List<Material> SHULKER_TEXTURE_LOCATION;
    public static final Map<WoodType, Material> SIGN_MATERIALS;
    public static final Material[] BED_TEXTURES;
    public static final Material CHEST_TRAP_LOCATION;
    public static final Material CHEST_TRAP_LOCATION_LEFT;
    public static final Material CHEST_TRAP_LOCATION_RIGHT;
    public static final Material CHEST_XMAS_LOCATION;
    public static final Material CHEST_XMAS_LOCATION_LEFT;
    public static final Material CHEST_XMAS_LOCATION_RIGHT;
    public static final Material CHEST_LOCATION;
    public static final Material CHEST_LOCATION_LEFT;
    public static final Material CHEST_LOCATION_RIGHT;
    public static final Material ENDER_CHEST_LOCATION;
    
    public static RenderType bannerSheet() {
        return Sheets.BANNER_SHEET_TYPE;
    }
    
    public static RenderType shieldSheet() {
        return Sheets.SHIELD_SHEET_TYPE;
    }
    
    public static RenderType bedSheet() {
        return Sheets.BED_SHEET_TYPE;
    }
    
    public static RenderType shulkerBoxSheet() {
        return Sheets.SHULKER_BOX_SHEET_TYPE;
    }
    
    public static RenderType signSheet() {
        return Sheets.SIGN_SHEET_TYPE;
    }
    
    public static RenderType chestSheet() {
        return Sheets.CHEST_SHEET_TYPE;
    }
    
    public static RenderType solidBlockSheet() {
        return Sheets.SOLID_BLOCK_SHEET;
    }
    
    public static RenderType cutoutBlockSheet() {
        return Sheets.CUTOUT_BLOCK_SHEET;
    }
    
    public static RenderType translucentItemSheet() {
        return Sheets.TRANSLUCENT_ITEM_CULL_BLOCK_SHEET;
    }
    
    public static RenderType translucentCullBlockSheet() {
        return Sheets.TRANSLUCENT_CULL_BLOCK_SHEET;
    }
    
    public static void getAllMaterials(final Consumer<Material> consumer) {
        consumer.accept(Sheets.DEFAULT_SHULKER_TEXTURE_LOCATION);
        Sheets.SHULKER_TEXTURE_LOCATION.forEach((Consumer)consumer);
        for (final BannerPattern cby5 : BannerPattern.values()) {
            consumer.accept(new Material(Sheets.BANNER_SHEET, cby5.location(true)));
            consumer.accept(new Material(Sheets.SHIELD_SHEET, cby5.location(false)));
        }
        Sheets.SIGN_MATERIALS.values().forEach((Consumer)consumer);
        for (final Material elj5 : Sheets.BED_TEXTURES) {
            consumer.accept(elj5);
        }
        consumer.accept(Sheets.CHEST_TRAP_LOCATION);
        consumer.accept(Sheets.CHEST_TRAP_LOCATION_LEFT);
        consumer.accept(Sheets.CHEST_TRAP_LOCATION_RIGHT);
        consumer.accept(Sheets.CHEST_XMAS_LOCATION);
        consumer.accept(Sheets.CHEST_XMAS_LOCATION_LEFT);
        consumer.accept(Sheets.CHEST_XMAS_LOCATION_RIGHT);
        consumer.accept(Sheets.CHEST_LOCATION);
        consumer.accept(Sheets.CHEST_LOCATION_LEFT);
        consumer.accept(Sheets.CHEST_LOCATION_RIGHT);
        consumer.accept(Sheets.ENDER_CHEST_LOCATION);
    }
    
    public static Material signTexture(final WoodType cfn) {
        return new Material(Sheets.SIGN_SHEET, new ResourceLocation("entity/signs/" + cfn.name()));
    }
    
    private static Material chestMaterial(final String string) {
        return new Material(Sheets.CHEST_SHEET, new ResourceLocation("entity/chest/" + string));
    }
    
    public static Material chooseMaterial(final BlockEntity ccg, final ChestType cew, final boolean boolean3) {
        if (boolean3) {
            return chooseMaterial(cew, Sheets.CHEST_XMAS_LOCATION, Sheets.CHEST_XMAS_LOCATION_LEFT, Sheets.CHEST_XMAS_LOCATION_RIGHT);
        }
        if (ccg instanceof TrappedChestBlockEntity) {
            return chooseMaterial(cew, Sheets.CHEST_TRAP_LOCATION, Sheets.CHEST_TRAP_LOCATION_LEFT, Sheets.CHEST_TRAP_LOCATION_RIGHT);
        }
        if (ccg instanceof EnderChestBlockEntity) {
            return Sheets.ENDER_CHEST_LOCATION;
        }
        return chooseMaterial(cew, Sheets.CHEST_LOCATION, Sheets.CHEST_LOCATION_LEFT, Sheets.CHEST_LOCATION_RIGHT);
    }
    
    private static Material chooseMaterial(final ChestType cew, final Material elj2, final Material elj3, final Material elj4) {
        switch (cew) {
            case LEFT: {
                return elj3;
            }
            case RIGHT: {
                return elj4;
            }
            default: {
                return elj2;
            }
        }
    }
    
    static {
        SHULKER_SHEET = new ResourceLocation("textures/atlas/shulker_boxes.png");
        BED_SHEET = new ResourceLocation("textures/atlas/beds.png");
        BANNER_SHEET = new ResourceLocation("textures/atlas/banner_patterns.png");
        SHIELD_SHEET = new ResourceLocation("textures/atlas/shield_patterns.png");
        SIGN_SHEET = new ResourceLocation("textures/atlas/signs.png");
        CHEST_SHEET = new ResourceLocation("textures/atlas/chest.png");
        SHULKER_BOX_SHEET_TYPE = RenderType.entityCutoutNoCull(Sheets.SHULKER_SHEET);
        BED_SHEET_TYPE = RenderType.entitySolid(Sheets.BED_SHEET);
        BANNER_SHEET_TYPE = RenderType.entityNoOutline(Sheets.BANNER_SHEET);
        SHIELD_SHEET_TYPE = RenderType.entityNoOutline(Sheets.SHIELD_SHEET);
        SIGN_SHEET_TYPE = RenderType.entityCutoutNoCull(Sheets.SIGN_SHEET);
        CHEST_SHEET_TYPE = RenderType.entityCutout(Sheets.CHEST_SHEET);
        SOLID_BLOCK_SHEET = RenderType.entitySolid(TextureAtlas.LOCATION_BLOCKS);
        CUTOUT_BLOCK_SHEET = RenderType.entityCutout(TextureAtlas.LOCATION_BLOCKS);
        TRANSLUCENT_ITEM_CULL_BLOCK_SHEET = RenderType.itemEntityTranslucentCull(TextureAtlas.LOCATION_BLOCKS);
        TRANSLUCENT_CULL_BLOCK_SHEET = RenderType.entityTranslucentCull(TextureAtlas.LOCATION_BLOCKS);
        DEFAULT_SHULKER_TEXTURE_LOCATION = new Material(Sheets.SHULKER_SHEET, new ResourceLocation("entity/shulker/shulker"));
        SHULKER_TEXTURE_LOCATION = (List)Stream.of((Object[])new String[] { "white", "orange", "magenta", "light_blue", "yellow", "lime", "pink", "gray", "light_gray", "cyan", "purple", "blue", "brown", "green", "red", "black" }).map(string -> new Material(Sheets.SHULKER_SHEET, new ResourceLocation("entity/shulker/shulker_" + string))).collect(ImmutableList.toImmutableList());
        SIGN_MATERIALS = (Map)WoodType.values().collect(Collectors.toMap(Function.identity(), Sheets::signTexture));
        BED_TEXTURES = (Material[])Arrays.stream((Object[])DyeColor.values()).sorted(Comparator.comparingInt(DyeColor::getId)).map(bku -> new Material(Sheets.BED_SHEET, new ResourceLocation("entity/bed/" + bku.getName()))).toArray(Material[]::new);
        CHEST_TRAP_LOCATION = chestMaterial("trapped");
        CHEST_TRAP_LOCATION_LEFT = chestMaterial("trapped_left");
        CHEST_TRAP_LOCATION_RIGHT = chestMaterial("trapped_right");
        CHEST_XMAS_LOCATION = chestMaterial("christmas");
        CHEST_XMAS_LOCATION_LEFT = chestMaterial("christmas_left");
        CHEST_XMAS_LOCATION_RIGHT = chestMaterial("christmas_right");
        CHEST_LOCATION = chestMaterial("normal");
        CHEST_LOCATION_LEFT = chestMaterial("normal_left");
        CHEST_LOCATION_RIGHT = chestMaterial("normal_right");
        ENDER_CHEST_LOCATION = chestMaterial("ender");
    }
}

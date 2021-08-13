package net.minecraft.data.models.model;

import net.minecraft.core.Registry;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import java.util.Collection;
import java.util.stream.Stream;
import com.google.common.collect.Sets;
import com.google.common.collect.Maps;
import java.util.Set;
import net.minecraft.resources.ResourceLocation;
import java.util.Map;

public class TextureMapping {
    private final Map<TextureSlot, ResourceLocation> slots;
    private final Set<TextureSlot> forcedSlots;
    
    public TextureMapping() {
        this.slots = (Map<TextureSlot, ResourceLocation>)Maps.newHashMap();
        this.forcedSlots = (Set<TextureSlot>)Sets.newHashSet();
    }
    
    public TextureMapping put(final TextureSlot ja, final ResourceLocation vk) {
        this.slots.put(ja, vk);
        return this;
    }
    
    public Stream<TextureSlot> getForced() {
        return (Stream<TextureSlot>)this.forcedSlots.stream();
    }
    
    public TextureMapping copyForced(final TextureSlot ja1, final TextureSlot ja2) {
        this.slots.put(ja2, this.slots.get(ja1));
        this.forcedSlots.add(ja2);
        return this;
    }
    
    public ResourceLocation get(final TextureSlot ja) {
        for (TextureSlot ja2 = ja; ja2 != null; ja2 = ja2.getParent()) {
            final ResourceLocation vk4 = (ResourceLocation)this.slots.get(ja2);
            if (vk4 != null) {
                return vk4;
            }
        }
        throw new IllegalStateException(new StringBuilder().append("Can't find texture for slot ").append(ja).toString());
    }
    
    public TextureMapping copyAndUpdate(final TextureSlot ja, final ResourceLocation vk) {
        final TextureMapping iz4 = new TextureMapping();
        iz4.slots.putAll((Map)this.slots);
        iz4.forcedSlots.addAll((Collection)this.forcedSlots);
        iz4.put(ja, vk);
        return iz4;
    }
    
    public static TextureMapping cube(final Block bul) {
        final ResourceLocation vk2 = getBlockTexture(bul);
        return cube(vk2);
    }
    
    public static TextureMapping defaultTexture(final Block bul) {
        final ResourceLocation vk2 = getBlockTexture(bul);
        return defaultTexture(vk2);
    }
    
    public static TextureMapping defaultTexture(final ResourceLocation vk) {
        return new TextureMapping().put(TextureSlot.TEXTURE, vk);
    }
    
    public static TextureMapping cube(final ResourceLocation vk) {
        return new TextureMapping().put(TextureSlot.ALL, vk);
    }
    
    public static TextureMapping cross(final Block bul) {
        return singleSlot(TextureSlot.CROSS, getBlockTexture(bul));
    }
    
    public static TextureMapping cross(final ResourceLocation vk) {
        return singleSlot(TextureSlot.CROSS, vk);
    }
    
    public static TextureMapping plant(final Block bul) {
        return singleSlot(TextureSlot.PLANT, getBlockTexture(bul));
    }
    
    public static TextureMapping plant(final ResourceLocation vk) {
        return singleSlot(TextureSlot.PLANT, vk);
    }
    
    public static TextureMapping rail(final Block bul) {
        return singleSlot(TextureSlot.RAIL, getBlockTexture(bul));
    }
    
    public static TextureMapping rail(final ResourceLocation vk) {
        return singleSlot(TextureSlot.RAIL, vk);
    }
    
    public static TextureMapping wool(final Block bul) {
        return singleSlot(TextureSlot.WOOL, getBlockTexture(bul));
    }
    
    public static TextureMapping stem(final Block bul) {
        return singleSlot(TextureSlot.STEM, getBlockTexture(bul));
    }
    
    public static TextureMapping attachedStem(final Block bul1, final Block bul2) {
        return new TextureMapping().put(TextureSlot.STEM, getBlockTexture(bul1)).put(TextureSlot.UPPER_STEM, getBlockTexture(bul2));
    }
    
    public static TextureMapping pattern(final Block bul) {
        return singleSlot(TextureSlot.PATTERN, getBlockTexture(bul));
    }
    
    public static TextureMapping fan(final Block bul) {
        return singleSlot(TextureSlot.FAN, getBlockTexture(bul));
    }
    
    public static TextureMapping crop(final ResourceLocation vk) {
        return singleSlot(TextureSlot.CROP, vk);
    }
    
    public static TextureMapping pane(final Block bul1, final Block bul2) {
        return new TextureMapping().put(TextureSlot.PANE, getBlockTexture(bul1)).put(TextureSlot.EDGE, getBlockTexture(bul2, "_top"));
    }
    
    public static TextureMapping singleSlot(final TextureSlot ja, final ResourceLocation vk) {
        return new TextureMapping().put(ja, vk);
    }
    
    public static TextureMapping column(final Block bul) {
        return new TextureMapping().put(TextureSlot.SIDE, getBlockTexture(bul, "_side")).put(TextureSlot.END, getBlockTexture(bul, "_top"));
    }
    
    public static TextureMapping cubeTop(final Block bul) {
        return new TextureMapping().put(TextureSlot.SIDE, getBlockTexture(bul, "_side")).put(TextureSlot.TOP, getBlockTexture(bul, "_top"));
    }
    
    public static TextureMapping logColumn(final Block bul) {
        return new TextureMapping().put(TextureSlot.SIDE, getBlockTexture(bul)).put(TextureSlot.END, getBlockTexture(bul, "_top"));
    }
    
    public static TextureMapping column(final ResourceLocation vk1, final ResourceLocation vk2) {
        return new TextureMapping().put(TextureSlot.SIDE, vk1).put(TextureSlot.END, vk2);
    }
    
    public static TextureMapping cubeBottomTop(final Block bul) {
        return new TextureMapping().put(TextureSlot.SIDE, getBlockTexture(bul, "_side")).put(TextureSlot.TOP, getBlockTexture(bul, "_top")).put(TextureSlot.BOTTOM, getBlockTexture(bul, "_bottom"));
    }
    
    public static TextureMapping cubeBottomTopWithWall(final Block bul) {
        final ResourceLocation vk2 = getBlockTexture(bul);
        return new TextureMapping().put(TextureSlot.WALL, vk2).put(TextureSlot.SIDE, vk2).put(TextureSlot.TOP, getBlockTexture(bul, "_top")).put(TextureSlot.BOTTOM, getBlockTexture(bul, "_bottom"));
    }
    
    public static TextureMapping columnWithWall(final Block bul) {
        final ResourceLocation vk2 = getBlockTexture(bul);
        return new TextureMapping().put(TextureSlot.WALL, vk2).put(TextureSlot.SIDE, vk2).put(TextureSlot.END, getBlockTexture(bul, "_top"));
    }
    
    public static TextureMapping door(final Block bul) {
        return new TextureMapping().put(TextureSlot.TOP, getBlockTexture(bul, "_top")).put(TextureSlot.BOTTOM, getBlockTexture(bul, "_bottom"));
    }
    
    public static TextureMapping particle(final Block bul) {
        return new TextureMapping().put(TextureSlot.PARTICLE, getBlockTexture(bul));
    }
    
    public static TextureMapping particle(final ResourceLocation vk) {
        return new TextureMapping().put(TextureSlot.PARTICLE, vk);
    }
    
    public static TextureMapping fire0(final Block bul) {
        return new TextureMapping().put(TextureSlot.FIRE, getBlockTexture(bul, "_0"));
    }
    
    public static TextureMapping fire1(final Block bul) {
        return new TextureMapping().put(TextureSlot.FIRE, getBlockTexture(bul, "_1"));
    }
    
    public static TextureMapping lantern(final Block bul) {
        return new TextureMapping().put(TextureSlot.LANTERN, getBlockTexture(bul));
    }
    
    public static TextureMapping torch(final Block bul) {
        return new TextureMapping().put(TextureSlot.TORCH, getBlockTexture(bul));
    }
    
    public static TextureMapping torch(final ResourceLocation vk) {
        return new TextureMapping().put(TextureSlot.TORCH, vk);
    }
    
    public static TextureMapping particleFromItem(final Item blu) {
        return new TextureMapping().put(TextureSlot.PARTICLE, getItemTexture(blu));
    }
    
    public static TextureMapping commandBlock(final Block bul) {
        return new TextureMapping().put(TextureSlot.SIDE, getBlockTexture(bul, "_side")).put(TextureSlot.FRONT, getBlockTexture(bul, "_front")).put(TextureSlot.BACK, getBlockTexture(bul, "_back"));
    }
    
    public static TextureMapping orientableCube(final Block bul) {
        return new TextureMapping().put(TextureSlot.SIDE, getBlockTexture(bul, "_side")).put(TextureSlot.FRONT, getBlockTexture(bul, "_front")).put(TextureSlot.TOP, getBlockTexture(bul, "_top")).put(TextureSlot.BOTTOM, getBlockTexture(bul, "_bottom"));
    }
    
    public static TextureMapping orientableCubeOnlyTop(final Block bul) {
        return new TextureMapping().put(TextureSlot.SIDE, getBlockTexture(bul, "_side")).put(TextureSlot.FRONT, getBlockTexture(bul, "_front")).put(TextureSlot.TOP, getBlockTexture(bul, "_top"));
    }
    
    public static TextureMapping orientableCubeSameEnds(final Block bul) {
        return new TextureMapping().put(TextureSlot.SIDE, getBlockTexture(bul, "_side")).put(TextureSlot.FRONT, getBlockTexture(bul, "_front")).put(TextureSlot.END, getBlockTexture(bul, "_end"));
    }
    
    public static TextureMapping top(final Block bul) {
        return new TextureMapping().put(TextureSlot.TOP, getBlockTexture(bul, "_top"));
    }
    
    public static TextureMapping craftingTable(final Block bul1, final Block bul2) {
        return new TextureMapping().put(TextureSlot.PARTICLE, getBlockTexture(bul1, "_front")).put(TextureSlot.DOWN, getBlockTexture(bul2)).put(TextureSlot.UP, getBlockTexture(bul1, "_top")).put(TextureSlot.NORTH, getBlockTexture(bul1, "_front")).put(TextureSlot.EAST, getBlockTexture(bul1, "_side")).put(TextureSlot.SOUTH, getBlockTexture(bul1, "_side")).put(TextureSlot.WEST, getBlockTexture(bul1, "_front"));
    }
    
    public static TextureMapping fletchingTable(final Block bul1, final Block bul2) {
        return new TextureMapping().put(TextureSlot.PARTICLE, getBlockTexture(bul1, "_front")).put(TextureSlot.DOWN, getBlockTexture(bul2)).put(TextureSlot.UP, getBlockTexture(bul1, "_top")).put(TextureSlot.NORTH, getBlockTexture(bul1, "_front")).put(TextureSlot.SOUTH, getBlockTexture(bul1, "_front")).put(TextureSlot.EAST, getBlockTexture(bul1, "_side")).put(TextureSlot.WEST, getBlockTexture(bul1, "_side"));
    }
    
    public static TextureMapping campfire(final Block bul) {
        return new TextureMapping().put(TextureSlot.LIT_LOG, getBlockTexture(bul, "_log_lit")).put(TextureSlot.FIRE, getBlockTexture(bul, "_fire"));
    }
    
    public static TextureMapping layer0(final Item blu) {
        return new TextureMapping().put(TextureSlot.LAYER0, getItemTexture(blu));
    }
    
    public static TextureMapping layer0(final Block bul) {
        return new TextureMapping().put(TextureSlot.LAYER0, getBlockTexture(bul));
    }
    
    public static TextureMapping layer0(final ResourceLocation vk) {
        return new TextureMapping().put(TextureSlot.LAYER0, vk);
    }
    
    public static ResourceLocation getBlockTexture(final Block bul) {
        final ResourceLocation vk2 = Registry.BLOCK.getKey(bul);
        return new ResourceLocation(vk2.getNamespace(), "block/" + vk2.getPath());
    }
    
    public static ResourceLocation getBlockTexture(final Block bul, final String string) {
        final ResourceLocation vk3 = Registry.BLOCK.getKey(bul);
        return new ResourceLocation(vk3.getNamespace(), "block/" + vk3.getPath() + string);
    }
    
    public static ResourceLocation getItemTexture(final Item blu) {
        final ResourceLocation vk2 = Registry.ITEM.getKey(blu);
        return new ResourceLocation(vk2.getNamespace(), "item/" + vk2.getPath());
    }
    
    public static ResourceLocation getItemTexture(final Item blu, final String string) {
        final ResourceLocation vk3 = Registry.ITEM.getKey(blu);
        return new ResourceLocation(vk3.getNamespace(), "item/" + vk3.getPath() + string);
    }
}

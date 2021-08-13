package net.minecraft.client.resources;

import java.util.stream.Collectors;
import com.google.common.collect.Lists;
import java.util.function.Consumer;
import net.minecraft.Util;
import com.google.common.collect.Maps;
import java.util.HashMap;
import net.minecraft.server.packs.metadata.MetadataSectionSerializer;
import java.util.Collection;
import java.util.function.Predicate;
import javax.annotation.Nullable;
import java.io.ByteArrayInputStream;
import com.mojang.blaze3d.platform.NativeImage;
import net.minecraft.server.packs.PackType;
import java.io.IOException;
import java.io.InputStream;
import java.util.Set;
import java.util.List;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.properties.ChestType;
import com.mojang.datafixers.util.Pair;
import java.util.Map;
import net.minecraft.server.packs.PackResources;

public class PackResourcesAdapterV4 implements PackResources {
    private static final Map<String, Pair<ChestType, ResourceLocation>> CHESTS;
    private static final List<String> PATTERNS;
    private static final Set<String> SHIELDS;
    private static final Set<String> BANNERS;
    public static final ResourceLocation SHIELD_BASE;
    public static final ResourceLocation BANNER_BASE;
    public static final ResourceLocation OLD_IRON_GOLEM_LOCATION;
    private final PackResources pack;
    
    public PackResourcesAdapterV4(final PackResources abh) {
        this.pack = abh;
    }
    
    public InputStream getRootResource(final String string) throws IOException {
        return this.pack.getRootResource(string);
    }
    
    public boolean hasResource(final PackType abi, final ResourceLocation vk) {
        if (!"minecraft".equals(vk.getNamespace())) {
            return this.pack.hasResource(abi, vk);
        }
        final String string4 = vk.getPath();
        if ("textures/misc/enchanted_item_glint.png".equals(string4)) {
            return false;
        }
        if ("textures/entity/iron_golem/iron_golem.png".equals(string4)) {
            return this.pack.hasResource(abi, PackResourcesAdapterV4.OLD_IRON_GOLEM_LOCATION);
        }
        if ("textures/entity/conduit/wind.png".equals(string4) || "textures/entity/conduit/wind_vertical.png".equals(string4)) {
            return false;
        }
        if (PackResourcesAdapterV4.SHIELDS.contains(string4)) {
            return this.pack.hasResource(abi, PackResourcesAdapterV4.SHIELD_BASE) && this.pack.hasResource(abi, vk);
        }
        if (PackResourcesAdapterV4.BANNERS.contains(string4)) {
            return this.pack.hasResource(abi, PackResourcesAdapterV4.BANNER_BASE) && this.pack.hasResource(abi, vk);
        }
        final Pair<ChestType, ResourceLocation> pair5 = (Pair<ChestType, ResourceLocation>)PackResourcesAdapterV4.CHESTS.get(string4);
        return (pair5 != null && this.pack.hasResource(abi, (ResourceLocation)pair5.getSecond())) || this.pack.hasResource(abi, vk);
    }
    
    public InputStream getResource(final PackType abi, final ResourceLocation vk) throws IOException {
        if (!"minecraft".equals(vk.getNamespace())) {
            return this.pack.getResource(abi, vk);
        }
        final String string4 = vk.getPath();
        if ("textures/entity/iron_golem/iron_golem.png".equals(string4)) {
            return this.pack.getResource(abi, PackResourcesAdapterV4.OLD_IRON_GOLEM_LOCATION);
        }
        if (PackResourcesAdapterV4.SHIELDS.contains(string4)) {
            final InputStream inputStream5 = fixPattern(this.pack.getResource(abi, PackResourcesAdapterV4.SHIELD_BASE), this.pack.getResource(abi, vk), 64, 2, 2, 12, 22);
            if (inputStream5 != null) {
                return inputStream5;
            }
        }
        else if (PackResourcesAdapterV4.BANNERS.contains(string4)) {
            final InputStream inputStream5 = fixPattern(this.pack.getResource(abi, PackResourcesAdapterV4.BANNER_BASE), this.pack.getResource(abi, vk), 64, 0, 0, 42, 41);
            if (inputStream5 != null) {
                return inputStream5;
            }
        }
        else {
            if ("textures/entity/enderdragon/dragon.png".equals(string4) || "textures/entity/enderdragon/dragon_exploding.png".equals(string4)) {
                try (final NativeImage deq5 = NativeImage.read(this.pack.getResource(abi, vk))) {
                    for (int integer7 = deq5.getWidth() / 256, integer8 = 88 * integer7; integer8 < 200 * integer7; ++integer8) {
                        for (int integer9 = 56 * integer7; integer9 < 112 * integer7; ++integer9) {
                            deq5.setPixelRGBA(integer9, integer8, 0);
                        }
                    }
                    return (InputStream)new ByteArrayInputStream(deq5.asByteArray());
                }
            }
            if ("textures/entity/conduit/closed_eye.png".equals(string4) || "textures/entity/conduit/open_eye.png".equals(string4)) {
                return fixConduitEyeTexture(this.pack.getResource(abi, vk));
            }
            final Pair<ChestType, ResourceLocation> pair5 = (Pair<ChestType, ResourceLocation>)PackResourcesAdapterV4.CHESTS.get(string4);
            if (pair5 != null) {
                final ChestType cew6 = (ChestType)pair5.getFirst();
                final InputStream inputStream6 = this.pack.getResource(abi, (ResourceLocation)pair5.getSecond());
                if (cew6 == ChestType.SINGLE) {
                    return fixSingleChest(inputStream6);
                }
                if (cew6 == ChestType.LEFT) {
                    return fixLeftChest(inputStream6);
                }
                if (cew6 == ChestType.RIGHT) {
                    return fixRightChest(inputStream6);
                }
            }
        }
        return this.pack.getResource(abi, vk);
    }
    
    @Nullable
    public static InputStream fixPattern(final InputStream inputStream1, final InputStream inputStream2, final int integer3, final int integer4, final int integer5, final int integer6, final int integer7) throws IOException {
        try (final NativeImage deq8 = NativeImage.read(inputStream1);
             final NativeImage deq9 = NativeImage.read(inputStream2)) {
            final int integer8 = deq8.getWidth();
            final int integer9 = deq8.getHeight();
            if (integer8 == deq9.getWidth() && integer9 == deq9.getHeight()) {
                try (final NativeImage deq10 = new NativeImage(integer8, integer9, true)) {
                    for (int integer10 = integer8 / integer3, integer11 = integer5 * integer10; integer11 < integer7 * integer10; ++integer11) {
                        for (int integer12 = integer4 * integer10; integer12 < integer6 * integer10; ++integer12) {
                            final int integer13 = NativeImage.getR(deq9.getPixelRGBA(integer12, integer11));
                            final int integer14 = deq8.getPixelRGBA(integer12, integer11);
                            deq10.setPixelRGBA(integer12, integer11, NativeImage.combine(integer13, NativeImage.getB(integer14), NativeImage.getG(integer14), NativeImage.getR(integer14)));
                        }
                    }
                    return (InputStream)new ByteArrayInputStream(deq10.asByteArray());
                }
            }
        }
        return null;
    }
    
    public static InputStream fixConduitEyeTexture(final InputStream inputStream) throws IOException {
        try (final NativeImage deq2 = NativeImage.read(inputStream)) {
            final int integer4 = deq2.getWidth();
            final int integer5 = deq2.getHeight();
            try (final NativeImage deq3 = new NativeImage(2 * integer4, 2 * integer5, true)) {
                copyRect(deq2, deq3, 0, 0, 0, 0, integer4, integer5, 1, false, false);
                return (InputStream)new ByteArrayInputStream(deq3.asByteArray());
            }
        }
    }
    
    public static InputStream fixLeftChest(final InputStream inputStream) throws IOException {
        try (final NativeImage deq2 = NativeImage.read(inputStream)) {
            final int integer4 = deq2.getWidth();
            final int integer5 = deq2.getHeight();
            try (final NativeImage deq3 = new NativeImage(integer4 / 2, integer5, true)) {
                final int integer6 = integer5 / 64;
                copyRect(deq2, deq3, 29, 0, 29, 0, 15, 14, integer6, false, true);
                copyRect(deq2, deq3, 59, 0, 14, 0, 15, 14, integer6, false, true);
                copyRect(deq2, deq3, 29, 14, 43, 14, 15, 5, integer6, true, true);
                copyRect(deq2, deq3, 44, 14, 29, 14, 14, 5, integer6, true, true);
                copyRect(deq2, deq3, 58, 14, 14, 14, 15, 5, integer6, true, true);
                copyRect(deq2, deq3, 29, 19, 29, 19, 15, 14, integer6, false, true);
                copyRect(deq2, deq3, 59, 19, 14, 19, 15, 14, integer6, false, true);
                copyRect(deq2, deq3, 29, 33, 43, 33, 15, 10, integer6, true, true);
                copyRect(deq2, deq3, 44, 33, 29, 33, 14, 10, integer6, true, true);
                copyRect(deq2, deq3, 58, 33, 14, 33, 15, 10, integer6, true, true);
                copyRect(deq2, deq3, 2, 0, 2, 0, 1, 1, integer6, false, true);
                copyRect(deq2, deq3, 4, 0, 1, 0, 1, 1, integer6, false, true);
                copyRect(deq2, deq3, 2, 1, 3, 1, 1, 4, integer6, true, true);
                copyRect(deq2, deq3, 3, 1, 2, 1, 1, 4, integer6, true, true);
                copyRect(deq2, deq3, 4, 1, 1, 1, 1, 4, integer6, true, true);
                return (InputStream)new ByteArrayInputStream(deq3.asByteArray());
            }
        }
    }
    
    public static InputStream fixRightChest(final InputStream inputStream) throws IOException {
        try (final NativeImage deq2 = NativeImage.read(inputStream)) {
            final int integer4 = deq2.getWidth();
            final int integer5 = deq2.getHeight();
            try (final NativeImage deq3 = new NativeImage(integer4 / 2, integer5, true)) {
                final int integer6 = integer5 / 64;
                copyRect(deq2, deq3, 14, 0, 29, 0, 15, 14, integer6, false, true);
                copyRect(deq2, deq3, 44, 0, 14, 0, 15, 14, integer6, false, true);
                copyRect(deq2, deq3, 0, 14, 0, 14, 14, 5, integer6, true, true);
                copyRect(deq2, deq3, 14, 14, 43, 14, 15, 5, integer6, true, true);
                copyRect(deq2, deq3, 73, 14, 14, 14, 15, 5, integer6, true, true);
                copyRect(deq2, deq3, 14, 19, 29, 19, 15, 14, integer6, false, true);
                copyRect(deq2, deq3, 44, 19, 14, 19, 15, 14, integer6, false, true);
                copyRect(deq2, deq3, 0, 33, 0, 33, 14, 10, integer6, true, true);
                copyRect(deq2, deq3, 14, 33, 43, 33, 15, 10, integer6, true, true);
                copyRect(deq2, deq3, 73, 33, 14, 33, 15, 10, integer6, true, true);
                copyRect(deq2, deq3, 1, 0, 2, 0, 1, 1, integer6, false, true);
                copyRect(deq2, deq3, 3, 0, 1, 0, 1, 1, integer6, false, true);
                copyRect(deq2, deq3, 0, 1, 0, 1, 1, 4, integer6, true, true);
                copyRect(deq2, deq3, 1, 1, 3, 1, 1, 4, integer6, true, true);
                copyRect(deq2, deq3, 5, 1, 1, 1, 1, 4, integer6, true, true);
                return (InputStream)new ByteArrayInputStream(deq3.asByteArray());
            }
        }
    }
    
    public static InputStream fixSingleChest(final InputStream inputStream) throws IOException {
        try (final NativeImage deq2 = NativeImage.read(inputStream)) {
            final int integer4 = deq2.getWidth();
            final int integer5 = deq2.getHeight();
            try (final NativeImage deq3 = new NativeImage(integer4, integer5, true)) {
                final int integer6 = integer5 / 64;
                copyRect(deq2, deq3, 14, 0, 28, 0, 14, 14, integer6, false, true);
                copyRect(deq2, deq3, 28, 0, 14, 0, 14, 14, integer6, false, true);
                copyRect(deq2, deq3, 0, 14, 0, 14, 14, 5, integer6, true, true);
                copyRect(deq2, deq3, 14, 14, 42, 14, 14, 5, integer6, true, true);
                copyRect(deq2, deq3, 28, 14, 28, 14, 14, 5, integer6, true, true);
                copyRect(deq2, deq3, 42, 14, 14, 14, 14, 5, integer6, true, true);
                copyRect(deq2, deq3, 14, 19, 28, 19, 14, 14, integer6, false, true);
                copyRect(deq2, deq3, 28, 19, 14, 19, 14, 14, integer6, false, true);
                copyRect(deq2, deq3, 0, 33, 0, 33, 14, 10, integer6, true, true);
                copyRect(deq2, deq3, 14, 33, 42, 33, 14, 10, integer6, true, true);
                copyRect(deq2, deq3, 28, 33, 28, 33, 14, 10, integer6, true, true);
                copyRect(deq2, deq3, 42, 33, 14, 33, 14, 10, integer6, true, true);
                copyRect(deq2, deq3, 1, 0, 3, 0, 2, 1, integer6, false, true);
                copyRect(deq2, deq3, 3, 0, 1, 0, 2, 1, integer6, false, true);
                copyRect(deq2, deq3, 0, 1, 0, 1, 1, 4, integer6, true, true);
                copyRect(deq2, deq3, 1, 1, 4, 1, 2, 4, integer6, true, true);
                copyRect(deq2, deq3, 3, 1, 3, 1, 1, 4, integer6, true, true);
                copyRect(deq2, deq3, 4, 1, 1, 1, 2, 4, integer6, true, true);
                return (InputStream)new ByteArrayInputStream(deq3.asByteArray());
            }
        }
    }
    
    public Collection<ResourceLocation> getResources(final PackType abi, final String string2, final String string3, final int integer, final Predicate<String> predicate) {
        return this.pack.getResources(abi, string2, string3, integer, predicate);
    }
    
    public Set<String> getNamespaces(final PackType abi) {
        return this.pack.getNamespaces(abi);
    }
    
    @Nullable
    public <T> T getMetadataSection(final MetadataSectionSerializer<T> abl) throws IOException {
        return this.pack.<T>getMetadataSection(abl);
    }
    
    public String getName() {
        return this.pack.getName();
    }
    
    public void close() {
        this.pack.close();
    }
    
    private static void copyRect(final NativeImage deq1, final NativeImage deq2, int integer3, int integer4, int integer5, int integer6, int integer7, int integer8, final int integer9, final boolean boolean10, final boolean boolean11) {
        integer8 *= integer9;
        integer7 *= integer9;
        integer5 *= integer9;
        integer6 *= integer9;
        integer3 *= integer9;
        integer4 *= integer9;
        for (int integer10 = 0; integer10 < integer8; ++integer10) {
            for (int integer11 = 0; integer11 < integer7; ++integer11) {
                deq2.setPixelRGBA(integer5 + integer11, integer6 + integer10, deq1.getPixelRGBA(integer3 + (boolean10 ? (integer7 - 1 - integer11) : integer11), integer4 + (boolean11 ? (integer8 - 1 - integer10) : integer10)));
            }
        }
    }
    
    static {
        CHESTS = Util.<Map>make((Map)Maps.newHashMap(), (java.util.function.Consumer<Map>)(hashMap -> {
            hashMap.put("textures/entity/chest/normal_left.png", new Pair((Object)ChestType.LEFT, (Object)new ResourceLocation("textures/entity/chest/normal_double.png")));
            hashMap.put("textures/entity/chest/normal_right.png", new Pair((Object)ChestType.RIGHT, (Object)new ResourceLocation("textures/entity/chest/normal_double.png")));
            hashMap.put("textures/entity/chest/normal.png", new Pair((Object)ChestType.SINGLE, (Object)new ResourceLocation("textures/entity/chest/normal.png")));
            hashMap.put("textures/entity/chest/trapped_left.png", new Pair((Object)ChestType.LEFT, (Object)new ResourceLocation("textures/entity/chest/trapped_double.png")));
            hashMap.put("textures/entity/chest/trapped_right.png", new Pair((Object)ChestType.RIGHT, (Object)new ResourceLocation("textures/entity/chest/trapped_double.png")));
            hashMap.put("textures/entity/chest/trapped.png", new Pair((Object)ChestType.SINGLE, (Object)new ResourceLocation("textures/entity/chest/trapped.png")));
            hashMap.put("textures/entity/chest/christmas_left.png", new Pair((Object)ChestType.LEFT, (Object)new ResourceLocation("textures/entity/chest/christmas_double.png")));
            hashMap.put("textures/entity/chest/christmas_right.png", new Pair((Object)ChestType.RIGHT, (Object)new ResourceLocation("textures/entity/chest/christmas_double.png")));
            hashMap.put("textures/entity/chest/christmas.png", new Pair((Object)ChestType.SINGLE, (Object)new ResourceLocation("textures/entity/chest/christmas.png")));
            hashMap.put("textures/entity/chest/ender.png", new Pair((Object)ChestType.SINGLE, (Object)new ResourceLocation("textures/entity/chest/ender.png")));
        }));
        PATTERNS = (List)Lists.newArrayList((Object[])new String[] { "base", "border", "bricks", "circle", "creeper", "cross", "curly_border", "diagonal_left", "diagonal_right", "diagonal_up_left", "diagonal_up_right", "flower", "globe", "gradient", "gradient_up", "half_horizontal", "half_horizontal_bottom", "half_vertical", "half_vertical_right", "mojang", "rhombus", "skull", "small_stripes", "square_bottom_left", "square_bottom_right", "square_top_left", "square_top_right", "straight_cross", "stripe_bottom", "stripe_center", "stripe_downleft", "stripe_downright", "stripe_left", "stripe_middle", "stripe_right", "stripe_top", "triangle_bottom", "triangle_top", "triangles_bottom", "triangles_top" });
        SHIELDS = (Set)PackResourcesAdapterV4.PATTERNS.stream().map(string -> "textures/entity/shield/" + string + ".png").collect(Collectors.toSet());
        BANNERS = (Set)PackResourcesAdapterV4.PATTERNS.stream().map(string -> "textures/entity/banner/" + string + ".png").collect(Collectors.toSet());
        SHIELD_BASE = new ResourceLocation("textures/entity/shield_base.png");
        BANNER_BASE = new ResourceLocation("textures/entity/banner_base.png");
        OLD_IRON_GOLEM_LOCATION = new ResourceLocation("textures/entity/iron_golem.png");
    }
}

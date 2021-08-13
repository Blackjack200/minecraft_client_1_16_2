package net.minecraft.data.models.model;

import java.util.function.Function;
import com.google.gson.JsonElement;
import java.util.function.Supplier;
import net.minecraft.resources.ResourceLocation;
import java.util.function.BiConsumer;
import net.minecraft.world.level.block.Block;
import java.util.function.Consumer;

public class TexturedModel {
    public static final Provider CUBE;
    public static final Provider CUBE_MIRRORED;
    public static final Provider COLUMN;
    public static final Provider COLUMN_HORIZONTAL;
    public static final Provider CUBE_TOP_BOTTOM;
    public static final Provider CUBE_TOP;
    public static final Provider ORIENTABLE_ONLY_TOP;
    public static final Provider ORIENTABLE;
    public static final Provider CARPET;
    public static final Provider GLAZED_TERRACOTTA;
    public static final Provider CORAL_FAN;
    public static final Provider PARTICLE_ONLY;
    public static final Provider ANVIL;
    public static final Provider LEAVES;
    public static final Provider LANTERN;
    public static final Provider HANGING_LANTERN;
    public static final Provider SEAGRASS;
    public static final Provider COLUMN_ALT;
    public static final Provider COLUMN_HORIZONTAL_ALT;
    public static final Provider TOP_BOTTOM_WITH_WALL;
    public static final Provider COLUMN_WITH_WALL;
    private final TextureMapping mapping;
    private final ModelTemplate template;
    
    private TexturedModel(final TextureMapping iz, final ModelTemplate ix) {
        this.mapping = iz;
        this.template = ix;
    }
    
    public ModelTemplate getTemplate() {
        return this.template;
    }
    
    public TextureMapping getMapping() {
        return this.mapping;
    }
    
    public TexturedModel updateTextures(final Consumer<TextureMapping> consumer) {
        consumer.accept(this.mapping);
        return this;
    }
    
    public ResourceLocation create(final Block bul, final BiConsumer<ResourceLocation, Supplier<JsonElement>> biConsumer) {
        return this.template.create(bul, this.mapping, biConsumer);
    }
    
    public ResourceLocation createWithSuffix(final Block bul, final String string, final BiConsumer<ResourceLocation, Supplier<JsonElement>> biConsumer) {
        return this.template.createWithSuffix(bul, string, this.mapping, biConsumer);
    }
    
    private static Provider createDefault(final Function<Block, TextureMapping> function, final ModelTemplate ix) {
        return bul -> new TexturedModel((TextureMapping)function.apply(bul), ix);
    }
    
    public static TexturedModel createAllSame(final ResourceLocation vk) {
        return new TexturedModel(TextureMapping.cube(vk), ModelTemplates.CUBE_ALL);
    }
    
    static {
        CUBE = createDefault((Function<Block, TextureMapping>)TextureMapping::cube, ModelTemplates.CUBE_ALL);
        CUBE_MIRRORED = createDefault((Function<Block, TextureMapping>)TextureMapping::cube, ModelTemplates.CUBE_MIRRORED_ALL);
        COLUMN = createDefault((Function<Block, TextureMapping>)TextureMapping::column, ModelTemplates.CUBE_COLUMN);
        COLUMN_HORIZONTAL = createDefault((Function<Block, TextureMapping>)TextureMapping::column, ModelTemplates.CUBE_COLUMN_HORIZONTAL);
        CUBE_TOP_BOTTOM = createDefault((Function<Block, TextureMapping>)TextureMapping::cubeBottomTop, ModelTemplates.CUBE_BOTTOM_TOP);
        CUBE_TOP = createDefault((Function<Block, TextureMapping>)TextureMapping::cubeTop, ModelTemplates.CUBE_TOP);
        ORIENTABLE_ONLY_TOP = createDefault((Function<Block, TextureMapping>)TextureMapping::orientableCubeOnlyTop, ModelTemplates.CUBE_ORIENTABLE);
        ORIENTABLE = createDefault((Function<Block, TextureMapping>)TextureMapping::orientableCube, ModelTemplates.CUBE_ORIENTABLE_TOP_BOTTOM);
        CARPET = createDefault((Function<Block, TextureMapping>)TextureMapping::wool, ModelTemplates.CARPET);
        GLAZED_TERRACOTTA = createDefault((Function<Block, TextureMapping>)TextureMapping::pattern, ModelTemplates.GLAZED_TERRACOTTA);
        CORAL_FAN = createDefault((Function<Block, TextureMapping>)TextureMapping::fan, ModelTemplates.CORAL_FAN);
        PARTICLE_ONLY = createDefault((Function<Block, TextureMapping>)TextureMapping::particle, ModelTemplates.PARTICLE_ONLY);
        ANVIL = createDefault((Function<Block, TextureMapping>)TextureMapping::top, ModelTemplates.ANVIL);
        LEAVES = createDefault((Function<Block, TextureMapping>)TextureMapping::cube, ModelTemplates.LEAVES);
        LANTERN = createDefault((Function<Block, TextureMapping>)TextureMapping::lantern, ModelTemplates.LANTERN);
        HANGING_LANTERN = createDefault((Function<Block, TextureMapping>)TextureMapping::lantern, ModelTemplates.HANGING_LANTERN);
        SEAGRASS = createDefault((Function<Block, TextureMapping>)TextureMapping::defaultTexture, ModelTemplates.SEAGRASS);
        COLUMN_ALT = createDefault((Function<Block, TextureMapping>)TextureMapping::logColumn, ModelTemplates.CUBE_COLUMN);
        COLUMN_HORIZONTAL_ALT = createDefault((Function<Block, TextureMapping>)TextureMapping::logColumn, ModelTemplates.CUBE_COLUMN_HORIZONTAL);
        TOP_BOTTOM_WITH_WALL = createDefault((Function<Block, TextureMapping>)TextureMapping::cubeBottomTopWithWall, ModelTemplates.CUBE_BOTTOM_TOP);
        COLUMN_WITH_WALL = createDefault((Function<Block, TextureMapping>)TextureMapping::columnWithWall, ModelTemplates.CUBE_COLUMN);
    }
    
    @FunctionalInterface
    public interface Provider {
        TexturedModel get(final Block bul);
        
        default ResourceLocation create(final Block bul, final BiConsumer<ResourceLocation, Supplier<JsonElement>> biConsumer) {
            return this.get(bul).create(bul, biConsumer);
        }
        
        default ResourceLocation createWithSuffix(final Block bul, final String string, final BiConsumer<ResourceLocation, Supplier<JsonElement>> biConsumer) {
            return this.get(bul).createWithSuffix(bul, string, biConsumer);
        }
        
        default Provider updateTexture(final Consumer<TextureMapping> consumer) {
            return bul -> this.get(bul).updateTextures(consumer);
        }
    }
}

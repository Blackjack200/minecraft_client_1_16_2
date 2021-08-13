package net.minecraft.data.models;

import com.google.gson.GsonBuilder;
import org.apache.logging.log4j.LogManager;
import net.minecraft.data.models.model.DelegatedModel;
import net.minecraft.data.models.model.ModelLocationUtils;
import java.util.function.BiConsumer;
import net.minecraft.world.item.Item;
import java.util.Set;
import java.util.function.Consumer;
import net.minecraft.data.models.blockstates.BlockStateGenerator;
import net.minecraft.resources.ResourceLocation;
import java.nio.file.Path;
import java.util.function.BiFunction;
import com.google.gson.JsonElement;
import java.util.function.Supplier;
import java.util.Map;
import net.minecraft.world.level.block.Block;
import java.util.stream.Collectors;
import net.minecraft.core.Registry;
import java.util.List;
import com.google.common.collect.Sets;
import com.google.common.collect.Maps;
import net.minecraft.data.HashCache;
import net.minecraft.data.DataGenerator;
import com.google.gson.Gson;
import org.apache.logging.log4j.Logger;
import net.minecraft.data.DataProvider;

public class ModelProvider implements DataProvider {
    private static final Logger LOGGER;
    private static final Gson GSON;
    private final DataGenerator generator;
    
    public ModelProvider(final DataGenerator hl) {
        this.generator = hl;
    }
    
    public void run(final HashCache hn) {
        final Path path3 = this.generator.getOutputFolder();
        final Map<Block, BlockStateGenerator> map4 = (Map<Block, BlockStateGenerator>)Maps.newHashMap();
        final Consumer<BlockStateGenerator> consumer5 = (Consumer<BlockStateGenerator>)(il -> {
            final Block bul3 = il.getBlock();
            final BlockStateGenerator il2 = (BlockStateGenerator)map4.put(bul3, il);
            if (il2 != null) {
                throw new IllegalStateException(new StringBuilder().append("Duplicate blockstate definition for ").append(bul3).toString());
            }
        });
        final Map<ResourceLocation, Supplier<JsonElement>> map5 = (Map<ResourceLocation, Supplier<JsonElement>>)Maps.newHashMap();
        final Set<Item> set7 = (Set<Item>)Sets.newHashSet();
        final BiConsumer<ResourceLocation, Supplier<JsonElement>> biConsumer8 = (BiConsumer<ResourceLocation, Supplier<JsonElement>>)((vk, supplier) -> {
            final Supplier<JsonElement> supplier2 = (Supplier<JsonElement>)map5.put(vk, supplier);
            if (supplier2 != null) {
                throw new IllegalStateException(new StringBuilder().append("Duplicate model definition for ").append(vk).toString());
            }
        });
        final Consumer<Item> consumer6 = (Consumer<Item>)set7::add;
        new BlockModelGenerators(consumer5, biConsumer8, consumer6).run();
        new ItemModelGenerators(biConsumer8).run();
        final List<Block> list10 = (List<Block>)Registry.BLOCK.stream().filter(bul -> !map4.containsKey(bul)).collect(Collectors.toList());
        if (!list10.isEmpty()) {
            throw new IllegalStateException(new StringBuilder().append("Missing blockstate definitions for: ").append(list10).toString());
        }
        Registry.BLOCK.forEach(bul -> {
            final Item blu4 = (Item)Item.BY_BLOCK.get(bul);
            if (blu4 != null) {
                if (set7.contains(blu4)) {
                    return;
                }
                final ResourceLocation vk5 = ModelLocationUtils.getModelLocation(blu4);
                if (!map5.containsKey(vk5)) {
                    map5.put(vk5, new DelegatedModel(ModelLocationUtils.getModelLocation(bul)));
                }
            }
        });
        this.<Block>saveCollection(hn, path3, (java.util.Map<Block, ? extends Supplier<JsonElement>>)map4, (java.util.function.BiFunction<Path, Block, Path>)ModelProvider::createBlockStatePath);
        this.<ResourceLocation>saveCollection(hn, path3, map5, (java.util.function.BiFunction<Path, ResourceLocation, Path>)ModelProvider::createModelPath);
    }
    
    private <T> void saveCollection(final HashCache hn, final Path path, final Map<T, ? extends Supplier<JsonElement>> map, final BiFunction<Path, T, Path> biFunction) {
        map.forEach((object, supplier) -> {
            final Path path2 = (Path)biFunction.apply(path, object);
            try {
                DataProvider.save(ModelProvider.GSON, hn, (JsonElement)supplier.get(), path2);
            }
            catch (Exception exception7) {
                ModelProvider.LOGGER.error("Couldn't save {}", path2, exception7);
            }
        });
    }
    
    private static Path createBlockStatePath(final Path path, final Block bul) {
        final ResourceLocation vk3 = Registry.BLOCK.getKey(bul);
        return path.resolve("assets/" + vk3.getNamespace() + "/blockstates/" + vk3.getPath() + ".json");
    }
    
    private static Path createModelPath(final Path path, final ResourceLocation vk) {
        return path.resolve("assets/" + vk.getNamespace() + "/models/" + vk.getPath() + ".json");
    }
    
    public String getName() {
        return "Block State Definitions";
    }
    
    static {
        LOGGER = LogManager.getLogger();
        GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
    }
}

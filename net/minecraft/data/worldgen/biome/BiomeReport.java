package net.minecraft.data.worldgen.biome;

import com.google.gson.GsonBuilder;
import org.apache.logging.log4j.LogManager;
import net.minecraft.resources.ResourceLocation;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.function.Function;
import java.util.Iterator;
import java.nio.file.Path;
import java.io.IOException;
import com.google.gson.JsonElement;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.Encoder;
import com.mojang.serialization.JsonOps;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.resources.ResourceKey;
import java.util.Map;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.data.HashCache;
import net.minecraft.data.DataGenerator;
import com.google.gson.Gson;
import org.apache.logging.log4j.Logger;
import net.minecraft.data.DataProvider;

public class BiomeReport implements DataProvider {
    private static final Logger LOGGER;
    private static final Gson GSON;
    private final DataGenerator generator;
    
    public BiomeReport(final DataGenerator hl) {
        this.generator = hl;
    }
    
    public void run(final HashCache hn) {
        final Path path3 = this.generator.getOutputFolder();
        for (final Map.Entry<ResourceKey<Biome>, Biome> entry5 : BuiltinRegistries.BIOME.entrySet()) {
            final Path path4 = createPath(path3, ((ResourceKey)entry5.getKey()).location());
            final Biome bss7 = (Biome)entry5.getValue();
            final Function<Supplier<Biome>, DataResult<JsonElement>> function8 = (Function<Supplier<Biome>, DataResult<JsonElement>>)JsonOps.INSTANCE.withEncoder((Encoder)Biome.CODEC);
            try {
                final Optional<JsonElement> optional9 = (Optional<JsonElement>)((DataResult)function8.apply((() -> bss7))).result();
                if (optional9.isPresent()) {
                    DataProvider.save(BiomeReport.GSON, hn, (JsonElement)optional9.get(), path4);
                }
                else {
                    BiomeReport.LOGGER.error("Couldn't serialize biome {}", path4);
                }
            }
            catch (IOException iOException9) {
                BiomeReport.LOGGER.error("Couldn't save biome {}", path4, iOException9);
            }
        }
    }
    
    private static Path createPath(final Path path, final ResourceLocation vk) {
        return path.resolve("reports/biomes/" + vk.getPath() + ".json");
    }
    
    public String getName() {
        return "Biomes";
    }
    
    static {
        LOGGER = LogManager.getLogger();
        GSON = new GsonBuilder().setPrettyPrinting().create();
    }
}

package net.minecraft.data.info;

import com.google.gson.GsonBuilder;
import java.io.IOException;
import java.nio.file.Path;
import com.google.common.collect.UnmodifiableIterator;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.resources.ResourceLocation;
import java.util.Iterator;
import net.minecraft.world.level.block.state.BlockState;
import com.google.gson.JsonElement;
import net.minecraft.Util;
import com.google.gson.JsonArray;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.Block;
import net.minecraft.core.Registry;
import com.google.gson.JsonObject;
import net.minecraft.data.HashCache;
import net.minecraft.data.DataGenerator;
import com.google.gson.Gson;
import net.minecraft.data.DataProvider;

public class BlockListReport implements DataProvider {
    private static final Gson GSON;
    private final DataGenerator generator;
    
    public BlockListReport(final DataGenerator hl) {
        this.generator = hl;
    }
    
    public void run(final HashCache hn) throws IOException {
        final JsonObject jsonObject3 = new JsonObject();
        for (final Block bul5 : Registry.BLOCK) {
            final ResourceLocation vk6 = Registry.BLOCK.getKey(bul5);
            final JsonObject jsonObject4 = new JsonObject();
            final StateDefinition<Block, BlockState> cef8 = bul5.getStateDefinition();
            if (!cef8.getProperties().isEmpty()) {
                final JsonObject jsonObject5 = new JsonObject();
                for (final Property<?> cfg11 : cef8.getProperties()) {
                    final JsonArray jsonArray12 = new JsonArray();
                    for (final Comparable<?> comparable14 : cfg11.getPossibleValues()) {
                        jsonArray12.add(Util.getPropertyName(cfg11, comparable14));
                    }
                    jsonObject5.add(cfg11.getName(), (JsonElement)jsonArray12);
                }
                jsonObject4.add("properties", (JsonElement)jsonObject5);
            }
            final JsonArray jsonArray13 = new JsonArray();
            for (final BlockState cee11 : cef8.getPossibleStates()) {
                final JsonObject jsonObject6 = new JsonObject();
                final JsonObject jsonObject7 = new JsonObject();
                for (final Property<?> cfg12 : cef8.getProperties()) {
                    jsonObject7.addProperty(cfg12.getName(), Util.getPropertyName(cfg12, cee11.getValue(cfg12)));
                }
                if (jsonObject7.size() > 0) {
                    jsonObject6.add("properties", (JsonElement)jsonObject7);
                }
                jsonObject6.addProperty("id", (Number)Block.getId(cee11));
                if (cee11 == bul5.defaultBlockState()) {
                    jsonObject6.addProperty("default", Boolean.valueOf(true));
                }
                jsonArray13.add((JsonElement)jsonObject6);
            }
            jsonObject4.add("states", (JsonElement)jsonArray13);
            jsonObject3.add(vk6.toString(), (JsonElement)jsonObject4);
        }
        final Path path4 = this.generator.getOutputFolder().resolve("reports/blocks.json");
        DataProvider.save(BlockListReport.GSON, hn, (JsonElement)jsonObject3, path4);
    }
    
    public String getName() {
        return "Block List";
    }
    
    static {
        GSON = new GsonBuilder().setPrettyPrinting().create();
    }
}

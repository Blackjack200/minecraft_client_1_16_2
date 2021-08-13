package net.minecraft.data.info;

import com.google.gson.GsonBuilder;
import java.util.Iterator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.DefaultedRegistry;
import java.io.IOException;
import java.nio.file.Path;
import com.google.gson.JsonElement;
import net.minecraft.core.Registry;
import com.google.gson.JsonObject;
import net.minecraft.data.HashCache;
import net.minecraft.data.DataGenerator;
import com.google.gson.Gson;
import net.minecraft.data.DataProvider;

public class RegistryDumpReport implements DataProvider {
    private static final Gson GSON;
    private final DataGenerator generator;
    
    public RegistryDumpReport(final DataGenerator hl) {
        this.generator = hl;
    }
    
    public void run(final HashCache hn) throws IOException {
        final JsonObject jsonObject3 = new JsonObject();
        Registry.REGISTRY.keySet().forEach(vk -> jsonObject3.add(vk.toString(), RegistryDumpReport.dumpRegistry((Registry<Object>)Registry.REGISTRY.get(vk))));
        final Path path4 = this.generator.getOutputFolder().resolve("reports/registries.json");
        DataProvider.save(RegistryDumpReport.GSON, hn, (JsonElement)jsonObject3, path4);
    }
    
    private static <T> JsonElement dumpRegistry(final Registry<T> gm) {
        final JsonObject jsonObject2 = new JsonObject();
        if (gm instanceof DefaultedRegistry) {
            final ResourceLocation vk3 = ((DefaultedRegistry)gm).getDefaultKey();
            jsonObject2.addProperty("default", vk3.toString());
        }
        final int integer3 = Registry.REGISTRY.getId(gm);
        jsonObject2.addProperty("protocol_id", (Number)integer3);
        final JsonObject jsonObject3 = new JsonObject();
        for (final ResourceLocation vk4 : gm.keySet()) {
            final T object7 = gm.get(vk4);
            final int integer4 = gm.getId(object7);
            final JsonObject jsonObject4 = new JsonObject();
            jsonObject4.addProperty("protocol_id", (Number)integer4);
            jsonObject3.add(vk4.toString(), (JsonElement)jsonObject4);
        }
        jsonObject2.add("entries", (JsonElement)jsonObject3);
        return (JsonElement)jsonObject2;
    }
    
    public String getName() {
        return "Registry Dump";
    }
    
    static {
        GSON = new GsonBuilder().setPrettyPrinting().create();
    }
}

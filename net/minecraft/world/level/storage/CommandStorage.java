package net.minecraft.world.level.storage;

import net.minecraft.nbt.Tag;
import java.util.Iterator;
import net.minecraft.world.level.saveddata.SavedData;
import java.util.stream.Stream;
import java.util.function.Supplier;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import com.google.common.collect.Maps;
import java.util.Map;

public class CommandStorage {
    private final Map<String, Container> namespaces;
    private final DimensionDataStorage storage;
    
    public CommandStorage(final DimensionDataStorage cxz) {
        this.namespaces = (Map<String, Container>)Maps.newHashMap();
        this.storage = cxz;
    }
    
    private Container newStorage(final String string1, final String string2) {
        final Container a4 = new Container(string2);
        this.namespaces.put(string1, a4);
        return a4;
    }
    
    public CompoundTag get(final ResourceLocation vk) {
        final String string3 = vk.getNamespace();
        final String string4 = createId(string3);
        final Container a5 = this.storage.<Container>get((java.util.function.Supplier<Container>)(() -> this.newStorage(string3, string4)), string4);
        return (a5 != null) ? a5.get(vk.getPath()) : new CompoundTag();
    }
    
    public void set(final ResourceLocation vk, final CompoundTag md) {
        final String string4 = vk.getNamespace();
        final String string5 = createId(string4);
        this.storage.<Container>computeIfAbsent((java.util.function.Supplier<Container>)(() -> this.newStorage(string4, string5)), string5).put(vk.getPath(), md);
    }
    
    public Stream<ResourceLocation> keys() {
        return (Stream<ResourceLocation>)this.namespaces.entrySet().stream().flatMap(entry -> ((Container)entry.getValue()).getKeys((String)entry.getKey()));
    }
    
    private static String createId(final String string) {
        return "command_storage_" + string;
    }
    
    static class Container extends SavedData {
        private final Map<String, CompoundTag> storage;
        
        public Container(final String string) {
            super(string);
            this.storage = (Map<String, CompoundTag>)Maps.newHashMap();
        }
        
        @Override
        public void load(final CompoundTag md) {
            final CompoundTag md2 = md.getCompound("contents");
            for (final String string5 : md2.getAllKeys()) {
                this.storage.put(string5, md2.getCompound(string5));
            }
        }
        
        @Override
        public CompoundTag save(final CompoundTag md) {
            final CompoundTag md2 = new CompoundTag();
            this.storage.forEach((string, md3) -> md2.put(string, md3.copy()));
            md.put("contents", (Tag)md2);
            return md;
        }
        
        public CompoundTag get(final String string) {
            final CompoundTag md3 = (CompoundTag)this.storage.get(string);
            return (md3 != null) ? md3 : new CompoundTag();
        }
        
        public void put(final String string, final CompoundTag md) {
            if (md.isEmpty()) {
                this.storage.remove(string);
            }
            else {
                this.storage.put(string, md);
            }
            this.setDirty();
        }
        
        public Stream<ResourceLocation> getKeys(final String string) {
            return (Stream<ResourceLocation>)this.storage.keySet().stream().map(string2 -> new ResourceLocation(string, string2));
        }
    }
}

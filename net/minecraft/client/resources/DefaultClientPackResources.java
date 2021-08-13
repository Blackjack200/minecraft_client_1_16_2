package net.minecraft.client.resources;

import java.util.Collection;
import java.util.function.Predicate;
import javax.annotation.Nullable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileInputStream;
import java.io.InputStream;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.VanillaPackResources;

public class DefaultClientPackResources extends VanillaPackResources {
    private final AssetIndex assetIndex;
    
    public DefaultClientPackResources(final AssetIndex ejy) {
        super("minecraft", "realms");
        this.assetIndex = ejy;
    }
    
    @Nullable
    @Override
    protected InputStream getResourceAsStream(final PackType abi, final ResourceLocation vk) {
        if (abi == PackType.CLIENT_RESOURCES) {
            final File file4 = this.assetIndex.getFile(vk);
            if (file4 != null && file4.exists()) {
                try {
                    return (InputStream)new FileInputStream(file4);
                }
                catch (FileNotFoundException ex) {}
            }
        }
        return super.getResourceAsStream(abi, vk);
    }
    
    @Override
    public boolean hasResource(final PackType abi, final ResourceLocation vk) {
        if (abi == PackType.CLIENT_RESOURCES) {
            final File file4 = this.assetIndex.getFile(vk);
            if (file4 != null && file4.exists()) {
                return true;
            }
        }
        return super.hasResource(abi, vk);
    }
    
    @Nullable
    @Override
    protected InputStream getResourceAsStream(final String string) {
        final File file3 = this.assetIndex.getRootFile(string);
        if (file3 != null && file3.exists()) {
            try {
                return (InputStream)new FileInputStream(file3);
            }
            catch (FileNotFoundException ex) {}
        }
        return super.getResourceAsStream(string);
    }
    
    @Override
    public Collection<ResourceLocation> getResources(final PackType abi, final String string2, final String string3, final int integer, final Predicate<String> predicate) {
        final Collection<ResourceLocation> collection7 = super.getResources(abi, string2, string3, integer, predicate);
        collection7.addAll((Collection)this.assetIndex.getFiles(string3, string2, integer, predicate));
        return collection7;
    }
}

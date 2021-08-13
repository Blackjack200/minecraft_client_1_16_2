package net.minecraft.world.level.chunk.storage;

import java.util.zip.DeflaterOutputStream;
import java.util.zip.InflaterInputStream;
import java.util.zip.GZIPOutputStream;
import java.util.zip.GZIPInputStream;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import java.io.IOException;
import javax.annotation.Nullable;
import java.io.OutputStream;
import java.io.InputStream;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;

public class RegionFileVersion {
    private static final Int2ObjectMap<RegionFileVersion> VERSIONS;
    public static final RegionFileVersion VERSION_GZIP;
    public static final RegionFileVersion VERSION_DEFLATE;
    public static final RegionFileVersion VERSION_NONE;
    private final int id;
    private final StreamWrapper<InputStream> inputWrapper;
    private final StreamWrapper<OutputStream> outputWrapper;
    
    private RegionFileVersion(final int integer, final StreamWrapper<InputStream> a2, final StreamWrapper<OutputStream> a3) {
        this.id = integer;
        this.inputWrapper = a2;
        this.outputWrapper = a3;
    }
    
    private static RegionFileVersion register(final RegionFileVersion cgx) {
        RegionFileVersion.VERSIONS.put(cgx.id, cgx);
        return cgx;
    }
    
    @Nullable
    public static RegionFileVersion fromId(final int integer) {
        return (RegionFileVersion)RegionFileVersion.VERSIONS.get(integer);
    }
    
    public static boolean isValidVersion(final int integer) {
        return RegionFileVersion.VERSIONS.containsKey(integer);
    }
    
    public int getId() {
        return this.id;
    }
    
    public OutputStream wrap(final OutputStream outputStream) throws IOException {
        return this.outputWrapper.wrap(outputStream);
    }
    
    public InputStream wrap(final InputStream inputStream) throws IOException {
        return this.inputWrapper.wrap(inputStream);
    }
    
    static {
        VERSIONS = (Int2ObjectMap)new Int2ObjectOpenHashMap();
        VERSION_GZIP = register(new RegionFileVersion(1, GZIPInputStream::new, GZIPOutputStream::new));
        VERSION_DEFLATE = register(new RegionFileVersion(2, InflaterInputStream::new, DeflaterOutputStream::new));
        VERSION_NONE = register(new RegionFileVersion(3, inputStream -> inputStream, outputStream -> outputStream));
    }
    
    @FunctionalInterface
    interface StreamWrapper<O> {
        O wrap(final O object) throws IOException;
    }
}

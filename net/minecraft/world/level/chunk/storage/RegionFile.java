package net.minecraft.world.level.chunk.storage;

import java.io.ByteArrayOutputStream;
import org.apache.logging.log4j.LogManager;
import java.nio.file.StandardCopyOption;
import java.nio.file.CopyOption;
import java.nio.file.attribute.FileAttribute;
import net.minecraft.Util;
import java.io.BufferedOutputStream;
import java.io.OutputStream;
import java.io.DataOutputStream;
import java.io.ByteArrayInputStream;
import java.io.BufferedInputStream;
import javax.annotation.Nullable;
import java.io.InputStream;
import java.io.DataInputStream;
import net.minecraft.world.level.ChunkPos;
import java.nio.file.StandardOpenOption;
import java.nio.file.OpenOption;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.io.IOException;
import java.io.File;
import com.google.common.annotations.VisibleForTesting;
import java.nio.IntBuffer;
import java.nio.file.Path;
import java.nio.channels.FileChannel;
import java.nio.ByteBuffer;
import org.apache.logging.log4j.Logger;

public class RegionFile implements AutoCloseable {
    private static final Logger LOGGER;
    private static final ByteBuffer PADDING_BUFFER;
    private final FileChannel file;
    private final Path externalFileDir;
    private final RegionFileVersion version;
    private final ByteBuffer header;
    private final IntBuffer offsets;
    private final IntBuffer timestamps;
    @VisibleForTesting
    protected final RegionBitmap usedSectors;
    
    public RegionFile(final File file1, final File file2, final boolean boolean3) throws IOException {
        this(file1.toPath(), file2.toPath(), RegionFileVersion.VERSION_DEFLATE, boolean3);
    }
    
    public RegionFile(final Path path1, final Path path2, final RegionFileVersion cgx, final boolean boolean4) throws IOException {
        this.header = ByteBuffer.allocateDirect(8192);
        this.usedSectors = new RegionBitmap();
        this.version = cgx;
        if (!Files.isDirectory(path2, new LinkOption[0])) {
            throw new IllegalArgumentException(new StringBuilder().append("Expected directory, got ").append(path2.toAbsolutePath()).toString());
        }
        this.externalFileDir = path2;
        (this.offsets = this.header.asIntBuffer()).limit(1024);
        this.header.position(4096);
        this.timestamps = this.header.asIntBuffer();
        if (boolean4) {
            this.file = FileChannel.open(path1, new OpenOption[] { (OpenOption)StandardOpenOption.CREATE, (OpenOption)StandardOpenOption.READ, (OpenOption)StandardOpenOption.WRITE, (OpenOption)StandardOpenOption.DSYNC });
        }
        else {
            this.file = FileChannel.open(path1, new OpenOption[] { (OpenOption)StandardOpenOption.CREATE, (OpenOption)StandardOpenOption.READ, (OpenOption)StandardOpenOption.WRITE });
        }
        this.usedSectors.force(0, 2);
        this.header.position(0);
        final int integer6 = this.file.read(this.header, 0L);
        if (integer6 != -1) {
            if (integer6 != 8192) {
                RegionFile.LOGGER.warn("Region file {} has truncated header: {}", path1, integer6);
            }
            final long long7 = Files.size(path1);
            for (int integer7 = 0; integer7 < 1024; ++integer7) {
                final int integer8 = this.offsets.get(integer7);
                if (integer8 != 0) {
                    final int integer9 = getSectorNumber(integer8);
                    final int integer10 = getNumSectors(integer8);
                    if (integer9 < 2) {
                        RegionFile.LOGGER.warn("Region file {} has invalid sector at index: {}; sector {} overlaps with header", path1, integer7, integer9);
                        this.offsets.put(integer7, 0);
                    }
                    else if (integer10 == 0) {
                        RegionFile.LOGGER.warn("Region file {} has an invalid sector at index: {}; size has to be > 0", path1, integer7);
                        this.offsets.put(integer7, 0);
                    }
                    else if (integer9 * 4096L > long7) {
                        RegionFile.LOGGER.warn("Region file {} has an invalid sector at index: {}; sector {} is out of bounds", path1, integer7, integer9);
                        this.offsets.put(integer7, 0);
                    }
                    else {
                        this.usedSectors.force(integer9, integer10);
                    }
                }
            }
        }
    }
    
    private Path getExternalChunkPath(final ChunkPos bra) {
        final String string3 = new StringBuilder().append("c.").append(bra.x).append(".").append(bra.z).append(".mcc").toString();
        return this.externalFileDir.resolve(string3);
    }
    
    @Nullable
    public synchronized DataInputStream getChunkDataInputStream(final ChunkPos bra) throws IOException {
        final int integer3 = this.getOffset(bra);
        if (integer3 == 0) {
            return null;
        }
        final int integer4 = getSectorNumber(integer3);
        final int integer5 = getNumSectors(integer3);
        final int integer6 = integer5 * 4096;
        final ByteBuffer byteBuffer7 = ByteBuffer.allocate(integer6);
        this.file.read(byteBuffer7, (long)(integer4 * 4096));
        byteBuffer7.flip();
        if (byteBuffer7.remaining() < 5) {
            RegionFile.LOGGER.error("Chunk {} header is truncated: expected {} but read {}", bra, integer6, byteBuffer7.remaining());
            return null;
        }
        final int integer7 = byteBuffer7.getInt();
        final byte byte9 = byteBuffer7.get();
        if (integer7 == 0) {
            RegionFile.LOGGER.warn("Chunk {} is allocated, but stream is missing", bra);
            return null;
        }
        final int integer8 = integer7 - 1;
        if (isExternalStreamChunk(byte9)) {
            if (integer8 != 0) {
                RegionFile.LOGGER.warn("Chunk has both internal and external streams");
            }
            return this.createExternalChunkInputStream(bra, getExternalChunkVersion(byte9));
        }
        if (integer8 > byteBuffer7.remaining()) {
            RegionFile.LOGGER.error("Chunk {} stream is truncated: expected {} but read {}", bra, integer8, byteBuffer7.remaining());
            return null;
        }
        if (integer8 < 0) {
            RegionFile.LOGGER.error("Declared size {} of chunk {} is negative", integer7, bra);
            return null;
        }
        return this.createChunkInputStream(bra, byte9, (InputStream)createStream(byteBuffer7, integer8));
    }
    
    private static boolean isExternalStreamChunk(final byte byte1) {
        return (byte1 & 0x80) != 0x0;
    }
    
    private static byte getExternalChunkVersion(final byte byte1) {
        return (byte)(byte1 & 0xFFFFFF7F);
    }
    
    @Nullable
    private DataInputStream createChunkInputStream(final ChunkPos bra, final byte byte2, final InputStream inputStream) throws IOException {
        final RegionFileVersion cgx5 = RegionFileVersion.fromId(byte2);
        if (cgx5 == null) {
            RegionFile.LOGGER.error("Chunk {} has invalid chunk stream version {}", bra, byte2);
            return null;
        }
        return new DataInputStream((InputStream)new BufferedInputStream(cgx5.wrap(inputStream)));
    }
    
    @Nullable
    private DataInputStream createExternalChunkInputStream(final ChunkPos bra, final byte byte2) throws IOException {
        final Path path4 = this.getExternalChunkPath(bra);
        if (!Files.isRegularFile(path4, new LinkOption[0])) {
            RegionFile.LOGGER.error("External chunk path {} is not file", path4);
            return null;
        }
        return this.createChunkInputStream(bra, byte2, Files.newInputStream(path4, new OpenOption[0]));
    }
    
    private static ByteArrayInputStream createStream(final ByteBuffer byteBuffer, final int integer) {
        return new ByteArrayInputStream(byteBuffer.array(), byteBuffer.position(), integer);
    }
    
    private int packSectorOffset(final int integer1, final int integer2) {
        return integer1 << 8 | integer2;
    }
    
    private static int getNumSectors(final int integer) {
        return integer & 0xFF;
    }
    
    private static int getSectorNumber(final int integer) {
        return integer >> 8 & 0xFFFFFF;
    }
    
    private static int sizeToSectors(final int integer) {
        return (integer + 4096 - 1) / 4096;
    }
    
    public boolean doesChunkExist(final ChunkPos bra) {
        final int integer3 = this.getOffset(bra);
        if (integer3 == 0) {
            return false;
        }
        final int integer4 = getSectorNumber(integer3);
        final int integer5 = getNumSectors(integer3);
        final ByteBuffer byteBuffer6 = ByteBuffer.allocate(5);
        try {
            this.file.read(byteBuffer6, (long)(integer4 * 4096));
            byteBuffer6.flip();
            if (byteBuffer6.remaining() != 5) {
                return false;
            }
            final int integer6 = byteBuffer6.getInt();
            final byte byte8 = byteBuffer6.get();
            if (isExternalStreamChunk(byte8)) {
                if (!RegionFileVersion.isValidVersion(getExternalChunkVersion(byte8))) {
                    return false;
                }
                if (!Files.isRegularFile(this.getExternalChunkPath(bra), new LinkOption[0])) {
                    return false;
                }
            }
            else {
                if (!RegionFileVersion.isValidVersion(byte8)) {
                    return false;
                }
                if (integer6 == 0) {
                    return false;
                }
                final int integer7 = integer6 - 1;
                if (integer7 < 0 || integer7 > 4096 * integer5) {
                    return false;
                }
            }
        }
        catch (IOException iOException7) {
            return false;
        }
        return true;
    }
    
    public DataOutputStream getChunkDataOutputStream(final ChunkPos bra) throws IOException {
        return new DataOutputStream((OutputStream)new BufferedOutputStream(this.version.wrap((OutputStream)new ChunkBuffer(bra))));
    }
    
    public void flush() throws IOException {
        this.file.force(true);
    }
    
    protected synchronized void write(final ChunkPos bra, final ByteBuffer byteBuffer) throws IOException {
        final int integer4 = getOffsetIndex(bra);
        final int integer5 = this.offsets.get(integer4);
        final int integer6 = getSectorNumber(integer5);
        final int integer7 = getNumSectors(integer5);
        final int integer8 = byteBuffer.remaining();
        int integer9 = sizeToSectors(integer8);
        int integer10;
        CommitOp b11;
        if (integer9 >= 256) {
            final Path path12 = this.getExternalChunkPath(bra);
            RegionFile.LOGGER.warn("Saving oversized chunk {} ({} bytes} to external file {}", bra, integer8, path12);
            integer9 = 1;
            integer10 = this.usedSectors.allocate(integer9);
            b11 = this.writeToExternalFile(path12, byteBuffer);
            final ByteBuffer byteBuffer2 = this.createExternalStub();
            this.file.write(byteBuffer2, (long)(integer10 * 4096));
        }
        else {
            integer10 = this.usedSectors.allocate(integer9);
            b11 = (() -> Files.deleteIfExists(this.getExternalChunkPath(bra)));
            this.file.write(byteBuffer, (long)(integer10 * 4096));
        }
        final int integer11 = (int)(Util.getEpochMillis() / 1000L);
        this.offsets.put(integer4, this.packSectorOffset(integer10, integer9));
        this.timestamps.put(integer4, integer11);
        this.writeHeader();
        b11.run();
        if (integer6 != 0) {
            this.usedSectors.free(integer6, integer7);
        }
    }
    
    private ByteBuffer createExternalStub() {
        final ByteBuffer byteBuffer2 = ByteBuffer.allocate(5);
        byteBuffer2.putInt(1);
        byteBuffer2.put((byte)(this.version.getId() | 0x80));
        byteBuffer2.flip();
        return byteBuffer2;
    }
    
    private CommitOp writeToExternalFile(final Path path, final ByteBuffer byteBuffer) throws IOException {
        final Path path2 = Files.createTempFile(this.externalFileDir, "tmp", (String)null, new FileAttribute[0]);
        try (final FileChannel fileChannel5 = FileChannel.open(path2, new OpenOption[] { (OpenOption)StandardOpenOption.CREATE, (OpenOption)StandardOpenOption.WRITE })) {
            byteBuffer.position(5);
            fileChannel5.write(byteBuffer);
        }
        return () -> Files.move(path2, path, new CopyOption[] { (CopyOption)StandardCopyOption.REPLACE_EXISTING });
    }
    
    private void writeHeader() throws IOException {
        this.header.position(0);
        this.file.write(this.header, 0L);
    }
    
    private int getOffset(final ChunkPos bra) {
        return this.offsets.get(getOffsetIndex(bra));
    }
    
    public boolean hasChunk(final ChunkPos bra) {
        return this.getOffset(bra) != 0;
    }
    
    private static int getOffsetIndex(final ChunkPos bra) {
        return bra.getRegionLocalX() + bra.getRegionLocalZ() * 32;
    }
    
    public void close() throws IOException {
        try {
            this.padToFullSector();
        }
        finally {
            try {
                this.file.force(true);
            }
            finally {
                this.file.close();
            }
        }
    }
    
    private void padToFullSector() throws IOException {
        final int integer2 = (int)this.file.size();
        final int integer3 = sizeToSectors(integer2) * 4096;
        if (integer2 != integer3) {
            final ByteBuffer byteBuffer4 = RegionFile.PADDING_BUFFER.duplicate();
            byteBuffer4.position(0);
            this.file.write(byteBuffer4, (long)(integer3 - 1));
        }
    }
    
    static {
        LOGGER = LogManager.getLogger();
        PADDING_BUFFER = ByteBuffer.allocateDirect(1);
    }
    
    class ChunkBuffer extends ByteArrayOutputStream {
        private final ChunkPos pos;
        
        public ChunkBuffer(final ChunkPos bra) {
            super(8096);
            super.write(0);
            super.write(0);
            super.write(0);
            super.write(0);
            super.write(RegionFile.this.version.getId());
            this.pos = bra;
        }
        
        public void close() throws IOException {
            final ByteBuffer byteBuffer2 = ByteBuffer.wrap(this.buf, 0, this.count);
            byteBuffer2.putInt(0, this.count - 5 + 1);
            RegionFile.this.write(this.pos, byteBuffer2);
        }
    }
    
    interface CommitOp {
        void run() throws IOException;
    }
}

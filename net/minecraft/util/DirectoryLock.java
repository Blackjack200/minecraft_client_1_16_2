package net.minecraft.util;

import com.google.common.base.Charsets;
import java.nio.file.NoSuchFileException;
import java.nio.file.AccessDeniedException;
import java.io.IOException;
import java.nio.file.StandardOpenOption;
import java.nio.file.OpenOption;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.ByteBuffer;
import java.nio.channels.FileLock;
import java.nio.channels.FileChannel;

public class DirectoryLock implements AutoCloseable {
    private final FileChannel lockFile;
    private final FileLock lock;
    private static final ByteBuffer DUMMY;
    
    public static DirectoryLock create(final Path path) throws IOException {
        final Path path2 = path.resolve("session.lock");
        if (!Files.isDirectory(path, new LinkOption[0])) {
            Files.createDirectories(path, new FileAttribute[0]);
        }
        final FileChannel fileChannel3 = FileChannel.open(path2, new OpenOption[] { (OpenOption)StandardOpenOption.CREATE, (OpenOption)StandardOpenOption.WRITE });
        try {
            fileChannel3.write(DirectoryLock.DUMMY.duplicate());
            fileChannel3.force(true);
            final FileLock fileLock4 = fileChannel3.tryLock();
            if (fileLock4 == null) {
                throw LockException.alreadyLocked(path2);
            }
            return new DirectoryLock(fileChannel3, fileLock4);
        }
        catch (IOException iOException4) {
            try {
                fileChannel3.close();
            }
            catch (IOException iOException5) {
                iOException4.addSuppressed((Throwable)iOException5);
            }
            throw iOException4;
        }
    }
    
    private DirectoryLock(final FileChannel fileChannel, final FileLock fileLock) {
        this.lockFile = fileChannel;
        this.lock = fileLock;
    }
    
    public void close() throws IOException {
        try {
            if (this.lock.isValid()) {
                this.lock.release();
            }
        }
        finally {
            if (this.lockFile.isOpen()) {
                this.lockFile.close();
            }
        }
    }
    
    public boolean isValid() {
        return this.lock.isValid();
    }
    
    public static boolean isLocked(final Path path) throws IOException {
        final Path path2 = path.resolve("session.lock");
        try (final FileChannel fileChannel3 = FileChannel.open(path2, new OpenOption[] { (OpenOption)StandardOpenOption.WRITE });
             final FileLock fileLock5 = fileChannel3.tryLock()) {
            return fileLock5 == null;
        }
        catch (AccessDeniedException accessDeniedException3) {
            return true;
        }
        catch (NoSuchFileException noSuchFileException3) {
            return false;
        }
    }
    
    static {
        final byte[] arr1 = "\u2603".getBytes(Charsets.UTF_8);
        (DUMMY = ByteBuffer.allocateDirect(arr1.length)).put(arr1);
        DirectoryLock.DUMMY.flip();
    }
    
    public static class LockException extends IOException {
        private LockException(final Path path, final String string) {
            super(new StringBuilder().append(path.toAbsolutePath()).append(": ").append(string).toString());
        }
        
        public static LockException alreadyLocked(final Path path) {
            return new LockException(path, "already locked (possibly by other Minecraft instance?)");
        }
    }
}

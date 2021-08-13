package net.minecraft.nbt;

import net.minecraft.CrashReportCategory;
import net.minecraft.ReportedException;
import net.minecraft.CrashReport;
import javax.annotation.Nullable;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.BufferedOutputStream;
import java.util.zip.GZIPOutputStream;
import java.io.OutputStream;
import java.io.FileOutputStream;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.BufferedInputStream;
import java.util.zip.GZIPInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.FileInputStream;
import java.io.File;

public class NbtIo {
    public static CompoundTag readCompressed(final File file) throws IOException {
        try (final InputStream inputStream2 = (InputStream)new FileInputStream(file)) {
            return readCompressed(inputStream2);
        }
    }
    
    public static CompoundTag readCompressed(final InputStream inputStream) throws IOException {
        try (final DataInputStream dataInputStream2 = new DataInputStream((InputStream)new BufferedInputStream((InputStream)new GZIPInputStream(inputStream)))) {
            return read((DataInput)dataInputStream2, NbtAccounter.UNLIMITED);
        }
    }
    
    public static void writeCompressed(final CompoundTag md, final File file) throws IOException {
        try (final OutputStream outputStream3 = (OutputStream)new FileOutputStream(file)) {
            writeCompressed(md, outputStream3);
        }
    }
    
    public static void writeCompressed(final CompoundTag md, final OutputStream outputStream) throws IOException {
        try (final DataOutputStream dataOutputStream3 = new DataOutputStream((OutputStream)new BufferedOutputStream((OutputStream)new GZIPOutputStream(outputStream)))) {
            write(md, (DataOutput)dataOutputStream3);
        }
    }
    
    public static void write(final CompoundTag md, final File file) throws IOException {
        try (final FileOutputStream fileOutputStream3 = new FileOutputStream(file);
             final DataOutputStream dataOutputStream5 = new DataOutputStream((OutputStream)fileOutputStream3)) {
            write(md, (DataOutput)dataOutputStream5);
        }
    }
    
    @Nullable
    public static CompoundTag read(final File file) throws IOException {
        if (!file.exists()) {
            return null;
        }
        try (final FileInputStream fileInputStream2 = new FileInputStream(file);
             final DataInputStream dataInputStream4 = new DataInputStream((InputStream)fileInputStream2)) {
            return read((DataInput)dataInputStream4, NbtAccounter.UNLIMITED);
        }
    }
    
    public static CompoundTag read(final DataInput dataInput) throws IOException {
        return read(dataInput, NbtAccounter.UNLIMITED);
    }
    
    public static CompoundTag read(final DataInput dataInput, final NbtAccounter mm) throws IOException {
        final Tag mt3 = readUnnamedTag(dataInput, 0, mm);
        if (mt3 instanceof CompoundTag) {
            return (CompoundTag)mt3;
        }
        throw new IOException("Root tag must be a named compound tag");
    }
    
    public static void write(final CompoundTag md, final DataOutput dataOutput) throws IOException {
        writeUnnamedTag(md, dataOutput);
    }
    
    private static void writeUnnamedTag(final Tag mt, final DataOutput dataOutput) throws IOException {
        dataOutput.writeByte((int)mt.getId());
        if (mt.getId() == 0) {
            return;
        }
        dataOutput.writeUTF("");
        mt.write(dataOutput);
    }
    
    private static Tag readUnnamedTag(final DataInput dataInput, final int integer, final NbtAccounter mm) throws IOException {
        final byte byte4 = dataInput.readByte();
        if (byte4 == 0) {
            return EndTag.INSTANCE;
        }
        dataInput.readUTF();
        try {
            return (Tag)TagTypes.getType(byte4).load(dataInput, integer, mm);
        }
        catch (IOException iOException5) {
            final CrashReport l6 = CrashReport.forThrowable((Throwable)iOException5, "Loading NBT data");
            final CrashReportCategory m7 = l6.addCategory("NBT Tag");
            m7.setDetail("Tag type", byte4);
            throw new ReportedException(l6);
        }
    }
}

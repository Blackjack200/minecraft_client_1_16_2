package net.minecraft.server.rcon;

import java.io.IOException;
import java.io.OutputStream;
import java.io.DataOutputStream;
import java.io.ByteArrayOutputStream;

public class NetworkDataOutputStream {
    private final ByteArrayOutputStream outputStream;
    private final DataOutputStream dataOutputStream;
    
    public NetworkDataOutputStream(final int integer) {
        this.outputStream = new ByteArrayOutputStream(integer);
        this.dataOutputStream = new DataOutputStream((OutputStream)this.outputStream);
    }
    
    public void writeBytes(final byte[] arr) throws IOException {
        this.dataOutputStream.write(arr, 0, arr.length);
    }
    
    public void writeString(final String string) throws IOException {
        this.dataOutputStream.writeBytes(string);
        this.dataOutputStream.write(0);
    }
    
    public void write(final int integer) throws IOException {
        this.dataOutputStream.write(integer);
    }
    
    public void writeShort(final short short1) throws IOException {
        this.dataOutputStream.writeShort((int)Short.reverseBytes(short1));
    }
    
    public byte[] toByteArray() {
        return this.outputStream.toByteArray();
    }
    
    public void reset() {
        this.outputStream.reset();
    }
}

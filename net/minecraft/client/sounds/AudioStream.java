package net.minecraft.client.sounds;

import java.io.IOException;
import java.nio.ByteBuffer;
import javax.sound.sampled.AudioFormat;
import java.io.Closeable;

public interface AudioStream extends Closeable {
    AudioFormat getFormat();
    
    ByteBuffer read(final int integer) throws IOException;
}

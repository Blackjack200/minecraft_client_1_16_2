package com.mojang.blaze3d.audio;

import org.lwjgl.openal.AL10;
import java.util.OptionalInt;
import javax.sound.sampled.AudioFormat;
import javax.annotation.Nullable;
import java.nio.ByteBuffer;

public class SoundBuffer {
    @Nullable
    private ByteBuffer data;
    private final AudioFormat format;
    private boolean hasAlBuffer;
    private int alBuffer;
    
    public SoundBuffer(final ByteBuffer byteBuffer, final AudioFormat audioFormat) {
        this.data = byteBuffer;
        this.format = audioFormat;
    }
    
    OptionalInt getAlBuffer() {
        if (!this.hasAlBuffer) {
            if (this.data == null) {
                return OptionalInt.empty();
            }
            final int integer2 = OpenAlUtil.audioFormatToOpenAl(this.format);
            final int[] arr3 = { 0 };
            AL10.alGenBuffers(arr3);
            if (OpenAlUtil.checkALError("Creating buffer")) {
                return OptionalInt.empty();
            }
            AL10.alBufferData(arr3[0], integer2, this.data, (int)this.format.getSampleRate());
            if (OpenAlUtil.checkALError("Assigning buffer data")) {
                return OptionalInt.empty();
            }
            this.alBuffer = arr3[0];
            this.hasAlBuffer = true;
            this.data = null;
        }
        return OptionalInt.of(this.alBuffer);
    }
    
    public void discardAlBuffer() {
        if (this.hasAlBuffer) {
            AL10.alDeleteBuffers(new int[] { this.alBuffer });
            if (OpenAlUtil.checkALError("Deleting stream buffers")) {
                return;
            }
        }
        this.hasAlBuffer = false;
    }
    
    public OptionalInt releaseAlBuffer() {
        final OptionalInt optionalInt2 = this.getAlBuffer();
        this.hasAlBuffer = false;
        return optionalInt2;
    }
}

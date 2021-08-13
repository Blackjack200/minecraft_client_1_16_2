package net.minecraft.network;

import io.netty.handler.codec.EncoderException;

public class SkipPacketException extends EncoderException {
    public SkipPacketException(final Throwable throwable) {
        super(throwable);
    }
}

package com.mojang.blaze3d.audio;

import org.apache.logging.log4j.LogManager;
import javax.sound.sampled.AudioFormat;
import org.lwjgl.openal.ALC10;
import org.lwjgl.openal.AL10;
import org.apache.logging.log4j.Logger;

public class OpenAlUtil {
    private static final Logger LOGGER;
    
    private static String alErrorToString(final int integer) {
        switch (integer) {
            case 40961: {
                return "Invalid name parameter.";
            }
            case 40962: {
                return "Invalid enumerated parameter value.";
            }
            case 40963: {
                return "Invalid parameter parameter value.";
            }
            case 40964: {
                return "Invalid operation.";
            }
            case 40965: {
                return "Unable to allocate memory.";
            }
            default: {
                return "An unrecognized error occurred.";
            }
        }
    }
    
    static boolean checkALError(final String string) {
        final int integer2 = AL10.alGetError();
        if (integer2 != 0) {
            OpenAlUtil.LOGGER.error("{}: {}", string, alErrorToString(integer2));
            return true;
        }
        return false;
    }
    
    private static String alcErrorToString(final int integer) {
        switch (integer) {
            case 40961: {
                return "Invalid device.";
            }
            case 40962: {
                return "Invalid context.";
            }
            case 40964: {
                return "Invalid value.";
            }
            case 40963: {
                return "Illegal enum.";
            }
            case 40965: {
                return "Unable to allocate memory.";
            }
            default: {
                return "An unrecognized error occurred.";
            }
        }
    }
    
    static boolean checkALCError(final long long1, final String string) {
        final int integer4 = ALC10.alcGetError(long1);
        if (integer4 != 0) {
            OpenAlUtil.LOGGER.error("{}{}: {}", string, long1, alcErrorToString(integer4));
            return true;
        }
        return false;
    }
    
    static int audioFormatToOpenAl(final AudioFormat audioFormat) {
        final AudioFormat.Encoding encoding2 = audioFormat.getEncoding();
        final int integer3 = audioFormat.getChannels();
        final int integer4 = audioFormat.getSampleSizeInBits();
        if (encoding2.equals(AudioFormat.Encoding.PCM_UNSIGNED) || encoding2.equals(AudioFormat.Encoding.PCM_SIGNED)) {
            if (integer3 == 1) {
                if (integer4 == 8) {
                    return 4352;
                }
                if (integer4 == 16) {
                    return 4353;
                }
            }
            else if (integer3 == 2) {
                if (integer4 == 8) {
                    return 4354;
                }
                if (integer4 == 16) {
                    return 4355;
                }
            }
        }
        throw new IllegalArgumentException(new StringBuilder().append("Invalid audio format: ").append(audioFormat).toString());
    }
    
    static {
        LOGGER = LogManager.getLogger();
    }
}

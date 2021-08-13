package com.mojang.blaze3d.shaders;

import org.apache.logging.log4j.LogManager;
import java.io.IOException;
import com.mojang.blaze3d.platform.GlStateManager;
import java.util.function.Supplier;
import com.mojang.blaze3d.systems.RenderSystem;
import org.apache.logging.log4j.Logger;

public class ProgramManager {
    private static final Logger LOGGER;
    
    public static void glUseProgram(final int integer) {
        RenderSystem.assertThread((Supplier<Boolean>)RenderSystem::isOnRenderThread);
        GlStateManager._glUseProgram(integer);
    }
    
    public static void releaseProgram(final Effect dfa) {
        RenderSystem.assertThread((Supplier<Boolean>)RenderSystem::isOnRenderThread);
        dfa.getFragmentProgram().close();
        dfa.getVertexProgram().close();
        GlStateManager.glDeleteProgram(dfa.getId());
    }
    
    public static int createProgram() throws IOException {
        RenderSystem.assertThread((Supplier<Boolean>)RenderSystem::isOnRenderThread);
        final int integer1 = GlStateManager.glCreateProgram();
        if (integer1 <= 0) {
            throw new IOException(new StringBuilder().append("Could not create shader program (returned program ID ").append(integer1).append(")").toString());
        }
        return integer1;
    }
    
    public static void linkProgram(final Effect dfa) throws IOException {
        RenderSystem.assertThread((Supplier<Boolean>)RenderSystem::isOnRenderThread);
        dfa.getFragmentProgram().attachToEffect(dfa);
        dfa.getVertexProgram().attachToEffect(dfa);
        GlStateManager.glLinkProgram(dfa.getId());
        final int integer2 = GlStateManager.glGetProgrami(dfa.getId(), 35714);
        if (integer2 == 0) {
            ProgramManager.LOGGER.warn("Error encountered when linking program containing VS {} and FS {}. Log output:", dfa.getVertexProgram().getName(), dfa.getFragmentProgram().getName());
            ProgramManager.LOGGER.warn(GlStateManager.glGetProgramInfoLog(dfa.getId(), 32768));
        }
    }
    
    static {
        LOGGER = LogManager.getLogger();
    }
}

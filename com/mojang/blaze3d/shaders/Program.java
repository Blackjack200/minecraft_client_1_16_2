package com.mojang.blaze3d.shaders;

import com.google.common.collect.Maps;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import java.io.IOException;
import com.mojang.blaze3d.platform.TextureUtil;
import java.io.InputStream;
import com.mojang.blaze3d.platform.GlStateManager;
import java.util.function.Supplier;
import com.mojang.blaze3d.systems.RenderSystem;

public class Program {
    private final Type type;
    private final String name;
    private final int id;
    private int references;
    
    private Program(final Type a, final int integer, final String string) {
        this.type = a;
        this.id = integer;
        this.name = string;
    }
    
    public void attachToEffect(final Effect dfa) {
        RenderSystem.assertThread((Supplier<Boolean>)RenderSystem::isOnRenderThread);
        ++this.references;
        GlStateManager.glAttachShader(dfa.getId(), this.id);
    }
    
    public void close() {
        RenderSystem.assertThread((Supplier<Boolean>)RenderSystem::isOnRenderThread);
        --this.references;
        if (this.references <= 0) {
            GlStateManager.glDeleteShader(this.id);
            this.type.getPrograms().remove(this.name);
        }
    }
    
    public String getName() {
        return this.name;
    }
    
    public static Program compileShader(final Type a, final String string2, final InputStream inputStream, final String string4) throws IOException {
        RenderSystem.assertThread((Supplier<Boolean>)RenderSystem::isOnRenderThread);
        final String string5 = TextureUtil.readResourceAsString(inputStream);
        if (string5 == null) {
            throw new IOException("Could not load program " + a.getName());
        }
        final int integer6 = GlStateManager.glCreateShader(a.getGlType());
        GlStateManager.glShaderSource(integer6, (CharSequence)string5);
        GlStateManager.glCompileShader(integer6);
        if (GlStateManager.glGetShaderi(integer6, 35713) == 0) {
            final String string6 = StringUtils.trim(GlStateManager.glGetShaderInfoLog(integer6, 32768));
            throw new IOException("Couldn't compile " + a.getName() + " program (" + string4 + ", " + string2 + ") : " + string6);
        }
        final Program dfb7 = new Program(a, integer6, string2);
        a.getPrograms().put(string2, dfb7);
        return dfb7;
    }
    
    public enum Type {
        VERTEX("vertex", ".vsh", 35633), 
        FRAGMENT("fragment", ".fsh", 35632);
        
        private final String name;
        private final String extension;
        private final int glType;
        private final Map<String, Program> programs;
        
        private Type(final String string3, final String string4, final int integer5) {
            this.programs = (Map<String, Program>)Maps.newHashMap();
            this.name = string3;
            this.extension = string4;
            this.glType = integer5;
        }
        
        public String getName() {
            return this.name;
        }
        
        public String getExtension() {
            return this.extension;
        }
        
        private int getGlType() {
            return this.glType;
        }
        
        public Map<String, Program> getPrograms() {
            return this.programs;
        }
    }
}

package net.minecraft.client.renderer;

import org.apache.logging.log4j.LogManager;
import it.unimi.dsi.fastutil.ints.IntList;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import javax.annotation.Nullable;
import com.mojang.blaze3d.platform.GlStateManager;
import java.util.function.Supplier;
import com.mojang.blaze3d.systems.RenderSystem;
import java.io.IOException;
import java.util.Iterator;
import net.minecraft.server.packs.resources.Resource;
import java.io.Closeable;
import org.apache.commons.io.IOUtils;
import com.mojang.blaze3d.shaders.ProgramManager;
import com.google.gson.JsonObject;
import net.minecraft.server.ChainedJsonException;
import com.google.gson.JsonElement;
import com.google.gson.JsonArray;
import java.io.Reader;
import net.minecraft.util.GsonHelper;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import net.minecraft.resources.ResourceLocation;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.server.packs.resources.ResourceManager;
import com.mojang.blaze3d.shaders.Program;
import com.mojang.blaze3d.shaders.BlendMode;
import com.mojang.blaze3d.shaders.Uniform;
import java.util.List;
import java.util.function.IntSupplier;
import java.util.Map;
import com.mojang.blaze3d.shaders.AbstractUniform;
import org.apache.logging.log4j.Logger;
import com.mojang.blaze3d.shaders.Effect;

public class EffectInstance implements Effect, AutoCloseable {
    private static final Logger LOGGER;
    private static final AbstractUniform DUMMY_UNIFORM;
    private static EffectInstance lastAppliedEffect;
    private static int lastProgramId;
    private final Map<String, IntSupplier> samplerMap;
    private final List<String> samplerNames;
    private final List<Integer> samplerLocations;
    private final List<Uniform> uniforms;
    private final List<Integer> uniformLocations;
    private final Map<String, Uniform> uniformMap;
    private final int programId;
    private final String name;
    private boolean dirty;
    private final BlendMode blend;
    private final List<Integer> attributes;
    private final List<String> attributeNames;
    private final Program vertexProgram;
    private final Program fragmentProgram;
    
    public EffectInstance(final ResourceManager acf, final String string) throws IOException {
        this.samplerMap = (Map<String, IntSupplier>)Maps.newHashMap();
        this.samplerNames = (List<String>)Lists.newArrayList();
        this.samplerLocations = (List<Integer>)Lists.newArrayList();
        this.uniforms = (List<Uniform>)Lists.newArrayList();
        this.uniformLocations = (List<Integer>)Lists.newArrayList();
        this.uniformMap = (Map<String, Uniform>)Maps.newHashMap();
        final ResourceLocation vk4 = new ResourceLocation("shaders/program/" + string + ".json");
        this.name = string;
        Resource ace5 = null;
        try {
            ace5 = acf.getResource(vk4);
            final JsonObject jsonObject6 = GsonHelper.parse((Reader)new InputStreamReader(ace5.getInputStream(), StandardCharsets.UTF_8));
            final String string2 = GsonHelper.getAsString(jsonObject6, "vertex");
            final String string3 = GsonHelper.getAsString(jsonObject6, "fragment");
            final JsonArray jsonArray9 = GsonHelper.getAsJsonArray(jsonObject6, "samplers", (JsonArray)null);
            if (jsonArray9 != null) {
                int integer10 = 0;
                for (final JsonElement jsonElement12 : jsonArray9) {
                    try {
                        this.parseSamplerNode(jsonElement12);
                    }
                    catch (Exception exception13) {
                        final ChainedJsonException vn14 = ChainedJsonException.forException(exception13);
                        vn14.prependJsonKey(new StringBuilder().append("samplers[").append(integer10).append("]").toString());
                        throw vn14;
                    }
                    ++integer10;
                }
            }
            final JsonArray jsonArray10 = GsonHelper.getAsJsonArray(jsonObject6, "attributes", (JsonArray)null);
            if (jsonArray10 != null) {
                int integer11 = 0;
                this.attributes = (List<Integer>)Lists.newArrayListWithCapacity(jsonArray10.size());
                this.attributeNames = (List<String>)Lists.newArrayListWithCapacity(jsonArray10.size());
                for (final JsonElement jsonElement13 : jsonArray10) {
                    try {
                        this.attributeNames.add(GsonHelper.convertToString(jsonElement13, "attribute"));
                    }
                    catch (Exception exception14) {
                        final ChainedJsonException vn15 = ChainedJsonException.forException(exception14);
                        vn15.prependJsonKey(new StringBuilder().append("attributes[").append(integer11).append("]").toString());
                        throw vn15;
                    }
                    ++integer11;
                }
            }
            else {
                this.attributes = null;
                this.attributeNames = null;
            }
            final JsonArray jsonArray11 = GsonHelper.getAsJsonArray(jsonObject6, "uniforms", (JsonArray)null);
            if (jsonArray11 != null) {
                int integer12 = 0;
                for (final JsonElement jsonElement14 : jsonArray11) {
                    try {
                        this.parseUniformNode(jsonElement14);
                    }
                    catch (Exception exception15) {
                        final ChainedJsonException vn16 = ChainedJsonException.forException(exception15);
                        vn16.prependJsonKey(new StringBuilder().append("uniforms[").append(integer12).append("]").toString());
                        throw vn16;
                    }
                    ++integer12;
                }
            }
            this.blend = parseBlendNode(GsonHelper.getAsJsonObject(jsonObject6, "blend", (JsonObject)null));
            this.vertexProgram = getOrCreate(acf, Program.Type.VERTEX, string2);
            this.fragmentProgram = getOrCreate(acf, Program.Type.FRAGMENT, string3);
            this.programId = ProgramManager.createProgram();
            ProgramManager.linkProgram(this);
            this.updateLocations();
            if (this.attributeNames != null) {
                for (final String string4 : this.attributeNames) {
                    final int integer13 = Uniform.glGetAttribLocation(this.programId, (CharSequence)string4);
                    this.attributes.add(integer13);
                }
            }
        }
        catch (Exception exception16) {
            String string3;
            if (ace5 != null) {
                string3 = " (" + ace5.getSourceName() + ")";
            }
            else {
                string3 = "";
            }
            final ChainedJsonException vn17 = ChainedJsonException.forException(exception16);
            vn17.setFilenameAndFlush(vk4.getPath() + string3);
            throw vn17;
        }
        finally {
            IOUtils.closeQuietly((Closeable)ace5);
        }
        this.markDirty();
    }
    
    public static Program getOrCreate(final ResourceManager acf, final Program.Type a, final String string) throws IOException {
        Program dfb4 = (Program)a.getPrograms().get(string);
        if (dfb4 == null) {
            final ResourceLocation vk5 = new ResourceLocation("shaders/program/" + string + a.getExtension());
            final Resource ace6 = acf.getResource(vk5);
            try {
                dfb4 = Program.compileShader(a, string, ace6.getInputStream(), ace6.getSourceName());
            }
            finally {
                IOUtils.closeQuietly((Closeable)ace6);
            }
        }
        return dfb4;
    }
    
    public static BlendMode parseBlendNode(final JsonObject jsonObject) {
        if (jsonObject == null) {
            return new BlendMode();
        }
        int integer2 = 32774;
        int integer3 = 1;
        int integer4 = 0;
        int integer5 = 1;
        int integer6 = 0;
        boolean boolean7 = true;
        boolean boolean8 = false;
        if (GsonHelper.isStringValue(jsonObject, "func")) {
            integer2 = BlendMode.stringToBlendFunc(jsonObject.get("func").getAsString());
            if (integer2 != 32774) {
                boolean7 = false;
            }
        }
        if (GsonHelper.isStringValue(jsonObject, "srcrgb")) {
            integer3 = BlendMode.stringToBlendFactor(jsonObject.get("srcrgb").getAsString());
            if (integer3 != 1) {
                boolean7 = false;
            }
        }
        if (GsonHelper.isStringValue(jsonObject, "dstrgb")) {
            integer4 = BlendMode.stringToBlendFactor(jsonObject.get("dstrgb").getAsString());
            if (integer4 != 0) {
                boolean7 = false;
            }
        }
        if (GsonHelper.isStringValue(jsonObject, "srcalpha")) {
            integer5 = BlendMode.stringToBlendFactor(jsonObject.get("srcalpha").getAsString());
            if (integer5 != 1) {
                boolean7 = false;
            }
            boolean8 = true;
        }
        if (GsonHelper.isStringValue(jsonObject, "dstalpha")) {
            integer6 = BlendMode.stringToBlendFactor(jsonObject.get("dstalpha").getAsString());
            if (integer6 != 0) {
                boolean7 = false;
            }
            boolean8 = true;
        }
        if (boolean7) {
            return new BlendMode();
        }
        if (boolean8) {
            return new BlendMode(integer3, integer4, integer5, integer6, integer2);
        }
        return new BlendMode(integer3, integer4, integer2);
    }
    
    public void close() {
        for (final Uniform dfd3 : this.uniforms) {
            dfd3.close();
        }
        ProgramManager.releaseProgram(this);
    }
    
    public void clear() {
        RenderSystem.assertThread((Supplier<Boolean>)RenderSystem::isOnRenderThread);
        ProgramManager.glUseProgram(0);
        EffectInstance.lastProgramId = -1;
        EffectInstance.lastAppliedEffect = null;
        for (int integer2 = 0; integer2 < this.samplerLocations.size(); ++integer2) {
            if (this.samplerMap.get(this.samplerNames.get(integer2)) != null) {
                GlStateManager._activeTexture(33984 + integer2);
                GlStateManager._disableTexture();
                GlStateManager._bindTexture(0);
            }
        }
    }
    
    public void apply() {
        RenderSystem.assertThread((Supplier<Boolean>)RenderSystem::isOnGameThread);
        this.dirty = false;
        EffectInstance.lastAppliedEffect = this;
        this.blend.apply();
        if (this.programId != EffectInstance.lastProgramId) {
            ProgramManager.glUseProgram(this.programId);
            EffectInstance.lastProgramId = this.programId;
        }
        for (int integer2 = 0; integer2 < this.samplerLocations.size(); ++integer2) {
            final String string3 = (String)this.samplerNames.get(integer2);
            final IntSupplier intSupplier4 = (IntSupplier)this.samplerMap.get(string3);
            if (intSupplier4 != null) {
                RenderSystem.activeTexture(33984 + integer2);
                RenderSystem.enableTexture();
                final int integer3 = intSupplier4.getAsInt();
                if (integer3 != -1) {
                    RenderSystem.bindTexture(integer3);
                    Uniform.uploadInteger((int)this.samplerLocations.get(integer2), integer2);
                }
            }
        }
        for (final Uniform dfd3 : this.uniforms) {
            dfd3.upload();
        }
    }
    
    public void markDirty() {
        this.dirty = true;
    }
    
    @Nullable
    public Uniform getUniform(final String string) {
        RenderSystem.assertThread((Supplier<Boolean>)RenderSystem::isOnRenderThread);
        return (Uniform)this.uniformMap.get(string);
    }
    
    public AbstractUniform safeGetUniform(final String string) {
        RenderSystem.assertThread((Supplier<Boolean>)RenderSystem::isOnGameThread);
        final Uniform dfd3 = this.getUniform(string);
        return (dfd3 == null) ? EffectInstance.DUMMY_UNIFORM : dfd3;
    }
    
    private void updateLocations() {
        RenderSystem.assertThread((Supplier<Boolean>)RenderSystem::isOnRenderThread);
        final IntList intList2 = (IntList)new IntArrayList();
        for (int integer3 = 0; integer3 < this.samplerNames.size(); ++integer3) {
            final String string4 = (String)this.samplerNames.get(integer3);
            final int integer4 = Uniform.glGetUniformLocation(this.programId, (CharSequence)string4);
            if (integer4 == -1) {
                EffectInstance.LOGGER.warn("Shader {} could not find sampler named {} in the specified shader program.", this.name, string4);
                this.samplerMap.remove(string4);
                intList2.add(integer3);
            }
            else {
                this.samplerLocations.add(integer4);
            }
        }
        for (int integer3 = intList2.size() - 1; integer3 >= 0; --integer3) {
            this.samplerNames.remove(intList2.getInt(integer3));
        }
        for (final Uniform dfd4 : this.uniforms) {
            final String string5 = dfd4.getName();
            final int integer5 = Uniform.glGetUniformLocation(this.programId, (CharSequence)string5);
            if (integer5 == -1) {
                EffectInstance.LOGGER.warn("Could not find uniform named {} in the specified shader program.", string5);
            }
            else {
                this.uniformLocations.add(integer5);
                dfd4.setLocation(integer5);
                this.uniformMap.put(string5, dfd4);
            }
        }
    }
    
    private void parseSamplerNode(final JsonElement jsonElement) {
        final JsonObject jsonObject3 = GsonHelper.convertToJsonObject(jsonElement, "sampler");
        final String string4 = GsonHelper.getAsString(jsonObject3, "name");
        if (!GsonHelper.isStringValue(jsonObject3, "file")) {
            this.samplerMap.put(string4, null);
            this.samplerNames.add(string4);
            return;
        }
        this.samplerNames.add(string4);
    }
    
    public void setSampler(final String string, final IntSupplier intSupplier) {
        if (this.samplerMap.containsKey(string)) {
            this.samplerMap.remove(string);
        }
        this.samplerMap.put(string, intSupplier);
        this.markDirty();
    }
    
    private void parseUniformNode(final JsonElement jsonElement) throws ChainedJsonException {
        final JsonObject jsonObject3 = GsonHelper.convertToJsonObject(jsonElement, "uniform");
        final String string4 = GsonHelper.getAsString(jsonObject3, "name");
        final int integer5 = Uniform.getTypeFromString(GsonHelper.getAsString(jsonObject3, "type"));
        final int integer6 = GsonHelper.getAsInt(jsonObject3, "count");
        final float[] arr7 = new float[Math.max(integer6, 16)];
        final JsonArray jsonArray8 = GsonHelper.getAsJsonArray(jsonObject3, "values");
        if (jsonArray8.size() != integer6 && jsonArray8.size() > 1) {
            throw new ChainedJsonException(new StringBuilder().append("Invalid amount of values specified (expected ").append(integer6).append(", found ").append(jsonArray8.size()).append(")").toString());
        }
        int integer7 = 0;
        for (final JsonElement jsonElement2 : jsonArray8) {
            try {
                arr7[integer7] = GsonHelper.convertToFloat(jsonElement2, "value");
            }
            catch (Exception exception12) {
                final ChainedJsonException vn13 = ChainedJsonException.forException(exception12);
                vn13.prependJsonKey(new StringBuilder().append("values[").append(integer7).append("]").toString());
                throw vn13;
            }
            ++integer7;
        }
        if (integer6 > 1 && jsonArray8.size() == 1) {
            while (integer7 < integer6) {
                arr7[integer7] = arr7[0];
                ++integer7;
            }
        }
        final int integer8 = (integer6 > 1 && integer6 <= 4 && integer5 < 8) ? (integer6 - 1) : 0;
        final Uniform dfd11 = new Uniform(string4, integer5 + integer8, integer6, this);
        if (integer5 <= 3) {
            dfd11.setSafe((int)arr7[0], (int)arr7[1], (int)arr7[2], (int)arr7[3]);
        }
        else if (integer5 <= 7) {
            dfd11.setSafe(arr7[0], arr7[1], arr7[2], arr7[3]);
        }
        else {
            dfd11.set(arr7);
        }
        this.uniforms.add(dfd11);
    }
    
    public Program getVertexProgram() {
        return this.vertexProgram;
    }
    
    public Program getFragmentProgram() {
        return this.fragmentProgram;
    }
    
    public int getId() {
        return this.programId;
    }
    
    static {
        LOGGER = LogManager.getLogger();
        DUMMY_UNIFORM = new AbstractUniform();
        EffectInstance.lastProgramId = -1;
    }
}

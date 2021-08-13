package net.minecraft.client.renderer;

import net.minecraft.client.Minecraft;
import com.mojang.blaze3d.shaders.Uniform;
import net.minecraft.client.renderer.texture.AbstractTexture;
import com.mojang.blaze3d.systems.RenderSystem;
import java.io.FileNotFoundException;
import java.util.Iterator;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.server.packs.resources.Resource;
import java.io.Closeable;
import org.apache.commons.io.IOUtils;
import net.minecraft.server.ChainedJsonException;
import com.google.gson.JsonElement;
import java.io.Reader;
import net.minecraft.util.GsonHelper;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import com.google.gson.JsonSyntaxException;
import java.io.IOException;
import com.google.common.collect.Maps;
import com.google.common.collect.Lists;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.renderer.texture.TextureManager;
import com.mojang.math.Matrix4f;
import java.util.Map;
import java.util.List;
import net.minecraft.server.packs.resources.ResourceManager;
import com.mojang.blaze3d.pipeline.RenderTarget;

public class PostChain implements AutoCloseable {
    private final RenderTarget screenTarget;
    private final ResourceManager resourceManager;
    private final String name;
    private final List<PostPass> passes;
    private final Map<String, RenderTarget> customRenderTargets;
    private final List<RenderTarget> fullSizedTargets;
    private Matrix4f shaderOrthoMatrix;
    private int screenWidth;
    private int screenHeight;
    private float time;
    private float lastStamp;
    
    public PostChain(final TextureManager ejv, final ResourceManager acf, final RenderTarget ded, final ResourceLocation vk) throws IOException, JsonSyntaxException {
        this.passes = (List<PostPass>)Lists.newArrayList();
        this.customRenderTargets = (Map<String, RenderTarget>)Maps.newHashMap();
        this.fullSizedTargets = (List<RenderTarget>)Lists.newArrayList();
        this.resourceManager = acf;
        this.screenTarget = ded;
        this.time = 0.0f;
        this.lastStamp = 0.0f;
        this.screenWidth = ded.viewWidth;
        this.screenHeight = ded.viewHeight;
        this.name = vk.toString();
        this.updateOrthoMatrix();
        this.load(ejv, vk);
    }
    
    private void load(final TextureManager ejv, final ResourceLocation vk) throws IOException, JsonSyntaxException {
        Resource ace4 = null;
        try {
            ace4 = this.resourceManager.getResource(vk);
            final JsonObject jsonObject5 = GsonHelper.parse((Reader)new InputStreamReader(ace4.getInputStream(), StandardCharsets.UTF_8));
            if (GsonHelper.isArrayNode(jsonObject5, "targets")) {
                final JsonArray jsonArray6 = jsonObject5.getAsJsonArray("targets");
                int integer7 = 0;
                for (final JsonElement jsonElement9 : jsonArray6) {
                    try {
                        this.parseTargetNode(jsonElement9);
                    }
                    catch (Exception exception10) {
                        final ChainedJsonException vn11 = ChainedJsonException.forException(exception10);
                        vn11.prependJsonKey(new StringBuilder().append("targets[").append(integer7).append("]").toString());
                        throw vn11;
                    }
                    ++integer7;
                }
            }
            if (GsonHelper.isArrayNode(jsonObject5, "passes")) {
                final JsonArray jsonArray6 = jsonObject5.getAsJsonArray("passes");
                int integer7 = 0;
                for (final JsonElement jsonElement9 : jsonArray6) {
                    try {
                        this.parsePassNode(ejv, jsonElement9);
                    }
                    catch (Exception exception10) {
                        final ChainedJsonException vn11 = ChainedJsonException.forException(exception10);
                        vn11.prependJsonKey(new StringBuilder().append("passes[").append(integer7).append("]").toString());
                        throw vn11;
                    }
                    ++integer7;
                }
            }
        }
        catch (Exception exception11) {
            String string6;
            if (ace4 != null) {
                string6 = " (" + ace4.getSourceName() + ")";
            }
            else {
                string6 = "";
            }
            final ChainedJsonException vn12 = ChainedJsonException.forException(exception11);
            vn12.setFilenameAndFlush(vk.getPath() + string6);
            throw vn12;
        }
        finally {
            IOUtils.closeQuietly((Closeable)ace4);
        }
    }
    
    private void parseTargetNode(final JsonElement jsonElement) throws ChainedJsonException {
        if (GsonHelper.isStringValue(jsonElement)) {
            this.addTempTarget(jsonElement.getAsString(), this.screenWidth, this.screenHeight);
        }
        else {
            final JsonObject jsonObject3 = GsonHelper.convertToJsonObject(jsonElement, "target");
            final String string4 = GsonHelper.getAsString(jsonObject3, "name");
            final int integer5 = GsonHelper.getAsInt(jsonObject3, "width", this.screenWidth);
            final int integer6 = GsonHelper.getAsInt(jsonObject3, "height", this.screenHeight);
            if (this.customRenderTargets.containsKey(string4)) {
                throw new ChainedJsonException(string4 + " is already defined");
            }
            this.addTempTarget(string4, integer5, integer6);
        }
    }
    
    private void parsePassNode(final TextureManager ejv, final JsonElement jsonElement) throws IOException {
        final JsonObject jsonObject4 = GsonHelper.convertToJsonObject(jsonElement, "pass");
        final String string5 = GsonHelper.getAsString(jsonObject4, "name");
        final String string6 = GsonHelper.getAsString(jsonObject4, "intarget");
        final String string7 = GsonHelper.getAsString(jsonObject4, "outtarget");
        final RenderTarget ded8 = this.getRenderTarget(string6);
        final RenderTarget ded9 = this.getRenderTarget(string7);
        if (ded8 == null) {
            throw new ChainedJsonException("Input target '" + string6 + "' does not exist");
        }
        if (ded9 == null) {
            throw new ChainedJsonException("Output target '" + string7 + "' does not exist");
        }
        final PostPass eac10 = this.addPass(string5, ded8, ded9);
        final JsonArray jsonArray11 = GsonHelper.getAsJsonArray(jsonObject4, "auxtargets", (JsonArray)null);
        if (jsonArray11 != null) {
            int integer12 = 0;
            for (final JsonElement jsonElement2 : jsonArray11) {
                try {
                    final JsonObject jsonObject5 = GsonHelper.convertToJsonObject(jsonElement2, "auxtarget");
                    final String string8 = GsonHelper.getAsString(jsonObject5, "name");
                    final String string9 = GsonHelper.getAsString(jsonObject5, "id");
                    boolean boolean18;
                    String string10;
                    if (string9.endsWith(":depth")) {
                        boolean18 = true;
                        string10 = string9.substring(0, string9.lastIndexOf(58));
                    }
                    else {
                        boolean18 = false;
                        string10 = string9;
                    }
                    final RenderTarget ded10 = this.getRenderTarget(string10);
                    if (ded10 == null) {
                        if (boolean18) {
                            throw new ChainedJsonException("Render target '" + string10 + "' can't be used as depth buffer");
                        }
                        final ResourceLocation vk21 = new ResourceLocation("textures/effect/" + string10 + ".png");
                        Resource ace22 = null;
                        try {
                            ace22 = this.resourceManager.getResource(vk21);
                        }
                        catch (FileNotFoundException fileNotFoundException23) {
                            throw new ChainedJsonException("Render target or texture '" + string10 + "' does not exist");
                        }
                        finally {
                            IOUtils.closeQuietly((Closeable)ace22);
                        }
                        ejv.bind(vk21);
                        final AbstractTexture eji23 = ejv.getTexture(vk21);
                        final int integer13 = GsonHelper.getAsInt(jsonObject5, "width");
                        final int integer14 = GsonHelper.getAsInt(jsonObject5, "height");
                        final boolean boolean19 = GsonHelper.getAsBoolean(jsonObject5, "bilinear");
                        if (boolean19) {
                            RenderSystem.texParameter(3553, 10241, 9729);
                            RenderSystem.texParameter(3553, 10240, 9729);
                        }
                        else {
                            RenderSystem.texParameter(3553, 10241, 9728);
                            RenderSystem.texParameter(3553, 10240, 9728);
                        }
                        eac10.addAuxAsset(string8, eji23::getId, integer13, integer14);
                    }
                    else if (boolean18) {
                        eac10.addAuxAsset(string8, ded10::getDepthTextureId, ded10.width, ded10.height);
                    }
                    else {
                        eac10.addAuxAsset(string8, ded10::getColorTextureId, ded10.width, ded10.height);
                    }
                }
                catch (Exception exception15) {
                    final ChainedJsonException vn16 = ChainedJsonException.forException(exception15);
                    vn16.prependJsonKey(new StringBuilder().append("auxtargets[").append(integer12).append("]").toString());
                    throw vn16;
                }
                ++integer12;
            }
        }
        final JsonArray jsonArray12 = GsonHelper.getAsJsonArray(jsonObject4, "uniforms", (JsonArray)null);
        if (jsonArray12 != null) {
            int integer15 = 0;
            for (final JsonElement jsonElement3 : jsonArray12) {
                try {
                    this.parseUniformNode(jsonElement3);
                }
                catch (Exception exception16) {
                    final ChainedJsonException vn17 = ChainedJsonException.forException(exception16);
                    vn17.prependJsonKey(new StringBuilder().append("uniforms[").append(integer15).append("]").toString());
                    throw vn17;
                }
                ++integer15;
            }
        }
    }
    
    private void parseUniformNode(final JsonElement jsonElement) throws ChainedJsonException {
        final JsonObject jsonObject3 = GsonHelper.convertToJsonObject(jsonElement, "uniform");
        final String string4 = GsonHelper.getAsString(jsonObject3, "name");
        final Uniform dfd5 = ((PostPass)this.passes.get(this.passes.size() - 1)).getEffect().getUniform(string4);
        if (dfd5 == null) {
            throw new ChainedJsonException("Uniform '" + string4 + "' does not exist");
        }
        final float[] arr6 = new float[4];
        int integer7 = 0;
        final JsonArray jsonArray8 = GsonHelper.getAsJsonArray(jsonObject3, "values");
        for (final JsonElement jsonElement2 : jsonArray8) {
            try {
                arr6[integer7] = GsonHelper.convertToFloat(jsonElement2, "value");
            }
            catch (Exception exception11) {
                final ChainedJsonException vn12 = ChainedJsonException.forException(exception11);
                vn12.prependJsonKey(new StringBuilder().append("values[").append(integer7).append("]").toString());
                throw vn12;
            }
            ++integer7;
        }
        switch (integer7) {
            case 1: {
                dfd5.set(arr6[0]);
                break;
            }
            case 2: {
                dfd5.set(arr6[0], arr6[1]);
                break;
            }
            case 3: {
                dfd5.set(arr6[0], arr6[1], arr6[2]);
                break;
            }
            case 4: {
                dfd5.set(arr6[0], arr6[1], arr6[2], arr6[3]);
                break;
            }
        }
    }
    
    public RenderTarget getTempTarget(final String string) {
        return (RenderTarget)this.customRenderTargets.get(string);
    }
    
    public void addTempTarget(final String string, final int integer2, final int integer3) {
        final RenderTarget ded5 = new RenderTarget(integer2, integer3, true, Minecraft.ON_OSX);
        ded5.setClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        this.customRenderTargets.put(string, ded5);
        if (integer2 == this.screenWidth && integer3 == this.screenHeight) {
            this.fullSizedTargets.add(ded5);
        }
    }
    
    public void close() {
        for (final RenderTarget ded3 : this.customRenderTargets.values()) {
            ded3.destroyBuffers();
        }
        for (final PostPass eac3 : this.passes) {
            eac3.close();
        }
        this.passes.clear();
    }
    
    public PostPass addPass(final String string, final RenderTarget ded2, final RenderTarget ded3) throws IOException {
        final PostPass eac5 = new PostPass(this.resourceManager, string, ded2, ded3);
        this.passes.add(this.passes.size(), eac5);
        return eac5;
    }
    
    private void updateOrthoMatrix() {
        this.shaderOrthoMatrix = Matrix4f.orthographic((float)this.screenTarget.width, (float)this.screenTarget.height, 0.1f, 1000.0f);
    }
    
    public void resize(final int integer1, final int integer2) {
        this.screenWidth = this.screenTarget.width;
        this.screenHeight = this.screenTarget.height;
        this.updateOrthoMatrix();
        for (final PostPass eac5 : this.passes) {
            eac5.setOrthoMatrix(this.shaderOrthoMatrix);
        }
        for (final RenderTarget ded5 : this.fullSizedTargets) {
            ded5.resize(integer1, integer2, Minecraft.ON_OSX);
        }
    }
    
    public void process(final float float1) {
        if (float1 < this.lastStamp) {
            this.time += 1.0f - this.lastStamp;
            this.time += float1;
        }
        else {
            this.time += float1 - this.lastStamp;
        }
        this.lastStamp = float1;
        while (this.time > 20.0f) {
            this.time -= 20.0f;
        }
        for (final PostPass eac4 : this.passes) {
            eac4.process(this.time / 20.0f);
        }
    }
    
    public final String getName() {
        return this.name;
    }
    
    private RenderTarget getRenderTarget(final String string) {
        if (string == null) {
            return null;
        }
        if (string.equals("minecraft:main")) {
            return this.screenTarget;
        }
        return (RenderTarget)this.customRenderTargets.get(string);
    }
}

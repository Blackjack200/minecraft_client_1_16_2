package net.minecraft.client.renderer.texture;

import java.util.concurrent.Executor;
import net.minecraft.resources.ResourceLocation;
import java.io.IOException;
import net.minecraft.server.packs.resources.ResourceManager;
import com.mojang.blaze3d.platform.TextureUtil;
import com.mojang.blaze3d.platform.GlStateManager;
import java.util.function.Supplier;
import com.mojang.blaze3d.systems.RenderSystem;

public abstract class AbstractTexture implements AutoCloseable {
    protected int id;
    protected boolean blur;
    protected boolean mipmap;
    
    public AbstractTexture() {
        this.id = -1;
    }
    
    public void setFilter(final boolean boolean1, final boolean boolean2) {
        RenderSystem.assertThread((Supplier<Boolean>)RenderSystem::isOnRenderThreadOrInit);
        this.blur = boolean1;
        this.mipmap = boolean2;
        int integer4;
        int integer5;
        if (boolean1) {
            integer4 = (boolean2 ? 9987 : 9729);
            integer5 = 9729;
        }
        else {
            integer4 = (boolean2 ? 9986 : 9728);
            integer5 = 9728;
        }
        GlStateManager._texParameter(3553, 10241, integer4);
        GlStateManager._texParameter(3553, 10240, integer5);
    }
    
    public int getId() {
        RenderSystem.assertThread((Supplier<Boolean>)RenderSystem::isOnRenderThreadOrInit);
        if (this.id == -1) {
            this.id = TextureUtil.generateTextureId();
        }
        return this.id;
    }
    
    public void releaseId() {
        if (!RenderSystem.isOnRenderThread()) {
            RenderSystem.recordRenderCall(() -> {
                if (this.id != -1) {
                    TextureUtil.releaseTextureId(this.id);
                    this.id = -1;
                }
            });
        }
        else if (this.id != -1) {
            TextureUtil.releaseTextureId(this.id);
            this.id = -1;
        }
    }
    
    public abstract void load(final ResourceManager acf) throws IOException;
    
    public void bind() {
        if (!RenderSystem.isOnRenderThreadOrInit()) {
            RenderSystem.recordRenderCall(() -> GlStateManager._bindTexture(this.getId()));
        }
        else {
            GlStateManager._bindTexture(this.getId());
        }
    }
    
    public void reset(final TextureManager ejv, final ResourceManager acf, final ResourceLocation vk, final Executor executor) {
        ejv.register(vk, this);
    }
    
    public void close() {
    }
}

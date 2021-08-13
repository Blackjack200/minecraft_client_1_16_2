package net.minecraft.client.model.geom;

import net.minecraft.core.Direction;
import java.util.Random;
import com.mojang.math.Matrix3f;
import com.mojang.math.Matrix4f;
import com.mojang.math.Vector4f;
import com.mojang.math.Vector3f;
import it.unimi.dsi.fastutil.objects.ObjectListIterator;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.PoseStack;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.client.model.Model;
import it.unimi.dsi.fastutil.objects.ObjectList;

public class ModelPart {
    private float xTexSize;
    private float yTexSize;
    private int xTexOffs;
    private int yTexOffs;
    public float x;
    public float y;
    public float z;
    public float xRot;
    public float yRot;
    public float zRot;
    public boolean mirror;
    public boolean visible;
    private final ObjectList<Cube> cubes;
    private final ObjectList<ModelPart> children;
    
    public ModelPart(final Model dun) {
        this.xTexSize = 64.0f;
        this.yTexSize = 32.0f;
        this.visible = true;
        this.cubes = (ObjectList<Cube>)new ObjectArrayList();
        this.children = (ObjectList<ModelPart>)new ObjectArrayList();
        dun.accept(this);
        this.setTexSize(dun.texWidth, dun.texHeight);
    }
    
    public ModelPart(final Model dun, final int integer2, final int integer3) {
        this(dun.texWidth, dun.texHeight, integer2, integer3);
        dun.accept(this);
    }
    
    public ModelPart(final int integer1, final int integer2, final int integer3, final int integer4) {
        this.xTexSize = 64.0f;
        this.yTexSize = 32.0f;
        this.visible = true;
        this.cubes = (ObjectList<Cube>)new ObjectArrayList();
        this.children = (ObjectList<ModelPart>)new ObjectArrayList();
        this.setTexSize(integer1, integer2);
        this.texOffs(integer3, integer4);
    }
    
    private ModelPart() {
        this.xTexSize = 64.0f;
        this.yTexSize = 32.0f;
        this.visible = true;
        this.cubes = (ObjectList<Cube>)new ObjectArrayList();
        this.children = (ObjectList<ModelPart>)new ObjectArrayList();
    }
    
    public ModelPart createShallowCopy() {
        final ModelPart dwf2 = new ModelPart();
        dwf2.copyFrom(this);
        return dwf2;
    }
    
    public void copyFrom(final ModelPart dwf) {
        this.xRot = dwf.xRot;
        this.yRot = dwf.yRot;
        this.zRot = dwf.zRot;
        this.x = dwf.x;
        this.y = dwf.y;
        this.z = dwf.z;
    }
    
    public void addChild(final ModelPart dwf) {
        this.children.add(dwf);
    }
    
    public ModelPart texOffs(final int integer1, final int integer2) {
        this.xTexOffs = integer1;
        this.yTexOffs = integer2;
        return this;
    }
    
    public ModelPart addBox(final String string, final float float2, final float float3, final float float4, final int integer5, final int integer6, final int integer7, final float float8, final int integer9, final int integer10) {
        this.texOffs(integer9, integer10);
        this.addBox(this.xTexOffs, this.yTexOffs, float2, float3, float4, (float)integer5, (float)integer6, (float)integer7, float8, float8, float8, this.mirror, false);
        return this;
    }
    
    public ModelPart addBox(final float float1, final float float2, final float float3, final float float4, final float float5, final float float6) {
        this.addBox(this.xTexOffs, this.yTexOffs, float1, float2, float3, float4, float5, float6, 0.0f, 0.0f, 0.0f, this.mirror, false);
        return this;
    }
    
    public ModelPart addBox(final float float1, final float float2, final float float3, final float float4, final float float5, final float float6, final boolean boolean7) {
        this.addBox(this.xTexOffs, this.yTexOffs, float1, float2, float3, float4, float5, float6, 0.0f, 0.0f, 0.0f, boolean7, false);
        return this;
    }
    
    public void addBox(final float float1, final float float2, final float float3, final float float4, final float float5, final float float6, final float float7) {
        this.addBox(this.xTexOffs, this.yTexOffs, float1, float2, float3, float4, float5, float6, float7, float7, float7, this.mirror, false);
    }
    
    public void addBox(final float float1, final float float2, final float float3, final float float4, final float float5, final float float6, final float float7, final float float8, final float float9) {
        this.addBox(this.xTexOffs, this.yTexOffs, float1, float2, float3, float4, float5, float6, float7, float8, float9, this.mirror, false);
    }
    
    public void addBox(final float float1, final float float2, final float float3, final float float4, final float float5, final float float6, final float float7, final boolean boolean8) {
        this.addBox(this.xTexOffs, this.yTexOffs, float1, float2, float3, float4, float5, float6, float7, float7, float7, boolean8, false);
    }
    
    private void addBox(final int integer1, final int integer2, final float float3, final float float4, final float float5, final float float6, final float float7, final float float8, final float float9, final float float10, final float float11, final boolean boolean12, final boolean boolean13) {
        this.cubes.add(new Cube(integer1, integer2, float3, float4, float5, float6, float7, float8, float9, float10, float11, boolean12, this.xTexSize, this.yTexSize));
    }
    
    public void setPos(final float float1, final float float2, final float float3) {
        this.x = float1;
        this.y = float2;
        this.z = float3;
    }
    
    public void render(final PoseStack dfj, final VertexConsumer dfn, final int integer3, final int integer4) {
        this.render(dfj, dfn, integer3, integer4, 1.0f, 1.0f, 1.0f, 1.0f);
    }
    
    public void render(final PoseStack dfj, final VertexConsumer dfn, final int integer3, final int integer4, final float float5, final float float6, final float float7, final float float8) {
        if (!this.visible) {
            return;
        }
        if (this.cubes.isEmpty() && this.children.isEmpty()) {
            return;
        }
        dfj.pushPose();
        this.translateAndRotate(dfj);
        this.compile(dfj.last(), dfn, integer3, integer4, float5, float6, float7, float8);
        for (final ModelPart dwf11 : this.children) {
            dwf11.render(dfj, dfn, integer3, integer4, float5, float6, float7, float8);
        }
        dfj.popPose();
    }
    
    public void translateAndRotate(final PoseStack dfj) {
        dfj.translate(this.x / 16.0f, this.y / 16.0f, this.z / 16.0f);
        if (this.zRot != 0.0f) {
            dfj.mulPose(Vector3f.ZP.rotation(this.zRot));
        }
        if (this.yRot != 0.0f) {
            dfj.mulPose(Vector3f.YP.rotation(this.yRot));
        }
        if (this.xRot != 0.0f) {
            dfj.mulPose(Vector3f.XP.rotation(this.xRot));
        }
    }
    
    private void compile(final PoseStack.Pose a, final VertexConsumer dfn, final int integer3, final int integer4, final float float5, final float float6, final float float7, final float float8) {
        final Matrix4f b10 = a.pose();
        final Matrix3f a2 = a.normal();
        for (final Cube a3 : this.cubes) {
            for (final Polygon b11 : a3.polygons) {
                final Vector3f g18 = b11.normal.copy();
                g18.transform(a2);
                final float float9 = g18.x();
                final float float10 = g18.y();
                final float float11 = g18.z();
                for (int integer5 = 0; integer5 < 4; ++integer5) {
                    final Vertex c23 = b11.vertices[integer5];
                    final float float12 = c23.pos.x() / 16.0f;
                    final float float13 = c23.pos.y() / 16.0f;
                    final float float14 = c23.pos.z() / 16.0f;
                    final Vector4f h27 = new Vector4f(float12, float13, float14, 1.0f);
                    h27.transform(b10);
                    dfn.vertex(h27.x(), h27.y(), h27.z(), float5, float6, float7, float8, c23.u, c23.v, integer4, integer3, float9, float10, float11);
                }
            }
        }
    }
    
    public ModelPart setTexSize(final int integer1, final int integer2) {
        this.xTexSize = (float)integer1;
        this.yTexSize = (float)integer2;
        return this;
    }
    
    public Cube getRandomCube(final Random random) {
        return (Cube)this.cubes.get(random.nextInt(this.cubes.size()));
    }
    
    public static class Cube {
        private final Polygon[] polygons;
        public final float minX;
        public final float minY;
        public final float minZ;
        public final float maxX;
        public final float maxY;
        public final float maxZ;
        
        public Cube(final int integer1, final int integer2, float float3, float float4, float float5, final float float6, final float float7, final float float8, final float float9, final float float10, final float float11, final boolean boolean12, final float float13, final float float14) {
            this.minX = float3;
            this.minY = float4;
            this.minZ = float5;
            this.maxX = float3 + float6;
            this.maxY = float4 + float7;
            this.maxZ = float5 + float8;
            this.polygons = new Polygon[6];
            float float15 = float3 + float6;
            float float16 = float4 + float7;
            float float17 = float5 + float8;
            float3 -= float9;
            float4 -= float10;
            float5 -= float11;
            float15 += float9;
            float16 += float10;
            float17 += float11;
            if (boolean12) {
                final float float18 = float15;
                float15 = float3;
                float3 = float18;
            }
            final Vertex c19 = new Vertex(float3, float4, float5, 0.0f, 0.0f);
            final Vertex c20 = new Vertex(float15, float4, float5, 0.0f, 8.0f);
            final Vertex c21 = new Vertex(float15, float16, float5, 8.0f, 8.0f);
            final Vertex c22 = new Vertex(float3, float16, float5, 8.0f, 0.0f);
            final Vertex c23 = new Vertex(float3, float4, float17, 0.0f, 0.0f);
            final Vertex c24 = new Vertex(float15, float4, float17, 0.0f, 8.0f);
            final Vertex c25 = new Vertex(float15, float16, float17, 8.0f, 8.0f);
            final Vertex c26 = new Vertex(float3, float16, float17, 8.0f, 0.0f);
            final float float19 = (float)integer1;
            final float float20 = integer1 + float8;
            final float float21 = integer1 + float8 + float6;
            final float float22 = integer1 + float8 + float6 + float6;
            final float float23 = integer1 + float8 + float6 + float8;
            final float float24 = integer1 + float8 + float6 + float8 + float6;
            final float float25 = (float)integer2;
            final float float26 = integer2 + float8;
            final float float27 = integer2 + float8 + float7;
            this.polygons[2] = new Polygon(new Vertex[] { c24, c23, c19, c20 }, float20, float25, float21, float26, float13, float14, boolean12, Direction.DOWN);
            this.polygons[3] = new Polygon(new Vertex[] { c21, c22, c26, c25 }, float21, float26, float22, float25, float13, float14, boolean12, Direction.UP);
            this.polygons[1] = new Polygon(new Vertex[] { c19, c23, c26, c22 }, float19, float26, float20, float27, float13, float14, boolean12, Direction.WEST);
            this.polygons[4] = new Polygon(new Vertex[] { c20, c19, c22, c21 }, float20, float26, float21, float27, float13, float14, boolean12, Direction.NORTH);
            this.polygons[0] = new Polygon(new Vertex[] { c24, c20, c21, c25 }, float21, float26, float23, float27, float13, float14, boolean12, Direction.EAST);
            this.polygons[5] = new Polygon(new Vertex[] { c23, c24, c25, c26 }, float23, float26, float24, float27, float13, float14, boolean12, Direction.SOUTH);
        }
    }
    
    static class Polygon {
        public final Vertex[] vertices;
        public final Vector3f normal;
        
        public Polygon(final Vertex[] arr, final float float2, final float float3, final float float4, final float float5, final float float6, final float float7, final boolean boolean8, final Direction gc) {
            this.vertices = arr;
            final float float8 = 0.0f / float6;
            final float float9 = 0.0f / float7;
            arr[0] = arr[0].remap(float4 / float6 - float8, float3 / float7 + float9);
            arr[1] = arr[1].remap(float2 / float6 + float8, float3 / float7 + float9);
            arr[2] = arr[2].remap(float2 / float6 + float8, float5 / float7 - float9);
            arr[3] = arr[3].remap(float4 / float6 - float8, float5 / float7 - float9);
            if (boolean8) {
                for (int integer13 = arr.length, integer14 = 0; integer14 < integer13 / 2; ++integer14) {
                    final Vertex c15 = arr[integer14];
                    arr[integer14] = arr[integer13 - 1 - integer14];
                    arr[integer13 - 1 - integer14] = c15;
                }
            }
            this.normal = gc.step();
            if (boolean8) {
                this.normal.mul(-1.0f, 1.0f, 1.0f);
            }
        }
    }
    
    static class Vertex {
        public final Vector3f pos;
        public final float u;
        public final float v;
        
        public Vertex(final float float1, final float float2, final float float3, final float float4, final float float5) {
            this(new Vector3f(float1, float2, float3), float4, float5);
        }
        
        public Vertex remap(final float float1, final float float2) {
            return new Vertex(this.pos, float1, float2);
        }
        
        public Vertex(final Vector3f g, final float float2, final float float3) {
            this.pos = g;
            this.u = float2;
            this.v = float3;
        }
    }
}

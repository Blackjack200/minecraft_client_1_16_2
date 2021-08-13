package net.minecraft.world.entity.ai.control;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.entity.Mob;

public class LookControl {
    protected final Mob mob;
    protected float yMaxRotSpeed;
    protected float xMaxRotAngle;
    protected boolean hasWanted;
    protected double wantedX;
    protected double wantedY;
    protected double wantedZ;
    
    public LookControl(final Mob aqk) {
        this.mob = aqk;
    }
    
    public void setLookAt(final Vec3 dck) {
        this.setLookAt(dck.x, dck.y, dck.z);
    }
    
    public void setLookAt(final Entity apx, final float float2, final float float3) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: aload_1         /* apx */
        //     2: invokevirtual   net/minecraft/world/entity/Entity.getX:()D
        //     5: aload_1         /* apx */
        //     6: invokestatic    net/minecraft/world/entity/ai/control/LookControl.getWantedY:(Lnet/minecraft/world/entity/Entity;)D
        //     9: aload_1         /* apx */
        //    10: invokevirtual   net/minecraft/world/entity/Entity.getZ:()D
        //    13: fload_2         /* float2 */
        //    14: fload_3         /* float3 */
        //    15: invokevirtual   net/minecraft/world/entity/ai/control/LookControl.setLookAt:(DDDFF)V
        //    18: return         
        //    MethodParameters:
        //  Name    Flags  
        //  ------  -----
        //  apx     
        //  float2  
        //  float3  
        // 
        // The error that occurred was:
        // 
        // java.lang.ArrayIndexOutOfBoundsException: Array index out of range: 8
        //     at java.util.Vector.get(Vector.java:751)
        //     at com.strobel.assembler.metadata.MetadataResolver.resolve(MetadataResolver.java:82)
        //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.resolve(CoreMetadataFactory.java:616)
        //     at com.strobel.assembler.metadata.MetadataHelper.isRawType(MetadataHelper.java:1581)
        //     at com.strobel.assembler.metadata.MetadataHelper$SameTypeVisitor.visitClassType(MetadataHelper.java:2369)
        //     at com.strobel.assembler.metadata.MetadataHelper$SameTypeVisitor.visitClassType(MetadataHelper.java:2322)
        //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.accept(CoreMetadataFactory.java:577)
        //     at com.strobel.assembler.metadata.MetadataHelper$SameTypeVisitor.visit(MetadataHelper.java:2336)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSameType(MetadataHelper.java:1411)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.isCastRequired(AstMethodBodyBuilder.java:1357)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.adjustArgumentsForMethodCallCore(AstMethodBodyBuilder.java:1318)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.adjustArgumentsForMethodCall(AstMethodBodyBuilder.java:1286)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformCall(AstMethodBodyBuilder.java:1197)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformByteCode(AstMethodBodyBuilder.java:715)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformExpression(AstMethodBodyBuilder.java:540)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformByteCode(AstMethodBodyBuilder.java:554)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformExpression(AstMethodBodyBuilder.java:540)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformNode(AstMethodBodyBuilder.java:392)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformBlock(AstMethodBodyBuilder.java:333)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:294)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:782)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:675)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:552)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:519)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:161)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:150)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:125)
        //     at cuchaz.enigma.source.procyon.ProcyonDecompiler.getSource(ProcyonDecompiler.java:75)
        //     at cuchaz.enigma.EnigmaProject$JarExport.decompileClass(EnigmaProject.java:266)
        //     at cuchaz.enigma.EnigmaProject$JarExport.lambda$decompileStream$1(EnigmaProject.java:242)
        //     at java.util.stream.ReferencePipeline$3$1.accept(ReferencePipeline.java:193)
        //     at java.util.ArrayList$ArrayListSpliterator.forEachRemaining(ArrayList.java:1382)
        //     at java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:482)
        //     at java.util.stream.ForEachOps$ForEachTask.compute(ForEachOps.java:291)
        //     at java.util.concurrent.CountedCompleter.exec(CountedCompleter.java:731)
        //     at java.util.concurrent.ForkJoinTask.doExec(ForkJoinTask.java:289)
        //     at java.util.concurrent.ForkJoinPool$WorkQueue.runTask(ForkJoinPool.java:1056)
        //     at java.util.concurrent.ForkJoinPool.runWorker(ForkJoinPool.java:1692)
        //     at java.util.concurrent.ForkJoinWorkerThread.run(ForkJoinWorkerThread.java:157)
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    public void setLookAt(final double double1, final double double2, final double double3) {
        this.setLookAt(double1, double2, double3, (float)this.mob.getHeadRotSpeed(), (float)this.mob.getMaxHeadXRot());
    }
    
    public void setLookAt(final double double1, final double double2, final double double3, final float float4, final float float5) {
        this.wantedX = double1;
        this.wantedY = double2;
        this.wantedZ = double3;
        this.yMaxRotSpeed = float4;
        this.xMaxRotAngle = float5;
        this.hasWanted = true;
    }
    
    public void tick() {
        if (this.resetXRotOnTick()) {
            this.mob.xRot = 0.0f;
        }
        if (this.hasWanted) {
            this.hasWanted = false;
            this.mob.yHeadRot = this.rotateTowards(this.mob.yHeadRot, this.getYRotD(), this.yMaxRotSpeed);
            this.mob.xRot = this.rotateTowards(this.mob.xRot, this.getXRotD(), this.xMaxRotAngle);
        }
        else {
            this.mob.yHeadRot = this.rotateTowards(this.mob.yHeadRot, this.mob.yBodyRot, 10.0f);
        }
        if (!this.mob.getNavigation().isDone()) {
            this.mob.yHeadRot = Mth.rotateIfNecessary(this.mob.yHeadRot, this.mob.yBodyRot, (float)this.mob.getMaxHeadYRot());
        }
    }
    
    protected boolean resetXRotOnTick() {
        return true;
    }
    
    public boolean isHasWanted() {
        return this.hasWanted;
    }
    
    public double getWantedX() {
        return this.wantedX;
    }
    
    public double getWantedY() {
        return this.wantedY;
    }
    
    public double getWantedZ() {
        return this.wantedZ;
    }
    
    protected float getXRotD() {
        final double double2 = this.wantedX - this.mob.getX();
        final double double3 = this.wantedY - this.mob.getEyeY();
        final double double4 = this.wantedZ - this.mob.getZ();
        final double double5 = Mth.sqrt(double2 * double2 + double4 * double4);
        return (float)(-(Mth.atan2(double3, double5) * 57.2957763671875));
    }
    
    protected float getYRotD() {
        final double double2 = this.wantedX - this.mob.getX();
        final double double3 = this.wantedZ - this.mob.getZ();
        return (float)(Mth.atan2(double3, double2) * 57.2957763671875) - 90.0f;
    }
    
    protected float rotateTowards(final float float1, final float float2, final float float3) {
        final float float4 = Mth.degreesDifference(float1, float2);
        final float float5 = Mth.clamp(float4, -float3, float3);
        return float1 + float5;
    }
    
    private static double getWantedY(final Entity apx) {
        if (apx instanceof LivingEntity) {
            return apx.getEyeY();
        }
        return (apx.getBoundingBox().minY + apx.getBoundingBox().maxY) / 2.0;
    }
}

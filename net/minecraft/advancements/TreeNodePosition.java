package net.minecraft.advancements;

import java.util.Iterator;
import com.google.common.collect.Lists;
import javax.annotation.Nullable;
import java.util.List;

public class TreeNodePosition {
    private final Advancement advancement;
    private final TreeNodePosition parent;
    private final TreeNodePosition previousSibling;
    private final int childIndex;
    private final List<TreeNodePosition> children;
    private TreeNodePosition ancestor;
    private TreeNodePosition thread;
    private int x;
    private float y;
    private float mod;
    private float change;
    private float shift;
    
    public TreeNodePosition(final Advancement y, @Nullable final TreeNodePosition ak2, @Nullable final TreeNodePosition ak3, final int integer4, final int integer5) {
        this.children = (List<TreeNodePosition>)Lists.newArrayList();
        if (y.getDisplay() == null) {
            throw new IllegalArgumentException("Can't position an invisible advancement!");
        }
        this.advancement = y;
        this.parent = ak2;
        this.previousSibling = ak3;
        this.childIndex = integer4;
        this.ancestor = this;
        this.x = integer5;
        this.y = -1.0f;
        TreeNodePosition ak4 = null;
        for (final Advancement y2 : y.getChildren()) {
            ak4 = this.addChild(y2, ak4);
        }
    }
    
    @Nullable
    private TreeNodePosition addChild(final Advancement y, @Nullable TreeNodePosition ak) {
        if (y.getDisplay() != null) {
            ak = new TreeNodePosition(y, this, ak, this.children.size() + 1, this.x + 1);
            this.children.add(ak);
        }
        else {
            for (final Advancement y2 : y.getChildren()) {
                ak = this.addChild(y2, ak);
            }
        }
        return ak;
    }
    
    private void firstWalk() {
        if (this.children.isEmpty()) {
            if (this.previousSibling != null) {
                this.y = this.previousSibling.y + 1.0f;
            }
            else {
                this.y = 0.0f;
            }
            return;
        }
        TreeNodePosition ak2 = null;
        for (final TreeNodePosition ak3 : this.children) {
            ak3.firstWalk();
            ak2 = ak3.apportion((ak2 == null) ? ak3 : ak2);
        }
        this.executeShifts();
        final float float3 = (((TreeNodePosition)this.children.get(0)).y + ((TreeNodePosition)this.children.get(this.children.size() - 1)).y) / 2.0f;
        if (this.previousSibling != null) {
            this.y = this.previousSibling.y + 1.0f;
            this.mod = this.y - float3;
        }
        else {
            this.y = float3;
        }
    }
    
    private float secondWalk(final float float1, final int integer, float float3) {
        this.y += float1;
        this.x = integer;
        if (this.y < float3) {
            float3 = this.y;
        }
        for (final TreeNodePosition ak6 : this.children) {
            float3 = ak6.secondWalk(float1 + this.mod, integer + 1, float3);
        }
        return float3;
    }
    
    private void thirdWalk(final float float1) {
        this.y += float1;
        for (final TreeNodePosition ak4 : this.children) {
            ak4.thirdWalk(float1);
        }
    }
    
    private void executeShifts() {
        float float2 = 0.0f;
        float float3 = 0.0f;
        for (int integer4 = this.children.size() - 1; integer4 >= 0; --integer4) {
            final TreeNodePosition treeNodePosition;
            final TreeNodePosition ak5 = treeNodePosition = (TreeNodePosition)this.children.get(integer4);
            treeNodePosition.y += float2;
            final TreeNodePosition treeNodePosition2 = ak5;
            treeNodePosition2.mod += float2;
            float3 += ak5.change;
            float2 += ak5.shift + float3;
        }
    }
    
    @Nullable
    private TreeNodePosition previousOrThread() {
        if (this.thread != null) {
            return this.thread;
        }
        if (!this.children.isEmpty()) {
            return (TreeNodePosition)this.children.get(0);
        }
        return null;
    }
    
    @Nullable
    private TreeNodePosition nextOrThread() {
        if (this.thread != null) {
            return this.thread;
        }
        if (!this.children.isEmpty()) {
            return (TreeNodePosition)this.children.get(this.children.size() - 1);
        }
        return null;
    }
    
    private TreeNodePosition apportion(TreeNodePosition ak) {
        if (this.previousSibling == null) {
            return ak;
        }
        TreeNodePosition ak2 = this;
        TreeNodePosition ak3 = this;
        TreeNodePosition ak4 = this.previousSibling;
        TreeNodePosition ak5 = (TreeNodePosition)this.parent.children.get(0);
        float float7 = this.mod;
        float float8 = this.mod;
        float float9 = ak4.mod;
        float float10 = ak5.mod;
        while (ak4.nextOrThread() != null && ak2.previousOrThread() != null) {
            ak4 = ak4.nextOrThread();
            ak2 = ak2.previousOrThread();
            ak5 = ak5.previousOrThread();
            ak3 = ak3.nextOrThread();
            ak3.ancestor = this;
            final float float11 = ak4.y + float9 - (ak2.y + float7) + 1.0f;
            if (float11 > 0.0f) {
                ak4.getAncestor(this, ak).moveSubtree(this, float11);
                float7 += float11;
                float8 += float11;
            }
            float9 += ak4.mod;
            float7 += ak2.mod;
            float10 += ak5.mod;
            float8 += ak3.mod;
        }
        if (ak4.nextOrThread() != null && ak3.nextOrThread() == null) {
            ak3.thread = ak4.nextOrThread();
            final TreeNodePosition treeNodePosition = ak3;
            treeNodePosition.mod += float9 - float8;
        }
        else {
            if (ak2.previousOrThread() != null && ak5.previousOrThread() == null) {
                ak5.thread = ak2.previousOrThread();
                final TreeNodePosition treeNodePosition2 = ak5;
                treeNodePosition2.mod += float7 - float10;
            }
            ak = this;
        }
        return ak;
    }
    
    private void moveSubtree(final TreeNodePosition ak, final float float2) {
        final float float3 = (float)(ak.childIndex - this.childIndex);
        if (float3 != 0.0f) {
            ak.change -= float2 / float3;
            this.change += float2 / float3;
        }
        ak.shift += float2;
        ak.y += float2;
        ak.mod += float2;
    }
    
    private TreeNodePosition getAncestor(final TreeNodePosition ak1, final TreeNodePosition ak2) {
        if (this.ancestor != null && ak1.parent.children.contains(this.ancestor)) {
            return this.ancestor;
        }
        return ak2;
    }
    
    private void finalizePosition() {
        if (this.advancement.getDisplay() != null) {
            this.advancement.getDisplay().setLocation((float)this.x, this.y);
        }
        if (!this.children.isEmpty()) {
            for (final TreeNodePosition ak3 : this.children) {
                ak3.finalizePosition();
            }
        }
    }
    
    public static void run(final Advancement y) {
        if (y.getDisplay() == null) {
            throw new IllegalArgumentException("Can't position children of an invisible root!");
        }
        final TreeNodePosition ak2 = new TreeNodePosition(y, null, null, 1, 0);
        ak2.firstWalk();
        final float float3 = ak2.secondWalk(0.0f, 0, ak2.y);
        if (float3 < 0.0f) {
            ak2.thirdWalk(-float3);
        }
        ak2.finalizePosition();
    }
}

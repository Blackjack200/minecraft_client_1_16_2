package net.minecraft.client.gui.components;

import com.google.common.collect.Lists;
import java.util.AbstractList;
import java.util.function.Predicate;
import net.minecraft.client.gui.components.events.GuiEventListener;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiComponent;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.util.Mth;
import java.util.Objects;
import java.util.Collection;
import javax.annotation.Nullable;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.events.AbstractContainerEventHandler;

public abstract class AbstractSelectionList<E extends Entry<E>> extends AbstractContainerEventHandler implements Widget {
    protected final Minecraft minecraft;
    protected final int itemHeight;
    private final List<E> children;
    protected int width;
    protected int height;
    protected int y0;
    protected int y1;
    protected int x1;
    protected int x0;
    protected boolean centerListVertically;
    private double scrollAmount;
    private boolean renderSelection;
    private boolean renderHeader;
    protected int headerHeight;
    private boolean scrolling;
    private E selected;
    
    public AbstractSelectionList(final Minecraft djw, final int integer2, final int integer3, final int integer4, final int integer5, final int integer6) {
        this.children = (List<E>)new TrackedList();
        this.centerListVertically = true;
        this.renderSelection = true;
        this.minecraft = djw;
        this.width = integer2;
        this.height = integer3;
        this.y0 = integer4;
        this.y1 = integer5;
        this.itemHeight = integer6;
        this.x0 = 0;
        this.x1 = integer2;
    }
    
    public void setRenderSelection(final boolean boolean1) {
        this.renderSelection = boolean1;
    }
    
    protected void setRenderHeader(final boolean boolean1, final int integer) {
        this.renderHeader = boolean1;
        this.headerHeight = integer;
        if (!boolean1) {
            this.headerHeight = 0;
        }
    }
    
    public int getRowWidth() {
        return 220;
    }
    
    @Nullable
    public E getSelected() {
        return this.selected;
    }
    
    public void setSelected(@Nullable final E a) {
        this.selected = a;
    }
    
    @Nullable
    @Override
    public E getFocused() {
        return (E)super.getFocused();
    }
    
    @Override
    public final List<E> children() {
        return this.children;
    }
    
    protected final void clearEntries() {
        this.children.clear();
    }
    
    protected void replaceEntries(final Collection<E> collection) {
        this.children.clear();
        this.children.addAll((Collection)collection);
    }
    
    protected E getEntry(final int integer) {
        return (E)this.children().get(integer);
    }
    
    protected int addEntry(final E a) {
        this.children.add(a);
        return this.children.size() - 1;
    }
    
    protected int getItemCount() {
        return this.children().size();
    }
    
    protected boolean isSelectedItem(final int integer) {
        return Objects.equals(this.getSelected(), this.children().get(integer));
    }
    
    @Nullable
    protected final E getEntryAtPosition(final double double1, final double double2) {
        final int integer6 = this.getRowWidth() / 2;
        final int integer7 = this.x0 + this.width / 2;
        final int integer8 = integer7 - integer6;
        final int integer9 = integer7 + integer6;
        final int integer10 = Mth.floor(double2 - this.y0) - this.headerHeight + (int)this.getScrollAmount() - 4;
        final int integer11 = integer10 / this.itemHeight;
        if (double1 < this.getScrollbarPosition() && double1 >= integer8 && double1 <= integer9 && integer11 >= 0 && integer10 >= 0 && integer11 < this.getItemCount()) {
            return (E)this.children().get(integer11);
        }
        return null;
    }
    
    public void updateSize(final int integer1, final int integer2, final int integer3, final int integer4) {
        this.width = integer1;
        this.height = integer2;
        this.y0 = integer3;
        this.y1 = integer4;
        this.x0 = 0;
        this.x1 = integer1;
    }
    
    public void setLeftPos(final int integer) {
        this.x0 = integer;
        this.x1 = integer + this.width;
    }
    
    protected int getMaxPosition() {
        return this.getItemCount() * this.itemHeight + this.headerHeight;
    }
    
    protected void clickedHeader(final int integer1, final int integer2) {
    }
    
    protected void renderHeader(final PoseStack dfj, final int integer2, final int integer3, final Tesselator dfl) {
    }
    
    protected void renderBackground(final PoseStack dfj) {
    }
    
    protected void renderDecorations(final PoseStack dfj, final int integer2, final int integer3) {
    }
    
    @Override
    public void render(final PoseStack dfj, final int integer2, final int integer3, final float float4) {
        this.renderBackground(dfj);
        final int integer4 = this.getScrollbarPosition();
        final int integer5 = integer4 + 6;
        final Tesselator dfl8 = Tesselator.getInstance();
        final BufferBuilder dfe9 = dfl8.getBuilder();
        this.minecraft.getTextureManager().bind(GuiComponent.BACKGROUND_LOCATION);
        RenderSystem.color4f(1.0f, 1.0f, 1.0f, 1.0f);
        final float float5 = 32.0f;
        dfe9.begin(7, DefaultVertexFormat.POSITION_TEX_COLOR);
        dfe9.vertex(this.x0, this.y1, 0.0).uv(this.x0 / 32.0f, (this.y1 + (int)this.getScrollAmount()) / 32.0f).color(32, 32, 32, 255).endVertex();
        dfe9.vertex(this.x1, this.y1, 0.0).uv(this.x1 / 32.0f, (this.y1 + (int)this.getScrollAmount()) / 32.0f).color(32, 32, 32, 255).endVertex();
        dfe9.vertex(this.x1, this.y0, 0.0).uv(this.x1 / 32.0f, (this.y0 + (int)this.getScrollAmount()) / 32.0f).color(32, 32, 32, 255).endVertex();
        dfe9.vertex(this.x0, this.y0, 0.0).uv(this.x0 / 32.0f, (this.y0 + (int)this.getScrollAmount()) / 32.0f).color(32, 32, 32, 255).endVertex();
        dfl8.end();
        final int integer6 = this.getRowLeft();
        final int integer7 = this.y0 + 4 - (int)this.getScrollAmount();
        if (this.renderHeader) {
            this.renderHeader(dfj, integer6, integer7, dfl8);
        }
        this.renderList(dfj, integer6, integer7, integer2, integer3, float4);
        this.minecraft.getTextureManager().bind(GuiComponent.BACKGROUND_LOCATION);
        RenderSystem.enableDepthTest();
        RenderSystem.depthFunc(519);
        final float float6 = 32.0f;
        final int integer8 = -100;
        dfe9.begin(7, DefaultVertexFormat.POSITION_TEX_COLOR);
        dfe9.vertex(this.x0, this.y0, -100.0).uv(0.0f, this.y0 / 32.0f).color(64, 64, 64, 255).endVertex();
        dfe9.vertex(this.x0 + this.width, this.y0, -100.0).uv(this.width / 32.0f, this.y0 / 32.0f).color(64, 64, 64, 255).endVertex();
        dfe9.vertex(this.x0 + this.width, 0.0, -100.0).uv(this.width / 32.0f, 0.0f).color(64, 64, 64, 255).endVertex();
        dfe9.vertex(this.x0, 0.0, -100.0).uv(0.0f, 0.0f).color(64, 64, 64, 255).endVertex();
        dfe9.vertex(this.x0, this.height, -100.0).uv(0.0f, this.height / 32.0f).color(64, 64, 64, 255).endVertex();
        dfe9.vertex(this.x0 + this.width, this.height, -100.0).uv(this.width / 32.0f, this.height / 32.0f).color(64, 64, 64, 255).endVertex();
        dfe9.vertex(this.x0 + this.width, this.y1, -100.0).uv(this.width / 32.0f, this.y1 / 32.0f).color(64, 64, 64, 255).endVertex();
        dfe9.vertex(this.x0, this.y1, -100.0).uv(0.0f, this.y1 / 32.0f).color(64, 64, 64, 255).endVertex();
        dfl8.end();
        RenderSystem.depthFunc(515);
        RenderSystem.disableDepthTest();
        RenderSystem.enableBlend();
        RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ZERO, GlStateManager.DestFactor.ONE);
        RenderSystem.disableAlphaTest();
        RenderSystem.shadeModel(7425);
        RenderSystem.disableTexture();
        final int integer9 = 4;
        dfe9.begin(7, DefaultVertexFormat.POSITION_TEX_COLOR);
        dfe9.vertex(this.x0, this.y0 + 4, 0.0).uv(0.0f, 1.0f).color(0, 0, 0, 0).endVertex();
        dfe9.vertex(this.x1, this.y0 + 4, 0.0).uv(1.0f, 1.0f).color(0, 0, 0, 0).endVertex();
        dfe9.vertex(this.x1, this.y0, 0.0).uv(1.0f, 0.0f).color(0, 0, 0, 255).endVertex();
        dfe9.vertex(this.x0, this.y0, 0.0).uv(0.0f, 0.0f).color(0, 0, 0, 255).endVertex();
        dfe9.vertex(this.x0, this.y1, 0.0).uv(0.0f, 1.0f).color(0, 0, 0, 255).endVertex();
        dfe9.vertex(this.x1, this.y1, 0.0).uv(1.0f, 1.0f).color(0, 0, 0, 255).endVertex();
        dfe9.vertex(this.x1, this.y1 - 4, 0.0).uv(1.0f, 0.0f).color(0, 0, 0, 0).endVertex();
        dfe9.vertex(this.x0, this.y1 - 4, 0.0).uv(0.0f, 0.0f).color(0, 0, 0, 0).endVertex();
        dfl8.end();
        final int integer10 = this.getMaxScroll();
        if (integer10 > 0) {
            int integer11 = (int)((this.y1 - this.y0) * (this.y1 - this.y0) / (float)this.getMaxPosition());
            integer11 = Mth.clamp(integer11, 32, this.y1 - this.y0 - 8);
            int integer12 = (int)this.getScrollAmount() * (this.y1 - this.y0 - integer11) / integer10 + this.y0;
            if (integer12 < this.y0) {
                integer12 = this.y0;
            }
            dfe9.begin(7, DefaultVertexFormat.POSITION_TEX_COLOR);
            dfe9.vertex(integer4, this.y1, 0.0).uv(0.0f, 1.0f).color(0, 0, 0, 255).endVertex();
            dfe9.vertex(integer5, this.y1, 0.0).uv(1.0f, 1.0f).color(0, 0, 0, 255).endVertex();
            dfe9.vertex(integer5, this.y0, 0.0).uv(1.0f, 0.0f).color(0, 0, 0, 255).endVertex();
            dfe9.vertex(integer4, this.y0, 0.0).uv(0.0f, 0.0f).color(0, 0, 0, 255).endVertex();
            dfe9.vertex(integer4, integer12 + integer11, 0.0).uv(0.0f, 1.0f).color(128, 128, 128, 255).endVertex();
            dfe9.vertex(integer5, integer12 + integer11, 0.0).uv(1.0f, 1.0f).color(128, 128, 128, 255).endVertex();
            dfe9.vertex(integer5, integer12, 0.0).uv(1.0f, 0.0f).color(128, 128, 128, 255).endVertex();
            dfe9.vertex(integer4, integer12, 0.0).uv(0.0f, 0.0f).color(128, 128, 128, 255).endVertex();
            dfe9.vertex(integer4, integer12 + integer11 - 1, 0.0).uv(0.0f, 1.0f).color(192, 192, 192, 255).endVertex();
            dfe9.vertex(integer5 - 1, integer12 + integer11 - 1, 0.0).uv(1.0f, 1.0f).color(192, 192, 192, 255).endVertex();
            dfe9.vertex(integer5 - 1, integer12, 0.0).uv(1.0f, 0.0f).color(192, 192, 192, 255).endVertex();
            dfe9.vertex(integer4, integer12, 0.0).uv(0.0f, 0.0f).color(192, 192, 192, 255).endVertex();
            dfl8.end();
        }
        this.renderDecorations(dfj, integer2, integer3);
        RenderSystem.enableTexture();
        RenderSystem.shadeModel(7424);
        RenderSystem.enableAlphaTest();
        RenderSystem.disableBlend();
    }
    
    protected void centerScrollOn(final E a) {
        this.setScrollAmount(this.children().indexOf(a) * this.itemHeight + this.itemHeight / 2 - (this.y1 - this.y0) / 2);
    }
    
    protected void ensureVisible(final E a) {
        final int integer3 = this.getRowTop(this.children().indexOf(a));
        final int integer4 = integer3 - this.y0 - 4 - this.itemHeight;
        if (integer4 < 0) {
            this.scroll(integer4);
        }
        final int integer5 = this.y1 - integer3 - this.itemHeight - this.itemHeight;
        if (integer5 < 0) {
            this.scroll(-integer5);
        }
    }
    
    private void scroll(final int integer) {
        this.setScrollAmount(this.getScrollAmount() + integer);
    }
    
    public double getScrollAmount() {
        return this.scrollAmount;
    }
    
    public void setScrollAmount(final double double1) {
        this.scrollAmount = Mth.clamp(double1, 0.0, this.getMaxScroll());
    }
    
    private int getMaxScroll() {
        return Math.max(0, this.getMaxPosition() - (this.y1 - this.y0 - 4));
    }
    
    protected void updateScrollingState(final double double1, final double double2, final int integer) {
        this.scrolling = (integer == 0 && double1 >= this.getScrollbarPosition() && double1 < this.getScrollbarPosition() + 6);
    }
    
    protected int getScrollbarPosition() {
        return this.width / 2 + 124;
    }
    
    @Override
    public boolean mouseClicked(final double double1, final double double2, final int integer) {
        this.updateScrollingState(double1, double2, integer);
        if (!this.isMouseOver(double1, double2)) {
            return false;
        }
        final E a7 = this.getEntryAtPosition(double1, double2);
        if (a7 != null) {
            if (a7.mouseClicked(double1, double2, integer)) {
                this.setFocused(a7);
                this.setDragging(true);
                return true;
            }
        }
        else if (integer == 0) {
            this.clickedHeader((int)(double1 - (this.x0 + this.width / 2 - this.getRowWidth() / 2)), (int)(double2 - this.y0) + (int)this.getScrollAmount() - 4);
            return true;
        }
        return this.scrolling;
    }
    
    @Override
    public boolean mouseReleased(final double double1, final double double2, final int integer) {
        if (this.getFocused() != null) {
            this.getFocused().mouseReleased(double1, double2, integer);
        }
        return false;
    }
    
    @Override
    public boolean mouseDragged(final double double1, final double double2, final int integer, final double double4, final double double5) {
        if (super.mouseDragged(double1, double2, integer, double4, double5)) {
            return true;
        }
        if (integer != 0 || !this.scrolling) {
            return false;
        }
        if (double2 < this.y0) {
            this.setScrollAmount(0.0);
        }
        else if (double2 > this.y1) {
            this.setScrollAmount(this.getMaxScroll());
        }
        else {
            final double double6 = Math.max(1, this.getMaxScroll());
            final int integer2 = this.y1 - this.y0;
            final int integer3 = Mth.clamp((int)(integer2 * integer2 / (float)this.getMaxPosition()), 32, integer2 - 8);
            final double double7 = Math.max(1.0, double6 / (integer2 - integer3));
            this.setScrollAmount(this.getScrollAmount() + double5 * double7);
        }
        return true;
    }
    
    @Override
    public boolean mouseScrolled(final double double1, final double double2, final double double3) {
        this.setScrollAmount(this.getScrollAmount() - double3 * this.itemHeight / 2.0);
        return true;
    }
    
    @Override
    public boolean keyPressed(final int integer1, final int integer2, final int integer3) {
        if (super.keyPressed(integer1, integer2, integer3)) {
            return true;
        }
        if (integer1 == 264) {
            this.moveSelection(SelectionDirection.DOWN);
            return true;
        }
        if (integer1 == 265) {
            this.moveSelection(SelectionDirection.UP);
            return true;
        }
        return false;
    }
    
    protected void moveSelection(final SelectionDirection b) {
        this.moveSelection(b, (Predicate<E>)(a -> true));
    }
    
    protected void refreshSelection() {
        final E a2 = this.getSelected();
        if (a2 != null) {
            this.setSelected(a2);
            this.ensureVisible(a2);
        }
    }
    
    protected void moveSelection(final SelectionDirection b, final Predicate<E> predicate) {
        final int integer4 = (b == SelectionDirection.UP) ? -1 : 1;
        if (!this.children().isEmpty()) {
            int integer5 = this.children().indexOf(this.getSelected());
            while (true) {
                final int integer6 = Mth.clamp(integer5 + integer4, 0, this.getItemCount() - 1);
                if (integer5 == integer6) {
                    break;
                }
                final E a7 = (E)this.children().get(integer6);
                if (predicate.test(a7)) {
                    this.setSelected(a7);
                    this.ensureVisible(a7);
                    break;
                }
                integer5 = integer6;
            }
        }
    }
    
    public boolean isMouseOver(final double double1, final double double2) {
        return double2 >= this.y0 && double2 <= this.y1 && double1 >= this.x0 && double1 <= this.x1;
    }
    
    protected void renderList(final PoseStack dfj, final int integer2, final int integer3, final int integer4, final int integer5, final float float6) {
        final int integer6 = this.getItemCount();
        final Tesselator dfl9 = Tesselator.getInstance();
        final BufferBuilder dfe10 = dfl9.getBuilder();
        for (int integer7 = 0; integer7 < integer6; ++integer7) {
            final int integer8 = this.getRowTop(integer7);
            final int integer9 = this.getRowBottom(integer7);
            if (integer9 >= this.y0) {
                if (integer8 <= this.y1) {
                    final int integer10 = integer3 + integer7 * this.itemHeight + this.headerHeight;
                    final int integer11 = this.itemHeight - 4;
                    final E a16 = this.getEntry(integer7);
                    final int integer12 = this.getRowWidth();
                    if (this.renderSelection && this.isSelectedItem(integer7)) {
                        final int integer13 = this.x0 + this.width / 2 - integer12 / 2;
                        final int integer14 = this.x0 + this.width / 2 + integer12 / 2;
                        RenderSystem.disableTexture();
                        final float float7 = this.isFocused() ? 1.0f : 0.5f;
                        RenderSystem.color4f(float7, float7, float7, 1.0f);
                        dfe10.begin(7, DefaultVertexFormat.POSITION);
                        dfe10.vertex(integer13, integer10 + integer11 + 2, 0.0).endVertex();
                        dfe10.vertex(integer14, integer10 + integer11 + 2, 0.0).endVertex();
                        dfe10.vertex(integer14, integer10 - 2, 0.0).endVertex();
                        dfe10.vertex(integer13, integer10 - 2, 0.0).endVertex();
                        dfl9.end();
                        RenderSystem.color4f(0.0f, 0.0f, 0.0f, 1.0f);
                        dfe10.begin(7, DefaultVertexFormat.POSITION);
                        dfe10.vertex(integer13 + 1, integer10 + integer11 + 1, 0.0).endVertex();
                        dfe10.vertex(integer14 - 1, integer10 + integer11 + 1, 0.0).endVertex();
                        dfe10.vertex(integer14 - 1, integer10 - 1, 0.0).endVertex();
                        dfe10.vertex(integer13 + 1, integer10 - 1, 0.0).endVertex();
                        dfl9.end();
                        RenderSystem.enableTexture();
                    }
                    final int integer13 = this.getRowLeft();
                    a16.render(dfj, integer7, integer8, integer13, integer12, integer11, integer4, integer5, this.isMouseOver(integer4, integer5) && Objects.equals(this.getEntryAtPosition(integer4, integer5), a16), float6);
                }
            }
        }
    }
    
    protected int getRowLeft() {
        return this.x0 + this.width / 2 - this.getRowWidth() / 2 + 2;
    }
    
    protected int getRowTop(final int integer) {
        return this.y0 + 4 - (int)this.getScrollAmount() + integer * this.itemHeight + this.headerHeight;
    }
    
    private int getRowBottom(final int integer) {
        return this.getRowTop(integer) + this.itemHeight;
    }
    
    protected boolean isFocused() {
        return false;
    }
    
    protected E remove(final int integer) {
        final E a3 = (E)this.children.get(integer);
        if (this.removeEntry((E)this.children.get(integer))) {
            return a3;
        }
        return null;
    }
    
    protected boolean removeEntry(final E a) {
        final boolean boolean3 = this.children.remove(a);
        if (boolean3 && a == this.getSelected()) {
            this.setSelected(null);
        }
        return boolean3;
    }
    
    private void bindEntryToSelf(final Entry<E> a) {
        ((Entry<Entry>)a).list = (AbstractSelectionList<Entry>)this;
    }
    
    public enum SelectionDirection {
        UP, 
        DOWN;
    }
    
    public abstract static class Entry<E extends Entry<E>> implements GuiEventListener {
        @Deprecated
        private AbstractSelectionList<E> list;
        
        public abstract void render(final PoseStack dfj, final int integer2, final int integer3, final int integer4, final int integer5, final int integer6, final int integer7, final int integer8, final boolean boolean9, final float float10);
        
        public boolean isMouseOver(final double double1, final double double2) {
            return Objects.equals(this.list.getEntryAtPosition(double1, double2), this);
        }
    }
    
    class TrackedList extends AbstractList<E> {
        private final List<E> delegate;
        
        private TrackedList() {
            this.delegate = (List<E>)Lists.newArrayList();
        }
        
        public E get(final int integer) {
            return (E)this.delegate.get(integer);
        }
        
        public int size() {
            return this.delegate.size();
        }
        
        public E set(final int integer, final E a) {
            final E a2 = (E)this.delegate.set(integer, a);
            AbstractSelectionList.this.bindEntryToSelf(a);
            return a2;
        }
        
        public void add(final int integer, final E a) {
            this.delegate.add(integer, a);
            AbstractSelectionList.this.bindEntryToSelf(a);
        }
        
        public E remove(final int integer) {
            return (E)this.delegate.remove(integer);
        }
    }
}

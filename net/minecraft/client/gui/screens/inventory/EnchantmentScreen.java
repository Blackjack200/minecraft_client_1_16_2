package net.minecraft.client.gui.screens.inventory;

import net.minecraft.network.chat.MutableComponent;
import java.util.List;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.TranslatableComponent;
import com.google.common.collect.Lists;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.network.chat.FormattedText;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import com.mojang.blaze3d.vertex.Tesselator;
import net.minecraft.util.Mth;
import com.mojang.math.Vector3f;
import com.mojang.math.Matrix4f;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.world.entity.player.Player;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import java.util.Random;
import net.minecraft.client.model.BookModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.EnchantmentMenu;

public class EnchantmentScreen extends AbstractContainerScreen<EnchantmentMenu> {
    private static final ResourceLocation ENCHANTING_TABLE_LOCATION;
    private static final ResourceLocation ENCHANTING_BOOK_LOCATION;
    private static final BookModel BOOK_MODEL;
    private final Random random;
    public int time;
    public float flip;
    public float oFlip;
    public float flipT;
    public float flipA;
    public float open;
    public float oOpen;
    private ItemStack last;
    
    public EnchantmentScreen(final EnchantmentMenu bip, final Inventory bfs, final Component nr) {
        super(bip, bfs, nr);
        this.random = new Random();
        this.last = ItemStack.EMPTY;
    }
    
    @Override
    public void tick() {
        super.tick();
        this.tickBook();
    }
    
    @Override
    public boolean mouseClicked(final double double1, final double double2, final int integer) {
        final int integer2 = (this.width - this.imageWidth) / 2;
        final int integer3 = (this.height - this.imageHeight) / 2;
        for (int integer4 = 0; integer4 < 3; ++integer4) {
            final double double3 = double1 - (integer2 + 60);
            final double double4 = double2 - (integer3 + 14 + 19 * integer4);
            if (double3 >= 0.0 && double4 >= 0.0 && double3 < 108.0 && double4 < 19.0 && ((EnchantmentMenu)this.menu).clickMenuButton(this.minecraft.player, integer4)) {
                this.minecraft.gameMode.handleInventoryButtonClick(((EnchantmentMenu)this.menu).containerId, integer4);
                return true;
            }
        }
        return super.mouseClicked(double1, double2, integer);
    }
    
    @Override
    protected void renderBg(final PoseStack dfj, final float float2, final int integer3, final int integer4) {
        Lighting.setupForFlatItems();
        RenderSystem.color4f(1.0f, 1.0f, 1.0f, 1.0f);
        this.minecraft.getTextureManager().bind(EnchantmentScreen.ENCHANTING_TABLE_LOCATION);
        final int integer5 = (this.width - this.imageWidth) / 2;
        final int integer6 = (this.height - this.imageHeight) / 2;
        this.blit(dfj, integer5, integer6, 0, 0, this.imageWidth, this.imageHeight);
        RenderSystem.matrixMode(5889);
        RenderSystem.pushMatrix();
        RenderSystem.loadIdentity();
        final int integer7 = (int)this.minecraft.getWindow().getGuiScale();
        RenderSystem.viewport((this.width - 320) / 2 * integer7, (this.height - 240) / 2 * integer7, 320 * integer7, 240 * integer7);
        RenderSystem.translatef(-0.34f, 0.23f, 0.0f);
        RenderSystem.multMatrix(Matrix4f.perspective(90.0, 1.3333334f, 9.0f, 80.0f));
        RenderSystem.matrixMode(5888);
        dfj.pushPose();
        final PoseStack.Pose a9 = dfj.last();
        a9.pose().setIdentity();
        a9.normal().setIdentity();
        dfj.translate(0.0, 3.299999952316284, 1984.0);
        final float float3 = 5.0f;
        dfj.scale(5.0f, 5.0f, 5.0f);
        dfj.mulPose(Vector3f.ZP.rotationDegrees(180.0f));
        dfj.mulPose(Vector3f.XP.rotationDegrees(20.0f));
        final float float4 = Mth.lerp(float2, this.oOpen, this.open);
        dfj.translate((1.0f - float4) * 0.2f, (1.0f - float4) * 0.1f, (1.0f - float4) * 0.25f);
        final float float5 = -(1.0f - float4) * 90.0f - 90.0f;
        dfj.mulPose(Vector3f.YP.rotationDegrees(float5));
        dfj.mulPose(Vector3f.XP.rotationDegrees(180.0f));
        float float6 = Mth.lerp(float2, this.oFlip, this.flip) + 0.25f;
        float float7 = Mth.lerp(float2, this.oFlip, this.flip) + 0.75f;
        float6 = (float6 - Mth.fastFloor(float6)) * 1.6f - 0.3f;
        float7 = (float7 - Mth.fastFloor(float7)) * 1.6f - 0.3f;
        if (float6 < 0.0f) {
            float6 = 0.0f;
        }
        if (float7 < 0.0f) {
            float7 = 0.0f;
        }
        if (float6 > 1.0f) {
            float6 = 1.0f;
        }
        if (float7 > 1.0f) {
            float7 = 1.0f;
        }
        RenderSystem.enableRescaleNormal();
        EnchantmentScreen.BOOK_MODEL.setupAnim(0.0f, float6, float7, float4);
        final MultiBufferSource.BufferSource a10 = MultiBufferSource.immediate(Tesselator.getInstance().getBuilder());
        final VertexConsumer dfn16 = a10.getBuffer(EnchantmentScreen.BOOK_MODEL.renderType(EnchantmentScreen.ENCHANTING_BOOK_LOCATION));
        EnchantmentScreen.BOOK_MODEL.renderToBuffer(dfj, dfn16, 15728880, OverlayTexture.NO_OVERLAY, 1.0f, 1.0f, 1.0f, 1.0f);
        a10.endBatch();
        dfj.popPose();
        RenderSystem.matrixMode(5889);
        RenderSystem.viewport(0, 0, this.minecraft.getWindow().getWidth(), this.minecraft.getWindow().getHeight());
        RenderSystem.popMatrix();
        RenderSystem.matrixMode(5888);
        Lighting.setupFor3DItems();
        RenderSystem.color4f(1.0f, 1.0f, 1.0f, 1.0f);
        EnchantmentNames.getInstance().initSeed(((EnchantmentMenu)this.menu).getEnchantmentSeed());
        final int integer8 = ((EnchantmentMenu)this.menu).getGoldCount();
        for (int integer9 = 0; integer9 < 3; ++integer9) {
            final int integer10 = integer5 + 60;
            final int integer11 = integer10 + 20;
            this.setBlitOffset(0);
            this.minecraft.getTextureManager().bind(EnchantmentScreen.ENCHANTING_TABLE_LOCATION);
            final int integer12 = ((EnchantmentMenu)this.menu).costs[integer9];
            RenderSystem.color4f(1.0f, 1.0f, 1.0f, 1.0f);
            if (integer12 == 0) {
                this.blit(dfj, integer10, integer6 + 14 + 19 * integer9, 0, 185, 108, 19);
            }
            else {
                final String string22 = new StringBuilder().append("").append(integer12).toString();
                final int integer13 = 86 - this.font.width(string22);
                final FormattedText nu24 = EnchantmentNames.getInstance().getRandomName(this.font, integer13);
                int integer14 = 6839882;
                if ((integer8 < integer9 + 1 || this.minecraft.player.experienceLevel < integer12) && !this.minecraft.player.abilities.instabuild) {
                    this.blit(dfj, integer10, integer6 + 14 + 19 * integer9, 0, 185, 108, 19);
                    this.blit(dfj, integer10 + 1, integer6 + 15 + 19 * integer9, 16 * integer9, 239, 16, 16);
                    this.font.drawWordWrap(nu24, integer11, integer6 + 16 + 19 * integer9, integer13, (integer14 & 0xFEFEFE) >> 1);
                    integer14 = 4226832;
                }
                else {
                    final int integer15 = integer3 - (integer5 + 60);
                    final int integer16 = integer4 - (integer6 + 14 + 19 * integer9);
                    if (integer15 >= 0 && integer16 >= 0 && integer15 < 108 && integer16 < 19) {
                        this.blit(dfj, integer10, integer6 + 14 + 19 * integer9, 0, 204, 108, 19);
                        integer14 = 16777088;
                    }
                    else {
                        this.blit(dfj, integer10, integer6 + 14 + 19 * integer9, 0, 166, 108, 19);
                    }
                    this.blit(dfj, integer10 + 1, integer6 + 15 + 19 * integer9, 16 * integer9, 223, 16, 16);
                    this.font.drawWordWrap(nu24, integer11, integer6 + 16 + 19 * integer9, integer13, integer14);
                    integer14 = 8453920;
                }
                this.font.drawShadow(dfj, string22, (float)(integer11 + 86 - this.font.width(string22)), (float)(integer6 + 16 + 19 * integer9 + 7), integer14);
            }
        }
    }
    
    @Override
    public void render(final PoseStack dfj, final int integer2, final int integer3, float float4) {
        float4 = this.minecraft.getFrameTime();
        this.renderBackground(dfj);
        super.render(dfj, integer2, integer3, float4);
        this.renderTooltip(dfj, integer2, integer3);
        final boolean boolean6 = this.minecraft.player.abilities.instabuild;
        final int integer4 = ((EnchantmentMenu)this.menu).getGoldCount();
        for (int integer5 = 0; integer5 < 3; ++integer5) {
            final int integer6 = ((EnchantmentMenu)this.menu).costs[integer5];
            final Enchantment bpp10 = Enchantment.byId(((EnchantmentMenu)this.menu).enchantClue[integer5]);
            final int integer7 = ((EnchantmentMenu)this.menu).levelClue[integer5];
            final int integer8 = integer5 + 1;
            if (this.isHovering(60, 14 + 19 * integer5, 108, 17, integer2, integer3) && integer6 > 0 && integer7 >= 0 && bpp10 != null) {
                final List<Component> list13 = (List<Component>)Lists.newArrayList();
                list13.add(new TranslatableComponent("container.enchant.clue", new Object[] { bpp10.getFullname(integer7) }).withStyle(ChatFormatting.WHITE));
                if (!boolean6) {
                    list13.add(TextComponent.EMPTY);
                    if (this.minecraft.player.experienceLevel < integer6) {
                        list13.add(new TranslatableComponent("container.enchant.level.requirement", new Object[] { ((EnchantmentMenu)this.menu).costs[integer5] }).withStyle(ChatFormatting.RED));
                    }
                    else {
                        MutableComponent nx14;
                        if (integer8 == 1) {
                            nx14 = new TranslatableComponent("container.enchant.lapis.one");
                        }
                        else {
                            nx14 = new TranslatableComponent("container.enchant.lapis.many", new Object[] { integer8 });
                        }
                        list13.add(nx14.withStyle((integer4 >= integer8) ? ChatFormatting.GRAY : ChatFormatting.RED));
                        MutableComponent nx15;
                        if (integer8 == 1) {
                            nx15 = new TranslatableComponent("container.enchant.level.one");
                        }
                        else {
                            nx15 = new TranslatableComponent("container.enchant.level.many", new Object[] { integer8 });
                        }
                        list13.add(nx15.withStyle(ChatFormatting.GRAY));
                    }
                }
                this.renderComponentTooltip(dfj, list13, integer2, integer3);
                break;
            }
        }
    }
    
    public void tickBook() {
        final ItemStack bly2 = ((EnchantmentMenu)this.menu).getSlot(0).getItem();
        if (!ItemStack.matches(bly2, this.last)) {
            this.last = bly2;
            do {
                this.flipT += this.random.nextInt(4) - this.random.nextInt(4);
            } while (this.flip <= this.flipT + 1.0f && this.flip >= this.flipT - 1.0f);
        }
        ++this.time;
        this.oFlip = this.flip;
        this.oOpen = this.open;
        boolean boolean3 = false;
        for (int integer4 = 0; integer4 < 3; ++integer4) {
            if (((EnchantmentMenu)this.menu).costs[integer4] != 0) {
                boolean3 = true;
            }
        }
        if (boolean3) {
            this.open += 0.2f;
        }
        else {
            this.open -= 0.2f;
        }
        this.open = Mth.clamp(this.open, 0.0f, 1.0f);
        float float4 = (this.flipT - this.flip) * 0.4f;
        final float float5 = 0.2f;
        float4 = Mth.clamp(float4, -0.2f, 0.2f);
        this.flipA += (float4 - this.flipA) * 0.9f;
        this.flip += this.flipA;
    }
    
    static {
        ENCHANTING_TABLE_LOCATION = new ResourceLocation("textures/gui/container/enchanting_table.png");
        ENCHANTING_BOOK_LOCATION = new ResourceLocation("textures/entity/enchanting_table_book.png");
        BOOK_MODEL = new BookModel();
    }
}

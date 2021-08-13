package net.minecraft.client.gui.screens.inventory;

import net.minecraft.network.chat.TextComponent;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.math.Matrix4f;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.resources.model.Material;
import net.minecraft.world.level.block.state.BlockState;
import com.mojang.blaze3d.vertex.BufferUploader;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.Tesselator;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import java.util.function.Function;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.level.block.StandingSignBlock;
import net.minecraft.client.gui.GuiComponent;
import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ServerboundSignUpdatePacket;
import java.util.function.Predicate;
import java.util.function.Consumer;
import java.util.function.Supplier;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.CommonComponents;
import java.util.stream.IntStream;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.client.gui.font.TextFieldHelper;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraft.client.renderer.blockentity.SignRenderer;
import net.minecraft.client.gui.screens.Screen;

public class SignEditScreen extends Screen {
    private final SignRenderer.SignModel signModel;
    private final SignBlockEntity sign;
    private int frame;
    private int line;
    private TextFieldHelper signField;
    private final String[] messages;
    
    public SignEditScreen(final SignBlockEntity cdc) {
        super(new TranslatableComponent("sign.edit"));
        this.signModel = new SignRenderer.SignModel();
        this.messages = (String[])IntStream.range(0, 4).mapToObj(cdc::getMessage).map(Component::getString).toArray(String[]::new);
        this.sign = cdc;
    }
    
    @Override
    protected void init() {
        this.minecraft.keyboardHandler.setSendRepeatsToGui(true);
        this.<Button>addButton(new Button(this.width / 2 - 100, this.height / 4 + 120, 200, 20, CommonComponents.GUI_DONE, dlg -> this.onDone()));
        this.sign.setEditable(false);
        this.signField = new TextFieldHelper((Supplier<String>)(() -> this.messages[this.line]), (Consumer<String>)(string -> {
            this.messages[this.line] = string;
            this.sign.setMessage(this.line, new TextComponent(string));
        }), TextFieldHelper.createClipboardGetter(this.minecraft), TextFieldHelper.createClipboardSetter(this.minecraft), (Predicate<String>)(string -> this.minecraft.font.width(string) <= 90));
    }
    
    @Override
    public void removed() {
        this.minecraft.keyboardHandler.setSendRepeatsToGui(false);
        final ClientPacketListener dwm2 = this.minecraft.getConnection();
        if (dwm2 != null) {
            dwm2.send(new ServerboundSignUpdatePacket(this.sign.getBlockPos(), this.messages[0], this.messages[1], this.messages[2], this.messages[3]));
        }
        this.sign.setEditable(true);
    }
    
    @Override
    public void tick() {
        ++this.frame;
        if (!this.sign.getType().isValid(this.sign.getBlockState().getBlock())) {
            this.onDone();
        }
    }
    
    private void onDone() {
        this.sign.setChanged();
        this.minecraft.setScreen(null);
    }
    
    @Override
    public boolean charTyped(final char character, final int integer) {
        this.signField.charTyped(character);
        return true;
    }
    
    @Override
    public void onClose() {
        this.onDone();
    }
    
    @Override
    public boolean keyPressed(final int integer1, final int integer2, final int integer3) {
        if (integer1 == 265) {
            this.line = (this.line - 1 & 0x3);
            this.signField.setCursorToEnd();
            return true;
        }
        if (integer1 == 264 || integer1 == 257 || integer1 == 335) {
            this.line = (this.line + 1 & 0x3);
            this.signField.setCursorToEnd();
            return true;
        }
        return this.signField.keyPressed(integer1) || super.keyPressed(integer1, integer2, integer3);
    }
    
    @Override
    public void render(final PoseStack dfj, final int integer2, final int integer3, final float float4) {
        Lighting.setupForFlatItems();
        this.renderBackground(dfj);
        GuiComponent.drawCenteredString(dfj, this.font, this.title, this.width / 2, 40, 16777215);
        dfj.pushPose();
        dfj.translate(this.width / 2, 0.0, 50.0);
        final float float5 = 93.75f;
        dfj.scale(93.75f, -93.75f, 93.75f);
        dfj.translate(0.0, -1.3125, 0.0);
        final BlockState cee7 = this.sign.getBlockState();
        final boolean boolean8 = cee7.getBlock() instanceof StandingSignBlock;
        if (!boolean8) {
            dfj.translate(0.0, -0.3125, 0.0);
        }
        final boolean boolean9 = this.frame / 6 % 2 == 0;
        final float float6 = 0.6666667f;
        dfj.pushPose();
        dfj.scale(0.6666667f, -0.6666667f, -0.6666667f);
        final MultiBufferSource.BufferSource a11 = this.minecraft.renderBuffers().bufferSource();
        final Material elj12 = SignRenderer.getMaterial(cee7.getBlock());
        final VertexConsumer dfn13 = elj12.buffer(a11, (Function<ResourceLocation, RenderType>)this.signModel::renderType);
        this.signModel.sign.render(dfj, dfn13, 15728880, OverlayTexture.NO_OVERLAY);
        if (boolean8) {
            this.signModel.stick.render(dfj, dfn13, 15728880, OverlayTexture.NO_OVERLAY);
        }
        dfj.popPose();
        final float float7 = 0.010416667f;
        dfj.translate(0.0, 0.3333333432674408, 0.046666666865348816);
        dfj.scale(0.010416667f, -0.010416667f, 0.010416667f);
        final int integer4 = this.sign.getColor().getTextColor();
        final int integer5 = this.signField.getCursorPos();
        final int integer6 = this.signField.getSelectionPos();
        final int integer7 = this.line * 10 - this.messages.length * 5;
        final Matrix4f b19 = dfj.last().pose();
        for (int integer8 = 0; integer8 < this.messages.length; ++integer8) {
            String string21 = this.messages[integer8];
            if (string21 != null) {
                if (this.font.isBidirectional()) {
                    string21 = this.font.bidirectionalShaping(string21);
                }
                final float float8 = (float)(-this.minecraft.font.width(string21) / 2);
                this.minecraft.font.drawInBatch(string21, float8, (float)(integer8 * 10 - this.messages.length * 5), integer4, false, b19, a11, false, 0, 15728880, false);
                if (integer8 == this.line && integer5 >= 0) {
                    if (boolean9) {
                        final int integer9 = this.minecraft.font.width(string21.substring(0, Math.max(Math.min(integer5, string21.length()), 0)));
                        final int integer10 = integer9 - this.minecraft.font.width(string21) / 2;
                        if (integer5 >= string21.length()) {
                            this.minecraft.font.drawInBatch("_", (float)integer10, (float)integer7, integer4, false, b19, (MultiBufferSource)a11, false, 0, 15728880, false);
                        }
                    }
                }
            }
        }
        a11.endBatch();
        for (int integer8 = 0; integer8 < this.messages.length; ++integer8) {
            final String string21 = this.messages[integer8];
            if (string21 != null && integer8 == this.line) {
                if (integer5 >= 0) {
                    final int integer11 = this.minecraft.font.width(string21.substring(0, Math.max(Math.min(integer5, string21.length()), 0)));
                    final int integer9 = integer11 - this.minecraft.font.width(string21) / 2;
                    if (boolean9 && integer5 < string21.length()) {
                        final int integer17 = integer9;
                        final int integer18 = integer7 - 1;
                        final int integer19 = integer9 + 1;
                        final int n = integer7;
                        this.minecraft.font.getClass();
                        GuiComponent.fill(dfj, integer17, integer18, integer19, n + 9, 0xFF000000 | integer4);
                    }
                    if (integer6 != integer5) {
                        final int integer10 = Math.min(integer5, integer6);
                        final int integer12 = Math.max(integer5, integer6);
                        final int integer13 = this.minecraft.font.width(string21.substring(0, integer10)) - this.minecraft.font.width(string21) / 2;
                        final int integer14 = this.minecraft.font.width(string21.substring(0, integer12)) - this.minecraft.font.width(string21) / 2;
                        final int integer15 = Math.min(integer13, integer14);
                        final int integer16 = Math.max(integer13, integer14);
                        final Tesselator dfl30 = Tesselator.getInstance();
                        final BufferBuilder dfe31 = dfl30.getBuilder();
                        RenderSystem.disableTexture();
                        RenderSystem.enableColorLogicOp();
                        RenderSystem.logicOp(GlStateManager.LogicOp.OR_REVERSE);
                        dfe31.begin(7, DefaultVertexFormat.POSITION_COLOR);
                        final BufferBuilder bufferBuilder = dfe31;
                        final Matrix4f b20 = b19;
                        final float float9 = (float)integer15;
                        final int n2 = integer7;
                        this.minecraft.font.getClass();
                        bufferBuilder.vertex(b20, float9, (float)(n2 + 9), 0.0f).color(0, 0, 255, 255).endVertex();
                        final BufferBuilder bufferBuilder2 = dfe31;
                        final Matrix4f b21 = b19;
                        final float float10 = (float)integer16;
                        final int n3 = integer7;
                        this.minecraft.font.getClass();
                        bufferBuilder2.vertex(b21, float10, (float)(n3 + 9), 0.0f).color(0, 0, 255, 255).endVertex();
                        dfe31.vertex(b19, (float)integer16, (float)integer7, 0.0f).color(0, 0, 255, 255).endVertex();
                        dfe31.vertex(b19, (float)integer15, (float)integer7, 0.0f).color(0, 0, 255, 255).endVertex();
                        dfe31.end();
                        BufferUploader.end(dfe31);
                        RenderSystem.disableColorLogicOp();
                        RenderSystem.enableTexture();
                    }
                }
            }
        }
        dfj.popPose();
        Lighting.setupFor3DItems();
        super.render(dfj, integer2, integer3, float4);
    }
}

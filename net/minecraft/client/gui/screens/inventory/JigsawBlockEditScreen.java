package net.minecraft.client.gui.screens.inventory;

import net.minecraft.client.gui.GuiComponent;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.util.Mth;
import net.minecraft.client.gui.components.AbstractSliderButton;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.level.block.JigsawBlock;
import net.minecraft.network.chat.FormattedText;
import java.util.function.Consumer;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.network.protocol.game.ServerboundJigsawGeneratePacket;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ServerboundSetJigsawBlockPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.gui.chat.NarratorChatListener;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.world.level.block.entity.JigsawBlockEntity;
import net.minecraft.network.chat.Component;
import net.minecraft.client.gui.screens.Screen;

public class JigsawBlockEditScreen extends Screen {
    private static final Component JOINT_LABEL;
    private static final Component POOL_LABEL;
    private static final Component NAME_LABEL;
    private static final Component TARGET_LABEL;
    private static final Component FINAL_STATE_LABEL;
    private final JigsawBlockEntity jigsawEntity;
    private EditBox nameEdit;
    private EditBox targetEdit;
    private EditBox poolEdit;
    private EditBox finalStateEdit;
    private int levels;
    private boolean keepJigsaws;
    private Button jointButton;
    private Button doneButton;
    private JigsawBlockEntity.JointType joint;
    
    public JigsawBlockEditScreen(final JigsawBlockEntity ccw) {
        super(NarratorChatListener.NO_TITLE);
        this.keepJigsaws = true;
        this.jigsawEntity = ccw;
    }
    
    @Override
    public void tick() {
        this.nameEdit.tick();
        this.targetEdit.tick();
        this.poolEdit.tick();
        this.finalStateEdit.tick();
    }
    
    private void onDone() {
        this.sendToServer();
        this.minecraft.setScreen(null);
    }
    
    private void onCancel() {
        this.minecraft.setScreen(null);
    }
    
    private void sendToServer() {
        this.minecraft.getConnection().send(new ServerboundSetJigsawBlockPacket(this.jigsawEntity.getBlockPos(), new ResourceLocation(this.nameEdit.getValue()), new ResourceLocation(this.targetEdit.getValue()), new ResourceLocation(this.poolEdit.getValue()), this.finalStateEdit.getValue(), this.joint));
    }
    
    private void sendGenerate() {
        this.minecraft.getConnection().send(new ServerboundJigsawGeneratePacket(this.jigsawEntity.getBlockPos(), this.levels, this.keepJigsaws));
    }
    
    @Override
    public void onClose() {
        this.onCancel();
    }
    
    @Override
    protected void init() {
        this.minecraft.keyboardHandler.setSendRepeatsToGui(true);
        (this.poolEdit = new EditBox(this.font, this.width / 2 - 152, 20, 300, 20, new TranslatableComponent("jigsaw_block.pool"))).setMaxLength(128);
        this.poolEdit.setValue(this.jigsawEntity.getPool().toString());
        this.poolEdit.setResponder((Consumer<String>)(string -> this.updateValidity()));
        this.children.add(this.poolEdit);
        (this.nameEdit = new EditBox(this.font, this.width / 2 - 152, 55, 300, 20, new TranslatableComponent("jigsaw_block.name"))).setMaxLength(128);
        this.nameEdit.setValue(this.jigsawEntity.getName().toString());
        this.nameEdit.setResponder((Consumer<String>)(string -> this.updateValidity()));
        this.children.add(this.nameEdit);
        (this.targetEdit = new EditBox(this.font, this.width / 2 - 152, 90, 300, 20, new TranslatableComponent("jigsaw_block.target"))).setMaxLength(128);
        this.targetEdit.setValue(this.jigsawEntity.getTarget().toString());
        this.targetEdit.setResponder((Consumer<String>)(string -> this.updateValidity()));
        this.children.add(this.targetEdit);
        (this.finalStateEdit = new EditBox(this.font, this.width / 2 - 152, 125, 300, 20, new TranslatableComponent("jigsaw_block.final_state"))).setMaxLength(256);
        this.finalStateEdit.setValue(this.jigsawEntity.getFinalState());
        this.children.add(this.finalStateEdit);
        this.joint = this.jigsawEntity.getJoint();
        final int integer2 = this.font.width(JigsawBlockEditScreen.JOINT_LABEL) + 10;
        final JigsawBlockEntity.JointType[] arr3;
        final int integer3;
        this.jointButton = this.<Button>addButton(new Button(this.width / 2 - 152 + integer2, 150, 300 - integer2, 20, this.getJointText(), dlg -> {
            arr3 = JigsawBlockEntity.JointType.values();
            integer3 = (this.joint.ordinal() + 1) % arr3.length;
            this.joint = arr3[integer3];
            dlg.setMessage(this.getJointText());
            return;
        }));
        final boolean boolean3 = JigsawBlock.getFrontFacing(this.jigsawEntity.getBlockState()).getAxis().isVertical();
        this.jointButton.active = boolean3;
        this.jointButton.visible = boolean3;
        this.addButton(new AbstractSliderButton(this.width / 2 - 154, 180, 100, 20, TextComponent.EMPTY, 0.0) {
            {
                this.updateMessage();
            }
            
            @Override
            protected void updateMessage() {
                this.setMessage(new TranslatableComponent("jigsaw_block.levels", new Object[] { JigsawBlockEditScreen.this.levels }));
            }
            
            @Override
            protected void applyValue() {
                JigsawBlockEditScreen.this.levels = Mth.floor(Mth.clampedLerp(0.0, 7.0, this.value));
            }
        });
        this.addButton(new Button(this.width / 2 - 50, 180, 100, 20, new TranslatableComponent("jigsaw_block.keep_jigsaws"), dlg -> {
            this.keepJigsaws = !this.keepJigsaws;
            dlg.queueNarration(250);
            return;
        }) {
            @Override
            public Component getMessage() {
                return CommonComponents.optionStatus(super.getMessage(), JigsawBlockEditScreen.this.keepJigsaws);
            }
        });
        this.<Button>addButton(new Button(this.width / 2 + 54, 180, 100, 20, new TranslatableComponent("jigsaw_block.generate"), dlg -> {
            this.onDone();
            this.sendGenerate();
            return;
        }));
        this.doneButton = this.<Button>addButton(new Button(this.width / 2 - 4 - 150, 210, 150, 20, CommonComponents.GUI_DONE, dlg -> this.onDone()));
        this.<Button>addButton(new Button(this.width / 2 + 4, 210, 150, 20, CommonComponents.GUI_CANCEL, dlg -> this.onCancel()));
        this.setInitialFocus(this.poolEdit);
        this.updateValidity();
    }
    
    private void updateValidity() {
        this.doneButton.active = (ResourceLocation.isValidResourceLocation(this.nameEdit.getValue()) && ResourceLocation.isValidResourceLocation(this.targetEdit.getValue()) && ResourceLocation.isValidResourceLocation(this.poolEdit.getValue()));
    }
    
    @Override
    public void resize(final Minecraft djw, final int integer2, final int integer3) {
        final String string5 = this.nameEdit.getValue();
        final String string6 = this.targetEdit.getValue();
        final String string7 = this.poolEdit.getValue();
        final String string8 = this.finalStateEdit.getValue();
        final int integer4 = this.levels;
        final JigsawBlockEntity.JointType a10 = this.joint;
        this.init(djw, integer2, integer3);
        this.nameEdit.setValue(string5);
        this.targetEdit.setValue(string6);
        this.poolEdit.setValue(string7);
        this.finalStateEdit.setValue(string8);
        this.levels = integer4;
        this.joint = a10;
        this.jointButton.setMessage(this.getJointText());
    }
    
    private Component getJointText() {
        return new TranslatableComponent("jigsaw_block.joint." + this.joint.getSerializedName());
    }
    
    @Override
    public void removed() {
        this.minecraft.keyboardHandler.setSendRepeatsToGui(false);
    }
    
    @Override
    public boolean keyPressed(final int integer1, final int integer2, final int integer3) {
        if (super.keyPressed(integer1, integer2, integer3)) {
            return true;
        }
        if (this.doneButton.active && (integer1 == 257 || integer1 == 335)) {
            this.onDone();
            return true;
        }
        return false;
    }
    
    @Override
    public void render(final PoseStack dfj, final int integer2, final int integer3, final float float4) {
        this.renderBackground(dfj);
        GuiComponent.drawString(dfj, this.font, JigsawBlockEditScreen.POOL_LABEL, this.width / 2 - 153, 10, 10526880);
        this.poolEdit.render(dfj, integer2, integer3, float4);
        GuiComponent.drawString(dfj, this.font, JigsawBlockEditScreen.NAME_LABEL, this.width / 2 - 153, 45, 10526880);
        this.nameEdit.render(dfj, integer2, integer3, float4);
        GuiComponent.drawString(dfj, this.font, JigsawBlockEditScreen.TARGET_LABEL, this.width / 2 - 153, 80, 10526880);
        this.targetEdit.render(dfj, integer2, integer3, float4);
        GuiComponent.drawString(dfj, this.font, JigsawBlockEditScreen.FINAL_STATE_LABEL, this.width / 2 - 153, 115, 10526880);
        this.finalStateEdit.render(dfj, integer2, integer3, float4);
        if (JigsawBlock.getFrontFacing(this.jigsawEntity.getBlockState()).getAxis().isVertical()) {
            GuiComponent.drawString(dfj, this.font, JigsawBlockEditScreen.JOINT_LABEL, this.width / 2 - 153, 156, 16777215);
        }
        super.render(dfj, integer2, integer3, float4);
    }
    
    static {
        JOINT_LABEL = new TranslatableComponent("jigsaw_block.joint_label");
        POOL_LABEL = new TranslatableComponent("jigsaw_block.pool");
        NAME_LABEL = new TranslatableComponent("jigsaw_block.name");
        TARGET_LABEL = new TranslatableComponent("jigsaw_block.target");
        FINAL_STATE_LABEL = new TranslatableComponent("jigsaw_block.final_state");
    }
}

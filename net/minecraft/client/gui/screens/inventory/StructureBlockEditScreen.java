package net.minecraft.client.gui.screens.inventory;

import net.minecraft.network.chat.FormattedText;
import net.minecraft.client.gui.GuiComponent;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ServerboundSetStructureBlockPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.CommonComponents;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.level.block.Blocks;
import java.text.DecimalFormat;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.world.level.block.state.properties.StructureMode;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.entity.StructureBlockEntity;
import net.minecraft.network.chat.Component;
import net.minecraft.client.gui.screens.Screen;

public class StructureBlockEditScreen extends Screen {
    private static final Component NAME_LABEL;
    private static final Component POSITION_LABEL;
    private static final Component SIZE_LABEL;
    private static final Component INTEGRITY_LABEL;
    private static final Component CUSTOM_DATA_LABEL;
    private static final Component INCLUDE_ENTITIES_LABEL;
    private static final Component DETECT_SIZE_LABEL;
    private static final Component SHOW_AIR_LABEL;
    private static final Component SHOW_BOUNDING_BOX_LABEL;
    private final StructureBlockEntity structure;
    private Mirror initialMirror;
    private Rotation initialRotation;
    private StructureMode initialMode;
    private boolean initialEntityIgnoring;
    private boolean initialShowAir;
    private boolean initialShowBoundingBox;
    private EditBox nameEdit;
    private EditBox posXEdit;
    private EditBox posYEdit;
    private EditBox posZEdit;
    private EditBox sizeXEdit;
    private EditBox sizeYEdit;
    private EditBox sizeZEdit;
    private EditBox integrityEdit;
    private EditBox seedEdit;
    private EditBox dataEdit;
    private Button doneButton;
    private Button cancelButton;
    private Button saveButton;
    private Button loadButton;
    private Button rot0Button;
    private Button rot90Button;
    private Button rot180Button;
    private Button rot270Button;
    private Button modeButton;
    private Button detectButton;
    private Button entitiesButton;
    private Button mirrorButton;
    private Button toggleAirButton;
    private Button toggleBoundingBox;
    private final DecimalFormat decimalFormat;
    
    public StructureBlockEditScreen(final StructureBlockEntity cdg) {
        super(new TranslatableComponent(Blocks.STRUCTURE_BLOCK.getDescriptionId()));
        this.initialMirror = Mirror.NONE;
        this.initialRotation = Rotation.NONE;
        this.initialMode = StructureMode.DATA;
        this.decimalFormat = new DecimalFormat("0.0###");
        this.structure = cdg;
        this.decimalFormat.setDecimalFormatSymbols(DecimalFormatSymbols.getInstance(Locale.ROOT));
    }
    
    @Override
    public void tick() {
        this.nameEdit.tick();
        this.posXEdit.tick();
        this.posYEdit.tick();
        this.posZEdit.tick();
        this.sizeXEdit.tick();
        this.sizeYEdit.tick();
        this.sizeZEdit.tick();
        this.integrityEdit.tick();
        this.seedEdit.tick();
        this.dataEdit.tick();
    }
    
    private void onDone() {
        if (this.sendToServer(StructureBlockEntity.UpdateType.UPDATE_DATA)) {
            this.minecraft.setScreen(null);
        }
    }
    
    private void onCancel() {
        this.structure.setMirror(this.initialMirror);
        this.structure.setRotation(this.initialRotation);
        this.structure.setMode(this.initialMode);
        this.structure.setIgnoreEntities(this.initialEntityIgnoring);
        this.structure.setShowAir(this.initialShowAir);
        this.structure.setShowBoundingBox(this.initialShowBoundingBox);
        this.minecraft.setScreen(null);
    }
    
    @Override
    protected void init() {
        this.minecraft.keyboardHandler.setSendRepeatsToGui(true);
        this.doneButton = this.<Button>addButton(new Button(this.width / 2 - 4 - 150, 210, 150, 20, CommonComponents.GUI_DONE, dlg -> this.onDone()));
        this.cancelButton = this.<Button>addButton(new Button(this.width / 2 + 4, 210, 150, 20, CommonComponents.GUI_CANCEL, dlg -> this.onCancel()));
        this.saveButton = this.<Button>addButton(new Button(this.width / 2 + 4 + 100, 185, 50, 20, new TranslatableComponent("structure_block.button.save"), dlg -> {
            if (this.structure.getMode() == StructureMode.SAVE) {
                this.sendToServer(StructureBlockEntity.UpdateType.SAVE_AREA);
                this.minecraft.setScreen(null);
            }
            return;
        }));
        this.loadButton = this.<Button>addButton(new Button(this.width / 2 + 4 + 100, 185, 50, 20, new TranslatableComponent("structure_block.button.load"), dlg -> {
            if (this.structure.getMode() == StructureMode.LOAD) {
                this.sendToServer(StructureBlockEntity.UpdateType.LOAD_AREA);
                this.minecraft.setScreen(null);
            }
            return;
        }));
        this.modeButton = this.<Button>addButton(new Button(this.width / 2 - 4 - 150, 185, 50, 20, new TextComponent("MODE"), dlg -> {
            this.structure.nextMode();
            this.updateMode();
            return;
        }));
        this.detectButton = this.<Button>addButton(new Button(this.width / 2 + 4 + 100, 120, 50, 20, new TranslatableComponent("structure_block.button.detect_size"), dlg -> {
            if (this.structure.getMode() == StructureMode.SAVE) {
                this.sendToServer(StructureBlockEntity.UpdateType.SCAN_AREA);
                this.minecraft.setScreen(null);
            }
            return;
        }));
        this.entitiesButton = this.<Button>addButton(new Button(this.width / 2 + 4 + 100, 160, 50, 20, new TextComponent("ENTITIES"), dlg -> {
            this.structure.setIgnoreEntities(!this.structure.isIgnoreEntities());
            this.updateEntitiesButton();
            return;
        }));
        this.mirrorButton = this.<Button>addButton(new Button(this.width / 2 - 20, 185, 40, 20, new TextComponent("MIRROR"), dlg -> {
            switch (this.structure.getMirror()) {
                case NONE: {
                    this.structure.setMirror(Mirror.LEFT_RIGHT);
                    break;
                }
                case LEFT_RIGHT: {
                    this.structure.setMirror(Mirror.FRONT_BACK);
                    break;
                }
                case FRONT_BACK: {
                    this.structure.setMirror(Mirror.NONE);
                    break;
                }
            }
            this.updateMirrorButton();
            return;
        }));
        this.toggleAirButton = this.<Button>addButton(new Button(this.width / 2 + 4 + 100, 80, 50, 20, new TextComponent("SHOWAIR"), dlg -> {
            this.structure.setShowAir(!this.structure.getShowAir());
            this.updateToggleAirButton();
            return;
        }));
        this.toggleBoundingBox = this.<Button>addButton(new Button(this.width / 2 + 4 + 100, 80, 50, 20, new TextComponent("SHOWBB"), dlg -> {
            this.structure.setShowBoundingBox(!this.structure.getShowBoundingBox());
            this.updateToggleBoundingBox();
            return;
        }));
        this.rot0Button = this.<Button>addButton(new Button(this.width / 2 - 1 - 40 - 1 - 40 - 20, 185, 40, 20, new TextComponent("0"), dlg -> {
            this.structure.setRotation(Rotation.NONE);
            this.updateDirectionButtons();
            return;
        }));
        this.rot90Button = this.<Button>addButton(new Button(this.width / 2 - 1 - 40 - 20, 185, 40, 20, new TextComponent("90"), dlg -> {
            this.structure.setRotation(Rotation.CLOCKWISE_90);
            this.updateDirectionButtons();
            return;
        }));
        this.rot180Button = this.<Button>addButton(new Button(this.width / 2 + 1 + 20, 185, 40, 20, new TextComponent("180"), dlg -> {
            this.structure.setRotation(Rotation.CLOCKWISE_180);
            this.updateDirectionButtons();
            return;
        }));
        this.rot270Button = this.<Button>addButton(new Button(this.width / 2 + 1 + 40 + 1 + 20, 185, 40, 20, new TextComponent("270"), dlg -> {
            this.structure.setRotation(Rotation.COUNTERCLOCKWISE_90);
            this.updateDirectionButtons();
            return;
        }));
        (this.nameEdit = new EditBox(this.font, this.width / 2 - 152, 40, 300, 20, new TranslatableComponent("structure_block.structure_name")) {
            @Override
            public boolean charTyped(final char character, final int integer) {
                return Screen.this.isValidCharacterForName(this.getValue(), character, this.getCursorPosition()) && super.charTyped(character, integer);
            }
        }).setMaxLength(64);
        this.nameEdit.setValue(this.structure.getStructureName());
        this.children.add(this.nameEdit);
        final BlockPos fx2 = this.structure.getStructurePos();
        (this.posXEdit = new EditBox(this.font, this.width / 2 - 152, 80, 80, 20, new TranslatableComponent("structure_block.position.x"))).setMaxLength(15);
        this.posXEdit.setValue(Integer.toString(fx2.getX()));
        this.children.add(this.posXEdit);
        (this.posYEdit = new EditBox(this.font, this.width / 2 - 72, 80, 80, 20, new TranslatableComponent("structure_block.position.y"))).setMaxLength(15);
        this.posYEdit.setValue(Integer.toString(fx2.getY()));
        this.children.add(this.posYEdit);
        (this.posZEdit = new EditBox(this.font, this.width / 2 + 8, 80, 80, 20, new TranslatableComponent("structure_block.position.z"))).setMaxLength(15);
        this.posZEdit.setValue(Integer.toString(fx2.getZ()));
        this.children.add(this.posZEdit);
        final BlockPos fx3 = this.structure.getStructureSize();
        (this.sizeXEdit = new EditBox(this.font, this.width / 2 - 152, 120, 80, 20, new TranslatableComponent("structure_block.size.x"))).setMaxLength(15);
        this.sizeXEdit.setValue(Integer.toString(fx3.getX()));
        this.children.add(this.sizeXEdit);
        (this.sizeYEdit = new EditBox(this.font, this.width / 2 - 72, 120, 80, 20, new TranslatableComponent("structure_block.size.y"))).setMaxLength(15);
        this.sizeYEdit.setValue(Integer.toString(fx3.getY()));
        this.children.add(this.sizeYEdit);
        (this.sizeZEdit = new EditBox(this.font, this.width / 2 + 8, 120, 80, 20, new TranslatableComponent("structure_block.size.z"))).setMaxLength(15);
        this.sizeZEdit.setValue(Integer.toString(fx3.getZ()));
        this.children.add(this.sizeZEdit);
        (this.integrityEdit = new EditBox(this.font, this.width / 2 - 152, 120, 80, 20, new TranslatableComponent("structure_block.integrity.integrity"))).setMaxLength(15);
        this.integrityEdit.setValue(this.decimalFormat.format((double)this.structure.getIntegrity()));
        this.children.add(this.integrityEdit);
        (this.seedEdit = new EditBox(this.font, this.width / 2 - 72, 120, 80, 20, new TranslatableComponent("structure_block.integrity.seed"))).setMaxLength(31);
        this.seedEdit.setValue(Long.toString(this.structure.getSeed()));
        this.children.add(this.seedEdit);
        (this.dataEdit = new EditBox(this.font, this.width / 2 - 152, 120, 240, 20, new TranslatableComponent("structure_block.custom_data"))).setMaxLength(128);
        this.dataEdit.setValue(this.structure.getMetaData());
        this.children.add(this.dataEdit);
        this.initialMirror = this.structure.getMirror();
        this.updateMirrorButton();
        this.initialRotation = this.structure.getRotation();
        this.updateDirectionButtons();
        this.initialMode = this.structure.getMode();
        this.updateMode();
        this.initialEntityIgnoring = this.structure.isIgnoreEntities();
        this.updateEntitiesButton();
        this.initialShowAir = this.structure.getShowAir();
        this.updateToggleAirButton();
        this.initialShowBoundingBox = this.structure.getShowBoundingBox();
        this.updateToggleBoundingBox();
        this.setInitialFocus(this.nameEdit);
    }
    
    @Override
    public void resize(final Minecraft djw, final int integer2, final int integer3) {
        final String string5 = this.nameEdit.getValue();
        final String string6 = this.posXEdit.getValue();
        final String string7 = this.posYEdit.getValue();
        final String string8 = this.posZEdit.getValue();
        final String string9 = this.sizeXEdit.getValue();
        final String string10 = this.sizeYEdit.getValue();
        final String string11 = this.sizeZEdit.getValue();
        final String string12 = this.integrityEdit.getValue();
        final String string13 = this.seedEdit.getValue();
        final String string14 = this.dataEdit.getValue();
        this.init(djw, integer2, integer3);
        this.nameEdit.setValue(string5);
        this.posXEdit.setValue(string6);
        this.posYEdit.setValue(string7);
        this.posZEdit.setValue(string8);
        this.sizeXEdit.setValue(string9);
        this.sizeYEdit.setValue(string10);
        this.sizeZEdit.setValue(string11);
        this.integrityEdit.setValue(string12);
        this.seedEdit.setValue(string13);
        this.dataEdit.setValue(string14);
    }
    
    @Override
    public void removed() {
        this.minecraft.keyboardHandler.setSendRepeatsToGui(false);
    }
    
    private void updateEntitiesButton() {
        this.entitiesButton.setMessage(CommonComponents.optionStatus(!this.structure.isIgnoreEntities()));
    }
    
    private void updateToggleAirButton() {
        this.toggleAirButton.setMessage(CommonComponents.optionStatus(this.structure.getShowAir()));
    }
    
    private void updateToggleBoundingBox() {
        this.toggleBoundingBox.setMessage(CommonComponents.optionStatus(this.structure.getShowBoundingBox()));
    }
    
    private void updateMirrorButton() {
        final Mirror byd2 = this.structure.getMirror();
        switch (byd2) {
            case NONE: {
                this.mirrorButton.setMessage(new TextComponent("|"));
                break;
            }
            case LEFT_RIGHT: {
                this.mirrorButton.setMessage(new TextComponent("< >"));
                break;
            }
            case FRONT_BACK: {
                this.mirrorButton.setMessage(new TextComponent("^ v"));
                break;
            }
        }
    }
    
    private void updateDirectionButtons() {
        this.rot0Button.active = true;
        this.rot90Button.active = true;
        this.rot180Button.active = true;
        this.rot270Button.active = true;
        switch (this.structure.getRotation()) {
            case NONE: {
                this.rot0Button.active = false;
                break;
            }
            case CLOCKWISE_180: {
                this.rot180Button.active = false;
                break;
            }
            case COUNTERCLOCKWISE_90: {
                this.rot270Button.active = false;
                break;
            }
            case CLOCKWISE_90: {
                this.rot90Button.active = false;
                break;
            }
        }
    }
    
    private void updateMode() {
        this.nameEdit.setVisible(false);
        this.posXEdit.setVisible(false);
        this.posYEdit.setVisible(false);
        this.posZEdit.setVisible(false);
        this.sizeXEdit.setVisible(false);
        this.sizeYEdit.setVisible(false);
        this.sizeZEdit.setVisible(false);
        this.integrityEdit.setVisible(false);
        this.seedEdit.setVisible(false);
        this.dataEdit.setVisible(false);
        this.saveButton.visible = false;
        this.loadButton.visible = false;
        this.detectButton.visible = false;
        this.entitiesButton.visible = false;
        this.mirrorButton.visible = false;
        this.rot0Button.visible = false;
        this.rot90Button.visible = false;
        this.rot180Button.visible = false;
        this.rot270Button.visible = false;
        this.toggleAirButton.visible = false;
        this.toggleBoundingBox.visible = false;
        switch (this.structure.getMode()) {
            case SAVE: {
                this.nameEdit.setVisible(true);
                this.posXEdit.setVisible(true);
                this.posYEdit.setVisible(true);
                this.posZEdit.setVisible(true);
                this.sizeXEdit.setVisible(true);
                this.sizeYEdit.setVisible(true);
                this.sizeZEdit.setVisible(true);
                this.saveButton.visible = true;
                this.detectButton.visible = true;
                this.entitiesButton.visible = true;
                this.toggleAirButton.visible = true;
                break;
            }
            case LOAD: {
                this.nameEdit.setVisible(true);
                this.posXEdit.setVisible(true);
                this.posYEdit.setVisible(true);
                this.posZEdit.setVisible(true);
                this.integrityEdit.setVisible(true);
                this.seedEdit.setVisible(true);
                this.loadButton.visible = true;
                this.entitiesButton.visible = true;
                this.mirrorButton.visible = true;
                this.rot0Button.visible = true;
                this.rot90Button.visible = true;
                this.rot180Button.visible = true;
                this.rot270Button.visible = true;
                this.toggleBoundingBox.visible = true;
                this.updateDirectionButtons();
                break;
            }
            case CORNER: {
                this.nameEdit.setVisible(true);
                break;
            }
            case DATA: {
                this.dataEdit.setVisible(true);
                break;
            }
        }
        this.modeButton.setMessage(new TranslatableComponent("structure_block.mode." + this.structure.getMode().getSerializedName()));
    }
    
    private boolean sendToServer(final StructureBlockEntity.UpdateType a) {
        final BlockPos fx3 = new BlockPos(this.parseCoordinate(this.posXEdit.getValue()), this.parseCoordinate(this.posYEdit.getValue()), this.parseCoordinate(this.posZEdit.getValue()));
        final BlockPos fx4 = new BlockPos(this.parseCoordinate(this.sizeXEdit.getValue()), this.parseCoordinate(this.sizeYEdit.getValue()), this.parseCoordinate(this.sizeZEdit.getValue()));
        final float float5 = this.parseIntegrity(this.integrityEdit.getValue());
        final long long6 = this.parseSeed(this.seedEdit.getValue());
        this.minecraft.getConnection().send(new ServerboundSetStructureBlockPacket(this.structure.getBlockPos(), a, this.structure.getMode(), this.nameEdit.getValue(), fx3, fx4, this.structure.getMirror(), this.structure.getRotation(), this.dataEdit.getValue(), this.structure.isIgnoreEntities(), this.structure.getShowAir(), this.structure.getShowBoundingBox(), float5, long6));
        return true;
    }
    
    private long parseSeed(final String string) {
        try {
            return Long.valueOf(string);
        }
        catch (NumberFormatException numberFormatException3) {
            return 0L;
        }
    }
    
    private float parseIntegrity(final String string) {
        try {
            return Float.valueOf(string);
        }
        catch (NumberFormatException numberFormatException3) {
            return 1.0f;
        }
    }
    
    private int parseCoordinate(final String string) {
        try {
            return Integer.parseInt(string);
        }
        catch (NumberFormatException numberFormatException3) {
            return 0;
        }
    }
    
    @Override
    public void onClose() {
        this.onCancel();
    }
    
    @Override
    public boolean keyPressed(final int integer1, final int integer2, final int integer3) {
        if (super.keyPressed(integer1, integer2, integer3)) {
            return true;
        }
        if (integer1 == 257 || integer1 == 335) {
            this.onDone();
            return true;
        }
        return false;
    }
    
    @Override
    public void render(final PoseStack dfj, final int integer2, final int integer3, final float float4) {
        this.renderBackground(dfj);
        final StructureMode cfl6 = this.structure.getMode();
        GuiComponent.drawCenteredString(dfj, this.font, this.title, this.width / 2, 10, 16777215);
        if (cfl6 != StructureMode.DATA) {
            GuiComponent.drawString(dfj, this.font, StructureBlockEditScreen.NAME_LABEL, this.width / 2 - 153, 30, 10526880);
            this.nameEdit.render(dfj, integer2, integer3, float4);
        }
        if (cfl6 == StructureMode.LOAD || cfl6 == StructureMode.SAVE) {
            GuiComponent.drawString(dfj, this.font, StructureBlockEditScreen.POSITION_LABEL, this.width / 2 - 153, 70, 10526880);
            this.posXEdit.render(dfj, integer2, integer3, float4);
            this.posYEdit.render(dfj, integer2, integer3, float4);
            this.posZEdit.render(dfj, integer2, integer3, float4);
            GuiComponent.drawString(dfj, this.font, StructureBlockEditScreen.INCLUDE_ENTITIES_LABEL, this.width / 2 + 154 - this.font.width(StructureBlockEditScreen.INCLUDE_ENTITIES_LABEL), 150, 10526880);
        }
        if (cfl6 == StructureMode.SAVE) {
            GuiComponent.drawString(dfj, this.font, StructureBlockEditScreen.SIZE_LABEL, this.width / 2 - 153, 110, 10526880);
            this.sizeXEdit.render(dfj, integer2, integer3, float4);
            this.sizeYEdit.render(dfj, integer2, integer3, float4);
            this.sizeZEdit.render(dfj, integer2, integer3, float4);
            GuiComponent.drawString(dfj, this.font, StructureBlockEditScreen.DETECT_SIZE_LABEL, this.width / 2 + 154 - this.font.width(StructureBlockEditScreen.DETECT_SIZE_LABEL), 110, 10526880);
            GuiComponent.drawString(dfj, this.font, StructureBlockEditScreen.SHOW_AIR_LABEL, this.width / 2 + 154 - this.font.width(StructureBlockEditScreen.SHOW_AIR_LABEL), 70, 10526880);
        }
        if (cfl6 == StructureMode.LOAD) {
            GuiComponent.drawString(dfj, this.font, StructureBlockEditScreen.INTEGRITY_LABEL, this.width / 2 - 153, 110, 10526880);
            this.integrityEdit.render(dfj, integer2, integer3, float4);
            this.seedEdit.render(dfj, integer2, integer3, float4);
            GuiComponent.drawString(dfj, this.font, StructureBlockEditScreen.SHOW_BOUNDING_BOX_LABEL, this.width / 2 + 154 - this.font.width(StructureBlockEditScreen.SHOW_BOUNDING_BOX_LABEL), 70, 10526880);
        }
        if (cfl6 == StructureMode.DATA) {
            GuiComponent.drawString(dfj, this.font, StructureBlockEditScreen.CUSTOM_DATA_LABEL, this.width / 2 - 153, 110, 10526880);
            this.dataEdit.render(dfj, integer2, integer3, float4);
        }
        GuiComponent.drawString(dfj, this.font, cfl6.getDisplayName(), this.width / 2 - 153, 174, 10526880);
        super.render(dfj, integer2, integer3, float4);
    }
    
    @Override
    public boolean isPauseScreen() {
        return false;
    }
    
    static {
        NAME_LABEL = new TranslatableComponent("structure_block.structure_name");
        POSITION_LABEL = new TranslatableComponent("structure_block.position");
        SIZE_LABEL = new TranslatableComponent("structure_block.size");
        INTEGRITY_LABEL = new TranslatableComponent("structure_block.integrity");
        CUSTOM_DATA_LABEL = new TranslatableComponent("structure_block.custom_data");
        INCLUDE_ENTITIES_LABEL = new TranslatableComponent("structure_block.include_entities");
        DETECT_SIZE_LABEL = new TranslatableComponent("structure_block.detect_size");
        SHOW_AIR_LABEL = new TranslatableComponent("structure_block.show_air");
        SHOW_BOUNDING_BOX_LABEL = new TranslatableComponent("structure_block.show_boundingbox");
    }
}

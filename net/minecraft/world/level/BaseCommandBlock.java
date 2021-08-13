package net.minecraft.world.level;

import com.mojang.brigadier.context.CommandContext;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.server.level.ServerLevel;
import java.util.Date;
import java.util.UUID;
import net.minecraft.CrashReportCategory;
import net.minecraft.server.MinecraftServer;
import net.minecraft.ReportedException;
import net.minecraft.CrashReportDetail;
import net.minecraft.CrashReport;
import net.minecraft.commands.CommandSourceStack;
import com.mojang.brigadier.ResultConsumer;
import net.minecraft.util.StringUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.TextComponent;
import javax.annotation.Nullable;
import net.minecraft.network.chat.Component;
import java.text.SimpleDateFormat;
import net.minecraft.commands.CommandSource;

public abstract class BaseCommandBlock implements CommandSource {
    private static final SimpleDateFormat TIME_FORMAT;
    private static final Component DEFAULT_NAME;
    private long lastExecution;
    private boolean updateLastExecution;
    private int successCount;
    private boolean trackOutput;
    @Nullable
    private Component lastOutput;
    private String command;
    private Component name;
    
    public BaseCommandBlock() {
        this.lastExecution = -1L;
        this.updateLastExecution = true;
        this.trackOutput = true;
        this.command = "";
        this.name = BaseCommandBlock.DEFAULT_NAME;
    }
    
    public int getSuccessCount() {
        return this.successCount;
    }
    
    public void setSuccessCount(final int integer) {
        this.successCount = integer;
    }
    
    public Component getLastOutput() {
        return (this.lastOutput == null) ? TextComponent.EMPTY : this.lastOutput;
    }
    
    public CompoundTag save(final CompoundTag md) {
        md.putString("Command", this.command);
        md.putInt("SuccessCount", this.successCount);
        md.putString("CustomName", Component.Serializer.toJson(this.name));
        md.putBoolean("TrackOutput", this.trackOutput);
        if (this.lastOutput != null && this.trackOutput) {
            md.putString("LastOutput", Component.Serializer.toJson(this.lastOutput));
        }
        md.putBoolean("UpdateLastExecution", this.updateLastExecution);
        if (this.updateLastExecution && this.lastExecution > 0L) {
            md.putLong("LastExecution", this.lastExecution);
        }
        return md;
    }
    
    public void load(final CompoundTag md) {
        this.command = md.getString("Command");
        this.successCount = md.getInt("SuccessCount");
        if (md.contains("CustomName", 8)) {
            this.setName(Component.Serializer.fromJson(md.getString("CustomName")));
        }
        if (md.contains("TrackOutput", 1)) {
            this.trackOutput = md.getBoolean("TrackOutput");
        }
        if (md.contains("LastOutput", 8) && this.trackOutput) {
            try {
                this.lastOutput = Component.Serializer.fromJson(md.getString("LastOutput"));
            }
            catch (Throwable throwable3) {
                this.lastOutput = new TextComponent(throwable3.getMessage());
            }
        }
        else {
            this.lastOutput = null;
        }
        if (md.contains("UpdateLastExecution")) {
            this.updateLastExecution = md.getBoolean("UpdateLastExecution");
        }
        if (this.updateLastExecution && md.contains("LastExecution")) {
            this.lastExecution = md.getLong("LastExecution");
        }
        else {
            this.lastExecution = -1L;
        }
    }
    
    public void setCommand(final String string) {
        this.command = string;
        this.successCount = 0;
    }
    
    public String getCommand() {
        return this.command;
    }
    
    public boolean performCommand(final Level bru) {
        if (bru.isClientSide || bru.getGameTime() == this.lastExecution) {
            return false;
        }
        if ("Searge".equalsIgnoreCase(this.command)) {
            this.lastOutput = new TextComponent("#itzlipofutzli");
            this.successCount = 1;
            return true;
        }
        this.successCount = 0;
        final MinecraftServer minecraftServer3 = this.getLevel().getServer();
        if (minecraftServer3.isCommandBlockEnabled() && !StringUtil.isNullOrEmpty(this.command)) {
            try {
                this.lastOutput = null;
                final CommandSourceStack db4 = this.createCommandSourceStack().withCallback((ResultConsumer<CommandSourceStack>)((commandContext, boolean2, integer) -> {
                    if (boolean2) {
                        ++this.successCount;
                    }
                }));
                minecraftServer3.getCommands().performCommand(db4, this.command);
            }
            catch (Throwable throwable4) {
                final CrashReport l5 = CrashReport.forThrowable(throwable4, "Executing command block");
                final CrashReportCategory m6 = l5.addCategory("Command to be executed");
                m6.setDetail("Command", (CrashReportDetail<String>)this::getCommand);
                m6.setDetail("Name", (CrashReportDetail<String>)(() -> this.getName().getString()));
                throw new ReportedException(l5);
            }
        }
        if (this.updateLastExecution) {
            this.lastExecution = bru.getGameTime();
        }
        else {
            this.lastExecution = -1L;
        }
        return true;
    }
    
    public Component getName() {
        return this.name;
    }
    
    public void setName(@Nullable final Component nr) {
        if (nr != null) {
            this.name = nr;
        }
        else {
            this.name = BaseCommandBlock.DEFAULT_NAME;
        }
    }
    
    public void sendMessage(final Component nr, final UUID uUID) {
        if (this.trackOutput) {
            this.lastOutput = new TextComponent("[" + BaseCommandBlock.TIME_FORMAT.format(new Date()) + "] ").append(nr);
            this.onUpdated();
        }
    }
    
    public abstract ServerLevel getLevel();
    
    public abstract void onUpdated();
    
    public void setLastOutput(@Nullable final Component nr) {
        this.lastOutput = nr;
    }
    
    public void setTrackOutput(final boolean boolean1) {
        this.trackOutput = boolean1;
    }
    
    public boolean isTrackOutput() {
        return this.trackOutput;
    }
    
    public InteractionResult usedBy(final Player bft) {
        if (!bft.canUseGameMasterBlocks()) {
            return InteractionResult.PASS;
        }
        if (bft.getCommandSenderWorld().isClientSide) {
            bft.openMinecartCommandBlock(this);
        }
        return InteractionResult.sidedSuccess(bft.level.isClientSide);
    }
    
    public abstract Vec3 getPosition();
    
    public abstract CommandSourceStack createCommandSourceStack();
    
    public boolean acceptsSuccess() {
        return this.getLevel().getGameRules().getBoolean(GameRules.RULE_SENDCOMMANDFEEDBACK) && this.trackOutput;
    }
    
    public boolean acceptsFailure() {
        return this.trackOutput;
    }
    
    public boolean shouldInformAdmins() {
        return this.getLevel().getGameRules().getBoolean(GameRules.RULE_COMMANDBLOCKOUTPUT);
    }
    
    static {
        TIME_FORMAT = new SimpleDateFormat("HH:mm:ss");
        DEFAULT_NAME = new TextComponent("@");
    }
}

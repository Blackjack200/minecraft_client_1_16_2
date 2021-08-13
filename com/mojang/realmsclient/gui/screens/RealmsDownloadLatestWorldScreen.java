package com.mojang.realmsclient.gui.screens;

import org.apache.logging.log4j.LogManager;
import java.util.concurrent.TimeUnit;
import net.minecraft.Util;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.systems.RenderSystem;
import java.util.Locale;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.client.gui.GuiComponent;
import com.mojang.blaze3d.vertex.PoseStack;
import java.util.List;
import net.minecraft.realms.NarrationHelper;
import java.util.stream.Collectors;
import net.minecraft.network.chat.TextComponent;
import com.google.common.collect.Lists;
import com.mojang.realmsclient.client.FileDownload;
import com.mojang.realmsclient.Unit;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.TranslatableComponent;
import it.unimi.dsi.fastutil.booleans.BooleanConsumer;
import net.minecraft.client.gui.components.Button;
import com.google.common.util.concurrent.RateLimiter;
import net.minecraft.network.chat.Component;
import com.mojang.realmsclient.dto.WorldDownload;
import net.minecraft.client.gui.screens.Screen;
import java.util.concurrent.locks.ReentrantLock;
import org.apache.logging.log4j.Logger;
import net.minecraft.realms.RealmsScreen;

public class RealmsDownloadLatestWorldScreen extends RealmsScreen {
    private static final Logger LOGGER;
    private static final ReentrantLock DOWNLOAD_LOCK;
    private final Screen lastScreen;
    private final WorldDownload worldDownload;
    private final Component downloadTitle;
    private final RateLimiter narrationRateLimiter;
    private Button cancelButton;
    private final String worldName;
    private final DownloadStatus downloadStatus;
    private volatile Component errorMessage;
    private volatile Component status;
    private volatile String progress;
    private volatile boolean cancelled;
    private volatile boolean showDots;
    private volatile boolean finished;
    private volatile boolean extracting;
    private Long previousWrittenBytes;
    private Long previousTimeSnapshot;
    private long bytesPersSecond;
    private int animTick;
    private static final String[] DOTS;
    private int dotIndex;
    private boolean checked;
    private final BooleanConsumer callback;
    
    public RealmsDownloadLatestWorldScreen(final Screen doq, final WorldDownload dha, final String string, final BooleanConsumer booleanConsumer) {
        this.status = new TranslatableComponent("mco.download.preparing");
        this.showDots = true;
        this.callback = booleanConsumer;
        this.lastScreen = doq;
        this.worldName = string;
        this.worldDownload = dha;
        this.downloadStatus = new DownloadStatus();
        this.downloadTitle = new TranslatableComponent("mco.download.title");
        this.narrationRateLimiter = RateLimiter.create(0.10000000149011612);
    }
    
    public void init() {
        this.minecraft.keyboardHandler.setSendRepeatsToGui(true);
        this.cancelButton = this.<Button>addButton(new Button(this.width / 2 - 100, this.height - 42, 200, 20, CommonComponents.GUI_CANCEL, dlg -> {
            this.cancelled = true;
            this.backButtonClicked();
            return;
        }));
        this.checkDownloadSize();
    }
    
    private void checkDownloadSize() {
        if (this.finished) {
            return;
        }
        if (!this.checked && this.getContentLength(this.worldDownload.downloadLink) >= 5368709120L) {
            final Component nr2 = new TranslatableComponent("mco.download.confirmation.line1", new Object[] { Unit.humanReadable(5368709120L) });
            final Component nr3 = new TranslatableComponent("mco.download.confirmation.line2");
            this.minecraft.setScreen(new RealmsLongConfirmationScreen(boolean1 -> {
                this.checked = true;
                this.minecraft.setScreen(this);
                this.downloadSave();
            }, RealmsLongConfirmationScreen.Type.Warning, nr2, nr3, false));
        }
        else {
            this.downloadSave();
        }
    }
    
    private long getContentLength(final String string) {
        final FileDownload dfv3 = new FileDownload();
        return dfv3.contentLength(string);
    }
    
    @Override
    public void tick() {
        super.tick();
        ++this.animTick;
        if (this.status != null && this.narrationRateLimiter.tryAcquire(1)) {
            final List<Component> list2 = (List<Component>)Lists.newArrayList();
            list2.add(this.downloadTitle);
            list2.add(this.status);
            if (this.progress != null) {
                list2.add(new TextComponent(this.progress + "%"));
                list2.add(new TextComponent(Unit.humanReadable(this.bytesPersSecond) + "/s"));
            }
            if (this.errorMessage != null) {
                list2.add(this.errorMessage);
            }
            final String string3 = (String)list2.stream().map(Component::getString).collect(Collectors.joining("\n"));
            NarrationHelper.now(string3);
        }
    }
    
    @Override
    public boolean keyPressed(final int integer1, final int integer2, final int integer3) {
        if (integer1 == 256) {
            this.cancelled = true;
            this.backButtonClicked();
            return true;
        }
        return super.keyPressed(integer1, integer2, integer3);
    }
    
    private void backButtonClicked() {
        if (this.finished && this.callback != null && this.errorMessage == null) {
            this.callback.accept(true);
        }
        this.minecraft.setScreen(this.lastScreen);
    }
    
    @Override
    public void render(final PoseStack dfj, final int integer2, final int integer3, final float float4) {
        this.renderBackground(dfj);
        GuiComponent.drawCenteredString(dfj, this.font, this.downloadTitle, this.width / 2, 20, 16777215);
        GuiComponent.drawCenteredString(dfj, this.font, this.status, this.width / 2, 50, 16777215);
        if (this.showDots) {
            this.drawDots(dfj);
        }
        if (this.downloadStatus.bytesWritten != 0L && !this.cancelled) {
            this.drawProgressBar(dfj);
            this.drawDownloadSpeed(dfj);
        }
        if (this.errorMessage != null) {
            GuiComponent.drawCenteredString(dfj, this.font, this.errorMessage, this.width / 2, 110, 16711680);
        }
        super.render(dfj, integer2, integer3, float4);
    }
    
    private void drawDots(final PoseStack dfj) {
        final int integer3 = this.font.width(this.status);
        if (this.animTick % 10 == 0) {
            ++this.dotIndex;
        }
        this.font.draw(dfj, RealmsDownloadLatestWorldScreen.DOTS[this.dotIndex % RealmsDownloadLatestWorldScreen.DOTS.length], (float)(this.width / 2 + integer3 / 2 + 5), 50.0f, 16777215);
    }
    
    private void drawProgressBar(final PoseStack dfj) {
        final double double3 = Math.min(this.downloadStatus.bytesWritten / (double)this.downloadStatus.totalBytes, 1.0);
        this.progress = String.format(Locale.ROOT, "%.1f", new Object[] { double3 * 100.0 });
        RenderSystem.color4f(1.0f, 1.0f, 1.0f, 1.0f);
        RenderSystem.disableTexture();
        final Tesselator dfl5 = Tesselator.getInstance();
        final BufferBuilder dfe6 = dfl5.getBuilder();
        dfe6.begin(7, DefaultVertexFormat.POSITION_COLOR);
        final double double4 = this.width / 2 - 100;
        final double double5 = 0.5;
        dfe6.vertex(double4 - 0.5, 95.5, 0.0).color(217, 210, 210, 255).endVertex();
        dfe6.vertex(double4 + 200.0 * double3 + 0.5, 95.5, 0.0).color(217, 210, 210, 255).endVertex();
        dfe6.vertex(double4 + 200.0 * double3 + 0.5, 79.5, 0.0).color(217, 210, 210, 255).endVertex();
        dfe6.vertex(double4 - 0.5, 79.5, 0.0).color(217, 210, 210, 255).endVertex();
        dfe6.vertex(double4, 95.0, 0.0).color(128, 128, 128, 255).endVertex();
        dfe6.vertex(double4 + 200.0 * double3, 95.0, 0.0).color(128, 128, 128, 255).endVertex();
        dfe6.vertex(double4 + 200.0 * double3, 80.0, 0.0).color(128, 128, 128, 255).endVertex();
        dfe6.vertex(double4, 80.0, 0.0).color(128, 128, 128, 255).endVertex();
        dfl5.end();
        RenderSystem.enableTexture();
        GuiComponent.drawCenteredString(dfj, this.font, this.progress + " %", this.width / 2, 84, 16777215);
    }
    
    private void drawDownloadSpeed(final PoseStack dfj) {
        if (this.animTick % 20 == 0) {
            if (this.previousWrittenBytes != null) {
                long long3 = Util.getMillis() - this.previousTimeSnapshot;
                if (long3 == 0L) {
                    long3 = 1L;
                }
                this.drawDownloadSpeed0(dfj, this.bytesPersSecond = 1000L * (this.downloadStatus.bytesWritten - this.previousWrittenBytes) / long3);
            }
            this.previousWrittenBytes = this.downloadStatus.bytesWritten;
            this.previousTimeSnapshot = Util.getMillis();
        }
        else {
            this.drawDownloadSpeed0(dfj, this.bytesPersSecond);
        }
    }
    
    private void drawDownloadSpeed0(final PoseStack dfj, final long long2) {
        if (long2 > 0L) {
            final int integer5 = this.font.width(this.progress);
            final String string6 = "(" + Unit.humanReadable(long2) + "/s)";
            this.font.draw(dfj, string6, (float)(this.width / 2 + integer5 / 2 + 15), 84.0f, 16777215);
        }
    }
    
    private void downloadSave() {
        new Thread(() -> {
            try {
                if (!RealmsDownloadLatestWorldScreen.DOWNLOAD_LOCK.tryLock(1L, TimeUnit.SECONDS)) {
                    this.status = new TranslatableComponent("mco.download.failed");
                    return;
                }
                if (this.cancelled) {
                    this.downloadCancelled();
                    return;
                }
                this.status = new TranslatableComponent("mco.download.downloading", new Object[] { this.worldName });
                final FileDownload dfv2 = new FileDownload();
                dfv2.contentLength(this.worldDownload.downloadLink);
                dfv2.download(this.worldDownload, this.worldName, this.downloadStatus, this.minecraft.getLevelSource());
                while (!dfv2.isFinished()) {
                    if (dfv2.isError()) {
                        dfv2.cancel();
                        this.errorMessage = new TranslatableComponent("mco.download.failed");
                        this.cancelButton.setMessage(CommonComponents.GUI_DONE);
                        return;
                    }
                    if (dfv2.isExtracting()) {
                        if (!this.extracting) {
                            this.status = new TranslatableComponent("mco.download.extracting");
                        }
                        this.extracting = true;
                    }
                    if (this.cancelled) {
                        dfv2.cancel();
                        this.downloadCancelled();
                        return;
                    }
                    try {
                        Thread.sleep(500L);
                    }
                    catch (InterruptedException interruptedException3) {
                        RealmsDownloadLatestWorldScreen.LOGGER.error("Failed to check Realms backup download status");
                    }
                }
                this.finished = true;
                this.status = new TranslatableComponent("mco.download.done");
                this.cancelButton.setMessage(CommonComponents.GUI_DONE);
            }
            catch (InterruptedException interruptedException4) {
                RealmsDownloadLatestWorldScreen.LOGGER.error("Could not acquire upload lock");
            }
            catch (Exception exception2) {
                this.errorMessage = new TranslatableComponent("mco.download.failed");
                exception2.printStackTrace();
            }
            finally {
                if (!RealmsDownloadLatestWorldScreen.DOWNLOAD_LOCK.isHeldByCurrentThread()) {
                    return;
                }
                RealmsDownloadLatestWorldScreen.DOWNLOAD_LOCK.unlock();
                this.showDots = false;
                this.finished = true;
            }
        }).start();
    }
    
    private void downloadCancelled() {
        this.status = new TranslatableComponent("mco.download.cancelled");
    }
    
    static {
        LOGGER = LogManager.getLogger();
        DOWNLOAD_LOCK = new ReentrantLock();
        DOTS = new String[] { "", ".", ". .", ". . ." };
    }
    
    public class DownloadStatus {
        public volatile long bytesWritten;
        public volatile long totalBytes;
    }
}

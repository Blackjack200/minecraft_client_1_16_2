package com.mojang.realmsclient.gui.screens;

import org.apache.logging.log4j.LogManager;
import com.mojang.realmsclient.dto.UploadInfo;
import com.mojang.realmsclient.exception.RealmsServiceException;
import java.util.function.Consumer;
import com.mojang.realmsclient.client.FileUpload;
import net.minecraft.SharedConstants;
import com.mojang.realmsclient.exception.RetryCallException;
import com.mojang.realmsclient.util.UploadTokenCache;
import java.util.concurrent.TimeUnit;
import com.mojang.realmsclient.client.RealmsClient;
import java.io.InputStream;
import org.apache.commons.compress.utils.IOUtils;
import java.io.FileInputStream;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import java.io.IOException;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import java.io.OutputStream;
import java.util.zip.GZIPOutputStream;
import java.io.FileOutputStream;
import java.io.File;
import java.util.List;
import net.minecraft.realms.NarrationHelper;
import java.util.stream.Stream;
import com.google.common.collect.Lists;
import com.mojang.realmsclient.Unit;
import net.minecraft.Util;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.systems.RenderSystem;
import java.util.Locale;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.client.gui.GuiComponent;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.client.gui.components.Button;
import com.google.common.util.concurrent.RateLimiter;
import com.mojang.realmsclient.client.UploadStatus;
import net.minecraft.world.level.storage.LevelSummary;
import net.minecraft.network.chat.Component;
import java.util.concurrent.locks.ReentrantLock;
import org.apache.logging.log4j.Logger;
import net.minecraft.realms.RealmsScreen;

public class RealmsUploadScreen extends RealmsScreen {
    private static final Logger LOGGER;
    private static final ReentrantLock UPLOAD_LOCK;
    private static final String[] DOTS;
    private static final Component VERIFYING_TEXT;
    private final RealmsResetWorldScreen lastScreen;
    private final LevelSummary selectedLevel;
    private final long worldId;
    private final int slotId;
    private final UploadStatus uploadStatus;
    private final RateLimiter narrationRateLimiter;
    private volatile Component[] errorMessage;
    private volatile Component status;
    private volatile String progress;
    private volatile boolean cancelled;
    private volatile boolean uploadFinished;
    private volatile boolean showDots;
    private volatile boolean uploadStarted;
    private Button backButton;
    private Button cancelButton;
    private int tickCount;
    private Long previousWrittenBytes;
    private Long previousTimeSnapshot;
    private long bytesPersSecond;
    private final Runnable callback;
    
    public RealmsUploadScreen(final long long1, final int integer, final RealmsResetWorldScreen dic, final LevelSummary cye, final Runnable runnable) {
        this.status = new TranslatableComponent("mco.upload.preparing");
        this.showDots = true;
        this.worldId = long1;
        this.slotId = integer;
        this.lastScreen = dic;
        this.selectedLevel = cye;
        this.uploadStatus = new UploadStatus();
        this.narrationRateLimiter = RateLimiter.create(0.10000000149011612);
        this.callback = runnable;
    }
    
    public void init() {
        this.minecraft.keyboardHandler.setSendRepeatsToGui(true);
        this.backButton = this.<Button>addButton(new Button(this.width / 2 - 100, this.height - 42, 200, 20, CommonComponents.GUI_BACK, dlg -> this.onBack()));
        this.backButton.visible = false;
        this.cancelButton = this.<Button>addButton(new Button(this.width / 2 - 100, this.height - 42, 200, 20, CommonComponents.GUI_CANCEL, dlg -> this.onCancel()));
        if (!this.uploadStarted) {
            if (this.lastScreen.slot == -1) {
                this.upload();
            }
            else {
                this.lastScreen.switchSlot(() -> {
                    if (!this.uploadStarted) {
                        this.uploadStarted = true;
                        this.minecraft.setScreen(this);
                        this.upload();
                    }
                });
            }
        }
    }
    
    @Override
    public void removed() {
        this.minecraft.keyboardHandler.setSendRepeatsToGui(false);
    }
    
    private void onBack() {
        this.callback.run();
    }
    
    private void onCancel() {
        this.cancelled = true;
        this.minecraft.setScreen(this.lastScreen);
    }
    
    @Override
    public boolean keyPressed(final int integer1, final int integer2, final int integer3) {
        if (integer1 == 256) {
            if (this.showDots) {
                this.onCancel();
            }
            else {
                this.onBack();
            }
            return true;
        }
        return super.keyPressed(integer1, integer2, integer3);
    }
    
    @Override
    public void render(final PoseStack dfj, final int integer2, final int integer3, final float float4) {
        this.renderBackground(dfj);
        if (!this.uploadFinished && this.uploadStatus.bytesWritten != 0L && this.uploadStatus.bytesWritten == this.uploadStatus.totalBytes) {
            this.status = RealmsUploadScreen.VERIFYING_TEXT;
            this.cancelButton.active = false;
        }
        GuiComponent.drawCenteredString(dfj, this.font, this.status, this.width / 2, 50, 16777215);
        if (this.showDots) {
            this.drawDots(dfj);
        }
        if (this.uploadStatus.bytesWritten != 0L && !this.cancelled) {
            this.drawProgressBar(dfj);
            this.drawUploadSpeed(dfj);
        }
        if (this.errorMessage != null) {
            for (int integer4 = 0; integer4 < this.errorMessage.length; ++integer4) {
                GuiComponent.drawCenteredString(dfj, this.font, this.errorMessage[integer4], this.width / 2, 110 + 12 * integer4, 16711680);
            }
        }
        super.render(dfj, integer2, integer3, float4);
    }
    
    private void drawDots(final PoseStack dfj) {
        final int integer3 = this.font.width(this.status);
        this.font.draw(dfj, RealmsUploadScreen.DOTS[this.tickCount / 10 % RealmsUploadScreen.DOTS.length], (float)(this.width / 2 + integer3 / 2 + 5), 50.0f, 16777215);
    }
    
    private void drawProgressBar(final PoseStack dfj) {
        final double double3 = Math.min(this.uploadStatus.bytesWritten / (double)this.uploadStatus.totalBytes, 1.0);
        this.progress = String.format(Locale.ROOT, "%.1f", new Object[] { double3 * 100.0 });
        RenderSystem.color4f(1.0f, 1.0f, 1.0f, 1.0f);
        RenderSystem.disableTexture();
        final double double4 = this.width / 2 - 100;
        final double double5 = 0.5;
        final Tesselator dfl9 = Tesselator.getInstance();
        final BufferBuilder dfe10 = dfl9.getBuilder();
        dfe10.begin(7, DefaultVertexFormat.POSITION_COLOR);
        dfe10.vertex(double4 - 0.5, 95.5, 0.0).color(217, 210, 210, 255).endVertex();
        dfe10.vertex(double4 + 200.0 * double3 + 0.5, 95.5, 0.0).color(217, 210, 210, 255).endVertex();
        dfe10.vertex(double4 + 200.0 * double3 + 0.5, 79.5, 0.0).color(217, 210, 210, 255).endVertex();
        dfe10.vertex(double4 - 0.5, 79.5, 0.0).color(217, 210, 210, 255).endVertex();
        dfe10.vertex(double4, 95.0, 0.0).color(128, 128, 128, 255).endVertex();
        dfe10.vertex(double4 + 200.0 * double3, 95.0, 0.0).color(128, 128, 128, 255).endVertex();
        dfe10.vertex(double4 + 200.0 * double3, 80.0, 0.0).color(128, 128, 128, 255).endVertex();
        dfe10.vertex(double4, 80.0, 0.0).color(128, 128, 128, 255).endVertex();
        dfl9.end();
        RenderSystem.enableTexture();
        GuiComponent.drawCenteredString(dfj, this.font, this.progress + " %", this.width / 2, 84, 16777215);
    }
    
    private void drawUploadSpeed(final PoseStack dfj) {
        if (this.tickCount % 20 == 0) {
            if (this.previousWrittenBytes != null) {
                long long3 = Util.getMillis() - this.previousTimeSnapshot;
                if (long3 == 0L) {
                    long3 = 1L;
                }
                this.drawUploadSpeed0(dfj, this.bytesPersSecond = 1000L * (this.uploadStatus.bytesWritten - this.previousWrittenBytes) / long3);
            }
            this.previousWrittenBytes = this.uploadStatus.bytesWritten;
            this.previousTimeSnapshot = Util.getMillis();
        }
        else {
            this.drawUploadSpeed0(dfj, this.bytesPersSecond);
        }
    }
    
    private void drawUploadSpeed0(final PoseStack dfj, final long long2) {
        if (long2 > 0L) {
            final int integer5 = this.font.width(this.progress);
            final String string6 = "(" + Unit.humanReadable(long2) + "/s)";
            this.font.draw(dfj, string6, (float)(this.width / 2 + integer5 / 2 + 15), 84.0f, 16777215);
        }
    }
    
    @Override
    public void tick() {
        super.tick();
        ++this.tickCount;
        if (this.status != null && this.narrationRateLimiter.tryAcquire(1)) {
            final List<String> list2 = (List<String>)Lists.newArrayList();
            list2.add(this.status.getString());
            if (this.progress != null) {
                list2.add((this.progress + "%"));
            }
            if (this.errorMessage != null) {
                Stream.of((Object[])this.errorMessage).map(Component::getString).forEach(list2::add);
            }
            NarrationHelper.now(String.join((CharSequence)System.lineSeparator(), (Iterable)list2));
        }
    }
    
    private void upload() {
        this.uploadStarted = true;
        new Thread(() -> {
            File file2 = null;
            final RealmsClient dfy3 = RealmsClient.create();
            final long long4 = this.worldId;
            try {
                if (!RealmsUploadScreen.UPLOAD_LOCK.tryLock(1L, TimeUnit.SECONDS)) {
                    this.status = new TranslatableComponent("mco.upload.close.failure");
                    return;
                }
                UploadInfo dgy6 = null;
                for (int integer7 = 0; integer7 < 20; ++integer7) {
                    try {
                        if (this.cancelled) {
                            this.uploadCancelled();
                            return;
                        }
                        dgy6 = dfy3.requestUploadInfo(long4, UploadTokenCache.get(long4));
                        if (dgy6 != null) {
                            break;
                        }
                    }
                    catch (RetryCallException dhg8) {
                        Thread.sleep((long)(dhg8.delaySeconds * 1000));
                    }
                }
                if (dgy6 == null) {
                    this.status = new TranslatableComponent("mco.upload.close.failure");
                    return;
                }
                UploadTokenCache.put(long4, dgy6.getToken());
                if (!dgy6.isWorldClosed()) {
                    this.status = new TranslatableComponent("mco.upload.close.failure");
                    return;
                }
                if (this.cancelled) {
                    this.uploadCancelled();
                    return;
                }
                final File file3 = new File(this.minecraft.gameDirectory.getAbsolutePath(), "saves");
                file2 = this.tarGzipArchive(new File(file3, this.selectedLevel.getLevelId()));
                if (this.cancelled) {
                    this.uploadCancelled();
                    return;
                }
                if (this.verify(file2)) {
                    this.status = new TranslatableComponent("mco.upload.uploading", new Object[] { this.selectedLevel.getLevelName() });
                    final FileUpload dfw8 = new FileUpload(file2, this.worldId, this.slotId, dgy6, this.minecraft.getUser(), SharedConstants.getCurrentVersion().getName(), this.uploadStatus);
                    dfw8.upload((Consumer<UploadResult>)(dil -> {
                        if (dil.statusCode >= 200 && dil.statusCode < 300) {
                            this.uploadFinished = true;
                            this.status = new TranslatableComponent("mco.upload.done");
                            this.backButton.setMessage(CommonComponents.GUI_DONE);
                            UploadTokenCache.invalidate(long4);
                        }
                        else if (dil.statusCode == 400 && dil.errorMessage != null) {
                            this.setErrorMessage(new TranslatableComponent("mco.upload.failed", new Object[] { dil.errorMessage }));
                        }
                        else {
                            this.setErrorMessage(new TranslatableComponent("mco.upload.failed", new Object[] { dil.statusCode }));
                        }
                    }));
                    while (!dfw8.isFinished()) {
                        if (this.cancelled) {
                            dfw8.cancel();
                            this.uploadCancelled();
                            return;
                        }
                        try {
                            Thread.sleep(500L);
                        }
                        catch (InterruptedException interruptedException9) {
                            RealmsUploadScreen.LOGGER.error("Failed to check Realms file upload status");
                        }
                    }
                    return;
                }
                final long long5 = file2.length();
                final Unit dfu10 = Unit.getLargest(long5);
                final Unit dfu11 = Unit.getLargest(5368709120L);
                if (Unit.humanReadable(long5, dfu10).equals(Unit.humanReadable(5368709120L, dfu11)) && dfu10 != Unit.B) {
                    final Unit dfu12 = Unit.values()[dfu10.ordinal() - 1];
                    this.setErrorMessage(new TranslatableComponent("mco.upload.size.failure.line1", new Object[] { this.selectedLevel.getLevelName() }), new TranslatableComponent("mco.upload.size.failure.line2", new Object[] { Unit.humanReadable(long5, dfu12), Unit.humanReadable(5368709120L, dfu12) }));
                    return;
                }
                this.setErrorMessage(new TranslatableComponent("mco.upload.size.failure.line1", new Object[] { this.selectedLevel.getLevelName() }), new TranslatableComponent("mco.upload.size.failure.line2", new Object[] { Unit.humanReadable(long5, dfu10), Unit.humanReadable(5368709120L, dfu11) }));
            }
            catch (IOException iOException6) {
                this.setErrorMessage(new TranslatableComponent("mco.upload.failed", new Object[] { iOException6.getMessage() }));
            }
            catch (RealmsServiceException dhf6) {
                this.setErrorMessage(new TranslatableComponent("mco.upload.failed", new Object[] { dhf6.toString() }));
            }
            catch (InterruptedException interruptedException10) {
                RealmsUploadScreen.LOGGER.error("Could not acquire upload lock");
            }
            finally {
                this.uploadFinished = true;
                if (!RealmsUploadScreen.UPLOAD_LOCK.isHeldByCurrentThread()) {
                    return;
                }
                RealmsUploadScreen.UPLOAD_LOCK.unlock();
                this.showDots = false;
                this.backButton.visible = true;
                this.cancelButton.visible = false;
                if (file2 != null) {
                    RealmsUploadScreen.LOGGER.debug("Deleting file " + file2.getAbsolutePath());
                    file2.delete();
                }
            }
        }).start();
    }
    
    private void setErrorMessage(final Component... arr) {
        this.errorMessage = arr;
    }
    
    private void uploadCancelled() {
        this.status = new TranslatableComponent("mco.upload.cancelled");
        RealmsUploadScreen.LOGGER.debug("Upload was cancelled");
    }
    
    private boolean verify(final File file) {
        return file.length() < 5368709120L;
    }
    
    private File tarGzipArchive(final File file) throws IOException {
        TarArchiveOutputStream tarArchiveOutputStream3 = null;
        try {
            final File file2 = File.createTempFile("realms-upload-file", ".tar.gz");
            tarArchiveOutputStream3 = new TarArchiveOutputStream((OutputStream)new GZIPOutputStream((OutputStream)new FileOutputStream(file2)));
            tarArchiveOutputStream3.setLongFileMode(3);
            this.addFileToTarGz(tarArchiveOutputStream3, file.getAbsolutePath(), "world", true);
            tarArchiveOutputStream3.finish();
            return file2;
        }
        finally {
            if (tarArchiveOutputStream3 != null) {
                tarArchiveOutputStream3.close();
            }
        }
    }
    
    private void addFileToTarGz(final TarArchiveOutputStream tarArchiveOutputStream, final String string2, final String string3, final boolean boolean4) throws IOException {
        if (this.cancelled) {
            return;
        }
        final File file6 = new File(string2);
        final String string4 = boolean4 ? string3 : (string3 + file6.getName());
        final TarArchiveEntry tarArchiveEntry8 = new TarArchiveEntry(file6, string4);
        tarArchiveOutputStream.putArchiveEntry((ArchiveEntry)tarArchiveEntry8);
        if (file6.isFile()) {
            IOUtils.copy((InputStream)new FileInputStream(file6), (OutputStream)tarArchiveOutputStream);
            tarArchiveOutputStream.closeArchiveEntry();
        }
        else {
            tarArchiveOutputStream.closeArchiveEntry();
            final File[] arr9 = file6.listFiles();
            if (arr9 != null) {
                for (final File file7 : arr9) {
                    this.addFileToTarGz(tarArchiveOutputStream, file7.getAbsolutePath(), string4 + "/", false);
                }
            }
        }
    }
    
    static {
        LOGGER = LogManager.getLogger();
        UPLOAD_LOCK = new ReentrantLock();
        DOTS = new String[] { "", ".", ". .", ". . ." };
        VERIFYING_TEXT = new TranslatableComponent("mco.upload.verifying");
    }
}

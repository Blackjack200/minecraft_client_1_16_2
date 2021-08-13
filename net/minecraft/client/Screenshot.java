package net.minecraft.client;

import java.text.SimpleDateFormat;
import org.apache.logging.log4j.LogManager;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.network.chat.Style;
import java.util.function.UnaryOperator;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.TextComponent;
import java.util.Date;
import com.mojang.blaze3d.platform.NativeImage;
import net.minecraft.Util;
import com.mojang.blaze3d.systems.RenderSystem;
import javax.annotation.Nullable;
import net.minecraft.network.chat.Component;
import java.util.function.Consumer;
import com.mojang.blaze3d.pipeline.RenderTarget;
import java.io.File;
import java.text.DateFormat;
import org.apache.logging.log4j.Logger;

public class Screenshot {
    private static final Logger LOGGER;
    private static final DateFormat DATE_FORMAT;
    
    public static void grab(final File file, final int integer2, final int integer3, final RenderTarget ded, final Consumer<Component> consumer) {
        grab(file, null, integer2, integer3, ded, consumer);
    }
    
    public static void grab(final File file, @Nullable final String string, final int integer3, final int integer4, final RenderTarget ded, final Consumer<Component> consumer) {
        if (!RenderSystem.isOnRenderThread()) {
            RenderSystem.recordRenderCall(() -> _grab(file, string, integer3, integer4, ded, consumer));
        }
        else {
            _grab(file, string, integer3, integer4, ded, consumer);
        }
    }
    
    private static void _grab(final File file, @Nullable final String string, final int integer3, final int integer4, final RenderTarget ded, final Consumer<Component> consumer) {
        final NativeImage deq7 = takeScreenshot(integer3, integer4, ded);
        final File file2 = new File(file, "screenshots");
        file2.mkdir();
        File file3;
        if (string == null) {
            file3 = getFile(file2);
        }
        else {
            file3 = new File(file2, string);
        }
        Util.ioPool().execute(() -> {
            try {
                deq7.writeToFile(file3);
                final Component nr4 = new TextComponent(file3.getName()).withStyle(ChatFormatting.UNDERLINE).withStyle((UnaryOperator<Style>)(ob -> ob.withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_FILE, file3.getAbsolutePath()))));
                consumer.accept(new TranslatableComponent("screenshot.success", new Object[] { nr4 }));
            }
            catch (Exception exception4) {
                Screenshot.LOGGER.warn("Couldn't save screenshot", (Throwable)exception4);
                consumer.accept(new TranslatableComponent("screenshot.failure", new Object[] { exception4.getMessage() }));
            }
            finally {
                deq7.close();
            }
        });
    }
    
    public static NativeImage takeScreenshot(int integer1, int integer2, final RenderTarget ded) {
        integer1 = ded.width;
        integer2 = ded.height;
        final NativeImage deq4 = new NativeImage(integer1, integer2, false);
        RenderSystem.bindTexture(ded.getColorTextureId());
        deq4.downloadTexture(0, true);
        deq4.flipY();
        return deq4;
    }
    
    private static File getFile(final File file) {
        final String string2 = Screenshot.DATE_FORMAT.format(new Date());
        int integer3 = 1;
        File file2;
        while (true) {
            file2 = new File(file, string2 + ((integer3 == 1) ? "" : new StringBuilder().append("_").append(integer3).toString()) + ".png");
            if (!file2.exists()) {
                break;
            }
            ++integer3;
        }
        return file2;
    }
    
    static {
        LOGGER = LogManager.getLogger();
        DATE_FORMAT = (DateFormat)new SimpleDateFormat("yyyy-MM-dd_HH.mm.ss");
    }
}

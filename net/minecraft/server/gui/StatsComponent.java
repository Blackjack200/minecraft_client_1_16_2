package net.minecraft.server.gui;

import java.util.function.Consumer;
import net.minecraft.Util;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import java.awt.event.ActionEvent;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Dimension;
import javax.swing.Timer;
import net.minecraft.server.MinecraftServer;
import java.text.DecimalFormat;
import javax.swing.JComponent;

public class StatsComponent extends JComponent {
    private static final DecimalFormat DECIMAL_FORMAT;
    private final int[] values;
    private int vp;
    private final String[] msgs;
    private final MinecraftServer server;
    private final Timer timer;
    
    public StatsComponent(final MinecraftServer minecraftServer) {
        this.values = new int[256];
        this.msgs = new String[11];
        this.server = minecraftServer;
        this.setPreferredSize(new Dimension(456, 246));
        this.setMinimumSize(new Dimension(456, 246));
        this.setMaximumSize(new Dimension(456, 246));
        (this.timer = new Timer(500, actionEvent -> this.tick())).start();
        this.setBackground(Color.BLACK);
    }
    
    private void tick() {
        final long long2 = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        this.msgs[0] = new StringBuilder().append("Memory use: ").append(long2 / 1024L / 1024L).append(" mb (").append(Runtime.getRuntime().freeMemory() * 100L / Runtime.getRuntime().maxMemory()).append("% free)").toString();
        this.msgs[1] = "Avg tick: " + StatsComponent.DECIMAL_FORMAT.format(this.getAverage(this.server.tickTimes) * 1.0E-6) + " ms";
        this.values[this.vp++ & 0xFF] = (int)(long2 * 100L / Runtime.getRuntime().maxMemory());
        this.repaint();
    }
    
    private double getAverage(final long[] arr) {
        long long3 = 0L;
        for (final long long4 : arr) {
            long3 += long4;
        }
        return long3 / (double)arr.length;
    }
    
    public void paint(final Graphics graphics) {
        graphics.setColor(new Color(16777215));
        graphics.fillRect(0, 0, 456, 246);
        for (int integer3 = 0; integer3 < 256; ++integer3) {
            final int integer4 = this.values[integer3 + this.vp & 0xFF];
            graphics.setColor(new Color(integer4 + 28 << 16));
            graphics.fillRect(integer3, 100 - integer4, 1, integer4);
        }
        graphics.setColor(Color.BLACK);
        for (int integer3 = 0; integer3 < this.msgs.length; ++integer3) {
            final String string4 = this.msgs[integer3];
            if (string4 != null) {
                graphics.drawString(string4, 32, 116 + integer3 * 16);
            }
        }
    }
    
    public void close() {
        this.timer.stop();
    }
    
    static {
        DECIMAL_FORMAT = Util.<DecimalFormat>make(new DecimalFormat("########0.000"), (java.util.function.Consumer<DecimalFormat>)(decimalFormat -> decimalFormat.setDecimalFormatSymbols(DecimalFormatSymbols.getInstance(Locale.ROOT))));
    }
}

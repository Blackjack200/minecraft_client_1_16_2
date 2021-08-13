package net.minecraft.server.gui;

import org.apache.logging.log4j.LogManager;
import java.awt.event.ActionEvent;
import com.mojang.util.QueueLogAppender;
import javax.swing.JScrollBar;
import javax.swing.text.Document;
import javax.swing.text.BadLocationException;
import javax.swing.text.AttributeSet;
import javax.swing.SwingUtilities;
import net.minecraft.DefaultUncaughtExceptionHandler;
import java.awt.event.FocusListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusAdapter;
import javax.swing.JTextField;
import javax.swing.JTextArea;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.border.EtchedBorder;
import net.minecraft.server.MinecraftServer;
import javax.swing.JPanel;
import java.awt.LayoutManager;
import java.awt.BorderLayout;
import java.awt.Dimension;
import com.google.common.collect.Lists;
import java.awt.event.WindowListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowAdapter;
import java.awt.Component;
import javax.swing.JFrame;
import javax.swing.UIManager;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.Collection;
import net.minecraft.server.dedicated.DedicatedServer;
import org.apache.logging.log4j.Logger;
import java.awt.Font;
import javax.swing.JComponent;

public class MinecraftServerGui extends JComponent {
    private static final Font MONOSPACED;
    private static final Logger LOGGER;
    private final DedicatedServer server;
    private Thread logAppenderThread;
    private final Collection<Runnable> finalizers;
    private final AtomicBoolean isClosing;
    
    public static MinecraftServerGui showFrameFor(final DedicatedServer zg) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }
        catch (Exception ex) {}
        final JFrame jFrame2 = new JFrame("Minecraft server");
        final MinecraftServerGui zm3 = new MinecraftServerGui(zg);
        jFrame2.setDefaultCloseOperation(2);
        jFrame2.add((Component)zm3);
        jFrame2.pack();
        jFrame2.setLocationRelativeTo((Component)null);
        jFrame2.setVisible(true);
        jFrame2.addWindowListener((WindowListener)new WindowAdapter() {
            public void windowClosing(final WindowEvent windowEvent) {
                if (!zm3.isClosing.getAndSet(true)) {
                    jFrame2.setTitle("Minecraft server - shutting down!");
                    zg.halt(true);
                    MinecraftServerGui.this.runFinalizers();
                }
            }
        });
        zm3.addFinalizer(jFrame2::dispose);
        zm3.start();
        return zm3;
    }
    
    private MinecraftServerGui(final DedicatedServer zg) {
        this.finalizers = (Collection<Runnable>)Lists.newArrayList();
        this.isClosing = new AtomicBoolean();
        this.server = zg;
        this.setPreferredSize(new Dimension(854, 480));
        this.setLayout((LayoutManager)new BorderLayout());
        try {
            this.add((Component)this.buildChatPanel(), "Center");
            this.add((Component)this.buildInfoPanel(), "West");
        }
        catch (Exception exception3) {
            MinecraftServerGui.LOGGER.error("Couldn't build server GUI", (Throwable)exception3);
        }
    }
    
    public void addFinalizer(final Runnable runnable) {
        this.finalizers.add(runnable);
    }
    
    private JComponent buildInfoPanel() {
        final JPanel jPanel2 = new JPanel((LayoutManager)new BorderLayout());
        final StatsComponent zo3 = new StatsComponent(this.server);
        this.finalizers.add(zo3::close);
        jPanel2.add((Component)zo3, "North");
        jPanel2.add((Component)this.buildPlayerPanel(), "Center");
        jPanel2.setBorder((Border)new TitledBorder((Border)new EtchedBorder(), "Stats"));
        return (JComponent)jPanel2;
    }
    
    private JComponent buildPlayerPanel() {
        final JList<?> jList2 = new PlayerListComponent(this.server);
        final JScrollPane jScrollPane3 = new JScrollPane((Component)jList2, 22, 30);
        jScrollPane3.setBorder((Border)new TitledBorder((Border)new EtchedBorder(), "Players"));
        return (JComponent)jScrollPane3;
    }
    
    private JComponent buildChatPanel() {
        final JPanel jPanel2 = new JPanel((LayoutManager)new BorderLayout());
        final JTextArea jTextArea3 = new JTextArea();
        final JScrollPane jScrollPane4 = new JScrollPane((Component)jTextArea3, 22, 30);
        jTextArea3.setEditable(false);
        jTextArea3.setFont(MinecraftServerGui.MONOSPACED);
        final JTextField jTextField5 = new JTextField();
        jTextField5.addActionListener(actionEvent -> {
            final String string4 = jTextField5.getText().trim();
            if (!string4.isEmpty()) {
                this.server.handleConsoleInput(string4, this.server.createCommandSourceStack());
            }
            jTextField5.setText("");
        });
        jTextArea3.addFocusListener((FocusListener)new FocusAdapter() {
            public void focusGained(final FocusEvent focusEvent) {
            }
        });
        jPanel2.add((Component)jScrollPane4, "Center");
        jPanel2.add((Component)jTextField5, "South");
        jPanel2.setBorder((Border)new TitledBorder((Border)new EtchedBorder(), "Log and chat"));
        (this.logAppenderThread = new Thread(() -> {
            String string4;
            while ((string4 = QueueLogAppender.getNextLogEvent("ServerGuiConsole")) != null) {
                this.print(jTextArea3, jScrollPane4, string4);
            }
        })).setUncaughtExceptionHandler((Thread.UncaughtExceptionHandler)new DefaultUncaughtExceptionHandler(MinecraftServerGui.LOGGER));
        this.logAppenderThread.setDaemon(true);
        return (JComponent)jPanel2;
    }
    
    public void start() {
        this.logAppenderThread.start();
    }
    
    public void close() {
        if (!this.isClosing.getAndSet(true)) {
            this.runFinalizers();
        }
    }
    
    private void runFinalizers() {
        this.finalizers.forEach(Runnable::run);
    }
    
    public void print(final JTextArea jTextArea, final JScrollPane jScrollPane, final String string) {
        if (!SwingUtilities.isEventDispatchThread()) {
            SwingUtilities.invokeLater(() -> this.print(jTextArea, jScrollPane, string));
            return;
        }
        final Document document5 = jTextArea.getDocument();
        final JScrollBar jScrollBar6 = jScrollPane.getVerticalScrollBar();
        boolean boolean7 = false;
        if (jScrollPane.getViewport().getView() == jTextArea) {
            boolean7 = (jScrollBar6.getValue() + jScrollBar6.getSize().getHeight() + MinecraftServerGui.MONOSPACED.getSize() * 4 > jScrollBar6.getMaximum());
        }
        try {
            document5.insertString(document5.getLength(), string, (AttributeSet)null);
        }
        catch (BadLocationException ex) {}
        if (boolean7) {
            jScrollBar6.setValue(Integer.MAX_VALUE);
        }
    }
    
    static {
        MONOSPACED = new Font("Monospaced", 0, 12);
        LOGGER = LogManager.getLogger();
    }
}

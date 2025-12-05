package view.utils;

import javax.swing.*;
import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

import view.control.AlgorithmCategory;

public class ConsoleTee {
    private static ConsoleTee instance;
    private final PrintStream originalOut;
    private final PrintStream originalErr;
    private final Map<AlgorithmCategory, List<JTextArea>> areasByChannel = new ConcurrentHashMap<>();
    private volatile AlgorithmCategory activeChannel = null;

    private ConsoleTee() {
        originalOut = System.out;
        originalErr = System.err;

        PrintStream ps = new PrintStream(new OutputStream() {
            @Override
            public void write(int b) {
                write(new byte[]{(byte) b}, 0, 1);
            }

            @Override
            public void write(byte[] b, int off, int len) {
                String s = new String(b, off, len, StandardCharsets.UTF_8);
                originalOut.print(s);
                AlgorithmCategory ch = activeChannel;
                if (ch != null) {
                    List<JTextArea> list = areasByChannel.get(ch);
                    if (list != null && !list.isEmpty()) {
                        for (JTextArea ta : list) {
                            SwingUtilities.invokeLater(() -> {
                                ta.append(s);
                                int max = 20000;
                                if (ta.getDocument().getLength() > max) {
                                    try {
                                        ta.getDocument().remove(0, ta.getDocument().getLength() - max);
                                    } catch (javax.swing.text.BadLocationException ex) {
                                        //
                                    }
                                }
                                ta.setCaretPosition(ta.getDocument().getLength());
                            });
                        }
                    }
                }
            }

            @Override
            public void flush() {
                originalOut.flush();
            }

            @Override
            public void close() {
                originalOut.close();
            }
        }, true);

        System.setOut(ps);
        System.setErr(ps);
    }

    public static synchronized ConsoleTee getInstance() {
        if (instance == null) instance = new ConsoleTee();
        return instance;
    }

    public void register(JTextArea ta, AlgorithmCategory channel) {
        if (ta == null || channel == null) return;
        areasByChannel.computeIfAbsent(channel, k -> new CopyOnWriteArrayList<>()).add(ta);
    }

    public void setActiveChannel(AlgorithmCategory channel) {
        this.activeChannel = channel;
    }

    public void clearChannel(AlgorithmCategory channel) {
        if (channel == null) return;
        List<JTextArea> list = areasByChannel.get(channel);
        if (list == null) return;
        for (JTextArea ta : list) {
            SwingUtilities.invokeLater(() -> ta.setText(""));
        }
    }
}

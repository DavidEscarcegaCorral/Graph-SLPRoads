package view;

import view.panels.MainAppPanel;
import view.styles.scroll.ScrollPaneCustom;

import javax.swing.*;
import java.awt.*;

/**
 * Ventana principal de la aplicación.
 * Solo posee un MainPanel.
 */
public class MainFrame extends JFrame {
    private ScrollPaneCustom scrollCustom;

    public MainFrame() {
        super("Graph-SLPRoads");
        setSize(1380, 750);
        getContentPane().setBackground(Color.white);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    public void setMainPanel(MainAppPanel mainAppPanel) {
        // Deshabilitar scroll horizontal
        scrollCustom = new ScrollPaneCustom(mainAppPanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        add(scrollCustom);
    }
}

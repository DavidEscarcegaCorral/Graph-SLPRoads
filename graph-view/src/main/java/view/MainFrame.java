package view;

import view.panels.MainAppPanel;

import javax.swing.*;
import java.awt.*;

/**
 * Ventana principal de la aplicación.
 * Solo posee un MainPanel.
 */
public class MainFrame extends JFrame {

    public MainFrame() {
        super("Graph-SLPRoads");
        setSize(1380, 750);
        getContentPane().setBackground(Color.white);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    public void setMainPanel(MainAppPanel mainAppPanel) {
        add(mainAppPanel);
    }
}

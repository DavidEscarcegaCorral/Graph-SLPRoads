package view;

import view.panels.MainAppPanel;

import javax.swing.*;
import java.awt.*;

/**
 * Ventana principal (JFrame) de la aplicación.
 * Su única tarea es contener el MainPanel.
 */
public class MainFrame extends JFrame {

    public MainFrame() {
        super("Graph-SLPRoads");
        setSize(1380, 800);
        getContentPane().setBackground(Color.white);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

    }

    public void setMainPanel(MainAppPanel mainAppPanel) {
        add(mainAppPanel);
    }
}

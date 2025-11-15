package view;

import javax.swing.*;

/**
 * Ventana principal (JFrame) de la aplicación.
 * Su única tarea es contener el MainPanel.
 */
public class MainFrame extends JFrame {

    public MainFrame() {
        super("Graph-SLPRoads");

        MainPanel mainPanel = new MainPanel();
        setSize(1380, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        add(mainPanel);
    }
}

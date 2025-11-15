package main;

import view.MainFrame;

import javax.swing.*;

public class GraphSLPRoads {
    static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                MainFrame mainFrame = new MainFrame();
                mainFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
                mainFrame.setVisible(true);
                mainFrame.setVisible(true);
            }
        });
    }
}

package main;

import view.MainFrame;
import view.control.ViewControl;

import javax.swing.*;

public class GraphSLPRoads {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                MainFrame mainFrame = new MainFrame();
                ViewControl viewControl = new ViewControl(mainFrame);
                mainFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
                mainFrame.setVisible(true);
            }
        });
    }
}

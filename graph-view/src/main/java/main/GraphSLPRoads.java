package main;

import view.MainFrame;
import view.viewControl.ViewControl;

import javax.swing.*;

public class GraphSLPRoads {
    static void main(String[] args) {
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

package view.panels.leftPanels;

import javax.swing.*;

public class LeftPanel extends JPanel {
    private MapPanel mapPanel;

    public LeftPanel(){
        mapPanel = new MapPanel();
        add(mapPanel);
    }
}

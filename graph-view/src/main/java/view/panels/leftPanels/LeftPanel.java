package view.panels.leftPanels;

import javax.swing.*;

public class LeftPanel extends JPanel {
    private MapPanel mapPanel;
    private ControlsPanel controlsPanel;

    public LeftPanel(ControlsPanel controlsPanel) {
        setOpaque(false);
//        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        mapPanel = new MapPanel();

        add(mapPanel);
        add(Box.createVerticalStrut(50));
        add(controlsPanel);
    }

    public MapPanel getMapPanel() {
        return mapPanel;
    }

}

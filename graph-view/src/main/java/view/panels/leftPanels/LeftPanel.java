package view.panels.leftPanels;

import javax.swing.*;

public class LeftPanel extends JPanel {
    private MapPanel mapPanel;
    private ControlsPanel controlsPanel;

    public LeftPanel(ControlsPanel controlsPanel) {
        setOpaque(false);

        mapPanel = new MapPanel();

        add(mapPanel);
        add(controlsPanel);
    }

    public MapPanel getMapPanel() {
        return mapPanel;
    }

}

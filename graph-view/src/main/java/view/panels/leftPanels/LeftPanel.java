package view.panels.leftPanels;

import javax.swing.*;

public class LeftPanel extends JPanel {
    private final MapPanel mapPanel;

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

package view.panels.leftPanels;

import view.panels.rightPanels.ControlsPanel;

import javax.swing.*;
import java.awt.*;

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

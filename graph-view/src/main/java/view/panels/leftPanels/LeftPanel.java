package view.panels.leftPanels;

import javax.swing.*;
import java.awt.*;

public class LeftPanel extends JPanel {
    private final MapPanel mapPanel;

    public LeftPanel(ControlsPanel controlsPanel) {
        setOpaque(false);
        setLayout(new BorderLayout());

        mapPanel = new MapPanel();
        add(mapPanel, BorderLayout.CENTER);

        JPanel southPanel = new JPanel();
        southPanel.setOpaque(false);
        southPanel.setLayout(new BorderLayout());

        JPanel controlsWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        controlsWrapper.setOpaque(false);
        controlsWrapper.add(controlsPanel);

        southPanel.add(controlsWrapper, BorderLayout.NORTH);

        add(southPanel, BorderLayout.SOUTH);
    }

    public MapPanel getMapPanel() {
        return mapPanel;
    }

}

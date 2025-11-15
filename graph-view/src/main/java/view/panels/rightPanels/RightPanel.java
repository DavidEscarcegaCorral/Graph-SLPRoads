package view.panels.rightPanels;

import view.panels.leftPanels.MapPanel;

import javax.swing.*;

public class RightPanel extends JPanel {
    private ControlsPanel controlsPanel;

    public RightPanel() {
        ControlsPanel controlsPanel = new ControlsPanel();
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        add(controlsPanel);
    }

}

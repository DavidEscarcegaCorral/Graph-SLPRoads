package view.panels;

import javax.swing.*;
import java.awt.*;

public class PanelMainFrame extends JPanel {
    private MenuPanel menuPanel;
    private MapPanel mapPanel;
    private ControlsPanel controlsPanel;

    public PanelMainFrame() {
        setOpaque(false);

        menuPanel = new MenuPanel();
        mapPanel = new MapPanel();
        controlsPanel = new ControlsPanel();

        // Añadir componentes
        add(mapPanel);
        add(controlsPanel);

    }
}

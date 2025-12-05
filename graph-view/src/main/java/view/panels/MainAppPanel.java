package view.panels;

import view.panels.rightPanels.RightPanel;
import view.panels.leftPanels.LeftPanel;

import javax.swing.*;
import java.awt.*;

public class MainAppPanel extends JSplitPane {
    private static final double SPLIT_RATIO = 0.45;
    private RightPanel rightPanel;
    private LeftPanel leftPanel;

    public MainAppPanel() {
        setOpaque(false);
        setOrientation(JSplitPane.HORIZONTAL_SPLIT);

        setResizeWeight(SPLIT_RATIO);
        setDividerSize(0);
        setContinuousLayout(true);
        setEnabled(false);
        setBorder(null);
    }

    public void setRightPanel(RightPanel rightPanel) {
        rightPanel.setMinimumSize(new Dimension(0, 0));
        this.rightPanel = rightPanel;
        setRightComponent(rightPanel);
        revalidate();
        repaint();
    }

    public void setLeftPanel(LeftPanel leftPanel) {
        leftPanel.setMinimumSize(new Dimension(0, 0));
        this.leftPanel = leftPanel;
        setLeftComponent(leftPanel);
        revalidate();
        repaint();
    }

    @Override
    public Dimension getPreferredSize() {
        Dimension leftPref = (leftPanel != null) ? leftPanel.getPreferredSize() : new Dimension(600, 600);
        Dimension rightPref = (rightPanel != null) ? rightPanel.getPreferredSize() : new Dimension(400, 600);

        int width = leftPref.width + rightPref.width + getDividerSize();
        int height = Math.max(leftPref.height, rightPref.height);

        // tamaños minimos
        width = Math.max(width, 800);
        height = Math.max(height, 600);

        return new Dimension(width, height);
    }
}

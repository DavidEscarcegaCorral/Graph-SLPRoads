package view.panels;

import view.panels.rightPanels.RightPanel;
import view.panels.leftPanels.LeftPanel;

import javax.swing.*;
import java.awt.*;

public class MainAppPanel extends JSplitPane {
    private static final double SPLIT_RATIO = 0.7;

    private RightPanel rightPanel;
    private LeftPanel leftPanel;

    public MainAppPanel() {
        setOpaque(false);
        leftPanel = new LeftPanel();
        rightPanel = new RightPanel();

        setOrientation(JSplitPane.HORIZONTAL_SPLIT);
        setLeftComponent(leftPanel);
        setRightComponent(rightPanel);
        setResizeWeight(SPLIT_RATIO);
        setContinuousLayout(true);
        setDividerSize(8);

    }

    @Override
    public void addNotify() {
        super.addNotify();
        this.setDividerLocation(SPLIT_RATIO);
    }

    /**
     * @return El panel de imagen (izquierdo).
     */
    public JPanel getImagePanel() {
        return leftPanel;
    }

    /**
     * @return El panel de controles (derecho).
     */
    public JPanel getControlPanel() {
        return rightPanel;
    }
}

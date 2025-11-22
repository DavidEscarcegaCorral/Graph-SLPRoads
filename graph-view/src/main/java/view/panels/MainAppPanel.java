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
        setDividerSize(2);
        setContinuousLayout(true);
        setEnabled(false);
        setBorder(null);

    }

    public void setRightPanel(RightPanel rightPanel) {
        rightPanel.setMinimumSize(new Dimension(0, 0));
        setRightComponent(rightPanel);
    }

    public void setLeftPanel(LeftPanel leftPanel) {
        leftPanel.setMinimumSize(new Dimension(0, 0));
        setLeftComponent(leftPanel);
    }

//    @Override
//    public void addNotify() {
//        super.addNotify();
//        setDividerLocation(SPLIT_RATIO);
//    }
}

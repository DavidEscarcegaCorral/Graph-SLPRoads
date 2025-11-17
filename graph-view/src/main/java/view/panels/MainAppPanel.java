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
        setOrientation(JSplitPane.HORIZONTAL_SPLIT);

        setResizeWeight(SPLIT_RATIO);
        setDividerSize(1);
        setContinuousLayout(true);
        setEnabled(false);
        setBorder(null);

    }

    public void setRightPanel(RightPanel rightPanel) {
        setRightComponent(rightPanel);
    }

    public void setLeftPanel(LeftPanel leftPanel) {
        setLeftComponent(leftPanel);
    }

//    public RightPanel getRightPanel() {
//        return rightPanel;
//    }
//
//    public LeftPanel getLeftPanel() {
//        return leftPanel;
//    }
}

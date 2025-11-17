package view.panels.rightPanels;

import javax.swing.*;
import java.awt.*;

public class RightPanel extends JPanel {
    private JPanel firstPanel;
    private JPanel secondPanel;
    private JPanel thirdPanel;
    private MenuButtonsPanel menuButtonsPanel;
    private RecorridoButtonsPanel recorridoButtonsPanel;

    public RightPanel(MenuButtonsPanel menuButtonsPanel) {
        setOpaque(false);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        this.menuButtonsPanel = menuButtonsPanel;

        firstPanel = new JPanel();
        firstPanel.setOpaque(false);
        secondPanel = new JPanel();
        secondPanel.setOpaque(false);
        thirdPanel = new JPanel();
        thirdPanel.setOpaque(false);

        // Añadir componetnes
        firstPanel.add(menuButtonsPanel);

        add(firstPanel);
        add(secondPanel);
        add(thirdPanel);
    }

    public JPanel getFirstPanel() {
        return firstPanel;
    }

    public JPanel getSecondPanel() {
        return secondPanel;
    }

    public JPanel getThirdPanel() {
        return thirdPanel;
    }
}

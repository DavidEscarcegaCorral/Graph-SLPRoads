package view.panels.rightPanels;

import view.panels.rightPanels.header.HeaderMenuPanel;

import javax.swing.*;
import java.awt.*;

public class RightPanel extends JPanel {
    private JPanel firstPanel;
    private JPanel secondPanel;
    private JPanel thirdPanel;

    public RightPanel(HeaderMenuPanel headerMenuPanel) {
        setOpaque(false);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        firstPanel = new JPanel();
        firstPanel.setOpaque(false);
        firstPanel.setMaximumSize(new Dimension(700, 140));

        secondPanel = new JPanel();
        secondPanel.setOpaque(false);

        thirdPanel = new JPanel();
        thirdPanel.setOpaque(false);

        // Añadir componetnes
        firstPanel.add(headerMenuPanel);

        add(Box.createVerticalStrut(20));
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

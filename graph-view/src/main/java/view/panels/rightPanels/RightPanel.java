package view.panels.rightPanels;

import view.panels.rightPanels.header.HeaderMenuPanel;

import javax.swing.*;
import java.awt.*;

public class RightPanel extends JPanel {
    private JPanel firstPanel;
    private JPanel secondPanel;
    private JPanel thirdPanel;
    private JPanel logCardPanel; // panel con CardLayout

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

        // CardLayout para posibles tarjetas (sin áreas de texto)
        logCardPanel = new JPanel(new CardLayout());
        JPanel placeholder = new JPanel();
        placeholder.setOpaque(false);
        placeholder.setPreferredSize(new Dimension(600, 150));
        logCardPanel.add(placeholder, "PLACEHOLDER");

        JPanel logWrapper = new JPanel(new BorderLayout());
        logWrapper.setOpaque(false);
        JPanel center = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        center.setOpaque(false);
        center.add(logCardPanel);
        logWrapper.add(center, BorderLayout.CENTER);

        firstPanel.add(headerMenuPanel);

        add(Box.createVerticalStrut(20));
        add(firstPanel);
        add(secondPanel);
        add(thirdPanel);
        add(Box.createVerticalStrut(10));
        add(logWrapper);
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

    public void showLogCard(String key) {
        if (logCardPanel == null) return;
        CardLayout cl = (CardLayout) logCardPanel.getLayout();
        cl.show(logCardPanel, key);
    }
}

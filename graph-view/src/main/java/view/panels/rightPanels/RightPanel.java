package view.panels.rightPanels;

import javax.swing.*;

public class RightPanel extends JPanel {
    private JPanel firstPanel;
    private JPanel secondPanel;
    private JPanel thirdPanel;
    private HeaderMenuPanel headerMenuPanel;
    private OptionsMenuComponent optionsMenuComponent;

    public RightPanel(HeaderMenuPanel headerMenuPanel) {
        setOpaque(false);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        this.headerMenuPanel = headerMenuPanel;

        firstPanel = new JPanel();
        firstPanel.setOpaque(false);
        secondPanel = new JPanel();
        secondPanel.setOpaque(false);
        thirdPanel = new JPanel();
        thirdPanel.setOpaque(false);

        // Añadir componetnes
        firstPanel.add(headerMenuPanel);

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

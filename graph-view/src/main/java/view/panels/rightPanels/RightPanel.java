package view.panels.rightPanels;

import javax.swing.*;

public class RightPanel extends JPanel {
    private JPanel firstPanel;
    private JPanel secondPanel;
    private JPanel thirdPanel;
    private HeaderMenuPanel headerMenuPanel;
    private AlgotihmsMenuComponent algotihmsMenuComponent;

    public RightPanel(HeaderMenuPanel headerMenuPanel) {
        setOpaque(false);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        this.headerMenuPanel = headerMenuPanel;

        firstPanel = new JPanel();
        firstPanel.setOpaque(false);
//        firstPanel.setPreferredSize(new Dimension(850, 340));
        secondPanel = new JPanel();
        secondPanel.setOpaque(false);
//        secondPanel.setPreferredSize(new Dimension(850, 340));
        thirdPanel = new JPanel();
        thirdPanel.setOpaque(false);
//        thirdPanel.setPreferredSize(new Dimension(850, 340));

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

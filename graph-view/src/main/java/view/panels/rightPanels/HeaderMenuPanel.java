package view.panels.rightPanels;

import view.styles.Button;
import view.styles.FontUtil;

import javax.swing.*;

public class HeaderMenuPanel extends JPanel {
    private HeaderComponent headerPanel;
    private AlgotihmsMenuComponent algotihmsMenuComponent;
    private JLabel lbl1;

    public HeaderMenuPanel(HeaderComponent header, AlgotihmsMenuComponent algotihmsMenu) {
        setOpaque(false);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        headerPanel = header;
        algotihmsMenuComponent = algotihmsMenu;
        lbl1 = new  JLabel("Seleccionar algoritmo");
        lbl1.setFont(FontUtil.loadFont(26, "Inter_Regular"));


        add(headerPanel);
        add(Box.createVerticalStrut(20));
        add(lbl1);
        add(Box.createVerticalStrut(20));
        add(algotihmsMenuComponent);

    }

    public HeaderComponent getHeaderPanel() {
        return headerPanel;
    }

    public AlgotihmsMenuComponent getAlgotihmsMenuComponent() {
        return algotihmsMenuComponent;
    }
}

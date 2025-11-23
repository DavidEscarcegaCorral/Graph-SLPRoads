package view.panels.rightPanels.header;

import view.styles.FontUtil;

import javax.swing.*;

public class HeaderMenuPanel extends JPanel {
    private HeaderComponent headerPanel;
    private OptionsMenuComponent optionsMenuComponent;
    private JLabel lbl1;

    public HeaderMenuPanel(HeaderComponent header, OptionsMenuComponent algotihmsMenu) {
        setOpaque(false);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        headerPanel = header;
        optionsMenuComponent = algotihmsMenu;
        lbl1 = new  JLabel("Seleccionar algoritmo");
        lbl1.setFont(FontUtil.loadFont(26, "Inter_SemiBold"));


        add(Box.createHorizontalGlue());
        add(headerPanel);
        add(Box.createVerticalStrut(20));
        add(Box.createHorizontalGlue());
        add(lbl1);
        add(Box.createVerticalStrut(20));
        add(optionsMenuComponent);

    }

    public HeaderComponent getHeaderPanel() {
        return headerPanel;
    }

    public OptionsMenuComponent getAlgotihmsMenuComponent() {
        return optionsMenuComponent;
    }
}

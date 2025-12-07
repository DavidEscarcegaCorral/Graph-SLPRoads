package view.panels.rightPanels.header;

import view.styles.FontUtil;

import javax.swing.*;

public class HeaderMenuPanel extends JPanel {
    private final HeaderComponent headerPanel;
    private final OptionsMenuComponent optionsMenuComponent;
    private JLabel lbl1;

    public HeaderMenuPanel(HeaderComponent header, OptionsMenuComponent algotihmsMenu) {
        setOpaque(false);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        headerPanel = header;
        optionsMenuComponent = algotihmsMenu;
        lbl1 = new  JLabel("Seleccionar Algoritmo");
        lbl1.setFont(FontUtil.loadFont(26, "Inter_SemiBold"));
        lbl1.setAlignmentX(CENTER_ALIGNMENT);


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

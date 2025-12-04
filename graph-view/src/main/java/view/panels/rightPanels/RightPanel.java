package view.panels.rightPanels;

import view.panels.rightPanels.header.HeaderMenuPanel;
import view.styles.scroll.ScrollPaneCustom;
import view.styles.Button;
import view.styles.Colors;

import javax.swing.*;
import java.awt.*;

public class RightPanel extends JPanel {
    private JPanel firstPanel;
    private JPanel secondPanel;
    private JPanel thirdPanel;

    private JTextArea searchLogArea;
    private JTextArea mstLogArea;
    private JTextArea spLogArea;
    private Button clearBtn; // botón limpiar del wrapper
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

        // Áreas de logs separadas por tipo
        searchLogArea = createStyledTextArea();
        mstLogArea = createStyledTextArea();
        spLogArea = createStyledTextArea();

        // Panel con CardLayout para cambiar entre logs
        logCardPanel = new JPanel(new CardLayout());
        ScrollPaneCustom s1 = new ScrollPaneCustom(searchLogArea, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        ScrollPaneCustom s2 = new ScrollPaneCustom(mstLogArea, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        ScrollPaneCustom s3 = new ScrollPaneCustom(spLogArea, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        s1.setPreferredSize(new Dimension(600, 150)); s1.setMaximumSize(new Dimension(600,150));
        s2.setPreferredSize(new Dimension(600, 150)); s2.setMaximumSize(new Dimension(600,150));
        s3.setPreferredSize(new Dimension(600, 150)); s3.setMaximumSize(new Dimension(600,150));

        logCardPanel.add(s1, "SEARCH");
        logCardPanel.add(s2, "MST");
        logCardPanel.add(s3, "SP");

        clearBtn = new Button("Limpiar", 100, 36, 12, 14, Color.WHITE, Colors.COLOR_BUTTON, Colors.COLOR_BUTTON_HOVER);

        JPanel logWrapper = new JPanel(new BorderLayout());
        logWrapper.setOpaque(false);
        JPanel center = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        center.setOpaque(false);
        center.add(logCardPanel);
        logWrapper.add(center, BorderLayout.CENTER);
        // botón arriba
        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 6));
        btnRow.setOpaque(false);
        btnRow.add(clearBtn);
        logWrapper.add(btnRow, BorderLayout.NORTH);

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

    public JTextArea getLogArea() {
        // por compatibilidad devuelve la del SEARCH
        return searchLogArea;
    }

    public Button getClearLogBtn() {
        return clearBtn;
    }

    // Helpers para seleccionar y limpiar
    public void showLogCard(String key) {
        CardLayout cl = (CardLayout) logCardPanel.getLayout();
        cl.show(logCardPanel, key);
    }

    public JTextArea getCurrentLogArea() {
        // encontrar cuál está visible por key; intentar buscar por CardLayout is cumbersome;
        // simpler: track last shown via a field, but for ahora assume caller knows which to request.
        return searchLogArea;
    }

    public JTextArea getSearchLogArea() { return searchLogArea; }
    public JTextArea getMstLogArea() { return mstLogArea; }
    public JTextArea getSpLogArea() { return spLogArea; }

    public void clearCurrentLog(String key) {
        switch (key) {
            case "SEARCH": searchLogArea.setText(""); break;
            case "MST": mstLogArea.setText(""); break;
            case "SP": spLogArea.setText(""); break;
            default: searchLogArea.setText("");
        }
    }

    private JTextArea createStyledTextArea() {
        JTextArea ta = new JTextArea();
        ta.setEditable(false);
        ta.setLineWrap(true);
        ta.setWrapStyleWord(true);
        ta.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1),
                BorderFactory.createEmptyBorder(6, 8, 6, 8)
        ));
        return ta;
    }
}

package view.panels.rightPanels.searchAlgorithms;

import view.styles.*;
import view.styles.Button;
import view.styles.scroll.ScrollPaneCustom;
import view.styles.textFields.TxtFieldPh;
import view.utils.ConsoleTee;
import view.control.AlgorithmCategory;

import javax.swing.*;
import java.awt.*;

public class SearchAlgorithmsComponent extends JPanel {
    private final ButtonGroup buttonGroup;

    private final CustomRadioButton rbtn1;
    private final CustomRadioButton rbtn2;

    private final JLabel lbl1;
    private final JLabel lbl2;

    private final TxtFieldPh textField;
    private final Button citiesBtn;

    private JPanel titlePanel;
    private final JPanel p1;
    private final JPanel p2;
    private final JPanel p3;
    private final JPanel p4;

    public SearchAlgorithmsComponent() {
        setOpaque(false);
        setPreferredSize(new Dimension(700, 500));
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        titlePanel.setOpaque(false);
        titlePanel.setMaximumSize(new Dimension(700, 50));
        titlePanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        p1 = new JPanel(new FlowLayout(FlowLayout.LEFT, 30, 5));
        p1.setOpaque(false);
        p1.setMaximumSize(new Dimension(700, 50));

        p2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        p2.setOpaque(false);
        p2.setMaximumSize(new Dimension(700, 50));
        p2.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 5));

        p3 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        p3.setOpaque(false);
        p3.setMaximumSize(new Dimension(700, 60));
        p3.setBorder(BorderFactory.createEmptyBorder(5, 8, 5, 5));

        p4 = new JPanel(new BorderLayout());
        p4.setOpaque(false);
        p4.setMaximumSize(new Dimension(700, 450));
        p4.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 5));

        JLabel titleLbL = new JLabel("Recorrido del grafo");
        titleLbL.setFont(FontUtil.loadFont(20, "Inter_SemiBold"));
        titleLbL.setForeground(Colors.COLOR_BUTTON);

        rbtn1 = new CustomRadioButton("En anchura (BFS)");
        rbtn2 = new CustomRadioButton("En profundidad (DFS)");

        buttonGroup = new ButtonGroup();
        buttonGroup.add(rbtn1);
        buttonGroup.add(rbtn2);

        lbl1 = new JLabel("Iniciar el recorrido en:");
        lbl1.setFont(FontUtil.loadFont(18, "Inter_Regular"));
        lbl1.setForeground(Color.BLACK);
        lbl2 = new JLabel("Complejidad temporal: ");
        lbl2.setFont(FontUtil.loadFont(18, "Inter_Regular"));
        lbl2.setForeground(Color.BLACK);

        rbtn1.setSelected(true);
        rbtn1.addActionListener(e -> updateComplexityLabel());
        rbtn2.addActionListener(e -> updateComplexityLabel());

        textField = new TxtFieldPh("Ciudad o No. de nodo", 220, 40, 16);
        citiesBtn = new Button("Ver ciudades", 150, 40, 16, 10, Color.WHITE, Colors.COLOR_BUTTON, Colors.COLOR_BUTTON_HOVER);

        titlePanel.add(titleLbL);
        p1.add(rbtn1);
        p1.add(rbtn2);
        p2.add(lbl1);
        p3.add(textField);
        p3.add(citiesBtn);

        TextAreaCustom consoleArea = new TextAreaCustom(10, 20);
        ScrollPaneCustom scroll = new ScrollPaneCustom(consoleArea);

        ConsoleTee.getInstance().register(consoleArea, AlgorithmCategory.SEARCH);

        JPanel southPanel = new JPanel(new BorderLayout());
        southPanel.setOpaque(false);
        southPanel.add(lbl2, BorderLayout.WEST);
        Button clearBtn = new Button("Limpiar", 100, 30, 16, 8, Color.WHITE, Colors.COLOR_BUTTON, Colors.COLOR_BUTTON_HOVER);
        clearBtn.addActionListener(ev -> {
            consoleArea.setText("");
            ConsoleTee.getInstance().clearChannel(AlgorithmCategory.SEARCH);
        });

        JPanel btnWrapPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 10));
        btnWrapPanel.setOpaque(false);
        btnWrapPanel.add(clearBtn);
        southPanel.add(btnWrapPanel, BorderLayout.EAST);

        p4.add(scroll, BorderLayout.CENTER);
        p4.add(southPanel, BorderLayout.SOUTH);

        add(titlePanel);
        add(p1);
        add(p2);
        add(p3);
        add(p4);

        updateComplexityLabel();
    }

    private void updateComplexityLabel() {
        lbl2.setText("Complejidad temporal: O(V + E)");
    }

    public TxtFieldPh getTextField() {
        return textField;
    }

    public Button getCitiesBtn() {
        return citiesBtn;
    }

    public boolean isBFSSelected() {
        return rbtn1.isSelected();
    }

    public boolean isDFSSelected() {
        return rbtn2.isSelected();
    }


}

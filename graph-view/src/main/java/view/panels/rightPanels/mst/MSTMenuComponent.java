package view.panels.rightPanels.mst;

import view.styles.*;
import view.styles.Button;
import view.styles.scroll.ScrollPaneCustom;
import view.styles.textFields.TxtFieldPh;
import view.utils.ConsoleTee;
import view.control.AlgorithmCategory;

import javax.swing.*;
import java.awt.*;

public class MSTMenuComponent extends JPanel {
    private ButtonGroup buttonGroup;

    private CustomRadioButton rbtn1;
    private CustomRadioButton rbtn2;
    private CustomRadioButton rbtn3;

    private JLabel lbl1;
    private JLabel finalWeightLbl;

    private TxtFieldPh textField;
    private Button citiesBtn;

    private JPanel titlePanel;
    private JPanel p1;
    private JPanel p2;
    private JPanel p3;
    private JPanel p4;

    public MSTMenuComponent() {
        setOpaque(false);
        setPreferredSize(new Dimension(700, 360));
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
        p4.setMaximumSize(new Dimension(700, 200));
        p4.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 5));

        JLabel titleLbL = new JLabel("Árbol de expansion minima");
        titleLbL.setFont(FontUtil.loadFont(20, "Inter_SemiBold"));
        titleLbL.setForeground(Colors.COLOR_BUTTON);

        rbtn1 = new CustomRadioButton("Kruskal");
        rbtn2 = new CustomRadioButton("Prim");
        rbtn3 = new CustomRadioButton("Boruvka");

        buttonGroup = new ButtonGroup();
        buttonGroup.add(rbtn1);
        buttonGroup.add(rbtn2);
        buttonGroup.add(rbtn3);

        lbl1 = new JLabel("Iniciar el recorrido en:");
        lbl1.setFont(FontUtil.loadFont(18, "Inter_Regular"));
        lbl1.setForeground(Color.BLACK);

        finalWeightLbl = new JLabel("Peso final: ");
        finalWeightLbl.setFont(FontUtil.loadFont(18, "Inter_Regular"));
        finalWeightLbl.setForeground(Color.BLACK);

        textField = new TxtFieldPh("Ciudad o No. de nodo", 220, 40, 16);
        citiesBtn = new Button("Ver ciudades", 150, 40, 16, 10, Color.WHITE, Colors.COLOR_BUTTON, Colors.COLOR_BUTTON_HOVER);

        titlePanel.add(titleLbL);
        p1.add(rbtn1);
        p1.add(rbtn2);
        p1.add(rbtn3);
        p2.add(lbl1);
        p3.add(textField);
        p3.add(citiesBtn);

        TextAreaCustom consoleArea = new TextAreaCustom(10, 20);
        ScrollPaneCustom scroll = new ScrollPaneCustom(consoleArea);

        p4.add(scroll, BorderLayout.CENTER);

        ConsoleTee.getInstance().register(consoleArea, AlgorithmCategory.MST);

        JPanel southPanel = new JPanel(new BorderLayout());
        southPanel.setOpaque(false);
        southPanel.add(finalWeightLbl, BorderLayout.WEST);
        Button clearBtn = new Button("Limpiar", 100, 30, 16, 8, Color.WHITE, Colors.COLOR_BUTTON, Colors.COLOR_BUTTON_HOVER);
        clearBtn.addActionListener(ev -> {
            consoleArea.setText("");
            ConsoleTee.getInstance().clearChannel(AlgorithmCategory.MST);
        });

        JPanel btnWrapPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 10));
        btnWrapPanel.setOpaque(false);
        btnWrapPanel.add(clearBtn);
        southPanel.add(btnWrapPanel, BorderLayout.EAST);

        p4.add(southPanel, BorderLayout.SOUTH);

        add(titlePanel);
        add(p1);
        add(p2);
        add(p3);
        add(p4);
    }


    public TxtFieldPh getTextField() {
        return textField;
    }

    public Button getCitiesBtn() {
        return citiesBtn;
    }

    public void setWeight(int peso) {
        if (peso == -1) {
            finalWeightLbl.setText("Peso final: ");
        } else {
            finalWeightLbl.setText("Peso final: " + peso);
        }
        finalWeightLbl.revalidate();
        finalWeightLbl.repaint();
    }

    public boolean isKruskalSelected() {
        return rbtn1.isSelected();
    }

    public boolean isPrimSelected() {
        return rbtn2.isSelected();
    }

    public boolean isBoruvkaSelected() {
        return rbtn3.isSelected();
    }
}

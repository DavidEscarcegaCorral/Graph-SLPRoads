package view.panels.rightPanels.mst;

import view.styles.Button;
import view.styles.Colors;
import view.styles.CustomRadioButton;
import view.styles.FontUtil;
import view.styles.textFields.TxtFieldPh;

import javax.swing.*;
import java.awt.*;

public class MSTMenuComponent extends JPanel {
    private ButtonGroup buttonGroup;

    private CustomRadioButton rbtn1;
    private CustomRadioButton rbtn2;
    private CustomRadioButton rbtn3;

    private JLabel lbl1;
    private JLabel lbl2;

    private TxtFieldPh textField;
    private Button citiesBtn;

    private JPanel p1;
    private JPanel p2;
    private JPanel p3;
    private JPanel p4;

    public MSTMenuComponent() {
        setOpaque(false);
        setPreferredSize(new Dimension(700, 340));
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        p1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        p1.setOpaque(false);
        p1.setMaximumSize(new Dimension(700, 50));
        p1.setBorder(BorderFactory.createEmptyBorder(5, 4, 5, 5));

        p2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        p2.setOpaque(false);
        p2.setMaximumSize(new Dimension(700, 50));
        p2.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 5));

        p3 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        p3.setOpaque(false);
        p3.setMaximumSize(new Dimension(700, 60));
        p3.setBorder(BorderFactory.createEmptyBorder(5, 8, 5, 5));

        p4 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        p4.setOpaque(false);
        p4.setMaximumSize(new Dimension(700, 50));
        p4.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 5));

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

        lbl2 = new JLabel("Peso final: ");
        lbl2.setFont(FontUtil.loadFont(18, "Inter_Regular"));
        lbl2.setForeground(Color.BLACK);

        textField = new TxtFieldPh("Ciudad o No. de nodo", 220, 40, 16);
        citiesBtn = new Button("Ver ciudades", 150, 40, 16, 10, Color.WHITE, Colors.COLOR_BUTTON, Colors.COLOR_BUTTON_HOVER);

        p1.add(rbtn1);
        p1.add(rbtn2);
        p1.add(rbtn3);
        p2.add(lbl1);
        p3.add(textField);
        p3.add(citiesBtn);
        p4.add(lbl2);

        add(p1);
        add(p2);
        add(p3);
        add(p4);
    }

    public CustomRadioButton getRbtn1() {
        return rbtn1;
    }

    public CustomRadioButton getRbtn2() {
        return rbtn2;
    }

    public JLabel getLbl1() {
        return lbl1;
    }

    public JLabel getLbl2() {
        return lbl2;
    }

    public TxtFieldPh getTextField() {
        return textField;
    }

    public Button getCitiesBtn() {
        return citiesBtn;
    }
}

package view.panels.rightPanels;

import view.styles.Button;
import view.styles.Colors;
import view.styles.CustomRadioButton;
import view.styles.FontUtil;
import view.styles.textFields.TxtFieldPh;

import javax.swing.*;
import java.awt.*;

public class TraversalAlgorithmsComponent extends JPanel {
    private ButtonGroup buttonGroup;

    private CustomRadioButton rbtn1;
    private CustomRadioButton rbtn2;

    private JLabel lbl1;
    private JLabel lbl2;

    private TxtFieldPh textField;
    private Button citiesBtn;

    public TraversalAlgorithmsComponent() {
        setOpaque(false);
        setPreferredSize(new Dimension(700, 340));
        setLayout(new FlowLayout(FlowLayout.CENTER, 8, 5));

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

        textField = new TxtFieldPh("Ciudad o No. de nodo", 220, 40, 16);
        citiesBtn = new Button("Ver ciudades", 150, 40, 16, 10, Color.WHITE, Colors.COLOR_BUTTON, Colors.COLOR_BUTTON_HOVER);

        add(rbtn1);
        add(rbtn2);
        add(lbl1);
        add(textField);
        add(citiesBtn);
        add(lbl2);

    }

    public CustomRadioButton getRbtn2() {
        return rbtn2;
    }

    public CustomRadioButton getRbtn1() {
        return rbtn1;
    }

    public TxtFieldPh getTextField() {
        return textField;
    }

    public Button getCitiesBtn() {
        return citiesBtn;
    }


}

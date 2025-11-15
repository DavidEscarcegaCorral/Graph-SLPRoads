package view.panels.rightPanels;

import view.styles.Button;
import view.styles.Colors;
import view.styles.CustomRadioButton;
import view.styles.FontUtil;
import view.styles.textFields.TxtFieldPh;

import javax.swing.*;
import java.awt.*;

public class SearchOptionPanel extends JPanel{
    private CustomRadioButton rbtn1;
    private CustomRadioButton rbtn2;

    private JLabel lbl1;
    private JLabel lbl2;

    private TxtFieldPh textField;
    private Button citiesBtn;

    public SearchOptionPanel() {
        setOpaque(false);
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        rbtn1 = new CustomRadioButton("En anchura");
        rbtn2 = new CustomRadioButton("En profundidad");

        lbl1 = new JLabel("Iniciar el recorrido en:");
        lbl1.setFont(FontUtil.loadFont(24, "Inter_Regular"));
        lbl1.setForeground(Color.BLACK);
        lbl2 = new JLabel("Complejidad temporal: ");
        lbl2.setFont(FontUtil.loadFont(24, "Inter_Regular"));
        lbl2.setForeground(Color.BLACK);

        textField = new TxtFieldPh("", 150, 50, 18);
        citiesBtn = new Button("Ver ciudades", 150, 40, 16, 10, Color.WHITE, Colors.COLOR_BUTTON, Colors.COLOR_BUTTON_HOVER);

        add(rbtn1);
        add(rbtn2);
        add(lbl1);
        add(textField);
        add(citiesBtn);
        add(lbl2);
    }

}

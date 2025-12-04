package view.panels.rightPanels.shortestPath;

import view.styles.Button;
import view.styles.Colors;
import view.styles.CustomRadioButton;
import view.styles.FontUtil;
import view.styles.textFields.TxtFieldPh;

import javax.swing.*;
import java.awt.*;

public class ShortestPathComponent extends JPanel {
    private ButtonGroup buttonGroup;

    private CustomRadioButton rbtn1;
    private CustomRadioButton rbtn2;

    private JLabel lbl1;
    private JLabel totalDistanceLbl;

    private TxtFieldPh textFieldOrigen;
    private TxtFieldPh textFieldDestino;
    private Button citiesBtn;

    private JPanel titlePanel;
    private JPanel p1;
    private JPanel p2;
    private JPanel p3;
    private JPanel p4;

    public ShortestPathComponent() {
        setOpaque(false);
        setPreferredSize(new Dimension(700, 340));
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
        p2.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        p3 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        p3.setOpaque(false);
        p3.setMaximumSize(new Dimension(700, 60));
        p3.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        p4 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        p4.setOpaque(false);
        p4.setMaximumSize(new Dimension(700, 50));
        p4.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        JLabel titleLbL = new JLabel("Calculo de ruta más corta");
        titleLbL.setFont(FontUtil.loadFont(20, "Inter_SemiBold"));
        titleLbL.setForeground(Colors.COLOR_BUTTON);

        rbtn1 = new CustomRadioButton("Bellman-Ford");
        rbtn2 = new CustomRadioButton("Dijkstra");

        buttonGroup = new ButtonGroup();
        buttonGroup.add(rbtn1);
        buttonGroup.add(rbtn2);

        lbl1 = new JLabel("Iniciar el recorrido en:");
        lbl1.setFont(FontUtil.loadFont(18, "Inter_Regular"));
        lbl1.setForeground(Color.BLACK);

        totalDistanceLbl = new JLabel("Distancia total: ");
        totalDistanceLbl.setFont(FontUtil.loadFont(18, "Inter_Regular"));
        totalDistanceLbl.setForeground(Color.BLACK);

        textFieldOrigen = new TxtFieldPh("Ciudad de origen", 220, 40, 16);
        textFieldDestino = new TxtFieldPh("Destino", 220, 40, 16);
        citiesBtn = new Button("Ver ciudades", 150, 40, 16, 10, Color.WHITE, Colors.COLOR_BUTTON, Colors.COLOR_BUTTON_HOVER);

        titlePanel.add(titleLbL);
        p1.add(rbtn1);
        p1.add(rbtn2);
        p2.add(lbl1);
        p3.add(textFieldOrigen);
        p3.add(textFieldDestino);
        p3.add(citiesBtn);
        p4.add(totalDistanceLbl);

        add(titlePanel);
        add(p1);
        add(p2);
        add(p3);
        add(p4);
    }

    public TxtFieldPh getTextFieldDestino() {
        return textFieldDestino;
    }

    public Button getCitiesBtn() {
        return citiesBtn;
    }

    public TxtFieldPh getTextFieldOrigen() {
        return textFieldOrigen;
    }

    public void setWeight(int peso) {
        if (peso == -1) {
            totalDistanceLbl.setText("Distancia total: ");
        } else {
            totalDistanceLbl.setText("Distancia total: " + peso);
        }
        totalDistanceLbl.revalidate();
        totalDistanceLbl.repaint();
    }

    public boolean isBellmanFordSelected() {
        return  rbtn1.isSelected();
    }

    public boolean isDijkstraSelected() {
        return  rbtn2.isSelected();
    }

}

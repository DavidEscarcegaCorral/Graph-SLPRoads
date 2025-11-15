package view.panels.rightPanels;

import view.styles.Button;
import view.styles.Colors;

import javax.swing.*;
import java.awt.*;

public class ControlsPanel extends JPanel {
    private Button recorridoBtn;
    private Button mstBtn;
    private Button rutaCortaBtn;

    public ControlsPanel() {
        setOpaque(false);
        setLayout(new FlowLayout(FlowLayout.CENTER, 8, 5));
        recorridoBtn = new Button("Recorrido", 120, 40, 16, 10, Color.WHITE, Colors.COLOR_BUTTON, Colors.COLOR_BUTTON_HOVER);
        mstBtn = new Button("Árbol de expansion minima", 280, 40, 16, 10, Color.WHITE, Colors.COLOR_BUTTON, Colors.COLOR_BUTTON_HOVER);
        rutaCortaBtn = new Button("Cálculo de rutas más cortas", 280, 40, 16, 10, Color.WHITE, Colors.COLOR_BUTTON, Colors.COLOR_BUTTON_HOVER);

        add(recorridoBtn);
        add(mstBtn);
        add(rutaCortaBtn);
    }

}

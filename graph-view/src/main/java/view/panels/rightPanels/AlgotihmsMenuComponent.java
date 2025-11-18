package view.panels.rightPanels;

import view.styles.Button;
import view.styles.Colors;

import javax.swing.*;
import java.awt.*;

public class AlgotihmsMenuComponent extends JPanel {
    private Button recorridoBtn;
    private Button mstBtn;
    private Button rutaCortaBtn;

    public AlgotihmsMenuComponent() {
        setOpaque(false);
        setLayout(new FlowLayout(FlowLayout.LEFT, 8, 5));

        recorridoBtn = new Button("Recorrer el grafo", 170, 35, 15, 10, Color.WHITE, Colors.COLOR_BUTTON, Colors.COLOR_BUTTON_HOVER);
        mstBtn = new Button("Árbol de expansion minima", 250, 35, 15, 10, Color.WHITE, Colors.COLOR_BUTTON, Colors.COLOR_BUTTON_HOVER);
        rutaCortaBtn = new Button("Cálculo de rutas más cortas", 250, 35, 15, 10, Color.WHITE, Colors.COLOR_BUTTON, Colors.COLOR_BUTTON_HOVER);

        add(recorridoBtn);
        add(mstBtn);
        add(rutaCortaBtn);
    }

    public Button getRecorridoBtn() {
        return recorridoBtn;
    }

    public Button getMstBtn() {
        return mstBtn;
    }

    public Button getRutaCortaBtn() {
        return rutaCortaBtn;
    }
}

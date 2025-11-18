package view.panels.leftPanels;

import view.styles.Button;
import view.styles.Colors;

import javax.swing.*;
import java.awt.*;

public class ControlsPanel extends JPanel {
    private Button playBtn;
    private Button pauseBtn;
    private Button restartBtn;

    public ControlsPanel() {
        setOpaque(false);
        setLayout(new FlowLayout(FlowLayout.CENTER, 10, 5));

        playBtn = new Button("Iniciar ►", 150, 40, 20, 12, Color.WHITE, Colors.COLOR_BUTTON, Colors.COLOR_BUTTON_HOVER);
        pauseBtn = new Button("Detener", 120, 40, 20, 12, Color.WHITE, Colors.COLOR_BUTTON, Colors.COLOR_BUTTON_HOVER);
        restartBtn = new Button("Reiniciar", 120, 40, 20, 12, Color.WHITE, Colors.COLOR_BUTTON, Colors.COLOR_BUTTON_HOVER);

        add(pauseBtn);
        add(playBtn);
        add(restartBtn);

    }

    public Button getPlayBtn() {
        return playBtn;
    }

    public Button getPauseBtn() {
        return pauseBtn;
    }

    public Button getRestartBtn() {
        return restartBtn;
    }
}

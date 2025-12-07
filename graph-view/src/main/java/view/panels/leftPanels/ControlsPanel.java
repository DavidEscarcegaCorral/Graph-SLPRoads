package view.panels.leftPanels;

import view.styles.Button;
import view.styles.Colors;

import javax.swing.*;
import java.awt.*;

public class ControlsPanel extends JPanel {
    private final Button playBtn;
    private final Button pauseBtn;
    private final Button restartBtn;

    public ControlsPanel() {
        setOpaque(false);
        setLayout(new FlowLayout(FlowLayout.CENTER, 10, 5));

        Font newFont = new Font(Font.DIALOG, Font.BOLD, 32);

        playBtn = new Button("▶ \n Iniciar ", 150, 65, 20, 22, Color.WHITE, Colors.COLOR_BUTTON, Colors.COLOR_BUTTON_HOVER);
        pauseBtn = new Button("⏸", 70, 55, 20, 22, Color.WHITE, Colors.COLOR_BUTTON, Colors.COLOR_BUTTON_HOVER);
        pauseBtn.setFont(newFont);
        restartBtn = new Button("↻", 70, 55, 20, 22, Color.WHITE, Colors.COLOR_BUTTON, Colors.COLOR_BUTTON_HOVER);
        restartBtn.setFont(newFont);

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

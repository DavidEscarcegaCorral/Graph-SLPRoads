package view.panels.rightPanels.header;

import view.styles.Button;
import view.styles.Colors;

import javax.swing.*;
import java.awt.*;

public class HeaderComponent extends JPanel {
    private Button aboutGraphBtn;
    private Button aboutProyectBtn;
    private Button inicioBtn;

    public HeaderComponent() {
        setOpaque(false);
        setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        setPreferredSize(new Dimension(600, 55));

        aboutGraphBtn = new Button("Sobre los Grafos", 200, 35, 15, 5, Color.black, Colors.COLOR_HEADER,
                Colors.COLOR_HEADER_HOOVER);
        aboutGraphBtn.setNewFont(18, "Inter_Regular");
        aboutProyectBtn = new Button("Sobre el Proyecto", 200, 35, 15, 5, Color.black, Colors.COLOR_HEADER,
                Colors.COLOR_HEADER_HOOVER);
        aboutProyectBtn.setNewFont(18, "Inter_Regular");

        inicioBtn = new Button("Inicio", 120, 35, 15, 5, Color.black, Colors.COLOR_HEADER, Colors.COLOR_HEADER_HOOVER);
        inicioBtn.setNewFont(18, "Inter_Regular");

        aboutGraphBtn.addActionListener(e -> {});
        aboutProyectBtn.addActionListener(e -> {});

        add(inicioBtn);
        add(aboutGraphBtn);
        add(aboutProyectBtn);

    }

    public Button getAboutGraphBtn() { return aboutGraphBtn; }
    public Button getAboutProyectBtn() { return aboutProyectBtn; }
    public Button getInicioBtn() { return inicioBtn; }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(Colors.COLOR_HEADER);
        g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);

        g2d.dispose();
    }
}

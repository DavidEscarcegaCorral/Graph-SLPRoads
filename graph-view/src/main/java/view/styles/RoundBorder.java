package view.styles;

import javax.swing.border.LineBorder;
import java.awt.*;

public class RoundBorder extends LineBorder {
    private final int radio;

    public RoundBorder(int radio, Color color) {
        super(color, 1, true);
        this.radio = radio;
    }

    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setColor(lineColor);
        g2.setStroke(new BasicStroke(thickness));
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.drawRoundRect(x, y, width - 1, height - 1, radio, radio);
        g2.dispose();
    }

    @Override
    public Insets getBorderInsets(Component c) {
        return new Insets(this.thickness + 5, this.thickness + 10, this.thickness + 5, this.thickness + 10);
    }

    public boolean isBorderOpaque() {
        return false;
    }
}

package view.styles;

import javax.swing.*;
import java.awt.*;

public class CustomRadioButton extends JRadioButton {
    private static final int ICON_SIZE = 16;
    private Icon customSelectedIcon;
    private Icon customUnselectedIcon;

    public CustomRadioButton(String text) {
        super(text);

        setOpaque(false);
        setForeground(Color.BLACK);
        setFont(FontUtil.loadFont(15, "Inter_SemiBold"));
        setFocusPainted(false);

        this.customUnselectedIcon = new ColorIcon(ICON_SIZE, Color.GRAY, false);
        this.customSelectedIcon = new ColorIcon(ICON_SIZE, Colors.COLOR_BUTTON, true);

        this.setIcon(customUnselectedIcon);
        this.setSelectedIcon(customSelectedIcon);
    }

    private static class ColorIcon implements Icon {
        private final int size;
        private final Color color;
        private final boolean selected;

        public ColorIcon(int size, Color color, boolean selected) {
            this.size = size;
            this.color = color;
            this.selected = selected;
        }

        @Override
        public void paintIcon(Component c, Graphics g, int x, int y) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g2.setColor(color.darker());
            g2.fillOval(x, y, size, size);

            g2.setColor(Color.WHITE);
            g2.fillOval(x + 1, y + 1, size - 2, size - 2);

            if (selected) {
                g2.setColor(color);
                int dotSize = size / 2;
                int dotX = x + (size / 2) - (dotSize / 2);
                int dotY = y + (size / 2) - (dotSize / 2);
                g2.fillOval(dotX, dotY, dotSize, dotSize);
            }

            g2.dispose();
        }

        @Override
        public int getIconWidth() {
            return size;
        }

        @Override
        public int getIconHeight() {
            return size;
        }
    }
}

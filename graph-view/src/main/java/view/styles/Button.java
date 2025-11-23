package view.styles;

import javax.swing.*;
import javax.swing.plaf.basic.BasicButtonUI;
import java.awt.*;
import java.awt.event.MouseEvent;

public class Button extends JButton {
    private boolean hovered = false;
    private int cornerRadius;
    private int width;
    private int height;
    private int fontSize;
    private Color colorText;
    private Color colorBtn;
    private Color colorHover;

    public Button(String text, int width, int height, int fontSize, int cornerRadius, Color colorText, Color colorBtn, Color colorHover) {
        super(text);
        this.width = width;
        this.height = height;
        this.cornerRadius = cornerRadius;
        this.colorText = colorText;
        this.colorBtn = colorBtn;
        this.colorHover = colorHover;
        this.fontSize = fontSize;

        setFont(FontUtil.loadFont(fontSize, "Inter_SemiBold"));
        setForeground(this.colorText);
        setFocusPainted(false);
        setContentAreaFilled(false);
        setBorderPainted(false);
        setOpaque(false);

        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                hovered = true;
                repaint();
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                hovered = false;
                repaint();
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                hovered = false;
                repaint();
            }
        });
    }

    public void setNewFont(int fontSize, String font) {
        setFont(FontUtil.loadFont(fontSize, font));

    }

    public void setHeight(int height) {
        this.height = height;
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(hovered ? colorHover : colorBtn);
        g2d.fillRoundRect(0, 0, getWidth(), getHeight(), cornerRadius, cornerRadius);
        g2d.setStroke(new BasicStroke(2));
        super.paintComponent(g);
        g2d.dispose();

    }

    @Override
    public void updateUI() {
        setUI(new BasicButtonUI());
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(width, height);
    }
}

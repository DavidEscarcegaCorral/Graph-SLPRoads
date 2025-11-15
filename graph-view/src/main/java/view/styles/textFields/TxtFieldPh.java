package view.styles.textFields;

import view.styles.FontUtil;
import view.styles.RoundBorder;
import javax.swing.*;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.PlainDocument;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

public class TxtFieldPh extends JTextField {
    private String txtPlaceholder;
    private int limit;
    private int width;
    private int height;
    private int fontSize;

    public TxtFieldPh(String txtPlaceholder, int width, int height, int fontSize) {
        this.txtPlaceholder = txtPlaceholder;
        this.width = width;
        this.height = height;
        this.fontSize = fontSize;

        setFont(FontUtil.loadFont(fontSize, "Inter_Regular"));
        setPreferredSize(new Dimension(width, height));
        setBorder(new RoundBorder(5, Color.white));
        setOpaque(false);
    }

    public TxtFieldPh(String txtPlaceholder, int limit, int width, int height, int fontSize) {
        this.txtPlaceholder = txtPlaceholder;
        this.limit = limit;
        this.width = width;
        this.height = height;
        this.fontSize = fontSize;

        setFont(FontUtil.loadFont(fontSize, "Inter_Regular"));
        setPreferredSize(new Dimension(width, height));
        setBorder(new RoundBorder(5, Color.white));
        setOpaque(false);
        if (limit > 0) {
            setDocument(new LimitDocument());
        }
    }

    @Override
    protected Document createDefaultModel() {
        if (limit > 0) {
            return new LimitDocument();
        } else {
            return super.createDefaultModel();
        }
    }

    private class LimitDocument extends PlainDocument {
        @Override
        public void insertString( int offset, String  str, AttributeSet attr ) throws BadLocationException {
            if (str == null) return;

            if ((getLength() + str.length()) <= limit) {
                super.insertString(offset, str, attr);
            }
        }

    }

    public void verifyFocus() {
        FocusListener myFocusListener = new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                JTextField source = (JTextField) e.getSource();
            }

            @Override
            public void focusLost(FocusEvent e) {
                JTextField source = (JTextField) e.getSource();
            }
        };
        addFocusListener(myFocusListener);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setStroke(new BasicStroke(2));

        g2d.setColor(Color.WHITE);
        g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);

        if ((getText().isEmpty())) {

            g2d.setColor(new Color(172, 172, 172));
            g2d.setFont(FontUtil.loadFont(this.fontSize, "Inter_Regular"));
            FontMetrics fm = g2d.getFontMetrics();
            int padding = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();
            g2d.drawString(this.txtPlaceholder, 15, padding);
            g2d.dispose();
        }

        super.paintComponent(g);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(width, height);
    }
}

package view.styles.scroll;

import view.styles.Colors;
import view.styles.RoundBorder;

import javax.swing.*;
import java.awt.*;

public class ScrollPaneCustom extends JScrollPane {

    public ScrollPaneCustom() {
        super();
        aplicarEstilo();
    }

    public ScrollPaneCustom(Component view) {
        super(view);
        aplicarEstilo();
    }

    public ScrollPaneCustom(Component view, int vsbPolicy, int hsbPolicy) {
        super(view, vsbPolicy, hsbPolicy);
        aplicarEstilo();
    }

    private void aplicarEstilo() {
        setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        setOpaque(false);
        getViewport().setOpaque(false);

        JScrollBar vertical = getVerticalScrollBar();
        JScrollBar horizontal = getHorizontalScrollBar();

        vertical.setUnitIncrement(20);
        horizontal.setUnitIncrement(20);

        vertical.setUI(new ScrollBarCustom());
        horizontal.setUI(new ScrollBarCustom());

        vertical.setOpaque(true);
        horizontal.setOpaque(true);
        vertical.setVisible(true);
        horizontal.setVisible(true);

        Dimension prefV = vertical.getPreferredSize();
        int minWidth = Math.max(prefV.width, 12);
        vertical.setPreferredSize(new Dimension(minWidth, prefV.height));
        Dimension prefH = horizontal.getPreferredSize();
        int minHeight = Math.max(prefH.height, 12);
        horizontal.setPreferredSize(new Dimension(prefH.width, minHeight));

        revalidate();
        repaint();
    }
}
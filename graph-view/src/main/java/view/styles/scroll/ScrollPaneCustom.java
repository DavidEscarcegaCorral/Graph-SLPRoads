package view.styles.scroll;

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
        setOpaque(false);
        getViewport().setOpaque(false);

        JScrollBar vertical = getVerticalScrollBar();
        JScrollBar horizontal = getHorizontalScrollBar();

        vertical.setOpaque(true);
        horizontal.setOpaque(true);

        vertical.setUnitIncrement(20);
        horizontal.setUnitIncrement(20);
        setBorder(null);

        ScrollBarCustom miUI = new ScrollBarCustom();
        vertical.setUI(miUI);
        horizontal.setUI(miUI);

        // Forzar tamaño visible de la barra vertical y colores de track
        int ancho = miUI.getPreferredSize(vertical).width;
        vertical.setPreferredSize(new Dimension(Math.max(ancho, 12), vertical.getPreferredSize().height));
        vertical.setBackground(new Color(240, 240, 240));
        horizontal.setBackground(new Color(240, 240, 240));

        vertical.setVisible(true);

        this.setBorder(BorderFactory.createEmptyBorder());

    }
}
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
        // NOTA: no forzamos las políticas aquí para respetar las que pueda pasar el creador
        // (por ejemplo: VERTICAL_SCROLLBAR_ALWAYS si se quiere forzar la barra vertical).

        getVerticalScrollBar().setUnitIncrement(20);
        getHorizontalScrollBar().setUnitIncrement(20);
        setBorder(null);

        ScrollBarCustom miUI = new ScrollBarCustom();

        JScrollBar vertical = getVerticalScrollBar();
        vertical.setUI(miUI);

        JScrollBar horizontal = getHorizontalScrollBar();
        horizontal.setUI(miUI);

        this.setBorder(BorderFactory.createEmptyBorder());

        // DEBUG: si quieres forzar que la barra vertical siempre esté visible durante pruebas,
        // puedes pasar JScrollPane.VERTICAL_SCROLLBAR_ALWAYS en el constructor de ScrollPaneCustom.
    }
}
package view.dialogs;

import view.panels.leftPanels.MapPanel;
import view.styles.scroll.ScrollPaneCustom;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class NodesDialog extends JDialog {
    public NodesDialog(Frame owner, List<MapPanel.NodeSummary> nodes) {
        super(owner, "Ciudades y Conexiones", true);
        setSize(700, 400);
        setLocationRelativeTo(owner);

        String[] cols = new String[]{"Nombre", "# Nodo", "Conexiones"};
        DefaultTableModel model = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };

        if (nodes != null) {
            for (MapPanel.NodeSummary ns : nodes) {
                String joins = String.join(", ", ns.getConexiones());
                model.addRow(new Object[]{ ns.getNombre(), ns.getNumeroNodo(), joins });
            }
        }

        JTable table = new JTable(model);
        table.setFillsViewportHeight(true);
        table.setRowHeight(26);

        ScrollPaneCustom sp = new ScrollPaneCustom(table, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        sp.setPreferredSize(new Dimension(660, 300));

        JPanel p = new JPanel(new BorderLayout());
        p.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        p.add(sp, BorderLayout.CENTER);

        JPanel btns = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton close = new JButton("Cerrar");
        close.addActionListener(e -> setVisible(false));
        btns.add(close);
        p.add(btns, BorderLayout.SOUTH);

        setContentPane(p);
    }
}


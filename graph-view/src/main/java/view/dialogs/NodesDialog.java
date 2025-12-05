package view.dialogs;

import view.MainFrame;
import view.panels.leftPanels.MapPanel;
import view.styles.CustomTable;
import view.styles.scroll.ScrollPaneCustom;
import view.styles.Button;
import view.styles.Colors;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class NodesDialog extends JDialog {
    public NodesDialog(MainFrame owner, List<MapPanel.NodeSummary> nodes) {
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

        CustomTable customTable = new CustomTable(model);
        customTable.setFillsViewportHeight(true);
        customTable.setRowHeight(26);

        ScrollPaneCustom sp = new ScrollPaneCustom(customTable, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        sp.setPreferredSize(new Dimension(660, 300));

        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(Color.white);
        p.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        p.add(sp, BorderLayout.CENTER);

        JPanel btns = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        Button close = new Button("Cerrar", 100, 36, 14, 14, Color.WHITE, Colors.COLOR_BUTTON, Colors.COLOR_BUTTON_HOVER);
        close.addActionListener(e -> setVisible(false));
        btns.setBackground(Color.white);
        btns.add(close);
        p.add(btns, BorderLayout.SOUTH);

        setContentPane(p);
    }
}

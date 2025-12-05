package view.styles;

import view.MainFrame;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableModel;
import java.awt.*;

public class CustomTable extends JTable {
    private MainFrame owner;

    public CustomTable(TableModel model) {
        super(model);
        configStyle();

    }

    public void setModel(TableModel model) {
        super.setModel(model);
        alingColumn();
    }

    private void configStyle() {
        //Background, foreground, font, row height
        setOpaque(true);
        this.setForeground(Color.BLACK);
        this.setBackground(Color.white);
        this.setFont(FontUtil.loadFont(16, "Inter_Light_Italic"));
        this.setRowHeight(60);

        //Grid
        this.setShowHorizontalLines(false);
        this.setShowVerticalLines(false);
        this.setGridColor(Colors.COLOR_BUTTON_HOVER);
        setIntercellSpacing(new Dimension(10, 10));

        //Selection colors
        this.setSelectionBackground(Color.white);
        this.setFillsViewportHeight(true);

        //Header
        JTableHeader header = this.getTableHeader();
        header.setOpaque(false);
        alingColumn();

        header.setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus, int row, int column) {
                JLabel label = new JLabel(value.toString());
                label.setOpaque(true);
                label.setBackground(Colors.COLOR_BUTTON_HOVER);
                label.setForeground(Color.WHITE);
                label.setFont(FontUtil.loadFont(18, "Inter_Medium"));
                label.setHorizontalAlignment(SwingConstants.CENTER);
                label.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
                return label;
            }
        });

        header.setReorderingAllowed(false);
    }

    public void alingColumn() {
        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
        renderer.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < this.getColumnCount(); i++) {
            this.getColumnModel().getColumn(i).setCellRenderer(renderer);
        }
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return getColumnName(column).equals("Ver");
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

    }

}

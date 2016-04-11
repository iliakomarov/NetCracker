package Tree2.UI;


import Tree2.src.Info.Statistic;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

/**
 * Created by Ilia Komarov on 13.03.2016.
 */
public class StatisticWindow extends JFrame {
    private JTable table1;
    private JPanel panel1;

    private class StatisticTableModel extends DefaultTableModel {
        private client.src.info.Statistic statistic;

        public StatisticTableModel(client.src.info.Statistic statistic) {
            this.statistic = statistic;
        }

        public int getRowCount() {
            if (statistic != null) return statistic.getSize();
            else return 0;
        }

        public int getColumnCount() {
            if (statistic != null) return statistic.getParamsCount();
            else return 0;
        }

        public String getColumnName(int index) {
            switch (columnIndex) {
                case 0:
                    return "Name";
                case 1:
                    return "Creation Date";
                case 2:
                    return "Stopped";
                case 3:
                    return "Task Time";
                case 4:
                    return "Full Task Time";
            }
            return "";
        }

        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return false;
        }

        public Object getValueAt(int rowIndex, int columnIndex) {
            return statistic.getInfoAt(rowIndex).getInfoAt(columnIndex);
        }
    }

    public StatisticWindow(client.src.info.Statistic statistic) {
        table1 = new JTable(new StatisticTableModel(statistic));
        panel1 = new JPanel(new BorderLayout());
        table1.setSize(new Dimension(640, 480));
        panel1.add(table1, BorderLayout.CENTER);
        getContentPane().add(panel1);
        setMinimumSize(new Dimension(640, 480));
        setTitle("Statistic");
        table1.setBackground(Color.LIGHT_GRAY);
        pack();
    }

}

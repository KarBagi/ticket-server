package AdminPanel;

import CentralServer.Attraction;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Map;

public class AdminFrame extends JFrame {
    private JPanel adminPanel;
    private JTextField NameInput;
    private JTextField AmountInput;
    private JButton refreshButton;
    private JButton applyButton;
    private JButton exitButton;
    private JTable Table;
    private JButton addButton;
    private JButton removeButton;
    private AdminPanelConnectionHandler connectionHandler;
    private ArrayList tableData;

    public AdminFrame(String adminId) {
        connectionHandler = new AdminPanelConnectionHandler(adminId);


        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setContentPane(adminPanel);
        this.pack();
        this.setVisible(true);

        refreshButton.addActionListener(new ActionListener() {


            @Override
            public void actionPerformed(ActionEvent e) {
                refreshTable();
            }
        });
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addAttraction();
            }
        });
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
//        this.Table.getSelectionModel().
//                addListSelectionListener(new ListSelectionListener() {
//            @Override
//            public void valueChanged(ListSelectionEvent e) {
//                JList list = (JList) e.getSource();
//                System.out.println(list.getSelectedIndex());
////                tableData
//            }
//
//        });
        refreshTable();
        Table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                String name = Table.getValueAt(Table.getSelectedRow(), 0).toString();
                String availableTickets = Table.getValueAt(Table.getSelectedRow(), 2).toString().split("/")[0];
                NameInput.setText(name);
                AmountInput.setText(availableTickets);

            }
        });

        applyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedAttractionId = Table.getValueAt(Table.getSelectedRow(), 1).toString();
                String name = NameInput.getText();
                Integer ticketAmount = Integer.parseInt(AmountInput.getText());
                System.out.println(selectedAttractionId);
                connectionHandler.sendUpdateAttractionRequest(selectedAttractionId, name, ticketAmount);
                refreshTable();

            }
        });
        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedAttractionId = Table.getValueAt(Table.getSelectedRow(), 1).toString();
                connectionHandler.sendRemoveAttractionRequest(selectedAttractionId);
                refreshTable();
            }
        });
    }

    public void addAttraction() {
        String attractionName = this.NameInput.getText();
        Integer ticketAmount = Integer.parseInt(this.AmountInput.getText());
        this.NameInput.setText("");
        this.AmountInput.setText("");
        this.connectionHandler.sentAddAttractionRequest(attractionName, ticketAmount);
        refreshTable();

    }

    public void refreshTable() {
        this.tableData = new ArrayList();
        ArrayList element;
        Integer ticketsLeft;
        for (Map.Entry<String, Attraction> entry : this.connectionHandler.sendGetOffersRequest().entrySet()) {
            ticketsLeft = 0;
            for (Map.Entry<Integer, Boolean> entry1 : entry.getValue().getTicketsMap().entrySet()) {
                if (entry1.getValue()) {
                    ticketsLeft++;
                }
            }
            element = new ArrayList();
            element.add(entry.getValue().getName());
            element.add(entry.getKey());
            element.add(String.valueOf(entry.getValue().getTicketsMap().size()));
            element.add(String.valueOf(ticketsLeft));
            tableData.add(element);
        }
        ;
        TableModel dataModel = new AbstractTableModel() {
            String columnNames[] = {"Atrakcja", "ID", "Dostępne miejsca"};


            @Override
            public String getColumnName(int col) {
                return columnNames[col];
            }

            @Override
            public int getRowCount() {
                return tableData.size();
            }

            @Override
            public int getColumnCount() {

                return 3;
            }

            @Override
            public Object getValueAt(int row, int col) {
                ArrayList list = (ArrayList) tableData.get(row);


                switch (col) {
                    case 0 -> {
                        return list.get(0);
                    }
                    case 1 -> {
                        return list.get(1);
                    }
                    case 2 -> {
                        return list.get(3) + "/" + list.get(2);
                    }
                    default -> {
                        return "";
                    }
                }
            }
        };
        dataModel.addTableModelListener(new TableModelListener() {

            @Override
            public void tableChanged(TableModelEvent e) {

            }
        });
        this.Table.setModel(dataModel);
        this.AmountInput.setText("");
        this.NameInput.setText("");
    }


    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        adminPanel = new JPanel();
        adminPanel.setLayout(new GridLayoutManager(5, 6, new Insets(0, 0, 0, 0), -1, -1));
        final JLabel label1 = new JLabel();
        label1.setText("Nazwa Atrakcji");
        adminPanel.add(label1, new GridConstraints(1, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        NameInput = new JTextField();
        adminPanel.add(NameInput, new GridConstraints(2, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label2 = new JLabel();
        label2.setText("Ilość miejsc");
        adminPanel.add(label2, new GridConstraints(1, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        AmountInput = new JTextField();
        AmountInput.setText("");
        adminPanel.add(AmountInput, new GridConstraints(2, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        refreshButton = new JButton();
        refreshButton.setText("Refresh");
        adminPanel.add(refreshButton, new GridConstraints(3, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(83, 30), null, 0, false));
        applyButton = new JButton();
        applyButton.setText("Apply");
        adminPanel.add(applyButton, new GridConstraints(3, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        exitButton = new JButton();
        exitButton.setText("Exit");
        adminPanel.add(exitButton, new GridConstraints(3, 4, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JScrollPane scrollPane1 = new JScrollPane();
        adminPanel.add(scrollPane1, new GridConstraints(0, 1, 1, 4, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        scrollPane1.setBorder(BorderFactory.createTitledBorder(null, "Atrakcje", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        Table = new JTable();
        Table.setAutoCreateRowSorter(false);
        Table.putClientProperty("JTable.autoStartsEdit", Boolean.FALSE);
        scrollPane1.setViewportView(Table);
        final Spacer spacer1 = new Spacer();
        adminPanel.add(spacer1, new GridConstraints(4, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final Spacer spacer2 = new Spacer();
        adminPanel.add(spacer2, new GridConstraints(3, 5, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final Spacer spacer3 = new Spacer();
        adminPanel.add(spacer3, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final Spacer spacer4 = new Spacer();
        adminPanel.add(spacer4, new GridConstraints(4, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final Spacer spacer5 = new Spacer();
        adminPanel.add(spacer5, new GridConstraints(4, 4, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final Spacer spacer6 = new Spacer();
        adminPanel.add(spacer6, new GridConstraints(4, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(83, 14), null, 0, false));
        addButton = new JButton();
        addButton.setText("Add");
        adminPanel.add(addButton, new GridConstraints(3, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        removeButton = new JButton();
        removeButton.setText("Remove");
        adminPanel.add(removeButton, new GridConstraints(2, 4, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return adminPanel;
    }

}

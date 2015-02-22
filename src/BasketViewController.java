

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by aliyounas on 26/01/15.
 */
public class BasketViewController extends javax.swing.JFrame {

    public double totalValue =0.0;

        public BasketViewController() {
            initComponents();
        }

        @SuppressWarnings("unchecked")
        private void initComponents() {

            jScrollPane1 = new javax.swing.JScrollPane();
            shoppingCartTable = new javax.swing.JTable();
            shoppingCartLabel = new javax.swing.JLabel();
            totalLabel = new javax.swing.JLabel();
            totalValueLabel = new javax.swing.JLabel();
            checkOutButton = new javax.swing.JButton();
            removeSelectedButton = new javax.swing.JButton();
            removeSelectedButton.setVisible(false);
            StoreLabel = new javax.swing.JLabel();
            clearCartButton = new javax.swing.JButton();
            splitLabel = new javax.swing.JLabel();
            updateOrder = new javax.swing.JButton();

            setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
            //ArrayList<ArrayList<String>> items = new ArrayList<ArrayList<String>>();
            //items.add(WIDTH, null);

            final DefaultTableModel model = new DefaultTableModel();

            final ArrayList<Item> items = Item.fetchAllItemsInBackground(new Item.MultipleItemCompletionHandler() {
                @Override

                public void succeeded(final ArrayList<Item> items) {


                    totalValueLabel.setFont(new java.awt.Font("Lucida Grande", 0, 18));
                    totalValueLabel.setText("£0.00");

                    model.addColumn("Name");
                    model.addColumn("Price");
                    model.addColumn("Quantity");


                    for (int i =0;i<items.size();i++) {

                        model.addRow(new Object[]{items.get(i).getName(),items.get(i).getPrice() , '1'}); // getPrice method
                        totalValue = totalValue + items.get(i).getPrice(); // replace 4.99 with the item price
                        totalValueLabel.setText("£" + String.format("%.2f", totalValue));
                    }


                    removeSelectedButton.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                            //  int r=shoppingCartTable.getRowCount();
                            int in = shoppingCartTable.getSelectedRow();
                            System.out.println(in + "selected row");
                            //totalValue = totalValue - (items.get(in).getPrice());
                            //totalValue = totalValue - ((items.get(in).getPrice())*(Integer.parseInt(shoppingCartTable.getValueAt(in, 2).toString()))) ;
                            //totalValueLabel.setText("£" + String.format("%.2f", totalValue));
                            model.removeRow(in);
                            items.remove(in);

                            //if (totalValue <=0){ totalValue=0.00; }
                            // totalValueLabel.setText("£" + String.format("%.2f", totalValue));
                            updateOrder.doClick();

                        }
                    });


                    clearCartButton.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                            int totalRows = shoppingCartTable.getRowCount();
                            for (int i = 0; totalRows > i; i++) {

                                model.removeRow(0);
                                items.remove(i);
                                updateOrder.doClick();
                            }

                            //totalValue = 0.00; // use Item.getPrice() method for Item Object or by itemName


                        }
                    });


                    shoppingCartTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
                        public void valueChanged(ListSelectionEvent event) {
                            // do some actions here, for example
                            // print first column value from selected row
                            removeSelectedButton.setVisible(true);
                            //System.out.println(shoppingCartTable.getValueAt(shoppingCartTable.getSelectedRow(), 0).toString());
                        }
                    });

                    updateOrder.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {

                            int totalRows = shoppingCartTable.getRowCount();
                            System.out.println(totalRows + "total rows");

                            double totalValue = 0.0;

                            for (int i = 0; i < totalRows; i++) {

                                totalValue = totalValue + Integer.parseInt(shoppingCartTable.getValueAt(i, 2).toString()) * items.get(i).getPrice();
                            }
                            totalValueLabel.setText("£" + String.format("%.2f", totalValue));

                        }
                    });


                    checkOutButton.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                            checkOutButtonActionPerformed(evt);
                        }
                    });
                }

                @Override
                public void failed(SQLException exception) {

                }
            });

            shoppingCartTable.setModel(model);

            jScrollPane1.setViewportView(shoppingCartTable);

            shoppingCartLabel.setText("Shopping Cart");

            totalLabel.setFont(new java.awt.Font("Lucida Grande", 0, 18));
            totalLabel.setText("Total:");


            checkOutButton.setText("Check Out");
            updateOrder.setText("Update");

            removeSelectedButton.setText("Remove Selected Item");

            StoreLabel.setFont(new java.awt.Font("Lucida Grande", 0, 30));
            StoreLabel.setText("ORDR Basket");

            clearCartButton.setText("Clear Cart");

            javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
            getContentPane().setLayout(layout);
            layout.setHorizontalGroup(
                    layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                    .addContainerGap(14, Short.MAX_VALUE)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(layout.createSequentialGroup()
                                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                            .addGroup(layout.createSequentialGroup()
                                                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                                            .addGroup(layout.createSequentialGroup()
                                                                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                                                                            .addGroup(layout.createSequentialGroup()
                                                                                    .addGap(100, 100, 100)
                                                                                    .addComponent(StoreLabel))))
                                                            .addComponent(shoppingCartLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 362, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                    .addGroup(layout.createSequentialGroup()

                                                                            .addComponent(updateOrder)
                                                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                            .addComponent(clearCartButton)
                                                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                            .addComponent(removeSelectedButton))

                                                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                                                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                                    .addGap(357, 357, 357))
                                                                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                                                    .addGroup(layout.createSequentialGroup()
                                                                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED))
                                                                                    .addGroup(layout.createSequentialGroup()
                                                                                            .addComponent(totalLabel)
                                                                                            .addGap(1, 1, 1)
                                                                                            .addComponent(totalValueLabel))))))
                                                    .addContainerGap(12, Short.MAX_VALUE))
                                            .addGroup(layout.createSequentialGroup()
                                                    .addComponent(splitLabel)
                                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                    .addComponent(checkOutButton, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addGap(30, 30, 30))))
            );
            layout.setVerticalGroup(
                    layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                    .addGap(9, 9, 9)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(checkOutButton, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGroup(layout.createSequentialGroup()
                                                    .addComponent(StoreLabel)
                                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE))
                                                    .addGap(18, 18, 18)
                                                    .addComponent(shoppingCartLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                            .addComponent(removeSelectedButton)
                                                            .addComponent(clearCartButton)
                                                            .addComponent(updateOrder))
                                                    .addGap(27, 27, 27)
                                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                    )
                                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                    )
                                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                    .addComponent(splitLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                            .addComponent(totalLabel)
                                                            .addComponent(totalValueLabel))))
                                    .addContainerGap(41, Short.MAX_VALUE))
            );

            pack();
        }

        private void checkOutButtonActionPerformed(java.awt.event.ActionEvent evt) {
            // TODO add your handling code here:
        }

        /**
         * @param args the command line arguments
         */
        public static void main(String args[]) {


            try {
                for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                    if ("Nimbus".equals(info.getName())) {
                        javax.swing.UIManager.setLookAndFeel(info.getClassName());
                        break;
                    }
                }
            } catch (ClassNotFoundException ex) {
                java.util.logging.Logger.getLogger(BasketViewController.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            } catch (InstantiationException ex) {
                java.util.logging.Logger.getLogger(BasketViewController.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            } catch (IllegalAccessException ex) {
                java.util.logging.Logger.getLogger(BasketViewController.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            } catch (javax.swing.UnsupportedLookAndFeelException ex) {
                java.util.logging.Logger.getLogger(BasketViewController.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            }


            java.awt.EventQueue.invokeLater(new Runnable() {
                public void run() {
                    new BasketViewController().setVisible(true);
                }
            });
        }
        private javax.swing.JButton checkOutButton;
        private javax.swing.JButton clearCartButton;
        private javax.swing.JScrollPane jScrollPane1;
        private javax.swing.JButton removeSelectedButton;
        private javax.swing.JLabel shoppingCartLabel;
        private javax.swing.JTable shoppingCartTable;
        private javax.swing.JLabel splitLabel;
        private javax.swing.JLabel totalLabel;
        private javax.swing.JLabel totalValueLabel;
        private javax.swing.JLabel StoreLabel;
    private javax.swing.JButton updateOrder;
    }



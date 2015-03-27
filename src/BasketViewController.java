

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by aliyounas on 26/01/15.
 */
public class BasketViewController extends ViewController {

    private JLabel totalValueLabel;
    private double totalValue = 0.0;
    final private JTable table = new JTable();
    JLabel storeLabel = new javax.swing.JLabel();

    public BasketViewController() { initComponents(); }


    private void initComponents() {

        //Init table
        JScrollPane scrollPane = new javax.swing.JScrollPane();
        JLabel shoppingCartLabel = new javax.swing.JLabel();
        JLabel totalLabel = new javax.swing.JLabel();
        totalValueLabel = new javax.swing.JLabel();
        JButton checkOutButton = new javax.swing.JButton();
        JButton removeSelectedButton = new javax.swing.JButton();
        JButton clearCartButton = new javax.swing.JButton();
        JLabel splitLabel = new javax.swing.JLabel();
        JButton updateOrder = new javax.swing.JButton();



        //ALI!!!!!!
        //LOOK HERE!
        //TODO

        totalValueLabel.setFont(new java.awt.Font("Lucida Grande", 0, 18));
        totalValueLabel.setText("£0.00");



        removeSelectedButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
//                        int in = shoppingCartTable.getSelectedRow();
//                        System.out.println(in + "selected row");
//                        model.removeRow(in);
//                        items.remove(in);
//                        updateOrder.doClick();
            }
        });

        clearCartButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
//                        int totalRows = shoppingCartTable.getRowCount();
//                        for (int i = 0; totalRows > i; i++) {
//
//                            model.removeRow(0);
//                            items.remove(i);
//                            updateOrder.doClick();
//                        }
            }
        });


        updateOrder.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                int totalRows = table.getRowCount();
                System.out.println(totalRows + "total rows");

                totalValue = 0.0;
            }
        });

        checkOutButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                checkOutButtonActionPerformed(evt);
            }
        });
//

//        Item.fetchAllItemsInBackground(new Item.MultipleItemCompletionHandler() {
//
//            @Override
//            public void succeeded(final ArrayList<Item> items) {
//
//
//                totalValueLabel.setFont(new java.awt.Font("Lucida Grande", 0, 18));
//                totalValueLabel.setText("£0.00");
//
//                model.addColumn("Name");
//                model.addColumn("Price");
//                model.addColumn("Quantity");
//
//
//                for (int i = 0; i < items.size(); i++) {
//
//                    model.addRow(new Object[]{items.get(i).getName(), items.get(i).getPrice(), '1'});
//                    totalValue = totalValue + items.get(i).getPrice();
//                    totalValueLabel.setText("£" + String.format("%.2f", totalValue));
//                }
//
//
//                removeSelectedButton.addActionListener(new java.awt.event.ActionListener() {
//                    public void actionPerformed(java.awt.event.ActionEvent evt) {
//                        int in = shoppingCartTable.getSelectedRow();
//                        System.out.println(in + "selected row");
//                        model.removeRow(in);
//                        items.remove(in);
//                        updateOrder.doClick();
//
//                    }
//                });
//
//
//                clearCartButton.addActionListener(new java.awt.event.ActionListener() {
//                    public void actionPerformed(java.awt.event.ActionEvent evt) {
//                        int totalRows = shoppingCartTable.getRowCount();
//                        for (int i = 0; totalRows > i; i++) {
//
//                            model.removeRow(0);
//                            items.remove(i);
//                            updateOrder.doClick();
//                        }
//                    }
//                });
//
//
//                shoppingCartTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
//                    public void valueChanged(ListSelectionEvent event) {
//                        removeSelectedButton.setVisible(true);
//                    }
//                });
//
//                updateOrder.addActionListener(new java.awt.event.ActionListener() {
//                    public void actionPerformed(java.awt.event.ActionEvent evt) {
//                        int totalRows = shoppingCartTable.getRowCount();
//                        System.out.println(totalRows + "total rows");
//
//                        double totalValue = 0.0;
//
//                        for (int i = 0; i < totalRows; i++) {
//
//                            totalValue = totalValue + Integer.parseInt(shoppingCartTable.getValueAt(i, 2).toString()) * items.get(i).getPrice();
//                        }
//                        totalValueLabel.setText("£" + String.format("%.2f", totalValue));
//
//                    }
//                });
//
//
//                checkOutButton.addActionListener(new java.awt.event.ActionListener() {
//                    public void actionPerformed(java.awt.event.ActionEvent evt) {
//                        checkOutButtonActionPerformed(evt);
//                    }
//                });
//            }
//
//            @Override
//            public void failed(SQLException exception) {
//                System.out.println(exception.getLocalizedMessage());
//            }
//        });

        //shoppingCartTable.setModel(model);

        scrollPane.setViewportView(table);

        shoppingCartLabel.setText("Shopping Cart");

        totalLabel.setFont(new java.awt.Font("Lucida Grande", 0, 18));
        totalLabel.setText("Total:");


        checkOutButton.setText("Check Out");
        updateOrder.setText("Update");

        removeSelectedButton.setText("Remove Selected Item");

        storeLabel.setFont(new java.awt.Font("Lucida Grande", 0, 30));

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
                                                                                .addComponent(storeLabel))))
                                                        .addComponent(shoppingCartLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                                .addComponent(scrollPane, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 362, javax.swing.GroupLayout.PREFERRED_SIZE)
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
                                                .addComponent(storeLabel)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE))
                                                .addGap(18, 18, 18)
                                                .addComponent(shoppingCartLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(scrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE)
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

    private void refreshTableData() {
        DefaultTableModel tableModel = new DefaultTableModel();
        tableModel.addColumn("Name");
        tableModel.addColumn("Price");
        tableModel.addColumn("Quantity");
        if (User.getCurrentUser() != null) {
            HashMap<Integer, Item.BasketItem> basket = User.getCurrentUser().getBasket();
            if (basket != null && basket.size() > 0) {
                for (Item.BasketItem bItem : basket.values()) {

                    Double itemPrice = bItem.getItem().getPrice();
                    tableModel.addRow(new Object[]{bItem.getItem().getName(), itemPrice, '1'});
                    totalValue = totalValue + itemPrice;
                    totalValueLabel.setText("£" + String.format("%.2f", totalValue));
                }
                storeLabel.setText("");
            } else {
                storeLabel.setText("Your basket is empty");
            }
        } else {
            storeLabel.setText("Please log in to add items to your basket");
        }
        table.setModel(tableModel);
    }

    private void checkOutButtonActionPerformed(java.awt.event.ActionEvent evt) {
        Item.fetchAllItemsInBackground(new Item.MultipleItemCompletionHandler() {
            @Override
            public void succeeded(ArrayList<Item> items) {
                HashMap<Integer, Item.BasketItem> basket = new HashMap<Integer, Item.BasketItem>(items.size());
                for (Item item : items) {
                    Item.BasketItem bItem = Item.BasketItem.makeBasketItem(30, item, 3);
                    basket.put(item.getID(), bItem);
                }
                if (User.getCurrentUser() != null) {
                    OrderCompletionViewController orderVC = new OrderCompletionViewController(basket);
                    ApplicationManager.setDisplayedViewController(orderVC);
                }else {
                    JOptionPane.showMessageDialog(getView(), "Please login before trying to checkout.");
                }
            }

            @Override
            public void failed(SQLException exception) {

            }
        });
    }

    @Override
    public void viewWillAppear() {
        refreshTableData();
    }

    //Getters
    @Override
    public Component getView() {
        return getContentPane();
    }

    @Override
    public String getButtonLabel() {
        return "Basket";
    }
}



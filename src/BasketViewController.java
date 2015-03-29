

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by aliyounas on 26/01/15.
 */
public class BasketViewController extends ViewController {

    private JLabel totalPriceLabel = new JLabel();
    private double totalPrice = 0.0;
    private final JTable table = new JTable();
    private JLabel statusLabel = new JLabel();
    private DefaultTableModel tableModel;
    private ArrayList<Item.BasketItem> orderedBasket;

    public BasketViewController() { initComponents(); }


    private void initComponents() {

        //Init table
        JScrollPane scrollPane = new JScrollPane();
        JLabel shoppingCartLabel = new JLabel();
        JLabel totalLabel = new JLabel();
        JButton checkOutButton = new JButton();
        JButton removeSelectedButton = new JButton();
        JButton clearCartButton = new JButton();
        JLabel splitLabel = new JLabel();
        JButton updateOrder = new JButton();
        totalPriceLabel.setFont(new java.awt.Font("Lucida Grande", 0, 18));
        totalPriceLabel.setText("£0.00");



        removeSelectedButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                if (User.getCurrentUser() != null && table.getSelectedRow() > -1) {
                    Item.BasketItem selectedBItem = orderedBasket.get(table.getSelectedRow());
                    User.getCurrentUser().removeItemFromBasket(selectedBItem);
                    orderedBasket.remove(table.getSelectedRow());
                    tableModel.removeRow(table.getSelectedRow());
                    if (orderedBasket.size() == 0) {
                        statusLabel.setText("Your basket is empty");
                    }
                }
            }
        });

        clearCartButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {

            }
        });


        updateOrder.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                int totalRows = table.getRowCount();
                System.out.println(totalRows + "total rows");
                totalPrice = 0.0;
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
//                totalPriceLabel.setFont(new java.awt.Font("Lucida Grande", 0, 18));
//                totalPriceLabel.setText("£0.00");
//
//                model.addColumn("Name");
//                model.addColumn("Price");
//                model.addColumn("Quantity");
//
//
//                for (int i = 0; i < items.size(); i++) {
//
//                    model.addRow(new Object[]{items.get(i).getName(), items.get(i).getPrice(), '1'});
//                    totalPrice = totalPrice + items.get(i).getPrice();
//                    totalPriceLabel.setText("£" + String.format("%.2f", totalPrice));
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
//                        double totalPrice = 0.0;
//
//                        for (int i = 0; i < totalRows; i++) {
//
//                            totalPrice = totalPrice + Integer.parseInt(shoppingCartTable.getValueAt(i, 2).toString()) * items.get(i).getPrice();
//                        }
//                        totalPriceLabel.setText("£" + String.format("%.2f", totalPrice));
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

        statusLabel.setFont(new java.awt.Font("Lucida Grande", 0, 30));

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
                                                                                .addComponent(statusLabel))))
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
                                                                                        .addComponent(totalPriceLabel))))))
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
                                                .addComponent(statusLabel)
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
                                                        .addComponent(totalPriceLabel))))
                                .addContainerGap(41, Short.MAX_VALUE))
        );

        pack();
    }

    private void refreshTableData() {
        tableModel = new DefaultTableModel();
        tableModel.addColumn("Name");
        tableModel.addColumn("Price");
        tableModel.addColumn("Quantity");
        table.setModel(tableModel);
        orderedBasket = new ArrayList<Item.BasketItem>();

        if (User.getCurrentUser() != null) {

            final Runnable showBasket = new Runnable() {
                @Override
                public void run() {
                    HashMap<Integer, Item.BasketItem> basket = User.getCurrentUser().getBasket();
                    if (basket != null && basket.size() > 0) {
                        float totalPrice = 0;
                        for (Item.BasketItem bItem : basket.values()) {
                            Double itemPrice = bItem.getItem().getPrice();
                            tableModel.addRow(new Object[]{bItem.getItem().getName(), itemPrice, bItem.getQuantity()});
                            totalPrice += itemPrice * bItem.getQuantity();
                            orderedBasket.add(bItem);
                        }
                        totalPriceLabel.setText("£" + String.format("%.2f", totalPrice));
                        statusLabel.setText("");
                    } else {
                        statusLabel.setText("Your basket is empty");
                        totalPriceLabel.setText("");
                    }
                }
            };

            User.getCurrentUser().refreshBasketInBackground(new User.BasketRefreshCompletionHandler() {
                @Override
                public void succeeded() {
                    showBasket.run();
                }

                @Override
                public void failed(SQLException exception) {
                    showBasket.run();
                    statusLabel.setText("Unable to refresh basket");
                }
            });

        } else {
            statusLabel.setText("Please log in to add items to your basket");
            totalPriceLabel.setText("");
        }
    }

    private void checkOutButtonActionPerformed(java.awt.event.ActionEvent evt) {
        if (User.getCurrentUser() != null) {
            HashMap<Integer, Item.BasketItem> basket = User.getCurrentUser().getBasket();
            if (basket.size() > 0) {
                OrderCompletionViewController orderVC = new OrderCompletionViewController(basket);
                ApplicationManager.setDisplayedViewController(orderVC);
            }
        }
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




import net.miginfocom.swing.MigLayout;
import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.HashMap;

/**
 * Created by James on 13/02/2015.
 */
public class OrderCompletionViewController extends ViewController {

    public OrderCompletionViewController(HashMap<Integer, Item.BasketItem> basket) throws HeadlessException {
        //TODO: Either get rid of Headless exception of handle it
        this.basket = basket;
        initialiseGUI();
    }

    public double Ordertotal, Itemtotal, Itemcalc, PostingTotal;
    private JPanel panel1;
    private HashMap<Integer, Item.BasketItem> basket;


    public void initialiseGUI() {
        if (User.getCurrentUser() != null) {

            Container container = getContentPane();
            container.setLayout(new GridBagLayout());
            GridBagConstraints cns = new GridBagConstraints();

            JPanel DetailsPane = new JPanel();
            DetailsPane.setLayout(new MigLayout());
            cns.gridx = 0;
            cns.gridy = 0;
            cns.gridwidth = 1;
            cns.weightx = 0.9;
            cns.weighty = 0.3;
            cns.anchor = GridBagConstraints.FIRST_LINE_START;
            cns.fill = GridBagConstraints.BOTH;
            DetailsPane.setBackground(Color.WHITE);
            container.add(DetailsPane, cns);

            JPanel TotalPane = new JPanel();
            TotalPane.setLayout(new MigLayout());
//         TotalPane.setBackground(Color.orange);
            cns.gridx = 1;
            cns.gridy = 0;
            cns.weighty = 0.1;
            cns.weightx = 0.3;
            cns.anchor = GridBagConstraints.FIRST_LINE_END;
            cns.fill = GridBagConstraints.BOTH;
            container.add(TotalPane, cns);

            JPanel ItemPane = new JPanel();
            JScrollPane scroll = new JScrollPane(ItemPane);
            ItemPane.setLayout(new BoxLayout(ItemPane, BoxLayout.Y_AXIS));
            ItemPane.setBackground(Color.cyan);
            cns.gridx = 0;
            cns.gridy = 1;
            cns.gridwidth = 2;
            cns.weightx = 1.0;
            cns.weighty = 0.7;
            cns.anchor = GridBagConstraints.LAST_LINE_START;
            cns.fill = GridBagConstraints.BOTH;
            container.add(scroll, cns);

            JLabel UserForename = new JLabel(User.getCurrentUser().getFirstName());
            DetailsPane.add(UserForename, "split 2");
            JLabel UserSurname = new JLabel(User.getCurrentUser().getLastName());
            DetailsPane.add(UserSurname);
            JLabel PaymentMethodTitle = new JLabel("Payment Method:");
            DetailsPane.add(PaymentMethodTitle, "gapleft 100, wrap 15");
            JLabel DeliveryAddress = new JLabel("Delivery Address");
            DetailsPane.add(DeliveryAddress);
//            JLabel PaymentMethod = new JLabel(Paymentmethod);
//            DetailsPane.add(PaymentMethod, "gapleft 100, wrap");
//            JLabel DeliveryRoad = new JLabel(Roadname);
//            DetailsPane.add(DeliveryRoad, "split 2");
//            JLabel DeliveryRoadNum = new JLabel(Roadnumber);
//            DetailsPane.add(DeliveryRoadNum, "wrap");
//            JLabel DeliveryTown = new JLabel(Town);
//            DetailsPane.add(DeliveryTown, "split 2");
//            JLabel DeliveryPostCode = new JLabel(Postcode);
//            DetailsPane.add(DeliveryPostCode, "wrap");
//            JLabel PhoneTitle = new JLabel("Phone:");
//            DetailsPane.add(PhoneTitle, "split 2");
//            JLabel Phone = new JLabel(Phonenumber);
//            DetailsPane.add(Phone);

            final JButton OrderFinish = new JButton("Buy Now");
            TotalPane.add(OrderFinish, "cell 1 0 ,wrap");
            JLabel ItemTitle = new JLabel("Items:");
            TotalPane.add(ItemTitle, "cell 1 1");
            JLabel Item££sign = new JLabel("£");
            TotalPane.add(Item££sign, "split 2");
            final JLabel ItemTotal = new JLabel("null");
            TotalPane.add(ItemTotal, "cell 2 1");
            JLabel PackagingTitle = new JLabel("Postage & Packing:");
            TotalPane.add(PackagingTitle, "Cell 1 2");
            JLabel Packing£sign = new JLabel("£");
            TotalPane.add(Packing£sign, "split 2");
            final JLabel PackagingTotal = new JLabel("00.00");
            TotalPane.add(PackagingTotal, "cell 2 2, wrap 15");
            JSeparator TotalSeparator = new JSeparator(SwingConstants.HORIZONTAL);
            TotalSeparator.setBackground(Color.CYAN);
            TotalPane.add(TotalSeparator, "cell 0 4,pushx, growx, span,wrap");
            JLabel OrderTotalTitle = new JLabel("Order Total:");
            TotalPane.add(OrderTotalTitle, "cell 1 5");
            JLabel Order£sign = new JLabel("£");
            TotalPane.add(Order£sign, "split 2");
            final JLabel OrderTotal = new JLabel("null");
            TotalPane.add(OrderTotal, "wrap");
            final JRadioButton StandardD = new JRadioButton("Standard Delivery");
            TotalPane.add(StandardD, "cell 1 6, wrap");
            final JRadioButton ExpressD = new JRadioButton("Express Delivery");
            TotalPane.add(ExpressD, "cell 1 7");
            ButtonGroup Delivery = new ButtonGroup();
            Delivery.add(StandardD);
            Delivery.add(ExpressD);
//        StandardD.setSelected(true); doesn't up date immediately

//        ImageIcon icon = new ImageIcon("E:\\Downloads\\iron.png");
//        JLabel ItemImg = new JLabel(icon);
//        ItemPane.add(ItemImg, "cell 0 1,span 2 2, gap right 15");
//        JLabel ItemName = new JLabel("ItemName");
//        ItemPane.add(ItemName, "cell 2 1");
//        JLabel ItemCost£sign = new JLabel("£");
//        ItemPane.add(ItemCost£sign,"cell 2 2");
//        JLabel ItemCost = new JLabel("ItemCost");
//        ItemPane.add(ItemCost, "cell 2 2");
//        JLabel ItemQuantity = new JLabel("Quantity:");
//        ItemPane.add(ItemQuantity,"cell 2 3");
//        JLabel ItemQuantitynum =new JLabel("null");
//        ItemPane.add(ItemQuantitynum, "cell 2 3");
//        JLabel StandardShip = new JLabel("Standard Shipping");
//        ItemPane.add(StandardShip,"cell 5 2, center");
//        JLabel ExpressShipping = new JLabel("Express Shipping");
//        JRadioButton StandardD = new JRadioButton();
//        JRadioButton ExpressD = new JRadioButton();
//        ButtonGroup Packaging = new ButtonGroup();
//        Packaging.add(StandardD);
//        Packaging.add(ExpressD);
            final JPanel[] Ipanel = new JPanel[100];
            JLabel[] INumlabel = new JLabel[100];
            JLabel[] INumlabelTitle = new JLabel[100];
            JLabel[] IImg = new JLabel[100];
            JLabel[] Iname = new JLabel[100];
            JLabel[] I£sign = new JLabel[100];
            final JLabel[] IPrice = new JLabel[100];
            JLabel[] IQuantity = new JLabel[100];
            JLabel[] IQuantityNum = new JLabel[100];
//        JLabel[] IStandardTitle = new JLabel[100];
//        final JRadioButton[] IStandard = new JRadioButton[100];
//        JLabel[] IExpressTitle= new JLabel[100];
//        final JRadioButton[] IExpress = new JRadioButton[100];
//        ButtonGroup[] Packaging = new ButtonGroup[100];
//        ChangeListener[] Change = new ChangeListener[100];

            int i = 0;
            for (Item.BasketItem bItem : basket.values()) {
                Ipanel[i] = new JPanel();
                INumlabel[i] = new JLabel();
                Iname[i] = new JLabel(bItem.getItem().getName());
                INumlabelTitle[i] = new JLabel("Item Number:");
                IImg[i] = new JLabel();


                I£sign[i] = new JLabel("£");

                IPrice[i] = new JLabel(String.valueOf(bItem.getItem().getPrice()));
                IQuantity[i] = new JLabel("Quantity:");
                IQuantityNum[i] = new JLabel("null");
                IQuantityNum[i].setText(String.valueOf(bItem.getQuantity()));
//            Packaging[i]=new ButtonGroup();
//            IStandardTitle[i]=new JLabel("Standard Shipping");
//            IStandard[i]=new JRadioButton();
//            Packaging[i].add(IStandard[i]);
//            IExpressTitle[i]=new JLabel("Express Shipping");
//            IExpress[i]=new JRadioButton();
//            Packaging[i].add(IExpress[i]);

                Ipanel[i].setBorder(BorderFactory.createLineBorder(Color.BLACK));
                Ipanel[i].setLayout(new MigLayout());

                INumlabel[i].setText(String.valueOf(i));
                Ipanel[i].add(INumlabelTitle[i]); // doesn't need to be in an array
                Ipanel[i].add(INumlabel[i], "split 2");
                Ipanel[i].add(INumlabel[i], "wrap");
                Ipanel[i].add(Iname[i], "wrap");
//            Ipanel[i].add(IStandardTitle[i]);//doesn't need to be in an array
//            Ipanel[i].add(IStandard[i], "wrap");
                //Haven't figured out how to put a change listener in an array yet
                Ipanel[i].add(I£sign[i], "split 2");// doesn't need to be an array
                Ipanel[i].add(IPrice[i], "wrap");
//            Ipanel[i].add(IExpressTitle[i]);//doesn't need to be an array
//            Ipanel[i].add(IExpress[i],"wrap");
                //Haven't figured out how to put a change listener in an array yet
                Ipanel[i].add(IQuantity[i], "split 2");//doesn't need to be in array
                Ipanel[i].add(IQuantityNum[i]);

                Itemcalc = Double.parseDouble(IPrice[i].getText());
                Itemcalc = Itemcalc * bItem.getQuantity();

                Itemtotal += Itemcalc;

                ItemPane.add(Ipanel[i]);
                i++;
            }
            ItemTotal.setText(new DecimalFormat("######.##").format(Itemtotal));
            ChangeListener Change = new ChangeListener() {
                @Override
                public void stateChanged(ChangeEvent e) {
                    if (StandardD.isSelected()) {
                        PostingTotal = 2.27 * basket.size();
                        PackagingTotal.setText(new DecimalFormat("##.##").format(PostingTotal));
                        Ordertotal = Itemtotal + PostingTotal;
                        OrderTotal.setText(new DecimalFormat("##.##").format(Ordertotal));
                    } else if (ExpressD.isSelected()) {
                        PostingTotal = 3.73 * basket.size();
                        PackagingTotal.setText(new DecimalFormat("##.##").format(PostingTotal));
                        Ordertotal = Itemtotal + PostingTotal;
                        OrderTotal.setText(new DecimalFormat("##.##").format(Ordertotal));
                    }

                }

            };
            StandardD.addChangeListener(Change);
            ExpressD.addChangeListener(Change);
            StandardD.doClick();

            ActionListener BuyNow = new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        for (int i = 0; i < basket.size(); i++) {
//                            String query = "Insert into Completed (UserID,ItemID,ItemPrice,Date) values(?,?,?,?)";
//                            PreparedStatement stm = DatabaseManager.getSharedDbConnection().prepareStatement(query);
//                            stm.setInt(1, userID);
//                            stm.setInt(2, ItemID.get(i));
//                            String Itemprice = IPrice[i].getText();
//                            stm.setString(3, Itemprice);
//                            stm.setDate(4, timeNow);
//                            stm.execute();
//                            System.out.println("works");
                        }

                    } catch (Exception ex) {
                        System.out.println(ex);
                    }

                }
            };

            OrderFinish.addActionListener(BuyNow);

//            orderView.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
//            orderView.setSize(1000, 1000);
//            orderView.pack();
//            orderView.setVisible(true);
//            orderView.setLocationRelativeTo(null);
        } else {
            //TODO: display no logged in user message
        }


    }

    //Getters
    @Override
    public Component getView() {
        return getContentPane();
    }
}

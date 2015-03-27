
import net.miginfocom.swing.MigLayout;
import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.HashMap;
import static java.lang.System.*;

/**
 * Created by James on 13/02/2015.
 */
public class OrderCompletionViewController extends javax.swing.JFrame implements ViewController {

    public OrderCompletionViewController(HashMap<Integer, Item.BasketItem> basket) throws HeadlessException {
        //TODO: Either get rid of Headless exception of handle it
        this.basket = basket; //Gets user HasMap basket
        initialiseGUI(); // Starts the gui
    }

    public double orderTotal, itemTotal, itemCalc, postingTotal; //Used to calculate the total
    String paymentMethod, roadName, roadNumber, town, postCode, phoneNumber; // Used to store user delivery details
    private HashMap<Integer, Item.BasketItem> basket; //contains the item details passed from basket
    private static final String jUSERID_COLUMN_NAME = "user_id";
    private static final String jItemID_COLUMN_NAME = "item_id";
    private static final String jPRICE_COLUMN_NAME = "price";
    private static final String jSTOCK_COLUMN_NAME = "stock_qty";
    private static final String jDATE_COLUMN_NAME = "date";
    private static final String jTABLE_NAME="completeorders";


    public void initialiseGUI() {
        if (User.getCurrentUser() != null) { //Checks if user is logged into the system.
            enterDetails();//prompts user to enter delivery details.


            Container container = getContentPane(); //Creates contentPane container which I use to display my JPanels
            container.setLayout(new GridBagLayout()); //Sets Layout of my ContentPane to GridBagLayout.
            GridBagConstraints cns = new GridBagConstraints(); //Create GridBagConstraints which allows me to manipulate the layout.

            JPanel detailsPane = new JPanel(); //Creates one of 3 my JPanels I add to my ContentPane container.
            detailsPane.setLayout(new MigLayout());//Sets my JPanel a Free and Open Source Layout Manager available at http://www.miglayout.com/
            cns.gridx = 0; //GridBagConstraints start.
            cns.gridy = 0;
            cns.gridwidth = 1;
            cns.weightx = 0.9;
            cns.weighty = 0.3;
            cns.anchor = GridBagConstraints.FIRST_LINE_START;
            cns.fill = GridBagConstraints.BOTH; //GridBagConstraints end this set of Constraints ensure that the detailsPane is displayed at the top Left.
            detailsPane.setBackground(Color.WHITE);//Sets the detailsPane JPanel background to white.
            container.add(detailsPane, cns); //Adds detailsPane to containers using the GridBagConstraints.

            JPanel totalPane = new JPanel(); //Creates one of my 3 JPanels I add to my ContentPane container.
            totalPane.setLayout(new MigLayout());//Sets my JPanel a Free and Open Source Layout Manager available at http://www.miglayout.com/
            cns.gridx = 1;//GridBagConstraints start.
            cns.gridy = 0;
            cns.weighty = 0.1;
            cns.weightx = 0.3;
            cns.anchor = GridBagConstraints.FIRST_LINE_END;
            cns.fill = GridBagConstraints.BOTH;//GridBagConstraints end this set of Constraints ensure that the TotalPane is displayed at the top Right.
            container.add(totalPane, cns);//Adds TotalPane to containers using the GridBagConstraints.

            JPanel itemPane = new JPanel(); //Creates Last of my 3 JPanels I add to my ContentPane container.
            JScrollPane scroll = new JScrollPane(itemPane);//I create JScrollPane and add itemPane this allows my JPanel to become scrollable unlike my other two Panels.
            itemPane.setLayout(new BoxLayout(itemPane, BoxLayout.Y_AXIS)); // As Opposed to using MIG Layout I use BoxLayout because of the it handles new content being added to the Layout.
            itemPane.setBackground(Color.cyan);
            cns.gridx = 0;//GridBagConstraints start.
            cns.gridy = 1;
            cns.gridwidth = 2;
            cns.weightx = 1.0;
            cns.weighty = 0.7;
            cns.anchor = GridBagConstraints.LAST_LINE_START;
            cns.fill = GridBagConstraints.BOTH;//GridBagConstraints end this set of Constraints ensure that the Scroll is displayed beneath my other two JPanel.
            container.add(scroll, cns);//Adds scroll to container.

            JLabel userForename = new JLabel(User.getCurrentUser().getFirstName());//Creates a JLabel and sets it to the value of the currently logged in user.
            detailsPane.add(userForename, "split 2"); // adds userForename detailsPane, MigLayout unlike box layouts handles adding content via cells 'split 2' is making the next added content share the cell
            JLabel userSurname = new JLabel(User.getCurrentUser().getLastName());//Creates JLabel and sets it to the value of the users Surname.
            detailsPane.add(userSurname);// Adds  userSurname to previously userForename Cell.
            JLabel paymentMethodTitle = new JLabel("Payment Method:");//paymentMethodTitle Label
            detailsPane.add(paymentMethodTitle, "gapleft 100, wrap 15");// Adds the Label to the Details pane, gapleft 100 moves the label, wrap moves cell down.
            JLabel deliveryAddress = new JLabel("Delivery Address");//Creates new label and sets its contents
            detailsPane.add(deliveryAddress);// adds label to pane
            JLabel billing = new JLabel(paymentMethod);//Similar to before.
            detailsPane.add(billing, "gapleft 100, wrap");
            JLabel deliveryRoad = new JLabel(roadName);
            detailsPane.add(deliveryRoad, "split 2");
            JLabel deliveryRoadNum = new JLabel(roadNumber);
            detailsPane.add(deliveryRoadNum, "wrap");
            JLabel deliveryTown = new JLabel(town);
            detailsPane.add(deliveryTown, "split 2");
            JLabel deliveryPostCode = new JLabel(postCode);
            detailsPane.add(deliveryPostCode, "wrap");
            JLabel phoneTitle = new JLabel("Phone:");
            detailsPane.add(phoneTitle, "split 2");
            JLabel Phone = new JLabel(phoneNumber);
            detailsPane.add(Phone);//completes detailsPane

            final JButton OrderFinish = new JButton("Buy Now");
            totalPane.add(OrderFinish, "cell 1 0 ,wrap");// Mig layout also lets you state specific cell coordinates to put things into.
            JLabel ItemTitle = new JLabel("Items:");
            totalPane.add(ItemTitle, "cell 1 1");
            JLabel Item££sign = new JLabel("£");
            totalPane.add(Item££sign, "split 2");
            final JLabel ItemTotal = new JLabel("null");
            totalPane.add(ItemTotal, "cell 2 1");
            JLabel PackagingTitle = new JLabel("Postage & Packing:");
            totalPane.add(PackagingTitle, "Cell 1 2");
            JLabel Packing£sign = new JLabel("£");
            totalPane.add(Packing£sign, "split 2");
            final JLabel PackagingTotal = new JLabel("00.00");
            totalPane.add(PackagingTotal, "cell 2 2, wrap 15");
            JSeparator TotalSeparator = new JSeparator(SwingConstants.HORIZONTAL);//Separator for different values.
            TotalSeparator.setBackground(Color.CYAN);
            totalPane.add(TotalSeparator, "cell 0 4,pushx, growx, span,wrap");//push x and grow x and span lets the separator take up more than one cell.
            JLabel OrderTotalTitle = new JLabel("Order Total:");
            totalPane.add(OrderTotalTitle, "cell 1 5");
            JLabel Order£sign = new JLabel("£");
            totalPane.add(Order£sign, "split 2");
            final JLabel OrderTotal = new JLabel("null");
            totalPane.add(OrderTotal, "wrap");
            final JRadioButton StandardD = new JRadioButton("Standard Delivery");
            totalPane.add(StandardD, "cell 1 6, wrap");
            final JRadioButton ExpressD = new JRadioButton("Express Delivery");
            totalPane.add(ExpressD, "cell 1 7");
            ButtonGroup Delivery = new ButtonGroup();
            Delivery.add(StandardD);
            Delivery.add(ExpressD);


            final JPanel[] Ipanel = new JPanel[100];//This creates an array of Java swing components
            JLabel[] iNumLabel = new JLabel[100];
            JLabel[] iNumlabelTitle = new JLabel[100];
            JLabel[] iImg = new JLabel[100];
            final JLabel[] iname = new JLabel[100];
            JLabel[] i£Sign = new JLabel[100];
            final JLabel[] IPrice = new JLabel[100];
            JLabel[] IQuantity = new JLabel[100];
            final JLabel[] IQuantityNum = new JLabel[100];


            int i = 0;
            for (Item.BasketItem bItem : basket.values()) {   //for the length of the HashMap passed in by basket this code will run and generate and display play the equivalent number of panels.
                Ipanel[i] = new JPanel();
                iNumLabel[i] = new JLabel();
                iname[i] = new JLabel(bItem.getItem().getName());//Gets and displays name of item from hashmap
                iNumlabelTitle[i] = new JLabel("Item Number:");
                iImg[i] = new JLabel();//TODO place to put images.
                i£Sign[i] = new JLabel("£");
                IPrice[i] = new JLabel(String.valueOf(bItem.getItem().getPrice()));//gets and displays price of item
                IQuantity[i] = new JLabel("Quantity:");
                IQuantityNum[i] = new JLabel("null");
                IQuantityNum[i].setText(String.valueOf(bItem.getQuantity()));//gets quantity of item

                Ipanel[i].setBorder(BorderFactory.createLineBorder(Color.BLACK));//gives each panel a border.
                Ipanel[i].setLayout(new MigLayout());
                iNumLabel[i].setText(String.valueOf(i));
                Ipanel[i].add(iNumlabelTitle[i]); // doesn't need to be in an array
                Ipanel[i].add(iNumLabel[i], "split 2");
                Ipanel[i].add(iNumLabel[i], "wrap");

                Ipanel[i].add(iname[i], "wrap");
                Ipanel[i].add(i£Sign[i], "split 2");// doesn't need to be an array
                Ipanel[i].add(IPrice[i], "wrap");

                Ipanel[i].add(IQuantity[i], "split 2");//doesn't need to be in array
                Ipanel[i].add(IQuantityNum[i]);

                itemCalc = Double.parseDouble(IPrice[i].getText());//Each time the code is run it gets the price and calculates it
                itemCalc = itemCalc * bItem.getQuantity();// it then * the price by the quantity.

                itemTotal += itemCalc; // and adds that into the itemTotal value not replacing it but adding it. via +=

                itemPane.add(Ipanel[i]);
                i++;//so the next panel in the array can be made, we increase I.
            }
            ItemTotal.setText(new DecimalFormat("######.##").format(itemTotal));//sets item total in decimal format.
            ChangeListener Change = new ChangeListener() {
                @Override
                public void stateChanged(ChangeEvent e) {
                    if (StandardD.isSelected()) {//Checks if Standard delivery is checked and calculates the new order Total
                        postingTotal = 2.27 * basket.size();//The more items the more it costs.
                        PackagingTotal.setText(new DecimalFormat("##.##").format(postingTotal));
                        orderTotal = itemTotal + postingTotal;
                        OrderTotal.setText(new DecimalFormat("##.##").format(orderTotal));
                    } else if (ExpressD.isSelected()) {//Checks if express delivery is checked and calculates the new order Total
                        postingTotal = 3.73 * basket.size();
                        PackagingTotal.setText(new DecimalFormat("##.##").format(postingTotal));
                        orderTotal = itemTotal + postingTotal;
                        OrderTotal.setText(new DecimalFormat("##.##").format(orderTotal));
                    }

                }

            };
            StandardD.addChangeListener(Change);
            ExpressD.addChangeListener(Change);
            StandardD.doClick(); // Makes StandardD already selected so the calculation can happen immediatelyy.

            ActionListener BuyNow = new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        for (int i = 0; i < basket.size(); i++) {
                            //TODO Kyles Insert Api.
                            HashMap<String, Object> query = new HashMap<String, Object>();
                            query.put(jUSERID_COLUMN_NAME,User.getCurrentUser().getID());
                            query.put(jItemID_COLUMN_NAME, iname[i].getDisplayedMnemonic());
                            query.put(jPRICE_COLUMN_NAME,IPrice[i].getText());
                            query.put(jSTOCK_COLUMN_NAME,IQuantityNum[i].getDisplayedMnemonic());
                            query.put(jDATE_COLUMN_NAME,new Date(currentTimeMillis()));

                            //TODO Make database table, Create a handler?, I have tried but failed.
                            // DatabaseManager.CreateCompletionHandler callback = new DatabaseManager.CreateCompletionHandler();
                            //DatabaseManager.createRecordInBackground(query,jTABLE_NAME,callback);


//                            String query = "Insert into Completed (UserID,ItemID,ItemPrice,Quantity,Date) values(?,?,?,?,?)";
//                            PreparedStatement stm = DatabaseManager.getSharedDbConnection().prepareStatement(query);
//                            stm.setInt(1, User.getCurrentUser().getID());
//                            stm.setInt(2, iname[i].getDisplayedMnemonic());
//                            String itemCost = IPrice[i].getText();
//                            stm.setString(3, itemCost);
//                            stm.setInt(4,IQuantityNum[i].getDisplayedMnemonic());
//                            stm.setDate(5, new Date(currentTimeMillis()));
//                            stm.execute();
                        }

                    } catch (Exception ex) {
                        out.println(ex);
                    }

                }
            };

            OrderFinish.addActionListener(BuyNow);
        } else {
            JOptionPane.showMessageDialog(getView(), "Please login before trying to checkout.");
            BasketViewController basketVC = new BasketViewController();
            ApplicationManager.setDisplayedViewController(basketVC);
            //TODO set a display.
            //TODO I've added a sout  ApplicationManager.setDisplayedViewController, for some reason after i've set the view and passed basketVC, OrderCompletionViewController is passed.
            //TODO So i am not sure how to handle it,we could try to do a button click from here or just let the user click the button themselves.
            //TODO the only problem being with the later is that the current view disappears.

        }


    }

    @Override
    public Component getView() {
        return getContentPane();
    }


    public void enterDetails() {
        String[] Submit = {"OK"};
        JTextField pMethod = new JTextField(5);
        JTextField rName = new JTextField(5);
        JTextField rNumber = new JTextField(5);
        JTextField twn = new JTextField(5);
        JTextField pCode = new JTextField(5);
        JTextField pNumber = new JTextField(5);


        JPanel detailPanel = new JPanel();
        detailPanel.add(new JLabel("PaymentMethod:"));
        detailPanel.add(pMethod);

        detailPanel.add(new JLabel("RoadName:"));
        detailPanel.add(rName);

        detailPanel.add(new JLabel("RoadNumber:"));
        detailPanel.add(rNumber);

        detailPanel.add(new JLabel("Town:"));
        detailPanel.add(twn);

        detailPanel.add(new JLabel("PostCode:"));
        detailPanel.add(pCode);

        detailPanel.add(new JLabel("PhoneNumber:"));
        detailPanel.add(pNumber);

        int Sub = JOptionPane.showOptionDialog(getView(), detailPanel, "Please Enter Details", JOptionPane.NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, Submit, Submit[0]); // we make it in an int so we can check the outcome
        if (Sub == 0) {
            if (pMethod.getText().trim().length() == 0 || rName.getText().trim().length() == 0 || rNumber.getText().trim().length() == 0 || twn.getText().trim().length() == 0 || pCode.getText().trim().length() == 0 || pNumber.getText().trim().length() == 0) { //checks fields aren't null or 0
                JOptionPane.showMessageDialog(null,"Please Fill All Fields");
                enterDetails();//if user fails to fill in details restarts panel.
            } else {//user successfully fills in details
                paymentMethod = pMethod.getText();
                roadName = rName.getText();
                roadNumber = rNumber.getText();
                town = twn.getText();
                postCode = pCode.getText();
                phoneNumber = pNumber.getText();

            }


        }
    }


}




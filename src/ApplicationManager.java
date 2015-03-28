import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

/**
 * Created by kylejm on 28/11/14.
 */
public class ApplicationManager {

    private static final JPanel mainView = new JPanel(new BorderLayout());
    private static ArrayList<ViewController> navViewControllers = new ArrayList<ViewController>();
    private static ViewController displayedViewController;
    private static JPanel navView = new JPanel();

    public static void applicationDidLaunch() {
        JFrame window = new JFrame("ORDR");

        //Init mainView to contain all view controllers
        window.setSize(700, 600);
        window.add(mainView);

        //Init navBar
        addViewControllerToNav(new LogInViewController());
        addViewControllerToNav(new CreateAccountViewController());
        addViewControllerToNav(new ItemTableViewController());
        addViewControllerToNav(new BasketViewController());
        addViewControllerToNav(new ContactForm());
        mainView.add(navView, BorderLayout.NORTH);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setVisible(true);
    }

    public static void setDisplayedViewController(ViewController controller) {
        if (displayedViewController != controller) {
            if (displayedViewController != null) mainView.remove(displayedViewController.getView());
            displayedViewController = controller;
            mainView.add(controller.getView(),  BorderLayout.CENTER);
            controller.viewWillAppear();
            mainView.revalidate();
            mainView.repaint();
        }
    }

    public static void addViewControllerToNav(final ViewController controller) {
        if (navViewControllers.contains(controller)) return;

        navViewControllers.add(controller);
        final JButton vcButton = new JButton(controller.getButtonLabel());
        vcButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                setDisplayedViewController(controller);
            }
        });
        navView.add(vcButton, BorderLayout.NORTH);
    }

    public static void displayViewControllerAtIndex(int index) {
        setDisplayedViewController(navViewControllers.get(index));
    }

}
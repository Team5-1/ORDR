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
    private static ArrayList<ViewControllerButton> navViewControllers = new ArrayList<ViewControllerButton>();
    private static ViewController displayedViewController;
    private static JPanel navView = new JPanel();
    private static boolean userLoggedIn = false;

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
        displayViewControllerAtIndex(2);
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
        if (navContainsViewController(controller)) return;

        ViewControllerButton vcButton = new ViewControllerButton(controller);
        navViewControllers.add(vcButton);
        navView.add(vcButton.button, BorderLayout.NORTH);
    }

    public static void addViewControllerToNav(final ViewController controller, int index) {
        if (navContainsViewController(controller)) return;

        ViewControllerButton vcButton = new ViewControllerButton(controller);
        navViewControllers.add(index, vcButton);
        navView.add(vcButton.button, BorderLayout.NORTH, index);
    }

    public static void removeViewControllerFromNavAtIndex(int index) {
        ViewControllerButton vcButton = navViewControllers.get(index);
        navView.remove(vcButton.button);
        navViewControllers.remove(vcButton);
    }

    public static void setUserLoggedIn(boolean isLoggedIn) {
        if (isLoggedIn != userLoggedIn) {
            userLoggedIn = isLoggedIn;
            if (userLoggedIn) {
                removeViewControllerFromNavAtIndex(0); //Remove login
                removeViewControllerFromNavAtIndex(0); //Remove create
                JButton logOutButton = new JButton("Log Out");
                logOutButton.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        User.logOutCurrentUser();
                        setUserLoggedIn(false);
                        displayedViewController.viewWillAppear();
                    }
                });
                navView.add(logOutButton, BorderLayout.NORTH, 0);
            } else {
                navView.remove(0);
                addViewControllerToNav(new LogInViewController(), 0);
                addViewControllerToNav(new CreateAccountViewController(), 1);
            }
            navView.revalidate();
            navView.repaint();
        }
    }

    public static void displayViewControllerAtIndex(int index) {
        setDisplayedViewController(navViewControllers.get(index).viewController);
    }

    static private class ViewControllerButton {
        final ViewController viewController;
        final JButton button;

        public ViewControllerButton(final ViewController viewController) {
            this.viewController = viewController;
            this.button = new JButton(viewController.getButtonLabel());
            this.button.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseReleased(MouseEvent e) {
                    setDisplayedViewController(viewController);
                }
            });
        }
    }



    //Helpers
    private static boolean navContainsViewController(ViewController controller) {
        boolean containsVC = false;
        for (ViewControllerButton vcButton : navViewControllers) {
            if (vcButton.viewController == controller) {
                containsVC = true;
                break;
            }
        }
        return containsVC;
    }

}
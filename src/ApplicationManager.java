import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Created by kylejm on 28/11/14.
 */
public class ApplicationManager {

    private static final LogInViewController logInVC = new LogInViewController();
    private static final CreateAccountViewController createAccVC = new CreateAccountViewController();
    private static final ItemListView listVC = new ItemListView();
    private static final BasketViewController basketVC = new BasketViewController();
    private static ViewController currentVC = logInVC;
    private static final JPanel mainView = new JPanel(new BorderLayout());

    public static void applicationDidLaunch() {
        JFrame window = new JFrame("ORDR");

        //Create view to contain all view controllers
        window.setSize(800, 600);
        window.add(mainView);
        mainView.add(logInVC.getView(), BorderLayout.CENTER);

        //Create nav bar
        JPanel nav = new JPanel();
        final JButton logInButton = new JButton("Log in");
        logInButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                if (currentVC != logInVC) {
                    mainView.remove(currentVC.getView());
                    currentVC = logInVC;
                    mainView.add(logInVC.getView(), BorderLayout.CENTER);
                    mainView.revalidate();
                    mainView.repaint();
                }
            }
        });
        final JButton createButton = new JButton("Create account");
        createButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                if (currentVC != createAccVC) {
                    mainView.remove(currentVC.getView());
                    currentVC = createAccVC;
                    mainView.add(createAccVC.getView(),  BorderLayout.CENTER);
                    mainView.revalidate();
                    mainView.repaint();
                }
            }
        });
        final JButton listButton = new JButton("Products");
        listButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                if (currentVC != listVC) {
                    mainView.remove(currentVC.getView());
                    currentVC = listVC;
                    mainView.add(listVC.getView(),  BorderLayout.CENTER);
                    mainView.revalidate();
                    mainView.repaint();
                }
            }
        });
        final JButton basketButton = new JButton("Basket");
        basketButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                if (currentVC != basketVC) {
                    mainView.remove(currentVC.getView());
                    currentVC = basketVC;
                    mainView.add(basketVC.getView(),  BorderLayout.CENTER);
                    mainView.revalidate();
                    mainView.repaint();
                }
            }
        });
        nav.add(logInButton, BorderLayout.NORTH);
        nav.add(createButton, BorderLayout.NORTH);
        nav.add(listButton, BorderLayout.NORTH);
        nav.add(basketButton, BorderLayout.NORTH);
        //Add nav bar
        mainView.add(nav, BorderLayout.NORTH);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setVisible(true);
    }

    public static void setDisplayedViewController(ViewController controller) {
        if (currentVC != controller) {
            mainView.remove(currentVC.getView());
            currentVC = controller;
            mainView.add(controller.getView(),  BorderLayout.CENTER);
            mainView.revalidate();
            mainView.repaint();
        }
    }

}
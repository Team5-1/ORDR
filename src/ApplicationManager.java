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

    public static void applicationDidLaunch() {
        JFrame window = new JFrame("ORDR");

        //Create view to contain all view controllers
        final JPanel mainView = new JPanel(new BorderLayout());
        window.setSize(800, 600);
        window.add(mainView);
        mainView.add(logInVC.getView(), BorderLayout.CENTER);

        //Create nav bar
        JPanel nav = new JPanel();
        final JButton logInButton = new JButton("Log in");
        logInButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                if (!logInVC.getView().isDisplayable()) {
                    mainView.remove(createAccVC.getView());
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
                if (!createAccVC.getView().isDisplayable()) {
                    mainView.remove(logInVC.getView());
                    mainView.add(createAccVC.getView(),  BorderLayout.CENTER);
                    mainView.revalidate();
                    mainView.repaint();
                }
            }
        });
        nav.add(logInButton, BorderLayout.NORTH);
        nav.add(createButton, BorderLayout.NORTH);

        //Add nav bar
        mainView.add(nav, BorderLayout.NORTH);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setVisible(true);
    }

}
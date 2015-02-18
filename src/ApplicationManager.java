import javax.swing.*;
import java.awt.*;

/**
 * Created by kylejm on 28/11/14.
 */
public class ApplicationManager {

    public static void applicationDidLaunch() {
        JFrame window = new JFrame("ORDR");
        JPanel mainView = new JPanel(new BorderLayout());
        window.setSize(300, 200);
        window.add(mainView);
        mainView.add(new LogInViewController().getView(), BorderLayout.CENTER);
        JPanel nav = new JPanel();
        nav.add(new JButton("Log in"), BorderLayout.NORTH);
        nav.add(new JButton("Create account"), BorderLayout.NORTH);
        mainView.add(nav, BorderLayout.NORTH);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setVisible(true);
    }

}
import javax.swing.*;

/**
 * Created by kylejm on 28/11/14.
 */
public class ApplicationManager {

    public static void applicationDidLaunch() {
        JFrame window = new JFrame("ORDR");
        window.setSize(300, 200);
        LogInViewController vc = new LogInViewController();
        window.add(vc.getView());
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setVisible(true);
    }

}
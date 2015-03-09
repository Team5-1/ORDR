import javax.swing.*;

/**
 * Created by kylejm on 28/11/14.
 */
public class ApplicationManager {

    public static void applicationDidLaunch() {
        new BasketViewController().setVisible(true);
    }
}

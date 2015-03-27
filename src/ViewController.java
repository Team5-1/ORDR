import javax.swing.*;
import java.awt.*;

/**
 * Created by kylejm on 10/03/15.
 */
public abstract class ViewController extends JFrame {
    abstract public Component getView();
    public void viewWillAppear() {}
    public String getButtonLabel() { return ""; }
}

import javax.swing.*;
import java.awt.*;

public class ClientFrame extends JFrame {

    public static final Dimension SCREEN_SIZE = Toolkit.getDefaultToolkit().getScreenSize();

    public ClientFrame() {
        super("CS:NO");
        add(new ClientPanel());
        setUndecorated(true);
        setSize(SCREEN_SIZE);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }
}

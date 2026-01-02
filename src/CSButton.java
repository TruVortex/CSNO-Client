import javax.swing.*;
import java.awt.*;

public class CSButton extends JButton {
    public CSButton(String text) {
        super(text);
        setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.WHITE), BorderFactory.createEmptyBorder(5, 10, 3, 10)));
        setFont(new Font("Bahnschrift", Font.PLAIN, 16));
        setForeground(Color.WHITE);
        setContentAreaFilled(false);
        setFocusable(false);
        setAlignmentX(Component.CENTER_ALIGNMENT);
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }
}

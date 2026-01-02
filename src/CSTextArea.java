import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

public class CSTextArea extends JTextField {
    public CSTextArea(String prompt, int width) {
        super(prompt);
        setBackground(new Color(0, 0, 0, 0));
        setForeground(new Color(255, 255, 255, 150));
        setCaretColor(Color.WHITE);
        setFont(new Font("Bahnschrift", Font.PLAIN, 16));
        setBorder(BorderFactory.createLineBorder(Color.WHITE));
        setHorizontalAlignment(JTextField.CENTER);
        setMinimumSize(new Dimension(width, 33));
        setPreferredSize(new Dimension(width, 33));
        setMaximumSize(new Dimension(width, 33));
        addFocusListener(new FocusListener() {
            @Override
            public void focusLost(FocusEvent e) {
                if (getText().isEmpty()) {
                    setText(prompt);
                    setForeground(new Color(255, 255, 255, 150));
                }
            }

            @Override
            public void focusGained(FocusEvent e) {
                if (getText().equals(prompt)) {
                    setText("");
                    setForeground(Color.WHITE);
                }
            }
        });
    }
}
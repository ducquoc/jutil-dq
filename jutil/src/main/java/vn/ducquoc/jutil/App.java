package vn.ducquoc.jutil;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/**
 * Main class for vn.ducquoc.jutil package.
 */
public class App {

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        } catch (Exception ex) {
            System.out.println("[WARN] Minor exception: " + ex.getMessage());
        }

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                JFrame frame = new AppFrame();
                frame.setVisible(true);
            }
        });
    }

}

class AppFrame extends JFrame {

    private static final long serialVersionUID = 1L;

    JLabel introLabel;
    JPanel introPanel;

    JTextField validationTextField;
    JPanel validationPanel;
    JButton validationButton;
    JLabel validationLabel;

    public AppFrame() {
        setTitle("NPI Validation tool");
        setSize(300, 110);
        setMinimumSize(new Dimension(290, 100));
        // setLocation(0, 0);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        introPanel = new JPanel();
        introLabel = new JLabel("Enter your 10-digit NPI number for validation");
        introPanel.add(introLabel);
        add(introPanel, BorderLayout.NORTH);

        validationPanel = new JPanel();
        validationTextField = new JTextField("1234567893", 10);
        validationPanel.add(validationTextField);
        add(validationPanel, BorderLayout.CENTER);

        validationButton = new JButton("Validate");
        validationPanel.add(validationButton);

        validationLabel = new JLabel("             ");
        validationLabel.setFont(new Font("Arial", Font.BOLD, 11));
        validationPanel.add(validationLabel);

        validationButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {

                validationLabel.setText(""); // clean old message

                String npiText = validationTextField.getText();
                if (HealthcareUtil.isValidNpi(npiText)) {
                    validationLabel.setText("VALID  ");
                } else {
                    validationLabel.setText("INVALID");
                }
            }
        });
    }

}

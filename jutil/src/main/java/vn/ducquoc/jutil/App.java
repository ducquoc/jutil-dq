package vn.ducquoc.jutil;

import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;
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

    public AppFrame() {
        // setLocation(0, 0);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JTabbedPane tabPanel = new JTabbedPane();
        add(tabPanel);

        vn.ducquoc.jutil.gui.NpiValidationPanel npiPanel = new vn.ducquoc.jutil.gui.NpiValidationPanel();
        tabPanel.add("NPI", npiPanel);

        vn.ducquoc.jutil.gui.DeaValidationPanel deaPanel = new vn.ducquoc.jutil.gui.DeaValidationPanel();
        tabPanel.add("DEA", deaPanel);


        setTitle("Healthcare ID Validation");
        setSize(300, 150);
        setMinimumSize(new Dimension(290, 140));
    }

}

package Code;
import javax.swing.*;
import GUI.LoginGUI;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginGUI().setVisible(true));
    }
}

import javax.swing.*;
import view.MainFrame;

public class MainApp {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new MainFrame().setVisible(true);
        });
    }
}

package t2;

import javax.swing.*;

public class App {
    public static void main(String[] args) {
        new App();
    }

    public App() {
        System.out.println("Hello world");
        SwingUtilities.invokeLater(() -> new LayeredPane());
    }
}

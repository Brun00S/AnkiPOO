package application;

import view.AnkiGUI;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new AnkiGUI(); // apenas chama a GUI
        });
    }
}

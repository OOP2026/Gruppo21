package gui;

import controller.Controller;
import javax.swing.*;

public class MainAvvio {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Controller controller = new Controller();
            InserimentoEntitaFrame frame = new InserimentoEntitaFrame(controller);
            frame.setVisible(true);
        });
    }
}
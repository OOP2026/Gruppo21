package gui;

import controller.Controller;
import javax.swing.*;

public class GUI_Medico {
    public JPanel panel;
    protected JFrame frame;

    private JButton aggiungiRicoveroButton;
    private JButton gestisciRicoveroButton;
    private JButton logoutButton;

    private Controller controller;

    public GUI_Medico(Controller controller) {
        this.controller = controller;

        logoutButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(null, "Logout effettuato!");

        });
    }
}
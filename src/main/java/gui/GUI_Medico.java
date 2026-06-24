package gui;

import controller.Controller;
import javax.swing.*;

public class GUI_Medico {
    // Deve chiamarsi "panel" per corrispondere esattamente al tuo Component Tree
    public JPanel panel;

    // Inseriamo i bottoni della tua grafica
    private JButton aggiungiRicoveroButton;
    private JButton gestisciRicoveroButton; // Questo lo vedo già nominato così nella tua foto!
    private JButton logoutButton;

    private Controller controller;

    public GUI_Medico(Controller controller) {
        this.controller = controller;

        // Esempio: colleghiamo subito il logout
        logoutButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(null, "Logout effettuato!");
            // Più avanti potremo far chiudere la finestra e riaprire il Login
        });

        // Qui potremo aggiungere le azioni per gli altri due bottoni
    }
}
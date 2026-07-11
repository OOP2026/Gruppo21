package gui;

import controller.Controller;
import exceptions.BadArgsException;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Main {
    private static JFrame frame;
    private JButton loginButton;
    private JButton registratiButton;
    private JPanel panel;
    private static Controller controller;

    public Main() {
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Login login = new Login(frame, controller);
                login.frame.setVisible(true);
                frame.setVisible(false);
            }
        });
        registratiButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Registrazione registrazione = new Registrazione(frame, controller);
                registrazione.frame.setVisible(true);
                frame.setVisible(false);
            }
        });
    }

    public static void main(String[] args) {
        frame = new JFrame("Benvenuto");

        frame.setContentPane(new Main().panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        try {
            controller = new Controller();
        } catch (BadArgsException e) {
            System.err.println("Errore di Controller - Inizializzazione fallita.");
            System.exit(1);
        }
    }
}

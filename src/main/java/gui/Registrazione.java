package gui;

import controller.Controller;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.Objects;

public class Registrazione {
    private JPanel panel;
    private JTextField textField1;
    private JComboBox comboBox1;
    private JPasswordField passwordField1;
    private boolean selezione = false; //false per medico, true per ammnistratore
    private static Controller controller;

    public Registrazione() {
        passwordField1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String email = textField1.getText();
                String password = Arrays.toString(passwordField1.getPassword());
                //Viene salvata come "[PASSWORD]", ove PASSWORD è cio' che inserisce l'utente.

                if (selezione) controller.aggiungiAmministratoreAnonimo(email, password);
                else //aggiungiMedico con altri reparti vari

                System.out.println("Aggiunto amministratore. Email: " + email + ". Password: " + password + "\n");
            }
        });
        textField1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            }
        });
        comboBox1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String value = Objects.requireNonNull(comboBox1.getSelectedItem()).toString();
                selezione = value.equals("Amministratore");
                System.out.println("Selezionato: " + value);
            }
        });
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Registrazione");
        frame.setContentPane(new Registrazione().panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        controller = new Controller();
    }
}

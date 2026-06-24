package gui;

import controller.Controller;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;

public class Login {
    private JPanel panel;
    private JTextField textField1;
    private JPasswordField passwordField1;
    private JComboBox comboBox1;
    private static Controller controller;
    private boolean selezione = false; //false per medico, true per ammnistratore


    public Login() {
        passwordField1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

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
}

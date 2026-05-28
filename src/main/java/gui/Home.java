package gui;

import controller.Controller;

import javax.swing.*;
import java.awt.*;

public class Home {
    private JPanel panel1;
    private JButton button1;
    private JButton button2;
    private static JFrame frame;
    private static Controller controller;

    public static void main(String[] args) {
        String[] ciao = {"Ciao"};
        frame = new JFrame("Home");
        frame.setContentPane(new Home().panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        controller = new Controller();

        //Instanzia controller
        if(true) {
            IstanziamentoController.main(controller);
        }

        frame.setVisible(true);
    }

}

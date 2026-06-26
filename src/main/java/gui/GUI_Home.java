package gui;

import controller.Controller;
import javax.swing.*;

public class GUI_Home {
    public JPanel panelMain;
    protected JFrame frame;
    private JButton btnPazienti;
    private JButton btnMedici;
    private JButton btnStanze;
    private JButton btnLetti;
    private JButton btnRicoveri;
    private JButton btnTurni;
    private Controller controller;

    public GUI_Home(Controller controller) {
        this.controller = controller;
        frame = new JFrame("Amministratore: ");
        frame.setContentPane(panelMain);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

        btnPazienti.addActionListener(e -> apriFinestra("Gestione Pazienti", new GUI_Paziente(controller).panelMain));
        btnMedici.addActionListener(e -> apriFinestra("Gestione Medici", new GUI_Medico(controller).panel));
        btnStanze.addActionListener(e -> apriFinestra("Gestione Stanze", new GUI_Stanza(controller).panelMain));
        btnLetti.addActionListener(e -> apriFinestra("Gestione Letti", new GUI_Letto(controller).panelMain));
        btnRicoveri.addActionListener(e -> apriFinestra("Gestione Ricoveri", new GUI_Ricovero(controller).panelMain));
        btnTurni.addActionListener(e -> apriFinestra("Gestione Turni", new GUI_TurnoLavorativo(controller).panelMain));
    }

    private void apriFinestra(String titolo, JPanel pannello) {
        JFrame frame = new JFrame(titolo);
        frame.setContentPane(pannello);
        frame.setSize(500, 400);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void createUIComponents() {
        // TODO:
    }
}
package gui;

import controller.Controller;
import model.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MainGUI extends JFrame {

    private static final Logger LOGGER = Logger.getLogger(MainGUI.class.getName());
    private static final String FONT_NAME = "Arial";
    private static final String RUOLO_MEDICO = "Medico";
    private static final String RUOLO_AMMINISTRATORE = "Amministratore";
    private static final String BTN_AGGIUNGI = "Aggiungi";
    private static final String BTN_MODIFICA = "Modifica";
    private static final String BTN_ELIMINA = "Elimina";
    private static final String BTN_SALVA = "Salva";
    private static final String MSG_CONFERMA_ELIMINAZIONE = "Sei sicuro di voler eliminare questo elemento?";
    private static final String MSG_SELEZIONA_RIGA = "Seleziona una riga!";
    private static final String TITOLO_ERRORE = "Errore";

    private transient Controller controller;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private JTabbedPane tabsDashboard;

    public MainGUI() {
        controller = new Controller();
        setTitle("Sistema Gestione Ospedale - Enterprise Edition");
        setSize(1200, 800);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        mostraSchermataLogin();
    }

    private void mostraSchermataLogin() {
        getContentPane().removeAll();
        JPanel panelLogin = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblTitolo = new JLabel("Accesso al Sistema", SwingConstants.CENTER);
        lblTitolo.setFont(new Font(FONT_NAME, Font.BOLD, 24));

        JComboBox<String> comboRuolo = new JComboBox<>(new String[]{RUOLO_AMMINISTRATORE, RUOLO_MEDICO});
        JTextField txtEmail = new JTextField(20);
        JPasswordField txtPassword = new JPasswordField(20);

        JButton btnLogin = new JButton("Accedi");
        btnLogin.setFont(new Font(FONT_NAME, Font.BOLD, 14));

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panelLogin.add(lblTitolo, gbc);

        gbc.gridwidth = 1;
        gbc.gridy = 1;
        panelLogin.add(new JLabel("Ruolo:"), gbc);
        gbc.gridx = 1;
        panelLogin.add(comboRuolo, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        panelLogin.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1;
        panelLogin.add(txtEmail, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        panelLogin.add(new JLabel("Password:"), gbc);
        gbc.gridx = 1;
        panelLogin.add(txtPassword, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        panelLogin.add(btnLogin, gbc);

        btnLogin.addActionListener(e -> gestisciLogin(comboRuolo, txtEmail, txtPassword));

        add(panelLogin);
        revalidate();
        repaint();
    }

    private void gestisciLogin(JComboBox<String> comboRuolo, JTextField txtEmail, JPasswordField txtPassword) {
        try {
            String password = new String(txtPassword.getPassword());
            boolean ok;
            if (RUOLO_AMMINISTRATORE.equals(comboRuolo.getSelectedItem())) {
                ok = controller.loginAmministratore(txtEmail.getText(), password);
            } else {
                ok = controller.loginMedico(txtEmail.getText(), password);
            }

            if (ok) {
                mostraDashboard();
            } else {
                JOptionPane.showMessageDialog(this, "Credenziali errate!", TITOLO_ERRORE, JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Errore DB durante il login", ex);
            JOptionPane.showMessageDialog(this, "Errore DB: " + ex.getMessage(), TITOLO_ERRORE, JOptionPane.ERROR_MESSAGE);
        }
    }

    private void mostraDashboard() {
        getContentPane().removeAll();
        tabsDashboard = new JTabbedPane();
        tabsDashboard.setFont(new Font(FONT_NAME, Font.BOLD, 12));

        if (controller.isAmministratore()) {
            inizializzaTabsAmministratore();
        } else {
            inizializzaTabsMedico();
        }

        JPanel pnlBottom = new JPanel(new BorderLayout());
        pnlBottom.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        String info = " Loggato come: " + controller.getUtenteLoggatoRuolo();
        Medico medicoLoggato = getMedicoLoggato();
        if (!controller.isAmministratore() && medicoLoggato != null) {
            info += " (" + medicoLoggato.getNome() + " " + medicoLoggato.getCognome() + ")";
        }

        JButton btnLogout = new JButton("Logout");
        btnLogout.addActionListener(e -> mostraSchermataLogin());

        pnlBottom.add(new JLabel(info), BorderLayout.WEST);
        pnlBottom.add(btnLogout, BorderLayout.EAST);

        add(tabsDashboard, BorderLayout.CENTER);
        add(pnlBottom, BorderLayout.SOUTH);
        revalidate();
        repaint();
    }

    private void inizializzaTabsAmministratore() {
        tabsDashboard.addTab("Pazienti", creaPannelloPazienti());
        tabsDashboard.addTab("Medici", creaPannelloMedici());
        tabsDashboard.addTab("Reparti", creaPannelloReparti());
        tabsDashboard.addTab("Stanze", creaPannelloStanze());
        tabsDashboard.addTab("Letti", creaPannelloLetti());
        tabsDashboard.addTab("Turni", creaPannelloTurni());
        tabsDashboard.addTab("Ricoveri", creaPannelloRicoveri());
        tabsDashboard.addTab("Interventi", creaPannelloInterventi());
        tabsDashboard.addTab("Visite", creaPannelloVisite());
        tabsDashboard.addTab("Amministratori", creaPannelloAmministratori());
        tabsDashboard.addTab("Collega Med-Ricovero", creaPannelloGestisce());
        tabsDashboard.addTab("Collega Med-Intervento", creaPannelloOpera());
    }

    private void inizializzaTabsMedico() {
        tabsDashboard.addTab("Il Mio Profilo", creaPannelloProfiloMedico());
        tabsDashboard.addTab("I Miei Pazienti", creaPannelloPazientiMedico());
        tabsDashboard.addTab("I Miei Turni", creaPannelloTurniMedico());
        tabsDashboard.addTab("I Miei Interventi", creaPannelloInterventiMedico());
        tabsDashboard.addTab("Le Mie Visite", creaPannelloVisiteMedico());
    }

    private void rinfrescaDashboard() {
        if (tabsDashboard != null) {
            int indiceAttuale = tabsDashboard.getSelectedIndex();
            mostraDashboard();
            if (indiceAttuale >= 0 && indiceAttuale < tabsDashboard.getTabCount()) {
                tabsDashboard.setSelectedIndex(indiceAttuale);
            }
        }
    }

    private Medico getMedicoLoggato() {
        for (Medico m : controller.getMedici()) {
            if (m.getIdMedico() == controller.getIdUtenteLoggato()) {
                return m;
            }
        }
        return null;
    }

    private JPanel creaPannelloProfiloMedico() {
        JPanel p = new JPanel(new GridBagLayout());
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(10, 10, 10, 10);
        g.gridy = 0;

        Medico m = getMedicoLoggato();
        if (m == null) {
            return p;
        }

        p.add(new JLabel("<html><h2>Scheda Personale</h2></html>"), g);
        g.gridy++;
        p.add(new JLabel("Nome: " + m.getNome() + " " + m.getCognome()), g);
        g.gridy++;
        p.add(new JLabel("Email: " + m.getEmail() + " | Qualifica: " + m.getTipoMedico()), g);
        g.gridy++;
        String repartoNome = m.getReparto() != null ? m.getReparto().getNome() : "Nessuno";
        p.add(new JLabel("Reparto: " + repartoNome), g);

        return p;
    }

    private JPanel creaPannelloPazientiMedico() {
        JPanel p = new JPanel(new BorderLayout());
        DefaultTableModel tm = new DefaultTableModel(new String[]{"CF", "Nome", "Cognome", "Letto Ricovero"}, 0);
        Medico m = getMedicoLoggato();

        if (m != null && m.getRicoveri() != null) {
            for (Ricovero r : m.getRicoveri()) {
                if (r.getPaziente() != null) {
                    String letto = r.getLetto() != null ? String.valueOf(r.getLetto().getIdLetto()) : "N/D";
                    tm.addRow(new Object[]{r.getPaziente().getCodFiscale(), r.getPaziente().getNome(), r.getPaziente().getCognome(), letto});
                }
            }
        }
        p.add(new JScrollPane(new JTable(tm)), BorderLayout.CENTER);
        return p;
    }

    private JPanel creaPannelloTurniMedico() {
        JPanel p = new JPanel(new BorderLayout());
        DefaultTableModel tm = new DefaultTableModel(new String[]{"Inizio", "Fine"}, 0);
        Medico m = getMedicoLoggato();

        if (m != null && m.getTurniLavorativi() != null) {
            for (TurnoLavorativo t : m.getTurniLavorativi()) {
                tm.addRow(new Object[]{t.getDataOraInizio(), t.getDataOraFine()});
            }
        }
        p.add(new JScrollPane(new JTable(tm)), BorderLayout.CENTER);
        return p;
    }

    private JPanel creaPannelloInterventiMedico() {
        JPanel p = new JPanel(new BorderLayout());
        DefaultTableModel tm = new DefaultTableModel(new String[]{"Nome Intervento"}, 0);
        Medico m = getMedicoLoggato();

        if (m != null && m.getInterventi() != null) {
            for (InterventoChirurgico i : m.getInterventi()) {
                tm.addRow(new Object[]{i.getNomeIntervento()});
            }
        }
        p.add(new JScrollPane(new JTable(tm)), BorderLayout.CENTER);
        return p;
    }

    private JPanel creaPannelloVisiteMedico() {
        JPanel p = new JPanel(new BorderLayout());
        DefaultTableModel tm = new DefaultTableModel(new String[]{"ID", "Nome Visita", "ID Ricovero"}, 0);
        JTable table = new JTable(tm);

        for (Visita v : controller.getVisite()) {
            String idRicovero = v.getRicovero() != null ? String.valueOf(v.getRicovero().getIdRicovero()) : "N/D";
            tm.addRow(new Object[]{v.getIdVisita(), v.getNomeVisita(), idRicovero});
        }

        p.add(new JScrollPane(table), BorderLayout.CENTER);
        return p;
    }

    private JPanel creaPannelloPazienti() {
        DefaultTableModel tm = new DefaultTableModel(new String[]{"Cod Fiscale", "Nome", "Cognome"}, 0);
        JTable table = new JTable(tm);
        for (Paziente p : controller.getPazienti()) {
            tm.addRow(new Object[]{p.getCodFiscale(), p.getNome(), p.getCognome()});
        }

        JPanel p = new JPanel(new BorderLayout());
        p.add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel bP = new JPanel();
        JButton bA = new JButton(BTN_AGGIUNGI);
        JButton bM = new JButton(BTN_MODIFICA);
        JButton bE = new JButton(BTN_ELIMINA);
        bP.add(bA);
        bP.add(bM);
        bP.add(bE);
        p.add(bP, BorderLayout.SOUTH);

        bA.addActionListener(e -> apriDialogPaziente(null));
        bM.addActionListener(e -> {
            int r = table.getSelectedRow();
            if (r != -1) {
                apriDialogPaziente(controller.getPazienti().get(r));
            } else {
                JOptionPane.showMessageDialog(this, MSG_SELEZIONA_RIGA);
            }
        });
        bE.addActionListener(e -> {
            int r = table.getSelectedRow();
            if (r != -1) {
                if (JOptionPane.showConfirmDialog(this, MSG_CONFERMA_ELIMINAZIONE) == JOptionPane.YES_OPTION) {
                    try {
                        controller.eliminaPaziente((String) tm.getValueAt(r, 0));
                        rinfrescaDashboard();
                    } catch (Exception ex) {
                        gestisciEccezioneGUI(ex);
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, MSG_SELEZIONA_RIGA);
            }
        });
        return p;
    }

    private void apriDialogPaziente(Paziente p) {
        boolean m = p != null;
        JDialog d = new JDialog(this, m ? BTN_MODIFICA : BTN_AGGIUNGI, true);
        d.setLayout(new GridLayout(4, 2));
        d.setSize(300, 200);
        d.setLocationRelativeTo(this);

        JTextField tN = new JTextField(m ? p.getNome() : "");
        JTextField tC = new JTextField(m ? p.getCognome() : "");
        JTextField tCF = new JTextField(m ? p.getCodFiscale() : "");
        if (m) tCF.setEditable(false);

        d.add(new JLabel("Nome:"));
        d.add(tN);
        d.add(new JLabel("Cognome:"));
        d.add(tC);
        d.add(new JLabel("CF:"));
        d.add(tCF);

        JButton bS = new JButton(BTN_SALVA);
        d.add(bS);
        bS.addActionListener(e -> {
            try {
                controller.salvaPaziente(tCF.getText(), tN.getText(), tC.getText(), m);
                d.dispose();
                rinfrescaDashboard();
            } catch (Exception ex) {
                gestisciEccezioneGUI(ex);
            }
        });
        d.setVisible(true);
    }

    private JPanel creaPannelloMedici() {
        DefaultTableModel tm = new DefaultTableModel(new String[]{"ID", "Nome", "Cognome", "Reparto", "Tipo"}, 0);
        JTable table = new JTable(tm);
        for (Medico m : controller.getMedici()) {
            String reparto = m.getReparto() != null ? m.getReparto().getNome() : "";
            tm.addRow(new Object[]{m.getIdMedico(), m.getNome(), m.getCognome(), reparto, m.getTipoMedico()});
        }

        JPanel p = new JPanel(new BorderLayout());
        p.add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel bP = new JPanel();
        JButton bA = new JButton(BTN_AGGIUNGI);
        JButton bM = new JButton(BTN_MODIFICA);
        JButton bE = new JButton(BTN_ELIMINA);
        bP.add(bA);
        bP.add(bM);
        bP.add(bE);
        p.add(bP, BorderLayout.SOUTH);

        bA.addActionListener(e -> apriDialogMedico(null));
        bM.addActionListener(e -> {
            int r = table.getSelectedRow();
            if (r != -1) {
                apriDialogMedico(controller.getMedici().get(r));
            } else {
                JOptionPane.showMessageDialog(this, MSG_SELEZIONA_RIGA);
            }
        });
        bE.addActionListener(e -> {
            int r = table.getSelectedRow();
            if (r != -1 && JOptionPane.showConfirmDialog(this, MSG_CONFERMA_ELIMINAZIONE) == JOptionPane.YES_OPTION) {
                try {
                    controller.eliminaMedico((int) tm.getValueAt(r, 0));
                    rinfrescaDashboard();
                } catch (Exception ex) {
                    gestisciEccezioneGUI(ex);
                }
            }
        });
        return p;
    }

    private void apriDialogMedico(Medico med) {
        boolean m = med != null;
        JDialog d = new JDialog(this, m ? BTN_MODIFICA : BTN_AGGIUNGI, true);
        d.setLayout(new GridLayout(7, 2));
        d.setSize(350, 300);
        d.setLocationRelativeTo(this);

        JTextField tN = new JTextField(m ? med.getNome() : "");
        JTextField tC = new JTextField(m ? med.getCognome() : "");
        JTextField tE = new JTextField(m ? med.getEmail() : "");
        JTextField tP = new JTextField(m ? med.getPassword() : "");
        JTextField tT = new JTextField(m ? med.getTipoMedico() : "");

        JComboBox<Reparto> cR = new JComboBox<>();
        for (Reparto r : controller.getReparti()) {
            cR.addItem(r);
            if (m && med.getReparto() != null && r.getId() == med.getReparto().getId()) {
                cR.setSelectedItem(r);
            }
        }

        d.add(new JLabel("Nome:")); d.add(tN);
        d.add(new JLabel("Cognome:")); d.add(tC);
        d.add(new JLabel("Email:")); d.add(tE);
        d.add(new JLabel("Password:")); d.add(tP);
        d.add(new JLabel("Tipo:")); d.add(tT);
        d.add(new JLabel("Reparto:")); d.add(cR);

        JButton bS = new JButton(BTN_SALVA);
        d.add(bS);
        bS.addActionListener(e -> {
            try {
                controller.salvaMedico(m ? med.getIdMedico() : -1, tN.getText(), tC.getText(), tE.getText(), tP.getText(), tT.getText(), (Reparto) cR.getSelectedItem(), m);
                d.dispose();
                rinfrescaDashboard();
            } catch (Exception ex) {
                gestisciEccezioneGUI(ex);
            }
        });
        d.setVisible(true);
    }

    private JPanel creaPannelloVisite() {
        DefaultTableModel tm = new DefaultTableModel(new String[]{"ID", "Nome Visita", "Ricovero ID", RUOLO_MEDICO}, 0);
        JTable table = new JTable(tm);

        popolaTabellaVisite(tm);

        JPanel p = new JPanel(new BorderLayout());
        p.add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel bP = new JPanel();
        JButton bA = new JButton(BTN_AGGIUNGI);
        JButton bE = new JButton(BTN_ELIMINA);
        bP.add(bA);
        bP.add(bE);
        p.add(bP, BorderLayout.SOUTH);

        bA.addActionListener(e -> {
            apriDialogVisita();
            popolaTabellaVisite(tm);
        });
        bE.addActionListener(e -> {
            int rw = table.getSelectedRow();
            if (rw != -1) {
                try {
                    controller.eliminaVisita((int) tm.getValueAt(rw, 0));
                    popolaTabellaVisite(tm);
                } catch (Exception ex) {
                    gestisciEccezioneGUI(ex);
                }
            } else {
                JOptionPane.showMessageDialog(this, MSG_SELEZIONA_RIGA);
            }
        });
        return p;
    }

    private void popolaTabellaVisite(DefaultTableModel tm) {
        tm.setRowCount(0);
        for (Visita v : controller.getVisite()) {
            String idRicovero = v.getRicovero() != null ? String.valueOf(v.getRicovero().getIdRicovero()) : "";
            String nomeMedico = v.getMedico() != null ? v.getMedico().getNome() : "";
            tm.addRow(new Object[]{v.getIdVisita(), v.getNomeVisita(), idRicovero, nomeMedico});
        }
    }

    private JPanel creaPannelloReparti() {
        DefaultTableModel tm = new DefaultTableModel(new String[]{"ID", "Nome"}, 0);
        JTable table = new JTable(tm);
        for (Reparto r : controller.getReparti()) {
            tm.addRow(new Object[]{r.getId(), r.getNome()});
        }

        JPanel p = new JPanel(new BorderLayout());
        p.add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel bP = new JPanel();
        JButton bA = new JButton(BTN_AGGIUNGI);
        JButton bM = new JButton(BTN_MODIFICA);
        JButton bE = new JButton(BTN_ELIMINA);
        bP.add(bA);
        bP.add(bM);
        bP.add(bE);
        p.add(bP, BorderLayout.SOUTH);

        bA.addActionListener(e -> apriDialogReparto(null));
        bM.addActionListener(e -> {
            int rw = table.getSelectedRow();
            if (rw != -1) {
                apriDialogReparto(controller.getReparti().get(rw));
            } else {
                JOptionPane.showMessageDialog(this, MSG_SELEZIONA_RIGA);
            }
        });
        bE.addActionListener(e -> {
            int rw = table.getSelectedRow();
            if (rw != -1 && JOptionPane.showConfirmDialog(this, MSG_CONFERMA_ELIMINAZIONE) == JOptionPane.YES_OPTION) {
                try {
                    controller.eliminaReparto((int) tm.getValueAt(rw, 0));
                    rinfrescaDashboard();
                } catch (Exception ex) {
                    gestisciEccezioneGUI(ex);
                }
            }
        });
        return p;
    }

    private void apriDialogReparto(Reparto r) {
        boolean m = r != null;
        JDialog d = new JDialog(this, m ? BTN_MODIFICA : BTN_AGGIUNGI, true);
        d.setLayout(new GridLayout(2, 2));
        d.setSize(300, 100);
        d.setLocationRelativeTo(this);

        JTextField tN = new JTextField(m ? r.getNome() : "");
        d.add(new JLabel("Nome Reparto:"));
        d.add(tN);

        JButton bS = new JButton(BTN_SALVA);
        d.add(bS);
        bS.addActionListener(e -> {
            try {
                controller.salvaReparto(m ? r.getId() : -1, tN.getText(), m);
                d.dispose();
                rinfrescaDashboard();
            } catch (Exception ex) {
                gestisciEccezioneGUI(ex);
            }
        });
        d.setVisible(true);
    }

    private JPanel creaPannelloStanze() {
        DefaultTableModel tm = new DefaultTableModel(new String[]{"ID Stanza", "Reparto"}, 0);
        JTable table = new JTable(tm);
        for (Stanza s : controller.getStanze()) {
            String reparto = s.getReparto() != null ? s.getReparto().getNome() : "";
            tm.addRow(new Object[]{s.getIdStanza(), reparto});
        }

        JPanel p = new JPanel(new BorderLayout());
        p.add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel bP = new JPanel();
        JButton bA = new JButton(BTN_AGGIUNGI);
        JButton bM = new JButton(BTN_MODIFICA);
        JButton bE = new JButton(BTN_ELIMINA);
        bP.add(bA);
        bP.add(bM);
        bP.add(bE);
        p.add(bP, BorderLayout.SOUTH);

        bA.addActionListener(e -> apriDialogStanza(null));
        bM.addActionListener(e -> {
            int rw = table.getSelectedRow();
            if (rw != -1) {
                apriDialogStanza(controller.getStanze().get(rw));
            } else {
                JOptionPane.showMessageDialog(this, MSG_SELEZIONA_RIGA);
            }
        });
        bE.addActionListener(e -> {
            int rw = table.getSelectedRow();
            if (rw != -1 && JOptionPane.showConfirmDialog(this, MSG_CONFERMA_ELIMINAZIONE) == JOptionPane.YES_OPTION) {
                try {
                    controller.eliminaStanza((int) tm.getValueAt(rw, 0));
                    rinfrescaDashboard();
                } catch (Exception ex) {
                    gestisciEccezioneGUI(ex);
                }
            }
        });
        return p;
    }

    private void apriDialogStanza(Stanza s) {
        boolean m = s != null;
        JDialog d = new JDialog(this, m ? BTN_MODIFICA : BTN_AGGIUNGI, true);
        d.setLayout(new GridLayout(2, 2));
        d.setSize(300, 100);
        d.setLocationRelativeTo(this);

        JComboBox<Reparto> cR = new JComboBox<>();
        for (Reparto r : controller.getReparti()) {
            cR.addItem(r);
            if (m && s.getReparto() != null && r.getId() == s.getReparto().getId()) {
                cR.setSelectedItem(r);
            }
        }

        d.add(new JLabel("Reparto:"));
        d.add(cR);

        JButton bS = new JButton(BTN_SALVA);
        d.add(bS);
        bS.addActionListener(e -> {
            try {
                controller.salvaStanza(m ? s.getIdStanza() : -1, (Reparto) cR.getSelectedItem(), m);
                d.dispose();
                rinfrescaDashboard();
            } catch (Exception ex) {
                gestisciEccezioneGUI(ex);
            }
        });
        d.setVisible(true);
    }

    private JPanel creaPannelloLetti() {
        DefaultTableModel tm = new DefaultTableModel(new String[]{"ID Letto", "Stanza Appartenenza"}, 0);
        JTable table = new JTable(tm);
        for (Letto l : controller.getLetti()) {
            String stanza = l.getStanza() != null ? l.getStanza().toString() : "";
            tm.addRow(new Object[]{l.getIdLetto(), stanza});
        }

        JPanel p = new JPanel(new BorderLayout());
        p.add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel bP = new JPanel();
        JButton bA = new JButton(BTN_AGGIUNGI);
        JButton bM = new JButton(BTN_MODIFICA);
        JButton bE = new JButton(BTN_ELIMINA);
        bP.add(bA);
        bP.add(bM);
        bP.add(bE);
        p.add(bP, BorderLayout.SOUTH);

        bA.addActionListener(e -> apriDialogLetto(null));
        bM.addActionListener(e -> {
            int rw = table.getSelectedRow();
            if (rw != -1) {
                apriDialogLetto(controller.getLetti().get(rw));
            } else {
                JOptionPane.showMessageDialog(this, MSG_SELEZIONA_RIGA);
            }
        });
        bE.addActionListener(e -> {
            int rw = table.getSelectedRow();
            if (rw != -1 && JOptionPane.showConfirmDialog(this, MSG_CONFERMA_ELIMINAZIONE) == JOptionPane.YES_OPTION) {
                try {
                    controller.eliminaLetto((int) tm.getValueAt(rw, 0));
                    rinfrescaDashboard();
                } catch (Exception ex) {
                    gestisciEccezioneGUI(ex);
                }
            }
        });
        return p;
    }

    private void apriDialogLetto(Letto l) {
        boolean m = l != null;
        JDialog d = new JDialog(this, m ? BTN_MODIFICA : BTN_AGGIUNGI, true);
        d.setLayout(new GridLayout(2, 2));
        d.setSize(300, 100);
        d.setLocationRelativeTo(this);

        JComboBox<Stanza> cS = new JComboBox<>();
        for (Stanza s : controller.getStanze()) {
            cS.addItem(s);
            if (m && l.getStanza() != null && s.getIdStanza() == l.getStanza().getIdStanza()) {
                cS.setSelectedItem(s);
            }
        }

        d.add(new JLabel("Seleziona Stanza:"));
        d.add(cS);

        JButton bS = new JButton(BTN_SALVA);
        d.add(bS);
        bS.addActionListener(e -> {
            try {
                controller.salvaLetto(m ? l.getIdLetto() : -1, (Stanza) cS.getSelectedItem(), m);
                d.dispose();
                rinfrescaDashboard();
            } catch (Exception ex) {
                gestisciEccezioneGUI(ex);
            }
        });
        d.setVisible(true);
    }

    private JPanel creaPannelloTurni() {
        DefaultTableModel tm = new DefaultTableModel(new String[]{"ID", "Inizio", "Fine", RUOLO_MEDICO}, 0);
        JTable table = new JTable(tm);
        for (TurnoLavorativo t : controller.getTurni()) {
            String medico = t.getMedico() != null ? t.getMedico().getNome() : "";
            tm.addRow(new Object[]{t.getIdTurno(), t.getDataOraInizio(), t.getDataOraFine(), medico});
        }

        JPanel p = new JPanel(new BorderLayout());
        p.add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel bP = new JPanel();
        JButton bA = new JButton(BTN_AGGIUNGI);
        JButton bM = new JButton(BTN_MODIFICA);
        JButton bE = new JButton(BTN_ELIMINA);
        bP.add(bA);
        bP.add(bM);
        bP.add(bE);
        p.add(bP, BorderLayout.SOUTH);

        bA.addActionListener(e -> apriDialogTurno(null));
        bM.addActionListener(e -> {
            int rw = table.getSelectedRow();
            if (rw != -1) {
                apriDialogTurno(controller.getTurni().get(rw));
            } else {
                JOptionPane.showMessageDialog(this, MSG_SELEZIONA_RIGA);
            }
        });
        bE.addActionListener(e -> {
            int rw = table.getSelectedRow();
            if (rw != -1 && JOptionPane.showConfirmDialog(this, MSG_CONFERMA_ELIMINAZIONE) == JOptionPane.YES_OPTION) {
                try {
                    controller.eliminaTurno((int) tm.getValueAt(rw, 0));
                    rinfrescaDashboard();
                } catch (Exception ex) {
                    gestisciEccezioneGUI(ex);
                }
            }
        });
        return p;
    }

    private void apriDialogTurno(TurnoLavorativo t) {
        boolean m = t != null;
        JDialog d = new JDialog(this, m ? BTN_MODIFICA : BTN_AGGIUNGI, true);
        d.setLayout(new GridLayout(4, 2));
        d.setSize(400, 200);
        d.setLocationRelativeTo(this);

        JTextField tI = new JTextField(m ? t.getDataOraInizio().format(formatter) : "2024-01-01 08:00");
        JTextField tF = new JTextField(m ? t.getDataOraFine().format(formatter) : "2024-01-01 16:00");

        JComboBox<Medico> cM = new JComboBox<>();
        for (Medico med : controller.getMedici()) {
            cM.addItem(med);
            if (m && t.getMedico() != null && med.getIdMedico() == t.getMedico().getIdMedico()) {
                cM.setSelectedItem(med);
            }
        }

        d.add(new JLabel("Inizio (yyyy-MM-dd HH:mm):")); d.add(tI);
        d.add(new JLabel("Fine (yyyy-MM-dd HH:mm):")); d.add(tF);
        d.add(new JLabel("Medico:")); d.add(cM);

        JButton bS = new JButton(BTN_SALVA);
        d.add(bS);
        bS.addActionListener(e -> {
            try {
                controller.salvaTurno(m ? t.getIdTurno() : -1, LocalDateTime.parse(tI.getText(), formatter), LocalDateTime.parse(tF.getText(), formatter), (Medico) cM.getSelectedItem(), m);
                d.dispose();
                rinfrescaDashboard();
            } catch (DateTimeParseException ex) {
                JOptionPane.showMessageDialog(d, "Formato data errato! Usa yyyy-MM-dd HH:mm");
            } catch (Exception ex) {
                gestisciEccezioneGUI(ex);
            }
        });
        d.setVisible(true);
    }

    private void apriDialogVisita() {
        JDialog d = new JDialog(this, "Aggiungi Visita", true);
        d.setLayout(new GridLayout(4, 2));
        d.setSize(400, 200);
        d.setLocationRelativeTo(this);

        JTextField tN = new JTextField();
        JComboBox<Ricovero> cR = new JComboBox<>();
        for (Ricovero r : controller.getRicoveri()) cR.addItem(r);

        JComboBox<Medico> cM = new JComboBox<>();
        for (Medico m : controller.getMedici()) cM.addItem(m);

        d.add(new JLabel("Nome Visita:")); d.add(tN);
        d.add(new JLabel("Ricovero:")); d.add(cR);
        d.add(new JLabel("Medico:")); d.add(cM);

        JButton bS = new JButton(BTN_SALVA);
        d.add(bS);
        bS.addActionListener(e -> {
            try {
                controller.salvaVisita(tN.getText(), (Ricovero) cR.getSelectedItem(), (Medico) cM.getSelectedItem());
                d.dispose();
                rinfrescaDashboard();
            } catch (Exception ex) {
                gestisciEccezioneGUI(ex);
            }
        });
        d.setVisible(true);
    }

    private JPanel creaPannelloRicoveri() {
        DefaultTableModel tm = new DefaultTableModel(new String[]{"ID", "Paziente", "Letto", "Inizio", "Fine"}, 0);
        JTable table = new JTable(tm);
        for (Ricovero r : controller.getRicoveri()) {
            String paziente = r.getPaziente() != null ? r.getPaziente().getCodFiscale() : "";
            String letto = r.getLetto() != null ? String.valueOf(r.getLetto().getIdLetto()) : "";
            tm.addRow(new Object[]{r.getIdRicovero(), paziente, letto, r.getDataOraInizio(), r.getDataOraFine()});
        }

        JPanel p = new JPanel(new BorderLayout());
        p.add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel bP = new JPanel();
        JButton bA = new JButton(BTN_AGGIUNGI);
        JButton bM = new JButton(BTN_MODIFICA);
        JButton bE = new JButton(BTN_ELIMINA);
        bP.add(bA);
        bP.add(bM);
        bP.add(bE);
        p.add(bP, BorderLayout.SOUTH);

        bA.addActionListener(e -> apriDialogRicovero(null));
        bM.addActionListener(e -> {
            int rw = table.getSelectedRow();
            if (rw != -1) {
                apriDialogRicovero(controller.getRicoveri().get(rw));
            } else {
                JOptionPane.showMessageDialog(this, MSG_SELEZIONA_RIGA);
            }
        });
        bE.addActionListener(e -> {
            int rw = table.getSelectedRow();
            if (rw != -1 && JOptionPane.showConfirmDialog(this, MSG_CONFERMA_ELIMINAZIONE) == JOptionPane.YES_OPTION) {
                try {
                    controller.eliminaRicovero((int) tm.getValueAt(rw, 0));
                    rinfrescaDashboard();
                } catch (Exception ex) {
                    gestisciEccezioneGUI(ex);
                }
            }
        });
        return p;
    }

    private void apriDialogRicovero(Ricovero r) {
        boolean m = r != null;
        JDialog d = new JDialog(this, m ? BTN_MODIFICA : BTN_AGGIUNGI, true);
        d.setLayout(new GridLayout(5, 2));
        d.setSize(400, 250);
        d.setLocationRelativeTo(this);

        JComboBox<Paziente> cP = new JComboBox<>();
        for (Paziente p : controller.getPazienti()) {
            cP.addItem(p);
            if (m && r.getPaziente() != null && p.getCodFiscale().equals(r.getPaziente().getCodFiscale())) {
                cP.setSelectedItem(p);
            }
        }

        JComboBox<Letto> cL = new JComboBox<>();
        for (Letto l : controller.getLetti()) {
            cL.addItem(l);
            if (m && r.getLetto() != null && l.getIdLetto() == r.getLetto().getIdLetto()) {
                cL.setSelectedItem(l);
            }
        }

        JTextField tI = new JTextField(m && r.getDataOraInizio() != null ? r.getDataOraInizio().format(formatter) : "2024-01-01 08:00");
        JTextField tF = new JTextField(m && r.getDataOraFine() != null ? r.getDataOraFine().format(formatter) : "2024-01-10 16:00");

        d.add(new JLabel("Paziente:")); d.add(cP);
        d.add(new JLabel("Letto:")); d.add(cL);
        d.add(new JLabel("Inizio:")); d.add(tI);
        d.add(new JLabel("Fine:")); d.add(tF);

        JButton bS = new JButton(BTN_SALVA);
        d.add(bS);
        bS.addActionListener(e -> {
            try {
                controller.salvaRicovero(m ? r.getIdRicovero() : -1, LocalDateTime.parse(tI.getText(), formatter), LocalDateTime.parse(tF.getText(), formatter), (Paziente) cP.getSelectedItem(), (Letto) cL.getSelectedItem(), m);
                d.dispose();
                rinfrescaDashboard();
            } catch (Exception ex) {
                gestisciEccezioneGUI(ex);
            }
        });
        d.setVisible(true);
    }

    private JPanel creaPannelloInterventi() {
        DefaultTableModel tm = new DefaultTableModel(new String[]{"ID", "Nome", "Inizio", "Fine", "Visita ID"}, 0);
        JTable table = new JTable(tm);

        popolaTabellaInterventi(tm);

        JPanel p = new JPanel(new BorderLayout());
        p.add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel bP = new JPanel();
        JButton bA = new JButton(BTN_AGGIUNGI);
        JButton bM = new JButton(BTN_MODIFICA);
        JButton bE = new JButton(BTN_ELIMINA);
        bP.add(bA);
        bP.add(bM);
        bP.add(bE);
        p.add(bP, BorderLayout.SOUTH);

        bA.addActionListener(e -> {
            apriDialogIntervento(null);
            popolaTabellaInterventi(tm);
        });
        bM.addActionListener(e -> {
            int rw = table.getSelectedRow();
            if (rw != -1) {
                apriDialogIntervento(controller.getInterventi().get(rw));
                popolaTabellaInterventi(tm);
            } else {
                JOptionPane.showMessageDialog(this, "Seleziona un intervento!");
            }
        });
        bE.addActionListener(e -> {
            int rw = table.getSelectedRow();
            if (rw != -1 && JOptionPane.showConfirmDialog(this, MSG_CONFERMA_ELIMINAZIONE) == JOptionPane.YES_OPTION) {
                try {
                    controller.eliminaIntervento((int) tm.getValueAt(rw, 0));
                    popolaTabellaInterventi(tm);
                } catch (Exception ex) {
                    gestisciEccezioneGUI(ex);
                }
            }
        });
        return p;
    }

    private void popolaTabellaInterventi(DefaultTableModel tm) {
        tm.setRowCount(0);
        for (InterventoChirurgico i : controller.getInterventi()) {
            String visitaId = i.getVisita() != null ? String.valueOf(i.getVisita().getIdVisita()) : "";
            tm.addRow(new Object[]{i.getIdIntervento(), i.getNomeIntervento(), i.getDataOraInizio(), i.getDataOraFine(), visitaId});
        }
    }

    private void apriDialogIntervento(InterventoChirurgico i) {
        boolean m = i != null;
        JDialog d = new JDialog(this, m ? BTN_MODIFICA : BTN_AGGIUNGI, true);
        d.setLayout(new GridLayout(6, 2, 10, 10));
        d.setSize(450, 300);
        d.setLocationRelativeTo(this);

        JTextField tN = new JTextField(m ? i.getNomeIntervento() : "");
        JTextField tI = new JTextField(m && i.getDataOraInizio() != null ? i.getDataOraInizio().format(formatter) : "2024-01-01 08:00");
        JTextField tF = new JTextField(m && i.getDataOraFine() != null ? i.getDataOraFine().format(formatter) : "2024-01-01 10:00");

        JComboBox<Visita> cV = new JComboBox<>();
        for (Visita v : controller.getVisite()) {
            cV.addItem(v);
            if (m && i.getVisita() != null && v.getIdVisita() == i.getVisita().getIdVisita()) {
                cV.setSelectedItem(v);
            }
        }

        d.add(new JLabel("Nome Intervento:")); d.add(tN);
        d.add(new JLabel("Inizio (yyyy-MM-dd HH:mm):")); d.add(tI);
        d.add(new JLabel("Fine (yyyy-MM-dd HH:mm):")); d.add(tF);
        d.add(new JLabel("Visita:")); d.add(cV);

        JButton bS = new JButton(BTN_SALVA);
        d.add(bS);
        bS.addActionListener(e -> {
            try {
                controller.salvaIntervento(
                        m ? i.getIdIntervento() : -1,
                        tN.getText(),
                        LocalDateTime.parse(tI.getText(), formatter),
                        LocalDateTime.parse(tF.getText(), formatter),
                        (Visita) cV.getSelectedItem(),
                        m
                );
                d.dispose();
                rinfrescaDashboard();
            } catch (DateTimeParseException ex) {
                JOptionPane.showMessageDialog(d, "Formato data errato! Usa yyyy-MM-dd HH:mm");
            } catch (Exception ex) {
                gestisciEccezioneGUI(ex);
            }
        });
        d.setVisible(true);
    }

    private JPanel creaPannelloAmministratori() {
        DefaultTableModel tm = new DefaultTableModel(new String[]{"ID", "Nome", "Cognome", "Email"}, 0);
        JTable table = new JTable(tm);
        for (Amministratore a : controller.getAmministratori()) {
            tm.addRow(new Object[]{a.getId(), a.getNome(), a.getCognome(), a.getEmail()});
        }

        JPanel p = new JPanel(new BorderLayout());
        p.add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel bP = new JPanel();
        JButton bA = new JButton(BTN_AGGIUNGI);
        JButton bM = new JButton(BTN_MODIFICA);
        JButton bE = new JButton(BTN_ELIMINA);
        bP.add(bA);
        bP.add(bM);
        bP.add(bE);
        p.add(bP, BorderLayout.SOUTH);

        bA.addActionListener(e -> apriDialogAdmin(null));
        bM.addActionListener(e -> {
            int rw = table.getSelectedRow();
            if (rw != -1) {
                apriDialogAdmin(controller.getAmministratori().get(rw));
            } else {
                JOptionPane.showMessageDialog(this, MSG_SELEZIONA_RIGA);
            }
        });
        bE.addActionListener(e -> {
            int rw = table.getSelectedRow();
            if (rw != -1 && JOptionPane.showConfirmDialog(this, MSG_CONFERMA_ELIMINAZIONE) == JOptionPane.YES_OPTION) {
                try {
                    controller.eliminaAmministratore((int) tm.getValueAt(rw, 0));
                    rinfrescaDashboard();
                } catch (Exception ex) {
                    gestisciEccezioneGUI(ex);
                }
            }
        });
        return p;
    }

    private void apriDialogAdmin(Amministratore a) {
        boolean m = a != null;
        JDialog d = new JDialog(this, m ? BTN_MODIFICA : BTN_AGGIUNGI, true);
        d.setLayout(new GridLayout(5, 2));
        d.setSize(300, 200);
        d.setLocationRelativeTo(this);

        JTextField tN = new JTextField(m ? a.getNome() : "");
        JTextField tC = new JTextField(m ? a.getCognome() : "");
        JTextField tE = new JTextField(m ? a.getEmail() : "");
        JTextField tP = new JTextField(m ? a.getPassword() : "");

        d.add(new JLabel("Nome:")); d.add(tN);
        d.add(new JLabel("Cognome:")); d.add(tC);
        d.add(new JLabel("Email:")); d.add(tE);
        d.add(new JLabel("Password:")); d.add(tP);

        JButton bS = new JButton(BTN_SALVA);
        d.add(bS);
        bS.addActionListener(e -> {
            try {
                controller.salvaAmministratore(m ? a.getId() : -1, tN.getText(), tC.getText(), tE.getText(), tP.getText(), m);
                d.dispose();
                rinfrescaDashboard();
            } catch (Exception ex) {
                gestisciEccezioneGUI(ex);
            }
        });
        d.setVisible(true);
    }

    private JPanel creaPannelloGestisce() {
        JPanel panel = new JPanel(new BorderLayout());
        DefaultTableModel tModel = new DefaultTableModel(new String[]{"ID Medico", "Nome Medico", "ID Ricovero", "Paziente"}, 0);
        JTable table = new JTable(tModel);

        for (Gestisce g : controller.getGestisce()) {
            Medico m = null;
            Ricovero r = null;
            for (Medico med : controller.getMedici()) {
                if (med.getIdMedico() == g.getIdMedico()) m = med;
            }
            for (Ricovero ric : controller.getRicoveri()) {
                if (ric.getIdRicovero() == g.getIdRicovero()) r = ric;
            }
            String mNome = m != null ? m.getNome() + " " + m.getCognome() : "Sconosciuto";
            String rNome = r != null && r.getPaziente() != null ? r.getPaziente().getCodFiscale() : "Sconosciuto";
            tModel.addRow(new Object[]{g.getIdMedico(), mNome, g.getIdRicovero(), rNome});
        }
        panel.add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel topPanel = new JPanel();
        JComboBox<Medico> cmbMedici = new JComboBox<>();
        for (Medico m : controller.getMedici()) cmbMedici.addItem(m);

        JComboBox<Ricovero> cmbRicoveri = new JComboBox<>();
        for (Ricovero r : controller.getRicoveri()) cmbRicoveri.addItem(r);

        JButton btnAssocia = new JButton("Associa");
        JButton btnRimuovi = new JButton("Scollega Selezionato");

        topPanel.add(new JLabel("Medico:")); topPanel.add(cmbMedici);
        topPanel.add(new JLabel("Ricovero:")); topPanel.add(cmbRicoveri);
        topPanel.add(btnAssocia);

        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(btnRimuovi, BorderLayout.SOUTH);

        btnAssocia.addActionListener(e -> {
            Medico m = (Medico) cmbMedici.getSelectedItem();
            Ricovero r = (Ricovero) cmbRicoveri.getSelectedItem();
            if (m != null && r != null) {
                try {
                    controller.collegaMedicoRicovero(m.getIdMedico(), r.getIdRicovero());
                    rinfrescaDashboard();
                } catch (Exception ex) {
                    gestisciEccezioneGUI(ex);
                }
            }
        });
        btnRimuovi.addActionListener(e -> {
            int rw = table.getSelectedRow();
            if (rw != -1) {
                try {
                    controller.scollegaMedicoRicovero((int) tModel.getValueAt(rw, 0), (int) tModel.getValueAt(rw, 2));
                    rinfrescaDashboard();
                } catch (Exception ex) {
                    gestisciEccezioneGUI(ex);
                }
            } else {
                JOptionPane.showMessageDialog(this, MSG_SELEZIONA_RIGA);
            }
        });
        return panel;
    }

    private JPanel creaPannelloOpera() {
        JPanel panel = new JPanel(new BorderLayout());
        DefaultTableModel tModel = new DefaultTableModel(new String[]{"ID Medico", RUOLO_MEDICO, "ID Intervento", "Intervento", "Ruolo"}, 0);
        JTable table = new JTable(tModel);

        for (Opera o : controller.getOpera()) {
            Medico m = null;
            InterventoChirurgico ic = null;
            for (Medico med : controller.getMedici()) {
                if (med.getIdMedico() == o.getIdMedico()) m = med;
            }
            for (InterventoChirurgico i : controller.getInterventi()) {
                if (i.getIdIntervento() == o.getIdIntervento()) ic = i;
            }
            String mNome = m != null ? m.getNome() + " " + m.getCognome() : "N/D";
            String iNome = ic != null ? ic.getNomeIntervento() : "N/D";
            tModel.addRow(new Object[]{o.getIdMedico(), mNome, o.getIdIntervento(), iNome, o.getRuolo()});
        }
        panel.add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel topPanel = new JPanel();
        JComboBox<Medico> cmbMedici = new JComboBox<>();
        for (Medico m : controller.getMedici()) cmbMedici.addItem(m);

        JComboBox<InterventoChirurgico> cmbInt = new JComboBox<>();
        for (InterventoChirurgico i : controller.getInterventi()) cmbInt.addItem(i);

        JTextField txtRuolo = new JTextField(10);

        JButton btnAssocia = new JButton("Associa");
        JButton btnRimuovi = new JButton("Scollega Selezionato");

        topPanel.add(new JLabel("Medico:")); topPanel.add(cmbMedici);
        topPanel.add(new JLabel("Intervento:")); topPanel.add(cmbInt);
        topPanel.add(new JLabel("Ruolo:")); topPanel.add(txtRuolo);
        topPanel.add(btnAssocia);

        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(btnRimuovi, BorderLayout.SOUTH);

        btnAssocia.addActionListener(e -> {
            Medico m = (Medico) cmbMedici.getSelectedItem();
            InterventoChirurgico i = (InterventoChirurgico) cmbInt.getSelectedItem();
            if (m != null && i != null) {
                try {
                    controller.collegaMedicoIntervento(m.getIdMedico(), i.getIdIntervento(), txtRuolo.getText());
                    txtRuolo.setText("");
                    rinfrescaDashboard();
                } catch (Exception ex) {
                    gestisciEccezioneGUI(ex);
                }
            }
        });
        btnRimuovi.addActionListener(e -> {
            int rw = table.getSelectedRow();
            if (rw != -1) {
                try {
                    controller.scollegaMedicoIntervento((int) tModel.getValueAt(rw, 0), (int) tModel.getValueAt(rw, 2));
                    rinfrescaDashboard();
                } catch (Exception ex) {
                    gestisciEccezioneGUI(ex);
                }
            } else {
                JOptionPane.showMessageDialog(this, MSG_SELEZIONA_RIGA);
            }
        });
        return panel;
    }

    private void gestisciEccezioneGUI(Exception ex) {
        LOGGER.log(Level.SEVERE, "Si è verificato un errore:", ex);
        JOptionPane.showMessageDialog(this, "Operazione fallita: " + ex.getMessage(), TITOLO_ERRORE, JOptionPane.ERROR_MESSAGE);
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Impossibile impostare il Look and Feel di sistema", e);
        }
        SwingUtilities.invokeLater(() -> new MainGUI().setVisible(true));
    }
}
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

public class MainGUI extends JFrame {

    private Controller controller;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private JTabbedPane tabsDashboard;

    public MainGUI() {
        controller = new Controller();
        setTitle("Sistema Gestione Ospedale - Enterprise Edition");
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        mostraSchermataLogin();
    }




    private void mostraSchermataLogin() {
        getContentPane().removeAll();
        JPanel panelLogin = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblTitolo = new JLabel("Accesso al Sistema", SwingConstants.CENTER);
        lblTitolo.setFont(new Font("Arial", Font.BOLD, 24));

        JComboBox<String> comboRuolo = new JComboBox<>(new String[]{"Amministratore", "Medico"});
        JTextField txtEmail = new JTextField(20);
        JPasswordField txtPassword = new JPasswordField(20);

        JButton btnLogin = new JButton("Accedi");
        btnLogin.setFont(new Font("Arial", Font.BOLD, 14));

        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2; panelLogin.add(lblTitolo, gbc);
        gbc.gridwidth = 1; gbc.gridy = 1; panelLogin.add(new JLabel("Ruolo:"), gbc); gbc.gridx = 1; panelLogin.add(comboRuolo, gbc);
        gbc.gridx = 0; gbc.gridy = 2; panelLogin.add(new JLabel("Email:"), gbc); gbc.gridx = 1; panelLogin.add(txtEmail, gbc);
        gbc.gridx = 0; gbc.gridy = 3; panelLogin.add(new JLabel("Password:"), gbc); gbc.gridx = 1; panelLogin.add(txtPassword, gbc);
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2; panelLogin.add(btnLogin, gbc);

        btnLogin.addActionListener(e -> {
            try {
                boolean ok = "Amministratore".equals(comboRuolo.getSelectedItem()) ?
                        controller.loginAmministratore(txtEmail.getText(), new String(txtPassword.getPassword())) :
                        controller.loginMedico(txtEmail.getText(), new String(txtPassword.getPassword()));
                if (ok) mostraDashboard(); else JOptionPane.showMessageDialog(this, "Credenziali errate!", "Errore", JOptionPane.ERROR_MESSAGE);
            } catch (SQLException ex) { JOptionPane.showMessageDialog(this, "Errore DB: " + ex.getMessage()); }
        });

        add(panelLogin); revalidate(); repaint();
    }


    //  MOTORE DI SINCRONIZZAZIONE GLOBALE

    private void mostraDashboard() {
        getContentPane().removeAll();
        tabsDashboard = new JTabbedPane();
        tabsDashboard.setFont(new Font("Arial", Font.BOLD, 12));

        if (controller.isAmministratore()) {
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
        } else {
            tabsDashboard.addTab("Il Mio Profilo", creaPannelloProfiloMedico());
            tabsDashboard.addTab("I Miei Pazienti", creaPannelloPazientiMedico());
            tabsDashboard.addTab("I Miei Turni", creaPannelloTurniMedico());
            tabsDashboard.addTab("I Miei Interventi", creaPannelloInterventiMedico());
            tabsDashboard.addTab("Le Mie Visite", creaPannelloVisiteMedico()); // AGGIUNGI QUESTA
        }

        JPanel pnlBottom = new JPanel(new BorderLayout());
        pnlBottom.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        String info = " Loggato come: " + controller.getUtenteLoggatoRuolo();
        if(!controller.isAmministratore() && getMedicoLoggato()!=null) info += " (" + getMedicoLoggato().getNome() + " " + getMedicoLoggato().getCognome() + ")";

        JButton btnLogout = new JButton("Logout");
        btnLogout.addActionListener(e -> mostraSchermataLogin());
        pnlBottom.add(new JLabel(info), BorderLayout.WEST); pnlBottom.add(btnLogout, BorderLayout.EAST);

        add(tabsDashboard, BorderLayout.CENTER); add(pnlBottom, BorderLayout.SOUTH);
        revalidate(); repaint();
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
        for (Medico m : controller.getMedici()) if (m.getIdMedico() == controller.getIdUtenteLoggato()) return m; return null;
    }


    //  PANNELLI MEDICO (SOLA LETTURA)

    private JPanel creaPannelloProfiloMedico() {
        JPanel p = new JPanel(new GridBagLayout()); GridBagConstraints g = new GridBagConstraints(); g.insets = new Insets(10,10,10,10); g.gridy = 0;
        Medico m = getMedicoLoggato(); if(m==null) return p;
        p.add(new JLabel("<html><h2>Scheda Personale</h2></html>"), g); g.gridy++;
        p.add(new JLabel("Nome: " + m.getNome() + " " + m.getCognome()), g); g.gridy++;
        p.add(new JLabel("Email: " + m.getEmail() + " | Qualifica: " + m.getTipoMedico()), g); g.gridy++;
        p.add(new JLabel("Reparto: " + (m.getReparto()!=null ? m.getReparto().getNome() : "Nessuno")), g);
        return p;
    }

    private JPanel creaPannelloPazientiMedico() {
        JPanel p = new JPanel(new BorderLayout());
        DefaultTableModel tm = new DefaultTableModel(new String[]{"CF", "Nome", "Cognome", "Letto Ricovero"}, 0);
        Medico m = getMedicoLoggato();
        if(m!=null && m.getRicoveri()!=null) {
            for(Ricovero r : m.getRicoveri()) if(r.getPaziente()!=null)
                tm.addRow(new Object[]{r.getPaziente().getCOD_FISCALE(), r.getPaziente().getNome(), r.getPaziente().getCognome(), r.getLetto()!=null ? r.getLetto().getId_letto() : "N/D"});
        }
        p.add(new JScrollPane(new JTable(tm)), BorderLayout.CENTER); return p;
    }

    private JPanel creaPannelloTurniMedico() {
        JPanel p = new JPanel(new BorderLayout());
        DefaultTableModel tm = new DefaultTableModel(new String[]{"Inizio", "Fine"}, 0);
        Medico m = getMedicoLoggato();
        if(m!=null && m.getTurniLavorativi()!=null) {
            for(TurnoLavorativo t : m.getTurniLavorativi()) tm.addRow(new Object[]{t.getDataOraInizio(), t.getDataOraFine()});
        }
        p.add(new JScrollPane(new JTable(tm)), BorderLayout.CENTER); return p;
    }

    private JPanel creaPannelloInterventiMedico() {
        JPanel p = new JPanel(new BorderLayout());
        DefaultTableModel tm = new DefaultTableModel(new String[]{"Nome Intervento"}, 0);
        Medico m = getMedicoLoggato();
        if(m!=null && m.getInterventi()!=null) {
            for(InterventoChirurgico i : m.getInterventi()) tm.addRow(new Object[]{i.getNomeIntervento()});
        }
        p.add(new JScrollPane(new JTable(tm)), BorderLayout.CENTER); return p;
    }


    // PANNELLI AMMINISTRATORE


    // --- PAZIENTI ---
    private JPanel creaPannelloPazienti() {
        DefaultTableModel tm = new DefaultTableModel(new String[]{"Cod Fiscale", "Nome", "Cognome"}, 0); JTable table = new JTable(tm);
        for (Paziente p : controller.getPazienti()) tm.addRow(new Object[]{p.getCOD_FISCALE(), p.getNome(), p.getCognome()});
        JPanel p = new JPanel(new BorderLayout()); p.add(new JScrollPane(table), BorderLayout.CENTER);
        JPanel bP = new JPanel(); JButton bA=new JButton("Aggiungi"); JButton bM=new JButton("Modifica"); JButton bE=new JButton("Elimina"); bP.add(bA); bP.add(bM); bP.add(bE); p.add(bP, BorderLayout.SOUTH);
        bA.addActionListener(e -> apriDialogPaziente(null));
        bM.addActionListener(e -> { int r=table.getSelectedRow(); if(r!=-1) apriDialogPaziente(controller.getPazienti().get(r)); else JOptionPane.showMessageDialog(this, "Seleziona una riga!"); });
        bE.addActionListener(e -> { int r=table.getSelectedRow(); if(r!=-1) { if(JOptionPane.showConfirmDialog(this,"Eliminare?")==JOptionPane.YES_OPTION) { try{controller.eliminaPaziente((String)tm.getValueAt(r,0)); rinfrescaDashboard();}catch(Exception ex){} } } else JOptionPane.showMessageDialog(this, "Seleziona una riga!"); });
        return p;
    }
    private void apriDialogPaziente(Paziente p) {
        boolean m = p != null; JDialog d = new JDialog(this, m?"Modifica":"Aggiungi", true); d.setLayout(new GridLayout(4,2)); d.setSize(300,200); d.setLocationRelativeTo(this);
        JTextField tN=new JTextField(m?p.getNome():""); JTextField tC=new JTextField(m?p.getCognome():""); JTextField tCF=new JTextField(m?p.getCOD_FISCALE():""); if(m) tCF.setEditable(false);
        d.add(new JLabel("Nome:")); d.add(tN); d.add(new JLabel("Cognome:")); d.add(tC); d.add(new JLabel("CF:")); d.add(tCF);
        JButton bS=new JButton("Salva"); d.add(bS); bS.addActionListener(e->{ try{controller.salvaPaziente(tCF.getText(),tN.getText(),tC.getText(),m); d.dispose(); rinfrescaDashboard();}catch(Exception ex){} }); d.setVisible(true);
    }

    // --- MEDICI ---
    private JPanel creaPannelloMedici() {
        DefaultTableModel tm = new DefaultTableModel(new String[]{"ID", "Nome", "Cognome", "Reparto", "Tipo"}, 0); JTable table = new JTable(tm);
        for (Medico m : controller.getMedici()) tm.addRow(new Object[]{m.getIdMedico(), m.getNome(), m.getCognome(), m.getReparto()!=null?m.getReparto().getNome():"", m.getTipoMedico()});
        JPanel p = new JPanel(new BorderLayout()); p.add(new JScrollPane(table), BorderLayout.CENTER);
        JPanel bP = new JPanel(); JButton bA=new JButton("Aggiungi"); JButton bM=new JButton("Modifica"); JButton bE=new JButton("Elimina"); bP.add(bA); bP.add(bM); bP.add(bE); p.add(bP, BorderLayout.SOUTH);
        bA.addActionListener(e -> apriDialogMedico(null));
        bM.addActionListener(e -> { int r=table.getSelectedRow(); if(r!=-1) apriDialogMedico(controller.getMedici().get(r)); });
        bE.addActionListener(e -> { int r=table.getSelectedRow(); if(r!=-1) { if(JOptionPane.showConfirmDialog(this,"Eliminare?")==JOptionPane.YES_OPTION) { try{controller.eliminaMedico((int)tm.getValueAt(r,0)); rinfrescaDashboard();}catch(Exception ex){} } } });
        return p;
    }
    private void apriDialogMedico(Medico med) {
        boolean m = med != null; JDialog d = new JDialog(this, m?"Modifica":"Aggiungi", true); d.setLayout(new GridLayout(7,2)); d.setSize(350,300); d.setLocationRelativeTo(this);
        JTextField tN=new JTextField(m?med.getNome():""); JTextField tC=new JTextField(m?med.getCognome():""); JTextField tE=new JTextField(m?med.getEmail():"");
        JTextField tP=new JTextField(m?med.getPassword():""); JTextField tT=new JTextField(m?med.getTipoMedico():"");
        JComboBox<Reparto> cR=new JComboBox<>(); for(Reparto r:controller.getReparti()) {cR.addItem(r); if(m&&med.getReparto()!=null&&r.getId()==med.getReparto().getId()) cR.setSelectedItem(r);}
        d.add(new JLabel("Nome:")); d.add(tN); d.add(new JLabel("Cognome:")); d.add(tC); d.add(new JLabel("Email:")); d.add(tE); d.add(new JLabel("Password:")); d.add(tP); d.add(new JLabel("Tipo:")); d.add(tT); d.add(new JLabel("Reparto:")); d.add(cR);
        JButton bS=new JButton("Salva"); d.add(bS); bS.addActionListener(e->{ try{controller.salvaMedico(m?med.getIdMedico():-1,tN.getText(),tC.getText(),tE.getText(),tP.getText(),tT.getText(),(Reparto)cR.getSelectedItem(),m); d.dispose(); rinfrescaDashboard();}catch(Exception ex){} }); d.setVisible(true);
    }

    private JPanel creaPannelloVisite() {
        DefaultTableModel tm = new DefaultTableModel(new String[]{"ID", "Nome Visita", "Ricovero ID", "Medico"}, 0);
        JTable table = new JTable(tm);
        Runnable popola = () -> {
            tm.setRowCount(0);
            for (Visita v : controller.getVisite())
                tm.addRow(new Object[]{v.getIdVisita(), v.getNomeVisita(), v.getRicovero()!=null?v.getRicovero().getIdRicovero():"", v.getMedico()!=null?v.getMedico().getNome():""});
        };
        popola.run();
        JPanel p = new JPanel(new BorderLayout()); p.add(new JScrollPane(table), BorderLayout.CENTER);
        JPanel bP = new JPanel(); JButton bA=new JButton("Aggiungi"); JButton bE=new JButton("Elimina"); bP.add(bA); bP.add(bE); p.add(bP, BorderLayout.SOUTH);

        bA.addActionListener(e -> { apriDialogVisita(); popola.run(); });
        bE.addActionListener(e -> { int rw=table.getSelectedRow(); if(rw!=-1) { try{controller.eliminaVisita((int)tm.getValueAt(rw,0)); popola.run();}catch(Exception ex){} } });
        return p;
    }

    // --- REPARTI ---
    private JPanel creaPannelloReparti() {
        DefaultTableModel tm = new DefaultTableModel(new String[]{"ID", "Nome"}, 0); JTable table = new JTable(tm);
        for (Reparto r : controller.getReparti()) tm.addRow(new Object[]{r.getId(), r.getNome()});
        JPanel p = new JPanel(new BorderLayout()); p.add(new JScrollPane(table), BorderLayout.CENTER);
        JPanel bP = new JPanel(); JButton bA=new JButton("Aggiungi"); JButton bM=new JButton("Modifica"); JButton bE=new JButton("Elimina"); bP.add(bA); bP.add(bM); bP.add(bE); p.add(bP, BorderLayout.SOUTH);
        bA.addActionListener(e -> apriDialogReparto(null));
        bM.addActionListener(e -> { int rw=table.getSelectedRow(); if(rw!=-1) apriDialogReparto(controller.getReparti().get(rw)); });
        bE.addActionListener(e -> { int rw=table.getSelectedRow(); if(rw!=-1) { if(JOptionPane.showConfirmDialog(this,"Eliminare?")==JOptionPane.YES_OPTION) { try{controller.eliminaReparto((int)tm.getValueAt(rw,0)); rinfrescaDashboard();}catch(Exception ex){} } } });
        return p;
    }
    private void apriDialogReparto(Reparto r) {
        boolean m = r != null; JDialog d = new JDialog(this, m?"Modifica":"Aggiungi", true); d.setLayout(new GridLayout(2,2)); d.setSize(300,100); d.setLocationRelativeTo(this);
        JTextField tN=new JTextField(m?r.getNome():""); d.add(new JLabel("Nome Reparto:")); d.add(tN);
        JButton bS=new JButton("Salva"); d.add(bS); bS.addActionListener(e->{ try{controller.salvaReparto(m?r.getId():-1,tN.getText(),m); d.dispose(); rinfrescaDashboard();}catch(Exception ex){} }); d.setVisible(true);
    }

    // --- STANZE ---
    private JPanel creaPannelloStanze() {
        DefaultTableModel tm = new DefaultTableModel(new String[]{"ID Stanza", "Reparto"}, 0); JTable table = new JTable(tm);
        for (Stanza s : controller.getStanze()) tm.addRow(new Object[]{s.getIdStanza(), s.getReparto()!=null?s.getReparto().getNome():""});
        JPanel p = new JPanel(new BorderLayout()); p.add(new JScrollPane(table), BorderLayout.CENTER);
        JPanel bP = new JPanel(); JButton bA=new JButton("Aggiungi"); JButton bM=new JButton("Modifica"); JButton bE=new JButton("Elimina"); bP.add(bA); bP.add(bM); bP.add(bE); p.add(bP, BorderLayout.SOUTH);
        bA.addActionListener(e -> apriDialogStanza(null));
        bM.addActionListener(e -> { int rw=table.getSelectedRow(); if(rw!=-1) apriDialogStanza(controller.getStanze().get(rw)); });
        bE.addActionListener(e -> { int rw=table.getSelectedRow(); if(rw!=-1) { if(JOptionPane.showConfirmDialog(this,"Eliminare?")==JOptionPane.YES_OPTION) { try{controller.eliminaStanza((int)tm.getValueAt(rw,0)); rinfrescaDashboard();}catch(Exception ex){} } } });
        return p;
    }
    private void apriDialogStanza(Stanza s) {
        boolean m = s != null; JDialog d = new JDialog(this, m?"Modifica":"Aggiungi", true); d.setLayout(new GridLayout(2,2)); d.setSize(300,100); d.setLocationRelativeTo(this);
        JComboBox<Reparto> cR=new JComboBox<>(); for(Reparto r:controller.getReparti()) {cR.addItem(r); if(m&&s.getReparto()!=null&&r.getId()==s.getReparto().getId()) cR.setSelectedItem(r);}
        d.add(new JLabel("Reparto:")); d.add(cR);
        JButton bS=new JButton("Salva"); d.add(bS); bS.addActionListener(e->{ try{controller.salvaStanza(m?s.getIdStanza():-1,(Reparto)cR.getSelectedItem(),m); d.dispose(); rinfrescaDashboard();}catch(Exception ex){} }); d.setVisible(true);
    }

    // --- LETTI ---
    private JPanel creaPannelloLetti() {
        DefaultTableModel tm = new DefaultTableModel(new String[]{"ID Letto", "Stanza Appartenenza"}, 0); JTable table = new JTable(tm);
        for (Letto l : controller.getLetti()) tm.addRow(new Object[]{l.getId_letto(), l.getStanza()!=null?l.getStanza().toString():""});
        JPanel p = new JPanel(new BorderLayout()); p.add(new JScrollPane(table), BorderLayout.CENTER);
        JPanel bP = new JPanel(); JButton bA=new JButton("Aggiungi"); JButton bM=new JButton("Modifica"); JButton bE=new JButton("Elimina"); bP.add(bA); bP.add(bM); bP.add(bE); p.add(bP, BorderLayout.SOUTH);
        bA.addActionListener(e -> apriDialogLetto(null));
        bM.addActionListener(e -> { int rw=table.getSelectedRow(); if(rw!=-1) apriDialogLetto(controller.getLetti().get(rw)); });
        bE.addActionListener(e -> { int rw=table.getSelectedRow(); if(rw!=-1) { if(JOptionPane.showConfirmDialog(this,"Eliminare?")==JOptionPane.YES_OPTION) { try{controller.eliminaLetto((int)tm.getValueAt(rw,0)); rinfrescaDashboard();}catch(Exception ex){} } } });
        return p;
    }
    private void apriDialogLetto(Letto l) {
        boolean m = l != null; JDialog d = new JDialog(this, m?"Modifica":"Aggiungi", true); d.setLayout(new GridLayout(2,2)); d.setSize(300,100); d.setLocationRelativeTo(this);
        JComboBox<Stanza> cS=new JComboBox<>(); for(Stanza s:controller.getStanze()) {cS.addItem(s); if(m&&l.getStanza()!=null&&s.getIdStanza()==l.getStanza().getIdStanza()) cS.setSelectedItem(s);}
        d.add(new JLabel("Seleziona Stanza:")); d.add(cS);
        JButton bS=new JButton("Salva"); d.add(bS); bS.addActionListener(e->{ try{controller.salvaLetto(m?l.getId_letto():-1,(Stanza)cS.getSelectedItem(),m); d.dispose(); rinfrescaDashboard();}catch(Exception ex){} }); d.setVisible(true);
    }

    // --- TURNI ---
    private JPanel creaPannelloTurni() {
        DefaultTableModel tm = new DefaultTableModel(new String[]{"ID", "Inizio", "Fine", "Medico"}, 0); JTable table = new JTable(tm);
        for (TurnoLavorativo t : controller.getTurni()) tm.addRow(new Object[]{t.getIdTurno(), t.getDataOraInizio(), t.getDataOraFine(), t.getMedico()!=null?t.getMedico().getNome():""});
        JPanel p = new JPanel(new BorderLayout()); p.add(new JScrollPane(table), BorderLayout.CENTER);
        JPanel bP = new JPanel(); JButton bA=new JButton("Aggiungi"); JButton bM=new JButton("Modifica"); JButton bE=new JButton("Elimina"); bP.add(bA); bP.add(bM); bP.add(bE); p.add(bP, BorderLayout.SOUTH);
        bA.addActionListener(e -> apriDialogTurno(null));
        bM.addActionListener(e -> { int rw=table.getSelectedRow(); if(rw!=-1) apriDialogTurno(controller.getTurni().get(rw)); });
        bE.addActionListener(e -> { int rw=table.getSelectedRow(); if(rw!=-1) { if(JOptionPane.showConfirmDialog(this,"Eliminare?")==JOptionPane.YES_OPTION) { try{controller.eliminaTurno((int)tm.getValueAt(rw,0)); rinfrescaDashboard();}catch(Exception ex){} } } });
        return p;
    }
    private void apriDialogTurno(TurnoLavorativo t) {
        boolean m = t != null; JDialog d = new JDialog(this, m?"Modifica":"Aggiungi", true); d.setLayout(new GridLayout(4,2)); d.setSize(400,200); d.setLocationRelativeTo(this);
        JTextField tI=new JTextField(m?t.getDataOraInizio().format(formatter):"2024-01-01 08:00"); JTextField tF=new JTextField(m?t.getDataOraFine().format(formatter):"2024-01-01 16:00");
        JComboBox<Medico> cM=new JComboBox<>(); for(Medico med:controller.getMedici()) {cM.addItem(med); if(m&&t.getMedico()!=null&&med.getIdMedico()==t.getMedico().getIdMedico()) cM.setSelectedItem(med);}
        d.add(new JLabel("Inizio (yyyy-MM-dd HH:mm):")); d.add(tI); d.add(new JLabel("Fine (yyyy-MM-dd HH:mm):")); d.add(tF); d.add(new JLabel("Medico:")); d.add(cM);
        JButton bS=new JButton("Salva"); d.add(bS); bS.addActionListener(e->{ try{controller.salvaTurno(m?t.getIdTurno():-1,LocalDateTime.parse(tI.getText(),formatter),LocalDateTime.parse(tF.getText(),formatter),(Medico)cM.getSelectedItem(),m); d.dispose(); rinfrescaDashboard();}catch(Exception ex){} }); d.setVisible(true);
    }
    private void apriDialogVisita() {
        JDialog d = new JDialog(this, "Aggiungi Visita", true); d.setLayout(new GridLayout(4,2)); d.setSize(400,200); d.setLocationRelativeTo(this);
        JTextField tN = new JTextField();
        JComboBox<Ricovero> cR = new JComboBox<>(); for(Ricovero r : controller.getRicoveri()) cR.addItem(r);
        JComboBox<Medico> cM = new JComboBox<>(); for(Medico m : controller.getMedici()) cM.addItem(m);
        d.add(new JLabel("Nome Visita:")); d.add(tN); d.add(new JLabel("Ricovero:")); d.add(cR); d.add(new JLabel("Medico:")); d.add(cM);
        JButton bS = new JButton("Salva"); d.add(bS);
        bS.addActionListener(e -> {
            try { controller.salvaVisita(tN.getText(), (Ricovero)cR.getSelectedItem(), (Medico)cM.getSelectedItem()); d.dispose(); rinfrescaDashboard(); } catch(Exception ex){}
        }); d.setVisible(true);
    }
    // --- RICOVERI ---
    private JPanel creaPannelloRicoveri() {
        DefaultTableModel tm = new DefaultTableModel(new String[]{"ID", "Paziente", "Letto", "Inizio", "Fine"}, 0); JTable table = new JTable(tm);
        for (Ricovero r : controller.getRicoveri()) tm.addRow(new Object[]{r.getIdRicovero(), r.getPaziente()!=null?r.getPaziente().getCOD_FISCALE():"", r.getLetto()!=null?r.getLetto().getId_letto():"", r.getDataOraInizio(), r.getDataOraFine()});
        JPanel p = new JPanel(new BorderLayout()); p.add(new JScrollPane(table), BorderLayout.CENTER);
        JPanel bP = new JPanel(); JButton bA=new JButton("Aggiungi"); JButton bM=new JButton("Modifica"); JButton bE=new JButton("Elimina"); bP.add(bA); bP.add(bM); bP.add(bE); p.add(bP, BorderLayout.SOUTH);
        bA.addActionListener(e -> apriDialogRicovero(null));
        bM.addActionListener(e -> { int rw=table.getSelectedRow(); if(rw!=-1) apriDialogRicovero(controller.getRicoveri().get(rw)); });
        bE.addActionListener(e -> { int rw=table.getSelectedRow(); if(rw!=-1) { if(JOptionPane.showConfirmDialog(this,"Eliminare?")==JOptionPane.YES_OPTION) { try{controller.eliminaRicovero((int)tm.getValueAt(rw,0)); rinfrescaDashboard();}catch(Exception ex){} } } });
        return p;
    }
    private void apriDialogRicovero(Ricovero r) {
        boolean m = r != null; JDialog d = new JDialog(this, m?"Modifica":"Aggiungi", true); d.setLayout(new GridLayout(5,2)); d.setSize(400,250); d.setLocationRelativeTo(this);
        JComboBox<Paziente> cP=new JComboBox<>(); for(Paziente p:controller.getPazienti()) {cP.addItem(p); if(m&&r.getPaziente()!=null&&p.getCOD_FISCALE().equals(r.getPaziente().getCOD_FISCALE())) cP.setSelectedItem(p);}
        JComboBox<Letto> cL=new JComboBox<>(); for(Letto l:controller.getLetti()) {cL.addItem(l); if(m&&r.getLetto()!=null&&l.getId_letto()==r.getLetto().getId_letto()) cL.setSelectedItem(l);}
        JTextField tI=new JTextField(m&&r.getDataOraInizio()!=null?r.getDataOraInizio().format(formatter):"2024-01-01 08:00"); JTextField tF=new JTextField(m&&r.getDataOraFine()!=null?r.getDataOraFine().format(formatter):"2024-01-10 16:00");
        d.add(new JLabel("Paziente:")); d.add(cP); d.add(new JLabel("Letto:")); d.add(cL); d.add(new JLabel("Inizio:")); d.add(tI); d.add(new JLabel("Fine:")); d.add(tF);
        JButton bS=new JButton("Salva"); d.add(bS); bS.addActionListener(e->{ try{controller.salvaRicovero(m?r.getIdRicovero():-1,LocalDateTime.parse(tI.getText(),formatter),LocalDateTime.parse(tF.getText(),formatter),(Paziente)cP.getSelectedItem(),(Letto)cL.getSelectedItem(),m); d.dispose(); rinfrescaDashboard();}catch(Exception ex){} }); d.setVisible(true);
    }

    // --- INTERVENTI ---
    private JPanel creaPannelloInterventi() {
        DefaultTableModel tm = new DefaultTableModel(new String[]{"ID", "Nome", "Inizio", "Fine", "Visita ID"}, 0);
        JTable table = new JTable(tm);
        Runnable popola = () -> {
            tm.setRowCount(0);
            for (InterventoChirurgico i : controller.getInterventi())
                tm.addRow(new Object[]{i.getIdIntervento(), i.getNomeIntervento(), i.getDataOraInizio(), i.getDataOraFine(), i.getVisita()!=null ? i.getVisita().getIdVisita() : ""});
        };
        popola.run();

        JPanel p = new JPanel(new BorderLayout()); p.add(new JScrollPane(table), BorderLayout.CENTER);
        JPanel bP = new JPanel(); JButton bA=new JButton("Aggiungi"); JButton bM=new JButton("Modifica"); JButton bE=new JButton("Elimina");
        bP.add(bA); bP.add(bM); bP.add(bE); p.add(bP, BorderLayout.SOUTH);

        bA.addActionListener(e -> { apriDialogIntervento(null); popola.run(); });
        bM.addActionListener(e -> { int rw=table.getSelectedRow(); if(rw!=-1) { apriDialogIntervento(controller.getInterventi().get(rw)); popola.run(); } else JOptionPane.showMessageDialog(this, "Seleziona un intervento!"); });
        bE.addActionListener(e -> { int rw=table.getSelectedRow(); if(rw!=-1) { if(JOptionPane.showConfirmDialog(this,"Eliminare?")==JOptionPane.YES_OPTION) { try{controller.eliminaIntervento((int)tm.getValueAt(rw,0)); popola.run();}catch(Exception ex){} } } });
        return p;
    }
    private void apriDialogIntervento(InterventoChirurgico i) {
        boolean m = i != null;
        JDialog d = new JDialog(this, m ? "Modifica" : "Aggiungi", true);
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

        JButton bS = new JButton("Salva");
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
                rinfrescaDashboard(); // Aggiorna la vista
            } catch (DateTimeParseException ex) {
                JOptionPane.showMessageDialog(d, "Formato data errato! Usa yyyy-MM-dd HH:mm");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(d, "Errore salvataggio: " + ex.getMessage());
            }
        });
        d.setVisible(true);
    }


    private JPanel creaPannelloAmministratori() {
        DefaultTableModel tm = new DefaultTableModel(new String[]{"ID", "Nome", "Cognome", "Email"}, 0); JTable table = new JTable(tm);
        for (Amministratore a : controller.getAmministratori()) tm.addRow(new Object[]{a.getId(), a.getNome(), a.getCognome(), a.getEmail()});
        JPanel p = new JPanel(new BorderLayout()); p.add(new JScrollPane(table), BorderLayout.CENTER);
        JPanel bP = new JPanel(); JButton bA=new JButton("Aggiungi"); JButton bM=new JButton("Modifica"); JButton bE=new JButton("Elimina"); bP.add(bA); bP.add(bM); bP.add(bE); p.add(bP, BorderLayout.SOUTH);
        bA.addActionListener(e -> apriDialogAdmin(null));
        bM.addActionListener(e -> { int rw=table.getSelectedRow(); if(rw!=-1) apriDialogAdmin(controller.getAmministratori().get(rw)); });
        bE.addActionListener(e -> { int rw=table.getSelectedRow(); if(rw!=-1) { if(JOptionPane.showConfirmDialog(this,"Eliminare?")==JOptionPane.YES_OPTION) { try{controller.eliminaAmministratore((int)tm.getValueAt(rw,0)); rinfrescaDashboard();}catch(Exception ex){} } } });
        return p;
    }
    private void apriDialogAdmin(Amministratore a) {
        boolean m = a != null; JDialog d = new JDialog(this, m?"Modifica":"Aggiungi", true); d.setLayout(new GridLayout(5,2)); d.setSize(300,200); d.setLocationRelativeTo(this);
        JTextField tN=new JTextField(m?a.getNome():""); JTextField tC=new JTextField(m?a.getCognome():""); JTextField tE=new JTextField(m?a.getEmail():""); JTextField tP=new JTextField(m?a.getPassword():"");
        d.add(new JLabel("Nome:")); d.add(tN); d.add(new JLabel("Cognome:")); d.add(tC); d.add(new JLabel("Email:")); d.add(tE); d.add(new JLabel("Password:")); d.add(tP);
        JButton bS=new JButton("Salva"); d.add(bS); bS.addActionListener(e->{ try{controller.salvaAmministratore(m?a.getId():-1,tN.getText(),tC.getText(),tE.getText(),tP.getText(),m); d.dispose(); rinfrescaDashboard();}catch(Exception ex){} }); d.setVisible(true);
    }


    //  PANNELLI RELAZIONI N:N (GESTISCE e OPERA)


    // GESTISCE (Medico -> Ricovero)
    private JPanel creaPannelloGestisce() {
        JPanel panel = new JPanel(new BorderLayout());
        DefaultTableModel tModel = new DefaultTableModel(new String[]{"ID Medico", "Nome Medico", "ID Ricovero", "Paziente"}, 0); JTable table = new JTable(tModel);

        for (Gestisce g : controller.getGestisce()) {
            Medico m = null; Ricovero r = null;
            for(Medico med : controller.getMedici()) if(med.getIdMedico() == g.getId_medico()) m = med;
            for(Ricovero ric : controller.getRicoveri()) if(ric.getIdRicovero() == g.getId_ricovero()) r = ric;
            String mNome = m != null ? m.getNome() + " " + m.getCognome() : "Sconosciuto";
            String rNome = r != null && r.getPaziente() != null ? r.getPaziente().getCOD_FISCALE() : "Sconosciuto";
            tModel.addRow(new Object[]{g.getId_medico(), mNome, g.getId_ricovero(), rNome});
        }
        panel.add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel topPanel = new JPanel();
        JComboBox<Medico> cmbMedici = new JComboBox<>(); for(Medico m: controller.getMedici()) cmbMedici.addItem(m);
        JComboBox<Ricovero> cmbRicoveri = new JComboBox<>(); for(Ricovero r: controller.getRicoveri()) cmbRicoveri.addItem(r);

        JButton btnAssocia = new JButton("Associa"); JButton btnRimuovi = new JButton("Scollega Selezionato");
        topPanel.add(new JLabel("Medico:")); topPanel.add(cmbMedici); topPanel.add(new JLabel("Ricovero:")); topPanel.add(cmbRicoveri); topPanel.add(btnAssocia);
        panel.add(topPanel, BorderLayout.NORTH); panel.add(btnRimuovi, BorderLayout.SOUTH);

        btnAssocia.addActionListener(e -> {
            Medico m=(Medico)cmbMedici.getSelectedItem(); Ricovero r=(Ricovero)cmbRicoveri.getSelectedItem();
            if(m!=null && r!=null) { try { controller.collegaMedicoRicovero(m.getIdMedico(), r.getIdRicovero()); rinfrescaDashboard(); } catch(Exception ex) {} }
        });
        btnRimuovi.addActionListener(e -> {
            int rw = table.getSelectedRow(); if (rw == -1) return;
            try { controller.scollegaMedicoRicovero((int)tModel.getValueAt(rw,0), (int)tModel.getValueAt(rw,2)); rinfrescaDashboard(); } catch(Exception ex) {}
        });
        return panel;
    }

    // OPERA (Medico -> Intervento Chirurgico)
    private JPanel creaPannelloOpera() {
        JPanel panel = new JPanel(new BorderLayout());
        DefaultTableModel tModel = new DefaultTableModel(new String[]{"ID Medico", "Medico", "ID Intervento", "Intervento", "Ruolo"}, 0); JTable table = new JTable(tModel);

        for (Opera o : controller.getOpera()) {
            Medico m = null; InterventoChirurgico ic = null;
            for(Medico med : controller.getMedici()) if(med.getIdMedico() == o.getId_medico()) m = med;
            for(InterventoChirurgico i : controller.getInterventi()) if(i.getIdIntervento() == o.getId_intervento()) ic = i;
            String mNome = m != null ? m.getNome() + " " + m.getCognome() : "N/D"; String iNome = ic != null ? ic.getNomeIntervento() : "N/D";
            tModel.addRow(new Object[]{o.getId_medico(), mNome, o.getId_intervento(), iNome, o.getRuolo()});
        }
        panel.add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel topPanel = new JPanel();
        JComboBox<Medico> cmbMedici = new JComboBox<>(); for(Medico m: controller.getMedici()) cmbMedici.addItem(m);
        JComboBox<InterventoChirurgico> cmbInt = new JComboBox<>(); for(InterventoChirurgico i: controller.getInterventi()) cmbInt.addItem(i);
        JTextField txtRuolo = new JTextField(10);

        JButton btnAssocia = new JButton("Associa"); JButton btnRimuovi = new JButton("Scollega Selezionato");
        topPanel.add(new JLabel("Medico:")); topPanel.add(cmbMedici); topPanel.add(new JLabel("Intervento:")); topPanel.add(cmbInt); topPanel.add(new JLabel("Ruolo:")); topPanel.add(txtRuolo); topPanel.add(btnAssocia);
        panel.add(topPanel, BorderLayout.NORTH); panel.add(btnRimuovi, BorderLayout.SOUTH);

        btnAssocia.addActionListener(e -> {
            Medico m=(Medico)cmbMedici.getSelectedItem(); InterventoChirurgico i=(InterventoChirurgico)cmbInt.getSelectedItem();
            if(m!=null && i!=null) { try { controller.collegaMedicoIntervento(m.getIdMedico(), i.getIdIntervento(), txtRuolo.getText()); txtRuolo.setText(""); rinfrescaDashboard(); } catch(Exception ex) {} }
        });
        btnRimuovi.addActionListener(e -> {
            int rw = table.getSelectedRow(); if (rw == -1) return;
            try { controller.scollegaMedicoIntervento((int)tModel.getValueAt(rw,0), (int)tModel.getValueAt(rw,2)); rinfrescaDashboard(); } catch(Exception ex) {}
        });
        return panel;
    }

    public static void main(String[] args) {
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } catch (Exception e) {}
        SwingUtilities.invokeLater(() -> new MainGUI().setVisible(true));
    }

    private JPanel creaPannelloVisiteMedico() {
        JPanel p = new JPanel(new BorderLayout());
        DefaultTableModel tm = new DefaultTableModel(new String[]{"ID", "Nome Visita", "ID Ricovero"}, 0);
        JTable table = new JTable(tm);


        for(Visita v : controller.getVisite()) {
            tm.addRow(new Object[]{
                    v.getIdVisita(),
                    v.getNomeVisita(),
                    v.getRicovero() != null ? v.getRicovero().getIdRicovero() : "N/D"
            });
        }
        p.add(new JScrollPane(table), BorderLayout.CENTER);
        return p;
    }
}
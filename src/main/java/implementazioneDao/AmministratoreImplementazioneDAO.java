package implementazioneDao;

import dao.DAO;
import database_connection.ConnessioneDatabase;
import exceptions.BadArgsException;
import model.*;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AmministratoreImplementazioneDAO implements DAO {
    private final Connection connection;

    // --- IDENTITY MAPS (Garantiscono l'Unicità degli Oggetti in Memoria) ---
    private final Map<Integer, Amministratore> mappaAdmin = new HashMap<>();
    private final Map<Integer, Reparto> mappaReparti = new HashMap<>();
    private final Map<String, Paziente> mappaPazienti = new HashMap<>();
    private final Map<Integer, Medico> mappaMedici = new HashMap<>();
    private final Map<Integer, Stanza> mappaStanze = new HashMap<>();
    private final Map<Integer, Letto> mappaLetti = new HashMap<>();
    private final Map<Integer, Ricovero> mappaRicoveri = new HashMap<>();
    private final Map<Integer, TurnoLavorativo> mappaTurni = new HashMap<>();

    // --- LISTE CACHE PER IL CONTROLLER ---
    private List<Amministratore> amministratoriCache = new ArrayList<>();
    private List<Reparto> repartiCache = new ArrayList<>();
    private List<Paziente> pazientiCache = new ArrayList<>();
    private List<Medico> mediciCache = new ArrayList<>();
    private List<TurnoLavorativo> turniCache = new ArrayList<>();
    private List<Stanza> stanzeCache = new ArrayList<>();
    private List<Letto> lettiCache = new ArrayList<>();
    private List<Ricovero> ricoveriCache = new ArrayList<>();
    private List<Visita> visiteCache = new ArrayList<>();

    public AmministratoreImplementazioneDAO() {
        connection = ConnessioneDatabase.getConnection();
    }

    @Override
    public void istanziaMemoriaLocale(int id) throws SQLException {
        System.out.println("DAO: Avvio caricamento topologico per garantire l'unicità degli oggetti...");

        // 1. Svuotiamo le mappe e le liste
        svuotaMemoria();

        // 2. Caricamento sequenziale: Si parte dalle entità indipendenti per arrivare a quelle dipendenti
        loadReparti();
        loadPazienti();
        loadAmministratori();

        loadStanze();    // Dipende da Reparto
        loadLetti();     // Dipende da Stanza
        loadMedici();    // Dipende da Reparto

        loadRicoveri();  // Dipende da Paziente e Letto
        loadTurni();     // Dipende da Medico
        loadVisite();    // Collega Ricovero e Medico

        // 3. Trasferiamo gli oggetti unici nelle liste per il Controller
        repartiCache = new ArrayList<>(mappaReparti.values());
        pazientiCache = new ArrayList<>(mappaPazienti.values());
        amministratoriCache = new ArrayList<>(mappaAdmin.values());
        stanzeCache = new ArrayList<>(mappaStanze.values());
        lettiCache = new ArrayList<>(mappaLetti.values());
        mediciCache = new ArrayList<>(mappaMedici.values());
        ricoveriCache = new ArrayList<>(mappaRicoveri.values());
        turniCache = new ArrayList<>(mappaTurni.values());

        System.out.println("DAO: Sincronizzazione e Doppia Associazione completate con successo!");
    }

    private void svuotaMemoria() {
        mappaAdmin.clear(); mappaReparti.clear(); mappaPazienti.clear();
        mappaMedici.clear(); mappaStanze.clear(); mappaLetti.clear();
        mappaRicoveri.clear(); mappaTurni.clear();
        visiteCache.clear();
    }

    // =====================================================================
    // LOGICA DI CARICAMENTO E DOPPIA ASSOCIAZIONE
    // =====================================================================

    private void loadReparti() throws SQLException {
        String sql = "SELECT * FROM Reparto";
        try (PreparedStatement ps = connection.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                try {
                    Reparto r = new Reparto(rs.getString("nome_reparto"), rs.getInt("id_reparto"));
                    mappaReparti.put(r.getId(), r);
                } catch (BadArgsException e) { System.err.println("Errore Reparto: " + e.getMessage()); }
            }
        }
    }

    private void loadPazienti() throws SQLException {
        String sql = "SELECT * FROM Paziente";
        try (PreparedStatement ps = connection.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                try {
                    Paziente p = new Paziente(rs.getString("nome"), rs.getString("cognome"), rs.getString("cod_fiscale"));
                    mappaPazienti.put(p.getCOD_FISCALE(), p);
                } catch (BadArgsException e) { System.err.println("Errore Paziente: " + e.getMessage()); }
            }
        }
    }

    private void loadAmministratori() throws SQLException {
        String sql = "SELECT * FROM Amministratore";
        try (PreparedStatement ps = connection.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                try {
                    Amministratore a = new Amministratore(rs.getString("nome"), rs.getString("cognome"), rs.getString("email"), rs.getString("password"));
                    a.setId(rs.getInt("id_amministratore"));
                    mappaAdmin.put(a.getId(), a);
                } catch (BadArgsException e) {}
            }
        }
    }

    private void loadStanze() throws SQLException {
        String sql = "SELECT * FROM Stanza";
        try (PreparedStatement ps = connection.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                try {
                    int idReparto = rs.getInt("id_reparto");
                    Reparto r = mappaReparti.get(idReparto);

                    Stanza s = new Stanza(r); // Assumo che Stanza abbia questo costruttore

                    // Vincolo Bidirezionale
                    if (r != null) { r.aggiungiStanza(s); }

                    mappaStanze.put(rs.getInt("id_stanza"), s); // Salvo l'ID se presente nel DB
                } catch (Exception e) {}
            }
        }
    }

    private void loadLetti() throws SQLException {
        String sql = "SELECT * FROM Letto";
        try (PreparedStatement ps = connection.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                try {
                    /* Scommenta e adegua in base ai tuoi costruttori
                    int idStanza = rs.getInt("id_stanza");
                    Stanza s = mappaStanze.get(idStanza);
                    Letto l = new Letto(rs.getString("cod_letto"), s);

                    // Vincolo Bidirezionale
                    if(s != null) { s.aggiungiLetto(l); }
                    mappaLetti.put(rs.getInt("id_letto"), l);
                    */
                } catch (Exception e) {}
            }
        }
    }

    private void loadMedici() throws SQLException {
        String sql = "SELECT * FROM Medico";
        try (PreparedStatement ps = connection.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                try {
                    Reparto r = mappaReparti.get(rs.getInt("id_reparto"));

                    Medico m = new Medico(
                            rs.getString("nome"), rs.getString("cognome"),
                            rs.getString("email"), rs.getString("password"),
                            rs.getString("tipo_medico"), r
                    );
                    m.setIdMedico(rs.getInt("id_medico"));

                    // Il vincolo bidirezionale (r.aggiungiMedico(m)) è già gestito nel costruttore di Medico!
                    mappaMedici.put(m.getIdMedico(), m);
                } catch (BadArgsException e) { System.err.println("Errore Medico: " + e.getMessage()); }
            }
        }
    }

    private void loadRicoveri() throws SQLException {
        String sql = "SELECT * FROM Ricovero";
        try (PreparedStatement ps = connection.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                try {
                    int idRicovero = rs.getInt("id_ricovero");
                    Ricovero r = new Ricovero(idRicovero);

                    // Peschiamo ESATTAMENTE le istanze uniche dalle mappe
                    Paziente p = mappaPazienti.get(rs.getString("cod_fiscale_paziente"));
                    Letto l = mappaLetti.get(rs.getInt("id_letto"));

                    r.setDataOraInizio(rs.getObject("data_ora_inizio", LocalDateTime.class));
                    r.setDataOraFine(rs.getObject("data_ora_fine", LocalDateTime.class));

                    // ==========================================
                    // RISOLUZIONE DELLA DOPPIA ASSOCIAZIONE
                    // ==========================================
                    if (p != null) {
                        r.setPaziente(p);
                        p.aggiungiRicovero(r); // Aggiorna il paziente
                    }
                    if (l != null) {
                        r.setLetto(l);
                        // l.aggiungiRicovero(r); // Scommenta se Letto ha questo metodo
                    }

                    mappaRicoveri.put(idRicovero, r);
                } catch (Exception e) { System.err.println("Errore Ricovero: " + e.getMessage()); }
            }
        }
    }

    private void loadVisite() throws SQLException {
        String sql = "SELECT * FROM Visita";
        try (PreparedStatement ps = connection.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                try {
                    String nomeVisita = rs.getString("nome_visita");
                    Ricovero r = mappaRicoveri.get(rs.getInt("id_ricovero"));
                    Medico m = mappaMedici.get(rs.getInt("id_medico"));

                    Visita v = new Visita(nomeVisita, r, m);

                    // ==========================================
                    // VINCOLO BIDIREZIONALE MEDICO-RICOVERO
                    // ==========================================
                    if (r != null && m != null) {
                        // aggiungiMedico dentro Ricovero richiama in automatico medico.aggiungiRicovero(this)
                        r.aggiungiMedico(m);
                    }

                    visiteCache.add(v);
                } catch (BadArgsException e) {}
            }
        }
    }

    private void loadTurni() throws SQLException {
        String sql = "SELECT * FROM Turno_Lavorativo";
        try (PreparedStatement ps = connection.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                try {
                    TurnoLavorativo t = new TurnoLavorativo(
                            rs.getObject("data_ora_inizio", LocalDateTime.class),
                            rs.getObject("data_ora_fine", LocalDateTime.class)
                    );

                    Medico m = mappaMedici.get(rs.getInt("id_medico"));
                    if (m != null) {
                        m.aggiungiTurnoLavorativo(t);
                    }

                    mappaTurni.put(rs.getInt("id_turno"), t);
                } catch (BadArgsException e) {}
            }
        }
    }

    // =====================================================================
    // GETTER DELL'INTERFACCIA DAO (RESTITUISCONO LA CACHE AL CONTROLLER)
    // =====================================================================

    @Override public Boolean verificaCredenziali(String email, String password) throws SQLException {
        String sql = "SELECT 1 FROM Amministratore WHERE email = ? AND password = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, email); ps.setString(2, password);
            try (ResultSet rs = ps.executeQuery()) { return rs.next(); }
        }
    }

    @Override public List<Medico> getMedici(int id) { return this.mediciCache; }
    @Override public List<Amministratore> getAmministratori(int id) { return this.amministratoriCache; }
    @Override public List<Letto> getLetti(int id) { return this.lettiCache; }
    @Override public List<Stanza> getStanze(int id) { return this.stanzeCache; }
    @Override public List<Stanza> getStanzePerReparto(int id, Reparto reparto) {
        Reparto r = mappaReparti.get(reparto.getId());
        return (r != null) ? r.getStanze() : new ArrayList<>();
    }
    @Override public List<Paziente> getPazienti(int id) { return this.pazientiCache; }
    @Override public List<TurnoLavorativo> getTurniLavorativi(int id) { return this.turniCache; }
    @Override public List<Reparto> getReparti(int id) { return this.repartiCache; }
    @Override public List<Visita> getVisite(int id) { return this.visiteCache; }
    public List<Ricovero> getRicoveri(int id) { return this.ricoveriCache; }
}
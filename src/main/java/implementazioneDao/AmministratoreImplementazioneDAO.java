package implementazioneDao;

import dao.DAO;
import database_connection.ConnessioneDatabase;
import exceptions.BadArgsException;
import model.*;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class AmministratoreImplementazioneDAO implements DAO {
    private final Connection connection;

    // --- MEMORIA LOCALE ---
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
        System.out.println("Amministratore: Inizio sincronizzazione memoria locale...");

        // Svuotamento e riempimento forzato a cascata
        this.repartiCache = fetchRepartiComplessi();
        this.pazientiCache = fetchPazientiComplessi();
        this.mediciCache = fetchMediciComplessi();

        // Popolamento liste globali piatte
        this.amministratoriCache = fetchAmministratoriGlobali();
        this.turniCache = fetchTurniGlobali();
        this.stanzeCache = fetchStanzeGlobali();
        this.lettiCache = fetchLettiGlobali();
        this.ricoveriCache = fetchRicoveriGlobali();
        this.visiteCache = fetchVisiteGlobali();

        System.out.println("Amministratore: Inizializzazione dati globali completata.");
    }

    @Override
    public Boolean verificaCredenziali(String email, String password) throws SQLException {
        String sql = "SELECT 1 FROM Amministratore WHERE email = ? AND password = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, email);
            ps.setString(2, password);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    // =====================================================================
    // LOGICA DI RIEMPIMENTO FORZATO (EAGER LOADING TRAMITE FK)
    // =====================================================================

    private List<Reparto> fetchRepartiComplessi() throws SQLException {
        List<Reparto> lista = new ArrayList<>();
        String sql = "SELECT * FROM Reparto";
        try (PreparedStatement ps = connection.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                try {
                    Reparto r = new Reparto(rs.getString("nome_reparto"), rs.getInt("id_reparto"));
                    r.setStanze(fetchStanzePerRepartoFK(r.getId()));
                    r.setMedici(fetchMediciPerRepartoFK(r.getId()));
                    lista.add(r);
                } catch (BadArgsException e) {
                    System.err.println("Errore Reparto: " + e.getMessage());
                }
            }
        }
        return lista;
    }

    private List<Paziente> fetchPazientiComplessi() throws SQLException {
        List<Paziente> lista = new ArrayList<>();
        String sql = "SELECT * FROM Paziente";
        try (PreparedStatement ps = connection.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                try {
                    Paziente p = new Paziente(rs.getString("nome"), rs.getString("cognome"), rs.getString("cod_fiscale"));
                    p.setRicoveri(fetchRicoveriPerPazienteFK(p.getCOD_FISCALE()));
                    lista.add(p);
                } catch (BadArgsException e) {
                    System.err.println("Errore Paziente: " + e.getMessage());
                }
            }
        }
        return lista;
    }

    private List<Medico> fetchMediciComplessi() throws SQLException {
        List<Medico> lista = new ArrayList<>();
        String sql = "SELECT * FROM Medico";
        try (PreparedStatement ps = connection.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                try {
                    Medico m = new Medico(rs.getInt("id_medico"));
                    m.setTurniLavorativi(fetchTurniPerMedicoFK(m.getIdMedico()));
                    lista.add(m);
                } catch (BadArgsException e) {
                    System.err.println("Errore Guscio Medico: " + e.getMessage());
                }
            }
        }
        return lista;
    }

    // =====================================================================
    // SOTTO-QUERY TRAMITE FOREIGN KEY (USATE PER RIEMPIRE GLI ARRAY)
    // =====================================================================

    private List<Stanza> fetchStanzePerRepartoFK(int idReparto) throws SQLException {
        List<Stanza> stanze = new ArrayList<>();
        String sql = "SELECT * FROM Stanza WHERE id_reparto = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, idReparto);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    try {
                        stanze.add(new Stanza(new Reparto(idReparto)));
                    } catch (BadArgsException e) {
                        System.err.println("Errore Guscio Stanza: " + e.getMessage());
                    }
                }
            }
        }
        return stanze;
    }

    private List<Medico> fetchMediciPerRepartoFK(int idReparto) throws SQLException {
        List<Medico> medici = new ArrayList<>();
        String sql = "SELECT * FROM Medico WHERE id_reparto = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, idReparto);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    try {
                        Medico m = new Medico(rs.getInt("id_medico"));
                        medici.add(m);
                    } catch (BadArgsException e) {
                        System.err.println("Errore Guscio Medico in Reparto: " + e.getMessage());
                    }
                }
            }
        }
        return medici;
    }

    private List<Ricovero> fetchRicoveriPerPazienteFK(String codFiscale) throws SQLException {
        List<Ricovero> ricoveri = new ArrayList<>();
        String sql = "SELECT * FROM Ricovero WHERE cod_fiscale_paziente = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, codFiscale);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    try {
                        ricoveri.add(new Ricovero(rs.getInt("id_ricovero")));
                    } catch (Exception e) {
                        System.err.println("Errore Guscio Ricovero in Paziente: " + e.getMessage());
                    }
                }
            }
        }
        return ricoveri;
    }

    private List<TurnoLavorativo> fetchTurniPerMedicoFK(int idMedico) throws SQLException {
        List<TurnoLavorativo> turni = new ArrayList<>();
        String sql = "SELECT * FROM Turno_Lavorativo WHERE id_medico = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, idMedico);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    try {
                        turni.add(new TurnoLavorativo(
                                rs.getObject("data_ora_inizio", LocalDateTime.class),
                                rs.getObject("data_ora_fine", LocalDateTime.class)
                        ));
                    } catch (BadArgsException e) {
                        System.err.println("Errore Turno: " + e.getMessage());
                    }
                }
            }
        }
        return turni;
    }

    // =====================================================================
    // METODI PER CARICAMENTI GLOBALI (SENZA FK SPECIFICA)
    // =====================================================================

    private List<Amministratore> fetchAmministratoriGlobali() throws SQLException {
        List<Amministratore> lista = new ArrayList<>();
        String sql = "SELECT * FROM Amministratore";
        try (PreparedStatement ps = connection.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                try {
                    Amministratore a = new Amministratore(rs.getString("nome"), rs.getString("cognome"), rs.getString("email"), rs.getString("password"));
                    a.setId(rs.getInt("id_amministratore"));
                    lista.add(a);
                } catch (BadArgsException e) {
                    System.err.println("Errore Amministratore: " + e.getMessage());
                }
            }
        }
        return lista;
    }

    private List<Stanza> fetchStanzeGlobali() throws SQLException {
        List<Stanza> lista = new ArrayList<>();
        String sql = "SELECT * FROM Stanza";
        try (PreparedStatement ps = connection.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                try {
                    lista.add(new Stanza(new Reparto(rs.getInt("id_reparto"))));
                } catch (BadArgsException e) {
                    System.err.println("Errore Stanza Globale: " + e.getMessage());
                }
            }
        }
        return lista;
    }

    // FIX WARNING: Rimosso throws SQLException e lista ridondante
    private List<Letto> fetchLettiGlobali() {
        /* String sql = "SELECT * FROM Letto";
        try (PreparedStatement ps = connection.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                try {
                    lista.add(new Letto(rs.getString("cod_letto")));
                } catch (Exception e) {}
            }
        } */
        return new ArrayList<>();
    }

    private List<Ricovero> fetchRicoveriGlobali() throws SQLException {
        List<Ricovero> lista = new ArrayList<>();
        String sql = "SELECT * FROM Ricovero";
        try (PreparedStatement ps = connection.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                try {
                    lista.add(new Ricovero(rs.getInt("id_ricovero")));
                } catch (Exception e) {
                    System.err.println("Errore Ricovero Globale: " + e.getMessage());
                }
            }
        }
        return lista;
    }

    // FIX WARNING: Rimosso throws SQLException e lista ridondante
    private List<Visita> fetchVisiteGlobali() {
        /* String sql = "SELECT * FROM Visita";
        try (PreparedStatement ps = connection.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                try {
                    lista.add(new Visita(rs.getString("nome_visita")));
                } catch (BadArgsException e) {}
            }
        } */
        return new ArrayList<>();
    }

    private List<TurnoLavorativo> fetchTurniGlobali() throws SQLException {
        List<TurnoLavorativo> lista = new ArrayList<>();
        String sql = "SELECT * FROM Turno_Lavorativo";
        try (PreparedStatement ps = connection.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                try {
                    lista.add(new TurnoLavorativo(rs.getObject("data_ora_inizio", LocalDateTime.class), rs.getObject("data_ora_fine", LocalDateTime.class)));
                } catch (BadArgsException e) {
                    System.err.println("Errore Turno Globale: " + e.getMessage());
                }
            }
        }
        return lista;
    }

    // =====================================================================
    // GETTER DELL'INTERFACCIA DAO E LOCALI (RESTITUISCONO LA CACHE)
    // =====================================================================

    @Override public List<Medico> getMedici(int id) { return this.mediciCache; }
    @Override public List<Amministratore> getAmministratori(int id) { return this.amministratoriCache; }
    @Override public List<Letto> getLetti(int id) { return this.lettiCache; }
    @Override public List<Stanza> getStanze(int id) { return this.stanzeCache; }

    @Override public List<Stanza> getStanzePerReparto(int id, Reparto reparto) {
        List<Stanza> stanzeReparto = new ArrayList<>();
        for (Reparto r : repartiCache) {
            if (r.getId() == reparto.getId()) return r.getStanze();
        }
        return stanzeReparto;
    }

    @Override public List<Paziente> getPazienti(int id) { return this.pazientiCache; }
    @Override public List<TurnoLavorativo> getTurniLavorativi(int id) { return this.turniCache; }
    @Override public List<Reparto> getReparti(int id) { return this.repartiCache; }
    @Override public List<Visita> getVisite(int id) { return this.visiteCache; }

    public List<Ricovero> getRicoveri(int id) { return this.ricoveriCache; }
}
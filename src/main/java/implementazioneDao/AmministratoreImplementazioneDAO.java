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

    public AmministratoreImplementazioneDAO() {
        connection = ConnessioneDatabase.getConnection();
    }

    @Override
    public void istanziaMemoriaLocale(int id) throws SQLException {
        // Il DAO ora è "stateless" (senza stato). Non salva dati al suo interno.
        System.out.println("Amministratore DAO: Connessione pronta. Attendo gli ordini dal Controller.");
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
    // METODI GET PRINCIPALI (Eseguono la query e restituiscono le liste)
    // =====================================================================

    @Override
    public List<Reparto> getReparti(int id) throws SQLException {
        List<Reparto> lista = new ArrayList<>();
        String sql = "SELECT * FROM Reparto";
        try (PreparedStatement ps = connection.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                try {
                    Reparto r = new Reparto(rs.getString("nome_reparto"), rs.getInt("id_reparto"));
                    // FORZATURA A CASCATA: Il DAO riempie l'oggetto prima di darlo al Controller
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

    @Override
    public List<Paziente> getPazienti(int id) throws SQLException {
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

    @Override
    public List<Medico> getMedici(int id) throws SQLException {
        List<Medico> lista = new ArrayList<>();
        String sql = "SELECT * FROM Medico";
        try (PreparedStatement ps = connection.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                try {
                    Medico m = new Medico(rs.getInt("id_medico"));
                    m.setTurniLavorativi(fetchTurniPerMedicoFK(m.getIdMedico()));
                    lista.add(m);
                } catch (BadArgsException e) {
                    System.err.println("Errore Medico: " + e.getMessage());
                }
            }
        }
        return lista;
    }

    @Override
    public List<Amministratore> getAmministratori(int id) throws SQLException {
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

    @Override
    public List<TurnoLavorativo> getTurniLavorativi(int id) throws SQLException {
        List<TurnoLavorativo> lista = new ArrayList<>();
        String sql = "SELECT * FROM Turno_Lavorativo";
        try (PreparedStatement ps = connection.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                try {
                    lista.add(new TurnoLavorativo(rs.getObject("data_ora_inizio", LocalDateTime.class), rs.getObject("data_ora_fine", LocalDateTime.class)));
                } catch (BadArgsException e) {
                    System.err.println("Errore Turno: " + e.getMessage());
                }
            }
        }
        return lista;
    }

    @Override
    public List<Stanza> getStanze(int id) throws SQLException {
        List<Stanza> lista = new ArrayList<>();
        String sql = "SELECT * FROM Stanza";
        try (PreparedStatement ps = connection.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                try { lista.add(new Stanza(new Reparto(rs.getInt("id_reparto")))); }
                catch (BadArgsException e) {}
            }
        }
        return lista;
    }

    @Override public List<Stanza> getStanzePerReparto(int id, Reparto reparto) throws SQLException {
        return fetchStanzePerRepartoFK(reparto.getId());
    }

    // Metodi implementati base per rispettare l'interfaccia (senza SQL attivo per ora)
    @Override public List<Letto> getLetti(int id) { return new ArrayList<>(); }
    @Override public List<Visita> getVisite(int id) { return new ArrayList<>(); }
    public List<Ricovero> getRicoveri(int id) throws SQLException {
        List<Ricovero> lista = new ArrayList<>();
        String sql = "SELECT * FROM Ricovero";
        try (PreparedStatement ps = connection.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                try { lista.add(new Ricovero(rs.getInt("id_ricovero"))); }
                catch (Exception e) {}
            }
        }
        return lista;
    }

    // =====================================================================
    // SOTTO-QUERY PRIVATE (Usate per l'Eager Loading)
    // =====================================================================

    private List<Stanza> fetchStanzePerRepartoFK(int idReparto) throws SQLException {
        List<Stanza> stanze = new ArrayList<>();
        String sql = "SELECT * FROM Stanza WHERE id_reparto = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, idReparto);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    try { stanze.add(new Stanza(new Reparto(idReparto))); }
                    catch (BadArgsException e) {}
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
                    try { medici.add(new Medico(rs.getInt("id_medico"))); }
                    catch (BadArgsException e) {}
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
                    try { ricoveri.add(new Ricovero(rs.getInt("id_ricovero"))); }
                    catch (Exception e) {}
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
                    try { turni.add(new TurnoLavorativo(rs.getObject("data_ora_inizio", LocalDateTime.class), rs.getObject("data_ora_fine", LocalDateTime.class))); }
                    catch (BadArgsException e) {}
                }
            }
        }
        return turni;
    }
}
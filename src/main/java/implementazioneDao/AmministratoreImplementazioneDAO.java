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

    public AmministratoreImplementazioneDAO() throws SQLException {
        connection = ConnessioneDatabase.getConnection();
    }

    @Override
    public void istanziaDB(int id) throws SQLException {
        System.out.println("Amministratore: inizializzazione dati globali completata.");
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

    @Override
    public List<Medico> getMedici(int id) throws SQLException {
        List<Medico> lista = new ArrayList<>();
        String sql = "SELECT * FROM Medico";
        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                try {
                    Medico m = new Medico(rs.getString("nome"), rs.getString("cognome"),
                            rs.getString("email"), rs.getString("password"),
                            rs.getString("tipo_medico"), null);
                    m.setIdMedico(rs.getInt("id_medico"));
                    lista.add(m);
                } catch (BadArgsException e) {
                    System.err.println("Errore caricamento Medico: " + e.getMessage());
                }
            }
        }
        return lista;
    }

    @Override
    public List<Amministratore> getAmministratori(int id) throws SQLException {
        return new ArrayList<>();
    }

    @Override
    public List<Letto> getLetti(int id) throws SQLException {
        return new ArrayList<>();
    }

    @Override
    public List<Stanza> getStanze(int id) throws SQLException {
        return new ArrayList<>();
    }

    @Override
    public List<Stanza> getStanzePerReparto(int id, Reparto reparto) throws SQLException {
        return new ArrayList<>();
    }

    @Override
    public List<Paziente> getPazienti(int id) throws SQLException {
        List<Paziente> lista = new ArrayList<>();
        String sql = "SELECT * FROM Paziente";
        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                try {
                    lista.add(new Paziente(rs.getString("nome"), rs.getString("cognome"), rs.getString("cod_fiscale")));
                } catch (BadArgsException e) {
                    System.err.println("Errore caricamento Paziente: " + e.getMessage());
                }
            }
        }
        return lista;
    }

    @Override
    public List<TurnoLavorativo> getTurniLavorativi(int id) throws SQLException {
        List<TurnoLavorativo> lista = new ArrayList<>();
        String sql = "SELECT * FROM Turno_Lavorativo";
        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                try {
                    lista.add(new TurnoLavorativo(rs.getObject("data_ora_inizio", LocalDateTime.class),
                            rs.getObject("data_ora_fine", LocalDateTime.class)));
                } catch (BadArgsException e) {
                    System.err.println("Errore caricamento Turno: " + e.getMessage());
                }
            }
        }
        return lista;
    }

    @Override
    public List<Reparto> getReparti(int id) throws SQLException {
        List<Reparto> lista = new ArrayList<>();
        String sql = "SELECT * FROM Reparto";
        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(new Reparto(rs.getString("nome_reparto"), rs.getInt("id_reparto")));
            }
        }
        return lista;
    }

    @Override
    public List<Visita> getVisite(int id) throws SQLException {
        return new ArrayList<>();
    }
}
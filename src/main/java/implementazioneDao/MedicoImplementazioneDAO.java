package implementazioneDao;

import dao.DAO;
import database_connection.ConnessioneDatabase;
import exceptions.BadArgsException;
import model.*;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class MedicoImplementazioneDAO implements DAO {
    private final Connection connection;

    public MedicoImplementazioneDAO() throws SQLException {
        connection = ConnessioneDatabase.getConnection();
    }

    @Override
    public void istanziaDB(int id) throws SQLException {
        System.out.println("Medico: connessione stabilita, pronto per scaricare dati filtrati.");
    }

    @Override
    public Boolean verificaCredenziali(String email, String password) throws SQLException {
        String sql = "SELECT 1 FROM Medico WHERE email = ? AND password = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, email);
            ps.setString(2, password);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    @Override
    public List<Visita> getVisite(int ID_Medico) throws SQLException {
        List<Visita> visite = new ArrayList<>();
        String sql = "SELECT * FROM Visita WHERE id_medico = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, ID_Medico);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    try {
                        visite.add(new Visita(rs.getString("nome_visita"),
                                new Ricovero(rs.getInt("id_ricovero")),
                                new Medico(ID_Medico)));
                    } catch (BadArgsException e) {
                        System.err.println("Errore creazione Visita: " + e.getMessage());
                    }
                }
            }
        }
        return visite;
    }

    @Override public List<Medico> getMedici(int id) { return new ArrayList<>(); }
    @Override public List<Amministratore> getAmministratori(int id) { return new ArrayList<>(); }
    @Override public List<Letto> getLetti(int id) { return new ArrayList<>(); }
    @Override public List<Stanza> getStanze(int id) { return new ArrayList<>(); }
    @Override public List<Stanza> getStanzePerReparto(int id, Reparto r) { return new ArrayList<>(); }
    @Override public List<Paziente> getPazienti(int id) { return new ArrayList<>(); }
    @Override public List<TurnoLavorativo> getTurniLavorativi(int id) { return new ArrayList<>(); }
    @Override public List<Reparto> getReparti(int id) { return new ArrayList<>(); }
}
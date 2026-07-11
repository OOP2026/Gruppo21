package implementazioneDao;

import dao.DAO;
import database_connection.ConnessioneDatabase;
import model.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class MedicoImplementazioneDAO implements DAO {
    private final Connection connection;

    public MedicoImplementazioneDAO() throws SQLException {
        connection = ConnessioneDatabase.getConnection();
    }

    @Override
    public Boolean verificaCredenziali(String email, String password) throws SQLException {
        String sql = "SELECT 1 FROM medico WHERE email = ? AND password = ?";

        // try-with-resources risolve i bug di gestione risorse segnalati da SonarQube
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, email);
            ps.setString(2, password);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    @Override
    public List<Medico> getMedici(int ID_Medico) throws SQLException {
        return List.of();
    }

    @Override
    public List<Amministratore> getAmministratori(int ID_Medico) throws SQLException {
        return List.of();
    }

    @Override
    public List<Letto> getLetti(int ID_Medico) throws SQLException {
        return List.of();
    }

    @Override
    public List<Stanza> getStanze(int ID_Medico) throws SQLException {
        return List.of();
    }

    @Override
    public List<Stanza> getStanzePerReparto(int ID_Medico, Reparto reparto) throws SQLException {
        return List.of();
    }

    @Override
    public List<Paziente> getPazienti(int ID_Medico) throws SQLException {
        return List.of();
    }

    @Override
    public List<TurnoLavorativo> getTurniLavorativi(int ID_Medico) throws SQLException {
        return List.of();
    }

    @Override
    public List<Reparto> getReparti(int ID_Medico) throws SQLException {
        return List.of();
    }

    @Override
    public List<Visita> getVisite(int ID_Medico) throws SQLException {
        return List.of();
    }
}
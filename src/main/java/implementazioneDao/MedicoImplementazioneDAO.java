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

    public MedicoImplementazioneDAO() {
        connection = ConnessioneDatabase.getConnection();
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
    public List<Medico> getMedici(int idMedico) throws SQLException {
        List<Medico> lista = new ArrayList<>();
        String sql = "SELECT * FROM Medico WHERE id_medico = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, idMedico);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    try {
                        Reparto guscioReparto = new Reparto(rs.getInt("id_reparto"));
                        Medico m = new Medico(rs.getString("nome"), rs.getString("cognome"),
                                rs.getString("email"), rs.getString("password"),
                                rs.getString("tipo_medico"), guscioReparto);
                        m.setIdMedico(rs.getInt("id_medico"));
                        lista.add(m);
                    } catch (BadArgsException e) { System.err.println(e.getMessage()); }
                }
            }
        }
        return lista;
    }

    @Override
    public List<Visita> getVisite(int idMedico) throws SQLException {
        List<Visita> lista = new ArrayList<>();
        String sql = "SELECT * FROM Visita WHERE id_medico = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, idMedico);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    try {
                        lista.add(new Visita(rs.getString("nome_visita"),
                                new Ricovero(rs.getInt("id_ricovero")),
                                new Medico(idMedico)));
                    } catch (BadArgsException e) { System.err.println(e.getMessage()); }
                }
            }
        }
        return lista;
    }

    @Override
    public List<TurnoLavorativo> getTurniLavorativi(int idMedico) throws SQLException {
        List<TurnoLavorativo> lista = new ArrayList<>();
        String sql = "SELECT * FROM Turno_Lavorativo WHERE id_medico = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, idMedico);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    try {
                        TurnoLavorativo t = new TurnoLavorativo(
                                rs.getObject("data_ora_inizio", LocalDateTime.class),
                                rs.getObject("data_ora_fine", LocalDateTime.class)
                        );
                        t.setMedico(new Medico(idMedico));
                        lista.add(t);
                    } catch (BadArgsException e) { System.err.println(e.getMessage()); }
                }
            }
        }
        return lista;
    }

    @Override
    public List<Gestisce> getCollegamentiGestisce() throws SQLException {
        List<Gestisce> collegamenti = new ArrayList<>();
        String sql = "SELECT id_medico, id_ricovero FROM Gestisce";
        try (PreparedStatement ps = connection.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Gestisce g = new Gestisce();
                g.setId_medico(rs.getInt("id_medico"));
                g.setId_ricovero(rs.getInt("id_ricovero"););
                collegamenti.add(g);
            }
        }
        return collegamenti;
    }

    @Override
    public List<Object[]> getCollegamentiOpera() throws SQLException {
        List<Object[]> collegamenti = new ArrayList<>();
        String sql = "SELECT id_medico, id_interventi, ruolo FROM Opera";
        try (PreparedStatement ps = connection.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Object[] tripla = new Object[3];
                tripla[0] = rs.getInt("id_medico");
                tripla[1] = rs.getInt("id_interventi");
                tripla[2] = rs.getString("ruolo");
                collegamenti.add(tripla);
            }
        }
        return collegamenti;
    }

    // Il medico di base non legge dati globali o amministrativi non di sua competenza
    @Override public List<Reparto> getReparti(int id) { return new ArrayList<>(); }
    @Override public List<Paziente> getPazienti(int id) { return new ArrayList<>(); }
    @Override public List<Stanza> getStanze(int id) { return new ArrayList<>(); }
    @Override public List<Ricovero> getRicoveri(int id) { return new ArrayList<>(); }
    @Override public List<InterventoChirurgico> getInterventi(int id) { return new ArrayList<>(); }
    @Override public List<Amministratore> getAmministratori(int id) { return new ArrayList<>(); }
    @Override public List<Letto> getLetti(int id) { return new ArrayList<>(); }
    @Override public List<Stanza> getStanzePerReparto(int id, Reparto r) { return new ArrayList<>(); }
}
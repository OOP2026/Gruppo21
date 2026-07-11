package implementazioneDao;

import dao.DAO;
import database_connection.ConnessioneDatabase;
import exceptions.BadArgsException;
import model.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MedicoImplementazioneDAO implements DAO {
    private final Connection connection;

    public MedicoImplementazioneDAO() throws SQLException {
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

    // ORA IL METODO IMPLEMENTA CORRETTAMENTE L'INTERFACCIA
    @Override
    public void istanziaDB(int idMedico) throws SQLException {
        // Qui non creiamo tabelle.
        // Il controller userà questo metodo per "inizializzare" le viste del medico
        System.out.println("Modulo Medico pronto per l'ID: " + idMedico);
    }

    @Override
    public List<Medico> getMedici(int ID_Medico) throws SQLException {
        List<Medico> listaMedici = new ArrayList<>();
        String sql = "SELECT * FROM Medico WHERE id_medico = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, ID_Medico);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    listaMedici.add(new Medico(
                            rs.getString("nome"), rs.getString("cognome"),
                            rs.getString("email"), rs.getString("password"),
                            rs.getString("tipo_medico"), null
                    ));
                }
            }
        } catch (Exception e) {
            System.err.println(e.getMessage() + Arrays.toString(e.getStackTrace()));
        }
        return listaMedici;
    }

    @Override
    public List<Visita> getVisite(int ID_Medico) throws SQLException {
        List<Visita> visite = new ArrayList<>();
        String sql = "SELECT * FROM Visita WHERE id_medico = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, ID_Medico);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    visite.add(new Visita(rs.getString("nome_visita"),
                            new Ricovero(rs.getInt("id_ricovero")),
                            new Medico(ID_Medico)));
                }
            }
        } catch (BadArgsException e) {
            System.err.println(e.getMessage() + Arrays.toString(e.getStackTrace()));
        }
        return visite;
    }

    @Override
    public List<Paziente> getPazienti(int ID_Medico) throws SQLException {
        List<Paziente> listaPazienti = new ArrayList<>();
        String sql = "SELECT DISTINCT p.* FROM Paziente p " +
                "INNER JOIN Ricovero r ON p.cod_fiscale = r.cod_fiscale_paziente " +
                "INNER JOIN Visita v ON r.id_ricovero = v.id_ricovero " +
                "WHERE v.id_medico = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, ID_Medico);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    listaPazienti.add(new Paziente(
                            rs.getString("nome"), rs.getString("cognome"), rs.getString("cod_fiscale")
                    ));
                }
            }
        } catch (BadArgsException e) {
            System.err.println(e.getMessage() + Arrays.toString(e.getStackTrace()));
        }
        return listaPazienti;
    }

    @Override
    public List<Reparto> getReparti(int ID_Medico) throws SQLException {
        List<Reparto> reparti = new ArrayList<>();
        String sql = "SELECT r.* FROM Reparto r INNER JOIN Medico m ON r.id_reparto = m.id_reparto WHERE m.id_medico = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, ID_Medico);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    reparti.add(new Reparto(rs.getString("nome_reparto"), rs.getInt("id_reparto")));
                }
            }
        } catch (BadArgsException e) {
            System.err.println(e.getMessage() + Arrays.toString(e.getStackTrace()));
        }
        return reparti;
    }

    @Override
    public List<Stanza> getStanze(int ID_Medico) throws SQLException {
        List<Stanza> stanze = new ArrayList<>();
        String sql = "SELECT s.* FROM Stanza s INNER JOIN Medico m ON s.id_reparto = m.id_reparto WHERE m.id_medico = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, ID_Medico);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    stanze.add(new Stanza(new Reparto(null, rs.getInt("id_reparto"))));
                }
            }
        } catch (BadArgsException e) {
            System.err.println(e.getMessage() + Arrays.toString(e.getStackTrace()));
        }
        return stanze;
    }

    @Override
    public List<TurnoLavorativo> getTurniLavorativi(int ID_Medico) throws SQLException {
        List<TurnoLavorativo> turni = new ArrayList<>();
        String sql = "SELECT * FROM Turno_Lavorativo WHERE id_medico = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, ID_Medico);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    turni.add(new TurnoLavorativo(
                            rs.getObject("data_ora_inizio", LocalDateTime.class),
                            rs.getObject("data_ora_fine", LocalDateTime.class)
                    ));
                }
            }
        } catch (BadArgsException e) {
            System.err.println(e.getMessage() + Arrays.toString(e.getStackTrace()));
        }
        return turni;
    }

    @Override public List<Amministratore> getAmministratori(int id) { return new ArrayList<>(); }
    @Override public List<Letto> getLetti(int id) { return new ArrayList<>(); }
    @Override public List<Stanza> getStanzePerReparto(int id, Reparto r) { return new ArrayList<>(); }
}
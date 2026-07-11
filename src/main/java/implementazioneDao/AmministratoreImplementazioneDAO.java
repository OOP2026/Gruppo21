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

public class AmministratoreImplementazioneDAO implements DAO {
    private final Connection connection;

    public AmministratoreImplementazioneDAO() throws SQLException {
        connection = ConnessioneDatabase.getConnection();
    }

    @Override
    public Boolean verificaCredenziali(String email, String password) throws SQLException {
        String sql = "SELECT 1 FROM amministratore WHERE email = ? AND password = ?";

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
        List<Medico> listaMedici = new ArrayList<>();
        String sql = "SELECT * FROM medico";

        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                String nome = rs.getString("nome");
                String cognome = rs.getString("cognome");
                String cf = rs.getString("codice_fiscale");

                listaMedici.add(new Medico(nome, cognome, cf));
            }
        } catch (BadArgsException e) {
            System.err.println(e.getMessage() + Arrays.toString(e.getStackTrace()));
        }
        return listaMedici;
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
        List<Stanza> stanze = new ArrayList<>();
        // Usiamo la chiave esterna id_reparto per filtrare la ricerca
        String sql = "SELECT * FROM stanza WHERE id_reparto = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            // Estraiamo l'ID dall'oggetto Java e lo passiamo al database
            ps.setInt(1, reparto.getId());

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String codiceStanza = rs.getString("codice");

                    // Creiamo la Stanza in memoria collegandola subito al suo Reparto
                    Stanza s = new Stanza(codiceStanza, reparto);
                    stanze.add(s);
                }
            }
        }
        return stanze;
    }

    @Override
    public List<Paziente> getPazienti(int ID_Medico) throws SQLException {
        List<Paziente> listaPazienti = new ArrayList<>();
        String sql = "SELECT * FROM paziente";

        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                String nome = rs.getString("nome");
                String cognome = rs.getString("cognome");
                String cf = rs.getString("codice_fiscale");

                listaPazienti.add(new Paziente(nome, cognome, cf));
            }
        } catch (BadArgsException e) {
            System.err.println(e.getMessage() + Arrays.toString(e.getStackTrace()));
        }
        return listaPazienti;
    }

    @Override
    public List<TurnoLavorativo> getTurniLavorativi(int ID_Medico) throws SQLException {
        List<TurnoLavorativo> turniLavorativi = new ArrayList<>();
        String sql = "SELECT * FROM Turno_Lavorativo";

        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                LocalDateTime dataInizio = rs.getObject("data_inizio", LocalDateTime.class);
                LocalDateTime dataFine = rs.getObject("data_fine", LocalDateTime.class);

                turniLavorativi.add(new TurnoLavorativo(dataInizio, dataFine));
            }
        } catch (BadArgsException e) {
            System.err.println(e.getMessage() + Arrays.toString(e.getStackTrace()));
        }
        return turniLavorativi;
    }

    @Override
    public List<Reparto> getReparti(int ID_Medico) throws SQLException {
        List<Reparto> reparti = new ArrayList<>();

        String sql = "SELECT * FROM Reparto";

        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                String Nome = rs.getString("nome_reparto");
                int id = rs.getInt("id_reparto");

                reparti.add(new Reparto(Nome, id));
            }
        } catch (BadArgsException e) {
            System.err.println(e.getMessage() + Arrays.toString(e.getStackTrace()));
        }

        return reparti;
    }

    @Override
    public List<Visita> getVisite(int ID_Medico) throws SQLException {
        return List.of();
    }
}
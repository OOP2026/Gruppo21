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
    public void istanziaDB() throws SQLException {
        String createAmministratore = "CREATE TABLE IF NOT EXISTS Amministratore (id_amministratore SERIAL PRIMARY KEY, nome VARCHAR(50), cognome VARCHAR(50), email VARCHAR(100) UNIQUE, password VARCHAR(100));";
        String createReparto = "CREATE TABLE IF NOT EXISTS Reparto (id_reparto SERIAL PRIMARY KEY, nome_reparto VARCHAR(50));";
        String createPaziente = "CREATE TABLE IF NOT EXISTS Paziente (cod_fiscale VARCHAR(16) PRIMARY KEY, nome VARCHAR(50), cognome VARCHAR(50), id_amministratore INT REFERENCES Amministratore(id_amministratore));";
        String createStanza = "CREATE TABLE IF NOT EXISTS Stanza (id_stanza SERIAL PRIMARY KEY, id_reparto INT REFERENCES Reparto(id_reparto));";
        String createLetto = "CREATE TABLE IF NOT EXISTS Letto (id_letto SERIAL PRIMARY KEY, cod_letto VARCHAR(20) UNIQUE, disponibilita BOOLEAN, id_stanza INT REFERENCES Stanza(id_stanza));";

        String insertAdmin = "INSERT INTO Amministratore (nome, cognome, email, password) VALUES ('Admin', 'Principale', 'admin@ospedale.it', 'admin123') ON CONFLICT (email) DO NOTHING;";

        try (PreparedStatement ps1 = connection.prepareStatement(createAmministratore);
             PreparedStatement ps2 = connection.prepareStatement(createReparto);
             PreparedStatement ps3 = connection.prepareStatement(createPaziente);
             PreparedStatement ps4 = connection.prepareStatement(createStanza);
             PreparedStatement ps5 = connection.prepareStatement(createLetto);
             PreparedStatement ps6 = connection.prepareStatement(insertAdmin)) {

            ps1.executeUpdate();
            ps2.executeUpdate();
            ps3.executeUpdate();
            ps4.executeUpdate();
            ps5.executeUpdate();
            ps6.executeUpdate();

            System.out.println("Tabelle strutturali dell'Amministratore create e verificate.");
        }
    }

    @Override
    public List<Medico> getMedici(int ID_Medico) throws SQLException {
        List<Medico> listaMedici = new ArrayList<>();
        String sql = "SELECT * FROM Medico";

        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                String nome = rs.getString("nome");
                String cognome = rs.getString("cognome");
                String email = rs.getString("email");
                String password = rs.getString("password");
                String tipoMedico = rs.getString("tipo_medico");

                listaMedici.add(new Medico(nome, cognome, email, password, tipoMedico, null));
            }
        } catch (Exception e) {
            System.err.println(e.getMessage() + Arrays.toString(e.getStackTrace()));
        }
        return listaMedici;
    }

    @Override
    public List<Amministratore> getAmministratori(int ID_Medico) throws SQLException {
        return new ArrayList<>();
    }

    @Override
    public List<Letto> getLetti(int ID_Medico) throws SQLException {
        return new ArrayList<>();
    }

    @Override
    public List<Stanza> getStanze(int ID_Medico) throws SQLException {
        return new ArrayList<>();
    }

    @Override
    public List<Stanza> getStanzePerReparto(int ID_Medico, Reparto reparto) throws SQLException {
        List<Stanza> stanze = new ArrayList<>();
        String sql = "SELECT * FROM Stanza WHERE id_reparto = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, reparto.getId());

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Stanza s = new Stanza(reparto);
                    stanze.add(s);
                }
            }
        } catch (BadArgsException e) {
            System.err.println(e.getMessage() + Arrays.toString(e.getStackTrace()));
        }
        return stanze;
    }

    @Override
    public List<Paziente> getPazienti(int ID_Medico) throws SQLException {
        List<Paziente> listaPazienti = new ArrayList<>();
        String sql = "SELECT * FROM Paziente";

        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                String nome = rs.getString("nome");
                String cognome = rs.getString("cognome");
                String cf = rs.getString("cod_fiscale");

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
                LocalDateTime dataInizio = rs.getObject("data_ora_inizio", LocalDateTime.class);
                LocalDateTime dataFine = rs.getObject("data_ora_fine", LocalDateTime.class);

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
                String nome = rs.getString("nome_reparto");
                int id = rs.getInt("id_reparto");

                reparti.add(new Reparto(nome, id));
            }
        } catch (BadArgsException e) {
            System.err.println(e.getMessage() + Arrays.toString(e.getStackTrace()));
        }
        return reparti;
    }

    @Override
    public List<Visita> getVisite(int ID_Medico) throws SQLException {
        return new ArrayList<>();
    }
}
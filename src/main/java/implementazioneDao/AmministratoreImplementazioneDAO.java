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
        // Stateless: Il DAO non memorizza nulla. Il Controller chiamerà i singoli metodi.
    }

    @Override
    public Boolean verificaCredenziali(String email, String password) throws SQLException {
        String sql = "SELECT 1 FROM Amministratore WHERE email = ? AND password = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, email); ps.setString(2, password);
            try (ResultSet rs = ps.executeQuery()) { return rs.next(); }
        }
    }

    @Override
    public List<Reparto> getReparti(int id) throws SQLException {
        List<Reparto> lista = new ArrayList<>();
        String sql = "SELECT * FROM Reparto";
        try (PreparedStatement ps = connection.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                try { lista.add(new Reparto(rs.getString("nome_reparto"), rs.getInt("id_reparto"))); }
                catch (BadArgsException e) {}
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
                try { lista.add(new Paziente(rs.getString("nome"), rs.getString("cognome"), rs.getString("cod_fiscale"))); }
                catch (BadArgsException e) {}
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
                    // Creiamo il Medico. La FK del reparto la inseriamo come guscio.
                    Reparto guscioReparto = new Reparto(rs.getInt("id_reparto"));
                    Medico m = new Medico(rs.getString("nome"), rs.getString("cognome"), rs.getString("email"), rs.getString("password"), rs.getString("tipo_medico"), guscioReparto);
                    m.setIdMedico(rs.getInt("id_medico"));
                    lista.add(m);
                } catch (BadArgsException e) {}
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
                try {
                    Reparto guscioReparto = new Reparto(rs.getInt("id_reparto"));
                    lista.add(new Stanza(guscioReparto));
                } catch (BadArgsException e) {}
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
                    TurnoLavorativo t = new TurnoLavorativo(rs.getObject("data_ora_inizio", LocalDateTime.class), rs.getObject("data_ora_fine", LocalDateTime.class));
                    // Nota: se il costruttore di Turno prende un Medico, passagli: new Medico(rs.getInt("id_medico"))
                    lista.add(t);
                } catch (BadArgsException e) {}
            }
        }
        return lista;
    }

    public List<Ricovero> getRicoveri(int id) throws SQLException {
        List<Ricovero> lista = new ArrayList<>();
        String sql = "SELECT * FROM Ricovero";
        try (PreparedStatement ps = connection.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Ricovero r = new Ricovero(rs.getInt("id_ricovero"));
                r.setDataOraInizio(rs.getObject("data_ora_inizio", LocalDateTime.class));
                r.setDataOraFine(rs.getObject("data_ora_fine", LocalDateTime.class));

                // Impostiamo i gusci per le Foreign Key
                r.setPaziente(new Paziente(rs.getString("cod_fiscale_paziente")));
                // r.setLetto(new Letto(rs.getInt("id_letto"))); // Se hai il guscio letto

                lista.add(r);
            }
        }
        return lista;
    }

    // Metodi di completamento
    @Override public List<Amministratore> getAmministratori(int id) { return new ArrayList<>(); }
    @Override public List<Letto> getLetti(int id) { return new ArrayList<>(); }
    @Override public List<Visita> getVisite(int id) { return new ArrayList<>(); }
    @Override public List<Stanza> getStanzePerReparto(int id, Reparto reparto) { return new ArrayList<>(); }
}
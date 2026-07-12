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
    public List<Reparto> getReparti(int id) throws SQLException {
        List<Reparto> lista = new ArrayList<>();
        String sql = "SELECT * FROM Reparto";
        try (PreparedStatement ps = connection.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                try {
                    lista.add(new Reparto(rs.getString("nome_reparto"), rs.getInt("id_reparto")));
                } catch (BadArgsException e) { System.err.println("Errore Reparto: " + e.getMessage()); }
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
                    lista.add(new Paziente(rs.getString("nome"), rs.getString("cognome"), rs.getString("cod_fiscale")));
                } catch (BadArgsException e) { System.err.println("Errore Paziente: " + e.getMessage()); }
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
                    // Creazione guscio per la FK 1:N (Ogni medico appartiene a UN reparto)
                    Reparto guscioReparto = new Reparto(rs.getInt("id_reparto"));

                    Medico m = new Medico(
                            rs.getString("nome"), rs.getString("cognome"),
                            rs.getString("email"), rs.getString("password"),
                            rs.getString("tipo_medico"), guscioReparto
                    );
                    m.setIdMedico(rs.getInt("id_medico"));
                    lista.add(m);
                } catch (BadArgsException e) { System.err.println("Errore Medico: " + e.getMessage()); }
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
                } catch (BadArgsException e) { System.err.println("Errore Stanza: " + e.getMessage()); }
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
                    TurnoLavorativo t = new TurnoLavorativo(
                            rs.getObject("data_ora_inizio", LocalDateTime.class),
                            rs.getObject("data_ora_fine", LocalDateTime.class)
                    );
                    t.setMedico(new Medico(rs.getInt("id_medico")));
                    lista.add(t);
                } catch (BadArgsException e) { System.err.println("Errore Turno: " + e.getMessage()); }
            }
        }
        return lista;
    }

    public List<Ricovero> getRicoveri(int id) throws SQLException {
        List<Ricovero> lista = new ArrayList<>();
        String sql = "SELECT * FROM Ricovero";
        try (PreparedStatement ps = connection.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                try {
                    Ricovero r = new Ricovero(rs.getInt("id_ricovero"));
                    r.setDataOraInizio(rs.getObject("data_ora_inizio", LocalDateTime.class));
                    r.setDataOraFine(rs.getObject("data_ora_fine", LocalDateTime.class));

                    r.setPaziente(new Paziente(rs.getString("cod_fiscale_paziente")));
                    r.setLetto(new Letto(rs.getInt("id_letto"))); // Assicurati di avere questo guscio in Letto

                    lista.add(r);
                } catch (Exception e) { System.err.println("Errore Ricovero: " + e.getMessage()); }
            }
        }
        return lista;
    }

    public List<InterventoChirurgico> getInterventi(int id) throws SQLException {
        List<InterventoChirurgico> lista = new ArrayList<>();
        String sql = "SELECT * FROM Intervento_Chirurgico";
        try (PreparedStatement ps = connection.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                try {
                    InterventoChirurgico i = new InterventoChirurgico(rs.getString("nome_intervento"));
                    i.setIdIntervento(rs.getInt("id_intervento"));

                    lista.add(i);
                } catch (Exception e) { System.err.println("Errore Intervento: " + e.getMessage()); }
            }
        }
        return lista;
    }

    public List<Gestisce> getCollegamentiGestisce() throws SQLException {
        List<Gestisce> collegamenti = new ArrayList<>();
        String sql = "SELECT id_medico, id_ricovero FROM Gestisce";

        try (PreparedStatement ps = connection.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Gestisce g = new Gestisce();
                g.setId_medico(rs.getInt("id_medico"));
                g.setId_ricovero(rs.getInt("id_ricovero"));
                collegamenti.add(g);
            }
        }
        return collegamenti;
    }

    // Tabella ponte: OPERA (Associazione tra Medico e Intervento con attributo aggiuntivo)
    public List<Opera> getCollegamentiOpera() throws SQLException {
        List<Opera> collegamenti = new ArrayList<>();
        String sql = "SELECT id_medico, id_intervento, ruolo FROM Opera";

        try (PreparedStatement ps = connection.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Opera opera = new Opera();
                opera.setId_medico(rs.getInt("id_medico"));       // Intero
                opera.setId_intervento(rs.getInt("id_intervento"));   // Intero
                opera.setRuolo(rs.getString("ruolo"));        // Stringa (es. "Primo Chirurgo")
                collegamenti.add(opera);
            }
        }
        return collegamenti;
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
                } catch (BadArgsException e) { System.err.println("Errore Amministratore: " + e.getMessage()); }
            }
        }
        return lista;
    }

    @Override
    public List<Letto> getLetti(int id) throws SQLException {
        List<Letto> lista = new ArrayList<>();
        String sql = "SELECT * FROM Letto";
        try (PreparedStatement ps = connection.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                try {
                    // Costruttore guscio per Letto, da adattare se necessario
                    lista.add(new Letto(rs.getInt("id_letto")));
                } catch (Exception e) {}
            }
        }
        return lista;
    }

    @Override
    public List<Visita> getVisite(int id) throws SQLException {
        List<Visita> lista = new ArrayList<>();
        String sql = "SELECT * FROM Visita";
        try (PreparedStatement ps = connection.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                try {
                    // Costruttore guscio o completo per Visita, da adattare se necessario
                    // lista.add(new Visita(...));
                } catch (Exception e) {}
            }
        }
        return lista;
    }

    @Override
    public List<Stanza> getStanzePerReparto(int id, Reparto reparto) throws SQLException {
        return new ArrayList<>();
    }
}
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
    public Boolean verificaCredenziali(String email, String password) throws SQLException {
        String sql = "SELECT 1 FROM Amministratore WHERE email = ? AND password = ?";
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            ps = connection.prepareStatement(sql);
            ps.setString(1, email);
            ps.setString(2, password);
            rs = ps.executeQuery();
            return rs.next();
        } finally {
            if (rs != null) {
                try { rs.close(); } catch (SQLException e) { System.err.println(e.getMessage()); }
            }
            if (ps != null) {
                try { ps.close(); } catch (SQLException e) { System.err.println(e.getMessage()); }
            }
        }
    }

    public List<Amministratore> getTuttiGliAmministratori() throws SQLException {
        List<Amministratore> lista = new ArrayList<>();
        String sql = "SELECT * FROM Amministratore";
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            ps = connection.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                try {
                    Amministratore a = new Amministratore(
                            rs.getString("nome"),
                            rs.getString("cognome"),
                            rs.getString("email"),
                            rs.getString("password")
                    );
                    a.setId(rs.getInt("id_amministratore"));
                    lista.add(a);
                } catch (BadArgsException e) {
                    System.err.println("Errore caricamento Amministratore: " + e.getMessage());
                }
            }
        } finally {
            if (rs != null) { try { rs.close(); } catch (SQLException e) {} }
            if (ps != null) { try { ps.close(); } catch (SQLException e) {} }
        }
        return lista;
    }

    public List<Reparto> getTuttiIReparti() throws SQLException {
        List<Reparto> lista = new ArrayList<>();
        String sql = "SELECT * FROM Reparto";
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            ps = connection.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                try {
                    Reparto r = new Reparto(rs.getString("nome_reparto"), rs.getInt("id_reparto"));
                    lista.add(r);
                } catch (Exception e) {
                    System.err.println("Errore caricamento Reparto: " + e.getMessage());
                }
            }
        } finally {
            if (rs != null) { try { rs.close(); } catch (SQLException e) {} }
            if (ps != null) { try { ps.close(); } catch (SQLException e) {} }
        }
        return lista;
    }

    public List<Paziente> getTuttiIPazienti() throws SQLException {
        List<Paziente> lista = new ArrayList<>();
        String sql = "SELECT * FROM Paziente";
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            ps = connection.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                try {
                    Paziente p = new Paziente(
                            rs.getString("nome"),
                            rs.getString("cognome"),
                            rs.getString("cod_fiscale")
                    );
                    lista.add(p);
                } catch (BadArgsException e) {
                    System.err.println("Errore caricamento Paziente: " + e.getMessage());
                }
            }
        } finally {
            if (rs != null) { try { rs.close(); } catch (SQLException e) {} }
            if (ps != null) { try { ps.close(); } catch (SQLException e) {} }
        }
        return lista;
    }

    public List<Medico> getTuttiIMedici() throws SQLException {
        List<Medico> lista = new ArrayList<>();
        String sql = "SELECT * FROM Medico";
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            ps = connection.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                try {
                    // Utilizziamo il costruttore "Guscio" per evitare BadArgsException passando oggetti null
                    Medico m = new Medico(rs.getInt("id_medico"));

                    // Se hai i setter per gli attributi anagrafici, impostali qui
                    // m.setNome(rs.getString("nome"));
                    // m.setCognome(rs.getString("cognome"));
                    // m.setEmail(rs.getString("email"));
                    // m.setPassword(rs.getString("password"));
                    // m.setTipoMedico(rs.getString("tipo_medico"));

                    // Salviamo le Foreign Key come primitivi
                    // m.setIdReparto(rs.getInt("id_reparto"));
                    // m.setIdAmministratore(rs.getInt("id_amministratore"));

                    lista.add(m);
                } catch (Exception e) {
                    System.err.println("Errore caricamento Medico: " + e.getMessage());
                }
            }
        } finally {
            if (rs != null) { try { rs.close(); } catch (SQLException e) {} }
            if (ps != null) { try { ps.close(); } catch (SQLException e) {} }
        }
        return lista;
    }

    public List<Stanza> getTutteLeStanze() throws SQLException {
        List<Stanza> lista = new ArrayList<>();
        String sql = "SELECT * FROM Stanza";
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            ps = connection.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                try {
                    // Presuppone l'esistenza di un costruttore vuoto o guscio in Stanza
                    // Stanza s = new Stanza();
                    // s.setIdStanza(rs.getInt("id_stanza"));
                    // s.setIdReparto(rs.getInt("id_reparto")); // FK
                    // lista.add(s);
                } catch (Exception e) {
                    System.err.println("Errore caricamento Stanza: " + e.getMessage());
                }
            }
        } finally {
            if (rs != null) { try { rs.close(); } catch (SQLException e) {} }
            if (ps != null) { try { ps.close(); } catch (SQLException e) {} }
        }
        return lista;
    }

    public List<Letto> getTuttiILetti() throws SQLException {
        List<Letto> lista = new ArrayList<>();
        String sql = "SELECT * FROM Letto";
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            ps = connection.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                try {
                    // Presuppone l'esistenza di un costruttore guscio
                    // Letto l = new Letto();
                    // l.setIdLetto(rs.getInt("id_letto"));
                    // l.setCodiceLetto(rs.getString("cod_letto"));
                    // l.setIdStanza(rs.getInt("id_stanza")); // FK
                    // lista.add(l);
                } catch (Exception e) {
                    System.err.println("Errore caricamento Letto: " + e.getMessage());
                }
            }
        } finally {
            if (rs != null) { try { rs.close(); } catch (SQLException e) {} }
            if (ps != null) { try { ps.close(); } catch (SQLException e) {} }
        }
        return lista;
    }

    public List<TurnoLavorativo> getTuttiITurniLavorativi() throws SQLException {
        List<TurnoLavorativo> lista = new ArrayList<>();
        String sql = "SELECT * FROM Turno_Lavorativo";
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            ps = connection.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                try {
                    TurnoLavorativo t = new TurnoLavorativo(
                            rs.getObject("data_ora_inizio", LocalDateTime.class),
                            rs.getObject("data_ora_fine", LocalDateTime.class)
                    );
                    t.setIdTurno(rs.getInt("id_turno"));
                    t.setIdMedico(rs.getInt("id_medico")); // FK (Come da Opzione 1 scelta)
                    lista.add(t);
                } catch (BadArgsException e) {
                    System.err.println("Errore caricamento Turno: " + e.getMessage());
                }
            }
        } finally {
            if (rs != null) { try { rs.close(); } catch (SQLException e) {} }
            if (ps != null) { try { ps.close(); } catch (SQLException e) {} }
        }
        return lista;
    }

    public List<Ricovero> getTuttiIRicoveri() throws SQLException {
        List<Ricovero> lista = new ArrayList<>();
        String sql = "SELECT * FROM Ricovero";
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            ps = connection.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                try {
                    // Costruttore guscio
                    Ricovero r = new Ricovero(rs.getInt("id_ricovero"));

                    // Aggiungi i setter nel Model se non presenti
                    // r.setDataOraInizio(rs.getObject("data_ora_inizio", LocalDateTime.class));
                    // r.setDataOraFine(rs.getObject("data_ora_fine", LocalDateTime.class));
                    // r.setCodFiscalePaziente(rs.getString("cod_fiscale_paziente")); // FK
                    // r.setIdLetto(rs.getInt("id_letto")); // FK

                    lista.add(r);
                } catch (Exception e) {
                    System.err.println("Errore caricamento Ricovero: " + e.getMessage());
                }
            }
        } finally {
            if (rs != null) { try { rs.close(); } catch (SQLException e) {} }
            if (ps != null) { try { ps.close(); } catch (SQLException e) {} }
        }
        return lista;
    }

    public List<Visita> getTutteLeVisite() throws SQLException {
        List<Visita> lista = new ArrayList<>();
        String sql = "SELECT * FROM Visita";
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            ps = connection.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                try {
                    // Assicurati di avere il costruttore vuoto o appropriato
                    // Visita v = new Visita(rs.getString("nome_visita"));
                    // v.setIdVisita(rs.getInt("id_visita"));
                    // v.setIdRicovero(rs.getInt("id_ricovero")); // FK
                    // v.setIdMedico(rs.getInt("id_medico")); // FK
                    // lista.add(v);
                } catch (Exception e) {
                    System.err.println("Errore caricamento Visita: " + e.getMessage());
                }
            }
        } finally {
            if (rs != null) { try { rs.close(); } catch (SQLException e) {} }
            if (ps != null) { try { ps.close(); } catch (SQLException e) {} }
        }
        return lista;
    }

    public List<InterventoChirurgico> getTuttiGliInterventiChirurgici() throws SQLException {
        List<InterventoChirurgico> lista = new ArrayList<>();
        String sql = "SELECT * FROM Intervento_Chirurgico";
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            ps = connection.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                try {
                    // Assicurati di avere il costruttore vuoto o appropriato
                    // InterventoChirurgico i = new InterventoChirurgico(rs.getString("nome_intervento"));
                    // i.setIdIntervento(rs.getInt("id_intervento"));
                    // i.setRuolo(rs.getString("ruolo"));
                    // i.setIdRicovero(rs.getInt("id_ricovero")); // FK
                    // i.setIdMedico(rs.getInt("id_medico")); // FK
                    // lista.add(i);
                } catch (Exception e) {
                    System.err.println("Errore caricamento Intervento: " + e.getMessage());
                }
            }
        } finally {
            if (rs != null) { try { rs.close(); } catch (SQLException e) {} }
            if (ps != null) { try { ps.close(); } catch (SQLException e) {} }
        }
        return lista;
    }

    @Override
    public void istanziaMemoriaLocale(int id) throws SQLException {
        System.out.println("Amministratore: inizializzazione dati completata.");
    }

    @Override
    public List<Medico> getMedici(int id) throws SQLException { return getTuttiIMedici(); }

    @Override
    public List<Amministratore> getAmministratori(int id) throws SQLException { return getTuttiGliAmministratori(); }

    @Override
    public List<Letto> getLetti(int id) throws SQLException { return getTuttiILetti(); }

    @Override
    public List<Stanza> getStanze(int id) throws SQLException { return getTutteLeStanze(); }

    @Override
    public List<Stanza> getStanzePerReparto(int id, Reparto reparto) throws SQLException { return new ArrayList<>(); }

    @Override
    public List<Paziente> getPazienti(int id) throws SQLException { return getTuttiIPazienti(); }

    @Override
    public List<TurnoLavorativo> getTurniLavorativi(int id) throws SQLException { return getTuttiITurniLavorativi(); }

    @Override
    public List<Reparto> getReparti(int id) throws SQLException, BadArgsException { return getTuttiIReparti(); }

    @Override
    public List<Visita> getVisite(int id) throws SQLException { return getTutteLeVisite(); }
}
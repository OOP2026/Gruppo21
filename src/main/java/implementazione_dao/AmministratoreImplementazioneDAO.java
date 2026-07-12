package implementazione_dao;

import dao.DAO;
import database_conn.ConnessioneDatabase;
import model.*;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static implementazione_dao.connection.connect;
import static implementazione_dao.connection.sqlVerificaCredenziali;

public class AmministratoreImplementazioneDAO implements DAO {
    private static Connection connection;

    private static final String ID_REPARTO_DB = "id_reparto";
    private static final String COGNOME_DB = "cognome";
    private static final String ID_MEDICO_DB = "id_medico";
    private static final String DATA_ORA_FINE_DB = "data_ora_fine";
    private static final String DATA_ORA_INIZIO_DB = "data_ora_inizio";
    private static final String ID_RICOVERO_DB = "id_ricovero";

    public AmministratoreImplementazioneDAO() {
        connection = connect();
    }

    @Override
    public Boolean verificaCredenziali(String email, String password) throws SQLException {
        return sqlVerificaCredenziali(connection, email, password, true);
    }

    @Override
    public List<Reparto> getReparti(int id) throws SQLException {
        List<Reparto> lista = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement("SELECT * FROM Reparto"); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) { try { lista.add(new Reparto(rs.getString("nome_reparto"), rs.getInt(ID_REPARTO_DB))); } catch (Exception e) {} }
        } return lista;
    }

    @Override
    public List<Paziente> getPazienti(int id) throws SQLException {
        List<Paziente> lista = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement("SELECT * FROM Paziente"); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) { try { lista.add(new Paziente(rs.getString("nome"), rs.getString(COGNOME_DB), rs.getString("cod_fiscale"))); } catch (Exception e) {} }
        } return lista;
    }

    @Override
    public List<Medico> getMedici(int id) throws SQLException {
        List<Medico> lista = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement("SELECT * FROM Medico"); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                try {
                    Medico m = new Medico(rs.getString("nome"), rs.getString(COGNOME_DB), rs.getString("email"), rs.getString("password"), rs.getString("tipo_medico"), new Reparto(rs.getInt(ID_REPARTO_DB)));
                    m.setIdMedico(rs.getInt(ID_MEDICO_DB)); lista.add(m);
                } catch (Exception e) {}
            }
        } return lista;
    }

    @Override
    public List<Stanza> getStanze(int id) throws SQLException {
        List<Stanza> lista = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement("SELECT * FROM Stanza"); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                try { Stanza s = new Stanza(new Reparto(rs.getInt(ID_REPARTO_DB))); s.setIdStanza(rs.getInt("id_stanza")); lista.add(s); } catch (Exception e) {}
            }
        } return lista;
    }

    @Override
    public List<Letto> getLetti(int id) throws SQLException {
        List<Letto> lista = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement("SELECT * FROM Letto"); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                try { Letto l = new Letto(rs.getInt("id_letto")); l.setStanza(new Stanza(rs.getInt("id_stanza"))); lista.add(l); } catch (Exception e) {}
            }
        } return lista;
    }

    @Override
    public List<TurnoLavorativo> getTurniLavorativi(int id) throws SQLException {
        List<TurnoLavorativo> lista = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement("SELECT * FROM Turno_Lavorativo"); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                try {
                    TurnoLavorativo t = new TurnoLavorativo(rs.getObject(DATA_ORA_INIZIO_DB, LocalDateTime.class), rs.getObject(DATA_ORA_FINE_DB, LocalDateTime.class));
                    t.setIdTurno(rs.getInt("id_turno")); t.setMedico(new Medico(rs.getInt(ID_MEDICO_DB))); lista.add(t);
                } catch (Exception e) {}
            }
        } return lista;
    }

    public List<Ricovero> getRicoveri(int id) throws SQLException {
        List<Ricovero> lista = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement("SELECT * FROM Ricovero"); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Ricovero r = new Ricovero(rs.getInt(ID_RICOVERO_DB));
                r.setDataOraInizio(rs.getObject(DATA_ORA_INIZIO_DB, LocalDateTime.class)); r.setDataOraFine(rs.getObject(DATA_ORA_FINE_DB, LocalDateTime.class));
                r.setPaziente(new Paziente(rs.getString("cod_fiscale_paziente"))); r.setLetto(new Letto(rs.getInt("id_letto"))); lista.add(r);
            }
        } return lista;
    }



    public List<InterventoChirurgico> getInterventi(int id) throws SQLException {
        List<InterventoChirurgico> lista = new ArrayList<>();
        String sql = "SELECT * FROM Intervento_Chirurgico";
        try (PreparedStatement ps = connection.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                try {

                    Visita v = new Visita(rs.getInt("id_visita"));

                    InterventoChirurgico i = new InterventoChirurgico(
                            rs.getString("nome_intervento"),
                            rs.getObject(DATA_ORA_INIZIO_DB, LocalDateTime.class),
                            rs.getObject(DATA_ORA_FINE_DB, LocalDateTime.class),
                            v
                    );
                    i.setIdIntervento(rs.getInt("id_intervento"));
                    lista.add(i);
                } catch (Exception e) { System.err.println("Errore Intervento: " + e.getMessage()); }
            }
        }
        return lista;
    }

    public void inserisciIntervento(InterventoChirurgico i) throws SQLException {
        String sql = "INSERT INTO Intervento_Chirurgico (nome_intervento, data_ora_inizio, data_ora_fine, id_visita) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, i.getNomeIntervento());
            ps.setObject(2, i.getDataOraInizio());
            ps.setObject(3, i.getDataOraFine());
            ps.setInt(4, i.getVisita().getIdVisita());
            ps.executeUpdate();
        }
    }

    public void aggiornaIntervento(InterventoChirurgico i) throws SQLException {
        String sql = "UPDATE Intervento_Chirurgico SET nome_intervento = ?, data_ora_inizio = ?, data_ora_fine = ?, id_visita = ? WHERE id_intervento = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, i.getNomeIntervento());
            ps.setObject(2, i.getDataOraInizio());
            ps.setObject(3, i.getDataOraFine());
            ps.setInt(4, i.getVisita().getIdVisita());
            ps.setInt(5, i.getIdIntervento());
            ps.executeUpdate();
        }
    }

    @Override
    public List<Visita> getVisite(int id) throws SQLException {
        List<Visita> lista = new ArrayList<>();
        String sql = "SELECT * FROM Visita";
        try (PreparedStatement ps = connection.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                try {

                    Visita v = new Visita(rs.getString("nome_visita"), new Ricovero(rs.getInt(ID_RICOVERO_DB)), new Medico(rs.getInt(ID_MEDICO_DB)));
                    v.setIdVisita(rs.getInt("id_visita"));
                    lista.add(v);
                } catch (Exception e) { System.err.println("Errore Visita: " + e.getMessage()); }
            }
        }
        return lista;
    }
    public void inserisciVisita(Visita v) throws SQLException {
        String sql = "INSERT INTO Visita (nome_visita, id_ricovero, id_medico) VALUES (?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, v.getNomeVisita());
            ps.setInt(2, v.getRicovero().getIdRicovero());
            ps.setInt(3, v.getMedico().getIdMedico());
            ps.executeUpdate();
        }
    }
    public void eliminaVisita(int id) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement("DELETE FROM Visita WHERE id_visita=?")) { ps.setInt(1, id); ps.executeUpdate(); }
    }

    @Override
    public List<Amministratore> getAmministratori(int id) throws SQLException {
        List<Amministratore> lista = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement("SELECT * FROM Amministratore"); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                try { Amministratore a = new Amministratore(rs.getString("nome"), rs.getString(COGNOME_DB), rs.getString("email"), rs.getString("password")); a.setId(rs.getInt("id_amministratore")); lista.add(a); } catch (Exception e) {}
            }
        } return lista;
    }


    public List<Gestisce> getCollegamentiGestisce() throws SQLException {
        List<Gestisce> list = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement("SELECT id_medico, id_ricovero FROM Gestisce"); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) { Gestisce g = new Gestisce(); g.setIdMedico(rs.getInt(ID_MEDICO_DB)); g.setIdRicovero(rs.getInt(ID_RICOVERO_DB)); list.add(g); }
        } return list;
    }

    public List<Opera> getCollegamentiOpera() throws SQLException {
        List<Opera> list = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement("SELECT id_medico, id_intervento, ruolo FROM Opera"); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) { Opera o = new Opera(); o.setIdMedico(rs.getInt(ID_MEDICO_DB)); o.setIdIntervento(rs.getInt("id_intervento")); o.setRuolo(rs.getString("ruolo")); list.add(o); }
        } return list;
    }

    // Paziente
    public void inserisciPaziente(Paziente p) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement("INSERT INTO Paziente (cod_fiscale, nome, cognome) VALUES (?, ?, ?)")) {
            ps.setString(1, p.getCodFiscale()); ps.setString(2, p.getNome()); ps.setString(3, p.getCognome()); ps.executeUpdate();
        }
    }
    public void aggiornaPaziente(Paziente p) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement("UPDATE Paziente SET nome=?, cognome=? WHERE cod_fiscale=?")) {
            ps.setString(1, p.getNome()); ps.setString(2, p.getCognome()); ps.setString(3, p.getCodFiscale()); ps.executeUpdate();
        }
    }
    public void eliminaPaziente(String cf) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement("DELETE FROM Paziente WHERE cod_fiscale=?")) { ps.setString(1, cf); ps.executeUpdate(); }
    }

    // Medico
    public void inserisciMedico(Medico m) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement("INSERT INTO Medico (nome, cognome, email, password, tipo_medico, id_reparto) VALUES (?,?,?,?,?,?)")) {
            ps.setString(1, m.getNome()); ps.setString(2, m.getCognome()); ps.setString(3, m.getEmail()); ps.setString(4, m.getPassword()); ps.setString(5, m.getTipoMedico()); ps.setInt(6, m.getReparto().getId()); ps.executeUpdate();
        }
    }
    public void aggiornaMedico(Medico m) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement("UPDATE Medico SET nome=?, cognome=?, email=?, password=?, tipo_medico=?, id_reparto=? WHERE id_medico=?")) {
            ps.setString(1, m.getNome()); ps.setString(2, m.getCognome()); ps.setString(3, m.getEmail()); ps.setString(4, m.getPassword()); ps.setString(5, m.getTipoMedico()); ps.setInt(6, m.getReparto().getId()); ps.setInt(7, m.getIdMedico()); ps.executeUpdate();
        }
    }
    public void eliminaMedico(int id) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement("DELETE FROM Medico WHERE id_medico=?")) { ps.setInt(1, id); ps.executeUpdate(); }
    }

    // Reparto
    public void inserisciReparto(Reparto r) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement("INSERT INTO Reparto (nome_reparto) VALUES (?)")) { ps.setString(1, r.getNome()); ps.executeUpdate(); }
    }
    public void aggiornaReparto(Reparto r) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement("UPDATE Reparto SET nome_reparto=? WHERE id_reparto=?")) { ps.setString(1, r.getNome()); ps.setInt(2, r.getId()); ps.executeUpdate(); }
    }
    public void eliminaReparto(int id) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement("DELETE FROM Reparto WHERE id_reparto=?")) { ps.setInt(1, id); ps.executeUpdate(); }
    }

    // Stanza
    public void inserisciStanza(Stanza s) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement("INSERT INTO Stanza (id_reparto) VALUES (?)")) { ps.setInt(1, s.getReparto().getId()); ps.executeUpdate(); }
    }
    public void aggiornaStanza(Stanza s) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement("UPDATE Stanza SET id_reparto=? WHERE id_stanza=?")) { ps.setInt(1, s.getReparto().getId()); ps.setInt(2, s.getIdStanza()); ps.executeUpdate(); }
    }
    public void eliminaStanza(int id) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement("DELETE FROM Stanza WHERE id_stanza=?")) { ps.setInt(1, id); ps.executeUpdate(); }
    }

    // Letto
    public void inserisciLetto(Letto l) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement("INSERT INTO Letto (id_stanza) VALUES (?)")) { ps.setInt(1, l.getStanza().getIdStanza()); ps.executeUpdate(); }
    }
    public void aggiornaLetto(Letto l) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement("UPDATE Letto SET id_stanza=? WHERE id_letto=?")) { ps.setInt(1, l.getStanza().getIdStanza()); ps.setInt(2, l.getIdLetto()); ps.executeUpdate(); }
    }
    public void eliminaLetto(int id) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement("DELETE FROM Letto WHERE id_letto=?")) { ps.setInt(1, id); ps.executeUpdate(); }
    }

    // Turno
    public void inserisciTurno(TurnoLavorativo t, int idMed) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement("INSERT INTO Turno_Lavorativo (data_ora_inizio, data_ora_fine, id_medico) VALUES (?,?,?)")) { ps.setObject(1, t.getDataOraInizio()); ps.setObject(2, t.getDataOraFine()); ps.setInt(3, idMed); ps.executeUpdate(); }
    }
    public void aggiornaTurno(TurnoLavorativo t, int idMed) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement("UPDATE Turno_Lavorativo SET data_ora_inizio=?, data_ora_fine=?, id_medico=? WHERE id_turno=?")) { ps.setObject(1, t.getDataOraInizio()); ps.setObject(2, t.getDataOraFine()); ps.setInt(3, idMed); ps.setInt(4, t.getIdTurno()); ps.executeUpdate(); }
    }
    public void eliminaTurno(int id) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement("DELETE FROM Turno_Lavorativo WHERE id_turno=?")) { ps.setInt(1, id); ps.executeUpdate(); }
    }

    // Ricovero
    public void inserisciRicovero(Ricovero r, String cf, int idL) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement("INSERT INTO Ricovero (data_ora_inizio, data_ora_fine, cod_fiscale_paziente, id_letto) VALUES (?,?,?,?)")) { ps.setObject(1, r.getDataOraInizio()); ps.setObject(2, r.getDataOraFine()); ps.setString(3, cf); ps.setInt(4, idL); ps.executeUpdate(); }
    }
    public void aggiornaRicovero(Ricovero r, String cf, int idL) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement("UPDATE Ricovero SET data_ora_inizio=?, data_ora_fine=?, cod_fiscale_paziente=?, id_letto=? WHERE id_ricovero=?")) { ps.setObject(1, r.getDataOraInizio()); ps.setObject(2, r.getDataOraFine()); ps.setString(3, cf); ps.setInt(4, idL); ps.setInt(5, r.getIdRicovero()); ps.executeUpdate(); }
    }
    public void eliminaRicovero(int id) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement("DELETE FROM Ricovero WHERE id_ricovero=?")) { ps.setInt(1, id); ps.executeUpdate(); }
    }

    // Intervento Chirurgico

    public void eliminaIntervento(int id) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement("DELETE FROM Intervento_Chirurgico WHERE id_intervento=?")) { ps.setInt(1, id); ps.executeUpdate(); }
    }

    // Amministratore
    public void inserisciAmministratore(Amministratore a) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement("INSERT INTO Amministratore (nome, cognome, email, password) VALUES (?,?,?,?)")) { ps.setString(1, a.getNome()); ps.setString(2, a.getCognome()); ps.setString(3, a.getEmail()); ps.setString(4, a.getPassword()); ps.executeUpdate(); }
    }
    public void aggiornaAmministratore(Amministratore a) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement("UPDATE Amministratore SET nome=?, cognome=?, email=?, password=? WHERE id_amministratore=?")) { ps.setString(1, a.getNome()); ps.setString(2, a.getCognome()); ps.setString(3, a.getEmail()); ps.setString(4, a.getPassword()); ps.setInt(5, a.getId()); ps.executeUpdate(); }
    }
    public void eliminaAmministratore(int id) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement("DELETE FROM Amministratore WHERE id_amministratore=?")) { ps.setInt(1, id); ps.executeUpdate(); }
    }


    public void collegaMedicoRicovero(int idMedico, int idRicovero) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement("INSERT INTO Gestisce (id_medico, id_ricovero) VALUES (?,?)")) { ps.setInt(1, idMedico); ps.setInt(2, idRicovero); ps.executeUpdate(); }
    }
    public void scollegaMedicoRicovero(int idMedico, int idRicovero) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement("DELETE FROM Gestisce WHERE id_medico=? AND id_ricovero=?")) { ps.setInt(1, idMedico); ps.setInt(2, idRicovero); ps.executeUpdate(); }
    }

    public void collegaMedicoIntervento(int idMedico, int idIntervento, String ruolo) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement("INSERT INTO Opera (id_medico, id_intervento, ruolo) VALUES (?,?,?)")) { ps.setInt(1, idMedico); ps.setInt(2, idIntervento); ps.setString(3, ruolo); ps.executeUpdate(); }
    }
    public void scollegaMedicoIntervento(int idMedico, int idIntervento) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement("DELETE FROM Opera WHERE id_medico=? AND id_intervento=?")) { ps.setInt(1, idMedico); ps.setInt(2, idIntervento); ps.executeUpdate(); }
    }
}
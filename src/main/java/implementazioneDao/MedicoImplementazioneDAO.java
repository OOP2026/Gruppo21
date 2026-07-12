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

    // Memoria locale del Medico loggato
    private List<Medico> mediciCorrenti = new ArrayList<>();
    private List<Visita> visiteCorrenti = new ArrayList<>();

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

    @Override
    public void istanziaMemoriaLocale(int idMedico) throws SQLException {
        // 1. Svuotiamo la memoria precedente (fondamentale per evitare duplicati se chiamato due volte)
        this.mediciCorrenti.clear();
        this.visiteCorrenti.clear();

        // 2. Carichiamo l'entità principale (Il Medico)
        String sqlMedico = "SELECT * FROM Medico WHERE id_medico = ?";
        try (PreparedStatement ps = connection.prepareStatement(sqlMedico)) {
            ps.setInt(1, idMedico);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    try {
                        // Creiamo il Medico con i dati reali del DB
                        Medico m = new Medico(
                                rs.getString("nome"), rs.getString("cognome"),
                                rs.getString("email"), rs.getString("password"),
                                rs.getString("tipo_medico"), null
                        );
                        m.setIdMedico(rs.getInt("id_medico"));

                        // 3. RIEMPIMENTO ARRAYLIST: Lanciamo la query per i Turni passando la FK
                        List<TurnoLavorativo> suoiTurni = fetchTurniPerMedico(idMedico);
                        // Assicurati di avere il metodo setTurniLavorativi(List<TurnoLavorativo> turni) nella classe Medico
                        m.setTurniLavorativi(suoiTurni);

                        this.mediciCorrenti.add(m);
                    } catch (BadArgsException e) {
                        System.err.println("Errore istanziazione Medico: " + e.getMessage());
                    }
                }
            }
        }

        // 4. Carichiamo le liste indipendenti (es. le visite fatte da questo medico)
        this.visiteCorrenti = fetchVisitePerMedico(idMedico);

        System.out.println("Memoria sincronizzata con successo per il Medico ID: " + idMedico);
    }

    // --- METODI PRIVATI PER LE QUERY TRAMITE FOREIGN KEY ---

    private List<TurnoLavorativo> fetchTurniPerMedico(int idMedicoFk) throws SQLException {
        List<TurnoLavorativo> lista = new ArrayList<>();
        // Query strettamente legata alla chiave esterna
        String sql = "SELECT * FROM Turno_Lavorativo WHERE id_medico = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, idMedicoFk);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    try {
                        lista.add(new TurnoLavorativo(
                                rs.getObject("data_ora_inizio", LocalDateTime.class),
                                rs.getObject("data_ora_fine", LocalDateTime.class)
                        ));
                    } catch (BadArgsException e) {
                        System.err.println("Dati Turno corrotti nel DB: " + e.getMessage());
                    }
                }
            }
        }
        return lista;
    }

    private List<Visita> fetchVisitePerMedico(int idMedicoFk) throws SQLException {
        List<Visita> lista = new ArrayList<>();
        // Query strettamente legata alla chiave esterna
        String sql = "SELECT * FROM Visita WHERE id_medico = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, idMedicoFk);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    try {
                        lista.add(new Visita(
                                rs.getString("nome_visita"),
                                new Ricovero(rs.getInt("id_ricovero")), // Guscio FK
                                new Medico(idMedicoFk) // Guscio FK
                        ));
                    } catch (BadArgsException e) {
                        System.err.println("Dati Visita corrotti nel DB: " + e.getMessage());
                    }
                }
            }
        }
        return lista;
    }

    // --- GETTER CHE RESTITUISCONO LA MEMORIA GIÀ COMPILATA AL CONTROLLER ---

    @Override
    public List<Medico> getMedici(int id) {
        return this.mediciCorrenti;
    }

    @Override
    public List<Visita> getVisite(int id) {
        return this.visiteCorrenti;
    }

    @Override
    public List<TurnoLavorativo> getTurniLavorativi(int id) {
        // Se il controller chiede i turni, li peschiamo direttamente dall'oggetto Medico caricato
        if (!this.mediciCorrenti.isEmpty()) {
            return this.mediciCorrenti.get(0).getTurniLavorativi();
        }
        return new ArrayList<>();
    }

    // Metodi negati per i permessi del Medico
    @Override public List<Amministratore> getAmministratori(int id) { return new ArrayList<>(); }
    @Override public List<Letto> getLetti(int id) { return new ArrayList<>(); }
    @Override public List<Stanza> getStanze(int id) { return new ArrayList<>(); }
    @Override public List<Stanza> getStanzePerReparto(int id, Reparto r) { return new ArrayList<>(); }
    @Override public List<Reparto> getReparti(int id) { return new ArrayList<>(); }
    @Override public List<Paziente> getPazienti(int id) { return new ArrayList<>(); }
}
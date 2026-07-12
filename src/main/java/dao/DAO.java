package dao;

import model.*;
import java.sql.SQLException;
import java.util.List;

public interface DAO {
    Boolean verificaCredenziali(String email, String password) throws SQLException;

    List<Reparto> getReparti(int id) throws SQLException;
    List<Paziente> getPazienti(int id) throws SQLException;
    List<Medico> getMedici(int id) throws SQLException;
    List<Stanza> getStanze(int id) throws SQLException;
    List<TurnoLavorativo> getTurniLavorativi(int id) throws SQLException;
    List<Ricovero> getRicoveri(int id) throws SQLException;
    List<InterventoChirurgico> getInterventi(int id) throws SQLException;
    List<Amministratore> getAmministratori(int id) throws SQLException;
    List<Letto> getLetti(int id) throws SQLException;
    List<Visita> getVisite(int id) throws SQLException;

    // Metodi per le tabelle ponte N:M
    List<Gestisce> getCollegamentiGestisce() throws SQLException;
    List<Opera> getCollegamentiOpera() throws SQLException;
}
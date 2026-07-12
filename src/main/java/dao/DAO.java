package dao;

import exceptions.BadArgsException;
import model.*;
import java.sql.SQLException;
import java.util.List;

public interface DAO {
    // Metodo per il login
    public Boolean verificaCredenziali(String email, String password) throws SQLException;

    public List<Medico> getMedici(int id) throws SQLException;
    public List<Amministratore> getAmministratori(int id) throws SQLException;
    public List<Letto> getLetti(int id) throws SQLException;
    public List<Stanza> getStanze(int id) throws SQLException;
    public List<Stanza> getStanzePerReparto(int id, Reparto reparto) throws SQLException;
    public List<Paziente> getPazienti(int id) throws SQLException;
    public List<TurnoLavorativo> getTurniLavorativi(int id) throws SQLException;
    public List<Reparto> getReparti(int id) throws SQLException;
    public List<Visita> getVisite(int id) throws SQLException;
    public List<Ricovero> getRicoveri(int i) throws SQLException;
}
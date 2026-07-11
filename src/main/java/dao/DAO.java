package dao;

import model.*;

import java.sql.SQLException;
import java.util.List;

public interface DAO {
    //Boolean tipo = 0 per medico, 1 per admin

    public Boolean verificaCredenziali(String email, String password) throws SQLException;

    public List<Medico> getMedici(int ID_Medico) throws SQLException;
    public List<Amministratore> getAmministratori(int ID_Medico) throws SQLException;
    public List<Letto> getLetti(int ID_Medico) throws SQLException;
    public List<Stanza> getStanze(int ID_Medico) throws SQLException;
    public List<Stanza> getStanzePerReparto(int ID_Medico, Reparto reparto) throws SQLException;
    public List<Paziente> getPazienti(int ID_Medico) throws SQLException;
    public List<TurnoLavorativo> getTurniLavorativi(int ID_Medico) throws SQLException;
    public List<Reparto> getReparti(int ID_Medico) throws SQLException;
    public List<Visita> getVisite(int ID_Medico) throws SQLException;
}

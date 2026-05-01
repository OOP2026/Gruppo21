
import model.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class MainTestOspedale {

    public static void main(String[] args) {

        Reparto reparto = new Reparto("Cardiologia", "Reparto per patologie cardiache");

        Stanza stanza = new Stanza("101", 1, reparto);

        Letto letto = new Letto("1", "DISPONIBILE", stanza);

        Medico medico = new Medico(
                "mverdi",
                "password",
                "M001",
                "Marco",
                "Verdi",
                "Cardiologo",
                reparto
        );

        Paziente paziente = new Paziente(
                "RSSMRA90A01H501X",
                "Mario",
                "Rossi",
                LocalDate.of(1990, 1, 1),
                "3331234567",
                "Via Roma 1"
        );

        Ricovero ricovero = new Ricovero(
                paziente,
                medico,
                LocalDateTime.of(2026, 4, 27, 8, 0),
                LocalDateTime.of(2026, 5, 3, 10, 0),
                "ATTIVO",
                "Ricovero ordinario"
        );

        AssegnazioneLetto assegnazione = new AssegnazioneLetto(
                ricovero,
                letto,
                LocalDateTime.of(2026, 4, 27, 8, 30),
                LocalDateTime.of(2026, 5, 3, 10, 0)
        );

        Turno turno = new Turno(
                medico,
                LocalDateTime.of(2026, 4, 27, 8, 0),
                LocalDateTime.of(2026, 4, 27, 14, 0)
        );

        TipoPrestazione tipo = new TipoPrestazione(
                "ECG",
                "Elettrocardiogramma"
        );

        Prestazione prestazione = new Prestazione(
                ricovero,
                medico,
                tipo,
                LocalDateTime.of(2026, 4, 27, 10, 0),
                LocalDateTime.of(2026, 4, 27, 10, 30),
                "ESEGUITA",
                "Esito nella norma",
                "Controllo cardiologico"
        );

        System.out.println(reparto);
        System.out.println(stanza);
        System.out.println(letto);
        System.out.println(medico);
        System.out.println(paziente);
        System.out.println(ricovero);
        System.out.println(assegnazione);
        System.out.println(turno);
        System.out.println(prestazione);
    }
}
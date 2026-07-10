package exceptions;

public class LogicaEccezione extends Exception {

    public LogicaEccezione() {
        super();
    }
    public LogicaEccezione(String message) {
        super(message);
    }
    public LogicaEccezione(String message, Throwable cause) {
        super(message, cause);
    }
    public LogicaEccezione(Throwable cause) {
        super(cause);
    }
}

package exceptions;

public class BadArgsException extends Exception {
    public BadArgsException() {
        super();
    }
    public BadArgsException(String message) {
        super(message);
    }
    public BadArgsException(String message, Throwable cause) {
        super(message, cause);
    }
    public BadArgsException(Throwable cause) {
        super(cause);
    }
}

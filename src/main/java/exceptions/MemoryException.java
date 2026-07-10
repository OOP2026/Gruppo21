package exceptions;

public class MemoryException extends Exception {
    public MemoryException() {}
    public MemoryException(String message) {
        super(message);
    }
    public MemoryException(String message, Throwable cause) {
        super(message, cause);
    }
    public MemoryException(Throwable cause) {
        super(cause);
    }
}

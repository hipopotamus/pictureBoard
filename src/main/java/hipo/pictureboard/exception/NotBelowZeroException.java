package hipo.pictureboard.exception;

public class NotBelowZeroException extends RuntimeException {

    public NotBelowZeroException() {
        super();
    }

    public NotBelowZeroException(String message) {
        super(message);
    }

    public NotBelowZeroException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotBelowZeroException(Throwable cause) {
        super(cause);
    }

    protected NotBelowZeroException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

package ch.lambdaj.function.closure;

/**
 * This Exception is thrown when trying to bindClosure and closure is not defined in current context
 * @author Ivo Smid
 */
public class UndefinedClosureException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    UndefinedClosureException(String message) {
        super(message);
    }

    UndefinedClosureException(String message, Throwable cause) {
        super(message, cause);
    }

}

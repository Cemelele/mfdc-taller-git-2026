package py.edu.uc.lp3.exception;

public abstract class EntidadException extends RuntimeException {
    protected EntidadException(String mensaje) {
        super(mensaje);
    }
}
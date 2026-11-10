package py.edu.uc.lp3.exception;

/**
 * Se lanza cuando el cuerpo de un pedido o una operacion intenta dejar una
 * entidad en un estado que el dominio no permite (vida no positiva, tipo
 * desconocido, velocidad negativa, etc.).
 */
public class EntidadInvalidaException extends EntidadException {

    public EntidadInvalidaException(String mensaje) {
        super(mensaje);
    }
}

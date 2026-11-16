package py.edu.uc.lp3.exception;

/**
 * Se lanza cuando una operacion es sintacticamente valida pero el estado
 * actual del recurso no permite realizarla, por ejemplo intentar revivir
 * una entidad muerta o explotar dos veces un Creeper que ya detono.
 * El manejador global la traduce a HTTP 409 Conflict.
 */
public class EstadoInvalidoException extends HostilDomainException {

    public EstadoInvalidoException(String mensaje) {
        super(mensaje);
    }
}

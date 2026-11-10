package py.edu.uc.lp3.exception;

/**
 * Excepcion base de la familia Hostil. Todas las reglas de negocio del
 * dominio lanzan una hija de esta clase para que la capa HTTP las pueda
 * traducir a un codigo de estado y un JSON uniforme.
 */
public class HostilDomainException extends RuntimeException {

    public HostilDomainException() {
        super();
    }

    public HostilDomainException(String mensaje) {
        super(mensaje);
    }

    public HostilDomainException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}

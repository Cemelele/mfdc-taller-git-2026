package py.edu.uc.lp3.exception;

/**
 * El companero no respondio: no hay conexion, la conexion se corto o se
 * agoto el tiempo de espera.
 */
public class CompaneroNoDisponibleException extends HostilDomainException {

    private final String origen;

    public CompaneroNoDisponibleException(String origen) {
        super("El companero " + origen + " no esta disponible");
        this.origen = origen;
    }

    public CompaneroNoDisponibleException(String origen, Throwable causa) {
        super("El companero " + origen + " no esta disponible", causa);
        this.origen = origen;
    }

    public String getOrigen() {
        return origen;
    }
}

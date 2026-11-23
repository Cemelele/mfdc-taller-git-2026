package py.edu.uc.lp3.exception;

/**
 * Un companero respondio la simulacion con un 4xx. Conserva el origen, el
 * status y el error reportado por el companero para que la capa HTTP los
 * devuelva en el JSON de rechazo.
 */
public class CompaneroRechazoException extends HostilDomainException {

    private final String origen;
    private final int status;
    private final String companeroError;

    public CompaneroRechazoException(String origen, int status,
                                     String companeroError, String mensaje) {
        super(mensaje);
        this.origen = origen;
        this.status = status;
        this.companeroError = companeroError;
    }

    public String getOrigen() {
        return origen;
    }

    public int getStatus() {
        return status;
    }

    public String getCompaneroError() {
        return companeroError;
    }
}

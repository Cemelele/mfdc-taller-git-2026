package py.edu.uc.lp3.exception;

public class EntidadNoEncontradaException extends EntidadException {

    public EntidadNoEncontradaException(Long id) {
        this("No se encontro la entidad con ID: " + id);
    }

    public EntidadNoEncontradaException(String mensaje) {
        super(mensaje);
    }
}